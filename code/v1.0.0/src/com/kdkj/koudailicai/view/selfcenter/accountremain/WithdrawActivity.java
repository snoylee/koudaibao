package com.kdkj.koudailicai.view.selfcenter.accountremain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeListener;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeRelativeLayout;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.invest.InvestChargeActivity;
import com.kdkj.koudailicai.view.selfcenter.password.BackPasswordActivity;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;

public class WithdrawActivity extends BaseActivity implements ResizeListener,HttpErrorInterface{
	private ResizeRelativeLayout parentView;
	private TitleView withdrawtitle;
	private TextView buttontixian;
	private EditText edittixian;
	private EditText editpassword;
	private ScrollView scrollView;
	private TextView lostpassword;
	private Handler mHandler = new Handler();
	private float lastY = 0;
	private int screenHeight;
	private RelativeLayout withdrawhead;
	private LinearLayout candrawlayout;
	private TextView candrawmoney, canwithdraw;
	private TextView withdrawname;
	private TextView withdrawlastnumber;
	private String url;
	private String withdrawurl;
	private String getOrderUrl;
    private RelativeLayout withdrawlLayout,withTop;
    private TextView networkLoad;
    private boolean Sign1;
    private ImageView withdrawIcon;
    private int curClick = -1;
    private String canUseMoney;
    private String orderNo;
    private boolean Sign2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.withdraw);
		parseUrl();
		initViews();
		initTitle();
		setListeners();
		initContent();
		getUserAccountInfo();
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			url = getApplicationContext().getActionUrl(G.GCK_API_GET_ACCOUNT_GET);
			withdrawurl = getApplicationContext().getActionUrl(G.GCK_API_POST_ACCOUNT_WITH_DRAW);
			getOrderUrl = getApplicationContext().getActionUrl(G.GCK_API_GET_WITHDRAW_ORDER);
		} else {
			url = G.URL_GET_ACCOUNT_GET;
			withdrawurl = G.URL_POST_ACCOUNT_WITH_DRAW;
			getOrderUrl = G.URL_GET_WITHDRAW_ORDER;
		}
	}
	
	public void initViews() {
		parentView = (ResizeRelativeLayout) this.findViewById(R.id.withdrawParentView);
		parentView.setResizeListener(this);
		withdrawtitle = (TitleView) findViewById(R.id.withdrawtitle);
		buttontixian = (TextView) findViewById(R.id.buttontixian);
		edittixian = (EditText) findViewById(R.id.edittixian);
		editpassword = (EditText) findViewById(R.id.editpassword);
		scrollView = (ScrollView) this.findViewById(R.id.scrollview);
		withdrawhead = (RelativeLayout) this.findViewById(R.id.withdrawhead);
		candrawlayout = (LinearLayout) this.findViewById(R.id.candrawlayout);
		lostpassword = (TextView) findViewById(R.id.lostpassword);
		candrawmoney = (TextView) findViewById(R.id.candrawmoney);
		canwithdraw = (TextView) findViewById(R.id.canwithdraw);
		withdrawname = (TextView) findViewById(R.id.withdrawname);
		withdrawlastnumber = (TextView) findViewById(R.id.withdrawlastnumber);
		withdrawlLayout=(RelativeLayout)findViewById(R.id.withdrawlayout);
		networkLoad=(TextView)findViewById(R.id.networkload);
		withTop=(RelativeLayout)findViewById(R.id.withtop);
		withdrawIcon=(ImageView)findViewById(R.id.withdrawicon);
		scrollView.setVisibility(View.INVISIBLE);
		withdrawlLayout.setVisibility(View.INVISIBLE);
		networkLoad.setVisibility(View.GONE);
	}
	
	private void initTitle() {
		withdrawtitle.setTitle(R.string.withdrawtitle);
		withdrawtitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WithdrawActivity.this.finish();
			}
		});
		withdrawtitle.setLeftImageButton(R.drawable.back);
		withdrawtitle.setLeftTextButton("返回");
		withdrawtitle.showRightButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), WithdrawListActivity.class);
				startActivity(intent);
			}
		});
		withdrawtitle.setRightTextButton("提现记录");
	}

	private void setListeners() {
		textChange tc2 = new textChange();
		edittixian.addTextChangedListener(accountTextWatcher);
		editpassword.addTextChangedListener(tc2);
		lostpassword.setOnClickListener(modify);
		editpassword.setKeyListener(new DigitsKeyListener(false,false));
		edittixian.setOnTouchListener(clickListener);
		editpassword.setOnTouchListener(clickListener);
	}

	private void initContent() {
		if(Tool.isBlank(KDLCApplication.app.getSessionVal("realname"))) {
			canwithdraw.setText("您可提现金额：");
		} else{
			canwithdraw.setText(Tool.changeName(KDLCApplication.app.getSessionVal("realname")) + "，您可提现金额：");	
		}
	}
	
	private void getUserAccountInfo() {
		dialog = KdlcDialog.showProgressDialog(WithdrawActivity.this);
		errorListener.setNetErrShowAlert(true);
		errorListener.setClickListener(finishActivity);//强制跳转上一页
		sendHttpGet(url, getUserAccountListener);
	}
	
	private void getOrderInfo() {
		sendHttpGet(getOrderUrl, getOrderListener);
	}

	private Listener<JSONObject> getUserAccountListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				if (response.getInt("code") == 0) {
					scrollView.setVisibility(View.VISIBLE);
					candrawmoney.setText(Tool.toDeciDouble(response.getString("usable_money")));
					canUseMoney=Tool.toDeciDouble(response.getString("usable_money"));
					JSONArray dataArray = response.getJSONArray("banks");
					for (int j = 0; j < dataArray.length(); j++) {
						withdrawlLayout.setVisibility(View.INVISIBLE);
						JSONObject dataRoot = dataArray.getJSONObject(j);
						withdrawname.setText(dataRoot.getString("bank_name"));
						withdrawlastnumber.setText("尾号"+dataRoot.getString("tail_number"));
						if (dataRoot.getString("id").equals("1")) {
							withdrawIcon.setBackgroundResource(R.drawable.logo_gongshang);
						}else if(dataRoot.getString("id").equals("2")){
							withdrawIcon.setBackgroundResource(R.drawable.logo_nognye);
						}else if(dataRoot.getString("id").equals("3")){
							withdrawIcon.setBackgroundResource(R.drawable.logo_guangda);
						}else if(dataRoot.getString("id").equals("4")){
							withdrawIcon.setBackgroundResource(R.drawable.logo_youzheng);
						}else if(dataRoot.getString("id").equals("5")){
							withdrawIcon.setBackgroundResource(R.drawable.logo_xingye);
						}else if(dataRoot.getString("id").equals("6")){
							withdrawIcon.setBackgroundResource(R.drawable.logo_shenfa);
						}else if(Tool.isBlank(dataRoot.getString("id"))){
							withdrawIcon.setBackgroundResource(R.drawable.logo_gongshang);
						}else{
							withdrawIcon.setBackgroundResource(R.drawable.logo_jianshe);
						}
					}
					getOrderInfo();
				} else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(WithdrawActivity.this);
        		} else {
					KdlcDialog.showBackDialog(WithdrawActivity.this, response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				KdlcDialog.showBackDialog(WithdrawActivity.this, "网络出错，请稍后再试");
			}
		}

	};
	
	private Listener<JSONObject> getOrderListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			dialog.cancel();
        	try {
        		if(response.getInt("code") == 0) {
        			orderNo = response.getString("order_id");
        		} else if(response.getInt("code") == -2) {
        			dialog = KdlcDialog.showLoginDialog(WithdrawActivity.this);
        		} else {
        			dialog = KdlcDialog.showBackDialog(WithdrawActivity.this, response.getString("message"));
        		}
        	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				dialog = KdlcDialog.showBackDialog(WithdrawActivity.this, "");
			}
		}
	};
	
	private OnClickListener modify = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("phone",KDLCApplication.app.getSession().get("username"));
			bundle.putString("find_pwd", "find_pay_pwd");
			intent.putExtras(bundle);
			intent.setClass(WithdrawActivity.this, BackPasswordActivity.class);
			startActivity(intent);
		}
	};
	
	private OnClickListener withdraw = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String cashMoney = edittixian.getText().toString().trim();
			if(Tool.isBlank(cashMoney) || Double.parseDouble(cashMoney) <= 0) {
				KdlcDialog.showToast("提现金额必须大于0", Gravity.CENTER, 0, 0);
			} else if(Double.parseDouble(cashMoney) > Double.parseDouble(canUseMoney)) {
				KdlcDialog.showToast("提现金额不能大于您当前可提现金额", Gravity.CENTER, 0, 0);
			} else {
				dialog = KdlcDialog.showProgressDialog(WithdrawActivity.this, "正在提现...");
				errorListener.setNetErrShowAlert(false);
				HttpParams params = new HttpParams();
				params.add("money", cashMoney);
				params.add("order_id", orderNo);
				params.add("pay_password", editpassword.getText().toString());
				String sign = Tool.signParams(params);
				params.add("sign", sign);
				sendHttpPostCharge(withdrawurl, params, responseListener);	
			}
		}
	};
	private Listener<JSONObject> responseListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject responseresult) {
			// TODO Auto-generated method stub
			dialog.cancel();
	        try {
				if (responseresult.getInt("code")==0) {
					KDLCApplication.app.setSessionVal("selfCenterAutoRefresh", "1");
					Intent intent=new Intent(WithdrawActivity.this,WithDrawSuccessActivity.class);
					JSONObject responseJsonObject=responseresult.getJSONObject("result");
					intent.putExtra("money", responseJsonObject.getString("money"));
					intent.putExtra("created_at", responseJsonObject.getString("created_at"));
					intent.putExtra("poundage", responseJsonObject.getString("poundage"));
					intent.putExtra("message", responseJsonObject.getString("message"));
					startActivity(intent);
					WithdrawActivity.this.finish();
				} else if(responseresult.getInt("code") == -2){
	    			dialog = KdlcDialog.showLoginDialog(WithdrawActivity.this);
	    		} else {
	    			KdlcDialog.showInformDialog(WithdrawActivity.this ,responseresult.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				KdlcDialog.showBottomToast("");
			}
		}
	};


	
	TextWatcher accountTextWatcher = new TextWatcher() {  
        private String temp;   
        @Override  
        public void onTextChanged(CharSequence s, int start, int before, int count) {  
            // TODO Auto-generated method stub  
             temp = s.toString();
             Sign1 = edittixian.getText().length() > 0;
             if (Sign1 == true && Sign2 == true) {
 				buttontixian.setBackgroundResource(R.drawable.btn_red_background);
 				buttontixian.setClickable(true);
 				buttontixian.setOnClickListener(withdraw);
 			} else {
 				buttontixian.setBackgroundResource(R.drawable.btn_grey_background);
 				buttontixian.setClickable(false);
 				buttontixian.setOnClickListener(null);
 			}
        }  
          
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  

        }  
          
        @Override  
        public void afterTextChanged(Editable s) {  
            // TODO Auto-generated method stub   
            if (temp.length() > 0) {
            } else {
            	
            }
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
			if (editpassword.getText().length() == 6) {
				Sign2 = true;
			} else {
				Sign2 = false;
			}
            
			if (Sign1 == true && Sign2 == true) {
				buttontixian.setBackgroundResource(R.drawable.btn_red_background);
				buttontixian.setClickable(true);
				buttontixian.setOnClickListener(withdraw);
			} else {
				buttontixian.setBackgroundResource(R.drawable.btn_grey_background);
				buttontixian.setClickable(false);
				buttontixian.setOnClickListener(null);
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
					if (curClick == R.id.edittixian||curClick == R.id.editpassword) {
						scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					}
					setFocus(curClick);
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
				case R.id.edittixian:
					break;
				case R.id.editpassword:
					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					editpassword.requestFocus();
					break;
				}
			}

			return false;
		}
	};

	private void setFocus(int resId) {
		switch (resId) {
		case R.id.edittixian:
			edittixian.requestFocus();
			break;
		case R.id.editpassword:
			editpassword.requestFocus();
			break;
		}
	}
	
	@Override
	public void showNoNetwork() {
		// TODO Auto-generated method stub
//		withdrawlLayout.setVisibility(View.VISIBLE);
//		withTop.setVisibility(View.INVISIBLE);
	}

	@Override
	public void showErrReq() {
		// TODO Auto-generated method stub
//		withdrawlLayout.setVisibility(View.VISIBLE);
//		withTop.setVisibility(View.INVISIBLE);
	}

}
