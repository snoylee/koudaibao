package com.kdkj.koudailicai.view.selfcenter.bankcard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.BankSupportInfo;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.invest.InvestAccountActivity;
import com.kdkj.koudailicai.view.selfcenter.AccountFinishedActivity;
import com.kdkj.koudailicai.view.selfcenter.TransactionRecordActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.TransferPasswordActivity;

public class UserBankCardInfoActivity extends BaseActivity implements HttpErrorInterface{
    private TitleView ownTitle;
    private String url;
    private TextView bankName;
    private TextView bankcardLeft;
    private TextView bankcardRight;
    private RelativeLayout ownCard;
    private ImageView bankLogo;

    private RelativeLayout netErrLayout;
    private TextView networkLoad,networkWord;
    
    private TextView oneBi,oneDay,oneCi;
    private RelativeLayout controlLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bankcard_usercard);
		findViews();
		initTitle();
		parseUrl();
		getOwnCreditcardInfo();
	}
	
	private void findViews() {
		ownCard=(RelativeLayout)findViewById(R.id.owncard);
		bankLogo=(ImageView)findViewById(R.id.logo);
		ownTitle=(TitleView)findViewById(R.id.owntitle);
		bankName=(TextView)findViewById(R.id.bankname);
		bankcardLeft=(TextView)findViewById(R.id.bankcardleft);
		bankcardRight=(TextView)findViewById(R.id.bankcardright);
		
		netErrLayout=(RelativeLayout)findViewById(R.id.cardnetlayout);
		networkLoad=(TextView)findViewById(R.id.networkload);
		networkWord=(TextView)findViewById(R.id.networktext);
		
		controlLayout=(RelativeLayout)findViewById(R.id.controllayout);
		oneBi=(TextView)findViewById(R.id.oneBi);
		oneDay=(TextView)findViewById(R.id.oneday);
		oneCi=(TextView)findViewById(R.id.oneCi);
		ownCard.setVisibility(View.GONE);
		controlLayout.setVisibility(View.GONE);
		netErrLayout.setVisibility(View.GONE);
	}
	
	private void initTitle() {
		ownTitle.setTitle(R.string.own_credit_name);
		ownTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				UserBankCardInfoActivity.this.finish();
			}
		});
		ownTitle.setLeftImageButton(R.drawable.back);
		ownTitle.setLeftTextButton("返回");

	}
	
	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = getApplicationContext().getActionUrl(G.GCK_API_GET_USER_CARDS);
		} else {
			url = G.URL_GET_USER_CARDS;
		}
	}
	
	private void getOwnCreditcardInfo() {
		dialog = KdlcDialog.showProgressDialog(UserBankCardInfoActivity.this);
		errorListener.setErrInterface(UserBankCardInfoActivity.this);
		sendHttpGet(UserBankCardInfoActivity.this.url, ownCreditcardListener);	
	}
	
	private Listener<JSONObject> ownCreditcardListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			dialog.cancel();
			try {
				if (response.getInt("code")==0) {
					JSONArray dataArray = response.getJSONArray("cards");
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataRoot = dataArray.getJSONObject(j);
						bankName.setText(dataRoot.getString("bank_name"));
						bankcardRight.setText(dataRoot.getString("card_no").substring(dataRoot.getString("card_no").length()-4, dataRoot.getString("card_no").length()));
					    if (dataRoot.getString("bank_id").equals("1")) {
					    	ownCard.setBackgroundResource(R.drawable.redbg);
					    	bankLogo.setBackgroundResource(R.drawable.bankgongshang);					    	
						}else if(dataRoot.getString("bank_id").equals("2")){
							ownCard.setBackgroundResource(R.drawable.greenbg);
					    	bankLogo.setBackgroundResource(R.drawable.banknonghang);		
						}else if (dataRoot.getString("bank_id").equals("3")) {
							ownCard.setBackgroundResource(R.drawable.redbg);
					    	bankLogo.setBackgroundResource(R.drawable.bankguangda);	
						}else if(dataRoot.getString("bank_id").equals("4")){
							ownCard.setBackgroundResource(R.drawable.redbg);
					    	bankLogo.setBackgroundResource(R.drawable.bankyouzheng);	
						}else if(dataRoot.getString("bank_id").equals("5")){
							ownCard.setBackgroundResource(R.drawable.redbg);
					    	bankLogo.setBackgroundResource(R.drawable.bankxingye);
						}else if(dataRoot.getString("bank_id").equals("6")){
							ownCard.setBackgroundResource(R.drawable.redbg);
					    	bankLogo.setBackgroundResource(R.drawable.bankshenfa);
						}else if(Tool.isBlank(dataRoot.getString("bank_id"))){
							ownCard.setBackgroundResource(R.drawable.redbg);
					    	bankLogo.setBackgroundResource(R.drawable.bankgongshang);
						}else {
							ownCard.setBackgroundResource(R.drawable.bluebg);
					    	bankLogo.setBackgroundResource(R.drawable.bankjianhang);
						}
					    if(dataRoot.getInt("sml")<1000000)
					    {
					    	 oneBi.setText("每笔限额"+dataRoot.getInt("sml")/100+"元，");
					    }else {
					    	 if(dataRoot.getInt("sml") % 1000000 == 0) {
					    		 oneBi.setText("每笔限额"+dataRoot.getInt("sml")/1000000+"万，");
					    	 } else{
						    	 oneBi.setText("每笔限额"+dataRoot.getDouble("sml")/1000000+"万，");
					    	 }
						}
					    if(dataRoot.getInt("dml")<1000000) {
					    	oneDay.setText("每日限额"+dataRoot.getInt("dml")/100+"元、");
					    }else{
					    	 if(dataRoot.getInt("dml") % 1000000 == 0) {
					    		 oneDay.setText("每日限额"+dataRoot.getInt("dml")/1000000+"万、");
					    	 } else{
					    		 oneDay.setText("每日限额"+dataRoot.getDouble("dml")/1000000+"万、");
					    	 }
					    } 
					    oneCi.setText("最多支付"+dataRoot.getString("dtl")+"次");
					}
					netErrLayout.setVisibility(View.GONE);
					ownCard.setVisibility(View.VISIBLE);
					controlLayout.setVisibility(View.VISIBLE);
				}  else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(UserBankCardInfoActivity.this);
				} else{
					showErrReq();
					KdlcDialog.showInformDialog(UserBankCardInfoActivity.this ,response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				showErrReq();
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
		dialog.cancel();
		ownCard.setVisibility(View.GONE);
		controlLayout.setVisibility(View.GONE);
		netErrLayout.setVisibility(View.VISIBLE);
		networkLoad.setText("点击刷新");
		networkWord.setText(err ? "网络出错" : "网络未连接");
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				netErrLayout.setVisibility(View.GONE);
				getOwnCreditcardInfo();
			}
		});
	}
}
