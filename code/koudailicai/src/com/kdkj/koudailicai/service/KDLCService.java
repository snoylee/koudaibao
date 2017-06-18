package com.kdkj.koudailicai.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.domain.HomeProductInfo;
import com.kdkj.koudailicai.lib.http.HttpRequestGet;
import com.kdkj.koudailicai.lib.http.RequestManager;
import com.kdkj.koudailicai.util.LogUtil;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.util.global.G;
import com.kdkj.koudailicai.view.KDLCApplication;

/**
 * 后台服务
 * 执行定时更新
 * @author liaoheng
 * @creation 2014-12-22
 *
 */
public class KDLCService extends Service{
	private String getHomeInfoUrl;
	long updatePeriod=10*60*1000;
	public Handler handler=new Handler();
	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		parseUrl();
		handler.postDelayed(runnable, updatePeriod);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		LogUtil.info("onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.info("onDestroy");
		handler.removeCallbacks(runnable);
		super.onDestroy();
		//重启服务
		//startService(new Intent(this,KDLCService.class));
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
		LogUtil.info("onRebind");
	}


	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		LogUtil.info("onUnbind");
		return super.onUnbind(intent);
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		LogUtil.info("onBind");
		return null;
	}
	
	
	
	
	public Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			LogUtil.info("更新首页内容");
			sendHttpGet(getHomeInfoUrl, getHomeInfoListener);
			handler.postDelayed(this, updatePeriod);
			
		}
	};
	
	private void parseUrl() {
		 if (KDLCApplication.app.isGlobalConfCompleted()) {
			 getHomeInfoUrl = KDLCApplication.app.getActionUrl(G.GCK_API_GET_INDEX);
		 } else {
			 getHomeInfoUrl = G.URL_GET_INDEX;
		 }
	}
	private void sendHttpGet(String url, Listener<JSONObject> responseListener) {
		if(!isAppForeground()){
			LogUtil.info("当前程序不在前台");
			return;
		}
		url = Tool.getUrl(url);
		LogUtil.info("本地地址？"+url);
		HttpRequestGet httpRequest = new HttpRequestGet(url, responseListener, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		RequestManager.addRequest(httpRequest, this);
	}
	private Listener<JSONObject> getHomeInfoListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			try {
				int retCode = response.getInt("code");
				if (retCode == 0) {
					JSONArray homeDataList = response.getJSONArray("data");
					
					ArrayList<HomeProductInfo> productList=new ArrayList<HomeProductInfo>();
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
					KdlcDB.deleteAllByClass(HomeProductInfo.class);
					KdlcDB.addByEntityList(productList);
					KDLCApplication.app.setSessionVal(G.HOME_INFO_UPDATED, "updated");
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	};
	
	private boolean isAppForeground(){
		ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks=am.getRunningTasks(1);
		if(!tasks.isEmpty()){
			ComponentName cm=tasks.get(0).topActivity;
			if(cm.getPackageName().equals(this.getPackageName()))
				return true;
			
			else 
				return false;
		}
		else return false;
	}

}
