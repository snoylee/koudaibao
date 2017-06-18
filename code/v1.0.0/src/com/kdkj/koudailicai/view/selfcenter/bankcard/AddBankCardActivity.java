package com.kdkj.koudailicai.view.selfcenter.bankcard;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
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
import com.kdkj.koudailicai.view.selfcenter.password.SetTradPwdActivity;

public class AddBankCardActivity extends BaseActivity implements ResizeListener {
	//views
	private TitleView addcredittitle;
	private TextView chooseBankName;
	private TextView userRealName;
	private EditText cardNo;
	private TextView submit;
	private ScrollView addscrollview;
	private ResizeRelativeLayout addcredit;
	//content
	private String bankCode;
	private String bankCardNo;
	private String bankName;
	private String realName;
	private boolean payPasswdSet;
	private String userBindCardUrl;
	private Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bankcard_addcard);
		registerBoradcastReceiver();
		getContents();
		parseUrl();
		findViews();
		initTitle();
		setListener();
		initContent();
	}
	
	private void getContents() {
		payPasswdSet = this.getApplicationContext().sessionEqual("set_paypwd_status", "1");
		realName     = this.getApplicationContext().getSessionVal("realname");	
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			userBindCardUrl = getApplicationContext().getActionUrl(G.GCK_API_GET_USER_BIND_CARD); 
		} else {
			userBindCardUrl = G.URL_GET_USER_BIND_CARD;
		}
	}
	
	private void findViews() {
		addcredit = (ResizeRelativeLayout) findViewById(R.id.addcredit);
		addcredit.setResizeListener(this);
		addscrollview = (ScrollView) findViewById(R.id.addscrollview);
		addcredittitle = (TitleView) findViewById(R.id.addcredittitle);
		userRealName = (TextView) findViewById(R.id.userRealName);
		chooseBankName = (TextView) findViewById(R.id.chooseBankName);
		cardNo = (EditText) findViewById(R.id.cardNo);
		submit = (TextView) findViewById(R.id.submit);
	}
	

	
	private void initContent() {
		userRealName.setText("持卡人 " + Tool.changeName(realName));
		if(payPasswdSet)
			submit.setText("提交");
	}
	

	private void initTitle() {
		addcredittitle.setTitle(R.string.add_credit_title);
		addcredittitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddBankCardActivity.this.finish();
			}
		});
		addcredittitle.setLeftImageButton(R.drawable.back);
		addcredittitle.setLeftTextButton("返回");

	}

	private void setListener() {
		TextChange cardNoListener = new TextChange();
		cardNo.addTextChangedListener(cardNoListener);
		chooseBankName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddBankCardActivity.this, ChooseBankNameActivity.class);
				AddBankCardActivity.this.startActivity(intent);
			}
		});
		submit.setClickable(false);
		submit.setOnClickListener(null);
//		submit.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(Tool.isBlank(bankCode)) {
//					KdlcDialog.showToast("请先选择绑卡银行");
//				} else if(Tool.isBlank(bankCardNo)) {
//					KdlcDialog.showToast("请输入银行卡号");
//				} else {
//					dialog = KdlcDialog.showProgressDialog(AddBankCardActivity.this, "正在绑卡...");
//					HttpParams params = new HttpParams();
//					Log.v("saddasasdgongshang", bankCode+"");
//					params.add("bank_card", bankCardNo.replaceAll(" ", "").trim());
//					params.add("bank_id", bankCode);
//					sendHttpPostCharge(userBindCardUrl, params, responseListener);
//				}
//			}
//		});

	}

	private Listener<JSONObject> responseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			dialog.cancel();
			try {
				Log.d("asdas", "response:"+response.toString());
				if (response.getInt("code") == 0) {
					KdlcDialog.showBottomToast("绑卡成功");
					KDLCApplication.app.setSessionVal("card_bind_status", "1");
					if(!payPasswdSet) {
						Intent intent = new Intent();
						intent.setClass(AddBankCardActivity.this, SetTradPwdActivity.class);
						startActivity(intent);
					}
					finish();
				} else {
					KdlcDialog.showInformDialog(AddBankCardActivity.this, response.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch bloc
					e.printStackTrace();
					KdlcDialog.showBottomToast("");
			}
		}
	};

	// EditText监听器
	class TextChange implements TextWatcher {
		int beforeTextLength = 0;
		int onTextLength = 0;
		boolean isChanged = false;

		int location = 0;// 记录光标的位置
		private char[] tempChar;
		private StringBuffer buffer = new StringBuffer();
		int konggeNumberB = 0;

		@Override
		public void afterTextChanged(Editable arg0) {
			if (isChanged) {
				location = cardNo.getSelectionEnd();
				int index = 0;
				while (index < buffer.length()) {
					if (buffer.charAt(index) == ' ') {
						buffer.deleteCharAt(index);
					} else {
						index++;
					}
				}

				index = 0;
				int konggeNumberC = 0;
				while (index < buffer.length()) {
					if ((index == 4 || index == 9 || index == 14 || index == 19)) {
						buffer.insert(index, ' ');
						konggeNumberC++;
					}
					index++;
				}

				if (konggeNumberC > konggeNumberB) {
					location += (konggeNumberC - konggeNumberB);
				}

				tempChar = new char[buffer.length()];
				buffer.getChars(0, buffer.length(), tempChar, 0);
				String str = buffer.toString();
				if (location > str.length()) {
					location = str.length();
				} else if (location < 0) {
					location = 0;
				}

				cardNo.setText(str);
				Editable etable = cardNo.getText();
				Selection.setSelection(etable, location);
				isChanged = false;
			}
		
		}

		@Override
		public void beforeTextChanged(CharSequence cs, int start, int before, int count) {
			beforeTextLength = cs.length();
			if (buffer.length() > 0) {
				buffer.delete(0, buffer.length());
			}
			konggeNumberB = 0;
			for (int i = 0; i < cs.length(); i++) {
				if (cs.charAt(i) == ' ') {
					konggeNumberB++;
				}
			}
		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before, int count) {
			bankCardNo = cs.toString();
			onTextLength = cs.length();
			buffer.append(cs.toString());
			if(!Tool.isBlank(bankCardNo) && !Tool.isBlank(bankCode)) {
				submit.setBackgroundResource(R.drawable.btn_red_background);
				submit.setClickable(true);
				submit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(Tool.isBlank(bankCode)) {
							KdlcDialog.showToast("请先选择绑卡银行");
						} else if(Tool.isBlank(bankCardNo)) {
							KdlcDialog.showToast("请输入银行卡号");
						} else {
							dialog = KdlcDialog.showProgressDialog(AddBankCardActivity.this, "正在绑卡...");
							HttpParams params = new HttpParams();
							Log.v("saddasasdgongshang", bankCode+"");
							params.add("bank_card", bankCardNo.replaceAll(" ", "").trim());
							params.add("bank_id", bankCode);
							sendHttpPostCharge(userBindCardUrl, params, responseListener);
						}
					}
				});

			} else {
				submit.setBackgroundResource(R.drawable.btn_grey_background);
				submit.setClickable(false);
				submit.setOnClickListener(null);
			}
			if (onTextLength == beforeTextLength || onTextLength <= 3
					|| isChanged) {
				isChanged = false;
				return;
			}
			isChanged = true;
			
		}		
	}

	@Override
	public void onResize(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		if (oldw != 0) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					addscrollview.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
		}
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if (action.equals(G.PAY_PASSWORD_SETTED)) {  
            	AddBankCardActivity.this.finish();
            } else if(action.equals(G.BROADCAST_SET_BANK_ID)) {
            	bankCode = intent.getStringExtra("bankCode");
            	bankName = intent.getStringExtra("bankName");
            	chooseBankName.setText(bankName);
    			if(!Tool.isBlank(bankCardNo) && !Tool.isBlank(bankCode)) {
    				submit.setBackgroundResource(R.drawable.btn_red_background);
    				submit.setClickable(true);
    				submit.setOnClickListener(new OnClickListener() {
    					@Override
    					public void onClick(View v) {
    						if(Tool.isBlank(bankCode)) {
    							KdlcDialog.showToast("请先选择绑卡银行");
    						} else if(Tool.isBlank(bankCardNo)) {
    							KdlcDialog.showToast("请输入银行卡号");
    						} else {
    							dialog = KdlcDialog.showProgressDialog(AddBankCardActivity.this, "正在绑卡...");
    							HttpParams params = new HttpParams();
    							Log.v("saddasasdgongshang", bankCode+"");
    							params.add("bank_card", bankCardNo.replaceAll(" ", "").trim());
    							params.add("bank_id", bankCode);
    							sendHttpPostCharge(userBindCardUrl, params, responseListener);
    						}
    					}
    				});
    			} else {
    				submit.setBackgroundResource(R.drawable.btn_grey_background);
    				submit.setClickable(false);
    				submit.setOnClickListener(null);
    			}
            }
        } 
	};  
      
	public void registerBoradcastReceiver(){  
	    IntentFilter myIntentFilter = new IntentFilter();  
	    myIntentFilter.addAction(G.PAY_PASSWORD_SETTED);
	    myIntentFilter.addAction(G.BROADCAST_SET_BANK_ID);
	    //注册广播        
	    registerReceiver(mBroadcastReceiver, myIntentFilter);  
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
}
