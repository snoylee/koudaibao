package com.kdkj.koudailicai.xgpush;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import com.kdkj.koudailicai.util.db.KdlcDB;
import com.kdkj.koudailicai.view.KDLCApplication;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class XGMessageReceiver extends XGPushBaseReceiver {

	private static final String LOG_TAG = "XGMessageReceiver";
	private XGNotification xgNotification;

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		// TODO Auto-generated method stub
		if (context == null || message == null) {
			return;
		}
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
			Log.d(LOG_TAG, " 通知在通知栏被点击啦");
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
			Log.d(LOG_TAG, "通知被清除啦");
			//只要有一个消息被清除说明消息为未读
			KDLCApplication.app.initMessageCenter("1");

		}

		// 获取自定义key-value
		String customContent = message.getCustomContent();
		Log.d(LOG_TAG, "customContent = " + customContent);
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理的过程。。。

	}

	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		// TODO Auto-generated method stub
		if (context == null || notifiShowedRlt == null) {
			return;
		}
		xgNotification = new XGNotification();
		xgNotification.setMsg_id(notifiShowedRlt.getMsgId());
		xgNotification.setTitle(notifiShowedRlt.getTitle());
		xgNotification.setContent(notifiShowedRlt.getContent());
		// notificationActionType==1为Activity，2为url，3为intent
		xgNotification.setNotificationActionType(notifiShowedRlt
				.getNotificationActionType());
		// Activity,url,intent都可以通过getActivity()获得
		xgNotification.setNotificationAction(notifiShowedRlt.getActivity());
		xgNotification.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm")
				.format(Calendar.getInstance().getTime()));
		KdlcDB.addByEntity(xgNotification);
	}

	// 注册回调
	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
		// TODO Auto-generated method stub
		if (context == null || message == null) {
			return;
		}
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			// 注册成功
		} else {
			// 注册失败
		}

	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		// TODO Auto-generated method stub

	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理消息的过程...

	}

	// 取消注册回调
	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		// TODO Auto-generated method stub
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			// 取消注册成功

		} else {
			// 取消注册失败
		}
	}
}
