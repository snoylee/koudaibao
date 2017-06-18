package com.kdkj.koudailicai.view.selfcenter.bankcard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;












import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.adapter.BankListInfoAdapter;
import com.kdkj.koudailicai.domain.BankSupportInfo;
import com.kdkj.koudailicai.domain.ProductListP2PInfo;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshListView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.product.KdbDetailActivity;
import com.kdkj.koudailicai.view.product.ProductDetailActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.IncomeDetailsActivity;

public class ChooseBankNameActivity extends BaseActivity implements HttpErrorInterface {

	private TitleView chooseBankTitle;
	private PullToRefreshListView bankListView;
	private BankListInfoAdapter bankListAdapter;
	private Handler mHandler = new Handler();
	
	private String getBankSupportUrl;
	private	List<BankSupportInfo> bankList = new ArrayList<BankSupportInfo>();
	//网络出错页面
	private RelativeLayout errNetLayout;
	private RelativeLayout noDataLayout;
	private TextView networkLoad;
	private TextView networkText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bankcard_choosebank);
		parseUrl();
		findViews();
		initTitle();
		initContent();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(bankListView);
		} else {
			createFlag = false;
		}
	}

	private void findViews() {
		chooseBankTitle = (TitleView) findViewById(R.id.chooseBankTitle);
		bankListView = (PullToRefreshListView) findViewById(R.id.bankListView);
		bankListAdapter = new BankListInfoAdapter(ChooseBankNameActivity.this, R.layout.item_choose_bank_info, bankList);
		bankListView.setAdapter(bankListAdapter);
		bankListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				errorListener.setRefreshView(bankListView);
				errorListener.setErrInterface(bankList.size()>0 ? null : ChooseBankNameActivity.this);
				mHandler.postDelayed(new Runnable() {
					public void run() {
						sendHttpGet(getBankSupportUrl, getBankListListener);
					}
				}, G.AUTO_LOAD_DELAYED);
			}
		});
		
		bankListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BankSupportInfo bankInfo = (BankSupportInfo)parent.getItemAtPosition(position);
				if(!"0".equals(bankInfo.getCode())) {
					Intent intent = new Intent(G.BROADCAST_SET_BANK_ID);
					intent.putExtra("bankCode", bankInfo.getCode());
					intent.putExtra("bankName", bankInfo.getName());
					sendBroadcast(intent);
					ChooseBankNameActivity.this.finish();
				}
			}
		});
		
		errNetLayout = (RelativeLayout) findViewById(R.id.errNetLayout);
		noDataLayout = (RelativeLayout) findViewById(R.id.noDataLayout);
		networkLoad = (TextView) findViewById(R.id.networkload);
		networkText = (TextView) findViewById(R.id.networktext);
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView();
				bankListView.setLoadRefreshing();
			}
		});
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			getBankSupportUrl = getApplicationContext().getActionUrl(G.GCK_API_GET_SUPPORT_BANKS);
		} else {
			getBankSupportUrl = G.URL_GET_USER_SUPPORT_BANKS;
		}
		Log.d("log_tag", "url"+getBankSupportUrl);
	}
	
	private void initTitle() {
		chooseBankTitle.setTitle(R.string.bankcard_choose_title);
		chooseBankTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChooseBankNameActivity.this.finish();
			}
		});
		chooseBankTitle.setLeftImageButton(R.drawable.back);
		chooseBankTitle.setLeftTextButton("返回");

	}
	
	private void initContent() {
		List dbBankList = (List<BankSupportInfo>) KdlcDB.findAllByClass(BankSupportInfo.class);
		if(dbBankList != null && dbBankList.size() > 0) {
			bankList.addAll(dbBankList);
		} else {
			getBankList();
		}
	}
	
	private void getBankList() {
		mHandler.postDelayed(new Runnable() {
			public void run() {
				bankListView.setLoadRefreshing();
			}
		}, G.AUTO_LOAD_DELAYED);
	}

	private Listener<JSONObject> getBankListListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				if (response.getInt("code") == 0) {
					Gson gson = new Gson();
					List<BankSupportInfo> newBankList = gson.fromJson(response.getString("banks"), new TypeToken<List<BankSupportInfo>>(){}.getType());
					if(newBankList != null && newBankList.size() >= 0) {
						bankList.clear();
						bankList.add(new BankSupportInfo("0", "请选择绑卡银行"));
						bankList.addAll(newBankList);
						KdlcDB.deleteAllByClass(BankSupportInfo.class);
						KdlcDB.addByEntityList(bankList);
						bankListAdapter.notifyDataSetChanged();
					}
					if(bankList.size() <= 0) {
						showNoDataView();
					}
				} else {
					if(bankList.size() <= 0) {
						KdlcDialog.showBackDialog(ChooseBankNameActivity.this, response.getString("message"));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch bloc
				e.printStackTrace();
				if(bankList.size() <= 0) {
					KdlcDialog.showBackDialog(ChooseBankNameActivity.this, "");
				}
			}
			if(bankListView != null)
				bankListView.onRefreshComplete();
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
		bankListView.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}
	
	private void showNoDataView() {
		bankListView.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.VISIBLE);
	}
	
	private void showListView() {
		bankListView.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
		noDataLayout.setVisibility(View.GONE);	
	}
	

	
}
