package com.kdkj.koudailicai.domain;

public class KdbRollOutListInfo {
    
	private int id;
	
	private String rolloutmoney;// 转出金额

	private String rollouttime;// 转出时间

	public KdbRollOutListInfo(String rolloutmoney, String rollouttime) {
		// TODO Auto-generated constructor stub
		this.rolloutmoney = rolloutmoney;
		this.rollouttime = rollouttime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRolloutmoney() {
		return rolloutmoney;
	}

	public void setRolloutmoney(String rolloutmoney) {
		this.rolloutmoney = rolloutmoney;
	}

	public String getRollouttime() {
		return rollouttime;
	}

	public void setRollouttime(String rollouttime) {
		this.rollouttime = rollouttime;
	}

}
