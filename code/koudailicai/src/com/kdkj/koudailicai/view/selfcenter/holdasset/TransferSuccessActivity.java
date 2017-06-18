package com.kdkj.koudailicai.view.selfcenter.holdasset;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
public class TransferSuccessActivity extends BaseActivity {
	private TitleView title;
	private RelativeLayout rl_rollout, rl_rollout_number, rl_rollout_return;
	private TextView tv_rollout_project, tv_rollout_project_name,
					 tv_rollout_money, tv_rollout_money_number;
	private TextView tv_rollout_about, tv_rollout_about_prompt_one,
					 tv_rollout_about_prompt_two, tv_rollout_return;
	private ImageView view;
	// size
	private int screenHeight;
	private int screenWidth;
	//
	private double rl_rolloutMarginTop = 0.026;
	private double rl_rolloutHeight = 0.184;
	private double viewHeight = 0.120;
	private double viewWidth = 0.203;
	private double rl_rollout_numberMarginLeft = 0.068;
	private double viewMarginTop = 0.035;
	private double viewMarginLeft = 0.043;
	private double tv_rollout_aboutMarginTop = 0.022;
	private double tv_rollout_about_prompt_oneMarginTop = 0.015;
	private double tv_rollout_returnMarginTop = 0.047;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rollout_success);
		parseUrl();
		findView();
		initTitle();
		initSize();
		setListener();
		content();
	}

	private void parseUrl() {
		// TODO Auto-generated method stub

	}

	private void findView() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		rl_rollout = (RelativeLayout) findViewById(R.id.rl_rollout);
		tv_rollout_project_name = (TextView) findViewById(R.id.tv_rollout_project_name);
		rl_rollout_number = (RelativeLayout) findViewById(R.id.rl_rollout_number);
		tv_rollout_money_number = (TextView) findViewById(R.id.tv_rollout_money_number);
		rl_rollout_return = (RelativeLayout) findViewById(R.id.rl_rollout_return);
		tv_rollout_about = (TextView) findViewById(R.id.tv_rollout_about);
		tv_rollout_about_prompt_one = (TextView) findViewById(R.id.tv_rollout_about_prompt_one);
		tv_rollout_about_prompt_two = (TextView) findViewById(R.id.tv_rollout_about_prompt_two);
		tv_rollout_return = (TextView) findViewById(R.id.tv_rollout_return);
		view = (ImageView) findViewById(R.id.view);
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		title.setTitle("提交成功");
		title.showLeftButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backToHoldAsset();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}

	private void setListener() {
		// TODO Auto-generated method stub
		tv_rollout_return.setOnClickListener(returnrollout);
	}

	private void content() {
		tv_rollout_project_name.setText(getIntent().getStringExtra("transferName"));
		tv_rollout_money_number.setText(getIntent().getStringExtra("money")+"元");
	}

	private OnClickListener returnrollout = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			backToHoldAsset();
		}
	};

	private void initSize() {
		// TODO Auto-generated method stub
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get(
				"screenWidth"));
		//
		RelativeLayout.LayoutParams rl_rolloutLayoutParams = (RelativeLayout.LayoutParams) rl_rollout
				.getLayoutParams();
		rl_rolloutLayoutParams.topMargin = (int) (screenHeight * rl_rolloutMarginTop);
		rl_rolloutLayoutParams.height = (int) (screenHeight * rl_rolloutHeight);
		rl_rollout.setLayoutParams(rl_rolloutLayoutParams);
		//
		RelativeLayout.LayoutParams viewLayoutParams = (RelativeLayout.LayoutParams) view
				.getLayoutParams();
		viewLayoutParams.height = (int) (screenHeight * viewHeight);
		viewLayoutParams.width = (int) (screenWidth * viewWidth);
		view.setLayoutParams(viewLayoutParams);
		//
		RelativeLayout.LayoutParams rl_rollout_numberLayoutParams = (RelativeLayout.LayoutParams) rl_rollout_number
				.getLayoutParams();
		rl_rollout_numberLayoutParams.leftMargin = (int) (screenWidth * rl_rollout_numberMarginLeft);
		rl_rollout_number.setLayoutParams(rl_rollout_numberLayoutParams);
		//
		RelativeLayout.LayoutParams tv_rollout_aboutLayoutParams = (RelativeLayout.LayoutParams) tv_rollout_about
				.getLayoutParams();
		tv_rollout_aboutLayoutParams.topMargin = (int) (screenHeight * tv_rollout_aboutMarginTop);
		tv_rollout_about.setLayoutParams(tv_rollout_aboutLayoutParams);
		//
		RelativeLayout.LayoutParams tv_rollout_about_prompt_oneLayoutParams = (RelativeLayout.LayoutParams) tv_rollout_about_prompt_one
				.getLayoutParams();
		tv_rollout_about_prompt_oneLayoutParams.topMargin = (int) (screenHeight * tv_rollout_about_prompt_oneMarginTop);
		tv_rollout_about_prompt_one
				.setLayoutParams(tv_rollout_about_prompt_oneLayoutParams);
		//
		RelativeLayout.LayoutParams tv_rollout_returnLayoutParams = (RelativeLayout.LayoutParams) tv_rollout_return
				.getLayoutParams();
		tv_rollout_returnLayoutParams.topMargin = (int) (screenHeight * tv_rollout_returnMarginTop);
		tv_rollout_return.setLayoutParams(tv_rollout_returnLayoutParams);
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
        TransferSuccessActivity.this.finish();
	}
}
