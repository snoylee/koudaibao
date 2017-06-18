package com.kdkj.koudailicai.view;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.GsonHelper;
import com.kdkj.koudailicai.domain.HomeProductInfo;
import com.kdkj.koudailicai.domain.UpdateInfo;
import com.kdkj.koudailicai.lib.AlarmReceiver;
import com.kdkj.koudailicai.lib.http.HttpRequestGet;
import com.kdkj.koudailicai.lib.http.RequestManager;
import com.kdkj.koudailicai.util.AppManager;
import com.kdkj.koudailicai.util.KdlcDialog;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.fragment.FragmentIndicator;
import com.kdkj.koudailicai.view.fragment.FragmentIndicator.OnIndicateListener;
import com.kdkj.koudailicai.view.fragment.HomeFragment;
import com.kdkj.koudailicai.view.fragment.HomeFragment.HomeFragmentInterface;
import com.kdkj.koudailicai.view.login.LoginAlreadyActivity;
import com.kdkj.koudailicai.view.register.RegisterPhoneActivity;
import com.kdkj.koudailicai.view.selfcenter.Gesture.GestureEditActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements HomeFragmentInterface{
	private String LOG_TAG = MainActivity.class.getName();
	public  static Fragment[] mFragments;
	private FragmentIndicator indicator;
	private Intent afterLoginIntent;
	PopupWindow popupWindow = null;
	private String getRiskH5Url;
	private int toFragment;
	private String appUrl;
	private AlertDialog dialog;
	public Handler handler=new Handler();
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		AppManager.getAppManager().addActivity(MainActivity.this);
		findViews();
		parseUrl();
		setFragmentIndicator(0);
		registerBoradcastReceiver();
		//检查更新
		if (!KDLCApplication.updateChecked) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					sendHttpGet(appUrl, appUpListener);// 检查更新
				}
			}, 1200);
		}
		//禁止默认的统计方法，采用activity和fragment分开统计的方式
	    MobclickAgent.openActivityDurationTrack(false);
	}
	
	/**
	 * 初始化fragment
	 */
	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new Fragment[4];
		mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_home);
		mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_product_list);
		mFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_selfcenter);
		mFragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_more);
		getSupportFragmentManager().beginTransaction()
								   .hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3])
								   .show(mFragments[whichIsDefault]).commit();
		FragmentIndicator mIndicator = (FragmentIndicator) findViewById(R.id.indicator);
		//mIndicator.setNotify();显示通知
		mIndicator.setOnIndicateListener(new OnIndicateListener() {
			@Override
			public void onIndicate(View v, int which) {
				FragmentIndicator.setIndicator(which);
				getSupportFragmentManager().beginTransaction()
				.hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3])
				.show(mFragments[which]).commit();
				mFragments[which].onStart();
			}
			
			@Override
			public void goLogin() {
				Intent intent = new Intent();				
				intent.setClass(MainActivity.this, LoginAlreadyActivity.class);
				intent.putExtra("toMain", "1");
				MainActivity.this.startActivity(intent);
			}
			
			@Override
			public void goNewer() {
				showFragmentById(G.FRAGMENT_TAG.CENTER);
				View contentView = getLayoutInflater().inflate(R.layout.activity_prompt, null);
				DisplayMetrics dm = new DisplayMetrics();
		        getWindowManager().getDefaultDisplay().getMetrics(dm);
				int popHeight =  dm.heightPixels-(int)(Integer.parseInt(KDLCApplication.app.getSessionVal("screenHeight"))*0.09);
				if(Tool.isMX()) {
					popHeight = popHeight - 145;
				}
				popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, popHeight, false);
				popupWindow.setContentView(contentView);
				View parent = MainActivity.this.getWindow().getDecorView();
				TextView goBtn = (TextView) contentView.findViewById(R.id.tv_prompt_register);
				goBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivity.this, RegisterPhoneActivity.class);
						startActivity(intent);
					}
				});
				TextView goRisk = (TextView) contentView.findViewById(R.id.tv_lookng);
				goRisk.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
						intent.putExtra("url", MainActivity.this.getRiskH5Url);
						intent.putExtra("title", "风险措施");
						MainActivity.this.startActivity(intent);
					}
				});		
				popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
				// 加上下面两行可以用back键关闭popupwindow，否则必须调用dismiss();
		        /*ColorDrawable dw = new ColorDrawable(-00000);
		        popupWindow.setBackgroundDrawable(dw);
		        popupWindow.update();*/
			}
			
			@Override 
			public void closePop() {
				if(popupWindow != null && popupWindow.isShowing())
					popupWindow.dismiss();
			}
		});
	}

	private void findViews() {
		indicator = (FragmentIndicator) this.findViewById(R.id.indicator);
	}
	
	private void parseUrl() {
		 if (KDLCApplication.app.isGlobalConfCompleted()) {
			 getRiskH5Url = KDLCApplication.app.getActionUrl(G.GCK_API_GET_PAGE_FXBZJ);
			 appUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_APP_UPGRADE);
		 } else {
			 getRiskH5Url = G.URL_GET_PAGE_FXBZJ;
			 appUrl = G.URL_GET_APP_UPGRADE;
		 }
	}
	
	public void startActivityNeedLogin(Intent intent) {
		this.afterLoginIntent = intent;
		if (KDLCApplication.app.hasLogin()) {
			this.startActivity(this.afterLoginIntent);
		} else {
			Intent loginIntent = new Intent();
			loginIntent.setClass(this, LoginAlreadyActivity.class);
			this.startActivityForResult(loginIntent, G.REQ_CODE_ACTIVITY_LOGIN);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == G.REQ_CODE_ACTIVITY_LOGIN) {
			if(resultCode == G.RET_CODE_LOGIN_SUCCESS)
				this.startActivity(this.afterLoginIntent);
		} else if(requestCode == G.REQ_CODE_FRAGMENT_LOGIN) {
			if(resultCode == G.RET_CODE_LOGIN_SUCCESS) {
				Log.d(LOG_TAG, "to self");
				FragmentIndicator.setIndicator(2);
				getSupportFragmentManager().beginTransaction()
				.hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3])
				.show(mFragments[2]).commitAllowingStateLoss();
				KDLCApplication.app.getSession().set("selfCenterAutoRefreshClick", "1");
				mFragments[2].onStart();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
			//sso回调设置
			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
		    if(ssoHandler != null){
		       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		    }
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		Log.d(LOG_TAG, "onResume");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		toFragment = getIntent().getIntExtra(G.TO_FRAGMENT_KEY, 5);
		Log.d(LOG_TAG, "onStart:"+toFragment);
		if(toFragment != 5) {
			Log.d(LOG_TAG, "show other");
			showFragmentById(toFragment);
			getIntent().removeExtra(G.TO_FRAGMENT_KEY);
		} else {
			Log.d(LOG_TAG, "noyr");
		}
	}
	
	private void showFragmentById(int which) {
		if(which == G.FRAGMENT_TAG.CENTER) {
			KDLCApplication.app.getSession().set("selfCenterAutoRefreshClick", "1");
			KDLCApplication.app.setSessionVal("selfCenterAutoRefresh", "1");
		}
		FragmentIndicator.setIndicator(which);
		getSupportFragmentManager().beginTransaction()
		.hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3])
		.show(mFragments[which]).commit();
		mFragments[which].onStart();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		Log.d(LOG_TAG, "onPause");
	}

	@Override
	public void showBottomMenu(boolean show) {
		// TODO Auto-generated method stub
		indicator.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if(action.equals(G.MAIN_SHOW_HOME)){  
            	FragmentIndicator.setIndicator(0);
				getSupportFragmentManager().beginTransaction()
				.hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3])
				.show(mFragments[0]).commitAllowingStateLoss();
				mFragments[0].onStart();
            }  
        }  
          
	};  
      
	public void registerBoradcastReceiver(){  
	    IntentFilter myIntentFilter = new IntentFilter();  
	    myIntentFilter.addAction(G.MAIN_SHOW_HOME);  
	    //注册广播        
	    registerReceiver(mBroadcastReceiver, myIntentFilter);  
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
	
	
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	super.onKeyDown(keyCode, event);
        if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(isExit==false) {
			    isExit=true;
			    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			    mHandler.sendEmptyMessageDelayed(1, 2000);
			}else {
				//停止闹钟
				Intent intent = new Intent(this,AlarmReceiver.class);  
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);  
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);  
                alarmManager.cancel(pendingIntent);  
			    MainActivity.this.finish();
			    AppManager.getAppManager().AppExit(MainActivity.this);
			}
        }
        return false;
    }
    
    private static boolean isExit=false;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					isExit=false;
					break;
				default:
					break;
			}
		};
	};
	
	private void sendHttpGet(String url, Listener<JSONObject> responseListener) {
		url = Tool.getUrl(url);
		HttpRequestGet httpRequest = new HttpRequestGet(url, responseListener, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				checkGesture();
			}
		});
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	private void appUpdate(final boolean force) {
		LogUtil.info("normal to update");
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateOnlyWifi(false);// 非wifi也可以更新
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				LogUtil.info(updateStatus + "");
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(MainActivity.this, updateInfo);
					if(force && UmengUpdateAgent.isIgnore(MainActivity.this, updateInfo)) { // 有更新且被忽略
						LogUtil.info("将版本设置为未忽略，并重新提示下载");
						UmengUpdateAgent.forceUpdate(MainActivity.this);
						return;
					}
					break;
				case UpdateStatus.No: // has no update
					break;
				default:
					break;
				}
			}
		});
		
		if(force) {
			UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
				@Override
				public void onClick(int arg0) {
					// TODO Auto-generated method stub
					switch (arg0) {
					case UpdateStatus.Update:
						// Toast.makeText(getActivity(), "User chooses update." ,
						// Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.Ignore:
					case UpdateStatus.NotNow:
						// 退出程序
						KdlcDialog.showBottomToast("本次更新包含重要信息，请及时更新！");
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								LogUtil.info("本次更新包含重要信息，请及时更新");
								System.exit(0);
							}
						}, 1000);
						break;
					}
					UmengUpdateAgent.setDialogListener(null);
				}
			});
		}
		UmengUpdateAgent.update(MainActivity.this);
	}
	
	private Listener<JSONObject> appUpListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			UpdateInfo updateInfo = GsonHelper.fromJson(response,
					UpdateInfo.class);
			int retCode = updateInfo.getCode();
			LogUtil.info(updateInfo.toJson());
			KDLCApplication.updateChecked = true;
			if (retCode == 0) {
				if (updateInfo.getHas_upgrade() == UpdateInfo.HAS_UPDATE) {
					if (updateInfo.getIs_force_upgrade() == UpdateInfo.FORCE_TO_UPDATE) {
						// 有强制更新
						appUpdate(true);
					} else
						appUpdate(false);
				} else 
					checkGesture();
			} else {
				checkGesture();
				LogUtil.info("出错");
			}
		}
	};

	private void checkGesture() {
		if(Tool.hasCacheData(HomeProductInfo.class) &&
		   KDLCApplication.app.sessionEqual("gestureInform", "1") && 
		   KDLCApplication.app.hasLogin() && 
		  (KDLCApplication.app.sessionEqual("gesture","0") || Tool.isBlank(KDLCApplication.app.getSessionVal("gesture")))) {
			KDLCApplication.app.setSessionVal("gestureInform", "0");
			dialog = KdlcDialog.showConfirmDialog(MainActivity.this, true, new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, GestureEditActivity.class);
					startActivity(intent);
					dialog.cancel();
				}
			}, "您还未设置手势密码，为了您的账户安全，请设置手势密码");
		}
	}
	
    @Override  
    public Resources getResources() {  
        Resources res = super.getResources();    
        Configuration config=new Configuration();    
        config.setToDefaults();    
        res.updateConfiguration(config,res.getDisplayMetrics() );  
        return res;  
    }  
}
