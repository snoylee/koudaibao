package com.kdkj.koudailicai.view.selfcenter.password;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;

public class ModifyPasswordTradingActivity extends BaseActivity implements
		ResizeListener {
	private TitleView title;
	private EditText etOldPwd, etNewPwd, etNewPwdAgain;
	private TextView tvPhoneNumber, tvComplete, tvPassword;

	private ResizeRelativeLayout parentView;
	private ScrollView scrollView;
	private Handler mHandler = new Handler();

	private String url;
	private String setUrl;
	private String result = "";

	private int curClick = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modify_password_trading);
		registerBoradcastReceiver();
		parseUrl();
		initView();
		initTitle();
		setContent();
		setListeners();
	}

	private void setContent() {
		String phone = KDLCApplication.app.getSession().get("username");// getIntent().getStringExtra("phone");
		tvPhoneNumber.setText(Tool.changeMobile(phone));
	}

	private void initView() {
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
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		title.setTitle("修改交易密码");
		title.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ModifyPasswordTradingActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}

	private void parseUrl() {
		// TODO Auto-generated method stub
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = this.getApplicationContext().getActionUrl(
					G.GCK_API_GET_USER_CHANGE_PAY_PASSWORD);
			setUrl = getApplicationContext().getActionUrl(
					G.GCK_API_POST_USER_RESET_PASSWORD_CODE);
		} else {
			url = G.URL_GET_USER_CHANGE_PAY_PASSWORD;
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

			if (Tool.isMobileNO(KDLCApplication.app.getSessionVal("username"))) {
				Intent intent = new Intent(getApplicationContext(), BackPasswordActivity.class);
				intent.putExtra("phone", KDLCApplication.app.getSessionVal("username"));
				intent.putExtra("find_pwd", "find_pay_pwd");
				startActivity(intent);
				finish();
				/*HttpParams params = new HttpParams();
				params.add("phone",
						KDLCApplication.app.getSession().get("username"));
				params.add("type", "find_pay_pwd");
				dialog = KdlcDialog.showProgressDialog(
						ModifyPasswordTradingActivity.this, "发送验证码...");
				sendHttpPost(setUrl, params, passwdListener);*/
			}
		}
	};
	private OnClickListener complete = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (etNewPwd.getText().length() == 6
					&& etNewPwdAgain.getText().length() == 6) {
				if (etNewPwd.getText().toString()
						.equals(etNewPwdAgain.getText().toString())) {
					dialog = KdlcDialog.showProgressDialog(
							ModifyPasswordTradingActivity.this, "正在修改...");
					HttpParams params = new HttpParams();
					params.add("old_pwd", etOldPwd.getText().toString());
					params.add("new_pwd", etNewPwd.getText().toString());
					sendHttpPost(url, params, responseListener1);
				} else {
					KdlcDialog.showToast("两次输入的密码不一致");
				}
			} else {
				KdlcDialog.showToast("交易密码只能为6位数字");
			}
		}
	};

	Listener<JSONObject> responseListener1 = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject result) {
			// TODO Auto-generated method stub
			dialog.cancel();
			try {
				if (result.getInt("code") == 0) {
					KdlcDialog.showBottomToast("交易密码修改成功");
					ModifyPasswordTradingActivity.this.finish();
				} else {
					KdlcDialog.showInformDialog(ModifyPasswordTradingActivity.this,result.getString("message"));
				}
			} catch (Exception ex) {
				// TODO: handle exception
				KdlcDialog.showBottomToast("");
				ex.printStackTrace();
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
					Intent intent = new Intent(getApplicationContext(),
							BackPasswordActivity.class);
					intent.putExtra("phone",
							KDLCApplication.app.getSessionVal("username"));
					intent.putExtra("find_pwd", "find_pay_pwd");
					startActivity(intent);
					finish();
				} else {
					KdlcDialog.showInformDialog(ModifyPasswordTradingActivity.this, result.getString("message"));
				}

			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
		}

	};

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(G.PAY_PASSWORD_SETTED)) {
				ModifyPasswordTradingActivity.this.finish();
			}
		}
	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(G.PAY_PASSWORD_SETTED);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	public void onResize(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		if (oldw != 0) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					LogUtil.info("resize");
					if (curClick == R.id.et_new_pwd_again
							|| curClick == R.id.et_new_pwd) {
						scrollView.fullScroll(ScrollView.FOCUS_DOWN);

					}
					setFocus(curClick);
					// etOldPwd.requestFocus();
				}
			});
		}
	}

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
			if (etOldPwd.getText().length() >= 6) {
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
				tvComplete
						.setBackgroundResource(R.drawable.btn_grey_background);
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
					LogUtil.info("旧密码框获得焦点");
					break;
				case R.id.et_new_pwd:
					LogUtil.info("新密码框获得焦点");
					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					etNewPwd.requestFocus();
					break;
				case R.id.et_new_pwd_again:
					LogUtil.info("新密码框2获得焦点");
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

