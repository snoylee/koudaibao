 package com.kdkj.koudailicai.domain;

public class TenderConstructionInfo {
//    "invests": [{
//        "id": "7",
//        "username": "1391****931",
//        "invest_money": "10000",
//        "created_at": "1416915992",
//        "status": "1",
//        "statusLabel": "成功"
//    },
	private String id;
	private String username;
	private String invest_money;
	private String created_at;
	private String status;
	private String statusLabel;

	private int infoType;
	
	public TenderConstructionInfo() {
		super();
		// TODO Auto-generated constructor stub
		setInfoType(0);
	}
	
	public TenderConstructionInfo(String id, String username, String invest_money,
		String created_at, String status, String statusLabel) {
	super();
	this.id = id;
	this.username = username;
	this.invest_money = invest_money;
	this.created_at = created_at;
	this.status = status;
	this.statusLabel = statusLabel;
	setInfoType(1);
}
	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getInvest_money() {
		return invest_money;
	}



	public void setInvest_money(String invest_money) {
		this.invest_money = invest_money;
	}



	public String getCreated_at() {
		return created_at;
	}



	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getStatusLabel() {
		return statusLabel;
	}



	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	
	public int getInfoType() {
		return infoType;
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  "id = "+ id +","+"username = "+ username +" invest_money = " + invest_money +"created_at = "+created_at 
				+ "status = " + status + "statusLabel = "+ statusLabel;
	}

	
	
	
}
