package com.kdkj.koudailicai.xgpush;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.kdkj.koudailicai.view.KDLCApplication;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;


public class XGRegisterOperate {
	private static final String LOG_TAG = "XGRegisterOperate";
	private static final int success = 1;
	private static final int error =2 ;
	
	private Handler registerHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case error:
				register(KDLCApplication.app.getApplicationContext());
				break;
			case success:
				
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	//注册
    public void register(final Context context){
    	XGPushManager.registerPush(context, new XGIOperateCallback() {
			@Override
			public void onSuccess(Object data, int flag) {
				// TODO Auto-generated method stub
//				Toast.makeText(context, "success ="+data, Toast.LENGTH_SHORT).show();
				
			}
			
			@Override
			public void onFail(Object data, int errCode, String msg) {
				// TODO Auto-generated method stub
				registerHandler.sendEmptyMessageDelayed(error, 1000);
//				Toast.makeText(context, "error ="+data, Toast.LENGTH_SHORT).show();
			}
		});
    }	
    
    public void register(Context context,String account){
    	XGPushManager.registerPush(context, account, new XGIOperateCallback() {
			
			@Override
			public void onSuccess(Object data, int flag) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onFail(Object data, int errCode, String msg) {
				// TODO Auto-generated method stub
			}
		});
    }
    
    
    //取消注册
    public void unRegister(Context context){
    	XGPushManager.unregisterPush(context);
    }
}
