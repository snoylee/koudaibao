package com.kdkj.koudailicai.view.register;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.SelfCenterRecord;
import com.kdkj.koudailicai.domain.UserInfoRecord;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.WebViewActivity;
import com.kdkj.koudailicai.view.invest.InvestAccountActivity;
import com.tencent.bugly.crashreport.CrashReport;

public class RegisterPasswordActivity extends BaseActivity {
	private RelativeLayout rlVerification, rlPassword,cb_view;
	private EditText etVerification, etPassword;
	private TextView tvVerification, tvAgreement, tvSubmit;
	private ImageView ivPasswordSee,cbAgreement;
	private TitleView title;
	// size
	private int screenHeight;
	// dimens
	private double rlVerificationMarginTop = 0.048;
	private double rlPasswordMarginTop = 0.026;
	private boolean register_agreement = true;
	private String type = "use"; 
	private String urlAgree;
	private String url;
	private String urlAgain;	
	private UserInfoRecord userInfoRecord;
    private SelfCenterRecord selfCenterRecord;
    private String phone;
    private boolean flag = true;
    private boolean seePasswordFlag = true;
    private static final int INTERVAL = 1;
    private int curTime;
    private SmsObserver smsObserver;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_pwd);
		parseUrl();
		findViews();
		initContents();
		initTitle();
		initSize();
		setListener();
		initSmsGet();
	}
	
	private void initContents() {
		phone = getIntent().getStringExtra("phone").trim();
		if(Tool.isBlank(phone)) {
			dialog = KdlcDialog.showAlertDialog(RegisterPasswordActivity.this, goToPhone, "手机号码获取失败，请重试");
		} else {
			setSendCode(true);
		}
	}
		
	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = this.getApplicationContext().getActionUrl(G.GCK_API_POST_USER_REGISTER);
			urlAgain = this.getApplicationContext().getActionUrl(G.GCK_API_POST_USER_REGGET_A);
			urlAgree = this.getApplicationContext().getActionUrl(G.GCK_API_GET_PAGE_AGREEMENT);
		} else {
			url = G.URL_POST_USER_REGISTER;
			urlAgain = G.URL_POST_USER_REGGET;
			urlAgree = G.URL_GET_PAGE_AGREEMENT;
		}
	}
	
	private void findViews() {
		// TODO Auto-generated method stub
		rlVerification = (RelativeLayout) findViewById(R.id.rl_verification);
		rlPassword = (RelativeLayout) findViewById(R.id.rl_password);
		etVerification = (EditText) findViewById(R.id.et_verification);
		etPassword = (EditText) findViewById(R.id.et_password);
		tvVerification = (TextView) findViewById(R.id.tv_verification);
		tvAgreement = (TextView) findViewById(R.id.tv_agreement);
		tvAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 加下划线
		tvSubmit = (TextView) findViewById(R.id.tv_submit);
		cbAgreement = (ImageView) findViewById(R.id.cb_agreement);
		cb_view = (RelativeLayout) findViewById(R.id.cb_view);
		ivPasswordSee = (ImageView) findViewById(R.id.iv_password_see);
	}
	
	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		title.setTitle(R.string.register_title_2);
		title.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//onBackPressed();
				RegisterPasswordActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}

	private void initSize() {
		// TODO Auto-generated method stub
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		// 设置验证码
		RelativeLayout.LayoutParams rlVerificationLayoutParams = (RelativeLayout.LayoutParams) rlVerification.getLayoutParams();
		rlVerificationLayoutParams.topMargin = (int) (screenHeight * rlVerificationMarginTop);
		rlVerification.setLayoutParams(rlVerificationLayoutParams);
		// 设置密码
		RelativeLayout.LayoutParams rlPasswordLayoutParams = (RelativeLayout.LayoutParams) rlPassword.getLayoutParams();
		rlPasswordLayoutParams.topMargin = (int) (screenHeight * rlPasswordMarginTop);
		rlPassword.setLayoutParams(rlPasswordLayoutParams);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		textChange passwordChange = new textChange();
		etVerification.addTextChangedListener(passwordChange);
		etPassword.addTextChangedListener(passwordChange);
		tvVerification.setOnClickListener(verification);
		cb_view.setOnClickListener(cbVew);
		
		ivPasswordSee.setOnClickListener(passwordSee);
		tvAgreement.setOnClickListener(agree);
	}

	private void setSendCode(boolean send) {
		curTime = 60;
		if(send == true) {
			mHandler.sendEmptyMessage(INTERVAL);
			tvVerification.setTextColor(getResources().getColor(R.color.global_label_color));
			tvVerification.setClickable(false);
		} else {
			tvVerification.setText("重新发送");
			tvVerification.setTextColor(getResources().getColor(R.color.global_black_color));
			tvVerification.setClickable(true);
		}
	}

	// 获取验证码的点击事件
	private OnClickListener verification = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog = KdlcDialog.showProgressDialog(RegisterPasswordActivity.this, "发送验证码...");
			HttpParams params = new HttpParams();
			params.add("phone", phone);
			sendHttpPost(urlAgain, params, responseListenerAgain);
		}
	};
	
	//用户协议
	private OnClickListener cbVew = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (flag) {
				cbAgreement.setImageDrawable(getResources().getDrawable(R.drawable.check_pressed));
				register_agreement = true;
				flag = false;
			}else {
				cbAgreement.setImageDrawable(getResources().getDrawable(R.drawable.check_empty));
				register_agreement = false;
				flag = true;
			}
		}
	};
	
	private OnClickListener agree = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(RegisterPasswordActivity.this, WebViewActivity.class);
			intent.putExtra("url", urlAgree+"?type="+type);
			intent.putExtra("title", "用户使用协议");
			startActivity(intent);
		}
	};
	
	private OnClickListener submit = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String edit_verification = etVerification.getText().toString().trim();
			String edit_password = etPassword.getText().toString().trim();
			if (TextUtils.isEmpty(edit_verification)) {
				KdlcDialog.showToast("请输入短信验证码");
			} else if (etVerification.getText().length() < 6) {
				KdlcDialog.showToast("验证码输入不正确");
			} else if (TextUtils.isEmpty(edit_password)) {
				KdlcDialog.showToast("请输入登录密码");
			} else if (etPassword.getText().length() < 6 || etPassword.getText().length() > 16) {
				KdlcDialog.showToast("请输入6~16位的登录密码");
			} else if (register_agreement == false) {
				KdlcDialog.showToast("您还未同意使用协议");
			} else {
				dialog = KdlcDialog.showProgressDialog(RegisterPasswordActivity.this, "正在注册...");
				HttpParams params = new HttpParams();
				params.add("phone", phone);
				params.add("code", etVerification.getText().toString());
				params.add("password", etPassword.getText().toString());
				sendHttpPost(url, params, responseListener);
			}
		}
	};
	
	
	private OnClickListener passwordSee = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (seePasswordFlag) {
				etPassword.setSelection(etPassword.getText().toString().length());
				etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				ivPasswordSee.setImageDrawable(getResources().getDrawable(R.drawable.see_pressed));
				seePasswordFlag = false;
			} else {
				etPassword.setSelection(etPassword.getText().toString().length());
				etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
				ivPasswordSee.setImageDrawable(getResources().getDrawable(R.drawable.see_unpressed));
				seePasswordFlag = true;
			}
			String pwd = etPassword.getText().toString();
			etPassword.setSelection(pwd.length());
			
		}
	};
	
	Listener<JSONObject> responseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				dialog.cancel();
				if (response.getInt("code") == 0) {
					JSONObject data;
					data = response.getJSONObject("user");	
					JSONObject dataAccount;
					dataAccount=response.getJSONObject("account");
					HashMap<String, String> vals = new HashMap<String, String>();
					vals.put("username", ""+phone);
					vals.put("uid", ""+data.getString("uid"));
					vals.put("sessionid", response.getString("sessionid"));	
					vals.put("realname", data.getString("realname"));
                    vals.put("real_verify_status", ""+data.getString("real_verify_status"));
                    vals.put("id_card", data.getString("id_card"));
                    vals.put("isLogin", "1");
					vals.put("gesture", "0");
					vals.put("is_novice", "1");
					vals.put("set_paypwd_status", data.getString("set_paypwd_status"));
					vals.put("card_bind_status", data.getString("card_bind_status"));
					KDLCApplication.app.getSession().set(vals);
					CrashReport.setUserId(phone); 
					userInfoRecord = new UserInfoRecord();
		    		userInfoRecord.setUid(data.getInt("uid"));
		    		userInfoRecord.setUsername(data.getString("username"));
		    		userInfoRecord.setRealname(data.getString("realname"));
		    		userInfoRecord.setId_card(data.getString("id_card"));
		    		userInfoRecord.setReal_verify_status(data.getString("real_verify_status"));
		    		userInfoRecord.setCard_bind_status(data.getString("card_bind_status"));
		    		userInfoRecord.setSet_paypwd_status(data.getString("set_paypwd_status"));
		    		userInfoRecord.setIs_novice(data.getString("is_novice"));
		    		if(Tool.hasCacheData(UserInfoRecord.class))
		    			KDLCApplication.app.kdDb.deleteByWhere(UserInfoRecord.class, "uid="+data.getInt("uid"));
		    		KDLCApplication.app.kdDb.save(userInfoRecord);
		    		
		    		selfCenterRecord = new SelfCenterRecord();
		    		selfCenterRecord.setUid(data.getInt("uid"));
		    		selfCenterRecord.setCurProfitsDate(dataAccount.getString("lastday_profits_date"));
		    		selfCenterRecord.setCurProfits(dataAccount.getString("lastday_profits"));
		    		selfCenterRecord.setTotalProfits(dataAccount.getString("total_profits"));
		    		selfCenterRecord.setTotalMoney(dataAccount.getString("total_money"));
		    		selfCenterRecord.setHoldMoney(dataAccount.getString("hold_money"));
		    		selfCenterRecord.setRemainMoney(dataAccount.getString("remain_money"));
		    		selfCenterRecord.setNewTradeCount(dataAccount.getString("trade_count"));
		    		if(Tool.hasCacheData(SelfCenterRecord.class))
		    			KDLCApplication.app.kdDb.deleteByWhere(SelfCenterRecord.class, "uid="+data.getInt("uid"));
		    		KDLCApplication.app.kdDb.save(selfCenterRecord);
		    		KdlcDialog.showBottomToast("恭喜您，注册成功！");
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(G.TO_FRAGMENT_KEY, G.FRAGMENT_TAG.CENTER);
					intent.setClass(RegisterPasswordActivity.this, MainActivity.class);
					RegisterPasswordActivity.this.startActivity(intent);
					RegisterPasswordActivity.this.finish();
				} else {
					KdlcDialog.showInformDialog(RegisterPasswordActivity.this,response.getString("message"));
				}

			} catch (Exception e) {
				// TODO: handle exception
				KdlcDialog.showBottomToast("");
				e.printStackTrace();
			}
		}
	};
	
	Listener<JSONObject> responseListenerAgain = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				dialog.cancel();
				if (response.getInt("code") == 0) {
					KdlcDialog.showBottomToast("验证码已发送");
					setSendCode(true);
				} else {
					KdlcDialog.showInformDialog(RegisterPasswordActivity.this,response.getString("message"));
				}
			} catch (Exception ex) {
				KdlcDialog.showNetErrToast("网络出错,请稍候再试");
				ex.printStackTrace();
			}
		}
	};
	

	// EditText监听器
	private class textChange implements TextWatcher {
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
			if (etVerification.getText().length() > 0) {
				Sign1 = true;
			} else {
				Sign1 = false;
			}
			boolean Sign2 = false;
			if (etPassword.getText().length() > 0) {//etVerification.getText().length() == 6 && 
				Sign2 = true;
			} else {
				Sign2 = false;
			}

			if (Sign1 == true && Sign2 == true) {
				tvSubmit.setBackgroundResource(R.drawable.btn_red_background);
				tvSubmit.setClickable(true);
				tvSubmit.setOnClickListener(submit);
			} else {
				tvSubmit.setBackgroundResource(R.drawable.btn_grey_background);
				tvSubmit.setClickable(false);
			}
		}

	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case INTERVAL:
					if (curTime > 0) {
						tvVerification.setText(""+curTime+"秒");
						mHandler.sendEmptyMessageDelayed(INTERVAL, 1000);
						curTime--;
					} else {
						setSendCode(false);
					}
					break;
				case SmsObserver.SEND_VERIFY_NUM:
					etVerification.setText(smsObserver.verifyNum);
					etVerification.setSelection(smsObserver.verifyNum.length());
					break;
				default:
					setSendCode(false);
					break;
			}
		};
	};
	
	private OnClickListener goToPhone = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			goToRegPhone();
		}
	};
	
	private void goToRegPhone() {
		dialog.cancel();
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getContentResolver().unregisterContentObserver(smsObserver);
	}

	private void initSmsGet(){
        smsObserver = new SmsObserver(mHandler);  
        getContentResolver().registerContentObserver(SMS_INBOX, true,  
                smsObserver);  
	}
	
	
}
