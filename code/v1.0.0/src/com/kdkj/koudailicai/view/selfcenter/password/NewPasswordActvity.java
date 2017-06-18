package com.kdkj.koudailicai.view.selfcenter.password;

import java.util.HashMap;



import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeListener;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeRelativeLayout;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.login.LoginAlreadyActivity;
public class NewPasswordActvity extends BaseActivity{
	private TitleView title;

	private EditText etNewPwd,etNewPwdAgain;
	private TextView  etPrompt,tvComplete;
	
	private ResizeRelativeLayout parentView;
	private ScrollView scrollView;
	private Handler mHandler = new Handler();
	private String url;
	private String payUrl;
	private String phone;
	private String realname;
	private String id_card;
	private String code;
	private String type;
	private Boolean realnameFlag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_password);
		parseUrl();
		getContents();
		initView();
		initTitle();
		setListeners();
	}
	private void initTitle() {
		title = (TitleView) findViewById(R.id.title);
		title.setTitle(R.string.pwd_new_input);
		title.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				NewPasswordActvity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}
	
	private void getContents() {
		phone  = getIntent().getStringExtra("phone");
		realname = getIntent().getStringExtra("realname");
		id_card = getIntent().getStringExtra("id_card");
		code = getIntent().getStringExtra("code");
		type = getIntent().getStringExtra("type");
		Log.e("INFO-----", type);
	}
	
	private void initView() {
		
		etNewPwd = (EditText) findViewById(R.id.et_new_pwd);
		etNewPwdAgain = (EditText) findViewById(R.id.et_new_pwd_again);
		etPrompt = (TextView) findViewById(R.id.et_prompt);
		tvComplete = (TextView) findViewById(R.id.tv_complete);
		if("find_pwd".equals(type)) {
			etPrompt.setText(R.string.login_prompt);
			//设置6-16位， 字母和数字
			etNewPwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
			etNewPwdAgain.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
			etNewPwd.setKeyListener(new NumberKeyListener() {  
			    @Override  
			    protected char[] getAcceptedChars() {  
			        return new char[] { '1', '2', '3', '4', '5', '6', '7', '8','9', '0' ,'q',
			        		'w','e','r','t','y','u','i','o','p','a','s','d','f','g','h',
			        		'j','k','l','z','x','c','v','b','n','m','Q','W','E','R','T','Y',
			        		'U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M'};  
			    }  
  		      @Override  
			    public int getInputType() {  
			        // TODO Auto-generated method stub   
			        return android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;  
			    }  
			});
			etNewPwdAgain.setKeyListener(new NumberKeyListener() {  
			    @Override  
			    protected char[] getAcceptedChars() {  
			        return new char[] { '1', '2', '3', '4', '5', '6', '7', '8','9', '0' ,'q',
			        		'w','e','r','t','y','u','i','o','p','a','s','d','f','g','h',
			        		'j','k','l','z','x','c','v','b','n','m','Q','W','E','R','T','Y',
			        		'U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M'};  
			    }  
//			    @Override  
			    public int getInputType() {  
			        // TODO Auto-generated method stub   
			        return android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;  
			    }  
			});
			etNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
			etNewPwdAgain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
		
		} else {
			etPrompt.setText(R.string.pay_prompt);
			//设置6位，只能为数字
			etNewPwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			etNewPwdAgain.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			etNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
			etNewPwdAgain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
		}
		
		
	}
	
	private void setListeners() {
		textChange password = new textChange();
		etNewPwd.addTextChangedListener(password);
		etNewPwdAgain.addTextChangedListener(password);
		
		
	}
	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = this.getApplicationContext().getActionUrl(G.GCK_API_POST_USER_RESET_PASSWORD);
			Log.e("INFO", url+"");
			payUrl = this.getApplicationContext().getActionUrl(G.GCK_API_POST_USER_RESET_PAY_PASSWORD);
			Log.e("INFO", payUrl+"");
		} else {
			url = G.URL_POST_USER_RESET_PASSWORD;
			Log.e("INFO", url+"");
			payUrl = G.URL_POST_USER_RESET_PAY_PASSWORD;
			Log.e("INFO", payUrl+"");
		}
	}
	
	private OnClickListener complete = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String newPwd = etNewPwd.getText().toString();
			String newPwdAgain = etNewPwdAgain.getText().toString();
			if (etNewPwd.getText().length()>= 6 && etNewPwd.getText().length()<=16) {			
				if (newPwd.equals(newPwdAgain)) {	
					dialog = KdlcDialog.showProgressDialog(NewPasswordActvity.this, "正在提交...");
					HttpParams params = new HttpParams();
					params.add("phone", phone);
					if(!Tool.isBlank(realname)) {
						params.add("realname", realname);
						params.add("id_card", id_card);
					}
					params.add("code", code);
					params.add("password", etNewPwd.getText().toString());
					if (type.equals("find_pwd")) {
			          sendHttpPost(url, params, responseListener);
					}else {
						sendHttpPost(payUrl, params, responseListener);
					}
					
				}else {
					KdlcDialog.showToast("两次输入的密码不一致");
				}
			}else {
				KdlcDialog.showToast("密码长度不正确");
			}
		}
	};
	Listener<JSONObject> responseListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			dialog.cancel();
			try {
				if (response.getInt("code") == 0) {
					type = getIntent().getStringExtra("type");
					if (type.equals("find_pwd")) {
						KdlcDialog.showBottomToast("登录密码修改成功");
						Intent intent = new Intent(NewPasswordActvity.this, LoginAlreadyActivity.class);
						intent.putExtra("toMain", "1");
						KDLCApplication.app.setSessionVal("username", getIntent().getStringExtra("phone"));
						startActivity(intent);
					}else {
						KdlcDialog.showBottomToast("交易密码修改成功");
						sendBroadcast(new Intent(G.PAY_PASSWORD_SETTED));
					}
					NewPasswordActvity.this.finish();
				} else {
					KdlcDialog.showInformDialog(NewPasswordActvity.this, response.getString("message"));
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
			boolean Sign1 = false;
			if (etNewPwd.getText().length() >0) {
				Sign1 = true; 
			} else {
				Sign1 = false;
			}
			boolean Sign2 = true;
			if (etNewPwdAgain.getText().length()>0) {
				Sign2 = true;
			} else {
				//Toast.makeText(getApplicationContext(), "两次输入的密码不一样！", Toast.LENGTH_SHORT).show();
				Sign2 = false;
			}

			if (Sign1 & Sign2) {
				tvComplete.setBackgroundResource(R.drawable.btn_red_background);
				tvComplete.setClickable(true);
				tvComplete.setOnClickListener(complete);
			}else {
				tvComplete.setBackgroundResource(R.drawable.btn_grey_background);
				tvComplete.setClickable(false);
			}
		}

	}

}

