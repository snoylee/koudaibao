package com.kdkj.koudailicai.domain;

public class TrasactionRecordOtherInfo {
	private int id;
	private String creatdAt;
	private String status;
	private String investMoney;
	private String investName;
	
	public TrasactionRecordOtherInfo(int id, String investMoney, String creatdAt,
			String status) {
		super();
		this.id = id;
		this.investMoney = investMoney;
		this.creatdAt = creatdAt;
		this.status = status;
	}

	
	public TrasactionRecordOtherInfo() {
		super();
		
	}
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCreatdAt() {
		return creatdAt;
	}


	public void setCreatdAt(String creatdAt) {
		this.creatdAt = creatdAt;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getInvestMoney() {
		return investMoney;
	}


	public void setInvestMoney(String investMoney) {
		this.investMoney = investMoney;
	}


	public String getInvestName() {
		return investName;
	}


	public void setInvestName(String investName) {
		this.investName = investName;
	}





}
