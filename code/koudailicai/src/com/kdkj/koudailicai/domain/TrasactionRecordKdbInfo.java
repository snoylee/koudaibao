package com.kdkj.koudailicai.domain;

public class TrasactionRecordKdbInfo {

	private int id;
	private String creatdAt;
	private String status;
	private String investMoney;
	
	public TrasactionRecordKdbInfo(int id, String investMoney, String creatdAt,
			String status) {
		super();
		this.id = id;
		this.investMoney = investMoney;
		this.creatdAt = creatdAt;
		this.status = status;
	}

	
	public TrasactionRecordKdbInfo() {
		super();
		
	}


	public String getInvestMoney() {
		return investMoney;
	}

	public void setInvestMoney(String investMoney) {
		this.investMoney = investMoney;
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



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
