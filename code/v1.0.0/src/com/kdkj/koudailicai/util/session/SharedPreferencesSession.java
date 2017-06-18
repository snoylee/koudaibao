package com.kdkj.koudailicai.util.session;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesSession extends Session {
	
	private static final String DEFAULT_SESSION_FILE = "session";
	
	protected static class Value {
		
		protected String value;
		
		protected long expireTime;
		
		public Value(String value, long expireTime) {
			this.value = value;
			this.expireTime = expireTime;
		}
		
		public Value(String value) {
			this(value, 0);
		}
		
		public String toString() {
			return expireTime + ":" + value;
		}
		
		public static Value toValue(String valueStr) {
			if(valueStr == null) {
				return null;
			}
			
			int pos = valueStr.indexOf(':');
			if(pos == -1) {
				return null;
			}
			
			return new Value(valueStr.substring(pos + 1), Long.parseLong(valueStr.substring(0, pos)));
		}

	}
	
	protected SharedPreferences sp;
	
	public SharedPreferencesSession(Context context) {
		this(context, DEFAULT_SESSION_FILE);
	}
	
	public SharedPreferencesSession(Context context, String filename) {
		this.sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
	}

	@Override
	public void set(String key, String value, long expireSeconds) {
		// TODO Auto-generated method stub
		if(expireSeconds <= 0) {
			this.sp.edit().putString(key, new Value(value, 0).toString()).commit();
		} else {
			this.sp.edit().putString(key, new Value(value, new Date().getTime() / 1000 + expireSeconds).toString()).commit();
		}
	}

	@Override
	public void set(Map<String, String> vals, long expireSeconds) {
		// TODO Auto-generated method stub
		Editor editor = this.sp.edit();
		for(Iterator i = vals.keySet().iterator(); i.hasNext(); ) {
			String key = (String)i.next();
			String value = vals.get(key);
			if(expireSeconds <= 0) {
				editor.putString(key, new Value(value, 0).toString());
			} else {
				editor.putString(key, new Value(value, new Date().getTime() / 1000 + expireSeconds).toString());
			}
		}
		editor.commit();
	}

	@Override
	public String get(String key) {
		// TODO Auto-generated method stub
		Value val = Value.toValue(this.sp.getString(key, null));
		if(val != null) {
			if(val.expireTime <= 0 || val.expireTime > new Date().getTime() / 1000) {
				return val.value;
			}
		}
		return null;
	}

}
