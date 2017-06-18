package com.kdkj.koudailicai.view.invest;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.kdkj.koudailicai.domain.ProductBaseInfo;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeListener;
import com.kdkj.koudailicai.lib.ui.RelativeLayout.ResizeRelativeLayout;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.login.LoginActivity;
import com.kdkj.koudailicai.view.selfcenter.password.SetTradPwdActivity;
import com.kdkj.koudailicai.view.selfcenter.bankcard.ConfirmIdentityActivity;
import com.kdkj.koudailicai.view.selfcenter.bankcard.AddBankCardActivity;
public class InvestAccountActivity extends BaseActivity implements ResizeListener{
	private String LOG_TAG = this.getClass().getName();
	private String getUserRemainUrl;
	private String warrantWord;
	//size
	private int screenHeight;
	private int screenWidth;
	//dimens
	private double investAccountViewHeight = 0.078;
	private double investBtnHeight = 0.08;
	//views
	private TitleView mTitle;
	private ResizeRelativeLayout parentView;
	private ScrollView scrollView;
	private RelativeLayout productInfoView;
	private RelativeLayout topView;
	private View bottomView;
	private RelativeLayout investAccountView;
	private TextView productName;
	private TextView minAccount;
	private TextView time;
	private TextView apr;
	private TextView fee;
	private EditText investAccountText;
	private TextView investTipFirst;
	private RelativeLayout investFirstTipView;
	private RelativeLayout investSecondTipView;
	private TextView investTipSecond;
	private CheckBox balancePayCheckBox;
	private TextView safeTip;
	private TextView investBtn;
	//contents
	private double kdbRemain = -1;
	private String userRemain;
	private boolean balancePayChecked = false;
	private ProductBaseInfo productBaseInfo;
	private String investAccountStr;
	private String investMoneyAccount;
	private boolean canGoCharge = false; 
	private Handler mHandler = new Handler();
	private boolean isKdb = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_account);
        getContents();
        parseUrl();
        findViews();
        initTitle();
        initSize();
        initContents();
        setListeners();
	}
	
	private void getContents() {
		productBaseInfo = (ProductBaseInfo) this.getIntent().getParcelableExtra(G.PRODUCT_INFO_SER_KEY);
		if(productBaseInfo == null) {
			dialog = KdlcDialog.showAlertDialog(InvestAccountActivity.this, goToProductFragment, "获取项目信息失败，请稍后再试");
		} else {
			if("kdb".equals(productBaseInfo.getType())) {
				isKdb = true;
			}
		}
	}
	
	private OnClickListener goToProductFragment = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			goToFragment();
		}
	};
	
	private void goToFragment() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(G.TO_FRAGMENT_KEY, G.FRAGMENT_TAG.LIST);
		intent.setClass(InvestAccountActivity.this, MainActivity.class);
		startActivity(intent);
		//dialog.cancel();
		finish();
	}
	
	private void parseUrl() {
		 if (this.getApplicationContext().isGlobalConfCompleted()) {
			 getUserRemainUrl = isKdb ? getApplicationContext().getActionUrl(G.GCK_API_GET_KDB_REMAIN) : 
				 						getApplicationContext().getActionUrl(G.GCK_API_GET_ACCOUNT_REAMAIN);
			 warrantWord = getApplicationContext().getConfVal(G.GCK_VAL_WARRANT_WORD);
			 Log.d(LOG_TAG, "conf url:"+getUserRemainUrl);

		 } else {
			 getUserRemainUrl = isKdb ? G.URL_GET_KDB_REMAIN : G.URL_GET_ACCOUNT_REMAIN;
			 warrantWord = G.VAL_WARRANT_WORD;
		 }
	}
	
	private void findViews() {
		parentView = (ResizeRelativeLayout) this.findViewById(R.id.parentView);
		parentView.setResizeListener(this);
		scrollView = (ScrollView) this.findViewById(R.id.scrollview);
		topView = (RelativeLayout) this.findViewById(R.id.topView); 
		productInfoView = (RelativeLayout) this.findViewById(R.id.productInfoView);
		bottomView = (View) this.findViewById(R.id.bottomView);
		mTitle = (TitleView) this.findViewById(R.id.title);
		investAccountView = (RelativeLayout) this.findViewById(R.id.investAccountView);
		productName = (TextView) this.findViewById(R.id.productName);
		minAccount = (TextView) this.findViewById(R.id.minAccount);
		time = (TextView) this.findViewById(R.id.time);
		apr = (TextView) this.findViewById(R.id.apr);
		fee = (TextView) this.findViewById(R.id.fee);
		investAccountText = (EditText) this.findViewById(R.id.investAccount);
		investTipFirst = (TextView) this.findViewById(R.id.investTipSecond);
		investFirstTipView = (RelativeLayout) this.findViewById(R.id.investSecondTipView);
		investTipSecond = (TextView) this.findViewById(R.id.balancePayTip);
		investSecondTipView = (RelativeLayout) this.findViewById(R.id.investFirstTipView);		
		balancePayCheckBox = (CheckBox) this.findViewById(R.id.balancePayCheckBox);
		safeTip = (TextView) this.findViewById(R.id.safeTip);
		investBtn = (TextView) this.findViewById(R.id.investBtn);
	}
	
	private void initTitle() {
		mTitle.setTitle(R.string.invest_title);
		mTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InvestAccountActivity.this.finish();
			}
		});
		mTitle.setLeftImageButton(R.drawable.back);
		mTitle.setLeftTextButton("返回");
	}
	
	private void initSize() {
		screenHeight = this.getScreenHeight();
		screenWidth = this.getScreenWidth();
		//设置基本信息
		RelativeLayout.LayoutParams investAccountViewLayoutParams = (RelativeLayout.LayoutParams) investAccountView.getLayoutParams();
		investAccountViewLayoutParams.height = (int) (screenHeight*investAccountViewHeight);
		investAccountView.setLayoutParams(investAccountViewLayoutParams);
		RelativeLayout.LayoutParams investBtnLayoutParams = (RelativeLayout.LayoutParams) investBtn.getLayoutParams();
		investBtnLayoutParams.height = (int) (screenHeight*investBtnHeight);
		investBtn.setLayoutParams(investBtnLayoutParams);
	}
	
	private void setListeners() {
		investAccountText.addTextChangedListener(accountTextWatcher);
		//investAccountText.setOnClickListener(inputAccount);
		investBtn.setOnClickListener(investPayStep);
		balancePayCheckBox.setOnCheckedChangeListener(balancePay);
		investSecondTipView.setOnClickListener(balanceChoosed);
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
        	showBalancePay(balancePayChecked, userRemain);
        }
    };
	
	
	private void initContents() {
		productBaseInfo = (ProductBaseInfo) this.getIntent().getParcelableExtra(G.PRODUCT_INFO_SER_KEY);
		if(productBaseInfo != null) {
			productName.setText(productBaseInfo.getName());
			minAccount.setText(productBaseInfo.getMinMoney()+"元");
			time.setText(productBaseInfo.getTime());
			apr.setText(productBaseInfo.getApr());
			fee.setText(("kdb".equals(productBaseInfo.getType()) ? Tool.toDivAccount2(productBaseInfo.getLeftMoney()) : Tool.toDivAccount(productBaseInfo.getLeftMoney()+"00"))+"元");
			safeTip.setText(warrantWord);
			if(!isKdb) {
				InputFilter[] filters = {new InputFilter.LengthFilter(9)};  
				investAccountText.setFilters(filters); 
				investAccountText.setInputType(InputType.TYPE_CLASS_NUMBER);
				investFirstTipView.setVisibility(View.VISIBLE);
				investTipFirst.setText("投资金额为"+productBaseInfo.getMinMoney()+"元的整数倍");
			}
			if(Double.parseDouble(productBaseInfo.getLeftMoney()) < Double.parseDouble(productBaseInfo.getMinMoney())) {
				dialog = KdlcDialog.showAlertDialog(InvestAccountActivity.this, goToProductFragment, "该项目已售罄，请选择其他投资项目哦");
			} else if(!isKdb && "1".equals(productBaseInfo.getIsNovice()) && KDLCApplication.app.sessionEqual("is_novice", "0")) {
				dialog = KdlcDialog.showAlertDialog(InvestAccountActivity.this, goToProductFragment, "您已经不是新手了，无法投资新手专享项目，请选择投资其他项目哦");
			} else {
				getUserRemain();
			}
		} else {
			dialog = KdlcDialog.showAlertDialog(InvestAccountActivity.this, goToProductFragment, "获取项目信息失败，请稍后再试");
		}
	}
	
	private void getUserRemain() {
		dialog = KdlcDialog.showProgressDialog(InvestAccountActivity.this);
		sendHttpGet(getUserRemainUrl, getUserRemainListener);
	}
	
	private Listener<JSONObject> getUserRemainListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
        	try {
        		dialog.cancel();
        		Log.d(LOG_TAG, "response:"+response.toString());
        		if(response.getInt("code") == 0) {
        			if(isKdb) {
        				String remain = response.getString("today_remain");
            			if(!Tool.isBlank(remain)) {
            				investFirstTipView.setVisibility(View.VISIBLE);
            				kdbRemain = Double.parseDouble(Tool.toDeciDouble(remain));
            				investTipFirst.setText("您当前的口袋宝购买额度剩余"+Tool.toDeciDouble(remain)+"元");
            				if(Integer.parseInt(remain) == 0) {
            					dialog = KdlcDialog.showAlertDialog(InvestAccountActivity.this, goToProductFragment, "您当前的口袋宝购买额度剩余0元，请选择投资其他项目哦");
            				}
            			}
            			userRemain = Tool.toDeciDouble(response.getString("usable_money"));
        			} else {
        				userRemain = Tool.toDeciDouble(response.getJSONObject("data").getString("usable_money"));
        			}
        			if(!Tool.isBlank(userRemain) && Double.parseDouble(userRemain) > 0 && Double.parseDouble(userRemain) >= Double.parseDouble(productBaseInfo.getMinMoney())) {
        				investSecondTipView.setVisibility(View.VISIBLE);
        				balancePayCheckBox.setChecked(false);
        				investTipSecond.setText("投入全部余额:"+userRemain+"元");
        			} else {
        				investSecondTipView.setVisibility(View.GONE);
        			}
        		} else if(response.getInt("code") == -2){
        			dialog = KdlcDialog.showLoginDialog(InvestAccountActivity.this);
        		}
        	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private void showBalancePay(boolean show, String userRemain) {
		if(show) {
			if(isKdb) {
				double addMoney = Double.parseDouble(productBaseInfo.getLeftMoney()) > kdbRemain ? kdbRemain : Double.parseDouble(productBaseInfo.getLeftMoney());
				addMoney = Double.parseDouble(userRemain) > addMoney ? addMoney : Double.parseDouble(userRemain);
				investAccountText.setText(Tool.toForDouble(""+addMoney));
			} else {
				int minInvest = Integer.parseInt(productBaseInfo.getMinMoney());
				int remainInvest = (int)(Double.parseDouble(userRemain)/minInvest) * minInvest;
				investAccountText.setText("" + (remainInvest > Integer.parseInt(productBaseInfo.getLeftMoney()) ? 
												Integer.parseInt(productBaseInfo.getLeftMoney()) : remainInvest));
			}
			investAccountText.setSelection(investAccountText.getText().length());
		} else {
			investAccountText.setText("");
		}
	}
	
	private OnClickListener investPayStep = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(canGoCharge == true) {
				investMoneyAccount = investAccountText.getText().toString().trim();
				String alertTip = "";
				if(Tool.isBlank(investMoneyAccount)) {
					alertTip = "请输入购买金额";
				} else if(isKdb && Double.parseDouble(investMoneyAccount) < Double.parseDouble(productBaseInfo.getMinMoney())) {
					alertTip = "口袋宝的起投金额为"+productBaseInfo.getMinMoney()+"元";
				} else if(!isKdb && (Integer.parseInt(investMoneyAccount) == 0 || Integer.parseInt(investMoneyAccount) % Integer.parseInt(productBaseInfo.getMinMoney()) != 0)){
					alertTip = "购买金额必须是"+productBaseInfo.getMinMoney()+"元的整数倍";
				} else if(productBaseInfo.getLeftMoney() != null && 
					Double.parseDouble(productBaseInfo.getLeftMoney()) < Double.parseDouble(investMoneyAccount)) {
					alertTip = "购买金额不能大于剩余金额："+Tool.toForDouble(productBaseInfo.getLeftMoney())+"元";
				}
				if(isKdb && kdbRemain != -1 && Double.parseDouble(investMoneyAccount) > kdbRemain) {
					dialog = KdlcDialog.showInformDialog(InvestAccountActivity.this, "购买金额不能大于您当日的口袋宝剩余交易额度："+kdbRemain+"元");
				} else {
					if(!Tool.isBlank(alertTip)) {
						KdlcDialog.showToast(alertTip, Gravity.TOP, 0, (int) (screenHeight*0.08+20));
					} else {
						if(!KDLCApplication.app.sessionEqual("real_verify_status", "1")) {
							dialog = KdlcDialog.showConfirmDialog(InvestAccountActivity.this, true, toRealName, "您还未通过实名认证，现在去认证？");
						} else if(!KDLCApplication.app.sessionEqual("card_bind_status", "1")) {
							dialog = KdlcDialog.showConfirmDialog(InvestAccountActivity.this, true, toBindCard, "您还未绑定银行卡，现在去绑定？");
						} else if(!KDLCApplication.app.sessionEqual("set_paypwd_status", "1")) {
							dialog = KdlcDialog.showConfirmDialog(InvestAccountActivity.this, true, toPayPassword, "您还未设置交易密码，现在去设置？");
//							} else if(isKdb && investRemain) {
//								dialog = KdlcDialog.showConfirmDialog(InvestAccountActivity.this, true, remainInvest, "您确定用全部余额"+userRemain+"元进行投资吗？");
						} else {
							goInvest();
						}
					}
				}
			}
		}
	};
	
	private OnClickListener remainInvest = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			dialog.cancel();
			goInvest();
		}
	};
	
	private void goInvest() {
		Intent intent = new Intent(InvestAccountActivity.this, InvestChargeActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("investAccount", Tool.toForDouble(investMoneyAccount));
		bundle.putString("productName", productBaseInfo.getName());
		bundle.putParcelable(G.PRODUCT_INFO_SER_KEY, productBaseInfo);
		intent.putExtras(bundle);
		startActivityNeedLogin(intent);
	}
	
	private OnClickListener toRealName = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			dialog.cancel();
			Tool.goToActivity(InvestAccountActivity.this, ConfirmIdentityActivity.class);
		}
	};
	
	private OnClickListener toBindCard = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			dialog.cancel();
			Tool.goToActivity(InvestAccountActivity.this, AddBankCardActivity.class);
		}
	};	
	
	private OnClickListener toPayPassword = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			dialog.cancel();
			Tool.goToActivity(InvestAccountActivity.this, SetTradPwdActivity.class);
		}
	};
	
	private OnClickListener inputAccount = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Log.d(LOG_TAG, "parentView height:"+parentView.getMeasuredHeight()+", scrollView:"+scrollView.getMeasuredHeight());
			mHandler.post(new Runnable() {  
			    @Override  
			    public void run() {  
			        scrollView.fullScroll(ScrollView.FOCUS_DOWN);  
			    }  
			});
		}
	};

	
	private void setAccountInputStatus(boolean show) {
		if(show == false) {
			investBtn.setBackgroundResource(R.drawable.btn_grey_background);
			canGoCharge = false;
		} else {
			investBtn.setBackgroundResource(R.drawable.btn_red_background);
			canGoCharge = true;
		}
	}
	
	TextWatcher accountTextWatcher = new TextWatcher() {  
        private String temp;
        @Override  
        public void onTextChanged(CharSequence s, int start, int before, int count) {  
            // TODO Auto-generated method stub  
             temp = s.toString();  
        }  
          
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  

        }  
          
        @Override  
        public void afterTextChanged(Editable s) {
//        	if(isKdb == true && investRemain == true && userRemain > 0 && (Tool.isBlank(temp) || Double.parseDouble(temp) < userRemain)) {
//        		s.clear();
//        		investRemain = false;
//        		setAccountInputStatus(false);
//        	} else {
	            // TODO Auto-generated method stub   
	            if (temp.length() > 0) {
	            	setAccountInputStatus(true);
	            } else {
	            	setAccountInputStatus(false);
	            }
	            int posDot = temp.indexOf(".");
	            if (posDot < 0) return;
	            if (temp.length() - posDot - 1 > 2)
	            {
	                s.delete(posDot + 3, posDot + 4);
	            }
        	//}
        }
    };
    
	@Override
	public void onResize(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		Log.d(LOG_TAG, "call onResize");
		if(oldw != 0) {
			Log.d(LOG_TAG, "parentView height:"+parentView.getMeasuredHeight()+", scrollView:"+scrollView.getMeasuredHeight());
			mHandler.post(new Runnable() {  
			    @Override  
			    public void run() {  
			        scrollView.fullScroll(ScrollView.FOCUS_DOWN);  
			    }  
			});
		}
	}

}
