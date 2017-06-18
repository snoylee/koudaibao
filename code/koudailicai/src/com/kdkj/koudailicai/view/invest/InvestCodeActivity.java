package com.kdkj.koudailicai.view.invest;


import org.json.JSONObject;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.InvestOrderInfo;
import com.kdkj.koudailicai.domain.InvestResultInfo;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeListener;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeRelativeLayout;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.register.SmsObserver;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;

public class InvestCodeActivity extends BaseActivity implements ResizeListener{
	private String LOG_TAG = this.getClass().getName();
	//size
	private int screenHeight;
	//dimens
	private double investBtnHeight = 0.08;
	//views
	private TitleView mTitle;
	private ResizeRelativeLayout parentView;
	private ScrollView scrollView;
	private RelativeLayout investInfoView;
	private TextView productNameText;
	private TextView investAccountText;
	private TextView telText;
	private TextView sendBtn;
	private TextView investBtn;
	private EditText codeText;
	private TextView safeTip;
	//contents
	private Uri SMS_INBOX = Uri.parse("content://sms/");
	private static final int INTERVAL = 1;
	private int curTime;
	private SmsObserver smsObserver;
	private InvestOrderInfo orderInfo;
	private boolean canInvest = false;
	private String warrantWord;
	private String investUrl;
	private String getCodeUrl;
	private String remainAccount;
	private String captcha;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invest_code);
        getContents();
        parseUrl();
        findViews();
        initTitle();
        initSize();
        setListeners();
        initViews();
        initSmsGet();
        setSendCode(true);
	}
	
	private void getContents() {
		orderInfo = this.getIntent().getParcelableExtra(G.INVEST_ORDER_INFO_SER_KEY);
		Log.d("sada", orderInfo.toString());
		if(orderInfo == null) {
			dialog = KdlcDialog.showAlertDialog(InvestCodeActivity.this, finishActivity, "获取投资信息失败，请重试");
		}
	}
	
	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			warrantWord = getApplicationContext().getConfVal(G.GCK_VAL_WARRANT_WORD);
			if("kdb".equals(orderInfo.getProductType())) {
				investUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_KDB_INVEST);
				getCodeUrl = getApplicationContext().getConfVal(G.GCK_API_GET_KDB_INVEST_CODE);
			} else if("cession".equals(orderInfo.getProductType())){
				investUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_CESSION_INVEST);
				getCodeUrl = getApplicationContext().getConfVal(G.GCK_API_GET_PRODUCT_INVEST_CODE);
			} else {
				investUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_PRODUCT_INVEST);
				getCodeUrl = getApplicationContext().getConfVal(G.GCK_API_GET_PRODUCT_INVEST_CODE);
			}
		} else {
			warrantWord = G.VAL_WARRANT_WORD;
			if("kdb".equals(orderInfo.getProductType())) {
				investUrl =  G.URL_POST_KDB_INVEST;
				getCodeUrl = G.URL_GET_KDB_INVEST_CODE;
			} else if("cession".equals(orderInfo.getProductType())){
				investUrl = G.URL_POST_CESSION_INVEST;
				getCodeUrl = G.URL_GET_PRODUCT_INVEST_CODE;
			} else {
				investUrl = G.URL_POST_PRODUCT_INVEST;
				getCodeUrl = G.URL_GET_PRODUCT_INVEST_CODE;
			}
		}
	}
	
	private void findViews() {
		parentView = (ResizeRelativeLayout) this.findViewById(R.id.parentView);
		parentView.setResizeListener(this);
		scrollView = (ScrollView) this.findViewById(R.id.scrollview);
		mTitle = (TitleView) this.findViewById(R.id.title);
		investInfoView = (RelativeLayout) this.findViewById(R.id.investInfoView);
		productNameText = (TextView) this.findViewById(R.id.productName); 
		investAccountText = (TextView) this.findViewById(R.id.investAccount);
		telText = (TextView) this.findViewById(R.id.telText);
		codeText = (EditText) this.findViewById(R.id.codeText);
		sendBtn = (TextView) this.findViewById(R.id.sendBtn);
		investBtn = (TextView) this.findViewById(R.id.investBtn);
		safeTip = (TextView) this.findViewById(R.id.safeTip);
	}
	
	private void initTitle() {
		mTitle.setTitle(R.string.invest_title);
		mTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InvestCodeActivity.this.finish();
			}
		});
		mTitle.setLeftImageButton(R.drawable.back);
		mTitle.setLeftTextButton("返回");
	}
	
	private void initSize() {
		screenHeight = this.getScreenHeight();
		//设置基本信息
		RelativeLayout.LayoutParams investBtnLayoutParams = (RelativeLayout.LayoutParams) investBtn.getLayoutParams();
		investBtnLayoutParams.height = (int) (screenHeight*investBtnHeight);
		investBtn.setLayoutParams(investBtnLayoutParams);
	}
	
	private void setListeners() {
		investBtn.setOnClickListener(investPayStep);
		sendBtn.setOnClickListener(sendCode);
		codeText.addTextChangedListener(codeTextWatcher);
		codeText.setOnClickListener(scroolPage);
	}
	
	private void initViews() {
		productNameText.setText(orderInfo.getProductName());
		investAccountText.setText(orderInfo.getInvestAccount());
		safeTip.setText(warrantWord);
		telText.setText(Tool.changeMobile(KDLCApplication.app.getSessionVal("username")));
	}
	
	//发送验证码
	private OnClickListener sendCode = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			dialog = KdlcDialog.showProgressDialog(InvestCodeActivity.this, "正在发送...");
			sendHttpGetCharge(getCodeUrl, codeResponseListener);
		}
	};
	
	Listener<JSONObject> codeResponseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				dialog.cancel();
				if (response.getInt("code") == 0) {
					setSendCode(true);
					KdlcDialog.showBottomToast("验证码发送成功");
				} else {
					KdlcDialog.showInformDialog(InvestCodeActivity.this,response.getString("message"));
				}
			} catch (Exception ex) {
				KdlcDialog.showBottomToast("");
				ex.printStackTrace();
			}
		}
	};
	
	private void setSendCode(boolean send) {
		curTime = 60;
		if(send == true) {
			mHandler.sendEmptyMessage(INTERVAL);
			sendBtn.setBackgroundResource(R.drawable.btn_grey_background);
			sendBtn.setClickable(false);
		} else {
			sendBtn.setText("重新发送");
			sendBtn.setBackgroundResource(R.drawable.btn_red_background);
			sendBtn.setClickable(true);
		}
	}

	private OnClickListener investPayStep = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(canInvest == true) {
				if(!Tool.isBlank(remainAccount)) {
					dialog.cancel();
					resetAccount();
				}
				HttpParams params = new HttpParams();
				params.add("captcha", captcha);
				if("kdb".equals(orderInfo.getProductType())) {
				} else if("cession".equals(orderInfo.getProductType())) {
					params.add("invest_id", ""+orderInfo.getProductId());
				} else {
					params.add("id", ""+orderInfo.getProductId());
				}
				params.add("money", investAccountText.getText().toString().trim());
				params.add("order_id", orderInfo.getOrderId());				
				params.add("pay_password", orderInfo.getPayPassword());
				params.add("use_remain", orderInfo.getUseRemain());				
				String sign = Tool.signParams(params);
				params.add("sign", sign);
				Log.d("asdsa", "parmas:"+params.toString());
				dialog = KdlcDialog.showProgressDialog(InvestCodeActivity.this, "安全支付中...");
				errorListener.setNetErrShowAlert(false);
				sendHttpPostCharge(investUrl, params, investListener);
			}
		}
	};

	private Listener<JSONObject> investListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
        	try {
        		dialog.cancel();
        		Log.d(LOG_TAG, "response:"+response.toString());
        		if(response.getInt("code") == 0) {
        			if(!"kdb".equals(orderInfo.getProductType())) {
        				KDLCApplication.app.setSessionVal("is_novice", "0");
        			}
        			JSONObject investInfo = response.getJSONObject("investInfo");
        			InvestResultInfo retInfo = new InvestResultInfo();
        			retInfo.setProjectName(investInfo.getJSONObject("invest").getString("project_name"));
        			retInfo.setProjectTypeDesc(investInfo.getJSONObject("invest").getString("project_type_desc"));
        			retInfo.setApr(investInfo.getJSONObject("invest").getString("apr"));
        			retInfo.setInvestMoney(investInfo.getJSONObject("invest").getString("invest_money"));
        			retInfo.setInvestDate(investInfo.getJSONObject("invest").getString("date"));
        			retInfo.setStartDate(investInfo.getJSONObject("start").getString("date"));
        			retInfo.setStartDesc(investInfo.getJSONObject("start").getString("desc"));
        			retInfo.setEndDate(investInfo.getJSONObject("end").getString("date"));
        			retInfo.setEndDesc(investInfo.getJSONObject("end").getString("desc"));
        			goToResult(retInfo);
				} else if(response.getInt("code") == -2){
	    			dialog = KdlcDialog.showLoginDialog(InvestCodeActivity.this);
        		} else if(response.getInt("code") == 1012) {
        			remainAccount = response.getString("remain_money");
        			dialog = KdlcDialog.showConfirmDialog(InvestCodeActivity.this, true, investPayStep, "该项目申购金额仅剩"+Tool.toDeciDouble(remainAccount)+"元，是否购买剩余金额？");       				
    			} else {
        			dialog = KdlcDialog.showInformDialog(InvestCodeActivity.this, response.getString("message"));
    			}
        	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				KdlcDialog.showBottomToast("");
			}
		}
	};
	
	private void resetAccount() {
		investAccountText.setText(Tool.toDeciDouble(remainAccount));
	}
				
	private void goToResult(InvestResultInfo retInfo) {
		Intent intent = new Intent(InvestCodeActivity.this, InvestSuccessActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(G.INVEST_INFO_SER_KEY, retInfo);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private OnClickListener scroolPage = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int scrollY = (int) (investInfoView.getMeasuredHeight());
			scrollView.scrollTo(0, scrollY);
		}
	};
	
	private void setInvestStatus(boolean show) {
		if(show == false) {
			investBtn.setBackgroundResource(R.drawable.btn_grey_background);
			canInvest = false;
		} else {
			investBtn.setBackgroundResource(R.drawable.btn_red_background);
			canInvest = true;
		}
	}
	
	TextWatcher codeTextWatcher = new TextWatcher() {  
        private CharSequence temp;   
        @Override  
        public void onTextChanged(CharSequence s, int start, int before, int count) {  
            // TODO Auto-generated method stub  
             temp = s;  
        }  
          
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  

        }  
          
        @Override  
        public void afterTextChanged(Editable s) {  
            // TODO Auto-generated method stub
        	captcha = s.toString().trim();
            if (captcha.length() == 6) {
            	setInvestStatus(true);
            } else {
            	setInvestStatus(false);
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
			        scrollView.fullScroll(ScrollView.FOCUS_DOWN);  
			    }  
			});
		}
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case INTERVAL:
					if (curTime > 0) {
						sendBtn.setText("重新发送（"+curTime+"秒）");
						mHandler.sendEmptyMessageDelayed(INTERVAL, 1000);
						curTime--;
					} else {
						setSendCode(false);
					}
					break;
				case SmsObserver.SEND_VERIFY_NUM:
					codeText.setText(smsObserver.verifyNum);
					codeText.setSelection(smsObserver.verifyNum.length());
					break;
				default:
					break;
			}
		};
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		getContentResolver().unregisterContentObserver(smsObserver);
	}
	
	private void initSmsGet(){
        smsObserver = new SmsObserver(mHandler);  
        getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);  
	}
}
