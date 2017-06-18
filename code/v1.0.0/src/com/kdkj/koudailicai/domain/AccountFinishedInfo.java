package com.kdkj.koudailicai.domain;

public class AccountFinishedInfo {

	private int id;
	
	private String itemId;
	
	private String projectName;
	
	private String InvsetMoney;
	
	private String statusLabel;
    
	private String upDate;
	
	public AccountFinishedInfo(){
		
	}
	public AccountFinishedInfo(String itemId,String projectName,String InvsetMoney,String statusLabel,String upDate)
	{
		this.itemId=itemId;
		this.projectName=projectName;
		this.InvsetMoney=InvsetMoney;
		this.statusLabel=statusLabel;
		this.upDate=upDate;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    
	
	public String getUpDate() {
		return upDate;
	}
	public void setUpDate(String upDate) {
		this.upDate = upDate;
	}
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getInvsetMoney() {
		return InvsetMoney;
	}

	public void setInvsetMoney(String invsetMoney) {
		InvsetMoney = invsetMoney;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	
	
}
