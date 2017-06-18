package com.kdkj.koudailicai.view.product;

import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.KdbDetailInfo;
import com.kdkj.koudailicai.domain.ProductBaseInfo;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.WebViewActivity;
import com.kdkj.koudailicai.view.invest.InvestAccountActivity;
import com.kdkj.koudailicai.view.selfcenter.profit.ProfitDetailActivity;

public class KdbDetailActivity extends BaseActivity implements
		HttpErrorInterface {
	private String LOG_TAG = this.getClass().getName();
	// size
	private int screenHeight;
	private int screenWidth;
	// dimens
	private double aprLabelMarginTop = 0.053;
	private double tipMarginTop = 0.036;
	private double computeMarginTop = 0.06;
	private double computeWidth = 0.919;
	private double computeTitleViewHeight = 0.076;
	private double computeMarginLeft = 0.062;
	private double computeLineMarginLeft = 0.047;
	private double computeEditWidth = 0.300;
	private double computeBtnWidth = 0.200;
	private double computeBankViewHeight = 0.065;
	private double productDetailMarginTop = 0.034;
	private double investBtnViewHeight = 0.100;
	private double investBtnHeight = 0.070;
	// views
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView scrollView;
	private RelativeLayout kdbInfoView;
	private RelativeLayout baseInfoView;
	private TitleView mTitle;
	private TextView aprLabel;
	private TextView apr;
	private RelativeLayout numView;
	private TextView investNum;
	private RelativeLayout tipView;
	private TextView accountTip;
	private RelativeLayout safeView;
	private TextView safeTip;
	private RelativeLayout computeProfitsView;
	private RelativeLayout computeTitleView;
	private TextView computeProfitsTitle;
	private View computeTitleLineView;
	private RelativeLayout computeAccountView;
	private TextView profitsText;
	private TextView bankProfits;
	private EditText computeAccount;
	private RelativeLayout computeDayView;
	private TextView bankProfitsTimes;
	private EditText computeDay;
	// private TextView computeProfitsBtn;
	private RelativeLayout profitsView;
	private View computeProfitsLineView;
	private RelativeLayout computeBankView;
	private TextView bankProfitsAccount;
	private TextView bankProfitsAccountTimes;
	private RelativeLayout productDetailView;
	private RelativeLayout productDescView;
	private TextView productType;
	private TextView productDesc;
	private RelativeLayout riskMeasuresView;
	private TextView fundsTrustTip;
	private TextView tradeGuarTip;
	private TextView fundsSpotTip;
	private TextView instructionText;
	private RelativeLayout investBtnView;
	private TextView investBtn;
	private RelativeLayout subBtnView;
	// contents
	private String getKdbDetailUrl;
	private String getKdbDetailH5Url;
	private String getRiskH5Url;
	private String warrantWord;
	private KdbDetailInfo kdbInfo;
	private boolean hasCachedData = false;
	private float lastY = 0;
	Handler mHandler = new Handler();
	//网络出错
	private RelativeLayout errNetLayout;
	private TextView networkLoad;
	private TextView networkText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kdb_detail);
		parseUrl();
		findViews();
		initTitle();
		initSize();
		initContents();
		setListeners();
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			getKdbDetailUrl = getApplicationContext().getActionUrl(
					G.GCK_API_GET_KDB_DETAIL);
			getKdbDetailH5Url = getApplicationContext().getActionUrl(
					G.GCK_API_H5_KDB_DETAIL);
			getRiskH5Url = getApplicationContext().getActionUrl(
					G.GCK_API_GET_PAGE_FXBZJ);
			warrantWord = getApplicationContext().getConfVal(
					G.GCK_VAL_WARRANT_WORD);
		} else {
			getKdbDetailUrl = G.URL_GET_KDB_DETAIL;
			getKdbDetailH5Url = G.URL_H5_KDB_DETAIL;
			getRiskH5Url = G.URL_GET_PAGE_FXBZJ;
			warrantWord = G.VAL_WARRANT_WORD;
		}
	}

	private void findViews() {
		mPullRefreshScrollView = (PullToRefreshScrollView) this.findViewById(R.id.refresh_root);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				mHandler.postDelayed(new Runnable() {
					public void run() {
						errorListener.setRefreshView(mPullRefreshScrollView);
						errorListener.setErrInterface(kdbInfo == null ? KdbDetailActivity.this : null);
						sendHttpGet(getKdbDetailUrl, getKdbInfoListener);
					}
				}, G.AUTO_LOAD_DELAYED);
			}
		});
		mPullRefreshScrollView.showPullLine(true);
		scrollView = (ScrollView) this.findViewById(R.id.scrollview);
		mTitle = (TitleView) this.findViewById(R.id.title);
		kdbInfoView = (RelativeLayout) this.findViewById(R.id.kdbInfoView);
		baseInfoView = (RelativeLayout) this.findViewById(R.id.baseInfoView);
		aprLabel = (TextView) this.findViewById(R.id.aprLabel);
		apr = (TextView) this.findViewById(R.id.apr);
		numView = (RelativeLayout) this.findViewById(R.id.numView);
		investNum = (TextView) this.findViewById(R.id.investNum);
		tipView = (RelativeLayout) this.findViewById(R.id.tipView);
		accountTip = (TextView) this.findViewById(R.id.accountTip);
		safeTip = (TextView) this.findViewById(R.id.safeTip);

		safeView = (RelativeLayout) this.findViewById(R.id.safeView);
		computeProfitsView = (RelativeLayout) this
				.findViewById(R.id.computeProfitsView);
		computeTitleView = (RelativeLayout) this
				.findViewById(R.id.computeTitleView);
		computeProfitsTitle = (TextView) this
				.findViewById(R.id.computeProfitsTitle);
		computeTitleLineView = (View) this.findViewById(R.id.computeTitleLine);
		computeAccountView = (RelativeLayout) this
				.findViewById(R.id.computeAccountView);
		bankProfits = (TextView) this.findViewById(R.id.bankProfits);
		computeAccount = (EditText) this.findViewById(R.id.computeAccount);
		computeAccount.setText(R.string.kdbdetail_compute_account);
		computeAccount.setSelection(computeAccount.getText().length());
		computeDayView = (RelativeLayout) this
				.findViewById(R.id.computeDayView);
		computeAccount.setText(R.string.kdbdetail_compute_account);
		profitsText = (TextView) this.findViewById(R.id.profits);
		bankProfitsTimes = (TextView) this.findViewById(R.id.bankProfitsTimes);
		computeDay = (EditText) this.findViewById(R.id.computeDay);
		computeDay.requestFocus();
		computeDay.setText(R.string.kdbdetail_compute_days);
		computeDay.setSelection(computeDay.getText().length());
		// computeProfitsBtn = (TextView)
		// this.findViewById(R.id.computeProfitsBtn);
		profitsView = (RelativeLayout) this.findViewById(R.id.profitsView);
		computeProfitsLineView = (View) this
				.findViewById(R.id.computeProfitsLine);
		computeBankView = (RelativeLayout) this
				.findViewById(R.id.computeBankView);
		bankProfitsAccount = (TextView) this
				.findViewById(R.id.bankProfitsAccount);
		bankProfitsAccountTimes = (TextView) this
				.findViewById(R.id.bankProfitsAccountTimes);
		productDetailView = (RelativeLayout) this
				.findViewById(R.id.productDetailView);
		productDescView = (RelativeLayout) this
				.findViewById(R.id.productDescView);
		riskMeasuresView = (RelativeLayout) this
				.findViewById(R.id.riskMeasuresView);
		productType = (TextView) this.findViewById(R.id.productType);
		productDesc = (TextView) this.findViewById(R.id.productDesc);
		fundsTrustTip = (TextView) this.findViewById(R.id.fundsTrustTip);
		tradeGuarTip = (TextView) this.findViewById(R.id.tradeGuarTip);
		fundsSpotTip = (TextView) this.findViewById(R.id.fundsSpotTip);
		instructionText = (TextView) this.findViewById(R.id.instructionText); 
		subBtnView = (RelativeLayout) this.findViewById(R.id.subBtnView);
		investBtn = (TextView) this.findViewById(R.id.investBtn);
		investBtnView = (RelativeLayout) this.findViewById(R.id.investBtnView);
		
		errNetLayout=(RelativeLayout)findViewById(R.id.errNetLayout);
		networkLoad=(TextView)findViewById(R.id.networkload);		
		networkText = (TextView) findViewById(R.id.networktext);
		networkLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRefreshView();
				mPullRefreshScrollView.setLoadRefreshing();
			}
		});
	}

	private void initTitle() {
		mTitle.setTitle(R.string.kdbdetail_title);
		mTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				KdbDetailActivity.this.finish();
			}
		});
		mTitle.setLeftImageButton(R.drawable.back);
		mTitle.setLeftTextButton("返回");
	}

	private void initSize() {
		screenHeight = this.getScreenHeight();
		screenWidth = this.getScreenWidth();
		// 设置基本信息
		RelativeLayout.LayoutParams aprLabelLayoutParams = (RelativeLayout.LayoutParams) aprLabel
				.getLayoutParams();
		aprLabelLayoutParams.topMargin = (int) (screenHeight * aprLabelMarginTop);
		aprLabel.setLayoutParams(aprLabelLayoutParams);
		RelativeLayout.LayoutParams tipViewLayoutParams = (RelativeLayout.LayoutParams) tipView
				.getLayoutParams();
		tipViewLayoutParams.topMargin = (int) (screenHeight * tipMarginTop);
		tipView.setLayoutParams(tipViewLayoutParams);
		RelativeLayout.LayoutParams safeViewLayoutParams = (RelativeLayout.LayoutParams) safeView
				.getLayoutParams();
		safeViewLayoutParams.topMargin = (int) (screenHeight
				* aprLabelMarginTop / 3);
		safeViewLayoutParams.leftMargin = (int) (screenHeight
				* aprLabelMarginTop / 2);
		safeViewLayoutParams.rightMargin = (int) (screenHeight
				* aprLabelMarginTop / 2);
		safeView.setLayoutParams(safeViewLayoutParams);
		// 设置计算收益布局
		RelativeLayout.LayoutParams computeProfitsViewLayoutParams = (RelativeLayout.LayoutParams) computeProfitsView
				.getLayoutParams();
		computeProfitsViewLayoutParams.width = (int) (screenWidth * computeWidth);
		computeProfitsViewLayoutParams.topMargin = (int) (screenHeight * computeMarginTop);
		computeProfitsViewLayoutParams.leftMargin = (int) ((screenWidth - (screenWidth * computeWidth)) / 2);
		computeProfitsView.setLayoutParams(computeProfitsViewLayoutParams);
		// 计算收益标题布局
		RelativeLayout.LayoutParams computeTitleViewLayoutParams = (RelativeLayout.LayoutParams) computeTitleView
				.getLayoutParams();
		computeTitleViewLayoutParams.height = (int) (screenHeight * computeTitleViewHeight);
		computeTitleView.setLayoutParams(computeTitleViewLayoutParams);
		RelativeLayout.LayoutParams computeProfitsTitleLayoutParams = (RelativeLayout.LayoutParams) computeProfitsTitle
				.getLayoutParams();
		computeProfitsTitleLayoutParams.bottomMargin = (int) (screenHeight
				* aprLabelMarginTop / 2);
		computeProfitsTitle.setLayoutParams(computeProfitsTitleLayoutParams);
		RelativeLayout.LayoutParams computeTitleLineViewLayoutParams = (RelativeLayout.LayoutParams) computeTitleLineView
				.getLayoutParams();
		computeTitleLineViewLayoutParams.topMargin = (int) (screenWidth * computeLineMarginLeft);
		computeTitleLineViewLayoutParams.leftMargin = (int) (screenWidth * computeLineMarginLeft);
		computeTitleLineView.setLayoutParams(computeTitleLineViewLayoutParams);
		// 计算收益金额输入布局
		RelativeLayout.LayoutParams computeAccountViewLayoutParams = (RelativeLayout.LayoutParams) computeAccountView
				.getLayoutParams();
		computeAccountViewLayoutParams.leftMargin = (int) (screenWidth * computeMarginLeft);
		computeAccountViewLayoutParams.topMargin = (int) (screenHeight
				* aprLabelMarginTop / 2);
		computeAccountViewLayoutParams.width = (int) (screenWidth * computeEditWidth);
		computeAccountView.setLayoutParams(computeAccountViewLayoutParams);
		/*
		 * RelativeLayout.LayoutParams bankProfitsLayoutParams =
		 * (RelativeLayout.LayoutParams) bankProfits.getLayoutParams();
		 * bankProfitsLayoutParams.leftMargin = (int)
		 * (screenWidth*(computeMarginLeft-computeLineMarginLeft));
		 * bankProfits.setLayoutParams(bankProfitsLayoutParams);
		 */
		// 计算收益天数输入布局
		RelativeLayout.LayoutParams computeDayViewLayoutParams = (RelativeLayout.LayoutParams) computeDayView
				.getLayoutParams();
		computeDayViewLayoutParams.leftMargin = (int) (screenWidth
				* computeLineMarginLeft / 2);
		computeDayViewLayoutParams.topMargin = (int) (screenHeight
				* aprLabelMarginTop / 2);
		computeDayViewLayoutParams.width = (int) (screenWidth * computeEditWidth);
		computeDayView.setLayoutParams(computeDayViewLayoutParams);
		/*
		 * RelativeLayout.LayoutParams bankProfitsTimesLayoutParams =
		 * (RelativeLayout.LayoutParams) bankProfitsTimes.getLayoutParams();
		 * bankProfitsTimesLayoutParams.leftMargin = (int)
		 * (screenWidth*(computeMarginLeft-computeLineMarginLeft));
		 * bankProfitsTimes.setLayoutParams(bankProfitsTimesLayoutParams);
		 */
		// 计算收益按钮布局
		/*
		 * RelativeLayout.LayoutParams computeProfitsBtnLayoutParams =
		 * (RelativeLayout.LayoutParams) computeProfitsBtn.getLayoutParams();
		 * computeProfitsBtnLayoutParams.rightMargin = (int)
		 * (screenWidth*computeLineMarginLeft);
		 * computeProfitsBtnLayoutParams.width = (int)
		 * (screenWidth*computeBtnWidth);
		 * computeProfitsBtn.setLayoutParams(computeProfitsBtnLayoutParams);
		 */

		RelativeLayout.LayoutParams profitsViewLayoutParams = (RelativeLayout.LayoutParams) profitsView
				.getLayoutParams();
		profitsViewLayoutParams.leftMargin = (int) (screenWidth * computeMarginLeft);
		profitsView.setLayoutParams(profitsViewLayoutParams);
		RelativeLayout.LayoutParams computeProfitsLineViewLayoutParams = (RelativeLayout.LayoutParams) computeProfitsLineView
				.getLayoutParams();
		computeProfitsLineViewLayoutParams.rightMargin = (int) (screenWidth * computeLineMarginLeft);
		computeProfitsLineViewLayoutParams.leftMargin = (int) (screenWidth * computeLineMarginLeft);
		computeProfitsLineView
				.setLayoutParams(computeProfitsLineViewLayoutParams);
		// 计算银行收益布局
		RelativeLayout.LayoutParams computeBankViewLayoutParams = (RelativeLayout.LayoutParams) computeBankView
				.getLayoutParams();
		computeBankViewLayoutParams.leftMargin = (int) (screenWidth * computeMarginLeft);
		computeBankViewLayoutParams.height = (int) (screenHeight * computeBankViewHeight);
		computeBankView.setLayoutParams(computeBankViewLayoutParams);
		// 产品详情布局
		RelativeLayout.LayoutParams productDetailViewLayoutParams = (RelativeLayout.LayoutParams) productDetailView
				.getLayoutParams();
		productDetailViewLayoutParams.topMargin = (int) (screenHeight * productDetailMarginTop);
		productDetailView.setLayoutParams(productDetailViewLayoutParams);
		RelativeLayout.LayoutParams subBtnViewLayoutParams = (RelativeLayout.LayoutParams) subBtnView
				.getLayoutParams();
		subBtnViewLayoutParams.height = (int) (screenHeight * investBtnViewHeight);
		subBtnView.setLayoutParams(subBtnViewLayoutParams);
		// 计算底部按钮布局
		RelativeLayout.LayoutParams investBtnViewLayoutParams = (RelativeLayout.LayoutParams) investBtnView
				.getLayoutParams();
		investBtnViewLayoutParams.height = (int) (screenHeight * investBtnViewHeight);
		investBtnView.setLayoutParams(investBtnViewLayoutParams);
		RelativeLayout.LayoutParams investBtnLayoutParams = (RelativeLayout.LayoutParams) investBtn
				.getLayoutParams();
		investBtnLayoutParams.height = (int) (screenHeight * investBtnHeight);
		investBtnLayoutParams.leftMargin = (int) ((screenWidth - (screenWidth * computeWidth)) / 2);
		investBtnLayoutParams.rightMargin = (int) ((screenWidth - (screenWidth * computeWidth)) / 2);
		investBtn.setLayoutParams(investBtnLayoutParams);
	}

	private void setListeners() {
		// computeAccount.setOnClickListener(scroolPage);
		// computeDay.setOnClickListener(dayScroolPage);
		computeAccount.setOnTouchListener(editTextListener);
		computeDay.setOnTouchListener(editTextListener);
		// computeProfitsBtn.setOnClickListener(computeProfits);
		investBtn.setOnClickListener(toInvest);
		numView.setOnClickListener(toTenderConstruction);
		computeAccount.addTextChangedListener(accountTextWatcher);
		computeDay.addTextChangedListener(accountTextWatcher);
		productDescView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(KdbDetailActivity.this,
						WebViewActivity.class);
				intent.putExtra("url", KdbDetailActivity.this.getKdbDetailH5Url);
				intent.putExtra("title", "项目描述");
				KdbDetailActivity.this.startActivity(intent);
			}
		});

		riskMeasuresView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(KdbDetailActivity.this,
						WebViewActivity.class);
				intent.putExtra("url", KdbDetailActivity.this.getRiskH5Url);
				intent.putExtra("title", "风险措施");
				KdbDetailActivity.this.startActivity(intent);
			}
		});
	}

	private OnClickListener toInvest = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(KdbDetailActivity.this,
					InvestAccountActivity.class);
			Bundle bundle = new Bundle();
			ProductBaseInfo productBaseInfo = new ProductBaseInfo();
			productBaseInfo.getFromKdb(kdbInfo);
			bundle.putParcelable(G.PRODUCT_INFO_SER_KEY, productBaseInfo);
			intent.putExtras(bundle);
			startActivityNeedLogin(intent);
		}
	};

	private OnClickListener scroolPage = new OnClickListener() {
		@SuppressLint("NewApi")
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			lastY = scrollView.getY();
			Log.d(LOG_TAG, "lastY22:" + lastY);
			final int scrollY = (int) (screenHeight * computeMarginTop)
					+ baseInfoView.getMeasuredHeight() - 2;
			Log.d(LOG_TAG, "scrollY:" + scrollY);
			mHandler.post(new Runnable() {
				public void run() {
					if (scrollView == null) {
						return;
					}
					LogUtil.info("真的执行scroolPage");
					scrollView.scrollTo(0, scrollY);
				}
			});
		}
	};

	private OnClickListener dayScroolPage = new OnClickListener() {
		@SuppressLint("NewApi")
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			lastY = scrollView.getY();
			Log.d(LOG_TAG, "lastY22:" + lastY);
			final int scrollY = (int) (screenHeight * computeMarginTop)
					+ baseInfoView.getMeasuredHeight() - 2;
			Log.d(LOG_TAG, "scrollY:" + scrollY);
			mHandler.post(new Runnable() {
				public void run() {
					if (scrollView == null) {
						return;
					}
					LogUtil.info("真的执行dayScroolPage");
					scrollView.scrollTo(0, scrollY);
				}
			});
			mHandler.postDelayed(new Runnable() {
				public void run() {
					if (scrollView == null) {
						return;
					}
					scrollView.scrollTo(0, scrollY);
				}
			}, 500);
		}
	};

	private OnClickListener computeProfits = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String account = computeAccount.getText().toString();
			String days = computeDay.getText().toString();
			Toast toast;
			if (Tool.isBlank(account)) {
				toast = Toast.makeText(KdbDetailActivity.this, "请输入投入金额",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, screenHeight / 2 - 50);
				toast.show();
			} else if (Tool.isBlank(days)) {
				toast = Toast.makeText(KdbDetailActivity.this, "请输入理财期限",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, screenHeight / 2 - 50);
				toast.show();
			} else {
				/*
				 * InputMethodManager m = (InputMethodManager)
				 * getSystemService(Context.INPUT_METHOD_SERVICE); m
				 * .hideSoftInputFromWindow
				 * (KdbDetailActivity.this.getCurrentFocus().getWindowToken(),
				 * 0); mHandler.post(new Runnable() { public void run() { if
				 * (scrollView == null) { return; } scrollView.scrollTo(0, 0); }
				 * });
				 */
				computeProfites();
			}
		}
	};
	private OnClickListener toTenderConstruction = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(KdbDetailActivity.this,
					TenderConstructionActivity.class);
			intent.putExtra("type", "kdb");
			KdbDetailActivity.this.startActivity(intent);
		}
	};

	private void computeProfites() {
		String account = computeAccount.getText().toString();
		String days = computeDay.getText().toString();
		if (Tool.isBlank(account) || Integer.parseInt(account) <= 0
				|| Tool.isBlank(days) || Integer.parseInt(days) <= 0) {
			profitsText.setText("0.00");
			bankProfitsAccount.setText("银行活期利息0.00元，是银行利息的");
			bankProfitsAccountTimes.setText("0倍");
		} else {
			int accountVal = Integer.parseInt(account);
			int daysVal = Integer.parseInt(days);
			double aprVal = Double.parseDouble(apr.getText().toString());
			double bankApr = Double.parseDouble(kdbInfo.getBankApr());
			double profitsVal = accountVal * aprVal * daysVal / 365 / 100;
			double bankProfitsVal = accountVal * bankApr * daysVal / 365 / 100;
			profitsText.setText(Tool.toFFDouble(profitsVal));
			bankProfitsAccount.setText("银行活期利息"
					+ Tool.toFFDouble(bankProfitsVal) + "元，是银行利息的");
			bankProfitsAccountTimes.setText(""
					+ Tool.toFFDouble(profitsVal / bankProfitsVal) + "倍");
		}
	}

	private void initContents() {
		List<KdbDetailInfo> list = KDLCApplication.app.kdDb.findAll(KdbDetailInfo.class);
		if (list != null && list.size() > 0) {
			kdbInfo = list.get(0);
			if (kdbInfo != null) {
				hasCachedData = true;
				setContents();
			}
		}
		mHandler.postDelayed(new Runnable() {
			public void run() {
				mPullRefreshScrollView.setLoadRefreshing();
			}
		}, 500);
	}

	private void setContents() {
		if (kdbInfo != null) {
			apr.setText(kdbInfo.getApr());
			investNum.setText(kdbInfo.getCurInvestTimes() + "人");
			accountTip.setText(Tool.toIntAccount(kdbInfo.getMinInvestMoney())
					+ "元起购");
			safeTip.setText(warrantWord);
			productType.setText(kdbInfo.getProductType());
			productDesc.setText(kdbInfo.getSummary());
			fundsTrustTip.setText(kdbInfo.getRiskControlManaged());
			tradeGuarTip.setText(kdbInfo.getRiskControlWarrant());
			fundsSpotTip.setText(kdbInfo.getRiskControlRepay());
			instructionText.setText(kdbInfo.getInstruction());
			computeProfites();
			kdbInfoView.setVisibility(View.VISIBLE);
			investBtnView.setVisibility(View.VISIBLE);
		}
	}

	private Listener<JSONObject> getKdbInfoListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				if (response.getInt("code") == 0) {
					kdbInfoView.setVisibility(View.VISIBLE);
					JSONObject data = response.getJSONObject("info");
					if (!hasCachedData) {
						kdbInfo = new KdbDetailInfo();
					}
					kdbInfo.setTitle(data.getString("title"));
					kdbInfo.setTotalMoney(data.getString("total_money"));
					kdbInfo.setRemainMoney(data.getString("remain_money"));
					kdbInfo.setApr(data.getString("apr"));
					kdbInfo.setBankApr(data.getString("bank_apr"));
					kdbInfo.setSummary(data.getString("summary"));
					kdbInfo.setStatus(data.getString("status"));
					kdbInfo.setDailyInvestLimit(data.getString("daily_invest_limit"));
					kdbInfo.setDailyWithdrawLimit(data.getString("daily_withdraw_limit"));
					kdbInfo.setInterestDesc(data.getString("interest_desc"));
					kdbInfo.setMinInvestMoney(data.getString("min_invest_money"));
					kdbInfo.setProductType(data.getString("product_type"));
					kdbInfo.setCurInvestTimes(data.getString("cur_invest_times"));
					kdbInfo.setRiskControlManaged(data.getString("risk_control_managed"));
					kdbInfo.setRiskControlWarrant(data.getString("risk_control_warrant"));
					kdbInfo.setRiskControlRepay(data.getString("risk_control_repay"));
					kdbInfo.setInstruction(data.getString("instruction"));
					if (hasCachedData) {
						KDLCApplication.app.kdDb.update(kdbInfo);
					} else {
						KDLCApplication.app.kdDb.save(kdbInfo);
					}
					setContents();
				} else if (response.getInt("code") == -2) {
					dialog = KdlcDialog.showLoginDialog(KdbDetailActivity.this);
				} else {
					// TODO:加载失败，请重试
					if (kdbInfo == null) {
						showErrReq();
					}
					dialog =KdlcDialog.showInformDialog(KdbDetailActivity.this,
							response.getString("message"));

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(kdbInfo == null) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}			}
			mPullRefreshScrollView.onRefreshComplete();
		}
	};

	TextWatcher accountTextWatcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
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
			computeProfites();
		}
	};
	
	@Override
	protected void onStart() {
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(mPullRefreshScrollView);
		} else {
			createFlag = false;
		}
	}
	
	@SuppressLint("NewApi")
	OnTouchListener editTextListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			lastY = scrollView.getY();
			Log.d(LOG_TAG, "lastY22:" + lastY);
			final int scrollY = (int) (screenHeight * computeMarginTop)
					+ baseInfoView.getMeasuredHeight() - 2;
			Log.d(LOG_TAG, "scrollY:" + scrollY);
			mHandler.post(new Runnable() {
				public void run() {
					if (scrollView == null) {
						return;
					}
					scrollView.scrollTo(0, scrollY);

				}
			});
			if (v.getId() == R.id.computeDay) {
				computeDay.requestFocus();
			} else
				computeAccount.requestFocus();
			// 如果软键盘未打开就打开软键盘
			InputMethodManager imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imgr == null) {
				imgr.showSoftInput(computeDay, 0);
				imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
			return false;
		}
	};
	
	@Override
	public void showNoNetwork() {
		// TODO Auto-generated method stub
		showNetView(false);
	}

	@Override
	public void showErrReq() {
		// TODO Auto-generated method stub
		showNetView(true);
	}
	
	private void showNetView(boolean err) {
		mPullRefreshScrollView.setVisibility(View.GONE);
		errNetLayout.setVisibility(View.VISIBLE);
		networkText.setText(err ? "网络出错" : "网络未连接");
	}

	private void showRefreshView() {
		mPullRefreshScrollView.setVisibility(View.VISIBLE);
		errNetLayout.setVisibility(View.GONE);
	}
}
