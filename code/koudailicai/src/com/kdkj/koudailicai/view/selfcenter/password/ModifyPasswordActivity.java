package com.kdkj.koudailicai.view.selfcenter.password;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
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
import com.kdkj.koudailicai.view.KDLCApplication;
public class ModifyPasswordActivity extends BaseActivity implements ResizeListener{
	private TitleView title;
	private EditText etOldPwd,etNewPwd,etNewPwdAgain;
	private TextView tvPhoneNumber,tvComplete,tvPassword;
	private ResizeRelativeLayout parentView;
	private ScrollView scrollView;
	private Handler mHandler = new Handler();
	private String url;
	private String setUrl;
	
	private int curClick = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_password);
		parseUrl();
		initView();
		initTitle();
		setContent();
		setListeners();
	}
	private void setContent() {
		// TODO Auto-generated method stub
		 String phone = getIntent().getStringExtra("phone");
		 if(Tool.isBlank(phone)) {
			 dialog = KdlcDialog.showAlertDialog(ModifyPasswordActivity.this, new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								ModifyPasswordActivity.this.finish();
							}
						}, "网络出错，请稍后再试");
		 }
		 tvPhoneNumber.setText(Tool.changeMobile(phone));
	}
	private void initView() {
		// TODO Auto-generated method stub
		parentView = (ResizeRelativeLayout) this.findViewById(R.id.ParentView);
		parentView.setResizeListener(this);
		scrollView = (ScrollView) findViewById(R.id.scrollview);
		title = (TitleView) findViewById(R.id.title);
		etOldPwd = (EditText) findViewById(R.id.et_old_pwd);
		etNewPwd = (EditText) findViewById(R.id.et_new_pwd);
		etNewPwdAgain = (EditText) findViewById(R.id.et_new_pwd_again);
		tvPhoneNumber = (TextView) findViewById(R.id.tv_phone_number);
		tvComplete = (TextView) findViewById(R.id.tv_complete);
		tvPassword = (TextView) findViewById(R.id.tv_password);
		//etOldPwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
		//etNewPwd.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
		//etNewPwdAgain.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		
	}
	private void initTitle() {
		// TODO Auto-generated method stub
		title.setTitle("修改登录密码");
		title.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ModifyPasswordActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}
	private void parseUrl() {
		// TODO Auto-generated method stub
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = this.getApplicationContext().getActionUrl(G.GCK_API_POST_USER_CHANGE_PWD);
			setUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_USER_RESET_PASSWORD_CODE);
		} else {
			url = G.URL_POST_USER_CHANGE_PWD;
			setUrl = G.URL_POST_USER_RESET_PASSWORD_CODE;
		}
	}
	private void setListeners() {
		// TODO Auto-generated method stub
		textChange passwordChange = new textChange();
		etOldPwd.addTextChangedListener(passwordChange);
		etNewPwd.addTextChangedListener(passwordChange);
		etNewPwdAgain.addTextChangedListener(passwordChange);
		
		etOldPwd.setOnTouchListener(clickListener);
		etNewPwd.setOnTouchListener(clickListener);
		etNewPwdAgain.setOnTouchListener(clickListener);
		
		tvPassword.setOnClickListener(password);
	}
	
	private OnClickListener password = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (Tool.isMobileNO(getIntent().getStringExtra("phone"))) {
				Intent intent = new Intent(getApplicationContext(), BackPasswordActivity.class);
				intent.putExtra("phone",KDLCApplication.app.getSessionVal("username"));
				intent.putExtra("real_verify_status", KDLCApplication.app.getSessionVal("real_verify_status"));
				intent.putExtra("find_pwd", "find_pwd");
				startActivity(intent);
				finish();
//				HttpParams params = new HttpParams();
//				params.add("phone", KDLCApplication.app.getSession().get("username"));
//				params.add("type", "find_pwd");
//				dialog = KdlcDialog.showProgressDialog(ModifyPasswordActivity.this, "发送验证码...");
//				sendHttpPost(setUrl, params, passwdListener);
			} 
			
		}
	};
	
	private OnClickListener complete = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String old_pwd = etOldPwd.getText().toString().trim();
			String new_pwd = etNewPwd.getText().toString().trim();
			String new_pwd_again = etNewPwdAgain.getText().toString().trim();
			if (etOldPwd.getText().length()<6) {
				KdlcDialog.showToast("原密码不正确");
			}else if (etNewPwd.getText().length()<6) {
				KdlcDialog.showToast("密码需由6~16位数字或字母组成");
			}else if(Tool.isBlank(old_pwd)) {
				KdlcDialog.showToast("旧密码不能为空");
			} else if(Tool.isBlank(new_pwd)) {
				KdlcDialog.showToast("新密码不能为空");
			} else if(Tool.isBlank(new_pwd_again)) {
				KdlcDialog.showToast("请再次输入新密码");
			} else if (!new_pwd.equals(new_pwd_again)) {
				KdlcDialog.showErrorToast("两次输入的密码不一致");
			}else {
				dialog = KdlcDialog.showProgressDialog(ModifyPasswordActivity.this, "正在提交...");
				HttpParams params = new HttpParams();
				params.add("old_pwd", old_pwd);
				params.add("new_pwd", new_pwd);
				sendHttpPost(url, params, responseListener1);
			}
		}
	};
	
	Listener<JSONObject> responseListener1 = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject result) {
			// TODO Auto-generated method stub
			try {
				dialog.cancel();
				if (result.getInt("code") == 0) {
					KdlcDialog.showBottomToast("登录密码修改成功");
		    		finish();
				} else {
					KdlcDialog.showInformDialog(ModifyPasswordActivity.this,result.getString("message"));
				}
			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
				KdlcDialog.showBottomToast("");
			}
		}

	};
	
	Listener<JSONObject> passwdListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject result) {
			// TODO Auto-generated method stub
			dialog.cancel();
			try {
				if (result.getInt("code") == 0) {
					Intent intent = new Intent(getApplicationContext(), BackPasswordActivity.class);
					intent.putExtra("phone",KDLCApplication.app.getSession().get("username"));
					intent.putExtra("real_verify_status", KDLCApplication.app.getSessionVal("real_verify_status"));
					intent.putExtra("find_pwd", "find_pwd");
					startActivity(intent);
					finish();
				} else {
					KdlcDialog.showInformDialog(ModifyPasswordActivity.this, result.getString("message"));
				}

			} catch (Exception ex) {
				// TODO: handle exception
				KdlcDialog.showNetErrToast("请求失败，请稍候再试");
				ex.printStackTrace();
			}
		}

	};

	
	@Override
	public void onResize(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		if(oldw != 0) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (curClick == R.id.et_new_pwd_again || curClick == R.id.et_new_pwd) {
						scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					}
					setFocus(curClick);
					// etOldPwd.requestFocus();
				}
			});
		}
	}
	
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
			if (etOldPwd.getText().length() >0 ) {
				Sign1 = true;
			} else {
				Sign1 = false;
			}
			boolean Sign2 = false;
			if (etNewPwd.getText().length() > 0
					&& etNewPwdAgain.getText().length() > 0) {
				Sign2 = true;
			} else {
				Sign2 = false;
			}

			if (Sign1 == true && Sign2 == true) {
				tvComplete.setBackgroundResource(R.drawable.btn_red_background);
				tvComplete.setClickable(true);
				tvComplete.setOnClickListener(complete);
			} else {
				tvComplete.setBackgroundResource(R.drawable.btn_grey_background);
				tvComplete.setClickable(false);
			}
		}

	}
	
	private OnTouchListener clickListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionevent) {
			// TODO Auto-generated method stub
			curClick = view.getId();
			if (motionevent.getAction() == MotionEvent.ACTION_UP) {
				switch (view.getId()) {
				case R.id.et_old_pwd:
					break;
				case R.id.et_new_pwd:
					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					etNewPwd.requestFocus();
					break;
				case R.id.et_new_pwd_again:
					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					etNewPwdAgain.requestFocus();
					break;
				}
			}

			return false;
		}
	};

	private void setFocus(int resId) {
		switch (resId) {
		case R.id.et_old_pwd:
			etOldPwd.requestFocus();
			break;
		case R.id.et_new_pwd:
			etNewPwd.requestFocus();
			break;
		case R.id.et_new_pwd_again:
			etNewPwdAgain.requestFocus();
			break;
		}
	}
}
