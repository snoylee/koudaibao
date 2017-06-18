package com.kdkj.koudailicai.view.selfcenter.profit;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.kdkj.koudailicai.adapter.EarningsYesterdayAdapter;
import com.kdkj.koudailicai.domain.EarningsYesterdayInfo;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.invest.InvestAccountActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.KdbRollOutListActivity;
public class LastDayProfitsActivity extends BaseActivity implements HttpErrorInterface{
	private TitleView title;
	private PullToRefreshListView lvEarnings;
	private String url;
	private String lastdate;
	private String lastday_profits;
	private ArrayList<EarningsYesterdayInfo> item = new ArrayList<EarningsYesterdayInfo>();
	private EarningsYesterdayAdapter adapter;

	private RelativeLayout errNetLayout;
	private TextView networkLoad;
	private TextView networkText;
	
	private int pageSize = 15;
	private Handler mHandler = new Handler();
	private int curPage = 1;
	private boolean cessionHasMore = true;
	private Mode cessionCurMode = Mode.PULL_FROM_START;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_earnings_yesterday);
		parseUrl();
		initTitle();
		initView();
	}
	
	private void parseUrl() {
		// TODO Auto-generated method stub
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = getApplicationContext().getActionUrl(
					G.GCK_API_GET_LASTDAY_PROFITS);
		} else {
			url = G.URL_GET_ACCOUNT_LASTDAY_PROFITS;
		}
	}
	
	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		title.setTitle("昨日收益");
		title.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LastDayProfitsActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}
	
	private void initView() {
		lvEarnings = (PullToRefreshListView) findViewById(R.id.lv_earnings);
		adapter = new EarningsYesterdayAdapter(getApplicationContext(), R.layout.activity_earnings_yesterday_info, item);
		lvEarnings.setAdapter(adapter);
		cessionRefresh(); 
		
		errNetLayout=(RelativeLayout)findViewById(R.id.errNetLayout);
		networkLoad=(TextView)findViewById(R.id.networkload);		
		networkText = (TextView) findViewById(R.id.networktext);
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView();
				lvEarnings.setLoadRefreshing();
			}
		});
		
		mHandler.postDelayed(new Runnable() {
			public void run() {
				lvEarnings.setLoadRefreshing();
			}
		}, 500);
	}
	private void cessionRefresh(){
		//下拉刷新
		lvEarnings.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				errorListener.setRefreshView(lvEarnings);
				errorListener.setErrInterface(item.size() > 0 ? null : LastDayProfitsActivity.this);
				if(cessionCurMode.equals(Mode.PULL_FROM_END)) {
					curPage += 1;
					sendHttpGet(url+"?page="+curPage+"&pageSize="+pageSize, getListListener);
				} else {
					curPage = 1;
					cessionHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(url+"?page="+curPage+"&pageSize="+pageSize, getListListener);
						}
					}, 500);
				}
			}
		});
		//判断最后一列状态上拉刷新
	/*	lvEarnings.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if(cessionHasMore == true && !lvEarnings.isRefreshing()) {
					cessionCurMode = Mode.PULL_FROM_END;
					lvEarnings.setCurrentMode(cessionCurMode);
					lvEarnings.setLoadRefreshing();
				} else {
					
				}
			}
			
		});*/
	}
	
	private Listener<JSONObject> getListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				if (response.getInt("code") == 0) {
					JSONObject response1 = response.getJSONObject("data");
					if(curPage == 1) {
						item.clear();
						lastdate = response1.getString("lastdate");
						lastday_profits = response1.getString("lastday_profits");
						item.add(new EarningsYesterdayInfo(lastdate,lastday_profits));
					}
					JSONArray dataArray = response.getJSONArray("profits");
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataRoot = dataArray.getJSONObject(j);
						item.add(new EarningsYesterdayInfo(
								dataRoot.getString("project_name"), 
								dataRoot.getString("date"),
								dataRoot.getString("lastday_profits")));
					}
					if(dataArray.length() < pageSize) {
						cessionHasMore = false;
					}
					adapter.notifyDataSetChanged();
				} else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(LastDayProfitsActivity.this);
        		} else {
    				if(item.size() <= 0) {
    					showErrReq();
    				}
    				dialog=KdlcDialog.showInformDialog(LastDayProfitsActivity.this,response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if(item.size() <= 0) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
			if(lvEarnings != null) {
				lvEarnings.onRefreshComplete();
				if(cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurMode = Mode.PULL_FROM_START;
					lvEarnings.setCurrentMode(cessionCurMode);
				}
			}

		}
	
	};
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(lvEarnings);
		} else {
			createFlag = false;
		}
	}

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
		lvEarnings.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}

	private void showListView() {
		lvEarnings.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
	}
}
