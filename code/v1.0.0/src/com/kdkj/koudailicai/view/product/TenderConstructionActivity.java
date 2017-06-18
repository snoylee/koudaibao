package com.kdkj.koudailicai.view.product;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.kdkj.koudailicai.adapter.TenderConstructionInfoAdapter;
import com.kdkj.koudailicai.domain.TenderConstructionInfo;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.accountremain.IncomeDetailsActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.KdbRollOutListActivity;

public class TenderConstructionActivity extends BaseActivity implements
		HttpErrorInterface {
	private String LOG_TAG = TenderConstructionActivity.class.getName();
	// views
	private TitleView mTitle;
	// contents
	private String getAccountKdbTenderConstructionUrl;
	private String getPriductTenderConstructionUrl;
	private ArrayList<TenderConstructionInfo> itemTender = new ArrayList<TenderConstructionInfo>();
	private TenderConstructionInfoAdapter tenderConsHAdapter;
	private PullToRefreshListView tenderConstructionlistView;
	private int kdbPage = 1;
	private int kdbPageSize = 15;
	private boolean kdbHasMore = true;
	private Mode kdbCurMode = Mode.PULL_FROM_START;
	private long productId;
	private int productPage = 1;
	private int productPageSize = 15;
	private boolean productHasMore = true;
	private Mode productCurMode = Mode.PULL_FROM_START;
	private Handler mHandler = new Handler();
	//网络出错页面
	private RelativeLayout errNetLayout;
	private RelativeLayout noDataLayout;
	private TextView networkLoad;
	private TextView networkText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tender_construction);
		parseUrl();
		initTitle();
		slipCursor();
	}

	private void slipCursor() {
		tenderConstructionlistView = (PullToRefreshListView) findViewById(R.id.tender_construction_ListView);
		tenderConsHAdapter = new TenderConstructionInfoAdapter(
				TenderConstructionActivity.this,
				R.layout.trasaction_record_list_h, itemTender);
		tenderConstructionlistView.setAdapter(tenderConsHAdapter);
		errNetLayout = (RelativeLayout) findViewById(R.id.errNetLayout);
		noDataLayout = (RelativeLayout) findViewById(R.id.noDataLayout);
		networkLoad = (TextView) findViewById(R.id.networkload);
		networkText = (TextView) findViewById(R.id.networktext);
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView();
				tenderConstructionlistView.setLoadRefreshing();
			}
		});
		final Intent intype = getIntent();
		// 判断口袋包 和温州贷的区别 productId
		if (intype.getStringExtra("type").contains("kdb")) {
			setKdbListViewRefresh();// 设置刷新
		} else if (intype.getStringExtra("type").contains("product")) {
			productId = intype.getLongExtra("productId", 2);
			setProductInvestListViewRefresh();
		}

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tenderConstructionlistView.setLoadRefreshing();
			}
		}, 500);

	}

	// 设置tenderConstructionlistView
	private void setKdbListViewRefresh() {
		// 下拉刷新
		tenderConstructionlistView.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(PullToRefreshBase<ListView> refreshView) {
						errorListener.setRefreshView(tenderConstructionlistView);
						errorListener.setErrInterface(itemTender.size()>0 ? null : TenderConstructionActivity.this);
						if (kdbCurMode.equals(Mode.PULL_FROM_END)) {
							kdbPage += 1;
							sendHttpGet(getAccountKdbTenderConstructionUrl
									+ "?page=" + kdbPage + "&pageSize="
									+ kdbPageSize,
									getTenderConsKdbListListener);
						} else {
							kdbPage = 1;
							kdbHasMore = true;
							mHandler.postDelayed(new Runnable() {
								public void run() {
									sendHttpGet(getAccountKdbTenderConstructionUrl
											+ "?page=" + kdbPage + "&pageSize="
											+ kdbPageSize,
											getTenderConsKdbListListener);
								}
							}, 500);
						}
					}
				});
		// 判断最后一列状态上拉刷新
		tenderConstructionlistView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (kdbHasMore == true && !tenderConstructionlistView.isRefreshing()) {
							kdbCurMode = Mode.PULL_FROM_END;
							tenderConstructionlistView.setCurrentMode(kdbCurMode);
							tenderConstructionlistView.setLoadRefreshing();
						}
					}
				});
	}

	private Listener<JSONObject> getTenderConsKdbListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				if (response.getInt("code") == 0) {
					JSONArray list = response.getJSONArray("invests");
					if (kdbPage == 1) {
						// 清空原始数据
						itemTender.clear();
						itemTender.add(new TenderConstructionInfo());
					}
					for (int i = 0; i < list.length(); i++) {
						TenderConstructionInfo tenderConstructionInfo = new TenderConstructionInfo(
								list.getJSONObject(i).getString("id"),
								list.getJSONObject(i).getString("username"),
								list.getJSONObject(i).getString("invest_money"),
								list.getJSONObject(i).getString("created_at"),
								list.getJSONObject(i).getString("status"), list
										.getJSONObject(i).getString(
												"statusLabel"));
						itemTender.add(tenderConstructionInfo);
					}
					if (list.length() < kdbPageSize) {
						kdbHasMore = false;
					}
					if(itemTender.size() <= 1) {
						showNoDataView();
					}
					tenderConsHAdapter.notifyDataSetChanged();
				} else {
					if (itemTender.size() <= 1) {
						showErrReq();
					}
					dialog =KdlcDialog.showInformDialog(
							TenderConstructionActivity.this,
							response.getString("message"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(itemTender.size() <= 1) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
			if (tenderConstructionlistView != null) {
				tenderConstructionlistView.onRefreshComplete();
				if (kdbCurMode.equals(Mode.PULL_FROM_END)) {
					kdbCurMode = Mode.PULL_FROM_START;
					tenderConstructionlistView.setCurrentMode(kdbCurMode);
				}
			}
		}
	};

	// 设置tenderConstructionlistView
	private void setProductInvestListViewRefresh() {
		// 下拉刷新
		tenderConstructionlistView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(PullToRefreshBase<ListView> refreshView) {
						errorListener.setRefreshView(tenderConstructionlistView);
						errorListener.setErrInterface(itemTender.size()>0 ? null : TenderConstructionActivity.this);
						if (productCurMode.equals(Mode.PULL_FROM_END)) {
							productPage += 1;
							sendHttpGet(getPriductTenderConstructionUrl + "?id="
									+ productId + "&page=" + productPage + "&pageSize="
									+ productPageSize, getProductInvestListListener);
						} else {
							productPage = 1;
							productHasMore = true;
							mHandler.postDelayed(new Runnable() {
								public void run() {
									sendHttpGet(getPriductTenderConstructionUrl + "?id="
											+ productId + "&page=" + productPage + "&pageSize="
											+ productPageSize, getProductInvestListListener);
								}
							}, 500);
						}
					}
				});
		// 判断最后一列状态上拉刷新
		tenderConstructionlistView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (productHasMore == true && !tenderConstructionlistView.isRefreshing()) {
							productCurMode = Mode.PULL_FROM_END;
							tenderConstructionlistView
									.setCurrentMode(productCurMode);
							tenderConstructionlistView.setLoadRefreshing();
						}
					}
				});
	}

	private Listener<JSONObject> getProductInvestListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				if (response.getInt("code") == 0) {
					JSONArray list = response.getJSONArray("invests");
					if (productPage == 1) {
						itemTender.clear();
						itemTender.add(new TenderConstructionInfo());
					}
					for (int i = 0; i < list.length(); i++) {
						TenderConstructionInfo tenderConstructionInfo = new TenderConstructionInfo(
								list.getJSONObject(i).getString("id"),
								list.getJSONObject(i).getString("username"),
								list.getJSONObject(i).getString("invest_money"),
								list.getJSONObject(i).getString("created_at"),
								list.getJSONObject(i).getString("status"), list
										.getJSONObject(i).getString(
												"statusLabel"));
						itemTender.add(tenderConstructionInfo);
					}
					if (list.length() < productPageSize) {
						productHasMore = false;
					}
					if(itemTender.size() <= 1) {
						showNoDataView();
					}
					tenderConsHAdapter.notifyDataSetChanged();
				} else {
					if (itemTender.size() <= 1) {
						showErrReq();
					}
					dialog =KdlcDialog.showInformDialog(
							TenderConstructionActivity.this,
							response.getString("message"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(itemTender.size() <= 1) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
			if (tenderConstructionlistView != null) {
				tenderConstructionlistView.onRefreshComplete();
				if (productCurMode.equals(Mode.PULL_FROM_END)) {
					productCurMode = Mode.PULL_FROM_START;
					tenderConstructionlistView.setCurrentMode(productCurMode);
				}
			}
		}
	};

	private void initTitle() {
		mTitle = (TitleView) findViewById(R.id.title);
		mTitle.setTitle(R.string.tenderConsrtruction);
		mTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TenderConstructionActivity.this.finish();
			}
		});
		mTitle.setLeftImageButton(R.drawable.back);
		mTitle.setLeftTextButton("返回");
	}

	// 投资记录
	private void parseUrl() {
		if (KDLCApplication.app.isGlobalConfCompleted()) {
			getAccountKdbTenderConstructionUrl = KDLCApplication.app
					.getActionUrl(G.GCK_API_GET_KDB_INVEST_LIST);
			getPriductTenderConstructionUrl = KDLCApplication.app
					.getActionUrl(G.GCK_API_GET_PRODUC_TINVEST_CESSION_INVEST);
		} else {
			getAccountKdbTenderConstructionUrl = G.URL_GET_KDB_INVEST_LIST;
			getPriductTenderConstructionUrl = G.URL_GET_PRODUC_TINVEST_CESSION_INVEST;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(tenderConstructionlistView);
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
		tenderConstructionlistView.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
	private void showNoDataView() {
		tenderConstructionlistView.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
	}
	
	private void showListView() {
		tenderConstructionlistView.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);	
	}
}
