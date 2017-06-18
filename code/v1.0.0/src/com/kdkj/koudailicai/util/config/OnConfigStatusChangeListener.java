package com.kdkj.koudailicai.util.config;

public interface OnConfigStatusChangeListener {

	public void onChange(ConfigManager cm, int oldStatus, int newStatus);
	
}
