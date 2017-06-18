package com.kdkj.koudailicai.view.selfcenter.holdasset;

import java.util.ArrayList;

import com.kdkj.koudailicai.view.KDLCApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.TransferRecordAdapter;
import com.kdkj.koudailicai.domain.CessionTradeInfo;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.lib.http.MyHttpClient;
import com.kdkj.koudailicai.view.BaseActivity;
public class TransferRecordActivity extends BaseActivity implements HttpErrorInterface{
	private ArrayList<CessionTradeInfo> item = new ArrayList<CessionTradeInfo>();
	private TransferRecordAdapter adapter;
	private PullToRefreshListView transferRecordList;
	private TitleView title;
	
	private RelativeLayout errNetLayout;
	private TextView networkLoad;
	private TextView networkText;
	
	private String url;
	private String creditItemsCount;
	private int PageSize = 15;
	private Handler mHandler = new Handler();
	private int cessionCurPage = 1;
	private boolean cessionHasMore = true;
	private Mode cessionCurMode = Mode.PULL_FROM_START;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transfer_record);
		parseUrl();
		initTitle();
		initView();
	}

	private void parseUrl() {
		// TODO Auto-generated method stub
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = this.getApplicationContext().getActionUrl(G.GCK_API_GET_CREDIT_RECENTLY);
		} else {
			url = G.URL_GET_CREDIT_RECENTLY;
		}
	}
	
	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		title.setTitle(R.string.transfer_record);
		title.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TransferRecordActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(transferRecordList);
		} else {
			createFlag = false;
		}
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		transferRecordList = (PullToRefreshListView) findViewById(R.id.transfer_record_list);
		adapter = new TransferRecordAdapter(TransferRecordActivity.this, R.layout.transfer_record_item, item);
		transferRecordList.setAdapter(adapter);
		initListView();
		
		errNetLayout=(RelativeLayout)findViewById(R.id.errNetLayout);
		networkLoad=(TextView)findViewById(R.id.networkload);		
		networkText=(TextView)findViewById(R.id.networktext);
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView();
				transferRecordList.setLoadRefreshing();
			}
		});
		
		mHandler.postDelayed(new Runnable() {
			public void run() {
				transferRecordList.setLoadRefreshing();
			}
		}, 500);
	}
	
	private void initListView(){
		//下拉刷新
		transferRecordList.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				errorListener.setRefreshView(transferRecordList);
				errorListener.setErrInterface(item.size()>0 ? null : TransferRecordActivity.this);
				if(cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurPage += 1;
					sendHttpGet(url+"?page="+cessionCurPage+"&pageSize="+PageSize, getP2PListListener);
				} else {
					cessionCurPage = 1;
					cessionHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(url+"?page="+cessionCurPage+"&pageSize="+PageSize, getP2PListListener);
						}
					}, 500);
				}
			}
		});
		//判断最后一列状态上拉刷新
		transferRecordList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if(cessionHasMore == true && !transferRecordList.isRefreshing()) {
					cessionCurMode = Mode.PULL_FROM_END;
					transferRecordList.setCurrentMode(cessionCurMode);
					transferRecordList.setLoadRefreshing();
				}
			}
			
		});
	}
	
	private Listener<JSONObject> getP2PListListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				if (response.getInt("code") == 0) {
					//JSONObject response1 = response.getJSONObject("creditItemsCount");
					if(cessionCurPage==1) {
						item.clear();
						creditItemsCount = response.getString("creditItemsCount");
						item.add(new CessionTradeInfo(creditItemsCount));
					}
					JSONArray dataArray = response.getJSONArray("creditItems");
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataRoot = dataArray.getJSONObject(j);
						item.add(new CessionTradeInfo(
								dataRoot.getString("trade_time"), 
								dataRoot.getString("duein_capital"), 
								dataRoot.getString("project_apr")));
					}

					if(dataArray.length() < PageSize) {
						cessionHasMore = false;
					}
					adapter.notifyDataSetChanged();
				} else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(TransferRecordActivity.this);
        		} else {
        			if(item.size() <= 0) {
        				showErrReq();
        			}
        			dialog =KdlcDialog.showInformDialog(TransferRecordActivity.this ,response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
    			if(item.size() <= 0) {
    				showErrReq();
    			} else {
    				KdlcDialog.showBottomToast("");
    			}
			}
			if(transferRecordList != null) {
				transferRecordList.onRefreshComplete();
				if(cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurMode = Mode.PULL_FROM_START;
					transferRecordList.setCurrentMode(cessionCurMode);
				}
			}
		}
	};

	@Override
	public void showNoNetwork() {
		// TODO Auto-generated method stub
		showNetView(false);
	}

	@Override
	public void showErrReq() {
		// TODO Auto-generated method stub
		showNetView(true);
	}
	
	private void showNetView(boolean err) {
		transferRecordList.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
	private void showListView() {
		transferRecordList.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
	}
}
