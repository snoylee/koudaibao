package com.kdkj.koudailicai.view.more;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.ActiveCenterAdapter;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.accountremain.WithdrawActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.WithdrawListActivity;
import com.kdkj.koudailicai.view.selfcenter.bankcard.ConfirmIdentityActivity;
import com.kdkj.koudailicai.domain.ActiveCenterInfo;
import com.kdkj.koudailicai.domain.CessionTradeInfo;
public class ActiveCenterActivity extends BaseActivity implements HttpErrorInterface{
	private String LOG_TAG = ActiveCenterActivity.class.getName();
	private PullToRefreshListView activeListView;
	private TitleView activetitle;
	private String urlString;
	private ActiveCenterAdapter ActiveAdapter;
	private ArrayList<ActiveCenterInfo> item = new ArrayList<ActiveCenterInfo>();
	private int cessionCurPage = 1;
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
		setContentView(R.layout.activity_active_center);
		parseUrl();
		init();
		inittitle();
		initContents();
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			urlString = getApplicationContext().getActionUrl(G.GCK_API_GET_ACTIVITYS);
		} else {
			urlString = G.URL_GET_GET_ACTIVITYS;
		}
	}

	private void init() {
		activetitle = (TitleView) findViewById(R.id.activetitle);
		activeListView = (PullToRefreshListView) findViewById(R.id.activeListView);
		ActiveAdapter = new ActiveCenterAdapter(ActiveCenterActivity.this, R.layout.activity_active_centerinfo, item);
		activeListView.setAdapter(ActiveAdapter);
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
				activeListView.setLoadRefreshing();
			}
		});
	}
	
	private void initContents() {
		List<ActiveCenterInfo> activieList = KDLCApplication.app.kdDb.findAll(ActiveCenterInfo.class);
		if (activieList != null && activieList.size() != 0) {
			Log.d(LOG_TAG, "db size:" + activieList.size());
			item.addAll(activieList);
		}
		mHandler.postDelayed(new Runnable() {
			public void run() {
				activeListView.setLoadRefreshing();
			}
		}, 500);
	
	}

	private void cessionRefresh() {
		// 下拉刷新
		activeListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				errorListener.setRefreshView(activeListView);
				errorListener.setErrInterface(item.size()>0 ? null : ActiveCenterActivity.this);
				if (cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurPage += 1;
					sendHttpGet(urlString, getP2PListListener);
				} else {
					cessionCurPage = 1;
					cessionHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(urlString, getP2PListListener);							}
					}, 500);
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
					item.clear();
					JSONArray dataArray = response.getJSONArray("activityList");
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataRoot = dataArray.getJSONObject(j);
						item.add(new ActiveCenterInfo(dataRoot.getString("id"),dataRoot
								.getString("thumbnail"), Tool.unixTimeToDate(
								dataRoot.getString("created_at"),
								"yyyy-MM-dd HH:mm"), dataRoot
								.getString("abstract"),dataRoot.getString("title")));
					}
				    ActiveAdapter.notifyDataSetChanged();
				    if(Tool.hasCacheData(ActiveCenterInfo.class))
				    	KdlcDB.deleteAllByClass(ActiveCenterInfo.class);
					if(item.size() <= 0) {
						showNoDataView();
					} else {
						KdlcDB.addByEntityList(item);
					}
				} else {
					if(item.size() <= 0) {
						showErrReq();
					}
					dialog =KdlcDialog.showInformDialog(ActiveCenterActivity.this ,response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(item.size() <= 0) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
			if (activeListView != null) {
				activeListView.onRefreshComplete();
				if (cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurMode = Mode.PULL_FROM_START;
					activeListView.setCurrentMode(cessionCurMode);
				}
			}

		}

	};

	private void inittitle() {
		activetitle.setTitle(R.string.more_active_center);
		activetitle.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ActiveCenterActivity.this.finish();
			}
		});
		activetitle.setLeftImageButton(R.drawable.back);
		activetitle.setLeftTextButton("返回");

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(activeListView);
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
		activeListView.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
	private void showNoDataView() {
		activeListView.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
	}
	
	private void showListView() {
		activeListView.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);	
	}
}

