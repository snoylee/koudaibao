package com.kdkj.koudailicai.view.selfcenter.holdasset;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
public class KdbRollOutSuccessActivity extends BaseActivity {
	private TitleView title; 
	private RelativeLayout rlTransfer,rlTransferNumber,rlTransferReturn;
	private ImageView view;
	private TextView tvTransferMoneyNumber,tvTransferTimeNumber;
	private TextView tvTransferAbout,tvTransferAboutPrompt,tvTransferReturn;
	//size
	private int screenHeight;
	private int screenWidth;
	//
	private double rlTransferMarginTop = 0.026;
	private double rlTransferHeight = 0.184;
	private double viewHeight = 0.120;
	private double viewWidth = 0.203;
	private double rlTransferNumberMarginLeft = 0.068;
	private double viewMarginLeft = 0.043;
	private double tvTransferAboutMarginTop = 0.022;
	private double tvTransferAboutPromptMarginTop = 0.015;
	private double tvTransferReturnMarginTop = 0.047;
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_transfer_success);
		findViews();
		initTitle();
		initSize();
		content();
		setListener();
	}
	 
	 private void findViews() {
		title = (TitleView) findViewById(R.id.title);
		rlTransfer = (RelativeLayout) findViewById(R.id.rl_transfer);
		rlTransferNumber = (RelativeLayout) findViewById(R.id.rl_transfer_number);
		rlTransferReturn = (RelativeLayout) findViewById(R.id.rl_transfer_return);
		view = (ImageView) findViewById(R.id.view);
		tvTransferAbout = (TextView) findViewById(R.id.tv_transfer_about);
		tvTransferAboutPrompt = (TextView) findViewById(R.id.tv_transfer_about_prompt);
		tvTransferReturn = (TextView) findViewById(R.id.tv_transfer_return);
		tvTransferMoneyNumber=(TextView)findViewById(R.id.tv_transfer_money_number);
		tvTransferTimeNumber=(TextView)findViewById(R.id.tv_transfer_time_number);
	}
	 private void content() {
		 Log.v("8798789797979", Tool.trimHeadZero(getIntent().getStringExtra("money"))+"");
		 tvTransferMoneyNumber.setText(getIntent().getStringExtra("money")+"元");
		 long currentTime = System.currentTimeMillis();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 Date date = new Date(currentTime);
		 tvTransferTimeNumber.setText(formatter.format(date));
		}
	 private void initTitle() {
		title = (TitleView) findViewById(R.id.title);
		title.setTitle("转出成功");
		title.showLeftButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				backToHoldAsset();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}
	 
	private void setListener() {
		 tvTransferReturn.setOnClickListener(transferReturn);
		 rlTransferReturn.setOnClickListener(returnrollout);
	
	}
	
	private OnClickListener returnrollout = new OnClickListener() {
			@Override
			public void onClick(View v) {
				backToHoldAsset();
			}
	};
	
	private OnClickListener transferReturn = new OnClickListener() {
		@Override
		public void onClick(View v) {
			backToHoldAsset();
		}
	};
	
	private void initSize() {
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		//
		RelativeLayout.LayoutParams rlTransferLayoutParams = (RelativeLayout.LayoutParams) rlTransfer.getLayoutParams();
		rlTransferLayoutParams.topMargin = (int) (screenHeight*rlTransferMarginTop);
		rlTransferLayoutParams.height = (int) (screenHeight*rlTransferHeight);
		rlTransfer.setLayoutParams(rlTransferLayoutParams);
		//
		RelativeLayout.LayoutParams viewLayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		viewLayoutParams.height = (int) (screenHeight*viewHeight);
		viewLayoutParams.width = (int) (screenWidth*viewWidth);
		view.setLayoutParams(viewLayoutParams);
		//
		RelativeLayout.LayoutParams rlTransferNumberLayoutParams = (RelativeLayout.LayoutParams) rlTransferNumber.getLayoutParams();
		rlTransferNumberLayoutParams.leftMargin = (int) (screenWidth*rlTransferNumberMarginLeft);
		rlTransferNumber.setLayoutParams(rlTransferNumberLayoutParams);
		//
		RelativeLayout.LayoutParams tvTransferAboutLayoutParams = (RelativeLayout.LayoutParams) tvTransferAbout.getLayoutParams();
		tvTransferAboutLayoutParams.topMargin = (int) (screenHeight*tvTransferAboutMarginTop);
		tvTransferAbout.setLayoutParams(tvTransferAboutLayoutParams);
		//
		RelativeLayout.LayoutParams tvTransferAboutPromptLayoutParams = (RelativeLayout.LayoutParams) tvTransferAboutPrompt.getLayoutParams();
		tvTransferAboutPromptLayoutParams.topMargin = (int) (screenHeight*tvTransferAboutPromptMarginTop);
		tvTransferAboutPrompt.setLayoutParams(tvTransferAboutPromptLayoutParams);
		//
		RelativeLayout.LayoutParams tvTransferReturnLayoutParams = (RelativeLayout.LayoutParams) tvTransferReturn.getLayoutParams();
		tvTransferReturnLayoutParams.topMargin = (int) (screenHeight*tvTransferReturnMarginTop);
		tvTransferReturn.setLayoutParams(tvTransferReturnLayoutParams);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToHoldAsset();
		}
		return false;
	}
	
	public void backToHoldAsset() {
		Intent intent = new Intent(G.HOLD_ASSET_REFRESH);
        sendBroadcast(intent);
		KdbRollOutSuccessActivity.this.finish();
	}
}
