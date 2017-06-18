package com.kdkj.koudailicai.view.login;

import java.util.HashMap;
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
import com.kdkj.koudailicai.view.register.RegisterPhoneActivity;
import com.kdkj.koudailicai.view.selfcenter.Gesture.GestureVerifyActivity;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginAlreadyActivity extends BaseActivity {
	private String LOG_TAG = LoginAlreadyActivity.class.getName();
	private TitleView loginTitle;
	private EditText loginPassword;
	private TextView buttonLogin;
	private TextView loginAlready;
	private String first;
	private String loginUrl;
	private UserInfoRecord userInfoRecord;
	private SelfCenterRecord selfCenterRecord;
	private TextView alreadylostloginpassword;
	private String resetUrl;
	private EditText loginName;
    private TextView registerNew;
    private String sessionUserName;
    private String getPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_already);
		parseUrl();
		getContents();
		init();
		initetitle();
		setListeners();
	}
	
	private void getContents() {
		sessionUserName = KDLCApplication.app.getSessionVal("username");
	}

	private void init() {
		loginTitle = (TitleView) findViewById(R.id.alreadytitle);
		loginAlready = (TextView) findViewById(R.id.logininalready);
		loginPassword = (EditText) findViewById(R.id.alreadylogininpassword);
		buttonLogin = (TextView) findViewById(R.id.alreadybuttonlogin);
		alreadylostloginpassword = (TextView) findViewById(R.id.alreadylostloginpassword);
		loginName = (EditText) findViewById(R.id.logininname);
		registerNew=(TextView)findViewById(R.id.registernew);
		Intent intent = getIntent();
		if (!Tool.isBlank(sessionUserName)) {
			loginTitle.showRightButton(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(LoginAlreadyActivity.this, LoginActivity.class);
					startActivity(intent);
				}
			});
			loginTitle.setRightTextButton("切换账号");
			loginAlready.setText(Tool.changeMobile(sessionUserName));
			loginName.setVisibility(View.GONE);
		} else {
			if (Tool.isBlank(intent.getStringExtra("phone"))) {
				loginAlready.setVisibility(View.GONE);
				registerNew.setVisibility(View.GONE);
				loginTitle.showRightButton(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(LoginAlreadyActivity.this, RegisterPhoneActivity.class);
						intent.putExtra("LoginType", "1");
						startActivity(intent);
					}
				});
				loginTitle.setRightTextButton("注册");
			} else {
				loginTitle.showRightButton(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(LoginAlreadyActivity.this, LoginActivity.class);
						startActivity(intent);
					}
				});
				loginTitle.setRightTextButton("切换账号");
				first = intent.getStringExtra("phone");
				loginAlready.setText(Tool.changeMobile(first));
				loginName.setVisibility(View.GONE);
			}
		}

	}

	private void initetitle() {
		loginTitle.setTitle(R.string.loginintitle);
		loginTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!Tool.isBlank(getIntent().getStringExtra("gestureToMain"))&&getIntent().getStringExtra("gestureToMain").equals("1"))
				{
					Intent intent=new Intent(LoginAlreadyActivity.this,MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					LoginAlreadyActivity.this.finish();
				}else {
					LoginAlreadyActivity.this.finish();
				}
					
			}
		});
		loginTitle.setLeftImageButton(R.drawable.back);
		loginTitle.setLeftTextButton("返回");

	}

	private void setListeners() {
		textChange passwordChange = new textChange();
		loginPassword.addTextChangedListener(passwordChange);
		alreadylostloginpassword.setOnClickListener(gopassword);
		registerNew.setOnClickListener(login);
	}
	
	private OnClickListener login = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.putExtra("LoginType", "1");
			intent.setClass(LoginAlreadyActivity.this, RegisterPhoneActivity.class);
			startActivity(intent);
		}
	};

	// 忘记密码
	private OnClickListener gopassword = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			HttpParams params = new HttpParams();
			getPhone = sessionUserName;
			getPhone = Tool.isBlank(getPhone) ? getIntent().getStringExtra("phone") : getPhone;
			getPhone = Tool.isBlank(getPhone) ? loginName.getText().toString() : getPhone;
			if (Tool.isBlank(getPhone)) {
				KdlcDialog.showToast("请输入手机号");
			} else if(!Tool.isMobileNO(getPhone)) {
				KdlcDialog.showToast("请输入正确的手机号");
			} else {
				dialog = KdlcDialog.showProgressDialog(LoginAlreadyActivity.this);
				params.add("phone", getPhone);
				//params.add("type", "find_pwd");
				sendHttpPost(resetUrl, params, responseListener1);
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
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("real_verify_status", result.getString("real_verify_status"));
					bundle.putString("phone", getPhone);
					bundle.putString("find_pwd", "find_pwd");
					intent.putExtras(bundle);
					intent.setClass(LoginAlreadyActivity.this, BackPasswordActivity.class);
					startActivity(intent);
				} else {
					KdlcDialog.showInformDialog(LoginAlreadyActivity.this ,result.getString("message"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				KdlcDialog.showBottomToast("");
			}

		}

	};

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			loginUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_LOGIN);
			resetUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_USER_STATE);
		} else {
			loginUrl = G.URL_POST_LOGIN;
			resetUrl = G.URL_POST_USER_STATE;
		}
	}

	private OnClickListener goLogin = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			dialog = KdlcDialog.showProgressDialog(LoginAlreadyActivity.this, "正在登录...");
			HttpParams params = new HttpParams();
			if (!Tool.isBlank(sessionUserName)) {
				params.add("username", KDLCApplication.app.getSession().get("username"));
				params.add("password", loginPassword.getText().toString());
				sendHttpPost(loginUrl, params, responseListener);
			} else {
				if (Tool.isBlank(getIntent().getStringExtra("phone"))) {
					params.add("username", loginName.getText().toString());
					params.add("password", loginPassword.getText().toString());
					sendHttpPost(loginUrl, params, responseListener);

				} else {
					params.add("username", first);
					params.add("password", loginPassword.getText().toString());
					sendHttpPost(loginUrl, params, responseListener);
				}
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
					HashMap<String, String> vals = new HashMap<String, String>();
					vals.put("username", data.getString("username"));
					vals.put("uid", "" + data.getString("uid"));
					vals.put("realname", data.getString("realname"));
					vals.put("sessionid", response.getString("sessionid"));
					vals.put("isLogin", "1");
					vals.put("real_verify_status", data.getString("real_verify_status"));
					vals.put("id_card", data.getString("id_card"));
					vals.put("set_paypwd_status", data.getString("set_paypwd_status"));					
					vals.put("card_bind_status", data.getString("card_bind_status"));
					vals.put("gesture", "0");
					vals.put("is_novice", data.getString("is_novice"));
					vals.put("selfCenterAutoRefresh", "1");
					vals.put("selfCenterAutoRefreshClick", "1");
					KDLCApplication.app.getSession().set(vals);
					CrashReport.setUserId(first);
					
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

					if (!Tool.isBlank(getIntent().getStringExtra("toMain"))&& getIntent().getStringExtra("toMain").equals("1")) {
					    Intent intent = new Intent();
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.setClass(LoginAlreadyActivity.this, MainActivity.class);
						intent.putExtra(G.TO_FRAGMENT_KEY, G.FRAGMENT_TAG.CENTER);
						startActivity(intent);
						finish();
						KdlcDialog.showBottomToast("登录成功");
					} else {
						KdlcDialog.showBottomToast("登录成功");
						setResult(G.RET_CODE_LOGIN_SUCCESS);
						finish();
					}
				} else {
					KdlcDialog.showInformDialog(LoginAlreadyActivity.this ,response.getString("message"));
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

			boolean Sign2 = false;
			if (loginPassword.getText().length() > 0) {
				Sign2 = true;
			} else {
				Sign2 = false;
			}

			if (Sign2) {
				buttonLogin
						.setBackgroundResource(R.drawable.btn_red_background);
				buttonLogin.setOnClickListener(goLogin);
			}

			else {
				buttonLogin
						.setBackgroundResource(R.drawable.btn_grey_background);
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
				Intent toMainIntent=new Intent(LoginAlreadyActivity.this,MainActivity.class);
				toMainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(toMainIntent);
				LoginAlreadyActivity.this.finish();
			}else {
				LoginAlreadyActivity.this.finish();
			}
         
		}
		return false;
	}

}
