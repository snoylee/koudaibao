package com.kdkj.koudailicai.view.invest;

import java.util.List;

import javax.xml.datatype.Duration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.InvestOrderInfo;
import com.kdkj.koudailicai.domain.InvestResultInfo;
import com.kdkj.koudailicai.domain.KdbDetailInfo;
import com.kdkj.koudailicai.domain.ProductBaseInfo;
import com.kdkj.koudailicai.domain.SelfCenterRecord;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeListener;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeRelativeLayout;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.selfcenter.accountremain.WithdrawActivity;
import com.kdkj.koudailicai.view.selfcenter.bankcard.AddBankCardActivity;
import com.kdkj.koudailicai.view.selfcenter.bankcard.ConfirmIdentityActivity;
import com.kdkj.koudailicai.view.selfcenter.password.BackPasswordActivity;
import com.kdkj.koudailicai.view.selfcenter.password.SetTradPwdActivity;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.WebViewActivity;
import com.kdkj.koudailicai.view.product.KdbDetailActivity;

public class InvestChargeActivity extends BaseActivity implements ResizeListener{
	private String LOG_TAG = this.getClass().getName();
	//size
	private int screenHeight;
	//dimens
	private double payPasswdViewHeight = 0.078;
	private double investBtnHeight = 0.08;
	//views
	private TitleView mTitle;
	private ResizeRelativeLayout parentView;
	private ScrollView scrollView;
	private RelativeLayout investInfoView;
	private TextView productNameText;
	private TextView investAccountText;
	private RelativeLayout payPasswdView;
	private TextView investBtn;
	private RelativeLayout balancePayView;
	private TextView balancePayAccountText;
	private RelativeLayout bankPayView;
	private TextView bankPayAccountText;
	private CheckBox balancePayCheckBox;
	private EditText payPasswdText;
	private TextView forgetPayPasswd;
	private RelativeLayout boxView1;
	private TextView acceptText1;
	private RelativeLayout boxView2;
	private TextView acceptText2;
	private TextView safeTip;
	
	private Handler mHandler = new Handler();
	//contents
	private boolean balancePayChecked = true;
	private boolean bankPayChecked = true;
	private ProductBaseInfo productBaseInfo;
	private boolean canInvest = false;
	private String payPasswd;
	private String remainAccount = "";//项目剩余金额
	private double investAccount;//投资金额
	private double balancePayAccount;//余额支付金额
	private double bankPayAccount;//银行卡支付金额
	private double userBalance;//用户余额
	private String bankName;
	private String bankNo;
	private String investOrder;
	private String getUserAccountUrl;
	private String investUrl;
	private String warrantWord;
	private String getOrderUrl;
	private String agreeUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invest_charge);
        initTitle();
        findViews();
        initSize();
		getContents();        
	}
	
	private void getContents() {
		productBaseInfo = (ProductBaseInfo) this.getIntent().getParcelableExtra(G.PRODUCT_INFO_SER_KEY);
		investAccount = Double.parseDouble(this.getIntent().getStringExtra("investAccount"));
		if(productBaseInfo == null) {
			dialog = KdlcDialog.showAlertDialog(InvestChargeActivity.this, finishActivity, "获取项目信息失败，请重试");
		} else if(Tool.isBlank(this.getIntent().getStringExtra("investAccount"))) {
			dialog = KdlcDialog.showAlertDialog(InvestChargeActivity.this, finishActivity, "获取投资金额失败，请重试");
		} else {
	        parseUrl();
	        setListeners();
	        initViews();
	        verify();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(!createFlag) {
			verify();
		} else {
			createFlag = false;
		}
	}
	
	private void verify() {
		if(!KDLCApplication.app.sessionEqual("real_verify_status", "1")) {
			dialog = KdlcDialog.showConfirmBackDialog(InvestChargeActivity.this, true, toRealName, "您还未通过实名认证，现在去认证？");
		} else if(!KDLCApplication.app.sessionEqual("card_bind_status", "1")) {
			dialog = KdlcDialog.showConfirmBackDialog(InvestChargeActivity.this, true, toBindCard, "您还未绑定银行卡，现在去绑定？");
		} else if(!KDLCApplication.app.sessionEqual("set_paypwd_status", "1")) {
			dialog = KdlcDialog.showConfirmBackDialog(InvestChargeActivity.this, true, toPayPassword, "您还未设置交易密码，现在去设置？");
		} else {
			getUserAccountInfo();
		}
	}
	
	private OnClickListener toRealName = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			dialog.cancel();
			Tool.goToActivity(InvestChargeActivity.this, ConfirmIdentityActivity.class);
		}
	};
	
	private OnClickListener toBindCard = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			dialog.cancel();
			Tool.goToActivity(InvestChargeActivity.this, AddBankCardActivity.class);
		}
	};	
	
	private OnClickListener toPayPassword = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			dialog.cancel();
			Tool.goToActivity(InvestChargeActivity.this, SetTradPwdActivity.class);
		}
	};
	
	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			getUserAccountUrl = getApplicationContext().getActionUrl(G.GCK_API_GET_USER_BANK_ACCOUNT_INFO);
			warrantWord = getApplicationContext().getConfVal(G.GCK_VAL_WARRANT_WORD);
			agreeUrl = this.getApplicationContext().getActionUrl(G.GCK_API_GET_PAGE_AGREEMENT);
			if("kdb".equals(productBaseInfo.getType())) {
				investUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_KDB_INVEST);
				getOrderUrl = getApplicationContext().getConfVal(G.GCK_API_GET_KDB_INVEST_ORDER);
			} else if("cession".equals(productBaseInfo.getType())){
				investUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_CESSION_INVEST);
				getOrderUrl = getApplicationContext().getConfVal(G.GCK_API_GET_KDB_INVEST_ORDER);
			} else {
				investUrl = getApplicationContext().getActionUrl(G.GCK_API_POST_PRODUCT_INVEST);
				getOrderUrl = getApplicationContext().getConfVal(G.GCK_API_GET_PRODUCT_INVEST_ORDER);
			}
		} else {
			getUserAccountUrl = G.URL_GET_USER_BANK_ACCOUNT_INFO;
			warrantWord = G.VAL_WARRANT_WORD;
			agreeUrl = G.URL_GET_PAGE_AGREEMENT;
			if("kdb".equals(productBaseInfo.getType())) {
				investUrl =  G.URL_POST_KDB_INVEST;
				getOrderUrl = G.URL_GET_KDB_INVEST_ORDER;
			} else if("cession".equals(productBaseInfo.getType())){
				investUrl = G.URL_POST_CESSION_INVEST;
				getOrderUrl = G.URL_GET_KDB_INVEST_ORDER;
			} else {
				investUrl = G.URL_POST_PRODUCT_INVEST;
				getOrderUrl = G.URL_GET_PRODUCT_INVEST_ORDER;
			}
		}
	}
	
	private void findViews() {
		parentView = (ResizeRelativeLayout) this.findViewById(R.id.parentView);
		parentView.setResizeListener(this);
		scrollView = (ScrollView) this.findViewById(R.id.scrollview);
		investInfoView = (RelativeLayout) this.findViewById(R.id.investInfoView);
		productNameText = (TextView) this.findViewById(R.id.productName); 
		investAccountText = (TextView) this.findViewById(R.id.investAccount);
		balancePayView = (RelativeLayout) this.findViewById(R.id.balancePayView);
		balancePayAccountText = (TextView) this.findViewById(R.id.balancePayAccount);
		bankPayView = (RelativeLayout) this.findViewById(R.id.bankPayView);
		bankPayAccountText = (TextView) this.findViewById(R.id.bankPayAccount);
		balancePayCheckBox = (CheckBox) this.findViewById(R.id.balancePayCheckBox);
		payPasswdView = (RelativeLayout) this.findViewById(R.id.payPasswdView);
		payPasswdText = (EditText) this.findViewById(R.id.payPasswd); 
		investBtn = (TextView) this.findViewById(R.id.investBtn);
		forgetPayPasswd = (TextView) this.findViewById(R.id.forgetPayPasswd);
		safeTip = (TextView) this.findViewById(R.id.safeTip);
		boxView1 = (RelativeLayout) this.findViewById(R.id.acceptView);
		acceptText1 = (TextView) this.findViewById(R.id.acceptTip1);
		acceptText1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		acceptText1.getPaint().setAntiAlias(true);
		boxView2 = (RelativeLayout) this.findViewById(R.id.acceptView2);
		acceptText2 = (TextView) this.findViewById(R.id.acceptTip2);
		acceptText2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		acceptText2.getPaint().setAntiAlias(true);

	}
	
	
	private void initTitle() {
		mTitle = (TitleView) this.findViewById(R.id.title);
		mTitle.setTitle(R.string.invest_title);
		mTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InvestChargeActivity.this.finish();
			}
		});
		mTitle.setLeftImageButton(R.drawable.back);
		mTitle.setLeftTextButton("返回");
	}
	
	private void initSize() {
		screenHeight = this.getScreenHeight();
		//设置基本信息
		RelativeLayout.LayoutParams payPasswdViewLayoutParams = (RelativeLayout.LayoutParams) payPasswdView.getLayoutParams();
		payPasswdViewLayoutParams.height = (int) (screenHeight*payPasswdViewHeight);
		payPasswdView.setLayoutParams(payPasswdViewLayoutParams);
		RelativeLayout.LayoutParams investBtnLayoutParams = (RelativeLayout.LayoutParams) investBtn.getLayoutParams();
		investBtnLayoutParams.height = (int) (screenHeight*investBtnHeight);
		investBtn.setLayoutParams(investBtnLayoutParams);
	}
	
	private void setListeners() {
		investBtn.setOnClickListener(investPayStep);
		balancePayCheckBox.setOnCheckedChangeListener(balancePay);
		balancePayView.setOnClickListener(balanceChoosed);
		payPasswdText.addTextChangedListener(payPasswdTextWatcher);
		payPasswdText.setOnClickListener(scroolPage);
		payPasswdText.setKeyListener(new DigitsKeyListener(false,false));
		forgetPayPasswd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(InvestChargeActivity.this, BackPasswordActivity.class);
				intent.putExtra("find_pwd", "find_pay_pwd");
				startActivityNeedLogin(intent);
			}
		});
		boxView1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(InvestChargeActivity.this, WebViewActivity.class);
				intent.putExtra("url", agreeUrl+"?type=buy");
				intent.putExtra("title", "购买及债权转让协议");
				InvestChargeActivity.this.startActivity(intent);
			}
		});
		boxView2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(InvestChargeActivity.this, WebViewActivity.class);
				intent.putExtra("url", agreeUrl+"?type=pay");
				intent.putExtra("title", "支付服务协议");
				InvestChargeActivity.this.startActivity(intent);
			}
		});
	}
	
	private void initViews() {
		productNameText.setText(productBaseInfo.getName());
		investAccountText.setText(this.getIntent().getStringExtra("investAccount"));
		safeTip.setText(warrantWord);
		balancePayCheckBox.setChecked(true);
	}
	
	private void setPay(boolean chooseBalance){
		if(chooseBalance) {
			if(userBalance >= investAccount) {
				bankPayAccount = 0;
				balancePayAccount = investAccount;
			} else {
				bankPayAccount = investAccount - userBalance;
				balancePayAccount = userBalance;
			}
		} else {
			bankPayAccount = investAccount;
			balancePayAccount = 0;
		}
		balancePayAccountText.setText("余额（￥"+userBalance+"）："+Tool.toDeciDouble(""+(long)(balancePayAccount*100))+"元");
		bankPayAccountText.setText(bankName+"（尾号"+bankNo+"）："+Tool.toDeciDouble(""+(long)(bankPayAccount*100))+"元");
	}
	
	private void getUserAccountInfo() {
		if(createFlag)
			dialog = KdlcDialog.showProgressDialog(InvestChargeActivity.this);
		errorListener.setNetErrShowAlert(true);
		errorListener.setClickListener(finishActivity);//强制跳转上一页
		sendHttpGet(getUserAccountUrl, getUserAccountListener);
	}
	
	private void getOrderInfo() {
		HttpParams params = new HttpParams();
		params.add("money", ""+investAccount);
		sendHttpPostCharge(getOrderUrl, params, getInvestOrderListener);
	}
	
	private Listener<JSONObject> getInvestOrderListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			if(dialog != null)
				dialog.cancel();
        	try {
        		Log.d(LOG_TAG, "response:"+response.toString());
        		if(response.getInt("code") == 0) {
        			investOrder = response.getString("order_id");
        		} else if(response.getInt("code") == -2) {
        			dialog = KdlcDialog.showLoginDialog(InvestChargeActivity.this);
        		} else {
        			dialog = KdlcDialog.showBackDialog(InvestChargeActivity.this, response.getString("message"));
        		}
        	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				dialog = KdlcDialog.showBackDialog(InvestChargeActivity.this, "网络出错，请稍后再试");
			}
		}
	};
	
	private Listener<JSONObject> getUserAccountListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
        	try {
        		Log.d(LOG_TAG, "response:"+response.toString());
        		if(response.getInt("code") == 0) {
        			userBalance = Double.parseDouble(Tool.toDeciDouble(response.getString("usable_money")));
        			JSONArray bankList = response.getJSONArray("banks");
        			for(int i=0; i < bankList.length(); i++) {
        				JSONObject bankInfo = bankList.getJSONObject(i);
        				bankName = bankInfo.getString("bank_name");
        				bankNo = bankInfo.getString("tail_number");
        			}
        			setPay(true);
        			if(investOrder == null) {
            			getOrderInfo();
        			} else {
        				if(dialog != null)
        					dialog.cancel();
        			}
        		} else {
        			if(dialog != null)
        				dialog.cancel();
        			dialog = KdlcDialog.showAlertDialog(InvestChargeActivity.this, finishActivity, response.getString("message"));
        		}
        	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(dialog != null)
					dialog.cancel();
				dialog = KdlcDialog.showAlertDialog(InvestChargeActivity.this, finishActivity, "获取投资金额失败，请重试");
			}
		}
	};
	
	private void setPasswdInputStatus(boolean show) {
		if(show == false) {
			investBtn.setBackgroundResource(R.drawable.btn_grey_background);
			canInvest = false;
		} else {
			investBtn.setBackgroundResource(R.drawable.btn_red_background);
			canInvest = true;
		}
	}
	
	private void resetAccount() {
		investAccountText.setText(Tool.toDeciDouble(remainAccount));
		investAccount = Tool.toForDouble2(Tool.toDeciDouble(remainAccount));
		setPay(balancePayChecked);
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
				payPasswd = payPasswdText.getText().toString();
				String alertTip = "";
				if(Tool.isBlank(payPasswd)) {
					alertTip = "请输入支付密码";
				} else if((balancePayAccount+bankPayAccount) != investAccount) {
					alertTip = "您选择的支付金额不正确";
				}
				if(!Tool.isBlank(alertTip)) {
					KdlcDialog.showToast(alertTip, Gravity.TOP, 0, (int) (screenHeight*0.08+20));
				} else {
					HttpParams params = new HttpParams();
					//参数务必按key的字母顺序进行插入，否则签名无法成功
					if(productBaseInfo.getType().equals("kdb")) {
					} else if(productBaseInfo.getType().equals("cession")) {
						params.add("invest_id", ""+productBaseInfo.getProductId());
					} else {
						params.add("id", ""+productBaseInfo.getProductId());
					}
					params.add("money", ""+investAccount);
					params.add("order_id", investOrder);
					params.add("pay_password", payPasswd);
					params.add("use_remain", balancePayChecked ? "1":"0");
					Log.d("asdsa", "parmas:"+params.toString());
					String sign = Tool.signParams(params);
					params.add("sign", sign);
					dialog = KdlcDialog.showProgressDialog(InvestChargeActivity.this, "安全支付中...");
					errorListener.setNetErrShowAlert(false);
					sendHttpPostCharge(investUrl, params, investListener);
				}
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
        			Log.d(LOG_TAG, "productBaseInfo.getType():"+productBaseInfo.getType());
        			if(!"kdb".equals(productBaseInfo.getType())) {
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
	    			dialog = KdlcDialog.showLoginDialog(InvestChargeActivity.this);
        		} else if(response.getInt("code") == 1012) {
        			remainAccount = response.getString("remain_money");
        			dialog = KdlcDialog.showConfirmDialog(InvestChargeActivity.this, true, investPayStep, "该项目申购金额仅剩"+Tool.toDeciDouble(remainAccount)+"元，是否购买剩余金额？");       				
    			} else if(response.getInt("code") == 2001) { 
    				InvestOrderInfo orderInfo = new InvestOrderInfo();
        			orderInfo.setOrderId(investOrder);
        			orderInfo.setPayPassword(payPasswd);
        			orderInfo.setProductId(productBaseInfo.getProductId());
        			orderInfo.setProductName(productBaseInfo.getName());
        			orderInfo.setProductType(productBaseInfo.getType());
        			orderInfo.setUseRemain(balancePayChecked ? "1" : "0");
        			orderInfo.setInvestAccount(investAccountText.getText().toString().trim());
        			Intent intent = new Intent(InvestChargeActivity.this, InvestCodeActivity.class);
        			intent.putExtra(G.INVEST_ORDER_INFO_SER_KEY, orderInfo);
        			InvestChargeActivity.this.startActivityNeedLogin(intent);
        			KdlcDialog.showBottomToast("验证码发送成功");
    			}else {
        			dialog = KdlcDialog.showInformDialog(InvestChargeActivity.this, response.getString("message"));
    			}
        	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				KdlcDialog.showBottomToast("");
			}
		}
	};
	
	private void goToResult(InvestResultInfo retInfo) {
		Intent intent = new Intent(InvestChargeActivity.this, InvestSuccessActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(G.INVEST_INFO_SER_KEY, retInfo);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	private OnClickListener balanceChoosed = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			balancePayCheckBox.setChecked(!balancePayChecked);
		}
	};
	
	private OnCheckedChangeListener balancePay = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
        	balancePayChecked = isChecked;
        	setPay(balancePayChecked);
        }
    };
    
	private OnClickListener scroolPage = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int scrollY = (int) (investInfoView.getMeasuredHeight());
			scrollView.scrollTo(0, scrollY);
		}
	};
	
	TextWatcher payPasswdTextWatcher = new TextWatcher() {  
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
            if (temp.length() == 6) {
            	setPasswdInputStatus(true);
            } else {
            	setPasswdInputStatus(false);
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
}
