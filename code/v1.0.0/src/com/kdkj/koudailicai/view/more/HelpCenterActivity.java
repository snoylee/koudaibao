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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.HelpCenterAdapter;
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
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.domain.ActiveCenterInfo;
import com.kdkj.koudailicai.domain.HelpCenterInfo;
public class HelpCenterActivity extends BaseActivity implements HttpErrorInterface {
	private String LOG_TAG = HelpCenterActivity.class.getName();
	private TitleView helptitle;
	private String url;
	private PullToRefreshListView helpListView;
	private HelpCenterAdapter helpAdapter;
	private ArrayList<HelpCenterInfo> item = new ArrayList<HelpCenterInfo>();
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
		setContentView(R.layout.activity_help_center);
		parseUrl();
		init();
		inittitle();
		initContents();
		
	}
	
	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = getApplicationContext().getActionUrl(
					G.GCK_API_GET_PAGE_HELP_LIST);
		} else {
			url = G.URL_GET_PAGE_HELP_LIST;
		}
	}
	
	private void inittitle() {
		helptitle = (TitleView) findViewById(R.id.helptitle);
		helptitle.setTitle(R.string.more_help_center);
		helptitle.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				HelpCenterActivity.this.finish();
			}
		});
		helptitle.setLeftImageButton(R.drawable.back);
		helptitle.setLeftTextButton("返回");
	}
	

	private void init() {
		helpListView = (PullToRefreshListView) findViewById(R.id.helpListView);
		helpAdapter = new HelpCenterAdapter(HelpCenterActivity.this,
				R.layout.activity_help_info, item);
		helpListView.setAdapter(helpAdapter);
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
				helpListView.setLoadRefreshing();
			}
		});
	}
	
	private void initContents() {
		List<HelpCenterInfo> transferInfoDBList = KDLCApplication.app.kdDb.findAll(HelpCenterInfo.class);
		if (transferInfoDBList != null && transferInfoDBList.size() != 0) {
			item.addAll(transferInfoDBList);
		}
		mHandler.postDelayed(new Runnable() {
			public void run() {
				helpListView.setLoadRefreshing();
			}
		}, 500);
	
	}

	private void cessionRefresh() {
		// 下拉刷新
		helpListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				errorListener.setRefreshView(helpListView);
				errorListener.setErrInterface(item.size()>0 ? null : HelpCenterActivity.this);
				if (cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurPage += 1;
					sendHttpGet(url + "?page=" + cessionCurPage + "&pageSize="
								+ p2pPageSize, getP2PListListener);
				} else {
					cessionCurPage = 1;
					cessionHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(url + "?page=" + cessionCurPage + "&pageSize="
									+ p2pPageSize, getP2PListListener);
						}
					}, 500);
				}
			}
		});

		// 判断最后一列状态上拉刷新
		helpListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if (cessionHasMore == true && !helpListView.isRefreshing()) {
					cessionCurMode = Mode.PULL_FROM_END;
					helpListView.setCurrentMode(cessionCurMode);
					helpListView.setLoadRefreshing();
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
					if (cessionCurPage == 1) {
						item.clear();
					}
					JSONArray dataArray = response.getJSONArray("articles");
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataRoot = dataArray.getJSONObject(j);
						item.add(new HelpCenterInfo(dataRoot.getString("id"),
								dataRoot.getString("title"), dataRoot
										.getString("content")));
					}
					if (dataArray.length() < p2pPageSize) {
						cessionHasMore = false;
					}
					helpAdapter.notifyDataSetChanged();
					if (cessionCurPage == 1) {
						if(Tool.hasCacheData(HelpCenterInfo.class))
							KdlcDB.deleteAllByClass(HelpCenterInfo.class);
						KdlcDB.addByEntityList(item);
					}
					if(item.size() <= 0) {
						showNoDataView();
					}
				} else {
					if(item.size() <= 0) {
						showErrReq();
					}
					dialog =KdlcDialog.showInformDialog(HelpCenterActivity.this ,response.getString("message"));
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
			if (helpListView != null) {
				helpListView.onRefreshComplete();
				if (cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurMode = Mode.PULL_FROM_START;
					helpListView.setCurrentMode(cessionCurMode);
				}
			}

		}

	};
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(helpListView);
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
		helpListView.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
	private void showNoDataView() {
		helpListView.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
	}
	
	private void showListView() {
		helpListView.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);	
	}

}
