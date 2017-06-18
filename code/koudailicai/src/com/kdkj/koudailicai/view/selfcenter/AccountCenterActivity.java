package com.kdkj.koudailicai.view.selfcenter;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.UserInfoRecord;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.product.KdbDetailActivity;
import com.kdkj.koudailicai.view.selfcenter.password.ModifyPasswordActivity;
import com.kdkj.koudailicai.view.selfcenter.password.ModifyPasswordTradingActivity;
import com.kdkj.koudailicai.view.selfcenter.Gesture.GestureEditActivity;
import com.kdkj.koudailicai.view.selfcenter.Gesture.GestureVerifyActivity;
import com.kdkj.koudailicai.view.selfcenter.bankcard.AddBankCardActivity;
import com.kdkj.koudailicai.view.selfcenter.bankcard.ConfirmIdentityActivity;
import com.kdkj.koudailicai.view.selfcenter.bankcard.UserBankCardInfoActivity;
import com.kdkj.koudailicai.view.selfcenter.password.SetTradPwdActivity;


public class AccountCenterActivity extends BaseActivity {
	private TitleView title_account_center;
	private RelativeLayout rl_account,rl_realname,rl_id_number,rl_bank_card,rl_key,rl_change;
	private TextView tv_key_trading;
	private TextView tv_account_real,tv_realname_real,tv_id_number_real,tv_bank_card_real;
	private RelativeLayout rl_key_image,rl_key_login,rl_key_trading;
	private RelativeLayout rl_lock,rl_gestures_on,rl_gestures_off,rl_image_gestures_on,rl_image_gestures_off;
	private ImageView iv_account_real,iv_realname_real,iv_id_number_real,iv_bank_card_real;
	//size
	private int screenHeight;
	//
	private double rl_accountHeight = 0.091;
	private double rl_realnameHeight = 0.091;
	private double rl_id_numberHeight = 0.091;
	private double rl_bank_cardHeight = 0.091;
	private double rl_keyHeight = 0.091;
	private double rl_key_loginHeight = 0.091;
	private double rl_key_tradingHeight = 0.091;
	private double rl_key_imageHeight = 0.230;
	private double rl_gesturesHeight = 0.091;
	
	private String userName;
	private String id_card;
	private String realName;
	private String setPayPwd;
	private String realVerify;
	private String gesture;
	private Boolean flag = true;
	
	private String getLoginUserInfoUrl;
	private UserInfoRecord userInfoRecord = new UserInfoRecord();
	private RelativeLayout nameLayout;
	private RelativeLayout numberLayout;
	private RelativeLayout cardLayout;
	private AlertDialog alterDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_center);
		initView();
		initSize();
		initTitle();
		getLoginUserInfo();
		setListener();
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		userName = KDLCApplication.app.getSession().get("username");
		id_card = KDLCApplication.app.getSession().get("id_card");
		realName = KDLCApplication.app.getSession().get("realname");
		setPayPwd = KDLCApplication.app.getSession().get("set_paypwd_status");
		if (Tool.isBlank(setPayPwd) || !setPayPwd.equals("1")) {
			tv_key_trading.setText("设置交易密码");
		}else {
			tv_key_trading.setText("修改交易密码");
		}
		realVerify = KDLCApplication.app.getSession().get("real_verify_status");
		if (!(userName.equals(""))) {
			tv_account_real.setText(Tool.changeMobile(userName));
		}
		if (realVerify.equals("1")) {
			if (!(id_card.equals(""))) {
				tv_id_number_real.setText(Tool.changeIdentity(id_card));
				numberLayout.setVisibility(View.GONE);
				rl_id_number.setClickable(false);
			}else {
				tv_id_number_real.setVisibility(View.GONE);
				numberLayout.setVisibility(View.VISIBLE);
			}
			if (!(realName.equals(""))) {
				tv_realname_real.setText(Tool.changeName(realName));
				nameLayout.setVisibility(View.GONE);
				rl_realname.setClickable(false);
			}else {
				tv_realname_real.setVisibility(View.GONE);
				nameLayout.setVisibility(View.VISIBLE);
			}
		}
		if (KDLCApplication.app.sessionEqual("card_bind_status", "1")) {
			tv_bank_card_real.setVisibility(View.VISIBLE);
			tv_bank_card_real.setText(R.string.account_center_bankcard_real);
			cardLayout.setVisibility(View.GONE);
		}else {
			tv_bank_card_real.setVisibility(View.GONE);
			cardLayout.setVisibility(View.VISIBLE);
		}
		if (KDLCApplication.app.getSession().get("gesture").equals("0")) {
			rl_lock.setVisibility(View.GONE);
			rl_gestures_off.setVisibility(View.VISIBLE);
			flag = true;
		}else {	
			rl_lock.setVisibility(View.VISIBLE);
			rl_gestures_off.setVisibility(View.GONE);
			flag = false;
		}
	}
	private void setContent() {
		userName = KDLCApplication.app.getSession().get("username");
		id_card = KDLCApplication.app.getSession().get("id_card");
		realName = KDLCApplication.app.getSession().get("realname");
		setPayPwd = KDLCApplication.app.getSession().get("set_paypwd_status");
		if (Tool.isBlank(setPayPwd) || !setPayPwd.equals("1")) {
			tv_key_trading.setText("设置交易密码");
		}
		realVerify = KDLCApplication.app.getSession().get("real_verify_status");
		if (!(userName.equals(""))) {
			tv_account_real.setText(Tool.changeMobile(userName));
		}
		if (realVerify.equals("1")) {
			if (!(id_card.equals(""))) {
				tv_id_number_real.setText(Tool.changeIdentity(id_card));
				numberLayout.setVisibility(View.GONE);
				rl_id_number.setClickable(false);
			}else {
				tv_id_number_real.setVisibility(View.GONE);
				numberLayout.setVisibility(View.VISIBLE);
				
			}
			if (!(realName.equals(""))) {
				tv_realname_real.setText(Tool.changeName(realName));
				nameLayout.setVisibility(View.GONE);
				rl_realname.setClickable(false);
			}else {
				tv_realname_real.setVisibility(View.GONE);
				nameLayout.setVisibility(View.VISIBLE);
			}
		}
		if (KDLCApplication.app.sessionEqual("card_bind_status", "1")) {
			tv_bank_card_real.setVisibility(View.VISIBLE);
			tv_bank_card_real.setText(R.string.account_center_bankcard_real);
			cardLayout.setVisibility(View.GONE);
		}else {
			tv_bank_card_real.setVisibility(View.GONE);
			cardLayout.setVisibility(View.VISIBLE);
		}
		if (KDLCApplication.app.getSession().get("gesture").equals("0")) {
			rl_lock.setVisibility(View.GONE);
			flag = true;
		}else {	
			rl_gestures_off.setVisibility(View.GONE);
			flag = false;
		}
	}
	private void initTitle() {
		title_account_center = (TitleView) findViewById(R.id.title_account_center);
		title_account_center.setTitle(R.string.account_center);
		title_account_center.showLeftButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AccountCenterActivity.this.finish();
			}
		});
		title_account_center.setLeftImageButton(R.drawable.back);
		title_account_center.setLeftTextButton("返回");
	}
	
	private void setListener() {
		rl_key_login.setOnClickListener(keyLogin);
		rl_key_trading.setOnClickListener(keyTrading);
		rl_bank_card.setOnClickListener(bankCard);
		rl_image_gestures_off.setOnClickListener(gesturesOff);
		rl_image_gestures_on.setOnClickListener(gesturesOn);
		rl_realname.setOnClickListener(realname);
		rl_id_number.setOnClickListener(idCard);
		rl_change.setOnClickListener(change);
		
		
	}
	private OnClickListener gesturesOff = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			flag = false;
			Intent intent = new Intent(getApplicationContext(), GestureEditActivity.class);
			startActivity(intent);
			//AccountCenterActivity.this.finish();
			
		}
	};
	private OnClickListener gesturesOn = new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getApplicationContext(), GestureVerifyActivity.class);
			intent.putExtra("gestureFlag", "1");
			startActivity(intent);
			//AccountCenterActivity.this.finish();
			
		}
	};
	private OnClickListener change = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getApplicationContext(), GestureVerifyActivity.class);
			 intent.putExtra("gestureCode", "1");
			 startActivity(intent);
			 //AccountCenterActivity.this.finish();
		}
	};
	private OnClickListener realname = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (KDLCApplication.app.sessionEqual("real_verify_status", "1")) {
				//Toast.makeText(getApplicationContext(), "您已进行过实名认证", Toast.LENGTH_SHORT).show();
			}else {
				Intent intent = new Intent(getApplicationContext(), ConfirmIdentityActivity.class);
				startActivity(intent);
			}
		}
	};
	private OnClickListener idCard = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			if (KDLCApplication.app.sessionEqual("real_verify_status", "1")) {
				//KdlcDialog.showToast("您已进行过实名认证");
			}else {
				Intent intent = new Intent(getApplicationContext(), ConfirmIdentityActivity.class);
				startActivity(intent);
				
			}
		}
	};
	private OnClickListener gestures = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (flag==false) {
				Intent intent = new Intent(getApplicationContext(), GestureVerifyActivity.class);
				intent.putExtra("gestureFlag", "1");
				startActivity(intent);
				//KDLCApplication.app.logout1();
				//rl_change.setVisibility(View.GONE);
				//flag = true;				
				AccountCenterActivity.this.finish();
			}else {
				flag = false;
				Intent intent = new Intent(getApplicationContext(), GestureEditActivity.class);
				startActivity(intent);
				AccountCenterActivity.this.finish();
				
			}
		}
	};
	private OnClickListener bankCard = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(KDLCApplication.app.sessionEqual("real_verify_status", "1"))
			{
				if (KDLCApplication.app.sessionEqual("card_bind_status", "1")) {
					//Toast.makeText(getApplicationContext(), "您已绑定银行卡号", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(AccountCenterActivity.this,UserBankCardInfoActivity.class);
				    startActivity(intent);			    
				}else {
					Intent intent = new Intent(getApplicationContext(), AddBankCardActivity.class);
					startActivity(intent);
				}
			}else {
				alterDialog=KdlcDialog.showConfirmDialog(AccountCenterActivity.this, true, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alterDialog.cancel();
						Intent intent=new Intent(AccountCenterActivity.this,ConfirmIdentityActivity.class);
						startActivity(intent);
					}
				}, "您还没进行实名认证，请先进行实名认证");
			}
			
		}
	};
	private OnClickListener keyTrading = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (setPayPwd.equals("1")) {
				Intent intent = new Intent(getApplicationContext(), ModifyPasswordTradingActivity.class);
				//intent.putExtra("phone", userName);
				startActivity(intent);
			}else {
				if (realVerify.equals("1")) {
					if (KDLCApplication.app.sessionEqual("card_bind_status", "1")) {
						Intent intent = new Intent(getApplicationContext(), SetTradPwdActivity.class);
						startActivity(intent);
					}else {
						alterDialog=KdlcDialog.showConfirmDialog(AccountCenterActivity.this, true, new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								alterDialog.cancel();
								Intent intent=new Intent(AccountCenterActivity.this,AddBankCardActivity.class);
								startActivity(intent);
							}
						}, "您还没绑定银行卡，请先绑定银行卡");
					}
				}else {
					alterDialog=KdlcDialog.showConfirmDialog(AccountCenterActivity.this, true, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							alterDialog.cancel();
							Intent intent=new Intent(AccountCenterActivity.this,ConfirmIdentityActivity.class);
							startActivity(intent);
						}
					}, "您还没进行实名认证，请先进行实名认证");
				}
			}
		}
	};
	private OnClickListener keyLogin = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), ModifyPasswordActivity.class);
			intent.putExtra("phone", userName);
			startActivity(intent);
		}
		
	};
	private void initView() {
		rl_account = (RelativeLayout) findViewById(R.id.rl_account);
		rl_realname = (RelativeLayout) findViewById(R.id.rl_realname);
		rl_id_number = (RelativeLayout) findViewById(R.id.rl_id_number);
		rl_bank_card = (RelativeLayout) findViewById(R.id.rl_bank_card);
		
		rl_key = (RelativeLayout) findViewById(R.id.rl_key);
		rl_key_login = (RelativeLayout) findViewById(R.id.rl_key_login);
		rl_key_trading = (RelativeLayout) findViewById(R.id.rl_key_trading);
		rl_key_image = (RelativeLayout) findViewById(R.id.rl_key_image);
		
		rl_lock = (RelativeLayout) findViewById(R.id.rl_lock);
		rl_gestures_on = (RelativeLayout) findViewById(R.id.rl_gestures_on);
		rl_gestures_off = (RelativeLayout) findViewById(R.id.rl_gestures_off);
		rl_image_gestures_on = (RelativeLayout) findViewById(R.id.rl_image_gestures_on);
		rl_image_gestures_off = (RelativeLayout) findViewById(R.id.rl_image_gestures_off);
		
		rl_change = (RelativeLayout) findViewById(R.id.rl_change);
		
		tv_key_trading = (TextView) findViewById(R.id.tv_key_trading);
		tv_account_real = (TextView) findViewById(R.id.tv_account_real);
		tv_realname_real = (TextView) findViewById(R.id.tv_realname_real);
		tv_id_number_real = (TextView) findViewById(R.id.tv_id_number_real);
		tv_bank_card_real = (TextView) findViewById(R.id.tv_bank_card_real);
		
		iv_account_real = (ImageView) findViewById(R.id.iv_account);
		iv_realname_real = (ImageView) findViewById(R.id.iv_realname_real);
		iv_id_number_real = (ImageView) findViewById(R.id.iv_id_number_real);
		iv_bank_card_real = (ImageView) findViewById(R.id.iv_bank_card_real);
		
		nameLayout=(RelativeLayout)findViewById(R.id.namelayout);
		numberLayout=(RelativeLayout)findViewById(R.id.numberlayout);
		cardLayout=(RelativeLayout)findViewById(R.id.cardlayout);
	}
	private void initSize() {
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		
		RelativeLayout.LayoutParams rl_accountLayoutParams = (RelativeLayout.LayoutParams) rl_account.getLayoutParams();
		rl_accountLayoutParams.height = (int) (screenHeight*rl_accountHeight);
		rl_account.setLayoutParams(rl_accountLayoutParams);
		
		RelativeLayout.LayoutParams rl_realnameLayoutParams = (RelativeLayout.LayoutParams) rl_realname.getLayoutParams();
		rl_realnameLayoutParams.height = (int) (screenHeight*rl_realnameHeight);
		rl_realname.setLayoutParams(rl_realnameLayoutParams);
		
		RelativeLayout.LayoutParams rl_id_numberLayoutParams = (RelativeLayout.LayoutParams) rl_id_number.getLayoutParams();
		rl_id_numberLayoutParams.height = (int) (screenHeight*rl_id_numberHeight);
		rl_id_number.setLayoutParams(rl_id_numberLayoutParams);
		
		RelativeLayout.LayoutParams rl_bank_cardLayoutParams = (RelativeLayout.LayoutParams) rl_bank_card.getLayoutParams();
		rl_bank_cardLayoutParams.height = (int) (screenHeight*rl_bank_cardHeight);
		rl_bank_card.setLayoutParams(rl_bank_cardLayoutParams);
		
		RelativeLayout.LayoutParams rl_keyLayoutParams = (RelativeLayout.LayoutParams) rl_key.getLayoutParams();
		rl_keyLayoutParams.height = (int) (screenHeight*rl_keyHeight*2);
		rl_key.setLayoutParams(rl_keyLayoutParams);
		
		RelativeLayout.LayoutParams rl_key_loginLayoutParams = (RelativeLayout.LayoutParams) rl_key_login.getLayoutParams();
		rl_key_loginLayoutParams.height = (int) (screenHeight*rl_key_loginHeight);
		rl_key_login.setLayoutParams(rl_key_loginLayoutParams);
		
		
		RelativeLayout.LayoutParams rl_key_tradingLayoutParams = (RelativeLayout.LayoutParams) rl_key_trading.getLayoutParams();
		rl_key_tradingLayoutParams.height = (int) (screenHeight*rl_key_tradingHeight);
		rl_key_trading.setLayoutParams(rl_key_tradingLayoutParams);
		
		RelativeLayout.LayoutParams rl_key_imageLayoutParams = (RelativeLayout.LayoutParams) rl_key_image.getLayoutParams();
		rl_key_imageLayoutParams.height = (int) (screenHeight*rl_key_imageHeight);
		rl_key_image.setLayoutParams(rl_key_imageLayoutParams);
		
		RelativeLayout.LayoutParams rl_lockLayoutParams = (RelativeLayout.LayoutParams) rl_lock.getLayoutParams();
		rl_lockLayoutParams.height = (int) (screenHeight*rl_keyHeight*2);
		rl_lock.setLayoutParams(rl_lockLayoutParams);
		
		RelativeLayout.LayoutParams rl_gestures_offLayoutParams = (RelativeLayout.LayoutParams) rl_gestures_off.getLayoutParams();
		rl_gestures_offLayoutParams.height = (int) (screenHeight*rl_gesturesHeight);
		rl_gestures_off.setLayoutParams(rl_gestures_offLayoutParams);
		
		RelativeLayout.LayoutParams rl_gestures_onLayoutParams = (RelativeLayout.LayoutParams) rl_gestures_on.getLayoutParams();
		rl_gestures_onLayoutParams.height = (int) (screenHeight*rl_gesturesHeight);
		rl_gestures_on.setLayoutParams(rl_gestures_onLayoutParams);
		
		RelativeLayout.LayoutParams rl_changeLayoutParams = (RelativeLayout.LayoutParams) rl_change.getLayoutParams();
		rl_changeLayoutParams.height = (int) (screenHeight*rl_gesturesHeight);
		rl_change.setLayoutParams(rl_changeLayoutParams);
	}
	
	public void getLoginUserInfo(){
		dialog = KdlcDialog.showProgressDialog(AccountCenterActivity.this);
		if(this.getApplicationContext().isGlobalConfCompleted()){
			getLoginUserInfoUrl = getApplicationContext().getActionUrl(G.GCK_API_GET_LOGIN_USER_INFO);
		}else{
			getLoginUserInfoUrl = G.URL_GET_LOGIN_USER_INFO;
		}
		sendHttpGet(getLoginUserInfoUrl, getLoginUserInfoListener,new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				dialog.cancel();
				setContent();
				if(NoConnectionError.class.isInstance(error)) {				
					KdlcDialog.showBottomToast("网络未连接，请检查网络设置后再试");
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
		});
	}
	
	private  Listener<JSONObject> getLoginUserInfoListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
	       try {
	    	   dialog.cancel();
			if(response.getInt("code") == 0){
				JSONObject data;
				data = response.getJSONObject("base_info");
				userInfoRecord.setUid(data.getInt("uid"));
				userInfoRecord.setUsername(data.getString("username"));
				userInfoRecord.setRealname(data.getString("realname"));
				userInfoRecord.setId_card(data.getString("id_card"));
				userInfoRecord.setReal_verify_status(data.getString("real_verify_status"));
				userInfoRecord.setCard_bind_status(data.getString("card_bind_status"));
				userInfoRecord.setSet_paypwd_status(data.getString("set_paypwd_status"));
				userInfoRecord.setIs_novice(data.getString("is_novice"));
				HashMap<String, String> vals = new HashMap<String, String>();
				vals.put("username", "" + data.getString("username"));
				vals.put("id_card", data.getString("id_card"));
				vals.put("realname", data.getString("realname"));
                vals.put("real_verify_status", ""+data.getString("real_verify_status"));
				vals.put("uid", "" + data.getString("uid"));
				vals.put("set_paypwd_status", data.getString("set_paypwd_status"));
				vals.put("card_bind_status", data.getString("card_bind_status"));
				vals.put("is_novice", data.getString("is_novice"));
				KDLCApplication.app.getSession().set(vals);
			   }else if(response.getInt("code") == -2){
				   dialog = KdlcDialog.showLoginDialog(AccountCenterActivity.this);
			   }else{
				    KdlcDialog.showInformDialog(AccountCenterActivity.this,
							response.getString("message"));
			   }
			setContent();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		}
	};
}
