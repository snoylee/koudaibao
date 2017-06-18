package com.kdkj.koudailicai.view.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.SelfCenterRecord;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.NumChangeTextView;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.invest.InvestSuccessActivity;
import com.kdkj.koudailicai.view.more.MessageCenterActivity;
import com.kdkj.koudailicai.view.selfcenter.AccountCenterActivity;
import com.kdkj.koudailicai.view.selfcenter.AccountFinishedActivity;
import com.kdkj.koudailicai.view.selfcenter.TransactionRecordActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.AccountRemainActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.HoldingAssetActivity;
import com.kdkj.koudailicai.view.selfcenter.profit.TotalProfitActivity;
import com.kdkj.koudailicai.view.selfcenter.profit.LastDayProfitsActivity;
import com.kdkj.koudailicai.xgpush.XGNotification;
public class SelfCenterFragment extends BaseFragment implements OnClickListener {
	private FragmentActivity mActivity;
	private View mParent;
	private String LOG_TAG = SelfCenterFragment.class.getName();
	//views
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ScrollView mScrollView;
	private TitleView mTitle;
	private RelativeLayout lastDayProfitsView;
	private TextView lastDayProfitsLabel;
	private NumChangeTextView lastDayProfitsText;
	private RelativeLayout totalProfitsView;
	private NumChangeTextView totalProfitsText;
	private RelativeLayout totalAssestsView;
	private NumChangeTextView totalAssestsText;
	private RelativeLayout leftAssestsView;
	private RelativeLayout holdingAssestsView;
	private NumChangeTextView  holdingAssestsText;
	private RelativeLayout availableAssestsView;
	private NumChangeTextView  availableAssestsText;
	private RelativeLayout investRecordView;
	private TextView  investRecordCountText;
	private RelativeLayout historyRecordView;
	private TextView  historyRecordCountText;	
	//sizes
	private int screenHeight;
	private int screenWidth;
	//dimens
	private double marginLeft = 0.046;
	private double marginRight = 0.046;
	private double totalProfitsViewHeight = 0.086;
	private double totalAssetsViewHeight = 0.060;
	private double leftAssetsViewHeight = 0.115;
	private double investRecordViewHeight = 0.086;
	//contents
	private Handler mHandler = new Handler();
	private int uid;
	private String getSelfCenterRecordUrl;
	private SelfCenterRecord selfcenterRecord;
	
	public static SelfCenterFragment newInstance(int index) {
		SelfCenterFragment f = new SelfCenterFragment();
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
		View view = inflater.inflate(R.layout.fragment_selfcenter, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(LOG_TAG, "call onActivityCreated");
		mActivity = getActivity();
		parseUrl();
		findViews();
		initSize();
		initTitle();
	}
	
	private void parseUrl() {
		 if (KDLCApplication.app.getGlobalConfigManager() != null && KDLCApplication.app.getGlobalConfigManager().isComplete()) {
			 getSelfCenterRecordUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_SELFCENTER_RECORD);
			 Log.d(LOG_TAG, "Succeed To Get G Info:"+getSelfCenterRecordUrl);
		 } else {
			 getSelfCenterRecordUrl = G.URL_GET_SELFCENTER_RECORD;
			 Log.d(LOG_TAG, "Get Local Url:"+getSelfCenterRecordUrl);
		 }
	}
	
	private void findViews() {
		mParent = getView();
		mPullRefreshScrollView = (PullToRefreshScrollView) mParent.findViewById(R.id.refresh_root);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				Log.d(LOG_TAG, "onRefresh");
				if(!Tool.isBlank(KDLCApplication.app.getSessionVal("uid"))) {
					mHandler.postDelayed(new Runnable() {
						public void run() {
							getRecord();
							setMessageIcon();
						}
					}, 500);
				} else {
					mPullRefreshScrollView.onRefreshComplete();
				}
			}
		});
		mScrollView = mPullRefreshScrollView.getRefreshableView();
		mTitle         		  = (TitleView) mParent.findViewById(R.id.title);
		lastDayProfitsView 	  = (RelativeLayout) mParent.findViewById(R.id.lastdayProfitsView);
		lastDayProfitsLabel   = (TextView) mParent.findViewById(R.id.lastdayProfitsLabel);
		lastDayProfitsText 	  = (NumChangeTextView) mParent.findViewById(R.id.lastdayProfits);
		totalProfitsView 	  = (RelativeLayout) mParent.findViewById(R.id.totalProfitsView);
		totalProfitsText 	  = (NumChangeTextView) mParent.findViewById(R.id.totalProfits);
		totalAssestsView 	  = (RelativeLayout) mParent.findViewById(R.id.totalAssetsView);
		totalAssestsText 	  = (NumChangeTextView) mParent.findViewById(R.id.totalAssets);
		leftAssestsView       = (RelativeLayout) mParent.findViewById(R.id.leftAssetsView);
		holdingAssestsView    = (RelativeLayout) mParent.findViewById(R.id.holdingAssetsView);
		holdingAssestsText 	  = (NumChangeTextView) mParent.findViewById(R.id.holdingAssets);
		availableAssestsView  = (RelativeLayout) mParent.findViewById(R.id.availableAssetsView);
		availableAssestsText  = (NumChangeTextView) mParent.findViewById(R.id.availableAssets);
		investRecordView 	  = (RelativeLayout) mParent.findViewById(R.id.investRecodView);
		investRecordCountText = (TextView) mParent.findViewById(R.id.investRecodCount);	
		historyRecordView 	  = (RelativeLayout) mParent.findViewById(R.id.historyProductView);
		historyRecordCountText= (TextView) mParent.findViewById(R.id.historyRecordCountText);
		holdingAssestsView .setOnClickListener(this);
		investRecordView.setOnClickListener(this);
		availableAssestsView.setOnClickListener(this);
		lastDayProfitsView.setOnClickListener(this);
		totalProfitsView.setOnClickListener(this);
		historyRecordView.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.holdingAssetsView:
			intent = new Intent(mActivity,HoldingAssetActivity.class);
			Tool.startActivityNeedLogin(mActivity, intent);
			break;
		case R.id.investRecodView:
			intent= new Intent(mActivity,TransactionRecordActivity.class);
			Tool.startActivityNeedLogin(mActivity, intent);
			break;	
		case R.id.totalProfitsView:
			intent = new Intent(mActivity,TotalProfitActivity.class);
			Tool.startActivityNeedLogin(mActivity, intent);
			break;
		case R.id.availableAssetsView:
			intent = new Intent(mActivity,AccountRemainActivity.class);
			Tool.startActivityNeedLogin(mActivity, intent);
			break;
		case R.id.lastdayProfitsView:
			intent = new Intent(mActivity,LastDayProfitsActivity.class);
			Tool.startActivityNeedLogin(mActivity, intent);
			break;
		case R.id.historyProductView:
			intent = new Intent(mActivity,AccountFinishedActivity.class);
			Tool.startActivityNeedLogin(mActivity, intent);
			break;
		default:
			break;
		}
	}
	
	private void initTitle() {
		mTitle.setTitle(R.string.selfcenter_title);
		mTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity, MessageCenterActivity.class);
				Tool.startActivityNeedLogin(mActivity, intent);
			}
		});
		
		setMessageIcon();
		
		mTitle.showRightButton(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(mActivity, AccountCenterActivity.class);
				Tool.startActivityNeedLogin(mActivity, intent);
			}
		});
		mTitle.setRightImageButton(R.drawable.account_icon);
		mTitle.setTitlePadding((int) (screenWidth*marginLeft));
	}
	
	private void initSize() {
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		//设置累计收益
		RelativeLayout.LayoutParams totalProfitsLayoutParams = (RelativeLayout.LayoutParams) totalProfitsView.getLayoutParams();
		totalProfitsLayoutParams.height = (int) (screenHeight*totalProfitsViewHeight);
		totalProfitsView.setLayoutParams(totalProfitsLayoutParams);
		//设置总资产
		RelativeLayout.LayoutParams totalAssestsLayoutParams = (RelativeLayout.LayoutParams) totalAssestsView.getLayoutParams();
		totalAssestsLayoutParams.height = (int) (screenHeight*totalAssetsViewHeight);
		totalAssestsView.setLayoutParams(totalAssestsLayoutParams);
		//设置持有资产等
		RelativeLayout.LayoutParams leftAssestsViewLayoutParams = (RelativeLayout.LayoutParams) leftAssestsView.getLayoutParams();
		leftAssestsViewLayoutParams.height = (int) (screenHeight*leftAssetsViewHeight);
		leftAssestsView.setLayoutParams(leftAssestsViewLayoutParams);
		//设置交易记录
		RelativeLayout.LayoutParams investRecordViewLayoutParams = (RelativeLayout.LayoutParams) investRecordView.getLayoutParams();
		investRecordViewLayoutParams.height = (int) (screenHeight*investRecordViewHeight);
		investRecordView.setLayoutParams(investRecordViewLayoutParams);
		//设置历史项目
		RelativeLayout.LayoutParams historyRecordViewLayoutParams = (RelativeLayout.LayoutParams) historyRecordView.getLayoutParams();
		historyRecordViewLayoutParams.height = (int) (screenHeight*investRecordViewHeight);
		historyRecordView.setLayoutParams(historyRecordViewLayoutParams);
	}
	
	private void initContents(boolean createFlag) {
		if(KDLCApplication.app.getSessionVal("uid") != null) {
			uid = Integer.parseInt(KDLCApplication.app.getSession().get("uid"));
			selfcenterRecord = (SelfCenterRecord) KdlcDB.findOneByWhere(SelfCenterRecord.class, "uid="+uid);
		}
		if(selfcenterRecord != null) {
			Log.d(LOG_TAG, "id:"+selfcenterRecord.getId()+", getNewTradeCount:"+selfcenterRecord.getNewTradeCount());
			setNoContents();
			if(!createFlag && !Tool.isBlank(KDLCApplication.app.getSessionVal("uid")) &&
			   KDLCApplication.app.sessionEqual("selfCenterAutoRefresh", "1") && 
			   KDLCApplication.app.sessionEqual("selfCenterAutoRefreshClick", "1")) {
				Log.d(LOG_TAG, "auto refresh");
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPullRefreshScrollView.setLoadRefreshing();
					}
				}, 500);
				Map<String, String> vals = new HashMap<String, String>();
				vals.put("selfCenterAutoRefresh", "0");
				vals.put("selfCenterAutoRefreshClick", "0");
				KDLCApplication.app.getSession().set(vals);
			}
		} else if(KDLCApplication.app.getSessionVal("uid") != null) {
			if(createFlag) {
				sendHttpGet(getSelfCenterRecordUrl, getSelfCenterRecordListener);
			} else {
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPullRefreshScrollView.setLoadRefreshing();
					}
				}, 500);
			}
		}
	}
	
	private void setContents() {
		if(selfcenterRecord != null) {
			Log.d(LOG_TAG, "cur:"+selfcenterRecord.getCurProfits());
			Log.d(LOG_TAG, "curdate:"+selfcenterRecord.getCurProfitsDate());
			lastDayProfitsLabel.setText(Tool.unixTimeToDate(selfcenterRecord.getCurProfitsDate(), "MM月dd日收益（元）"));
			lastDayProfitsText.setNumText(Tool.toDeciDouble(selfcenterRecord.getCurProfits()));
			totalProfitsText.setNumText(Tool.toDeciDouble(selfcenterRecord.getTotalProfits()));
			totalAssestsText.setText(Tool.toDeciDouble(selfcenterRecord.getTotalMoney()));
			holdingAssestsText.setNumText(Tool.toDeciDouble(selfcenterRecord.getHoldMoney()));
			availableAssestsText.setNumText(Tool.toDeciDouble(selfcenterRecord.getRemainMoney()));
			investRecordCountText.setText("("+selfcenterRecord.getNewTradeCount()+")");
			historyRecordCountText.setText("("+ (selfcenterRecord.getFinishCount()==null ? 0 : selfcenterRecord.getFinishCount())+")");
		}
	}
	
	private void setNoContents() {
		if(selfcenterRecord != null) {
			Log.d(LOG_TAG, "cur:"+selfcenterRecord.getCurProfits());
			Log.d(LOG_TAG, "curdate:"+selfcenterRecord.getCurProfitsDate());
			lastDayProfitsLabel.setText(Tool.unixTimeToDate(selfcenterRecord.getCurProfitsDate(), "MM月dd日收益（元）"));
			lastDayProfitsText.setText(Tool.toDeciDouble(selfcenterRecord.getCurProfits()));
			totalProfitsText.setText(Tool.toDeciDouble(selfcenterRecord.getTotalProfits()));
			totalAssestsText.setText(Tool.toDeciDouble(selfcenterRecord.getTotalMoney()));
			holdingAssestsText.setText(Tool.toDeciDouble(selfcenterRecord.getHoldMoney()));
			availableAssestsText.setText(Tool.toDeciDouble(selfcenterRecord.getRemainMoney()));
			investRecordCountText.setText("("+selfcenterRecord.getNewTradeCount()+")");
			historyRecordCountText.setText("("+ (selfcenterRecord.getFinishCount()==null ? 0 : selfcenterRecord.getFinishCount())+")");
		}
	}
	
	private void getRecord() {
		Log.d(LOG_TAG, "getRecord");
		sendHttpGet(getSelfCenterRecordUrl, getSelfCenterRecordListener, new PullToRefreshHttpErrorListener(mActivity, mPullRefreshScrollView));
	}
	
	private Listener<JSONObject> getSelfCenterRecordListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
        	try {
        		Log.d(LOG_TAG, "response:"+response.toString());
        		int retCode = response.getInt("code");
        		if(retCode == -1) {
        			KDLCApplication.app.logout();
        		} else if(retCode == 0){
					JSONObject data = response.getJSONObject("data");
		    		selfcenterRecord = new SelfCenterRecord();
		    		selfcenterRecord.setUid(uid);
		    		selfcenterRecord.setCurProfitsDate(data.getString("lastday_profits_date"));
		    		selfcenterRecord.setCurProfits(data.getString("lastday_profits"));
		    		selfcenterRecord.setTotalProfits(data.getString("total_profits"));
		    		selfcenterRecord.setTotalMoney(data.getString("total_money"));
		    		selfcenterRecord.setHoldMoney(data.getString("hold_money"));
		    		selfcenterRecord.setRemainMoney(data.getString("remain_money"));
		    		selfcenterRecord.setNewTradeCount(data.getString("trade_count"));
		    		selfcenterRecord.setFinishCount(data.getString("finished_count"));
		    		setContents();
		    		if(Tool.hasCacheData(SelfCenterRecord.class))
		    			KdlcDB.deleteByWhere(SelfCenterRecord.class, "uid="+uid);
	    			KdlcDB.addByEntity(selfcenterRecord);
        		} else {
        			
        		}
        	} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	mPullRefreshScrollView.onRefreshComplete();
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(mPullRefreshScrollView);
		} else {
			createFlag = false;
		}
		initContents(createFlag);
		setMessageIcon();
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
		if(mPullRefreshScrollView.isRefreshing()) {
			mPullRefreshScrollView.onRefreshComplete();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, "call onDestroy");
	}

	private void setMessageIcon(){
		if(KDLCApplication.app.sessionEqual("message_center_status", "0")){
			mTitle.setLeftImageButton(R.drawable.message_icon);
		}else{
			//消息未读图标
			mTitle.setLeftImageButton(R.drawable.message_no_read);
		}
	}
}
