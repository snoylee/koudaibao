package com.kdkj.koudailicai.view.selfcenter.holdasset;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.KdlcProgressBar;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeListener;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeRelativeLayout;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.WithdrawActivity;
public class TransferMoneyActivity extends BaseActivity implements
		ResizeListener {
	private TitleView transfertitle;
	private int screenHeight;
	private int screenWidth;
	private EditText transferAccount;
	private TextView transferBtn;
	private KdlcProgressBar buyprogressRedBar;
	private double transferAccountheight = 0.077;
	private double transferBtnheight = 0.067;
	private double buyprogressRedBarheight = 0.028;
	private TextView transferName, transferfee;
	private TextView transferapr, totalmoney, alreadytransferapr, lasttime,
			transfee;
	private String url;
	private String nameString;
	private ScrollView transferscrollview;
	private ResizeRelativeLayout transferParentView;
	private Handler handler = new Handler();
    private String totalMoneyString;
    private double progress;
    private int maxProgress;
    private String waitTotalMoney;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.transfer_money);
		parseUrl();
		findViews();
		initetitle();
		initSize();
		listener();
		registerBoradcastReceiver();
		getKdbRemainInfo();
	}

	public void findViews() {
		transferParentView = (ResizeRelativeLayout) this
				.findViewById(R.id.transferParentView);
		transferParentView.setResizeListener(this);
		transferscrollview = (ScrollView) findViewById(R.id.transferscrollview);
		transfertitle = (TitleView) findViewById(R.id.transfertitle);
		transferAccount = (EditText) findViewById(R.id.transferAccount);
		transferBtn = (TextView) findViewById(R.id.transferBtn);
		buyprogressRedBar = (KdlcProgressBar) findViewById(R.id.buyprogressRedBar);
		transferName = (TextView) findViewById(R.id.transferName);
		transferapr = (TextView) findViewById(R.id.transferapr);
		totalmoney = (TextView) findViewById(R.id.totalmoney);
		transfee = (TextView) findViewById(R.id.transfee);
		alreadytransferapr = (TextView) findViewById(R.id.alreadytransferapr);
		transferfee = (TextView) findViewById(R.id.transferfee);
		lasttime = (TextView) findViewById(R.id.lasttime);
		dialog = KdlcDialog.showProgressDialog(TransferMoneyActivity.this,
				"正在加载...");
		errorListener.setNetErrShowAlert(true);
		errorListener.setClickListener(finishActivity);

	}

	private void getKdbRemainInfo() {

		sendHttpGet(TransferMoneyActivity.this.url + "?invest_id="
				+ getIntent().getStringExtra("invest_id"), getKdbInfoListener);
	}

	private Listener<JSONObject> getKdbInfoListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub

			try {
				dialog.cancel();
				if (response.getInt("code") == 0) {
					JSONObject investItem = response
							.getJSONObject("investItem");
					transferName.setText(investItem.getString("project_name"));
					nameString = investItem.getString("project_name");
					transferapr.setText(investItem.getString("project_apr"));
					totalmoney.setText(Tool.toDeciDouble(investItem.getString("duein_money")));	
					waitTotalMoney=Tool.toDeciDouble(investItem.getString("duein_money"));
					transferfee.setText(Tool.toDeciDouble(investItem.getString("duein_capital")));
					totalMoneyString=Tool.toDeciDouble(investItem.getString("duein_capital"));
					alreadytransferapr.setText(Tool.toDeciDouble(investItem
							.getString("profits")));
					transfee.setText("参考金额："+ Tool.toDeciDouble(investItem.getString("refer_money")));
					lasttime.setText("剩余" + investItem.getString("rest_days")+ "天");
					progress=Double.parseDouble(investItem.getString("last_days"))/Double.parseDouble(investItem.getString("total_days"));
					maxProgress = (int) Math.ceil(progress*100);
					Log.v("dadsadasdasdasdas",maxProgress+"");
					buyprogressRedBar.setAnimProgress(maxProgress);
				}else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(TransferMoneyActivity.this);
        		}   
				else {
					KdlcDialog.showBackDialog(TransferMoneyActivity.this, response.getString("message"));
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				KdlcDialog.showBackDialog(TransferMoneyActivity.this, "网络出错，请稍后再试");
			}
		}

	};

	private void parseUrl() {
		if (this.getApplicationContext().getGlobalConfigManager() != null
				&& this.getApplicationContext().getGlobalConfigManager()
						.isComplete()) {
			Log.d("asdasd", "parseurl");
			url = this.getApplicationContext().getActionUrl(
					G.GCK_API_CREDIT_INVEST_BYID);
			Log.d("asdasd", "parseurl:" + url);
		} else {
			url = G.URL_POST_CREDIT_INVEST_BYID;
		}
	}

	private void listener() {
		textChange txChange = new textChange();
		transferAccount.addTextChangedListener(txChange);
	}

	private OnClickListener transBtn = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v("dsajdsgajd09090",totalMoneyString);
			if (Double.parseDouble(transferAccount.getText().toString()) >= Double
					.parseDouble(totalMoneyString)&&Double.parseDouble(transferAccount.getText().toString()) <= Double
							.parseDouble(waitTotalMoney)) {
				Intent intent = new Intent(TransferMoneyActivity.this,TransferPasswordActivity.class);
				intent.putExtra("transferName", nameString);
				intent.putExtra("money",Tool.trimHeadZero(transferAccount.getText().toString()));
				intent.putExtra("id", getIntent().getStringExtra("invest_id"));
				startActivity(intent);
			} else if(Double.parseDouble(transferAccount.getText().toString()) < Double
					.parseDouble(totalMoneyString)){
				KdlcDialog.showToast("转让金额必须大于本金");
			}else {
				KdlcDialog.showToast("转让金额不能超过总待收");
			}

		}
	};

	private void initetitle() {
		transfertitle.setTitle(R.string.transfertitle);
		transfertitle.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TransferMoneyActivity.this.finish();
			}
		});
		transfertitle.setLeftImageButton(R.drawable.back);
		transfertitle.setLeftTextButton("返回");

	}

	private void initSize() {
		screenHeight = this.getScreenHeight();
		screenWidth = this.getScreenWidth();
		RelativeLayout.LayoutParams transferAccountViewLayoutParams = (RelativeLayout.LayoutParams) transferAccount
				.getLayoutParams();
		transferAccountViewLayoutParams.height = (int) (screenHeight * transferAccountheight);
		transferAccount.setLayoutParams(transferAccountViewLayoutParams);
		RelativeLayout.LayoutParams investAccountViewLayoutParams = (RelativeLayout.LayoutParams) transferBtn
				.getLayoutParams();
		investAccountViewLayoutParams.height = (int) (screenHeight * transferBtnheight);
		transferBtn.setLayoutParams(investAccountViewLayoutParams);
		RelativeLayout.LayoutParams buyprogressRedBarLayoutParams = (RelativeLayout.LayoutParams) buyprogressRedBar
				.getLayoutParams();
		buyprogressRedBarLayoutParams.height = (int) (screenHeight * buyprogressRedBarheight);
		buyprogressRedBar.setLayoutParams(buyprogressRedBarLayoutParams);

	}

	// EditText监听器
	class textChange implements TextWatcher {
		private String temp;
		@Override
		public void onTextChanged(CharSequence cs, int start, int before,
				int count) {
			temp = cs.toString();
			boolean Sign1 = false;
			if (transferAccount.getText().length() > 0) {
				Sign1 = true;
			} else {
				Sign1 = false;
			}

			if (Sign1) {
				transferBtn.setBackgroundResource(R.drawable.btn_red_background);
				transferBtn.setOnClickListener(transBtn);
				transferBtn.setClickable(true);
			} else {
				transferBtn.setBackgroundResource(R.drawable.btn_grey_background);
				transferBtn.setClickable(false);
				transferBtn.setOnClickListener(null);
			}
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			if (temp.length() > 0) {
			} else {

			}
			int posDot = temp.indexOf(".");
			if (posDot < 0)
				return;
			if (temp.length() - posDot - 1 > 2) {
				arg0.delete(posDot + 3, posDot + 4);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		
	}

	@Override
	public void onResize(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		if (oldw != 0) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					transferscrollview.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
		}
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(G.HOLD_ASSET_REFRESH)) {
				TransferMoneyActivity.this.finish();
			}
		}
	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(G.HOLD_ASSET_REFRESH);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
}
