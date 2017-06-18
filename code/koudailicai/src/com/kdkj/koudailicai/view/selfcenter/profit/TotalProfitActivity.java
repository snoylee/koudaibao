package com.kdkj.koudailicai.view.selfcenter.profit;

import java.util.ArrayList;

import com.kdkj.koudailicai.domain.TotalSelfProfitInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.adapter.TotalSelfProfitInfoAdapter;
public class TotalProfitActivity extends BaseActivity implements HttpErrorInterface{
	private String LOG_TAG=TotalProfitActivity.class.getName();
	private String getKdbDetailUrl;
	private PullToRefreshListView listview;
	private TotalSelfProfitInfoAdapter adapter;
	private ArrayList<TotalSelfProfitInfo> item = new ArrayList<TotalSelfProfitInfo>();
	private String total_profits;
	private TitleView titleview;
	private int cessionCurPage = 1;
	private int p2pPageSize = 15;
	private boolean cessionHasMore = true;
	private Mode cessionCurMode = Mode.PULL_FROM_START;
	private Handler mHandler = new Handler();

	private RelativeLayout errNetLayout;
	private TextView networkLoad;
	private TextView networkText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.totalselflist);
		parseUrl();
		initeView();
		initetitle();
	}

	private void initeView() {
		titleview = (TitleView)findViewById(R.id.profittitle);
		listview = (PullToRefreshListView) findViewById(R.id.totallistview);
		adapter = new TotalSelfProfitInfoAdapter(TotalProfitActivity.this, R.layout.totalselfprofitinfo, item);
		listview.setAdapter(adapter);
		cessionRefresh(); 
		
		errNetLayout=(RelativeLayout)findViewById(R.id.errNetLayout);
		networkLoad=(TextView)findViewById(R.id.networkload);		
		networkText = (TextView) findViewById(R.id.networktext);
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView();
				listview.setLoadRefreshing();
			}
		});
		
		mHandler.postDelayed(new Runnable() {
			public void run() {
				listview.setLoadRefreshing();
			}
		}, 500);
	}
    
	private void cessionRefresh(){
		//下拉刷新
		listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				errorListener.setRefreshView(listview);
				errorListener.setErrInterface(item.size() > 0 ? null : TotalProfitActivity.this);
				
				if(cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurPage += 1;
					sendHttpGet(getKdbDetailUrl+"?page="+cessionCurPage+"&pageSize="+p2pPageSize, getP2PListListener);
				} else {
					cessionCurPage = 1;
					cessionHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(getKdbDetailUrl+"?page="+cessionCurPage+"&pageSize="+p2pPageSize, getP2PListListener);
						}
					}, 500);
				}
			}
		});
		
		//判断最后一列状态上拉刷新
		listview.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if(cessionHasMore == true && !listview.isRefreshing()) {
					cessionCurMode = Mode.PULL_FROM_END;
					listview.setCurrentMode(cessionCurMode);
					listview.setLoadRefreshing();
				}
			}
		});
		
		listview.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int i, long l) {
				// TODO Auto-generated method stub
				if(i != 1){					
					Intent intent=new Intent(TotalProfitActivity.this,ProfitDetailActivity.class);
					String profitId=item.get(i-1).getItemId();
					LogUtil.info("选择了"+i+item.get(i-1).getProject_name()+"id 是"+profitId);
					intent.putExtra("profitId", profitId);
					startActivity(intent);
				}
			}
		});
	}
	
		
	private void initetitle() {
		titleview.setTitle(R.string.totalprofit);
		titleview.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TotalProfitActivity.this.finish();
			}
		});
		titleview.setLeftImageButton(R.drawable.back);
		titleview.setLeftTextButton("返回");
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			getKdbDetailUrl = getApplicationContext().getActionUrl(
							  G.GCK_API_GET_TOTAL_PROFIT);
		} else {
			getKdbDetailUrl = G.URL_GET_TOTAL_PROFIT;
		}
	}
	private Listener<JSONObject> getP2PListListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				if (response.getInt("code") == 0) {					
					if(cessionCurPage ==1) {
						item.clear();	
						JSONObject response1 = response.getJSONObject("data");
						total_profits = response1.getString("total_profits");
						item.add(new TotalSelfProfitInfo(total_profits));
					}
					JSONArray dataArray = response.getJSONArray("profits");
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataRoot = dataArray.getJSONObject(j);
						item.add(new TotalSelfProfitInfo(dataRoot.getString("id"),dataRoot.getString("project_name"),dataRoot.getString("profits")));
					}
					if(dataArray.length() <p2pPageSize) {
						cessionHasMore = false; 
					}
					adapter.notifyDataSetChanged();
				} else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(TotalProfitActivity.this);
        		} else {
    				if(item.size() <= 0) {
    					showErrReq();
    				}
    				dialog=KdlcDialog.showInformDialog(TotalProfitActivity.this,response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if(item.size() <= 0) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}				
			}
			if(listview != null) {
				listview.onRefreshComplete();
				if(cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurMode = Mode.PULL_FROM_START;
					listview.setCurrentMode(cessionCurMode);
				}
			}
			
		}
	
	};
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(listview);
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
		listview.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}

	private void showListView() {
		listview.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
	}
	
}
