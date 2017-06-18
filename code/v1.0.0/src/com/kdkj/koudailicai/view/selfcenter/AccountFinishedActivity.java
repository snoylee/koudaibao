package com.kdkj.koudailicai.view.selfcenter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.AccountFinishedAdapter;
import com.kdkj.koudailicai.domain.AccountFinishedInfo;
import com.kdkj.koudailicai.domain.KdbRollOutListInfo;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.WithdrawListActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.KdbRollOutListActivity;
import com.kdkj.koudailicai.view.selfcenter.profit.ProfitDetailActivity;
import com.kdkj.koudailicai.view.selfcenter.profit.TotalProfitActivity;

public class AccountFinishedActivity extends BaseActivity implements HttpErrorInterface{
	private String url;
	private TitleView finishTitle;
	private PullToRefreshListView finishListView;
	private AccountFinishedAdapter finishedAdapter;
	private ArrayList<AccountFinishedInfo> finishInfoList = new ArrayList<AccountFinishedInfo>();
	private int cessionCurPage = 1;
	private int p2pPageSize = 15;
	private boolean cessionHasMore = true;
	private Mode cessionCurMode = Mode.PULL_FROM_START;
	private Handler mHandler = new Handler();
	//网络出错页面
	private RelativeLayout errNetLayout;
	private RelativeLayout noDataLayout;
	private TextView networkLoad;
	private TextView networkText;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_finished);
		parseUrl();
		findViews();
		initTitle();
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = this.getApplicationContext().getActionUrl(G.GCK_API_GET_ACCOUNT_FINISHED);
		} else {
			url = G.URL_GET_ACCOUNT_FINISHED;
		}
	}

	private void findViews() {
		finishListView = (PullToRefreshListView) findViewById(R.id.finishListView);
		finishedAdapter = new AccountFinishedAdapter(AccountFinishedActivity.this, R.layout.activity_acccount_finishinfo, finishInfoList);
		finishListView.setAdapter(finishedAdapter);
		finishListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {
				// TODO Auto-generated method stub
				if(finishInfoList.get(i-1).getItemId().equals("0"))
				{
					Log.v("ID", finishInfoList.get(i-1).getItemId());
				}else {
					Intent intent=new Intent(AccountFinishedActivity.this,ProfitDetailActivity.class);
					String profitId=finishInfoList.get(i-1).getItemId();
					Log.v("ID", profitId);
					intent.putExtra("profitId", profitId);
					startActivity(intent);	
				}
					
			}
		});
		cessionRefresh();
		
		errNetLayout = (RelativeLayout) findViewById(R.id.errNetLayout);
		noDataLayout = (RelativeLayout) findViewById(R.id.noDataLayout);
		networkLoad = (TextView) findViewById(R.id.networkload);
		networkText = (TextView) findViewById(R.id.networktext);
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView();
				finishListView.setLoadRefreshing();
			}
		});
		
		mHandler.postDelayed(new Runnable() {
			public void run() {
				finishListView.setLoadRefreshing();
			}
		}, 500);
	}
	
	private void initTitle() {
		finishTitle = (TitleView) findViewById(R.id.finishtitle);
		finishTitle.setTitle(R.string.finish_list);
		finishTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AccountFinishedActivity.this.finish();
			}
		});
		finishTitle.setLeftImageButton(R.drawable.back);
		finishTitle.setLeftTextButton("返回");

	}
	private void cessionRefresh() {
		// 下拉刷新
		finishListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				errorListener.setRefreshView(finishListView);
				errorListener.setErrInterface(finishInfoList.size()>0 ? null : AccountFinishedActivity.this);
				if (cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurPage += 1;
					sendHttpGet(url + "?page=" + cessionCurPage + "&pageSize="
								+ p2pPageSize, getFinishListListener);
				} else {
					cessionCurPage = 1;
					cessionHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(url + "?page=" + cessionCurPage + "&pageSize="
									    + p2pPageSize, getFinishListListener);
						}
					}, 500);
				}
			}
		});
		// 判断最后一列状态上拉刷新
		finishListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (cessionHasMore == true
								&& !finishListView.isRefreshing()) {
							cessionCurMode = Mode.PULL_FROM_END;
							finishListView.setCurrentMode(cessionCurMode);
							finishListView.setLoadRefreshing();
						} else {
						}
					}
				});
	}

	private Listener<JSONObject> getFinishListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				if (response.getInt("code") == 0) {
					if (cessionCurPage == 1) {
						finishInfoList.clear();
					}
					JSONArray dataArray = response.getJSONArray("projects");
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataRoot = dataArray.getJSONObject(j);
						finishInfoList.add(new AccountFinishedInfo(dataRoot
								.getString("profits_id"), dataRoot
								.getString("project_name"), dataRoot
								.getString("invest_money"), dataRoot
								.getString("status_label"),dataRoot.getString("updated_at")));
					}
					if(finishInfoList.size() <= 0) {
						showNoDataView();
					}
					if (dataArray.length() < p2pPageSize) {
						cessionHasMore = false;
					}
					finishedAdapter.notifyDataSetChanged();
				} else if (response.getInt("code") == -2) {
					dialog = KdlcDialog.showLoginDialog(AccountFinishedActivity.this);
				} else {
					if(finishInfoList.size() <= 0) {
						showErrReq();
					}
					dialog =KdlcDialog.showInformDialog(AccountFinishedActivity.this, response.getString("message"));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if(finishInfoList.size() <= 0) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}				
			}
			if (finishListView != null) {
				finishListView.onRefreshComplete();
				if (cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurMode = Mode.PULL_FROM_START;
					finishListView.setCurrentMode(cessionCurMode);
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
		finishListView.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
	private void showNoDataView() {
		finishListView.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
	}
	
	private void showListView() {
		finishListView.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);	
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(finishListView);
		} else{
			createFlag = false;
		}
	}

}
