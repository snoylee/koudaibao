package com.kdkj.koudailicai.view;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.VolleyError;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.http.HttpRequestGet;
import com.kdkj.koudailicai.lib.http.HttpRequestPost;
import com.kdkj.koudailicai.lib.http.RequestManager;
import com.kdkj.koudailicai.util.AppManager;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.invest.InvestChargeActivity;
import com.kdkj.koudailicai.view.login.LoginAlreadyActivity;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class BaseActivity extends FinalActivity {

	protected Activity activity;
	protected BaseHttpErrorListener errorListener;
	protected Intent afterLoginIntent;
	//sizes
	private int screenHeight;
	private int screenWidth;
	protected AlertDialog dialog = null;
	protected boolean createFlag = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity = this;
		AppManager.getAppManager().addActivity(activity);
		errorListener = new BaseHttpErrorListener(activity, dialog);
		getScreenSize();
	}
	
	public int getScreenHeight() {
		return this.screenHeight;
	}

	public int getScreenWidth() {
		return this.screenWidth;
	}
	
	public void getScreenSize()
	{
		//获取状态栏高度
		int statusBarHeight = getStatusBarHeight();
		//获取屏幕尺寸
		DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels-statusBarHeight;
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put("screenWidth", ""+screenWidth);
		vals.put("screenHeight", ""+screenHeight);
		this.getApplicationContext().getSession().set(vals);
	}
	
	private int getStatusBarHeight() {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = this.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sbar;
	}
	
	@Override
	public KDLCApplication getApplicationContext() {
		// TODO Auto-generated method stub
		return (KDLCApplication) super.getApplicationContext();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		RequestManager.cancelAll(this);
	}
	
	protected void sendHttpGet(String url, Listener<JSONObject> responseListener, ErrorListener errListener) {
		url = Tool.getUrl(url);
		Log.d("HttpRequestGet","url:"+url);
		HttpRequestGet httpRequest = new HttpRequestGet(url, responseListener, errListener);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	protected void sendHttpGet(String url, Listener<JSONObject> responseListener) {
		url = Tool.getUrl(url);
		Log.d("HttpRequestGet","url:"+url);
		if(dialog != null) 
			errorListener.setDialog(dialog);
		HttpRequestGet httpRequest = new HttpRequestGet(url, responseListener, errorListener);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	protected void sendHttpGetCharge(String url, Listener<JSONObject> responseListener, ErrorListener errListener) {
		url = Tool.getUrl(url);
		Log.d("HttpRequestGet","url:"+url);
		HttpRequestGet httpRequest = new HttpRequestGet(url, responseListener, errListener);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	protected void sendHttpGetCharge(String url, Listener<JSONObject> responseListener) {
		url = Tool.getUrl(url);
		Log.d("HttpRequestGet", "sendHttpPost:"+url);
		if(dialog != null) 
			errorListener.setDialog(dialog);
		HttpRequestGet httpRequest = new HttpRequestGet(url, responseListener, errorListener);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	public void sendHttpPost(String url, HttpParams params, Listener<JSONObject> responseListener) {
		url = Tool.getUrl(url);
		Log.d("BaseAct", "sendHttpPost:"+url);
		if(dialog != null) 
			errorListener.setDialog(dialog);
		HttpRequestPost httpRequest = new HttpRequestPost(url, responseListener, errorListener, params);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	public void sendHttpPost(String url, HttpParams params, Listener<JSONObject> responseListener, ErrorListener errListener) {
		url = Tool.getUrl(url);
		Log.d("BaseAct", "sendHttpPost:"+url);
		HttpRequestPost httpRequest = new HttpRequestPost(url, responseListener, errListener, params);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	public void sendHttpPostCharge(String url, HttpParams params, Listener<JSONObject> responseListener) {
		url = Tool.getUrl(url);
		Log.d("BaseAct", "sendHttpPost:"+url);
		if(dialog != null) 
			errorListener.setDialog(dialog);
		HttpRequestPost httpRequest = new HttpRequestPost(url, responseListener, errorListener, params);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	public void sendHttpPostCharge(String url, HttpParams params, Listener<JSONObject> responseListener, ErrorListener errListener) {
		Log.d("BaseAct", "sendHttpPost:"+url);
		url = Tool.getUrl(url);
		HttpRequestPost httpRequest = new HttpRequestPost(url, responseListener, errListener, params);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	public void startActivityNeedLogin(Class<?> toActivity) {
		Intent intent = new Intent();
		intent.setClass(activity, toActivity);
		this.afterLoginIntent = intent;
		if (this.getApplicationContext().hasLogin()) {
			this.startActivity(this.afterLoginIntent);
		} else {
			Intent loginIntent = new Intent();
			loginIntent.setClass(this, LoginAlreadyActivity.class);
			this.startActivityForResult(loginIntent, G.REQ_CODE_ACTIVITY_LOGIN);
		}
	}
	
	public void setDialog(AlertDialog dialog) {
		this.dialog = dialog;
	}
	
	public void closeDialog() {
		if(dialog != null) {
			dialog.cancel();
		}
	}
		
	public void startActivityNeedLogin(Intent intent) {
		this.afterLoginIntent = intent;
		if (this.getApplicationContext().hasLogin()) {
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
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	//在以下两个方法中调用umeng的数据统计方法
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		closeDialog();
	}
	
	protected OnClickListener finishActivity = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			activity.finish();
			if(dialog != null) {
				dialog.cancel();
			}
		}
	};
	protected OnClickListener finishDialog = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(dialog != null) {
				dialog.cancel();
			}
		}
	};
	
    @Override  
    public Resources getResources() {  
        Resources res = super.getResources();    
        Configuration config=new Configuration();    
        config.setToDefaults();    
        res.updateConfiguration(config,res.getDisplayMetrics() );  
        return res;  
    }  
}
