package com.kdkj.koudailicai.view.selfcenter.accountremain;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.WithdrawListAdapter;
import com.kdkj.koudailicai.domain.WithdrawListInfo;
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
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.holdasset.KdbRollOutListActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.TransferPasswordActivity;

public class WithdrawListActivity extends BaseActivity implements
		HttpErrorInterface {
	private TitleView title;
	private PullToRefreshListView withdrawListView;
	private List<WithdrawListInfo> item = new ArrayList<WithdrawListInfo>();
	private WithdrawListAdapter adapter;
	private String url;
	private int PageSize = 15;
	private Handler mHandler = new Handler();
	private int cessionCurPage = 1;
	private boolean cessionHasMore = true;
	private Mode cessionCurMode = Mode.PULL_FROM_START;
	//网络出错页面
	private RelativeLayout errNetLayout;
	private RelativeLayout noDataLayout;
	private TextView networkLoad;
	private TextView networkText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_listview);
		purseUrl();
		initTitle();
		initView();		
		initContents();
	}

	private void purseUrl() {
		// TODO Auto-generated method stub
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = getApplicationContext().getActionUrl(G.GCK_API_GET_ACCOUNT_WITHDRAW_LOG);
		} else {
			url = G.URL_GET_ACCOUNT_WITHDRAW_LOG;
		}
	}
	
	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.withdrawlist);		
		title.setTitle("提现记录");
		title.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WithdrawListActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}

	private void initView() {
		// TODO Auto-generated method stub
		withdrawListView = (PullToRefreshListView) findViewById(R.id.withdrawListView);
		adapter = new WithdrawListAdapter(getApplicationContext(),R.layout.activity_withdraw_item, item);
		withdrawListView.setAdapter(adapter);
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
				withdrawListView.setLoadRefreshing();
			}
		});
	}
	
	private void initContents() {
		mHandler.postDelayed(new Runnable() {
			public void run() {
				withdrawListView.setLoadRefreshing();
			}
		}, 500);
	}
	
	private void cessionRefresh() {
		// 下拉刷新
		withdrawListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(PullToRefreshBase<ListView> refreshView) {
						errorListener.setRefreshView(withdrawListView);
						errorListener.setErrInterface(item.size()>0 ? null : WithdrawListActivity.this);
						if (cessionCurMode.equals(Mode.PULL_FROM_END)) {
							cessionCurPage += 1;
							sendHttpGet(url + "?page=" + cessionCurPage + "&pageSize=" + PageSize, getListListener);
						} else {
							cessionCurPage = 1;
							cessionHasMore = true;
							mHandler.postDelayed(new Runnable() {
								public void run() {
									sendHttpGet(url + "?page=" + cessionCurPage + "&pageSize=" + PageSize, getListListener);
								}
							}, 500);
						}
					}
				});
		
		// 判断最后一列状态上拉刷新
		withdrawListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if (cessionHasMore == true && !withdrawListView.isRefreshing()) {
					cessionCurMode = Mode.PULL_FROM_END;
					withdrawListView.setCurrentMode(cessionCurMode);
					withdrawListView.setLoadRefreshing();
				}
			}
		});
	}

	private Listener<JSONObject> getListListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				if (response.getInt("code") == 0) {
					if (cessionCurPage == 1) {
						item.clear();
					}
					JSONArray dataArray = response.getJSONArray("data");
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataRoot = dataArray.getJSONObject(j);
						item.add(new WithdrawListInfo(dataRoot
								.getString("money"), dataRoot
								.getString("statusLabel"), dataRoot
								.getString("created_at")));
					}
					if(item.size() <= 0) {
						showNoDataView();
					}
					if (dataArray.length() < PageSize) {
						cessionHasMore = false;
					}
					adapter.notifyDataSetChanged();
				} else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(WithdrawListActivity.this);
        		} else {
					if(item.size() <= 0) {
						showErrReq();
					}
					dialog=KdlcDialog.showInformDialog(WithdrawListActivity.this ,response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if(item.size() <= 0) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}		
			}
			if (withdrawListView != null) {
				withdrawListView.onRefreshComplete();
				if (cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurMode = Mode.PULL_FROM_START;
					withdrawListView.setCurrentMode(cessionCurMode);
				}
			}

		}

	};



	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(withdrawListView);
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
		withdrawListView.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
	private void showNoDataView() {
		withdrawListView.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
	}
	
	private void showListView() {
		withdrawListView.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);	
	}
}
