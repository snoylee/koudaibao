package com.kdkj.koudailicai.view.selfcenter.accountremain;

import java.text.SimpleDateFormat;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.ui.TitleView;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
public class WithDrawSuccessActivity extends BaseActivity{
	private TitleView title; 
	private RelativeLayout rl_success,rl_success_number,rl_success_return;
	private ImageView view;
	private TextView tv_success_money,tv_success_money_number,tv_success_time,tv_success_time_number,tv_success_poundage,tv_success_poundage_number;
	private TextView tv_success_about,tv_success_about_prompt,tv_success_return;
	//size
	private int screenHeight;
	private int screenWidth;
	//
	private double rl_successMarginTop = 0.026;
	private double rl_successHeight = 0.184;
	private double viewHeight = 0.120;
	private double viewWidth = 0.203;
	private double rl_success_numberMarginLeft = 0.068;
	private double viewMarginLeft = 0.043;
	private double tv_success_aboutMarginTop = 0.022;
	private double tv_success_about_promptMarginTop = 0.015;
	private double tv_success_returnMarginTop = 0.047;
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_success);
		findViews();
		initTitle();
		initSize();
		content();
		setListener();
	}
	 
	 private void findViews() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		rl_success = (RelativeLayout) findViewById(R.id.rl_success);
		rl_success_number = (RelativeLayout) findViewById(R.id.rl_success_number);
		rl_success_return = (RelativeLayout) findViewById(R.id.rl_success_return);
		view = (ImageView) findViewById(R.id.view);
		tv_success_about = (TextView) findViewById(R.id.tv_success_about);
		tv_success_about_prompt = (TextView) findViewById(R.id.tv_success_about_prompt);
		tv_success_return = (TextView) findViewById(R.id.tv_success_return);
		tv_success_money_number=(TextView)findViewById(R.id.tv_success_money_number);
		tv_success_time_number=(TextView)findViewById(R.id.tv_success_time_number);
		tv_success_poundage = (TextView) findViewById(R.id.tv_success_poundage);
		tv_success_poundage_number = (TextView) findViewById(R.id.tv_success_poundage_number);
	}
	 private void content() {
		 tv_success_money_number.setText(Tool.toDeciDouble(getIntent().getStringExtra("money"))+"元");
		 long currentTime = System.currentTimeMillis();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 Date date = new Date(currentTime);
		 tv_success_time_number.setText(formatter.format(date));
		 tv_success_poundage_number.setText(Tool.toDeciDouble(getIntent().getStringExtra("poundage"))+"元");
		 tv_success_about_prompt.setText(getIntent().getStringExtra("message"));
		}
	 private void initTitle() {
		// TODO Auto-generated method stub
		title = (TitleView) findViewById(R.id.title);
		title.setTitle("提现成功");
		title.showLeftButton(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WithDrawSuccessActivity.this.finish();
			}
		});
		title.setLeftImageButton(R.drawable.back);
		title.setLeftTextButton("返回");
	}
	 private void setListener() {
		// TODO Auto-generated method stub
		 tv_success_return.setOnClickListener(successReturn);
		 rl_success_return.setOnClickListener(returnrollout);
	
	}
	 private OnClickListener returnrollout = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		   Intent intent = new Intent(G.WITH_DRAW_MESSAGE);
		   sendBroadcast(intent);
		   WithDrawSuccessActivity.this.finish();
			}
		};
	 private OnClickListener successReturn = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 Intent intent = new Intent(G.WITH_DRAW_MESSAGE);
			sendBroadcast(intent);
			WithDrawSuccessActivity.this.finish();
		}
	};
	 private void initSize() {
		// TODO Auto-generated method stub
		screenHeight = Integer.parseInt(KDLCApplication.app.getSession().get("screenHeight"));
		screenWidth = Integer.parseInt(KDLCApplication.app.getSession().get("screenWidth"));
		//
		RelativeLayout.LayoutParams rl_successLayoutParams = (RelativeLayout.LayoutParams) rl_success.getLayoutParams();
		rl_successLayoutParams.topMargin = (int) (screenHeight*rl_successMarginTop);
		rl_successLayoutParams.height = (int) (screenHeight*rl_successHeight);
		rl_success.setLayoutParams(rl_successLayoutParams);
		//
		RelativeLayout.LayoutParams viewLayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		viewLayoutParams.height = (int) (screenHeight*viewHeight);
		viewLayoutParams.width = (int) (screenWidth*viewWidth);
		view.setLayoutParams(viewLayoutParams);
		//
		RelativeLayout.LayoutParams rl_success_numberLayoutParams = (RelativeLayout.LayoutParams) rl_success_number.getLayoutParams();
		rl_success_numberLayoutParams.leftMargin = (int) (screenWidth*rl_success_numberMarginLeft);
		rl_success_number.setLayoutParams(rl_success_numberLayoutParams);
		//
		RelativeLayout.LayoutParams tv_success_aboutLayoutParams = (RelativeLayout.LayoutParams) tv_success_about.getLayoutParams();
		tv_success_aboutLayoutParams.topMargin = (int) (screenHeight*tv_success_aboutMarginTop);
		tv_success_about.setLayoutParams(tv_success_aboutLayoutParams);
		//
		RelativeLayout.LayoutParams tv_success_about_promptLayoutParams = (RelativeLayout.LayoutParams) tv_success_about_prompt.getLayoutParams();
		tv_success_about_promptLayoutParams.topMargin = (int) (screenHeight*tv_success_about_promptMarginTop);
		tv_success_about_prompt.setLayoutParams(tv_success_about_promptLayoutParams);
		//
		RelativeLayout.LayoutParams tv_success_returnLayoutParams = (RelativeLayout.LayoutParams) tv_success_return.getLayoutParams();
		tv_success_returnLayoutParams.topMargin = (int) (screenHeight*tv_success_returnMarginTop);
		tv_success_return.setLayoutParams(tv_success_returnLayoutParams);
	}
}
