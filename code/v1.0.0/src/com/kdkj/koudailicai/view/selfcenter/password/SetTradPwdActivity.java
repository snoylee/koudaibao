package com.kdkj.koudailicai.view.selfcenter.password;

import java.util.HashMap;
import com.kdkj.koudailicai.view.KDLCApplication;import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.util.session.Session;

import com.kdkj.koudailicai.view.selfcenter.AccountCenterActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.kdkj.koudailicai.view.BaseActivity;
public class SetTradPwdActivity extends BaseActivity {
	private EditText et_set_trading_password,et_set_trading_password_again;
	private TextView tv_set_trading_complete;
	
	private Session session;
	private TitleView title;
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_trading_pwd);
		parseUrl();
		initView();
		initTitle();
		setListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		et_set_trading_password = (EditText) findViewById(R.id.et_set_trading_password);
		et_set_trading_password_again = (EditText) findViewById(R.id.et_set_trading_password_again);
		tv_set_trading_complete = (TextView) findViewById(R.id.tv_set_trading_complete);
	}
	private void initTitle() {
		// TODO Auto-generated method stub
		title.setTitle("设置交易密码");
		title.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SetTradPwdActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}
	private void parseUrl() {
		// TODO Auto-generated method stub
		if (this.getApplicationContext().getGlobalConfigManager() != null
				&& this.getApplicationContext().getGlobalConfigManager()
						.isComplete()) {
			url = getApplicationContext().getActionUrl(
					G.GCK_API_POST_SET_PAYPASSWORD);
		} else {
			url = G.URL_POST_SET_PAYPASSWORD;
		}
	}
	private void setListener() {
		// TODO Auto-generated method stub
		textChange passwordChange = new textChange();
		et_set_trading_password.addTextChangedListener(passwordChange);
		et_set_trading_password_again.addTextChangedListener(passwordChange);
		
		tv_set_trading_complete.setOnClickListener(complete);
	}
	private OnClickListener complete = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		
			String pwd = et_set_trading_password.getText().toString();
			String pwd_again = et_set_trading_password_again.getText().toString();
			if (et_set_trading_password.getText().length()<6) {
				KdlcDialog.showToast("交易密码需由6位数的数字组成");
			}else if (pwd.equals(pwd_again)) {
				dialog = KdlcDialog.showProgressDialog(SetTradPwdActivity.this, "正在设置...");
				HttpParams params = new HttpParams();
				params.add("password", et_set_trading_password.getText().toString());
				sendHttpPost(url, params, responseListener);
			}else {
				KdlcDialog.showToast("两次输入的密码不一致");
			}
		}
	};
	Listener<JSONObject> responseListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			dialog.cancel();
			try {
				if (response.getInt("code") == 0) {
					KdlcDialog.showBottomToast("密码设置成功");
					KDLCApplication.app.setSessionVal("set_paypwd_status", "1");
					sendBroadcast(new Intent(G.PAY_PASSWORD_SETTED));
					SetTradPwdActivity.this.finish();
				} else {
					KdlcDialog.showInformDialog(SetTradPwdActivity.this, response.getString("message"));
				}

			} catch (Exception ex) {
				KdlcDialog.showBottomToast("");
				ex.printStackTrace();
			}
		}
	};
	// EditText监听器
	private	class textChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before,
				int count) {
			boolean Sign1=false;
			if (et_set_trading_password.getText().length()>0) {
				Sign1 = true;
			} else {
				Sign1 = false;
			}
			boolean Sign2 = false;
			if (et_set_trading_password_again.getText().length()>0) {
				Sign2 = true;
			} else {
				Sign2 = false;
			}
			if (Sign1==true && Sign2==true) {
				tv_set_trading_complete.setBackgroundResource(R.drawable.btn_red_background);
				tv_set_trading_complete.setClickable(true);
			}else {
				tv_set_trading_complete.setBackgroundResource(R.drawable.btn_grey_background);
				tv_set_trading_complete.setClickable(false);
			}
			
		}

	} 
}
