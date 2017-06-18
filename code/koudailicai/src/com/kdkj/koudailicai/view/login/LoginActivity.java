package com.kdkj.koudailicai.view.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.SelfCenterRecord;
import com.kdkj.koudailicai.domain.UserInfoRecord;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.more.FeedBackActivity;
import com.kdkj.koudailicai.view.register.RegisterPhoneActivity;
import com.kdkj.koudailicai.view.selfcenter.password.BackPasswordActivity;
import com.tencent.bugly.crashreport.CrashReport;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	private String LOG_TAG = LoginActivity.class.getName();
	private TitleView loginTitle;
	private EditText loginName;
	private EditText loginPassword;
	private TextView buttonLogin;
	private String loginUrl;
	private UserInfoRecord userInfoRecord;
	private SelfCenterRecord selfCenterRecord;
	private TextView lostloginpassword;
	private String resetUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginin);
		parseUrl();
		findViews();
		initTitle();
		setListeners();
		initContents();
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			loginUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_LOGIN);
			resetUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_USER_STATE);
			Log.d(LOG_TAG, "Succeed To Get G Info:" + loginUrl);
		} else {
			loginUrl = G.URL_POST_LOGIN;
			resetUrl = G.URL_POST_USER_STATE;
			Log.d(LOG_TAG, "Get Local Url:" + loginUrl);
		}
	}

	private void findViews() {
		loginTitle = (TitleView) findViewById(R.id.loginintitle);
		loginName = (EditText) findViewById(R.id.logininname);
		loginPassword = (EditText) findViewById(R.id.logininpassword);
		buttonLogin = (TextView) findViewById(R.id.buttonlogin);
		lostloginpassword = (TextView) findViewById(R.id.lostloginpassword);
	}

	private void initTitle() {
		loginTitle.setTitle(R.string.loginintitle);
		loginTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!Tool.isBlank(getIntent().getStringExtra("gestureToMain"))&&getIntent().getStringExtra("gestureToMain").equals("1"))
				{
					Intent intent=new Intent(LoginActivity.this,MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					LoginActivity.this.finish();
				}else {
					LoginActivity.this.finish();
				}
					
			}
		});
		loginTitle.setLeftImageButton(R.drawable.back);
		loginTitle.setLeftTextButton("返回");
		loginTitle.showRightButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterPhoneActivity.class);
				intent.putExtra("LoginType", "1");
				startActivity(intent);
			}
		});
		loginTitle.setRightTextButton("注册");
	}

	private void setListeners() {
		textChange passwordChange = new textChange();
		loginName.addTextChangedListener(passwordChange);
		loginPassword.addTextChangedListener(passwordChange);		
		lostloginpassword.setOnClickListener(goPassword);
	}

	// 忘记密码
	private OnClickListener goPassword = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (loginName.getText().toString().equals("")) {
				KdlcDialog.showToast("请输入手机号");
			} else if (Tool.isMobileNO(loginName.getText().toString())) {
					HttpParams params = new HttpParams();
					params.add("phone", loginName.getText().toString());
					dialog = KdlcDialog.showProgressDialog(LoginActivity.this);
					sendHttpPost(resetUrl, params, passwdResponseListener);
			} else {
				KdlcDialog.showToast("请输入正确的手机号");
			}
		}
	};
	
	Listener<JSONObject> passwdResponseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject result) {
			// TODO Auto-generated method stub
			dialog.cancel();
			try {
				if (result.getInt("code") == 0) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("real_verify_status", result.getString("real_verify_status"));
					bundle.putString("phone", loginName.getText().toString().trim()); // 装入数据
					bundle.putString("find_pwd", "find_pwd");
					intent.putExtras(bundle);
					intent.setClass(LoginActivity.this, BackPasswordActivity.class);
					startActivity(intent);
				} else {
					KdlcDialog.showInformDialog(LoginActivity.this ,result.getString("message"));
				}
			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
				KdlcDialog.showBottomToast("");
			}
		}

	};

	private void initContents() {
		String uid = this.getApplicationContext().getSessionVal("uid");
	}

	private OnClickListener goLogin = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (Tool.isMobileNO(loginName.getText().toString())) {
				HttpParams params = new HttpParams();
				params.add("username", loginName.getText().toString());
				params.add("password", loginPassword.getText().toString());
				dialog = KdlcDialog.showProgressDialog(LoginActivity.this, "正在登录...");
				sendHttpPost(loginUrl, params, responseListener);
			} else {
				KdlcDialog.showToast("请输入正确的电话号码");
			}

		}
	};

	Listener<JSONObject> responseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			Log.d(LOG_TAG, "response:" + response.toString());
			dialog.cancel();
			try {
				Log.d(LOG_TAG, "response:" + response.getInt("code"));
				if (response.getInt("code") == 0) {
					JSONObject data;
					data = response.getJSONObject("user");
					JSONObject dataaccount;
					dataaccount = response.getJSONObject("account");
					CrashReport.setUserId(loginName.getText().toString());
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
		    			KdlcDB.deleteByWhere(UserInfoRecord.class, "uid="+data.getInt("uid"));
		    		KdlcDB.addByEntity(userInfoRecord);
					HashMap<String, String> vals = new HashMap<String, String>();
					vals.put("username", "" + loginName.getText().toString());
					vals.put("id_card", data.getString("id_card"));
					vals.put("realname", data.getString("realname"));
                    vals.put("real_verify_status", ""+data.getString("real_verify_status"));
					vals.put("uid", "" + data.getString("uid"));
					vals.put("sessionid", response.getString("sessionid"));
					vals.put("isLogin", "1");
					vals.put("gesture", "0");
					vals.put("set_paypwd_status", data.getString("set_paypwd_status"));
					vals.put("card_bind_status", data.getString("card_bind_status"));
					vals.put("is_novice", data.getString("is_novice"));
					vals.put("selfCenterAutoRefresh", "1");
					vals.put("selfCenterAutoRefreshClick", "1");
					KDLCApplication.app.getSession().set(vals);

		    		selfCenterRecord = (SelfCenterRecord) KdlcDB.findOneByWhere(SelfCenterRecord.class, "uid="+data.getInt("uid"));
					if(selfCenterRecord == null)
						selfCenterRecord = new SelfCenterRecord();
		    		selfCenterRecord.setUid(data.getInt("uid"));
					selfCenterRecord.setCurProfitsDate(dataaccount.getString("lastday_profits_date"));
					selfCenterRecord.setCurProfits(dataaccount.getString("lastday_profits"));
					selfCenterRecord.setTotalProfits(dataaccount.getString("total_profits"));
					selfCenterRecord.setTotalMoney(dataaccount.getString("total_money"));
					selfCenterRecord.setHoldMoney(dataaccount.getString("hold_money"));
					selfCenterRecord.setRemainMoney(dataaccount.getString("remain_money"));
					selfCenterRecord.setNewTradeCount(dataaccount.getString("trade_count"));
					if(Tool.hasCacheData(SelfCenterRecord.class))
						KdlcDB.deleteByWhere(SelfCenterRecord.class, "uid="+data.getInt("uid"));
					KdlcDB.addByEntity(selfCenterRecord);
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(G.TO_FRAGMENT_KEY, G.FRAGMENT_TAG.CENTER);
					intent.setClass(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
					KdlcDialog.showBottomToast("登录成功");
				} else {
					KdlcDialog.showInformDialog(LoginActivity.this ,response.getString("message"));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				KdlcDialog.showBottomToast("");
			}
		}

	};

	// EditText监听器
	class textChange implements TextWatcher {

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
			if (loginName.getText().length() > 0) {
				Sign1 = true;
			} else {
				Sign1 = false;
			}
			boolean Sign2 = false;
			if (loginPassword.getText().length() > 0) {
				Sign2 = true;
			} else {
				Sign2 = false;
			}

			if (Sign1 & Sign2) {
				buttonLogin.setBackgroundResource(R.drawable.btn_red_background);
				buttonLogin.setOnClickListener(goLogin);
			} else {
				buttonLogin.setBackgroundResource(R.drawable.btn_grey_background);
				buttonLogin.setClickable(false);
			}
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(!Tool.isBlank(getIntent().getStringExtra("gestureToMain"))&&getIntent().getStringExtra("gestureToMain").equals("1"))
			{
				Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mainIntent);
				LoginActivity.this.finish();
			}else {
				LoginActivity.this.finish();
			}
		}
		return false;

	}
	
}
