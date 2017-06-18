package com.kdkj.koudailicai.view.selfcenter.Gesture;

import java.util.HashMap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GesturePoint;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.selfcenter.AccountCenterActivity;
import com.kdkj.koudailicai.view.selfcenter.Gesture.GestureDrawline.GestureCallBack;

/**
 * 
 * 手势密码设置界面
 * 
 */
public class GestureEditActivity extends Activity {
	/** 手机号码 */
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	/** 首次提示绘制手势密码，可以选择跳过 */
	public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
	private TextView mTextTitle;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextReset;
	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;
	private TextView textRight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_edit);
		setUpViews();
		setUpListeners();
	}

	private void setUpViews() {
		mTextReset = (TextView) findViewById(R.id.text_reset);
		textRight=(TextView)findViewById(R.id.text_right);
		textRight.setVisibility(View.GONE);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, false, "",
				new GestureCallBack() {
					@Override
					public void onGestureCodeInput(String inputCode) {
						if (!isInputPassValidate(inputCode)) {
							KdlcDialog.showToast("最少链接4个点, 请重新输入");
							mGestureContentView.clearDrawlineState(0L);
							return;
						}
						if (mIsFirstInput) {
							mFirstPassword = inputCode;
							mGestureContentView.clearDrawlineState(0L);
							mTextTip.setText("请再次绘制锁屏密码");
							textRight.setVisibility(View.VISIBLE);
							mTextReset.setVisibility(View.GONE);
							textRight.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mIsFirstInput = true;
									mTextTip.setText(getString(R.string.set_gesture_pattern));
									textRight.setVisibility(View.GONE);
									mTextReset.setVisibility(View.VISIBLE);
								}
							});
						} else {
							if (inputCode.equals(mFirstPassword)) {
								KdlcDialog.showBottomToast("手势密码设置成功");
								HashMap<String, String> vals = new HashMap<String, String>();
								vals.put("gesture", GestureDrawline.passWordSb
										+ "");
								KDLCApplication.app.getSession().set(vals);
								mGestureContentView.clearDrawlineState(0L);
								GestureEditActivity.this.finish();
								// Intent intent = new Intent(
								// GestureEditActivity.this,
								// AccountCenterActivity.class);
								// startActivity(intent);

							} else {
								mTextTip.setText("与上一次绘制不一致，请重新绘制");
								// 左右移动动画
								Animation shakeAnimation = AnimationUtils
										.loadAnimation(
												GestureEditActivity.this,
												R.anim.shake);
								mTextTip.startAnimation(shakeAnimation);
								// 保持绘制的线，1.5秒后清除
								mGestureContentView.clearDrawlineState(1300L);
							}
						}
						mIsFirstInput = false;
					}

					@Override
					public void checkedSuccess() {

					}

					@Override
					public void checkedFail() {

					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
	}

	private void setUpListeners() {

		mTextReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GestureEditActivity.this.finish();	
			}
		});
		
	}

	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Intent intent = new Intent();
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			GestureEditActivity.this.finish();
		}
		return false;
	}




}
