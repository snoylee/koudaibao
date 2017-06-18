package com.kdkj.koudailicai.view;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.R;
import com.kdkj.koudailicai.domain.HomeProductInfo;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.http.PullToRefreshHttpErrorListener;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase;
import com.kdkj.koudailicai.lib.ui.pulltorefresh.PullToRefreshBase.Mode;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.selfcenter.Gesture.GestureVerifyActivity;
import com.kdkj.koudailicai.xgpush.XGRegisterOperate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class SplashActivity extends BaseActivity {

	private static final int CHECK_CONFIG = 1001;
	private static final int GOTO_NEXT = 1002;

	private static final long SPLASH_DELAY_MILLIS = 2000;
	private String LOG_TAG = "SplashActivity";
	private String getHomeInfoUrl;
	private String getDeviceUrl;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHECK_CONFIG:
				// checkConfig();
				break;
			case GOTO_NEXT:
				gotoNext();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
            finish();
            return;
         }
		setContentView(R.layout.activity_splash);
		Log.d(LOG_TAG, "Init Start");
		setSplashImage();
		initMessage();
		// 初始化自动刷新控制
		initAutoRefresh();
		// 加载全局配置数据
		KDLCApplication.app.loadGlobalConfigInBackground();
		parseUrl();
		uploadDeviceInfo();
		sendHttpGet(getHomeInfoUrl, getHomeInfoListener, new SplashHttpErrorListener());
		this.mHandler.sendEmptyMessageDelayed(GOTO_NEXT, SPLASH_DELAY_MILLIS);
		KDLCApplication.updateChecked=false;
		Log.d(LOG_TAG, "Init End");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	private void initMessage() {
		//消息推送注册
		XGRegisterOperate xgRegisterOperate = new XGRegisterOperate();
		xgRegisterOperate.register(getApplicationContext());
		//初始化消息 全部标记为已读
		KDLCApplication.app.initMessageCenter("0");
	}

	private void parseUrl() {
		if (KDLCApplication.app.isGlobalConfCompleted()) {
			getHomeInfoUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_INDEX);
			getDeviceUrl = KDLCApplication.app.getActionUrl(G.GCK_API__APP_DEVICE_REPORT);
		} else {
			getHomeInfoUrl = G.URL_GET_INDEX;
			getDeviceUrl = G.URL_POST_APP_DEVICE_REPORT;
		}
	}

	private void uploadDeviceInfo() {
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(currentTime);
		HttpParams params = new HttpParams();
		params.add("device_id", KDLCApplication.app.getDeviceId());
		params.add("installed_time", this.getApplicationContext().isFirstIn() ? formatter.format(date) : "");
		params.add("uid", Tool.isBlank(KDLCApplication.app.getSessionVal("uid")) ? "" : KDLCApplication.app.getSessionVal("uid"));
		params.add("username", Tool.isBlank(KDLCApplication.app.getSessionVal("username")) ? "" : KDLCApplication.app.getSessionVal("username"));
		NetworkInfo localNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
		params.add("net_type", "");
		if (localNetworkInfo != null) {
			params.add("net_type", localNetworkInfo.getType() == 1 ? "wifi" : getNetworkClass(SplashActivity.this));
		}
		sendHttpPost(getDeviceUrl, params, uploadInfoListener, errListener);
	}
	
	private Listener<JSONObject> uploadInfoListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			
		}
	};
	
	private ErrorListener errListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			
		}

	};
		
	private String getNetworkClass(Context paramContext) {
		switch (((TelephonyManager) paramContext.getSystemService("phone"))
				.getNetworkType()) {
		case 1:
		case 2:
		case 4:
		case 7:
		case 11:
			return "2G";
		case 3:
		case 5:
		case 6:
		case 8:
		case 9:
		case 10:
		case 12:
		case 14:
		case 15:
			return "3G";
		case 13:
			return "4G";
		default:
			return "Unknown";
		}

	}

	private Listener<JSONObject> getHomeInfoListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				Log.d(LOG_TAG, "response:" + response.toString());
				int retCode = response.getInt("code");
				if (retCode == 0) {
					JSONArray homeDataList = response.getJSONArray("data");
					List<HomeProductInfo> productList = new ArrayList<HomeProductInfo>();
					for (int i = 0; i < homeDataList.length(); i++) {
						JSONObject homeInfoObject = homeDataList
								.getJSONObject(i);
						HomeProductInfo homeProInfo = new HomeProductInfo(
								homeInfoObject.getInt("id"),
								homeInfoObject.getString("name"),
								homeInfoObject.getString("apr"),
								homeInfoObject.getString("total_money"),
								homeInfoObject.getString("success_money"),
								homeInfoObject.getString("success_percent"),
								homeInfoObject.getString("min_invest_money"),
								homeInfoObject.getString("words"),
								homeInfoObject.getString("is_novice"));
						productList.add(homeProInfo);
					}
					if(!KDLCApplication.app.isFirstIn() && Tool.hasCacheData(HomeProductInfo.class))
						KdlcDB.deleteAllByClass(HomeProductInfo.class);
					KdlcDB.addByEntityList(productList);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	// 设置引导图
	private void setSplashImage() {
		View splashView = (View) this.findViewById(R.id.splash_view);
		ImageView splashImage = (ImageView) this.findViewById(R.id.splash_img);
		String imageName = this.getApplicationContext().imgPath + "/" + G.SPLASH_IMAGE_FILENAME;
		File splashImageFile = new File(imageName);
		if (!splashImageFile.exists()) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) splashView.getLayoutParams();
			params.height = (int)(Integer.parseInt(KDLCApplication.app.getSessionVal("screenWidth"))*0.6);
			params.width = params.height;
			params.bottomMargin = (int)(Integer.parseInt(KDLCApplication.app.getSessionVal("screenHeight"))*0.09);
			splashView.setLayoutParams(params);
			//splashView.setVisibility(View.VISIBLE);
			//splashImage.setVisibility(View.GONE);
		} else {
			splashView.setVisibility(View.GONE);
			splashImage.setVisibility(View.VISIBLE);
			Bitmap bm = BitmapFactory.decodeFile(imageName);
			splashImage.setImageBitmap(bm);
		}
	}

	private void goHome() {
		Log.d(LOG_TAG, "go Home");
		Intent intent = new Intent();
		if (!KDLCApplication.app.sessionEqual("gesture", "0") && KDLCApplication.app.hasLogin()) {
			intent.setClass(this, GestureVerifyActivity.class);
		} else {
			intent.setClass(this, MainActivity.class);
		}
		this.startActivity(intent);
		this.finish();
	}

	private void goGuide() {
		Log.d(LOG_TAG, "go Guide");
		Intent intent = new Intent(this, GuideActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	private void gotoNext() {
		if (this.getApplicationContext().isFirstIn()) {
			this.goGuide();
		} else {
			this.goHome();
		}
	}

	private void initAutoRefresh() {
		Log.d(LOG_TAG, "initAutoRefresh");
		Map<String, String> vals = new HashMap<String, String>();
		vals.put("p2pListAutoRefresh", "1");
		vals.put("p2pListAutoRefreshClick", "0");
		vals.put("cessionListAutoRefresh", "1");
		vals.put("trustListAutoRefresh", "1");
		vals.put("cessionListAutoRefresh", "1");
		vals.put("selfCenterAutoRefresh", "1");
		vals.put("selfCenterAutoRefreshClick", "0");
		vals.put("gestureInform", "1");
		vals.put("homeAnimationShowed", "0");//每次启动应用只显示一次动画
		this.getApplicationContext().getSession().set(vals);
	}

	public class SplashHttpErrorListener implements ErrorListener {
		@Override
		public void onErrorResponse(VolleyError error) {
			
		}
	}
}
