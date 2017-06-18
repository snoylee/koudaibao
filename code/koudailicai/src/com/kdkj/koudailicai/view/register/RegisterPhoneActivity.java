package com.kdkj.koudailicai.view.register;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.login.LoginAlreadyActivity;


public class RegisterPhoneActivity extends BaseActivity {
	
	private EditText etPhoneNumber;
	private TextView tvNext;
	private TitleView title;
	//size
	private int screenHeight;
	//dimens
	private double tvNextMarginTop = 0.044;
	//contents
	private String url;
	private String userName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_phone);
		parseUrl();
		initTitle();
		findViews();
		initSize();
		setListener();
	}
	
	private void parseUrl() {
		// TODO Auto-generated method stub
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = this.getApplicationContext().getActionUrl(G.GCK_API_POST_USER_REGGET_A);
		} else {
			url = G.URL_POST_USER_REGGET;
		}
	}
	
	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		title.setTitle(R.string.register_title_1);
		title.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegisterPhoneActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
		if("1".equals(getIntent().getStringExtra("LoginType")))
		{
			
		}else {
			title.showRightButton(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
		        	Intent intent = new Intent(RegisterPhoneActivity.this, LoginAlreadyActivity.class);
					intent.putExtra("phone", userName);
					intent.putExtra("toMain", "1");
					startActivity(intent);
					RegisterPhoneActivity.this.finish();
				}
			});
			title.setRightTextButton("登录");
		}
		
	}
		
	private void findViews() {
		// TODO Auto-generated method stub
		etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
		tvNext = (TextView) findViewById(R.id.tv_next);
	}

	private void initSize() {
		// TODO Auto-generated method stub
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		//设置下一步按钮
		RelativeLayout.LayoutParams tvNextLayoutParams = (RelativeLayout.LayoutParams) tvNext.getLayoutParams();
		tvNextLayoutParams.topMargin = (int) (screenHeight*tvNextMarginTop);
		tvNext.setLayoutParams(tvNextLayoutParams);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		textChange passwordChange = new textChange();
		etPhoneNumber.addTextChangedListener(passwordChange);
		
	}

	private OnClickListener next = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			userName = etPhoneNumber.getText().toString().trim();
			if (TextUtils.isEmpty(userName)) {
				KdlcDialog.showToast("手机号码不能为空");
			}else if(Tool.isMobileNO(userName)){
				dialog = KdlcDialog.showProgressDialog(RegisterPhoneActivity.this, "发送验证码...");
				HttpParams params = new HttpParams();
				params.add("phone", userName);
				sendHttpPost(url, params, responseListener);
			}else {
				KdlcDialog.showToast("手机号码输入不正确");
			}
		}
	};
		
	Listener<JSONObject> responseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				dialog.cancel();
				if (response.getInt("code") == 0) {
					KdlcDialog.showBottomToast("验证码已发送");
					Intent intent = new Intent(RegisterPhoneActivity.this, RegisterPasswordActivity.class);
					intent.putExtra("phone", userName);
					startActivity(intent);
				}else if(response.getInt("code") == 1001){
					dialog = KdlcDialog.showConfirmDialog(RegisterPhoneActivity.this, true, goLogin, "该手机号已被注册，立即登录？");
				} else {
					KdlcDialog.showInformDialog(RegisterPhoneActivity.this, response.getString("message"));
				}
			} catch (Exception ex) {
				KdlcDialog.showBottomToast("");
				ex.printStackTrace();
			}
		}
	};
		
	private OnClickListener goLogin = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			dialog.cancel();
        	Intent intent = new Intent(RegisterPhoneActivity.this, LoginAlreadyActivity.class);
			intent.putExtra("phone", userName);
			intent.putExtra("toMain", "1");
			startActivity(intent);
		}
	};

	// EditText监听器
	private	class textChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {

		}
		
		@Override
		public void onTextChanged(CharSequence cs, int start, int before,int count) {
			if (etPhoneNumber.getText().toString().trim().length() == 11) {
				tvNext.setBackgroundResource(R.drawable.btn_red_background);
				tvNext.setClickable(true);
				tvNext.setOnClickListener(next);
			} else {
				tvNext.setBackgroundResource(R.drawable.btn_grey_background);
				tvNext.setClickable(false);
			}
		}

	} 
		
}
