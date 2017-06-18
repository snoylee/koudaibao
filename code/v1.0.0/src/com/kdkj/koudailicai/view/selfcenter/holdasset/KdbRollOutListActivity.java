package com.kdkj.koudailicai.view.selfcenter.holdasset;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.KdbRollOutListAdapter;
import com.kdkj.koudailicai.domain.KdbRollOutListInfo;
import com.kdkj.koudailicai.domain.ProductListP2PInfo;
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
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.lib.http.MyHttpClient;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;

public class KdbRollOutListActivity extends BaseActivity implements HttpErrorInterface {
	private String LOG_TAG = KdbRollOutListActivity.class.getName();
	private TitleView rolloutlist;
	private PullToRefreshListView rolloutListView;
	private String url;
	private ArrayList<KdbRollOutListInfo> rolloutInfoList = new ArrayList<KdbRollOutListInfo>();
	private KdbRollOutListAdapter rollAdapter;
	private Handler mHandler = new Handler();
	private int cessionCurPage = 1;
	private int p2pPageSize = 15;
	private boolean cessionHasMore = true;
	private Mode cessionCurMode = Mode.PULL_FROM_START;
	
	private RelativeLayout errNetLayout;
	private RelativeLayout noDataLayout;
	private TextView networkLoad;
	private TextView networkText;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rollout_listview);
		parseUrl();
		findViews();
		initTitle();
	}

	private void findViews() {
		rolloutlist = (TitleView) findViewById(R.id.rolloutlist);
		rolloutListView = (PullToRefreshListView) findViewById(R.id.rolloutListView);
		rollAdapter = new KdbRollOutListAdapter(KdbRollOutListActivity.this, R.layout.activity_rollout_listinfo, rolloutInfoList);
		rolloutListView.setAdapter(rollAdapter);
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
				rolloutListView.setLoadRefreshing();
			}
		});
		
		mHandler.postDelayed(new Runnable() {
			public void run() {
				rolloutListView.setLoadRefreshing();
			}
		}, 500);
	}

	private void cessionRefresh() {
		// 下拉刷新
		rolloutListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				errorListener.setRefreshView(rolloutListView);
				errorListener.setErrInterface(rolloutInfoList.size() > 0 ? null : KdbRollOutListActivity.this);
				if (cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurPage += 1;
					sendHttpGet(url + "?page=" + cessionCurPage + "&pageSize=" + p2pPageSize,
								getP2PListListener);

				} else {
					cessionCurPage = 1;
					cessionHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(url + "?page=" + cessionCurPage + "&pageSize=" + p2pPageSize,
									getP2PListListener);
						}
					}, G.AUTO_LOAD_DELAYED);
				}
			}
		});

		// 判断最后一列状态上拉刷新
		rolloutListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if (cessionHasMore == true && !rolloutListView.isRefreshing()) {
					cessionCurMode = Mode.PULL_FROM_END;
					rolloutListView.setCurrentMode(cessionCurMode);
					rolloutListView.setLoadRefreshing();
				}
			}
		});
	}

	private Listener<JSONObject> getP2PListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				Log.d(LOG_TAG, "response:"+response.toString());
				if (response.getInt("code") == 0) {
					if (cessionCurPage == 1) {
						rolloutInfoList.clear();
					}
					JSONArray dataArray = response.getJSONArray("rollouts");
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataRoot = dataArray.getJSONObject(j);
						rolloutInfoList.add(new KdbRollOutListInfo(dataRoot
								.getString("money"), dataRoot
								.getString("created_at")));
					}
					if(rolloutInfoList.size() <= 0) {
						Log.d(LOG_TAG, "rolloutInfoList < 0:");
						showNoDataView();
					}
					if (dataArray.length() < p2pPageSize) {
						cessionHasMore = false;
					}
					rollAdapter.notifyDataSetChanged();
				} else if (response.getInt("code") == -2) {
					dialog = KdlcDialog.showLoginDialog(KdbRollOutListActivity.this);
				} else {
					if(rolloutInfoList.size() <= 0) {
						showErrReq();
					}
					dialog =KdlcDialog.showInformDialog(KdbRollOutListActivity.this, response.getString("message"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				if(rolloutInfoList.size() <= 0) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
			if (rolloutListView != null) {
				rolloutListView.onRefreshComplete();
				if (cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurMode = Mode.PULL_FROM_START;
					rolloutListView.setCurrentMode(cessionCurMode);
				}
			}
		}
	};

	private void initTitle() {
		rolloutlist.setTitle(R.string.rollout_list);
		rolloutlist.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				KdbRollOutListActivity.this.finish();
			}
		});
		rolloutlist.setLeftImageButton(R.drawable.back);
		rolloutlist.setLeftTextButton("返回");

	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = this.getApplicationContext().getActionUrl(G.GCK_API_GET_ROLLOUT_LIST);
		} else {
			url = G.URL_GET_KDB_ROLLOUT_LIST;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(rolloutListView);
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
		rolloutListView.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
	private void showNoDataView() {
		rolloutListView.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
	}
	
	private void showListView() {
		rolloutListView.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);	
	}

}
