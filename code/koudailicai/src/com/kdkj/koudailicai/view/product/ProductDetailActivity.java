package com.kdkj.koudailicai.view.product;

import java.util.Timer;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.ProductDetailInfo;
import com.kdkj.koudailicai.domain.ProductBaseInfo;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener.HttpErrorInterface;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.KdlcProgressBar;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshScrollView;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.WebViewActivity;
import com.kdkj.koudailicai.view.invest.InvestAccountActivity;

public class ProductDetailActivity extends BaseActivity implements
		HttpErrorInterface {
	private String LOG_TAG = this.getClass().getName();
	// size
	private int screenHeight;
	private int screenWidth;
	// dimens
	private double aprLabelMarginTop = 0.053;
	private double tipMarginTop = 0.036;

	private double progressMarginTop = 0.044;
	private double progressWidth = 0.919;
	private double totalAccountViewWidth = 0.454;
	private double totalAccountViewMarginLeft = 0.062;
	private double totalAccountViewMarginTop = 0.026;
	private double accountViewLineMarginTop = 0.017;
	private double progressImageViewMarginBottom = 0.034;
	private double progressImageViewHeight = 0.028;
	private double computeMarginTop = 0.03;
	private double computeWidth = 0.919;
	private double computeTitleViewHeight = 0.076;
	private double computeMarginLeft = 0.062;
	private double computeLineMarginLeft = 0.047;
	private double computeEditWidth = 0.300;
	private double computeBtnWidth = 0.200;
	private double computeBankViewHeight = 0.065;

	private double productDetailMarginTop = 0.034;
	private double investBtnViewHeight = 0.100;
	private double investBtnHeight = 0.07;
	// views
	private TitleView mTitle;
	private ScrollView scrollView;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private RelativeLayout detailView;
	private RelativeLayout baseInfoView;
	private TextView aprLabel;
	private TextView apr;
	private TextView time;
	private RelativeLayout tipView;
	private TextView accountTip;
	// private TextView timeTip;
	private TextView profitsTip;
	private TextView safeTip;
	private RelativeLayout producDescView;
	private RelativeLayout progressView;
	private RelativeLayout totalAccountView;
	private TextView totalAccount;
	private TextView leftAccount;
	private View accountViewLine;
	private TextView progressText;
	private RelativeLayout progressBarTitle;
	private RelativeLayout progressBarImageView;
	private KdlcProgressBar progressRedBar;

	private RelativeLayout safeView;

	private RelativeLayout repayStatusView;
	private ImageView statusIcon;
	private TextView repayTotalAccount;
	private TextView repayDay;
	private ImageView finishImageView;
	private RelativeLayout computeProfitsView;// 计算收益整体
	private RelativeLayout computeTitleView;
	private TextView computeProfitsTitle;
	private View computeTitleLineView;
	private RelativeLayout computeAccountView;
	private TextView profitsText;
	private TextView bankProfits;
	private TextView bankProfitsAccount;
	private TextView bankProfitsAccountTimes;
	private EditText computeAccount;// 投入金额
	private RelativeLayout computeDayView;
	private TextView bankProfitsTimes;
	private TextView computeDay;
	private RelativeLayout profitsView;
	private View computeProfitsLineView;
	private RelativeLayout computeBankView;

	private RelativeLayout productDetailView;
	private TextView productType;
	private TextView productDesc;
	private TextView investNum;
	private TextView fundsTrustTip;
	private TextView tradeGuarTip;
	private TextView fundsSpotTip;
	private TextView interestDayTip;
	private TextView repayDayTip;
	private TextView repayInstructionTip;
	private RelativeLayout riskMeasuresView;
	private RelativeLayout investBtnView, investNumView;
	private TextView investBtn;
	
	private RelativeLayout errNetLayout;
	private TextView networkLoad;
	private TextView networkText;

	// contents
	private String warrantWord;
	private String getProductDetailUrl;
	private String getProductDetailH5Url;
	private String getRiskH5Url;
	private String productName;
	private long productId;
	private ProductDetailInfo productInfo;
	private boolean hasCachedData = false;
	private float lastY = 0;
	Handler mHandler = new Handler();
	private double progress;
	private Timer timer;
	private int maxProgress;
	private int curProgress = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		getExtras();
		parseUrl();
		findViews();
		initSize();
		initTitle();
		setListeners();
		initContents();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(!createFlag) {
			Tool.resetPullRefreshView(mPullRefreshScrollView);
		} else {
			createFlag = false;
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onRestoreInstanceState called");
		initContents();
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void getExtras() {
		productId = this.getIntent().getExtras().getLong("productId");
		productName = this.getIntent().getExtras().getString("productName");
	}

	private void parseUrl() {
		if (this.getApplicationContext().isGlobalConfCompleted()) {
			getProductDetailUrl = getApplicationContext().getActionUrl(
					G.GCK_API_GET_PROJECT_DETAIL);
			getProductDetailH5Url = getApplicationContext().getActionUrl(
					G.GCK_API_H5_PROJECT_DETAIL);
			getRiskH5Url = getApplicationContext().getActionUrl(
					G.GCK_API_GET_PAGE_FXBZJ);
			warrantWord = getApplicationContext().getConfVal(
					G.GCK_VAL_WARRANT_WORD);
		} else {
			getProductDetailUrl = G.URL_GET_PROJECT_DETAIL;
			getProductDetailH5Url = G.URL_H5_PROJECT_DETAIL;
			getRiskH5Url = G.URL_GET_PAGE_FXBZJ;
			warrantWord = G.VAL_WARRANT_WORD;
		}
	}

	private void findViews() {
		mPullRefreshScrollView = (PullToRefreshScrollView) this
				.findViewById(R.id.refresh_root);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						mHandler.postDelayed(new Runnable() {
							public void run() {
								errorListener.setRefreshView(mPullRefreshScrollView);
								errorListener.setErrInterface(productInfo == null ? ProductDetailActivity.this : null);
								sendHttpGet(getProductDetailUrl + "?id=" + productId, getProductInfoListener);
							}
						}, G.AUTO_LOAD_DELAYED);
					}
				});
		mPullRefreshScrollView.showPullLine(true);
		scrollView = (ScrollView) this.findViewById(R.id.scrollview);
		detailView = (RelativeLayout) this.findViewById(R.id.detailView);
		mTitle = (TitleView) this.findViewById(R.id.title);
		baseInfoView = (RelativeLayout) this.findViewById(R.id.baseInfoView);
		aprLabel = (TextView) this.findViewById(R.id.aprLabel);
		apr = (TextView) this.findViewById(R.id.apr);
		time = (TextView) this.findViewById(R.id.time);
		tipView = (RelativeLayout) this.findViewById(R.id.tipView);
		accountTip = (TextView) this.findViewById(R.id.accountTip);
		// timeTip = (TextView) this.findViewById(R.id.timeTip);
		profitsTip = (TextView) this.findViewById(R.id.profitsTip);
		safeTip = (TextView) this.findViewById(R.id.safeTip);

		repayStatusView = (RelativeLayout) this
				.findViewById(R.id.repayStatusView);
		statusIcon = (ImageView) this.findViewById(R.id.statusIcon);
		repayTotalAccount = (TextView) this
				.findViewById(R.id.repayTotalAccount);
		repayDay = (TextView) this.findViewById(R.id.repayDay);
		finishImageView = (ImageView) this.findViewById(R.id.finishImageView);
		progressView = (RelativeLayout) this.findViewById(R.id.progressView);
		totalAccountView = (RelativeLayout) this
				.findViewById(R.id.totalAccountView);
		totalAccount = (TextView) this.findViewById(R.id.totalAccount);
		leftAccount = (TextView) this.findViewById(R.id.leftAccount);
		accountViewLine = (View) this.findViewById(R.id.accountViewLine);
		progressText = (TextView) this.findViewById(R.id.progressText);
		progressBarTitle = (RelativeLayout) this
				.findViewById(R.id.progressBarTitle);
		progressBarImageView = (RelativeLayout) this
				.findViewById(R.id.progressBarImageView);
		;
		progressRedBar = (KdlcProgressBar) this
				.findViewById(R.id.progressRedBar);

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
		computeAccount.setText(R.string.productdetail_compute_account);
		computeAccount.setSelection(computeAccount.getText().length());
		computeDayView = (RelativeLayout) this
				.findViewById(R.id.computeDayView);
		profitsText = (TextView) this.findViewById(R.id.profits);
		bankProfitsTimes = (TextView) this.findViewById(R.id.bankProfitsTimes);
		computeDay = (TextView) this.findViewById(R.id.computeDay);
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
		producDescView = (RelativeLayout) this
				.findViewById(R.id.producDescView);
		investNum = (TextView) this.findViewById(R.id.investNum);
		productType = (TextView) this.findViewById(R.id.productType);
		productDesc = (TextView) this.findViewById(R.id.productDesc);
		fundsTrustTip = (TextView) this.findViewById(R.id.fundsTrustTip);
		tradeGuarTip = (TextView) this.findViewById(R.id.tradeGuarTip);
		fundsSpotTip = (TextView) this.findViewById(R.id.fundsSpotTip);
		interestDayTip = (TextView) this.findViewById(R.id.interestDayTip);
		repayDayTip = (TextView) this.findViewById(R.id.repayDayTip);
		riskMeasuresView = (RelativeLayout) this
				.findViewById(R.id.riskMeasuresView);
		repayInstructionTip = (TextView) this
				.findViewById(R.id.repayInstructionTip);
		investBtn = (TextView) this.findViewById(R.id.investBtn);
		investBtnView = (RelativeLayout) this.findViewById(R.id.investBtnView);
		investNumView = (RelativeLayout) this.findViewById(R.id.investNumView);
		
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
		if (!Tool.isBlank(productName)) {
			mTitle.setTitle(productName);
		} else {
			mTitle.setTitle(R.string.productdetail_title);
		}
		mTitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ProductDetailActivity.this.finish();

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
				* aprLabelMarginTop / 2);
		safeView.setLayoutParams(safeViewLayoutParams);
		// 设置进度布局
		RelativeLayout.LayoutParams progressViewLayoutParams = (RelativeLayout.LayoutParams) progressView
				.getLayoutParams();
		progressViewLayoutParams.width = (int) (screenWidth * progressWidth);
		progressViewLayoutParams.topMargin = (int) (screenHeight * progressMarginTop);
		progressViewLayoutParams.leftMargin = (int) ((screenWidth - (screenWidth * progressWidth)) / 2);
		progressView.setLayoutParams(progressViewLayoutParams);
		RelativeLayout.LayoutParams totalAccountViewLayoutParams = (RelativeLayout.LayoutParams) totalAccountView
				.getLayoutParams();
		totalAccountViewLayoutParams.width = (int) (screenWidth * totalAccountViewWidth);
		totalAccountViewLayoutParams.bottomMargin = (int) (screenHeight * accountViewLineMarginTop);
		totalAccountViewLayoutParams.topMargin = (int) (screenHeight * totalAccountViewMarginTop);
		totalAccountViewLayoutParams.leftMargin = (int) (screenWidth * totalAccountViewMarginLeft);
		totalAccountView.setLayoutParams(totalAccountViewLayoutParams);
		RelativeLayout.LayoutParams accountViewLineLayoutParams = (RelativeLayout.LayoutParams) accountViewLine
				.getLayoutParams();
		accountViewLineLayoutParams.leftMargin = (int) (screenWidth * computeLineMarginLeft);
		accountViewLineLayoutParams.rightMargin = (int) (screenWidth * computeLineMarginLeft);
		accountViewLine.setLayoutParams(accountViewLineLayoutParams);
		RelativeLayout.LayoutParams progressBarTitleLayoutParams = (RelativeLayout.LayoutParams) progressBarTitle
				.getLayoutParams();
		progressBarTitleLayoutParams.topMargin = (int) (screenHeight * totalAccountViewMarginTop);
		progressBarTitle.setLayoutParams(progressBarTitleLayoutParams);
		RelativeLayout.LayoutParams progressBarImageViewLayoutParams = (RelativeLayout.LayoutParams) progressBarImageView
				.getLayoutParams();
		progressBarImageViewLayoutParams.width = (int) (screenWidth * (progressWidth - totalAccountViewMarginLeft * 2));
		progressBarImageViewLayoutParams.topMargin = (int) (screenHeight * totalAccountViewMarginTop);
		progressBarImageViewLayoutParams.leftMargin = (int) (screenWidth * totalAccountViewMarginLeft);
		progressBarImageViewLayoutParams.bottomMargin = (int) (screenHeight * progressImageViewMarginBottom);
		progressBarImageViewLayoutParams.height = (int) (screenHeight * progressImageViewHeight);
		progressBarImageView.setLayoutParams(progressBarImageViewLayoutParams);
		/*
		 * RelativeLayout.LayoutParams progressRedBarLayoutParams =
		 * (RelativeLayout.LayoutParams) progressRedBar.getLayoutParams();
		 * progressRedBarLayoutParams.width = (int)
		 * (screenWidth*(progressWidth-totalAccountViewMarginLeft*2)*progress);
		 * progressRedBar.setLayoutParams(progressRedBarLayoutParams);
		 */
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

		RelativeLayout.LayoutParams computeAccountLayoutParams = (RelativeLayout.LayoutParams) computeAccount
				.getLayoutParams();
		computeAccountLayoutParams.height = (int) (screenWidth
				* computeEditWidth * 5 / 16);
		computeAccount.setLayoutParams(computeAccountLayoutParams);
		computeAccount
				.setPadding(
						(int) (screenWidth * (computeMarginLeft - computeLineMarginLeft)),
						0, 0, 0);

		RelativeLayout.LayoutParams computeDayLayoutParams = (RelativeLayout.LayoutParams) computeDay
				.getLayoutParams();
		computeDayLayoutParams.height = (int) (screenWidth * computeEditWidth
				* 5 / 16);
		computeDay.setLayoutParams(computeDayLayoutParams);

		// 计算收益天数输入布局
		RelativeLayout.LayoutParams computeDayViewLayoutParams = (RelativeLayout.LayoutParams) computeDayView
				.getLayoutParams();
		computeDayViewLayoutParams.leftMargin = (int) (screenWidth * computeLineMarginLeft);
		computeDayViewLayoutParams.topMargin = (int) (screenHeight
				* aprLabelMarginTop / 2);
		computeDayView.setLayoutParams(computeDayViewLayoutParams);
		// 计算收益按钮布局
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
		computeAccount.setOnClickListener(scroolPage);
		computeAccount.addTextChangedListener(accountTextWatcher);
		investBtn.setOnClickListener(toInvest);

		producDescView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProductDetailActivity.this,
						WebViewActivity.class);
				intent.putExtra("url",
						ProductDetailActivity.this.getProductDetailH5Url
								+ "?id=" + productInfo.getProductId());
				intent.putExtra("title", productInfo.getName());
				startActivity(intent);
			}
		});

		riskMeasuresView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProductDetailActivity.this,
						WebViewActivity.class);
				intent.putExtra("url", ProductDetailActivity.this.getRiskH5Url);
				intent.putExtra("title", "风险措施");
				ProductDetailActivity.this.startActivity(intent);
			}
		});
		investNumView.setOnClickListener(toTenderConsTruction);
	}

	private OnClickListener toTenderConsTruction = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ProductDetailActivity.this,
					TenderConstructionActivity.class);
			intent.putExtra("type", "product");
			intent.putExtra("productId", productId);
			ProductDetailActivity.this.startActivity(intent);
		}
	};

	private OnClickListener toInvest = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if ("1".equals(productInfo.getIsNovice())
					&& KDLCApplication.app.hasLogin()
					&& KDLCApplication.app.sessionEqual("is_novice", "0")) {
				dialog = KdlcDialog.showBackDialog(ProductDetailActivity.this,
						"您已经不是新手了，无法投资新手专属项目，请选择投资其他项目哦");
			} else {
				Intent intent = new Intent(ProductDetailActivity.this,
						InvestAccountActivity.class);
				Bundle bundle = new Bundle();
				ProductBaseInfo productBaseInfo = new ProductBaseInfo();
				productBaseInfo.getFromProduct(productInfo);
				bundle.putParcelable(G.PRODUCT_INFO_SER_KEY, productBaseInfo);
				intent.putExtras(bundle);
				startActivityNeedLogin(intent);
			}
		}
	};

	private OnClickListener scroolPage = new OnClickListener() {
		@SuppressLint("NewApi")
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			lastY = scrollView.getY();
			final int scrollY = (int) (screenHeight * computeMarginTop)
					+ (int) (screenHeight * progressMarginTop)
					+ baseInfoView.getMeasuredHeight()
					+ progressView.getMeasuredHeight();
			mHandler.post(new Runnable() {
				public void run() {
					if (scrollView == null) {
						return;
					}
					scrollView.scrollTo(0, scrollY);
				}
			});
		}
	};

	private void computeProfites() {
		String account = computeAccount.getText().toString();
		if (Tool.isBlank(account) || account.equals("0")) {
			profitsText.setText("0.00");
			bankProfitsAccount.setText("银行活期利息0.00元，是银行利息的");
			bankProfitsAccountTimes.setText("0倍");
		} else {
			int accountVal = Integer.parseInt(account);
			int daysVal = Integer.parseInt(productInfo.getPeriod());
			double aprVal = Double.parseDouble(apr.getText().toString());
			double bankApr = Double.parseDouble(productInfo.getBankApr());
			double profitsVal = productInfo.getIsDay().equals("0") ? accountVal
					* aprVal * daysVal / 12 / 100 : accountVal * aprVal
					* daysVal / 365 / 100;
			double bankProfitsVal = productInfo.getIsDay().equals("0") ? accountVal
					* bankApr * daysVal / 12 / 100
					: accountVal * bankApr * daysVal / 365 / 100;
			profitsText.setText(Tool.toFFDouble(profitsVal));
			bankProfitsAccount.setText("银行活期利息"
					+ Tool.toFFDouble(bankProfitsVal) + "元，是银行利息的");
			bankProfitsAccountTimes.setText(""
					+ Tool.toFFDouble(profitsVal / bankProfitsVal) + "倍");
		}
	}

	private void initContents() {
		if (productId != 0) {
			productInfo = (ProductDetailInfo) KdlcDB.findOneByWhere(
					ProductDetailInfo.class, "productId=" + productId);
			if (productInfo != null) {
				Log.d(LOG_TAG, "info:"+productInfo.getName());
				setContents(false);
			} else {
				Log.d(LOG_TAG, "no info");
			}
			mHandler.postDelayed(new Runnable() {
				public void run() {
					mPullRefreshScrollView.setLoadRefreshing();
				}
			}, 500);
		} else {
			dialog = KdlcDialog.showBackDialog(ProductDetailActivity.this, "获取项目信息失败，请稍后再试");
		}
	}

	private void setContents(boolean showProgress) {
		if (productInfo != null) {
			detailView.setVisibility(View.VISIBLE);
			investBtnView.setVisibility(View.VISIBLE);
			progress = Double.parseDouble(productInfo.getSuccessMoney())
					/ Double.parseDouble(productInfo.getTotalMoney());
			apr.setText(productInfo.getApr());
			time.setText(productInfo.getPeriod()
					+ (productInfo.getIsDay().equals("1") ? "天" : "个月"));
			accountTip.setText(Tool.toIntAccount(productInfo
					.getMinInvestMoney()) + "元起购");
			profitsTip.setText(productInfo.getInterestDate());
			safeTip.setText(warrantWord);
			totalAccount.setText(Tool.toDivAccount(productInfo.getTotalMoney())
					+ "元");
			Long leftMoney = Long.parseLong(productInfo.getTotalMoney())
					- Long.parseLong(productInfo.getSuccessMoney());
			leftAccount.setText(Tool.toDivAccount("" + leftMoney) + "元");
			progressText.setText(Tool.toFFDouble(progress * 100) + "%");
			// computeAccount.setText(R.string.productdetail_compute_account);
			computeDay.setText(productInfo.getIsDay().equals("1") ? productInfo
					.getPeriod() + "天" : productInfo.getPeriod() + "个月");
			productType.setText(productInfo.getProductType());
			productDesc.setText(productInfo.getSummary());
			investNum.setText(productInfo.getSuccessNum() + "人");
			fundsTrustTip.setText(productInfo.getRiskControlManaged());
			tradeGuarTip.setText(productInfo.getRiskControlWarrant());
			fundsSpotTip.setText(productInfo.getRiskControlRepay());
			interestDayTip.setText(productInfo.getInterestDate());
			repayDayTip.setText(productInfo.getRepayDate());
			repayInstructionTip.setText(productInfo.getRepaymentMark());
			maxProgress = (int) Math.ceil(progress * 100);
			progressRedBar.setProgress(maxProgress);
			if (productInfo.getStatus()
					.equals(G.PRODUCT_STATUS.STATUS_REPAYING)) {
				showInvalidStatusView();
				statusIcon.setBackgroundResource(R.drawable.badge_process);
			} else if (productInfo.getStatus().equals(
					G.PRODUCT_STATUS.STATUS_REPAYED)) {
				showInvalidStatusView();
				statusIcon.setBackgroundResource(R.drawable.badge_done);
			} else if (productInfo.getStatus().equals(G.PRODUCT_STATUS.STATUS_FULL)
					|| productInfo.getTotalMoney().equals(productInfo.getSuccessMoney())) {
				finishImageView.setVisibility(View.VISIBLE);
				computeProfitsView.setVisibility(View.INVISIBLE);
				resetProductDetailView(R.id.progressView);
				investBtn.setBackgroundResource(R.drawable.btn_grey_background);
				investBtn.setOnClickListener(null);
			} else if (showProgress) {
				progressRedBar.setAnimProgress(maxProgress);
			}
			computeProfites();
		}
	}

	private void showInvalidStatusView() {
		repayStatusView.setVisibility(View.VISIBLE);
		tipView.setVisibility(View.GONE);
		safeView.setVisibility(View.GONE);
		progressView.setVisibility(View.INVISIBLE);
		computeProfitsView.setVisibility(View.INVISIBLE);
		resetProductDetailView(R.id.repayStatusView);
		investBtn.setBackgroundResource(R.drawable.btn_grey_background);
		investBtn.setOnClickListener(null);
		repayTotalAccount
				.setText(Tool.toDivAccount(productInfo.getTotalMoney()) + "元");
		repayDay.setText(Tool.unixTimeToDate(productInfo.getLastRepayDate(),
				"yyyy / MM / dd"));
	}

	private void resetProductDetailView(int viewId) {
		RelativeLayout.LayoutParams productDetailViewParams = (RelativeLayout.LayoutParams) productDetailView
				.getLayoutParams();
		productDetailViewParams.addRule(RelativeLayout.BELOW, viewId);
		productDetailView.setLayoutParams(productDetailViewParams);
	}

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

	private Listener<JSONObject> getProductInfoListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try {
				Log.d(LOG_TAG, "execute result:" + response.toString());
				if (response.getInt("code") == 0) {
					detailView.setVisibility(View.VISIBLE);
					JSONObject data = response.getJSONObject("project");
					if (productInfo != null) {
						KdlcDB.deleteByEntity(productInfo);
					}
					productInfo = new ProductDetailInfo();
					productInfo.setProductId(Integer.parseInt(data
							.getString("id")));
					productInfo.setName(data.getString("name"));
					productInfo.setType(data.getString("type"));
					productInfo.setProductType(data.getString("product_type"));
					productInfo.setStatus(data.getString("status"));
					productInfo.setPeriod(data.getString("period"));
					productInfo.setIsDay(data.getString("is_day"));
					productInfo.setPublishAt(data.getString("publish_at"));
					productInfo.setEffectTime(data.getString("effect_time"));
					productInfo.setTotalMoney(data.getString("total_money"));
					productInfo
							.setSuccessMoney(data.getString("success_money"));
					productInfo.setSuccessNum(data.getString("success_number"));
					productInfo.setMinInvestMoney(data
							.getString("min_invest_money"));
					productInfo.setApr(data.getString("apr"));
					productInfo.setBankApr(data.getString("bank_apr"));
					productInfo.setSummary(data.getString("summary"));
					productInfo.setIsNovice(data.getString("is_novice"));
					productInfo.setRepaymentMark(data
							.getString("repayment_remark"));
					productInfo
							.setInterestDate(data.getString("interest_date"));
					productInfo.setRepayDate(data.getString("repay_date"));
					productInfo.setRiskControlManaged(data
							.getString("risk_control_managed"));
					productInfo.setRiskControlWarrant(data
							.getString("risk_control_warrant"));
					productInfo.setRiskControlRepay(data
							.getString("risk_control_repay"));
					productInfo.setLastRepayDate(data
							.getString("last_repay_date"));
					KDLCApplication.app.kdDb.save(productInfo);
					setContents(true);
				} else {
					// TODO:加载失败，请重试
					if (productInfo == null) {
						showErrReq();
					}
					dialog =KdlcDialog.showInformDialog(ProductDetailActivity.this,
							response.getString("message"));

				}
			} catch (Exception e) {
				e.printStackTrace();
				if(productInfo == null) {
					showErrReq();
				} else {
					KdlcDialog.showBottomToast("");
				}
			}
			mPullRefreshScrollView.onRefreshComplete();
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

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
