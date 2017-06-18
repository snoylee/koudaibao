package com.kdkj.koudailicai.view.selfcenter.Gesture;

import java.util.HashMap;

import net.tsz.afinal.annotation.sqlite.OneToMany;

import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.BaseActivity;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.MainActivity;
import com.kdkj.koudailicai.view.invest.InvestSuccessActivity;
import com.kdkj.koudailicai.view.login.LoginActivity;
import com.kdkj.koudailicai.view.login.LoginAlreadyActivity;
import com.kdkj.koudailicai.view.selfcenter.AccountCenterActivity;
import com.kdkj.koudailicai.view.selfcenter.Gesture.GestureContentView;
import com.kdkj.koudailicai.view.selfcenter.Gesture.GestureDrawline.GestureCallBack;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 手势绘制/校验界面
 * 
 */
public class GestureVerifyActivity extends BaseActivity implements android.view.View.OnClickListener{
	/** 手机号码 */
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	private RelativeLayout mTopLayout;
	private ImageView mImgUserLogo;
	private TextView mTextPhoneNumber;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextForget;
	private TextView mTextOther;
	private String mParamPhoneNumber;
	private int mParamIntentCode;
	private int num = 5;
    private TextView mNextTip;
    private AlertDialog alterDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_verify);
		ObtainExtraData();
		setUpViews();
		setUpListeners();
	}

	private void ObtainExtraData() {
		mParamPhoneNumber = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
		mParamIntentCode = getIntent().getIntExtra(PARAM_INTENT_CODE, 0);
	}

	private void setUpViews() {
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mNextTip=(TextView)findViewById(R.id.text1_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
		mTextOther = (TextView) findViewById(R.id.text_other_account);
		mTextTip.setText(Tool.changeMobile(KDLCApplication.app.getSession()
				.get("username")));
		if (getIntent().getStringExtra("gestureCode") != null
				&& getIntent().getStringExtra("gestureCode")
						.equals("1"))
		{
			mTextForget.setText("取消");
		}else if (getIntent().getStringExtra("gestureFlag") != null
				&& getIntent().getStringExtra("gestureFlag")
				.equals("1")) {
			mTextForget.setText("取消");
		}else {
			mTextTip.setText(Tool.changeMobile(KDLCApplication.app.getSession().get("username"))+"，欢迎回来");
			mNextTip.setText("请输入锁屏密码");
			mTextForget.setText("切换用户");
		}
		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, true,
				KDLCApplication.app.getSession().get("gesture"),
				new GestureCallBack() {

					@Override
					public void onGestureCodeInput(String inputCode) {

					}

					@Override
					public void checkedSuccess() {
						mGestureContentView.clearDrawlineState(0L);
						Intent intent = new Intent();
						if (getIntent().getStringExtra("gestureCode") != null
								&& getIntent().getStringExtra("gestureCode")
										.equals("1")) {						
							intent.setClass(GestureVerifyActivity.this,
									GestureEditActivity.class);
							startActivity(intent);
							GestureVerifyActivity.this.finish();
						} else if (getIntent().getStringExtra("gestureFlag") != null
								&& getIntent().getStringExtra("gestureFlag")
										.equals("1")) {
//							intent.setClass(GestureVerifyActivity.this,
//									AccountCenterActivity.class);
							HashMap<String, String> vals = new HashMap<String, String>();
							vals.put("gesture", "0");
							KDLCApplication.app.getSession().set(vals);
//							startActivity(intent);
							GestureVerifyActivity.this.finish();
						} else {
							
							intent.setClass(GestureVerifyActivity.this,
									MainActivity.class);
							startActivity(intent);
							GestureVerifyActivity.this.finish();
						}

					}

					@Override
					public void checkedFail() {
						mGestureContentView.clearDrawlineState(1300L);
						mNextTip.setVisibility(View.VISIBLE);
						mNextTip.setText("密码错误，还可以输入" + --num + "次");
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils
								.loadAnimation(GestureVerifyActivity.this,
										R.anim.shake);
						mNextTip.startAnimation(shakeAnimation);
						if (num <= 0) {
							KdlcDialog.showBottomToast("手势密码输入次数太多请重新登录");
							KDLCApplication.app.logout();
							Intent intent = new Intent(
									GestureVerifyActivity.this,
									MainActivity.class);
							startActivity(intent);
							GestureVerifyActivity.this.finish();
						}
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
	}

	private void setUpListeners() {
		mTextForget.setOnClickListener(this);
		mTextOther.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_other_account:
			alterDialog=KdlcDialog.showConfirmDialog(GestureVerifyActivity.this, false, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alterDialog.cancel();
					KDLCApplication.app.logout();
					sendBroadcast(new Intent(G.MAIN_SHOW_HOME));
					Intent intent2 = new Intent(GestureVerifyActivity.this, LoginAlreadyActivity.class);
					intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent2.putExtra("gestureToMain", "1");
					intent2.putExtra("toMain", "1");		
					startActivity(intent2);	
					finish();
				}
			}, "忘记手势密码，需要重新登录");			
		
			break;
		case R.id.text_forget_gesture:
			if ("1".equals(getIntent().getStringExtra("gestureCode"))) {
				GestureVerifyActivity.this.finish();
			}else if ("1".equals(getIntent().getStringExtra("gestureFlag"))) {
				GestureVerifyActivity.this.finish();
			}else {
				alterDialog=KdlcDialog.showConfirmDialog(GestureVerifyActivity.this, false, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alterDialog.cancel();
						Intent intent = new Intent(GestureVerifyActivity.this,
								LoginActivity.class);
						KDLCApplication.app.logout();
						HashMap<String, String> vals = new HashMap<String, String>();
						vals.put("gesture", "0");
						KDLCApplication.app.getSession().set(vals);
						intent.putExtra("gestureToMain", "1");
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					}
				}, "确定用其他账号登录吗？");			
			
				
			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getIntent().getStringExtra("gestureCode") != null
					&& getIntent().getStringExtra("gestureCode").equals("1")) {
            
				GestureVerifyActivity.this.finish();
			} else if (getIntent().getStringExtra("gestureFlag") != null
					&& getIntent().getStringExtra("gestureFlag").equals("1")) {

				GestureVerifyActivity.this.finish();
			} else {
             
				GestureVerifyActivity.this.finish();
			}

		}
		return false;
	}

}
