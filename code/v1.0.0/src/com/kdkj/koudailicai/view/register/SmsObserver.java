package com.kdkj.koudailicai.view.register;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kdkj.koudailicai.view.KDLCApplication;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class SmsObserver extends ContentObserver {

	public String verifyNum = "";
	private Uri SMS_INBOX = Uri.parse("content://sms/");
	public static final int SEND_VERIFY_NUM = 10;
	private Handler handler;

	public SmsObserver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		// 每当有新短信到来时，使用我们获取短消息的方法
		getSmsFromPhone();
	}

	public void getSmsFromPhone() {
		ContentResolver cr = KDLCApplication.app.getContentResolver();
		String[] projection = new String[] { "body" };
		String where = "date >" + (System.currentTimeMillis() - 10 * 60 * 1000);
		Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
		if (null == cur)
			return;
		if (cur.moveToNext()) {
			String body = cur.getString(cur.getColumnIndex("body"));
			if (body != null && body.contains("口袋理财") && body.contains("验证码")) {
				Pattern p = Pattern.compile("[0-9]{6}");
				Matcher matcher = p.matcher(body);
				if (matcher.find()) {
					verifyNum = matcher.group();
					handler.sendEmptyMessage(SEND_VERIFY_NUM);
				}
			}
		}
	}
}
