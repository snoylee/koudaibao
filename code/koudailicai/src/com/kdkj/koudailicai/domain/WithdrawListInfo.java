package com.kdkj.koudailicai.domain;

public class WithdrawListInfo {
	
	/*"id": "14",
    "money": "10000",
    "status": "1",
    "created_at": "1417432818",
    "statusLabel": "提现中"*/
	private int id;
	private String money;
	private Boolean status;
	private String createdAt;
	private String statusLabel;
	public WithdrawListInfo(){
		super();
	}
	public WithdrawListInfo(String money,String statusLabel,String createdAt){
		this.money = money;
		this.statusLabel = statusLabel;
		this.createdAt = createdAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	
}
