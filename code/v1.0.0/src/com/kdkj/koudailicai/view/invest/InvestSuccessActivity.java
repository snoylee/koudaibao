package com.kdkj.koudailicai.view.invest;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.InvestResultInfo;
import com.kdkj.koudailicai.domain.ProductBaseInfo;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.login.LoginActivity;

public class InvestSuccessActivity extends BaseActivity {
	private TitleView buysuccesstitle;
	private TextView productName;
	private TextView buyTime;
	private TextView buyApr;
	private TextView investAccount;
	private TextView startTime;
	private TextView arivalTime;
	private TextView startDesc;
	private TextView arivalDesc;
	private TextView buttonHome;
	private InvestResultInfo retInfo;
	private int screenHeight;
	private int screenWidth;
    private RelativeLayout buySuccess;
    private RelativeLayout startFee;
    private RelativeLayout ariveFee;
    private RelativeLayout oneLayout,twoLayout,threeLayout,bottomLayout,topLayout;
    private TextView buyIn;
    private View divider3,divider5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_invest_success);
		findViews();
		initTitle();
		getContents();
		setlisteners();
		initSize();
	}

	private void findViews() {
		buysuccesstitle = (TitleView) findViewById(R.id.buysuccesstitle);
		productName = (TextView) findViewById(R.id.productName);
		buyTime = (TextView) findViewById(R.id.buyTime);
		buyApr = (TextView) findViewById(R.id.buyApr);
		investAccount = (TextView) findViewById(R.id.investAccount);
		startTime = (TextView) findViewById(R.id.startTime);
		arivalTime = (TextView) findViewById(R.id.arivalTime);
		startDesc = (TextView) findViewById(R.id.startDesc);
		arivalDesc = (TextView) findViewById(R.id.arivalDesc);
		buttonHome = (TextView) findViewById(R.id.buttonHome);
		buySuccess=(RelativeLayout)findViewById(R.id.buysuccess);
		startFee=(RelativeLayout)findViewById(R.id.startfee);
		ariveFee=(RelativeLayout)findViewById(R.id.arivefee);
		oneLayout=(RelativeLayout)findViewById(R.id.onelayout);
		twoLayout=(RelativeLayout)findViewById(R.id.twolayout);
		threeLayout=(RelativeLayout)findViewById(R.id.threelayout);
		bottomLayout=(RelativeLayout)findViewById(R.id.bottomlayout);
		topLayout=(RelativeLayout)findViewById(R.id.toplayout);
		divider3=(View)findViewById(R.id.divider3);
		divider5=(View)findViewById(R.id.divider5);
	}
	private void initSize() {
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		RelativeLayout.LayoutParams totalProfitsLayoutParams = (RelativeLayout.LayoutParams) oneLayout.getLayoutParams();
		totalProfitsLayoutParams.height = (int) (screenHeight * 0.24);
		oneLayout.setLayoutParams(totalProfitsLayoutParams);
		RelativeLayout.LayoutParams buySuccessLayoutParams = (RelativeLayout.LayoutParams) buySuccess.getLayoutParams();
		buySuccessLayoutParams.height = (int) (screenHeight * 0.22);
		buySuccess.setLayoutParams(buySuccessLayoutParams);
		RelativeLayout.LayoutParams startFeeMarginTop = (RelativeLayout.LayoutParams) topLayout.getLayoutParams();
		startFeeMarginTop.topMargin = (int) (screenHeight *0.026);
		topLayout.setLayoutParams(startFeeMarginTop);
		RelativeLayout.LayoutParams bottomLayoutMarginbottom = (RelativeLayout.LayoutParams) bottomLayout.getLayoutParams();
		bottomLayoutMarginbottom.bottomMargin = (int) (screenHeight *0.026);
		bottomLayout.setLayoutParams(bottomLayoutMarginbottom);
		
		RelativeLayout.LayoutParams twoLayoutLayoutParams = (RelativeLayout.LayoutParams) twoLayout.getLayoutParams();
		twoLayoutLayoutParams.height = (int) (screenHeight * 0.13);
		twoLayout.setLayoutParams(twoLayoutLayoutParams);
		RelativeLayout.LayoutParams startFeelayout = (RelativeLayout.LayoutParams) startFee.getLayoutParams();
		startFeelayout.height = (int) (screenHeight * 0.13);
		startFee.setLayoutParams(startFeelayout);
		RelativeLayout.LayoutParams startTimeMarginTop = (RelativeLayout.LayoutParams) startTime.getLayoutParams();
		startTimeMarginTop.topMargin = (int) (screenHeight * 0.026);
		startTime.setLayoutParams(startTimeMarginTop);
		RelativeLayout.LayoutParams startDescMarginBottom = (RelativeLayout.LayoutParams) startDesc.getLayoutParams();
		startDescMarginBottom.bottomMargin = (int) (screenHeight * 0.026);
		startDesc.setLayoutParams(startDescMarginBottom);
		
		RelativeLayout.LayoutParams divider3Height = (RelativeLayout.LayoutParams) divider3.getLayoutParams();
		divider3Height.height = (int) (screenHeight * 0.13/2-KDLCApplication.app.dip2px(18));
		divider3.setLayoutParams(divider3Height);
		
		
		RelativeLayout.LayoutParams ariveFeeLayoutParams = (RelativeLayout.LayoutParams) threeLayout.getLayoutParams();
		ariveFeeLayoutParams.height = (int) (screenHeight * 0.15);
		threeLayout.setLayoutParams(ariveFeeLayoutParams);
		RelativeLayout.LayoutParams ariveFeeMarginTop = (RelativeLayout.LayoutParams) ariveFee.getLayoutParams();
		ariveFeeMarginTop.height = (int) (screenHeight * 0.13);
		ariveFee.setLayoutParams(ariveFeeMarginTop);
		RelativeLayout.LayoutParams arivalTimeMarginTop = (RelativeLayout.LayoutParams) arivalTime.getLayoutParams();
		arivalTimeMarginTop .topMargin = (int) (screenHeight * 0.026);
		arivalTime.setLayoutParams(arivalTimeMarginTop);
		RelativeLayout.LayoutParams arivalDescMarginTop = (RelativeLayout.LayoutParams) arivalDesc.getLayoutParams();
		arivalDescMarginTop.bottomMargin = (int) (screenHeight * 0.026);
		arivalDesc.setLayoutParams(arivalDescMarginTop);
		RelativeLayout.LayoutParams divider5Height = (RelativeLayout.LayoutParams) divider5.getLayoutParams();
		divider5Height.height = (int) (screenHeight * 0.15/2-KDLCApplication.app.dip2px(18));
		divider5.setLayoutParams(divider5Height);
		
		RelativeLayout.LayoutParams buttonHomeMarginTop = (RelativeLayout.LayoutParams) buttonHome.getLayoutParams();
		buttonHomeMarginTop.topMargin = (int) (screenHeight * 0.07);
		buttonHome.setLayoutParams(buttonHomeMarginTop);
		
	}
	private void getContents() {
		retInfo = (InvestResultInfo) this.getIntent().getParcelableExtra(
				G.INVEST_INFO_SER_KEY);
		if (retInfo != null) {
			productName.setText(retInfo.getProjectName());
			buyTime.setText(retInfo.getInvestDate());
			buyApr.setText(retInfo.getApr() + "%");
			investAccount.setText(Tool.toDeciDouble(retInfo.getInvestMoney()));
			startTime.setText(retInfo.getStartDate());
			arivalTime.setText(retInfo.getEndDate());
			startDesc.setText(retInfo.getStartDesc());
			arivalDesc.setText(retInfo.getEndDesc());
		}
		Map<String, String> vals = new HashMap<String, String>();
		vals.put("p2pListAutoRefresh", "1");
		vals.put("cessionListAutoRefresh", "1");
		vals.put("trustListAutoRefresh", "1");
		vals.put("selfCenterAutoRefresh", "1");
		KDLCApplication.app.getSession().set(vals);
	}

	private void setlisteners() {
		buttonHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(G.TO_FRAGMENT_KEY, G.FRAGMENT_TAG.CENTER);
				intent.setClass(InvestSuccessActivity.this, MainActivity.class);
				startActivity(intent);
				InvestSuccessActivity.this.finish();
			}
		});
	}

	private void initTitle() {
		buysuccesstitle.setTitle(R.string.buysuccess);
		buysuccesstitle.showLeftButton(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goSelfCenter();
			}
		});
		buysuccesstitle.setLeftImageButton(R.drawable.back);
		buysuccesstitle.setLeftTextButton("返回");
	}

	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goSelfCenter();
		}
		return false;
	}

	private void goSelfCenter() {
		Intent intent = new Intent(InvestSuccessActivity.this,
				MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(G.TO_FRAGMENT_KEY, G.FRAGMENT_TAG.CENTER);
		startActivity(intent);
		InvestSuccessActivity.this.finish();
	}
}
