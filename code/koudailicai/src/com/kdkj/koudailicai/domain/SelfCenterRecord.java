package com.kdkj.koudailicai.domain;

import android.R.integer;

public class SelfCenterRecord {
	private int id;
	private int uid;
	private String curProfitsDate;
	private String curProfits;
	private String totalProfits;
	private String totalMoney;
	private String holdMoney;
	private String remainMoney;
	private String newTradeCount;
	private String finishCount;
	public SelfCenterRecord() {
		super();
	}
	
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public SelfCenterRecord(int id, int uid,String curProfitsDate, String curProfits,
			String totalProfits, String totalMoney, String holdMoney,
			String remainMoney, String newTradeCount,String finishCount) {
		super();
		this.id = id;
		this.uid=uid;
		this.curProfitsDate = curProfitsDate;
		this.curProfits = curProfits;
		this.totalProfits = totalProfits;
		this.totalMoney = totalMoney;
		this.holdMoney = holdMoney;
		this.remainMoney = remainMoney;
		this.newTradeCount = newTradeCount;
		this.finishCount=finishCount;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCurProfitsDate() {
		return curProfitsDate;
	}
	public void setCurProfitsDate(String curProfitsDate) {
		this.curProfitsDate = curProfitsDate;
	}
	public String getCurProfits() {
		return curProfits;
	}
	public void setCurProfits(String curProfits) {
		this.curProfits = curProfits;
	}
	public String getTotalProfits() {
		return totalProfits;
	}
	public void setTotalProfits(String totalProfits) {
		this.totalProfits = totalProfits;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getHoldMoney() {
		return holdMoney;
	}
	public void setHoldMoney(String holdMoney) {
		this.holdMoney = holdMoney;
	}
	public String getRemainMoney() {
		return remainMoney;
	}
	public void setRemainMoney(String remainMoney) {
		this.remainMoney = remainMoney;
	}
	public String getNewTradeCount() {
		return newTradeCount;
	}
	public void setNewTradeCount(String newTradeCount) {
		this.newTradeCount = newTradeCount;
	}

	public String getFinishCount() {
		return finishCount;
	}

	public void setFinishCount(String finishCount) {
		this.finishCount = finishCount;
	}

}
