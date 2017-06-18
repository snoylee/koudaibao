package com.kdkj.koudailicai.domain;


public class TotalSelfProfitInfo {
	private int id; //
	
	private int infoType;
	private String total_profits;//总累计收益
	
	private String total_money;//累计收益
	
	private String project_name;//收益名称
    
	private String itemId;
	public TotalSelfProfitInfo()
	{
		super();
	}
	public TotalSelfProfitInfo(String total_profits)
	{
		setInfoType(0);
		this.total_profits=total_profits;
	}
	public TotalSelfProfitInfo(String itemId,String project_name,String total_money)
	{
     		super();
     		setInfoType(1);
     		this.itemId=itemId;
     		this.project_name=project_name;
     		this.total_money=total_money;
	}
    	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTotal_profits() {
		return total_profits;
	}

	public void setTotal_profits(String total_profits) {
		this.total_profits = total_profits;
	}


	public String getTotal_money() {
		return total_money;
	}

	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public int getInfoType() {
		return infoType;
	}
	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

}
