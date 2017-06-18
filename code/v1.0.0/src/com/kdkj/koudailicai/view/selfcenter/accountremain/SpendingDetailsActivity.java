package com.kdkj.koudailicai.view.selfcenter.accountremain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.PaymentDetailsAdapter;
import com.kdkj.koudailicai.domain.PaymentDetailsInfo;
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
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
public class SpendingDetailsActivity extends BaseActivity implements HttpErrorInterface{

	private TitleView title;
	private PullToRefreshListView lvPaymentDetail;
	private ArrayList<PaymentDetailsInfo> item = new ArrayList<PaymentDetailsInfo>();
	private PaymentDetailsAdapter adapter;
	private String url;
	
	private int PageSize = 15;
	private Handler mHandler = new Handler();
	private int cessionCurPage = 20;
	private int type = 2;
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
		setContentView(R.layout.activity_payment_details);
		parseUrl();
		findView();
		initTitle();
		initContents();
	}
	
	private void initContents() {
		mHandler.postDelayed(new Runnable() {
			public void run() {
				lvPaymentDetail.setLoadRefreshing();
			}
		}, 500);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(lvPaymentDetail);
		} else {
			createFlag = false;
		}
	}
	
	private void parseUrl() {
		// TODO Auto-generated method stub
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = getApplicationContext().getActionUrl(G.GCK_API_GET_ACCOUNT_REMAIN_LIST);
		} else {
			url = G.URL_GET_ACCOUNT_REMAIN_LIST;
		}
	}
	
	private void findView() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		lvPaymentDetail = (PullToRefreshListView) findViewById(R.id.lv_payment_details);
		adapter = new PaymentDetailsAdapter(getApplicationContext(), R.layout.activity_payment_details_info, item);
		lvPaymentDetail.setAdapter(adapter);
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
				lvPaymentDetail.setLoadRefreshing();
			}
		});
	}
	
	private void cessionRefresh(){
		//下拉刷新
		lvPaymentDetail.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				errorListener.setRefreshView(lvPaymentDetail);
				errorListener.setErrInterface(item.size()>0 ? null : SpendingDetailsActivity.this);
				if(cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurPage += 1;
					sendHttpGet(url+"?type="+type+"&page="+cessionCurPage+"&pageSize="+PageSize, getListListener);
				} else {
					cessionCurPage = 1;
					cessionHasMore = true;
					mHandler.postDelayed(new Runnable() {
						public void run() {
							sendHttpGet(url+"?type="+type+"&page="+cessionCurPage+"&pageSize="+PageSize, getListListener);
						}
					}, 500);
				}
			}
		});
		//判断最后一列状态上拉刷新
		lvPaymentDetail.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if(cessionHasMore == true && !lvPaymentDetail.isRefreshing()) {
					cessionCurMode = Mode.PULL_FROM_END;
					lvPaymentDetail.setCurrentMode(cessionCurMode);
					lvPaymentDetail.setLoadRefreshing();
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
					if(cessionCurPage==1) {
						item.clear();
					}
					JSONArray dataArray = response.getJSONArray("data");
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataRoot = dataArray.getJSONObject(j);
						item.add(new PaymentDetailsInfo(
								dataRoot.getString("title"),
								dataRoot.getString("created_at"),
								dataRoot.getString("tag"), 
								dataRoot.getString("money")));
					}
					if(dataArray.length() <PageSize) {
						cessionHasMore = false;
					}
					if(item.size() <= 0) {
						showNoDataView();
					}
					adapter.notifyDataSetChanged();
				}else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(SpendingDetailsActivity.this);
        		}  
				else {
					if(item.size() <= 0) {
						showErrReq();
					}
					dialog =KdlcDialog.showInformDialog(SpendingDetailsActivity.this,response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if(item.size() <= 0) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}				
			}
			if(lvPaymentDetail != null) {
				lvPaymentDetail.onRefreshComplete();
				if(cessionCurMode.equals(Mode.PULL_FROM_END)) {
					cessionCurMode = Mode.PULL_FROM_START;
					lvPaymentDetail.setCurrentMode(cessionCurMode);
				}
			}

		}
	
	};
	
	private void initTitle() {
		// TODO Auto-generated method stub
		title.setTitle("支出明细");
		title.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SpendingDetailsActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
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
		lvPaymentDetail.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
	private void showNoDataView() {
		lvPaymentDetail.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
	}
	
	private void showListView() {
		lvPaymentDetail.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);	
	}

}
