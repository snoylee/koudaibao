package com.kdkj.koudailicai.util.session;

import java.util.Map;

public abstract class Session {
	
	public void set(String key, String value) {
		this.set(key, value, 0);
	}
	
	public abstract void set(String key, String value, long expireSeconds);
	
	public void set(Map<String, String> vals) {
		this.set(vals, 0);
	}
	
	public abstract void set(Map<String, String> vals, long expireSeconds);
	
	public abstract String get(String key);

}
