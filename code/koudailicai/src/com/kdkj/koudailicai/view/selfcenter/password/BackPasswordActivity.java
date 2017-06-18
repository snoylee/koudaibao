
package com.kdkj.koudailicai.view.selfcenter.password;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.kdkj.koudailicai.view.register.SmsObserver;
public class BackPasswordActivity extends BaseActivity implements ResizeListener{
	private RelativeLayout rlBackVerification;
	private EditText etBackPhoneNumber,etBackRealname,etBackIdcard,etBackVerification;
	private TextView tvBackVerification,tvNext; 
	private TitleView title;
	private ResizeRelativeLayout parentView;
	private ScrollView scrollView;
	//contents
	private String type;
	private String phone;
	private String sendPhone;
	private boolean hasPhone = true;
	private Boolean realnameFlag = false;
	private String type1 = "find_pwd";
	private String type2 = "find_pay_pwd";
	
	private String getVerifyCodeurl;
	private String resetPasswordUrl;
	private static final int INTERVAL = 1;
	private int curTime;
	
	private int curClick = -1;
    private SmsObserver smsObserver;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_back_password);
		registerBoradcastReceiver();
		parseUrl();
		initView();
		initTitle();
		//initSize();
		setContent();
		setListeners();
		initSmsGet();
		//setSendCode(true);
	}
	
	private void parseUrl() {
		// TODO Auto-generated method stub
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			getVerifyCodeurl = this.getApplicationContext().getActionUrl(G.GCK_API_POST_USER_RESET_PASSWORD_CODE);
			resetPasswordUrl = this.getApplicationContext().getActionUrl(G.GCK_API_POST_USER_VERIFY_RESET_PASSWORD);
		} else {
			getVerifyCodeurl = G.URL_POST_USER_RESET_PASSWORD_CODE;
			resetPasswordUrl = G.URL_POST_USER_VERIFY_RESET_PASSWORD;
		}
	}
	
	private void setContent() {
		type = getIntent().getStringExtra("find_pwd");
		//找回登录密码
		if(type != null && type.equals(type1)) {
			title.setTitle(R.string.pwd_back);
			phone = getIntent().getStringExtra("phone");
			String realStatus = getIntent().getStringExtra("real_verify_status");
			realnameFlag = (!Tool.isBlank(realStatus) && realStatus.equals("1")) ? true : false;
		} else {
		//找回交易密码
			title.setTitle(R.string.pwd_charge_back);
			phone = this.getApplicationContext().getSessionVal("username");
			String realStatus = this.getApplicationContext().getSessionVal("real_verify_status");
			realnameFlag = (!Tool.isBlank(realStatus) && realStatus.equals("1")) ? true : false;
		}
		
		if (realnameFlag == false) {
			etBackRealname.setVisibility(View.GONE);
			etBackIdcard.setVisibility(View.GONE);
		}
		
		if(!Tool.isBlank(phone)) {
			etBackPhoneNumber.setText(Tool.changeMobile(phone));
			etBackPhoneNumber.setFocusable(false);
			//发送验证码
//			if(type.equals(type2)) {
				//发送验证码
//				dialog = KdlcDialog.showProgressDialog(BackPasswordActivity.this, "发送验证码...");
//				HttpParams params = new HttpParams();
//				params.add("phone", phone);
//				params.add("type", type);
//				sendHttpPost(getVerifyCodeurl, params, responseListener);
//			} else {
//				setSendCode(true);
//			}
		} else {
			hasPhone = false;
		}
	}
	
	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		title.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BackPasswordActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		parentView = (ResizeRelativeLayout) this.findViewById(R.id.ParentView);
		parentView.setResizeListener(this);
		scrollView = (ScrollView) findViewById(R.id.scrollview);
		rlBackVerification = (RelativeLayout) findViewById(R.id.rl_back_verification);
		
		etBackPhoneNumber = (EditText) findViewById(R.id.et_back_phone_number);
		etBackRealname = (EditText) findViewById(R.id.et_back_realname);
		etBackIdcard = (EditText) findViewById(R.id.et_back_idcard);
		etBackVerification = (EditText) findViewById(R.id.et_back_verification);
		
		tvBackVerification = (TextView) findViewById(R.id.tv_back_verification);
		tvNext = (TextView) findViewById(R.id.tv_next);
		
	}
	

	private void setListeners() {
		// TODO Auto-generated method stub
		textChange passwordChange = new textChange();
		etBackPhoneNumber.addTextChangedListener(passwordChange);
		etBackVerification.addTextChangedListener(passwordChange);
		
		etBackRealname.setOnTouchListener(clickListener);
		etBackIdcard.setOnTouchListener(clickListener);
		etBackVerification.setOnTouchListener(clickListener);
		
		tvBackVerification.setOnClickListener(verification);
	}
	
	OnClickListener verification = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			//调接口
			sendPhone = hasPhone ? phone : etBackPhoneNumber.getText().toString().trim();
			if(Tool.isBlank(sendPhone)) {
				KdlcDialog.showToast("手机号码不能为空");
			} else if(!Tool.isMobileNO(sendPhone)) {
				KdlcDialog.showToast("手机号码格式不正确");
			} else {
				dialog = KdlcDialog.showProgressDialog(BackPasswordActivity.this, "发送验证码...");
				HttpParams params = new HttpParams();
				params.add("phone", phone);					
				params.add("type", type);
				sendHttpPost(getVerifyCodeurl, params, responseListener);				
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
					setSendCode(true);
					KdlcDialog.showBottomToast("验证码已发送");
				} else {
					KdlcDialog.showInformDialog(BackPasswordActivity.this,response.getString("message"));
				}
			} catch (Exception ex) {
				KdlcDialog.showBottomToast("");
				ex.printStackTrace();
			}
		}
	};
	
	OnClickListener next = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				sendPhone = hasPhone ? phone : etBackPhoneNumber.getText().toString();
				if(Tool.isBlank(sendPhone)) {
					KdlcDialog.showToast("手机号码不能为空");
				} else if(!Tool.isMobileNO(sendPhone)) {
					KdlcDialog.showToast("手机号码格式不正确");
				} else if (TextUtils.isEmpty(etBackVerification.getText().toString())) {
					KdlcDialog.showToast("验证码不能为空");
				} else if (etBackVerification.getText().length() < 6) {
					KdlcDialog.showToast("验证码不正确");
				} else if(realnameFlag && TextUtils.isEmpty(etBackRealname.getText().toString())) {
					KdlcDialog.showToast("真实姓名不能为空");	
				} else if(realnameFlag && !Tool.isWord(etBackRealname.getText().toString())) {
					KdlcDialog.showToast("真实姓名必须为中文");
				} else if (realnameFlag && etBackRealname.getText().length() < 2) {
					KdlcDialog.showToast("真实姓名不正确");
				}else if(realnameFlag && TextUtils.isEmpty(etBackIdcard.getText().toString())) {
					KdlcDialog.showToast("身份证号码不能为空");
				} else if(realnameFlag && !Tool.validatePersonalId(etBackIdcard.getText().toString())) {
					KdlcDialog.showToast( "身份证号码格式不正确");
				} else {
					//调接口
					dialog = KdlcDialog.showProgressDialog(BackPasswordActivity.this, "正在提交...");
					HttpParams params = new HttpParams();
					params.add("phone", phone);
					params.add("code", etBackVerification.getText().toString());
					if(realnameFlag) {
						params.add("realname", etBackRealname.getText().toString());
						params.add("id_card", etBackIdcard.getText().toString());
					}
					params.add("type", type);
					sendHttpPost(resetPasswordUrl, params, responseListenerCode);
				}	
		}
	};
	
	Listener<JSONObject> responseListenerCode = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			dialog.cancel();
			try {
				if (response.getInt("code") == 0) {
					Intent intent = new Intent(BackPasswordActivity.this, NewPasswordActvity.class);
					sendPhone = hasPhone ? phone : etBackPhoneNumber.getText().toString();
					intent.putExtra("phone", sendPhone);
					if(realnameFlag) {
						intent.putExtra("realname", etBackRealname.getText().toString());
						intent.putExtra("id_card", etBackIdcard.getText().toString());
					}
					intent.putExtra("code", etBackVerification.getText().toString());
					intent.putExtra("type", type);
					startActivity(intent);
					finish();
				} else {
					//KdlcDialog.showToast(response.getString("message"));
					KdlcDialog.showInformDialog(BackPasswordActivity.this,response.getString("message"));

				}

			} catch (Exception ex) {
				KdlcDialog.showBottomToast("");
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
				if (etBackVerification.getText().length() > 0) {
					Sign1 = true;
				} else {
					Sign1 = false;
				}
				boolean Sign2 = false;
				if (etBackVerification.getText().length() > 0 && etBackPhoneNumber.getText().length() > 0) {
					Sign2 = true;
				} else {
					Sign2 = false;
				}

				if (Sign1 == true && Sign2 == true) {
					tvNext.setBackgroundResource(R.drawable.btn_red_background);
					tvNext.setClickable(true);
					tvNext.setOnClickListener(next);
				} else {
					tvNext.setBackgroundResource(R.drawable.btn_grey_background);
					tvNext.setClickable(false);
				}
			}

		}
	@Override
	public void onResize(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		if(oldw != 0) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (curClick == R.id.et_back_idcard || curClick == R.id.et_back_verification) {
						scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					}
					setFocus(curClick);
					// etOldPwd.requestFocus();
				}
			});
		}
	}
	
	private OnTouchListener clickListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionevent) {
			// TODO Auto-generated method stub
			curClick = view.getId();
			if (motionevent.getAction() == MotionEvent.ACTION_UP) {
				switch (view.getId()) {
				case R.id.et_back_realname:
					break;
				case R.id.et_back_idcard:
					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					etBackIdcard.requestFocus();
					break;
				case R.id.et_back_verification:
					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					etBackVerification.requestFocus();
					break;
				}
			}

			return false;
		}
	};

	private void setFocus(int resId) {
		switch (resId) {
		case R.id.et_back_realname:
			etBackRealname.requestFocus();
			break;
		case R.id.et_back_idcard:
			etBackIdcard.requestFocus();
			break;
		case R.id.et_back_verification:
			etBackVerification.requestFocus();
			break;
		}
	}
	
	private void setSendCode(boolean send) {
		curTime = 60;
		if(send == true) {
			mHandler.sendEmptyMessage(INTERVAL);
			tvBackVerification.setTextColor(getResources().getColor(R.color.global_label_color));
			tvBackVerification.setClickable(false);
		} else {
			tvBackVerification.setText("重新发送");
			tvBackVerification.setTextColor(getResources().getColor(R.color.global_black_color));
			tvBackVerification.setClickable(true);
		}
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case INTERVAL:
					if (curTime > 0) {
						tvBackVerification.setText(""+curTime+"秒");
						mHandler.sendEmptyMessageDelayed(INTERVAL, 1000);
						curTime--;
					} else {
						setSendCode(false);
					}
					break;
				case SmsObserver.SEND_VERIFY_NUM:
					etBackVerification.setText(smsObserver.verifyNum);
					etBackVerification.setSelection(smsObserver.verifyNum.length());
					break;
				default:
					break;
			}
		};
	};
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if (action.equals(G.PAY_PASSWORD_SETTED)) {  
            	BackPasswordActivity.this.finish();
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
		getContentResolver().unregisterContentObserver(smsObserver);
	}
	
	private void initSmsGet(){
        smsObserver = new SmsObserver(mHandler);  
        getContentResolver().registerContentObserver(SMS_INBOX, true,  
                smsObserver);  
	}
}
