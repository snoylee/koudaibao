package com.kdkj.koudailicai.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class InvestResultInfo implements Parcelable {

	private String projectTypeDesc;
	private String projectName;
	private String apr;
	private String investMoney;
	private String investDate;
	private String startDate;
	private String startDesc;
	private String endDate;
	private String endDesc;
	
	public InvestResultInfo() {
		super();
	}
	
	public InvestResultInfo(String projectTypeDesc, String projectName,
			String apr, String investMoney, String investDate,
			String startDate, String startDesc, String endDate, String endDesc) {
		super();
		this.projectTypeDesc = projectTypeDesc;
		this.projectName = projectName;
		this.apr = apr;
		this.investMoney = investMoney;
		this.investDate = investDate;
		this.startDate = startDate;
		this.startDesc = startDesc;
		this.endDate = endDate;
		this.endDesc = endDesc;
	}
	
	public String getProjectTypeDesc() {
		return projectTypeDesc;
	}
	public void setProjectTypeDesc(String projectTypeDesc) {
		this.projectTypeDesc = projectTypeDesc;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getApr() {
		return apr;
	}
	public void setApr(String apr) {
		this.apr = apr;
	}
	public String getInvestMoney() {
		return investMoney;
	}
	public void setInvestMoney(String investMoney) {
		this.investMoney = investMoney;
	}
	public String getInvestDate() {
		return investDate;
	}
	public void setInvestDate(String investDate) {
		this.investDate = investDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartDesc() {
		return startDesc;
	}
	public void setStartDesc(String startDesc) {
		this.startDesc = startDesc;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEndDesc() {
		return endDesc;
	}
	public void setEndDesc(String endDesc) {
		this.endDesc = endDesc;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(projectTypeDesc);
		dest.writeString(projectName);
		dest.writeString(apr);      
		dest.writeString(investMoney); 
		dest.writeString(investDate); 
		dest.writeString(startDate); 
		dest.writeString(startDesc); 
		dest.writeString(endDate); 
		dest.writeString(endDesc); 
	
	}
	
	public static final Creator<InvestResultInfo> CREATOR = new Creator<InvestResultInfo>() {

		@Override
		public InvestResultInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			InvestResultInfo investResultInfo = new InvestResultInfo(
					source.readString(),
					source.readString(),
					source.readString(),
					source.readString(),
					source.readString(),
					source.readString(),
					source.readString(),
					source.readString(),
					source.readString()
					);
			return investResultInfo;
		}

		@Override
		public InvestResultInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new InvestResultInfo[size];
		}
	};
	
}
