package com.kdkj.koudailicai.view.selfcenter.holdasset;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeListener;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeRelativeLayout;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.lib.http.MyHttpClient;
import com.kdkj.koudailicai.view.login.LoginAlreadyActivity;
import com.kdkj.koudailicai.view.selfcenter.accountremain.WithdrawActivity;
import com.kdkj.koudailicai.view.selfcenter.password.BackPasswordActivity;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.selfcenter.password.ModifyPasswordTradingActivity;

public class KdbRollOutAccountActivity extends BaseActivity implements ResizeListener{

	private TitleView rollouttitle;
	private EditText polledittixian;
	private EditText polleditpassword;
	private TextView pollbuttontixian;
	private ResizeRelativeLayout rolloutParentView;
	private ScrollView rolloutscrollview;
	private TextView polllostpassword;
	private TextView canrolloutmoney;
    private TextView canrollout;
    
	private String url;
	private Handler mHandler = new Handler();
    private boolean Sign1;
    private boolean Sign2;
    private int curClick = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kdb_rollout_account);
		parseUrl();
		inite();
		initetitle();
		setListeners();
	}

	public void inite() {
		rolloutParentView = (ResizeRelativeLayout) this.findViewById(R.id.rolloutParentView);
		rolloutParentView.setResizeListener(this);
		rolloutscrollview = (ScrollView) findViewById(R.id.rolloutscrollview);
		rollouttitle = (TitleView) findViewById(R.id.rollouttitle);
		polledittixian = (EditText) findViewById(R.id.polledittixian);
		polleditpassword = (EditText) findViewById(R.id.polleditpassword);
		pollbuttontixian = (TextView) findViewById(R.id.pollbuttontixian);
		polllostpassword = (TextView) findViewById(R.id.polllostpassword);
		canrolloutmoney = (TextView) findViewById(R.id.canrolloutmoney);
		canrollout=(TextView)findViewById(R.id.canrollout);
		if(Tool.isBlank(KDLCApplication.app.getSessionVal("realname") ))
		{
			canrollout.setText("您可转出金额：");	
		}else {
			canrollout.setText(Tool.changeName(KDLCApplication.app.getSessionVal("realname")) + "，您可转出金额：");
		}		
		canrolloutmoney.setText(Tool.toDeciDouble(getIntent().getStringExtra("total_holding_asset_money")));
	}
 
	private void initetitle() {
		rollouttitle.setTitle(R.string.rollouttitle);
		rollouttitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				KdbRollOutAccountActivity.this.finish();
			}
		});
		rollouttitle.setLeftImageButton(R.drawable.back);
		rollouttitle.setLeftTextButton("返回");
		rollouttitle.showRightButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(KdbRollOutAccountActivity.this, KdbRollOutListActivity.class);
				startActivity(intent);
			}
		});
		rollouttitle.setRightTextButton("转出记录");
	}

	private void setListeners() {
		textChange tc1 = new textChange();
		polledittixian.addTextChangedListener(rolloutTextWatcher);
		polleditpassword.addTextChangedListener(tc1);
		polllostpassword.setOnClickListener(lostpass);
		polleditpassword.setKeyListener(new DigitsKeyListener(false,false));		
		polledittixian.setOnTouchListener(clickListener);
		polleditpassword.setOnTouchListener(clickListener);
	}

	private OnClickListener lostpass = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("phone",KDLCApplication.app.getSession().get("username"));
			bundle.putString("find_pwd", "find_pay_pwd");
			intent.putExtras(bundle);
			intent.setClass(KdbRollOutAccountActivity.this, BackPasswordActivity.class);
			startActivity(intent);
		}
	};

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = this.getApplicationContext().getActionUrl(G.GCK_API_GET_ROLL_OUT);
		} else {
			url = G.URL_GET_KDB_ROLL_OUT;
		}
	}

	private Listener<JSONObject> responseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			dialog.cancel();
			try {
				if (response.getInt("code") == 0) {
					KDLCApplication.app.setSessionVal("selfCenterAutoRefresh", "1");
					Intent intent = new Intent();
					intent.putExtra("money", Tool.toForDouble(polledittixian.getText().toString()));
					intent.setClass(KdbRollOutAccountActivity.this, KdbRollOutSuccessActivity.class);
					startActivity(intent);
					KdbRollOutAccountActivity.this.finish();
				}else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(KdbRollOutAccountActivity.this);
        		} else {
					KdlcDialog.showInformDialog(KdbRollOutAccountActivity.this ,response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				KdlcDialog.showBottomToast("");
				e.printStackTrace();
			}
		}
		
	};
	
	TextWatcher rolloutTextWatcher = new TextWatcher() {  
        private String temp;   
        @Override  
        public void onTextChanged(CharSequence s, int start, int before, int count) {  
            // TODO Auto-generated method stub  
             temp = s.toString();  
 			Sign1 = polledittixian.getText().length() > 0;
 			if (Sign1 & Sign2) {
				pollbuttontixian.setBackgroundResource(R.drawable.btn_red_background);
				pollbuttontixian.setClickable(true);
				pollbuttontixian.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String rollMoney = polledittixian.getText().toString().trim();
						Double holdMoney = Tool.isBlank(getIntent().getStringExtra("total_holding_asset_money")) ? 0 : 
										   Double.parseDouble(getIntent().getStringExtra("total_holding_asset_money")); 
						if(Tool.isBlank(rollMoney) || Double.parseDouble(rollMoney) <= 0) {
							KdlcDialog.showToast("转出金额必须大于0", Gravity.CENTER, 0, 0);
						} else if(Double.parseDouble(rollMoney)*100 > holdMoney) {
							KdlcDialog.showToast("转出金额不能大于您当前可转金额", Gravity.CENTER, 0, 0);
						} else {
							dialog = KdlcDialog.showProgressDialog(KdbRollOutAccountActivity.this, "正在转出...");
							HttpParams params = new HttpParams();
							params.add("money", rollMoney);
							params.add("pay_password", polleditpassword.getText().toString());
							sendHttpPostCharge(url, params, responseListener);
						}
					}
				});
			} else {
				pollbuttontixian.setBackgroundResource(R.drawable.btn_grey_background);
				pollbuttontixian.setClickable(false);
				pollbuttontixian.setOnClickListener(null);
			}
            
        }  
          
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  

        }  
          
        @Override  
        public void afterTextChanged(Editable s) {  
            // TODO Auto-generated method stub  
             int posDot = temp.indexOf(".");
             if (posDot < 0) return;
        	 if (temp.length() - posDot - 1 > 2)
             {
                 s.delete(posDot + 3, posDot + 4);
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
			Sign2 = false;

			if (polleditpassword.getText().length() == 6) {
				Sign2 = true;
			} else {
				Sign2 = false;
			}

			if (Sign1 & Sign2) {
				pollbuttontixian.setBackgroundResource(R.drawable.btn_red_background);
				pollbuttontixian.setClickable(true);
				pollbuttontixian.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String rollMoney = polledittixian.getText().toString().trim();
						Double holdMoney = Tool.isBlank(getIntent().getStringExtra("total_holding_asset_money")) ? 0 : 
										   Double.parseDouble(getIntent().getStringExtra("total_holding_asset_money")); 
						if(Tool.isBlank(rollMoney) || Double.parseDouble(rollMoney) <= 0) {
							KdlcDialog.showToast("转出金额必须大于0", Gravity.CENTER, 0, 0);
						} else if(Double.parseDouble(rollMoney)*100 > holdMoney) {
							KdlcDialog.showToast("转出金额不能大于您当前可转金额", Gravity.CENTER, 0, 0);
						} else {
							dialog = KdlcDialog.showProgressDialog(KdbRollOutAccountActivity.this, "正在转出...");
							HttpParams params = new HttpParams();
							params.add("money", rollMoney);
							params.add("pay_password", polleditpassword.getText().toString());
							sendHttpPostCharge(url, params, responseListener);
						}
					}
				});
			} else {
				pollbuttontixian.setBackgroundResource(R.drawable.btn_grey_background);
				pollbuttontixian.setClickable(false);
				pollbuttontixian.setOnClickListener(null);
			}
		}

	}

	@Override
	public void onResize(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		if (oldw != 0) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (curClick == R.id.polleditpassword) {
						rolloutscrollview.fullScroll(ScrollView.FOCUS_DOWN);
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
				case R.id.polledittixian:
					rolloutscrollview.fullScroll(ScrollView.FOCUS_DOWN);
					polledittixian.requestFocus();
					break;
				case R.id.polleditpassword:
					rolloutscrollview.fullScroll(ScrollView.FOCUS_DOWN);
					polleditpassword.requestFocus();
					break;
				}
			}

			return false;
		}
	};

	private void setFocus(int resId) {
		switch (resId) {
		case R.id.polledittixian:
			polledittixian.requestFocus();
			break;
		case R.id.polleditpassword:
			polleditpassword.requestFocus();
			break;
		}
	}

}
