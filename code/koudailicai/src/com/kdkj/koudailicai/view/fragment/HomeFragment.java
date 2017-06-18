package com.kdkj.koudailicai.view.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.HomePagerAdapter;
import com.kdkj.koudailicai.adapter.HomePagerAdapter.PlaceholderFragment;
import com.kdkj.koudailicai.domain.HomeProductInfo;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.ui.VerticalViewPager;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.KDLCApplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class HomeFragment extends BaseFragment implements ErrorListener,
		HttpErrorInterface {

	private FragmentActivity mActivity;
	private View mParent;
	private String LOG_TAG = HomeFragment.class.getName();
	private HomeFragmentInterface homeFragmentInterface;
	// views
	private VerticalViewPager viewPager;
	private List<View> viewLists = new ArrayList<View>();
	private int initIndex = 0;
	private int curIndex = 0;// 当前页卡编号
	private HomePageViewHolder viewHoler;
	// 动画时间
	private int showTime = 500;
	private int showStartTime = 0;
	private int showOffsetTime = 75;

	// 滑动高度
	private int curPage = 1;
	private Handler scroolHandler = new Handler();
	private int showBottomHeight;
	private Boolean isExit = false;
	private Boolean hasTask = false;

	// contents
	private static final float MIN_SCALE = 0.75f;
	private static final float MIN_ALPHA = 0.75f;
	private LayoutInflater mInflater;
	private String getHomeInfoUrl;
	private List<HomeProductInfo> productList = new ArrayList<HomeProductInfo>();
	private HomePagerAdapter pagerAdapter;
	private RelativeLayout network_disabled_layout;
	private TextView networkload;
	
	public static HomeFragment newInstance(int index) {
		HomeFragment f = new HomeFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);
		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			homeFragmentInterface = (HomeFragmentInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must HomeFragmentInterface");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mParent = getView();
		mInflater = LayoutInflater.from(mActivity);
		parseUrl();
		findViews();
		initPageView();
		initHomeInfo();
		
		
		// startAnimations();
		// setListeners();
		// bottomShowThread.start();
	}

	private void parseUrl() {
		if (KDLCApplication.app.isGlobalConfCompleted()) {
			getHomeInfoUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_INDEX);
		} else {
			getHomeInfoUrl = G.URL_GET_INDEX;
		}
	}

	private void findViews() {
		viewPager = (VerticalViewPager) mParent.findViewById(R.id.homePager);
		network_disabled_layout = (RelativeLayout) mParent
				.findViewById(R.id.network_disabled_layout);
		networkload = (TextView) mParent.findViewById(R.id.networkload);
		networkload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				LogUtil.info("点击刷新");
				network_disabled_layout.setVisibility(View.INVISIBLE);
				getHomeInfo();
			}
		});
	}

	/**
	 * 获取本地数据库中的产品信息
	 */
	private void initHomeInfo() {
		List<HomeProductInfo> dbInfoList = (List<HomeProductInfo>) KdlcDB.findAllByClass(HomeProductInfo.class);
		if (dbInfoList != null && dbInfoList.size() > 0) {
			Log.d(LOG_TAG, "get from db:" + dbInfoList.size());
			productList.addAll(dbInfoList);
			initPageList();
			initViewPager();
		}
	}

	private void getHomeInfo() {
		dialog = KdlcDialog.showProgressDialog(mActivity);
		sendHttpGet(getHomeInfoUrl, getHomeInfoListener, new BaseHttpErrorListener(getActivity(), this));
	}
	
	private Listener<JSONObject> getHomeInfoListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				if (dialog != null) {
					dialog.cancel();
				}
				Log.d(LOG_TAG, "response:" + response.toString());
				int retCode = response.getInt("code");
				if (retCode == 0) {
					network_disabled_layout.setVisibility(View.INVISIBLE);
					viewPager.setVisibility(View.VISIBLE);
					JSONArray homeDataList = response.getJSONArray("data");
					productList.clear();
					for (int i = 0; i < homeDataList.length(); i++) {
						JSONObject homeInfoObject = homeDataList
								.getJSONObject(i);
						HomeProductInfo homeProInfo = new HomeProductInfo(
								homeInfoObject.getInt("id"),
								homeInfoObject.getString("name"),
								homeInfoObject.getString("apr"),
								homeInfoObject.getString("total_money"),
								homeInfoObject.getString("success_money"),
								homeInfoObject.getString("success_percent"),
								homeInfoObject.getString("min_invest_money"),
								homeInfoObject.getString("words"),
								homeInfoObject.getString("is_novice"));
						productList.add(homeProInfo);
					}
					if(Tool.hasCacheData(HomeProductInfo.class))
						KdlcDB.deleteAllByClass(HomeProductInfo.class);
					KdlcDB.addByEntityList(productList);
					initPageList();
					initViewPager();
					viewPager.setCurrentItem(curIndex, true);
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
	
	

	/**
	 * 设置第一页
	 */
	private void initPageView() {
		viewHoler = new HomePageViewHolder();
		View pageView = mInflater.inflate(R.layout.home_first_page, null);
		viewHoler.pageInfoView = (RelativeLayout) pageView
				.findViewById(R.id.pageInfoView);
		viewHoler.productName = (TextView) pageView
				.findViewById(R.id.productName);
		viewHoler.aprView = (RelativeLayout) pageView
				.findViewById(R.id.aprView);
		viewHoler.aprText = (TextView) pageView.findViewById(R.id.aprText);
		viewHoler.circleView = (ImageView) pageView
				.findViewById(R.id.circleView);
		viewHoler.tipView = (LinearLayout) pageView.findViewById(R.id.tipView);
		viewHoler.investBtn = (TextView) pageView.findViewById(R.id.investBtn);
		viewHoler.safeTip = (LinearLayout) pageView.findViewById(R.id.safeTip);
		viewHoler.arrow = (ImageView) pageView
				.findViewById(R.id.arrow_homepage);
	}

	/**
	 * 初始化ViewList
	 */
	private void initPageList() {
		for (int i = 0; i < productList.size(); i++) {
			HomeProductInfo productInfo = productList.get(i);
			viewLists.add(getPageView(i, productInfo));
		}
	}

	/**
	 * 获取index页面
	 * 
	 * @param index
	 * @param productInfo
	 * @return
	 */
	private View getPageView(int index, HomeProductInfo productInfo) {
		View pageView;
		RelativeLayout pageLayout;
		switch (index) {
		case 0:
			pageView = mInflater.inflate(R.layout.home_first_page, null);
			pageLayout = (RelativeLayout) pageView
					.findViewById(R.id.pageInfoView);
			break;
		case 1:
			pageView = mInflater.inflate(R.layout.home_second_page, null);
			pageLayout = (RelativeLayout) pageView
					.findViewById(R.id.secondPage);
			break;
		case 2:
			pageView = mInflater.inflate(R.layout.home_third_page, null);
			pageLayout = (RelativeLayout) pageView.findViewById(R.id.thirdPage);
			break;
		default:
			pageView = mInflater.inflate(R.layout.home_first_page, null);
			pageLayout = (RelativeLayout) pageView
					.findViewById(R.id.pageInfoView);
			break;
		}
		return pageView;
	}

	private void initViewPager() {
		LogUtil.info("");
		//pagerAdapter = new HomePagerAdapter(getFragmentManager(), productList);
		pagerAdapter = new HomePagerAdapter(getChildFragmentManager(), productList);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(initIndex);
		viewPager.setOnPageChangeListener(new HomeOnPageChangeListener());
	}

	@Override
	public void onStart() {
		super.onStart();
		if (productList == null || productList.size() <= 0) {
			getHomeInfo();
		}
		
//		if("updated".equals(KDLCApplication.app.getSessionVal(G.HOME_INFO_UPDATED))){
//			LogUtil.info("数据库数据已经修改，需要更新");
//			viewLists.clear();
//			productList.clear();
//			initHomeInfo();
//			KDLCApplication.app.setSessionVal(G.HOME_INFO_UPDATED, "");
//		}
//		Log.d(LOG_TAG, "call onStart");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if("updated".equals(KDLCApplication.app.getSessionVal(G.HOME_INFO_UPDATED))){
			LogUtil.info("数据库数据已经修改，需要更新");
			//viewLists.clear();
			productList.clear();
			initHomeInfo();
			KDLCApplication.app.setSessionVal(G.HOME_INFO_UPDATED, "");
		}
		LogUtil.info("call onresume");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(LOG_TAG, "call onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		//停止更新主页内容
		Log.d(LOG_TAG, "call onStop");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, "call onDestroy");
	}

	public interface HomeFragmentInterface {
		public void showBottomMenu(boolean show);
	}

	public void setHomeFragmentInterface(
			HomeFragmentInterface homeFragmentInterface) {
		this.homeFragmentInterface = homeFragmentInterface;
	}

	public class HomeOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			curIndex = arg0;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

			if (arg1 > 0.20 && !"scrolled".equals(KDLCApplication.app
					.getSessionVal(G.HOMEPAGE_SCROLLED))) {
				LogUtil.info("页面编号=" + arg0 + "滑动比例=" + arg1 + "滑动像素=" + arg2);
				PlaceholderFragment fragment = (PlaceholderFragment) pagerAdapter
						.getItem(0);
				Message msg = fragment.handler.obtainMessage();
				msg.arg1 = 100;
				msg.sendToTarget();
			}

		}
	}

	public class HomePageViewHolder {
		private RelativeLayout pageInfoView;
		private TextView productName;
		private TextView fakeTitle;
		private RelativeLayout aprView;
		private TextView aprText;
		private ImageView circleView;
		private LinearLayout tipView;
		private TextView investBtn;
		private LinearLayout safeTip;
		private ImageView arrow;
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO Auto-generated method stub
		if (dialog != null) {
			dialog.cancel();
		}
		if (null == productList||productList.size()==0) {
			this.network_disabled_layout.setVisibility(View.VISIBLE);
			viewPager.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void showNoNetwork() {
		// TODO Auto-generated method stub
		if (dialog != null) {
			dialog.cancel();
		}
		if (null == productList||productList.size()==0) {
			this.network_disabled_layout.setVisibility(View.VISIBLE);
			viewPager.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void showErrReq() {
		// TODO Auto-generated method stub
		if (dialog != null) {
			dialog.cancel();
		}
		if (null == productList||productList.size()==0) {
			this.network_disabled_layout.setVisibility(View.VISIBLE);
			viewPager.setVisibility(View.INVISIBLE);
		}
	}

	
}
