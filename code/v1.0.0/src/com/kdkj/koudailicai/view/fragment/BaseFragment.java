package com.kdkj.koudailicai.view.fragment;

import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.kdkj.koudailicai.lib.http.BaseHttpErrorListener;
import com.kdkj.koudailicai.lib.http.HttpParams;
import com.kdkj.koudailicai.lib.http.HttpRequestGet;
import com.kdkj.koudailicai.lib.http.HttpRequestPost;
import com.kdkj.koudailicai.lib.http.RequestManager;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.kdkj.koudailicai.view.login.LoginActivity;
import com.kdkj.koudailicai.view.login.LoginAlreadyActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class BaseFragment extends Fragment {
	private FragmentActivity mActivity;
	private String LOG_TAG = BaseFragment.class.getName();
	protected  BaseHttpErrorListener errorListener;
	protected AlertDialog dialog = null;
	protected boolean createFlag = true;

	//protected boolean showDialog=true;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		errorListener = new BaseHttpErrorListener(mActivity);
		Log.d(LOG_TAG, mActivity.getClass().getName());
	}

//	protected void sendHttpGet(String url, Listener<JSONObject> responseListener, BaseHttpErrorListener errListener) {
//		url = Tool.getUrl(url);
//		/*if(dialog != null) 
//			errListener.setDialog(dialog);*/
//		
//		HttpRequestGet httpRequest = new HttpRequestGet(url, responseListener, errListener);
//		httpRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
//		RequestManager.addRequest(httpRequest, this);
//	}
	
	protected void sendHttpGet(String url, Listener<JSONObject> responseListener, ErrorListener errListener) {
		url = Tool.getUrl(url);
		HttpRequestGet httpRequest = new HttpRequestGet(url, responseListener, errListener);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	protected void sendHttpGet(String url, Listener<JSONObject> responseListener) {
		url = Tool.getUrl(url);
		if(dialog != null) 
			errorListener.setDialog(dialog);
		HttpRequestGet httpRequest = new HttpRequestGet(url, responseListener, errorListener);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	
	protected void sendHttpPost(String url, HttpParams params, Listener<JSONObject> responseListener, ErrorListener errListener) {
		url = Tool.getUrl(url);
		HttpRequestPost httpRequest = new HttpRequestPost(url, responseListener, errorListener, params);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	protected void sendHttpPost(String url, HttpParams params, Listener<JSONObject> responseListener) {
		url = Tool.getUrl(url);
		if(dialog != null) 
			errorListener.setDialog(dialog);
		HttpRequestPost httpRequest = new HttpRequestPost(url, responseListener, errorListener, params);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	public void sendChargeHttpPost(String url, HttpParams params, Listener<JSONObject> responseListener) {
		url = Tool.getUrl(url);
		if(dialog != null) 
			errorListener.setDialog(dialog);
		HttpRequestPost httpRequest = new HttpRequestPost(url, responseListener, errorListener, params);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 1.0f));//设置重试次数
		RequestManager.addRequest(httpRequest, this);
	}
	
	public void sendChargeHttpPost(String url, HttpParams params, Listener<JSONObject> responseListener, ErrorListener errListener) {
		url = Tool.getUrl(url);
		if(dialog != null) 
			errorListener.setDialog(dialog);
		HttpRequestPost httpRequest = new HttpRequestPost(url, responseListener, errListener, params);
		httpRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 1.0f));
		RequestManager.addRequest(httpRequest, this);
	}
	
	public void startActivityNeedLogin(Class<?> toActivity) {
		Intent intent = new Intent();
		intent.setClass(mActivity, KDLCApplication.app.hasLogin() ? toActivity : LoginAlreadyActivity.class);
		mActivity.startActivity(intent);
	}
	
	public void startActivityNeedLogin(Intent intent) {
		if(!KDLCApplication.app.hasLogin()) {
			intent.setClass(mActivity, LoginAlreadyActivity.class);
		}
		mActivity.startActivity(intent);
	}

	//统计fragment页面的时间
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageStart("basefragment");
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("basefragment");
	}

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//	}
}
