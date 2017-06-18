package com.kdkj.koudailicai.view.selfcenter.bankcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.SelfCenterRecord;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.IdCardUtils;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.lib.http.MyHttpClient;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.password.ModifyPasswordTradingActivity;
import com.kdkj.koudailicai.view.fragment.SelfCenterFragment;
import com.kdkj.koudailicai.view.selfcenter.bankcard.AddBankCardActivity;
import com.kdkj.koudailicai.view.selfcenter.holdasset.TransferPasswordActivity;

public class ConfirmIdentityActivity extends BaseActivity {
	private TitleView identitytitle;
	private TextView buttonnext;
	private EditText logininname;
	private EditText logininnumber;
	private String url;
	private String realName;
	private String idCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_identity);
		registerBoradcastReceiver();
		parseUrl();
		findViews();
		initTitle();
		setListener();
	}
	
	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = this.getApplicationContext().getActionUrl(G.GCK_API_GET_USER_REALVERIFY);
		} else {
			url = G.URL_GET_USER_REAL_VERIFY;
		}
	}
	
	private void findViews() {
		identitytitle = (TitleView) findViewById(R.id.identitytitle);
		buttonnext = (TextView) findViewById(R.id.buttonnext);
		logininname = (EditText) findViewById(R.id.logininname);
		logininnumber = (EditText) findViewById(R.id.logininnumber);
	}

	private void initTitle() {
		identitytitle.setTitle(R.string.identity_title);
		identitytitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ConfirmIdentityActivity.this.finish();
			}
		});
		identitytitle.setLeftImageButton(R.drawable.back);
		identitytitle.setLeftTextButton("返回");

	}

	private void setListener() {
		textChange listenChange = new textChange();
		logininname.addTextChangedListener(listenChange);
		logininnumber.addTextChangedListener(listenChange);		
	}
	
	private OnClickListener submit = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!Tool.isWord(realName)) {
				KdlcDialog.showToast("真实姓名输入不正确");
			} else if (logininname.getText().length() < 2) {
				KdlcDialog.showToast("真实姓名输入不正确");
			} else if (!IdCardUtils.IDCardValidate(idCard)){
				KdlcDialog.showToast("身份证号码输入不正确");
			} else {
				dialog = KdlcDialog.showProgressDialog(ConfirmIdentityActivity.this, "正在确认...");
				HttpParams params = new HttpParams();
				params.add("realname", realName);
				params.add("id_card", idCard);
				sendHttpPost(ConfirmIdentityActivity.this.url, params, realIdentityListener);
			}
		}
	};

	private Listener<JSONObject> realIdentityListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			dialog.cancel();
			try {
				if (response.getInt("code") == 0) {
					HashMap<String, String> vals=new HashMap<String, String>();
					vals.put("real_verify_status", "1");
					vals.put("realname", realName);
					vals.put("id_card", idCard);
					KDLCApplication.app.getSession().set(vals);
					Intent intent = new Intent();
					intent.putExtra("logininname", logininname.getText().toString());
					intent.setClass(ConfirmIdentityActivity.this, AddBankCardActivity.class);
					startActivity(intent);
					finish();
				} else if(response.getInt("code") == -2) {
					dialog = KdlcDialog.showLoginDialog(ConfirmIdentityActivity.this);
				} else {
					KdlcDialog.showInformDialog(ConfirmIdentityActivity.this ,response.getString("message"));
				}
			} catch (Exception ex) {
				KdlcDialog.showBottomToast("");
				ex.printStackTrace();
			}
		}
	};

	// EditText监听器
	class textChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before, int count) {
			realName = logininname.getText().toString().trim();
			idCard = logininnumber.getText().toString().trim();
			if (realName.length() > 0 && idCard.length() > 0) {
				buttonnext.setBackgroundResource(R.drawable.btn_red_background);
				buttonnext.setOnClickListener(submit);
			} else {
				buttonnext.setBackgroundResource(R.drawable.btn_grey_background);
				buttonnext.setClickable(false);
			}
		}

	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if (action.equals(G.PAY_PASSWORD_SETTED)) {  
            	ConfirmIdentityActivity.this.finish();
            }  
        } 
	};  
      
	public void registerBoradcastReceiver(){  
	    IntentFilter myIntentFilter = new IntentFilter();  
	    myIntentFilter.addAction(G.PAY_PASSWORD_SETTED);  
	    //注册广播        
	    registerReceiver(mBroadcastReceiver, myIntentFilter);  
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
}
