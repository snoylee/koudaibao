package com.kdkj.koudailicai.domain;

public class EarningsYesterdayInfo {
	private int id; // 标ID
	
	private int infoType;
	
	private String lastdate;
	
 	private String lastday_profits;//总累计收益
	
	private String total_profits;//累计收益
	
	private String project_name;//收益名称
	
	private String date;//日期
    
	public EarningsYesterdayInfo(){
		super();
	}
	public EarningsYesterdayInfo(String lastdate,String lastday_profits){
		setInfoType(0);
		this.lastdate = lastdate;
		this.lastday_profits = lastday_profits;
	}
	public EarningsYesterdayInfo(String project_name,String date,String total_profits){
     		super();
     		setInfoType(1);
     		this.project_name=project_name;
     		this.date=date;
     		this.total_profits=total_profits;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInfoType() {
		return infoType;
	}
	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}
	public String getLastdate() {
		return lastdate;
	}
	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}
	public String getLastday_profits() {
		return lastday_profits;
	}
	public void setLastday_profits(String lastday_profits) {
		this.lastday_profits = lastday_profits;
	}
	public String getTotal_profits() {
		return total_profits;
	}
	public void setTotal_profits(String total_profits) {
		this.total_profits = total_profits;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
