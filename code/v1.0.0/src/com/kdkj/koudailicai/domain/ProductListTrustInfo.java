package com.kdkj.koudailicai.domain;

import android.R.integer;

public class ProductListTrustInfo {
	
	private int id;// 记录ID
	
	private int productID;//项目ID
	private String name;// 名称
	private String status; // 状态
	private String totalMoney; //总金额
	private String successMoney;//成功金额
	private String successNumber;//投资人数
	private String isNovice;
	private String minInvestMoney;//最小投资金额
	private String period; // 借款期限
    private String isDay; // 期限类型
	private String apr; // 年利率
	
	public ProductListTrustInfo(){
		super();
	}
	
	public ProductListTrustInfo(int productID, String name, String status,
			String totalMoney, String successMoney, String successNumber,
			String isNovice, String minInvestMoney, String period,
			String isDay, String apr) {
		super();
		this.productID = productID;
		this.name = name;
		this.status = status;
		this.totalMoney = totalMoney;
		this.successMoney = successMoney;
		this.successNumber = successNumber;
		this.isNovice = isNovice;
		this.minInvestMoney = minInvestMoney;
		this.period = period;
		this.isDay = isDay;
		this.apr = apr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getSuccessMoney() {
		return successMoney;
	}

	public void setSuccessMoney(String successMoney) {
		this.successMoney = successMoney;
	}

	public String getSuccessNumber() {
		return successNumber;
	}

	public void setSuccessNumber(String successNumber) {
		this.successNumber = successNumber;
	}

	public String getIsNovice() {
		return isNovice;
	}

	public void setIsNovice(String isNovice) {
		this.isNovice = isNovice;
	}

	public String getMinInvestMoney() {
		return minInvestMoney;
	}

	public void setMinInvestMoney(String minInvestMoney) {
		this.minInvestMoney = minInvestMoney;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getIsDay() {
		return isDay;
	}

	public void setIsDay(String isDay) {
		this.isDay = isDay;
	}

	public String getApr() {
		return apr;
	}

	public void setApr(String apr) {
		this.apr = apr;
	}
  
}
