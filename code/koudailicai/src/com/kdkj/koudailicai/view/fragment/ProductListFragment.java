package com.kdkj.koudailicai.view.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.ProductListP2PInfoAdapter;
import com.kdkj.koudailicai.adapter.ProductListPagerAdapter;
import com.kdkj.koudailicai.adapter.ProductListCessionInfoAdapter;
import com.kdkj.koudailicai.adapter.ProductListTrustInfoAdapter;
import com.kdkj.koudailicai.domain.CessionTradeInfo;
import com.kdkj.koudailicai.domain.ProductListP2PInfo;
import com.kdkj.koudailicai.domain.ProductListCessionInfo;
import com.kdkj.koudailicai.domain.ProductListTrustInfo;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.lib.http.MyHttpClient;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.more.FeedBackActivity;
import com.kdkj.koudailicai.view.product.KdbDetailActivity;
import com.kdkj.koudailicai.view.product.ProductDetailActivity;
import com.kdkj.koudailicai.view.selfcenter.TransactionRecordActivity;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
/**
 * 产品列表
 * 
 * 
 */
public class ProductListFragment extends BaseFragment {
	private static String LOG_TAG = ProductListFragment.class.getName();
	private FragmentActivity mActivity;
	private View mParent;
	private TitleView mTitle;
	private RelativeLayout navView;
	private ViewPager mViewPager;
	private List<View> viewLists = new ArrayList<View>();
	private PullToRefreshListView p2pListView;
	private ProductListP2PInfoAdapter p2pAdapter;
	private PullToRefreshListView trustListView;
	private ProductListTrustInfoAdapter trustAdapter;
	private PullToRefreshListView cessionListView;
	private ProductListCessionInfoAdapter cessionAdapter;
	private TextView p2pLabel, trustLabel, cessionLabel;
	private ImageView imageView;
	
	private int screenHeight;
	private int screenWidth;
	private float bmWidth=0;
	private float offSet=0;
	private int initIndex = 0;
	private int curIndex = 0;// 当前页卡编号
	
	private String getP2PListUrl, getTrustListUrl, getCessionListUrl;
	private List<ProductListP2PInfo> p2pInfoList = new ArrayList<ProductListP2PInfo>();
	private List<ProductListTrustInfo> trustInfoList = new ArrayList<ProductListTrustInfo>();
	private List<ProductListCessionInfo> transferZoneList = new ArrayList<ProductListCessionInfo>();

	private int p2pCurPage = 1;
	private int p2pPageSize = 15;
	private boolean p2pAutoLoad = true;
	private boolean p2pHasMore = true;
	private Mode p2pCurMode = Mode.PULL_FROM_START;
	
	private int trustListCurrentPage = 1;
	private int trustListPageSize = 15;
	private boolean trustListAutoLoad = true;
	private boolean trustListHasMore = true;
	private Mode trustListCurrentMode = Mode.PULL_FROM_START;
	
	private int cessionPage = 1;
	private int cessionPageSize = 15;
	private boolean cessionAutoLoad = true;
	private boolean cessionHasMore = true;
	private Mode cessionCurMode = Mode.PULL_FROM_START;
	
	private Handler mHandler = new Handler();
	
	//网络出错页面
	private RelativeLayout errNetLayoutP2P;
	private RelativeLayout noDataLayoutP2P;
	private TextView networkLoadP2P;
	private TextView networkTextP2P;
	private TextView noDataTextP2P;
	//网络出错页面
	private RelativeLayout errNetLayoutTrust;
	private RelativeLayout noDataLayoutTrust;
	private TextView networkLoadTrust;
	private TextView networkTextTrust;
	private TextView noDataTextTrust;

	//网络出错页面
	private RelativeLayout errNetLayoutCession;
	private RelativeLayout noDataLayoutCession;
	private TextView networkLoadCession;
	private TextView networkTextCession;
	private TextView noDataTextCession;

	
	public static ProductListFragment newInstance(int index) {
		ProductListFragment f = new ProductListFragment();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_product_list, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(LOG_TAG, "call onActivityCreated");
		mActivity = getActivity();
		mParent = getView();
		parseUrl();
		initTitle();
		initCursor();
		initListView();
		initViewPager();
		initContents();
	}
	
	private void parseUrl() {
		if (KDLCApplication.app.isGlobalConfCompleted()) {
			getP2PListUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_PODUCT_P2P_LIST);
			getTrustListUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_PODUCT_TRUST_LIST); 
			getCessionListUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_PODUCT_CESSION_LIST);
		} else {
			getP2PListUrl = G.URL_GET_PODUCT_P2P_LIST;
			getTrustListUrl = G.URL_GET_PODUCT_TRUST_LIST; 
			getCessionListUrl =  G.URL_GET_PODUCT_CESSION_LIST;
		}
	}
	
	private void initListView() {
		LayoutInflater mInflater = LayoutInflater.from(mActivity);
		//设置P2P产品列表
		View p2pView = mInflater.inflate(R.layout.p2p_list_view, null);
		p2pListView = (PullToRefreshListView) p2pView.findViewById(R.id.p2pListView);
		p2pAdapter = new ProductListP2PInfoAdapter(mActivity, R.layout.productlist_p2p_info, p2pInfoList);
		p2pListView.setAdapter(p2pAdapter);
		setP2PListViewListener();
		//设置P2P产品加载失败
		errNetLayoutP2P = (RelativeLayout) p2pView.findViewById(R.id.errNetLayout);
		noDataLayoutP2P = (RelativeLayout) p2pView.findViewById(R.id.noDataLayout);
		networkLoadP2P = (TextView) p2pView.findViewById(R.id.networkload);
		networkTextP2P = (TextView) p2pView.findViewById(R.id.networktext);
		noDataTextP2P = (TextView) p2pView.findViewById(R.id.nodataText);
		noDataTextP2P.setText("敬请期待");
		networkLoadP2P.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(LOG_TAG, "p2p no net click:"+p2pListView);
				showListView(0);
				Tool.resetPullRefreshView(p2pListView);
				mHandler.postDelayed(new Runnable() {
					public void run() {
						p2pListView.setLoadRefreshing();
					}
				}, 250);
				
			}
		});
		

		//设置信托产品列表
		View trustView = mInflater.inflate(R.layout.trust_list_view, null);
		trustListView = (PullToRefreshListView) trustView.findViewById(R.id.trustListView);
		trustAdapter = new ProductListTrustInfoAdapter(mActivity, R.layout.projecttrustlist,trustInfoList);
		trustListView.setAdapter(trustAdapter);
		setTrustListViewListener();
		//设置信托产品加载失败
		errNetLayoutTrust = (RelativeLayout) trustView.findViewById(R.id.errNetLayout);
		noDataLayoutTrust = (RelativeLayout) trustView.findViewById(R.id.noDataLayout);
		networkLoadTrust = (TextView) trustView.findViewById(R.id.networkload);
		networkTextTrust = (TextView) trustView.findViewById(R.id.networktext);
		noDataTextTrust = (TextView) trustView.findViewById(R.id.nodataText);
		noDataTextTrust.setText("敬请期待");
		networkLoadTrust.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView(1);
				Log.d(LOG_TAG, "trust no net click");
				mHandler.postDelayed(new Runnable() {
					public void run() {
						trustListView.setLoadRefreshing();
					}
				}, 250);
			
			}
		});
		//设置转让专区
		View transferZoneView = mInflater.inflate(R.layout.transfer_zone_list_view, null);
		cessionListView = (PullToRefreshListView)transferZoneView.findViewById(R.id.transferZoneListView);
		cessionAdapter = new ProductListCessionInfoAdapter(mActivity, R.layout.cession_list_product_info, transferZoneList);
		cessionListView.setAdapter(cessionAdapter);
		setCessionRefresh();
		//设置转让专区加载失败
		errNetLayoutCession = (RelativeLayout) transferZoneView.findViewById(R.id.errNetLayout);
		noDataLayoutCession = (RelativeLayout) transferZoneView.findViewById(R.id.noDataLayout);
		networkLoadCession = (TextView) transferZoneView.findViewById(R.id.networkload);
		networkTextCession = (TextView) transferZoneView.findViewById(R.id.networktext);
		noDataTextCession = (TextView) transferZoneView.findViewById(R.id.nodataText);
		noDataTextCession.setText("敬请期待");
		networkLoadCession.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView(2);
				Log.d(LOG_TAG, "cession no net click");
				mHandler.postDelayed(new Runnable() {
					public void run() {
						cessionListView.setLoadRefreshing();
					}
				}, 250);
				
			}
		});

		//设置列表
		viewLists.add(p2pView); 
		viewLists.add(trustView);
		viewLists.add(transferZoneView);
	}
	
	private void initTitle() {
		mTitle = (TitleView) mParent.findViewById(R.id.title);
		mTitle.setTitle(R.string.product_list_title);	
	}
	
	private void initCursor() {
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		int navHeight = (int) (screenHeight*0.075);
		navView = (RelativeLayout) mParent.findViewById(R.id.nav);
		RelativeLayout.LayoutParams navParams = (RelativeLayout.LayoutParams) navView.getLayoutParams();
		navParams.height = navHeight;
		navView.setLayoutParams(navParams);
		p2pLabel = (TextView) mParent.findViewById(R.id.p2pLabel);
		trustLabel = (TextView) mParent.findViewById(R.id.trustLabel);
		cessionLabel = (TextView) mParent.findViewById(R.id.cessionLabel);
		imageView = (ImageView) mParent.findViewById(R.id.cursor);
		p2pLabel.setOnClickListener(new LabelOnClickListener(0));
		trustLabel.setOnClickListener(new LabelOnClickListener(1));
		cessionLabel.setOnClickListener(new LabelOnClickListener(2));
		bmWidth = BitmapFactory.decodeResource(getResources(), R.drawable.redbar).getWidth();// 获取图片宽度
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		offSet = (screenWidth / 3 - bmWidth) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate((float) (offSet), 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}
	
	
	
	private void initViewPager() {
		mViewPager = (ViewPager) mParent.findViewById(R.id.viewPager);
		mViewPager.setAdapter(new ProductListPagerAdapter(viewLists));
		mViewPager.setCurrentItem(initIndex);
		mViewPager.setOnPageChangeListener(new ProductListOnPageChangeListener());
	}
	
	private void initContents() {
		//p2p产品
		List<ProductListP2PInfo> p2pInfoDBList = (List<ProductListP2PInfo>) KdlcDB.findAllByClass(ProductListP2PInfo.class);
		if(p2pInfoDBList != null && p2pInfoDBList.size() != 0) {
			Log.d(LOG_TAG, "db size:"+p2pInfoDBList.size());
			p2pInfoList.addAll(p2pInfoDBList);
		} else {
			p2pAutoLoad = true;
			sendHttpGet(getP2PListUrl+"?page="+p2pCurPage+"&pageSize="+p2pPageSize, getP2PListListener, new productListErrorListener(0));
		}
		//信托
		List<ProductListTrustInfo> trustInfoDBList =  (List<ProductListTrustInfo>) KdlcDB.findAllByClass(ProductListTrustInfo.class);
		if(trustInfoDBList != null && trustInfoDBList.size()!=0){
			trustInfoList.addAll(trustInfoDBList);
		}else{
			trustListAutoLoad = true;
			sendHttpGet(getTrustListUrl+"?page="+trustListCurrentPage+"&pageSize="+trustListPageSize, getTrustListListener, new productListErrorListener(1));
		}
		//转让社区
		List<ProductListCessionInfo> transferInfoDBList = (List<ProductListCessionInfo>) KdlcDB.findAllByClass(ProductListCessionInfo.class);
		if(transferInfoDBList != null && transferInfoDBList.size() != 0) {
			Log.d(LOG_TAG, "db size:"+transferInfoDBList.size());
			List<CessionTradeInfo> cessionTradeInfoList = (List<CessionTradeInfo>) KdlcDB.findAllByClass(CessionTradeInfo.class);
			Log.d(LOG_TAG, "cess size:"+cessionTradeInfoList.size());
			if(transferInfoDBList.size() > 2 && cessionTradeInfoList != null && cessionTradeInfoList.size() != 0) {
				if(transferInfoDBList.get(1) != null) {
					transferInfoDBList.get(1).getDealList().clear();
					for(int i=0; i<cessionTradeInfoList.size(); i++) {
						transferInfoDBList.get(1).addDealInfo(cessionTradeInfoList.get(i));
					}
				}
			}
			transferZoneList.addAll(transferInfoDBList);
		} else {
			cessionAutoLoad = true;
			sendHttpGet(getCessionListUrl+"?page="+cessionPage+"&pageSize="+cessionPageSize, getCessionListListener, new productListErrorListener(2));
		}
	}
	
	//设置P2P产品列表刷新
	private void setP2PListViewListener() {
		//下拉刷新
		p2pListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.d(LOG_TAG, "p2p refresh");
				if(p2pCurMode.equals(Mode.PULL_FROM_END)) {
					p2pCurPage += 1;
					sendHttpGet(getP2PListUrl+"?page="+p2pCurPage+"&pageSize="+p2pPageSize, getP2PListListener, new productListErrorListener(0));				
				} else {
					p2pCurPage = 1;
					p2pHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(getP2PListUrl+"?page="+p2pCurPage+"&pageSize="+p2pPageSize, getP2PListListener, new productListErrorListener(0));
						}
					}, 500);
				}
				p2pAutoLoad = false;
			}
		});
		
		//判断最后一列状态上拉刷新
		p2pListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if(p2pHasMore == true && !p2pListView.isRefreshing()) {
					p2pCurMode = Mode.PULL_FROM_END;
					p2pListView.setCurrentMode(p2pCurMode);
					p2pListView.setLoadRefreshing();
				}
			}
		});
		//点击事件
		p2pListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				ProductListP2PInfo p2pInfo = (ProductListP2PInfo)parent.getItemAtPosition(position);
				if(p2pInfo.getProductType() == 1) {
					Intent intent = new Intent(mActivity, KdbDetailActivity.class);
					mActivity.startActivity(intent);
				} else {
					Intent intent = new Intent(mActivity, ProductDetailActivity.class);
					intent.putExtra("productId", id);
					intent.putExtra("productName", p2pInfo.getName());
					mActivity.startActivity(intent);
				}
			}
		});
	}
	
	//设置信托列表刷新
	private void setTrustListViewListener(){
		trustListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				Log.d(LOG_TAG, "trust refresh");
				if(trustListCurrentMode.equals(Mode.PULL_FROM_END)){
					trustListCurrentPage += 1;
					sendHttpGet(getTrustListUrl+"?page="+trustListCurrentPage+"&pageSize="+trustListPageSize, getTrustListListener, new productListErrorListener(1));
				}else{
					trustListCurrentPage = 1;
					trustListHasMore = true;
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							sendHttpGet(getTrustListUrl+"?page="+trustListCurrentPage+"&pageSize="+trustListPageSize, getTrustListListener, new productListErrorListener(1));
						}
					}, 500);
				}
				trustListAutoLoad = false;
			}
		});
		
		trustListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if(trustListHasMore == true && !trustListView.isRefreshing() ){
					trustListCurrentMode = Mode.PULL_FROM_END;
					trustListView.setMode(trustListCurrentMode);
					trustListView.setLoadRefreshing();
				}
			}
		});
		
		trustListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ProductListTrustInfo productListTrustInfo = (ProductListTrustInfo) parent.getItemAtPosition(position);
				Intent intent = new Intent(mActivity, ProductDetailActivity.class);
				intent.putExtra("productId", id);
				intent.putExtra("productName", productListTrustInfo.getName());
				mActivity.startActivity(intent);
			}
		});
	};
	
	//设置转让专区刷新
	private void setCessionRefresh(){
		//下拉刷新
		cessionListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.d(LOG_TAG, "cession refresh");
				if(cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionPage += 1;
					sendHttpGet(getCessionListUrl+"?page="+cessionPage+"&pageSize="+cessionPageSize, getCessionListListener, new productListErrorListener(2));						
				} else {
					cessionPage = 1;
					cessionHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(getCessionListUrl+"?page="+cessionPage+"&pageSize="+cessionPageSize, getCessionListListener, new productListErrorListener(2));
						}
					}, 500);
				}	
				cessionAutoLoad = false;
			}
		});
		
		//判断最后一列状态上拉刷新
		cessionListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if(cessionHasMore == true && !cessionListView.isRefreshing()) {
					Log.d(LOG_TAG, "litieshan tag");
					cessionCurMode = Mode.PULL_FROM_END;
					cessionListView.setCurrentMode(cessionCurMode);
					cessionListView.setLoadRefreshing();
				}
			}
			
		});		
	}
	
	private class LabelOnClickListener implements OnClickListener {
		private int index = 0;
		public LabelOnClickListener(int i) {
			index = i;
		}
		public void onClick(View v) {
			mViewPager.setCurrentItem(index);
		}
	}
	
	public class ProductListOnPageChangeListener implements OnPageChangeListener {
		float one = offSet * 2 + bmWidth;// 页卡1 -> 页卡2 偏移量
		float two = one * 2;// 页卡1 -> 页卡3 偏移量
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(one * curIndex, one * arg0, 0, 0);// 显然这个比较简洁，只有一行代码。
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
			curIndex = arg0;
			switch(arg0) {
				case 0:
					p2pLabel.setTextColor(getResources().getColor(R.color.global_red_color));
					trustLabel.setTextColor(getResources().getColor(R.color.global_label_color));
					cessionLabel.setTextColor(getResources().getColor(R.color.global_label_color));
					break;
				case 1:
					p2pLabel.setTextColor(getResources().getColor(R.color.global_label_color));
					trustLabel.setTextColor(getResources().getColor(R.color.global_red_color));
					cessionLabel.setTextColor(getResources().getColor(R.color.global_label_color));
					if(KDLCApplication.app.sessionEqual("trustListAutoRefresh", "1") &&
					   Tool.isVisible(trustListView)) {
						mHandler.postDelayed(new Runnable() {
							public void run() {
								trustListView.setLoadRefreshing();
							}
						}, 500);
						KDLCApplication.app.setSessionVal("trustListAutoRefresh", "0");
					}
					break;
				case 2:
					Log.d(LOG_TAG, "cession page");
					p2pLabel.setTextColor(getResources().getColor(R.color.global_label_color));
					trustLabel.setTextColor(getResources().getColor(R.color.global_label_color));
					cessionLabel.setTextColor(getResources().getColor(R.color.global_red_color));
					if(KDLCApplication.app.sessionEqual("cessionListAutoRefresh", "1") &&
					   Tool.isVisible(cessionListView)) {
						Log.d(LOG_TAG, "cession auto refresh");
						mHandler.postDelayed(new Runnable() {
							public void run() {
								cessionListView.setLoadRefreshing();
							}
						}, 500);
						KDLCApplication.app.setSessionVal("cessionListAutoRefresh", "0");
					}					
					break;
				default:
					p2pLabel.setTextColor(getResources().getColor(R.color.global_red_color));
					trustLabel.setTextColor(getResources().getColor(R.color.global_label_color));
					cessionLabel.setTextColor(getResources().getColor(R.color.global_label_color));
					break;	
			}
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}	
	}
	
	private Listener<JSONObject> getP2PListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				if(response.getInt("code") == 0) {
					if(p2pCurPage == 1) {
						p2pInfoList.clear();
						if(Tool.hasCacheData(ProductListP2PInfo.class))
							KdlcDB.deleteAllByClass(ProductListP2PInfo.class);
					}
					//加载口袋宝信息
					if(response.has("koudaibao") && !response.getString("koudaibao").equals("[]") && response.getJSONObject("koudaibao") != null) {
						JSONObject kdbInfo = response.getJSONObject("koudaibao");
						ProductListP2PInfo kdbInfoEntity = new ProductListP2PInfo( 1, 
																				   kdbInfo.getString("title"),
																				   kdbInfo.getString("status"), 
																				   kdbInfo.getString("apr"), 
																				   kdbInfo.getString("total_money"),
																				   kdbInfo.getString("min_invest_money"),
																				   kdbInfo.getString("cur_invest_times"),
																				   "随取随存",
																				   kdbInfo.getString("daily_invest_limit"),
																				   kdbInfo.getString("daily_withdraw_limit"),
																				   kdbInfo.getString("interest_desc"), 
																				   0);
						p2pInfoList.add(kdbInfoEntity);
					}
					//获取P2P产品列表
					JSONArray dataArray = response.getJSONArray("projects");
					Log.d(LOG_TAG, "dataArray.length():"+dataArray.length());
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject p2pInfo = dataArray.getJSONObject(j);
						Log.d(LOG_TAG, "name:"+p2pInfo.getString("name"));
						ProductListP2PInfo p2pInfoEntity = new ProductListP2PInfo(  2,
																					p2pInfo.getInt("id"),
																					p2pInfo.getString("name"),
																					p2pInfo.getString("status"), 
																					p2pInfo.getString("apr"), 
																					p2pInfo.getString("total_money"),
																					p2pInfo.getString("min_invest_money"),
																					p2pInfo.getString("success_number"),
																					p2pInfo.getString("success_money"),
																					p2pInfo.getString("is_novice"),
																					p2pInfo.getString("period"),
																					p2pInfo.getString("is_day"));					
						p2pInfoList.add(p2pInfoEntity);
					}
		
					if(dataArray.length() < p2pPageSize) {
						p2pHasMore = false;
					}
					if(p2pCurPage == 1) {
						KdlcDB.addByEntityList(p2pInfoList);
						p2pAdapter.setCurCount(0);
					}	
					p2pAdapter.notifyDataSetChanged();
					if (p2pInfoList.size()<=0) {
						 showNoDataView(0);
					}
				} else {
					if(p2pInfoList.size() <= 0) {
						showNetView(0, true);
					}
					if(!p2pAutoLoad)
						dialog = KdlcDialog.showInformDialogContext(mActivity ,response.getString("message"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(p2pInfoList.size() <= 0) {
					showNetView(0, true);
				} else {
					if(!p2pAutoLoad)
						KdlcDialog.showBottomToast("");
				}
			}
			if(p2pListView != null && !p2pAutoLoad) {
				p2pListView.onRefreshComplete();
				if(p2pCurMode.equals(Mode.PULL_FROM_END)) {
					p2pCurMode = Mode.PULL_FROM_START;
					p2pListView.setCurrentMode(p2pCurMode);
				}
			}
		}
	};
	
	private Listener<JSONObject> getTrustListListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				Log.d(LOG_TAG, "response2:"+response.toString());
				if (response.getInt("code") == 0) {
					if (trustListCurrentPage == 1) {
						trustInfoList.clear();
						if(Tool.hasCacheData(ProductListTrustInfo.class))
							KdlcDB.deleteAllByClass(ProductListTrustInfo.class);
					}
					JSONArray dataArray = response.getJSONArray("projects");
					for (int i = 0; i < dataArray.length(); i++) {
						JSONObject trustListInfo = dataArray.getJSONObject(i);
						ProductListTrustInfo productListTrustInfo = new ProductListTrustInfo(
								trustListInfo.getInt("id"),
								trustListInfo.getString("name"),
								trustListInfo.getString("status"),
								trustListInfo.getString("total_money"),
								trustListInfo.getString("success_money"),
								trustListInfo.getString("success_number"),
								trustListInfo.getString("is_novice"),
								trustListInfo.getString("min_invest_money"),
								trustListInfo.getString("period"),
								trustListInfo.getString("is_day"),
								trustListInfo.getString("apr"));
						trustInfoList.add(productListTrustInfo);
					}
					if(dataArray.length() < trustListPageSize) {
						trustListHasMore = false;
					}
					if(trustListCurrentPage == 1) {
						KdlcDB.addByEntityList(trustInfoList);
						trustAdapter.setCurCount(0);
					}	
					trustAdapter.notifyDataSetChanged();
					if (trustInfoList.size()<=0) {
						 showNoDataView(1);
					}
				} else {
					if(trustInfoList.size() <= 0) {
						showNetView(1, true);
					}
					if(!trustListAutoLoad)
						dialog = KdlcDialog.showInformDialogContext(mActivity ,response.getString("message"));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(trustInfoList.size() <= 0) {
					showNetView(1, true);
				} else {
					if(!trustListAutoLoad)
						KdlcDialog.showBottomToast("");
				}
			}
			if(trustListView != null && !trustListAutoLoad){
				trustListView.onRefreshComplete();
				if(trustListCurrentMode.equals(Mode.PULL_FROM_END)){
					trustListCurrentMode = Mode.PULL_FROM_START;
					trustListView.setCurrentMode(trustListCurrentMode);
				}
			}
		}
	};
	
	private Listener<JSONObject> getCessionListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				Log.d(LOG_TAG, "cession response:"+response.toString());
				if(response.getInt("code") == 0) {
					if(cessionPage == 1) {
						//清空原始数据
						transferZoneList.clear();
						if(Tool.hasCacheData(ProductListCessionInfo.class))
							KdlcDB.deleteAllByClass(ProductListCessionInfo.class);
						//添加首条记录
						JSONObject saData = response.getJSONObject("statistics");
						ProductListCessionInfo dealProductListTransferZoneInfo = new ProductListCessionInfo(0);
						if(saData.getInt("code") == 0) {
							dealProductListTransferZoneInfo.setAccumulatedAmount(saData.getString("accumulatedAmount"));
							dealProductListTransferZoneInfo.setAccumulatedCount(saData.getString("accumulatedCount"));
						} else {
							KdlcDialog.showBottomToast(saData.getString("message"));
							dealProductListTransferZoneInfo.setAccumulatedAmount("0");
							dealProductListTransferZoneInfo.setAccumulatedCount("0");
						}
						transferZoneList.add(dealProductListTransferZoneInfo);
						//最新交易列表
						JSONObject recentData = response.getJSONObject("recentlyAppliedItems");
						ProductListCessionInfo dealListInfo = new ProductListCessionInfo(1);
						if(recentData.getInt("code") == 0) {
							if(Tool.hasCacheData(CessionTradeInfo.class))
								KdlcDB.deleteAllByClass(CessionTradeInfo.class);
							JSONArray  creArray = recentData.getJSONArray("creditItems");
							if(creArray.length() > 0) {
								for(int i = 0; i < creArray.length(); i++) {
									JSONObject creObject = creArray.getJSONObject(i);
									CessionTradeInfo tradeInfo = new CessionTradeInfo(creObject.getString("id"),
																					   creObject.getString("invest_uid"),
																					   creObject.getString("profits_uid"),
																					   creObject.getString("trade_time"),
																					   creObject.getString("duein_capital"),
																					   creObject.getString("project_apr"));
									dealListInfo.addDealInfo(tradeInfo);
									KdlcDB.addByEntity(tradeInfo);
								}
							}
						}
						transferZoneList.add(dealListInfo);
					}
					//添加转让列表
					JSONObject recentPData = response.getJSONObject("recentlyPublishedItems");
					if(recentPData.getInt("code") == 0) {
						JSONArray dataArray = recentPData.getJSONArray("creditItems");
						for (int j = 0; j < dataArray.length(); j++) {
							JSONObject dataRoot = dataArray.getJSONObject(j);
							Log.d(LOG_TAG, "name:"+dataRoot.getJSONObject("project").getString("name"));
							ProductListCessionInfo tranInfo = new ProductListCessionInfo(2, 
																						 dataRoot.getString("id"),
																						 dataRoot.getString("invest_id"),
																						 dataRoot.getString("project_type"),
																						 dataRoot.getString("assign_fee"), 
																						 dataRoot.getString("assign_rate"), 
																						 dataRoot.getString("rest_days"), 
																						 dataRoot.getJSONObject("project").getString("id"),
																						 dataRoot.getJSONObject("project").getString("apr"), 
																						 dataRoot.getJSONObject("project").getString("name"),
																						 dataRoot.getString("user_name")
																						 );
							transferZoneList.add(tranInfo);
						}
						if(dataArray.length() < cessionPageSize){
							cessionHasMore = false;
						}
						if(cessionPage == 1) {
							if(dataArray.length() <= 0) {
								ProductListCessionInfo tranInfo = new ProductListCessionInfo();
								tranInfo.setInfoType(2);
								tranInfo.setNoCession("1");
								transferZoneList.add(tranInfo);
							}
							KdlcDB.addByEntityList(transferZoneList);
						}
					} else {
						KdlcDialog.showBottomToast(recentPData.getString("message"));
					}
					cessionAdapter.notifyDataSetChanged();
					if(transferZoneList.size() <= 0) {
						showNoDataView(2);
					}		
				} else {
					if(transferZoneList.size() <= 0) {
						showNetView(2, true);
					}
					if(!cessionAutoLoad)
						dialog = KdlcDialog.showInformDialogContext(mActivity ,response.getString("message"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(transferZoneList.size() <= 0) {
					showNetView(2, true);
				} else {
					if(!cessionAutoLoad)
						KdlcDialog.showBottomToast("");
				}
			}
			if(cessionListView != null && !cessionAutoLoad) {
				cessionListView.onRefreshComplete();
				if(cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurMode = Mode.PULL_FROM_START;
					cessionListView.setCurrentMode(cessionCurMode);
				}
			}
		}	
	};
	
	@Override
	public void onStart() {
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(p2pListView);
			Tool.resetPullRefreshView(trustListView);
			Tool.resetPullRefreshView(cessionListView);
		} else {
			createFlag = false;
		}
		if(KDLCApplication.app.sessionEqual("p2pListAutoRefresh", "1") && 
		   KDLCApplication.app.sessionEqual("p2pListAutoRefreshClick", "1") &&
		   Tool.isVisible(p2pListView) && curIndex == 0) {
			
			mHandler.postDelayed(new Runnable() {
				public void run() {
					p2pListView.setLoadRefreshing();
				}
			}, 500);
			Map<String, String> vals = new HashMap<String, String>();
			vals.put("p2pListAutoRefresh", "0");
			vals.put("p2pListAutoRefreshClick", "0");
			KDLCApplication.app.getSession().set(vals);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(LOG_TAG, "call onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(LOG_TAG, "call onStop");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.d(LOG_TAG, "call onHiddenChanged");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, "call onDestroy");
	}

	private void showNetView(int flag, boolean err) {
		if(flag == 0) {
			if(p2pInfoList.size() <= 0) {
				p2pListView.setVisibility(View.GONE);
				noDataLayoutP2P.setVisibility(View.GONE);
				errNetLayoutP2P.setVisibility(View.VISIBLE);
				networkTextP2P.setText(err ? "网络出错" : "网络未连接");
			} else {
				KdlcDialog.showBottomToast(err ? "" : "网络未连接，请检查设置后再试");
			}

		} else if(flag == 1) {
			if(trustInfoList.size() <= 0) {
				trustListView.setVisibility(View.GONE);
				noDataLayoutTrust.setVisibility(View.GONE);
				errNetLayoutTrust.setVisibility(View.VISIBLE);
				networkTextTrust.setText(err ? "网络出错" : "网络未连接");
			} else {
				KdlcDialog.showBottomToast(err ? "" : "网络未连接，请检查设置后再试");
			}
		} else {
			if(transferZoneList.size() <= 0) {
				cessionListView.setVisibility(View.GONE);
				noDataLayoutCession.setVisibility(View.GONE);
				errNetLayoutCession.setVisibility(View.VISIBLE);
				networkTextCession.setText(err ? "网络出错" : "网络未连接");
			} else {
				KdlcDialog.showBottomToast(err ? "" : "网络未连接，请检查设置后再试");
			}
		}
	}
	
	private void showNoDataView(int flag) {
		if(flag == 0) {
			noDataLayoutP2P.setVisibility(View.VISIBLE);
			p2pListView.setVisibility(View.GONE);
			errNetLayoutP2P.setVisibility(View.GONE);
			
		} else if(flag == 1) {
			noDataLayoutTrust.setVisibility(View.VISIBLE);
			trustListView.setVisibility(View.GONE);
			errNetLayoutTrust.setVisibility(View.GONE);
			
		} else {
			noDataLayoutCession.setVisibility(View.VISIBLE);
			cessionListView.setVisibility(View.GONE);
			errNetLayoutCession.setVisibility(View.GONE);
		}
	}
	
	private void showListView(int flag) {
		if(flag == 0) {
			errNetLayoutP2P.setVisibility(View.GONE);
			noDataLayoutP2P.setVisibility(View.GONE);
			p2pListView.setVisibility(View.VISIBLE);
		} else if(flag == 1) {
			errNetLayoutTrust.setVisibility(View.GONE);
			noDataLayoutTrust.setVisibility(View.GONE);
			trustListView.setVisibility(View.VISIBLE);
		} else {
			errNetLayoutCession.setVisibility(View.GONE);
			noDataLayoutCession.setVisibility(View.GONE);
			cessionListView.setVisibility(View.VISIBLE);
		}	
	}
	
	//首次加载使用错误监听
	private class productListErrorListener implements ErrorListener {
		private int flag;
		public productListErrorListener(int flag) {
			this.flag = flag;
		}
		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			if(NoConnectionError.class.isInstance(error)) {
				showNetView(flag, false);
			} else {
				showNetView(flag, true);
			}
			if(flag == 0) {
				Tool.resetPullRefreshView(p2pListView);
			} else if(flag == 1) {
				Tool.resetPullRefreshView(trustListView);
			} else {
				Tool.resetPullRefreshView(cessionListView);
			}
		}
	}
}
