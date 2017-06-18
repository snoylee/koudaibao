package com.kdkj.koudailicai.view.selfcenter.holdasset;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.HoldingAssetListInfoAdapter;
import com.kdkj.koudailicai.domain.HoldingAssetListInfoRecord;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
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
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.lib.http.MyHttpClient;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.TransactionRecordActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.WithdrawListActivity;

public class HoldingAssetActivity extends BaseActivity implements HttpErrorInterface{
	private String LOG_TAG = HoldingAssetActivity.class.getName();
	//views
	private TitleView mTitle;
	private PullToRefreshListView holdingAssetListView;
	//Adapter
	private HoldingAssetListInfoAdapter  mHoldingAdapter;
	public ArrayList<HoldingAssetListInfoRecord> holdingAssetList = new ArrayList<HoldingAssetListInfoRecord>();
	//contents
	private String getHoldingAssetUrl;
	private int holdingAssetPage = 1;
	private int holdingPageSize = 10;
	private boolean holdingAssetHasMore = true;
	private Mode holdingAssetCurMode = Mode.PULL_FROM_START;	
	
	private Handler mHandler = new Handler();
    private RelativeLayout netErrView;
    private TextView networkLoad;
    private TextView networkText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.holdingasset);
		parseUrl();
		initTitle();
		findViews();
		initListView();
		registerBoradcastReceiver();
		getContents();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(holdingAssetListView);
		} else {
			createFlag = false;
		}
	}
	
	private void parseUrl() {
		 if (KDLCApplication.app.isGlobalConfCompleted()) {
			 getHoldingAssetUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_ACCOUNT_HOLD);
		 } else {
			 getHoldingAssetUrl = G.URL_GET_ACCOUNT_HOLD;
		 }
	}
		
	private void initTitle() {
		mTitle = (TitleView)findViewById(R.id.title);
		mTitle.setTitle(R.string.holding_asset_title);
		mTitle.showLeftButton(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					HoldingAssetActivity.this.finish();
				}
			});
		mTitle.setLeftImageButton(R.drawable.back);
		mTitle.setLeftTextButton("返回");
	}
	
	private void findViews() {
		holdingAssetListView = (PullToRefreshListView) this.findViewById(R.id.holdingassentListView);
		netErrView = (RelativeLayout)findViewById(R.id.netErrView);
		networkLoad = (TextView) findViewById(R.id.networkload);
		networkText = (TextView) findViewById(R.id.networktext);
	}
	
	private void initListView() {
		mHoldingAdapter = new HoldingAssetListInfoAdapter(HoldingAssetActivity.this, holdingAssetList); 
		holdingAssetListView.setAdapter(mHoldingAdapter);
		setRefresh();
	}
	
	private void getContents() {
		mHandler.postDelayed(new Runnable() {
			public void run() {
				holdingAssetListView.setLoadRefreshing();
			}
		}, G.AUTO_LOAD_DELAYED);
	}
	
	private void setRefresh(){
		//下拉刷新
		holdingAssetListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				errorListener.setRefreshView(holdingAssetListView);
				errorListener.setErrInterface(holdingAssetList.size() > 0 ? null : HoldingAssetActivity.this);
				if(holdingAssetCurMode.equals(Mode.PULL_FROM_END)) {
					holdingAssetPage += 1;
					sendHttpGet(getHoldingAssetUrl+"?page="+holdingAssetPage+"&pageSize="+ holdingPageSize, getHolidngAssetListListener);
				} else {
					holdingAssetPage = 1;
					holdingAssetHasMore= true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(getHoldingAssetUrl+"?page="+holdingAssetPage+"&pageSize="+ holdingPageSize, getHolidngAssetListListener);
						}
					}, G.AUTO_LOAD_DELAYED);
				}
			}
		});
		//判断最后一列状态上拉刷新
		holdingAssetListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if(holdingAssetHasMore == true && !holdingAssetListView.isRefreshing()) {
					holdingAssetCurMode= Mode.PULL_FROM_END;
					holdingAssetListView.setCurrentMode(holdingAssetCurMode);
					holdingAssetListView.setLoadRefreshing();
				}
			}
		});
	}

	private Listener<JSONObject> getHolidngAssetListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try{
				if(response.getInt("code") == 0) {
					holdingAssetListView.setVisibility(View.VISIBLE);
		        	Log.d(LOG_TAG, "execute result:"+response);
		        	if(holdingAssetPage == 1) {
		        		holdingAssetList.clear();
			        	JSONObject data = response.getJSONObject("data");
			        	HoldingAssetListInfoRecord  holdingAssetListInfoRecord1 = new HoldingAssetListInfoRecord();
			        	holdingAssetListInfoRecord1.setInfoType(1);
			        	holdingAssetListInfoRecord1.setTotalHoldMoney(data.getString("total_hold_money"));
			        	holdingAssetListInfoRecord1.setKdbTotalMoney(data.getString("kdb_total_money"));
			    		holdingAssetListInfoRecord1.setKdbTotalProfits(data.getString("kdb_total_profits"));
			    		holdingAssetListInfoRecord1.setKdbDueinCapital(data.getString("duein_capital"));
			    		holdingAssetListInfoRecord1.setKdbDueinProfits(data.getString("duein_profits"));
			    		holdingAssetListInfoRecord1.setKdbInvestingMoney(data.getString("investing_money"));
			    		holdingAssetList.add(holdingAssetListInfoRecord1);
			    		JSONArray pendingList = response.getJSONArray("pending_projects");
			       		for(int i = 0; i<pendingList.length(); i++) {
			    			HoldingAssetListInfoRecord  holdingAssetListInfoRecordPending = new HoldingAssetListInfoRecord();
			    			JSONObject pendingDataObject = pendingList.getJSONObject(i);
			    			holdingAssetListInfoRecordPending.setInfoType(2);
			    			holdingAssetListInfoRecordPending.setProjectId(pendingDataObject.getString("id"));
			    			holdingAssetListInfoRecordPending.setInvestId(pendingDataObject.getString("invest_id"));
			    			holdingAssetListInfoRecordPending.setProjectName(pendingDataObject.getString("project_name"));
			    			holdingAssetListInfoRecordPending.setProjectApr(pendingDataObject.getString("project_apr"));
			    			holdingAssetListInfoRecordPending.setDueinCapiTal(pendingDataObject.getString("duein_capital"));
			    			holdingAssetListInfoRecordPending.setDueinProfits(pendingDataObject.getString("duein_profits"));
			    			holdingAssetListInfoRecordPending.setInerestAtartDate(pendingDataObject.getString("interest_start_date"));
			    			holdingAssetListInfoRecordPending.setLastRepayDate(pendingDataObject.getString("last_repay_date"));
			    			holdingAssetListInfoRecordPending.setIsTransfer(pendingDataObject.getString("is_transfer"));
			    			holdingAssetListInfoRecordPending.setStatus(pendingDataObject.getString("status"));
			    			holdingAssetListInfoRecordPending.setStatusLabel(pendingDataObject.getString("statusLabel"));
			    			holdingAssetListInfoRecordPending.setBtnType(pendingDataObject.getString("btn_type"));
				    		holdingAssetList.add(holdingAssetListInfoRecordPending);
			    		}
		        	}
		        	JSONArray list = response.getJSONArray("list");
		    		for(int i = 0; i<list.length(); i++) {
		    			HoldingAssetListInfoRecord  holdingAssetListInfoRecord = new HoldingAssetListInfoRecord();
		    			JSONObject dataObject = list.getJSONObject(i);
		    			holdingAssetListInfoRecord.setInfoType(2);
		    			holdingAssetListInfoRecord.setProjectId(dataObject.getString("id"));
		    			holdingAssetListInfoRecord.setInvestId(dataObject.getString("invest_id"));
		    			holdingAssetListInfoRecord.setProjectName(dataObject.getString("project_name"));
		    			holdingAssetListInfoRecord.setProjectApr(dataObject.getString("project_apr"));
		    			holdingAssetListInfoRecord.setDueinCapiTal(dataObject.getString("duein_capital"));
		    			holdingAssetListInfoRecord.setDueinProfits(dataObject.getString("duein_profits"));
		    			holdingAssetListInfoRecord.setInerestAtartDate(dataObject.getString("interest_start_date"));
		    			holdingAssetListInfoRecord.setLastRepayDate(dataObject.getString("last_repay_date"));
			    		holdingAssetListInfoRecord.setIsTransfer(dataObject.getString("is_transfer"));
			    		holdingAssetListInfoRecord.setStatus(dataObject.getString("status"));
			    		holdingAssetListInfoRecord.setStatusLabel(dataObject.getString("statusLabel"));
			    		holdingAssetListInfoRecord.setBtnType(dataObject.getString("btn_type"));
			    		holdingAssetList.add(holdingAssetListInfoRecord);
		    		}
		    		if(list.length() < holdingPageSize) {
						holdingAssetHasMore = false;
					}
		    		holdingAssetPage++;
			    	mHoldingAdapter.notifyDataSetChanged();
				}else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(HoldingAssetActivity.this);
        		} else {
    				if(holdingAssetList.size() <= 0) {
    					showErrReq();
    				}
    				dialog = KdlcDialog.showInformDialog(HoldingAssetActivity.this ,response.getString("message"));
				}
			} catch(Exception e) {
				if(holdingAssetList.size() <= 0) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
			if(holdingAssetListView != null) {
				holdingAssetListView.onRefreshComplete();
				if(holdingAssetCurMode.equals(Mode.PULL_FROM_END)) {
					holdingAssetCurMode = Mode.PULL_FROM_START;
					holdingAssetListView.setCurrentMode(holdingAssetCurMode);
				}
			}
		}
	};
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	            String action = intent.getAction();  
	            if(action.equals(G.HOLD_ASSET_REFRESH)){  
	                getContents();
	            }  
	        } 
	};  
	      
    public void registerBoradcastReceiver(){  
        IntentFilter myIntentFilter = new IntentFilter();  
        myIntentFilter.addAction(G.HOLD_ASSET_REFRESH);  
        //注册广播        
        registerReceiver(mBroadcastReceiver, myIntentFilter);  
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	public void showNoNetwork() {
		showNetView(false);
	}

	@Override
	public void showErrReq() {
		// TODO Auto-generated method stub
		showNetView(true);
	}
	
	private void showNetView(boolean err) {
		holdingAssetListView.setVisibility(View.GONE);
		netErrView.setVisibility(View.VISIBLE);
        networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holdingAssetListView.setVisibility(View.VISIBLE);
				netErrView.setVisibility(View.GONE);
				holdingAssetListView.setLoadRefreshing();
			}
		});
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
}
