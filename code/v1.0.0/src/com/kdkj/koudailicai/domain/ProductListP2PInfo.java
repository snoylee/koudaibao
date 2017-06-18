package com.kdkj.koudailicai.domain;

import android.R.integer;

/**
 * 产品列表详细信息
 * 
 * @author ok
 * 
 */
public class ProductListP2PInfo {

	private int id; // 记录ID
	private int productType; // 1表示口袋宝；2表示p2p产品
	//通用信息
	private int productID; //项目ID
	private String name;
	private String status; // 状态
	private String apr; // 年利率
	private String totalMoney;//总金额
	private String minInvestMoney;//最小投资金额
	private String successNumber;//投资人数
	//P2P项目专用信息
	private String successMoney;//成功金额
	private String isNovice;//成功金额
	private String period; // 借款期限
	private String isDay; // 期限类型
	//口袋宝信息
	private String summary;
	private String dailyInvestLimit;
	private String dailyWithdrawLimit;
	private String interestDesc;
	
	public ProductListP2PInfo() {
		super();
	}
	//口袋宝信息
	public ProductListP2PInfo(int productType, String name, String status, String apr, 
							  String totalMoney, String minInvestMoney, String successNumber, 
							  String summary, String dailyInvestLimit, String dailyWithdrawLimit,
							  String interestDesc, int productID) {
		super();
		this.productID = productID;
		this.productType = productType;
		this.name = name;
		this.status = status;
		this.apr = apr;
		this.totalMoney = totalMoney;
		this.minInvestMoney = minInvestMoney;
		this.successNumber = successNumber;
		this.summary = summary;
		this.dailyInvestLimit = dailyInvestLimit;
		this.dailyWithdrawLimit = dailyWithdrawLimit;
		this.interestDesc = interestDesc;
	}
	
	//P2P信息
	public ProductListP2PInfo(int productType, int productID,
			String name, String status, String apr, String totalMoney,
			String minInvestMoney, String successNumber, String successMoney,
			String isNovice, String period, String isDay) {
		super();
		this.productType = productType;
		this.productID = productID;
		this.name = name;
		this.status = status;
		this.apr = apr;
		this.totalMoney = totalMoney;
		this.minInvestMoney = minInvestMoney;
		this.successNumber = successNumber;
		this.successMoney = successMoney;
		this.isNovice = isNovice;
		this.period = period;
		this.isDay = isDay;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
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

	public String getApr() {
		return apr;
	}

	public void setApr(String apr) {
		this.apr = apr;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getMinInvestMoney() {
		return minInvestMoney;
	}

	public void setMinInvestMoney(String minInvestMoney) {
		this.minInvestMoney = minInvestMoney;
	}

	public String getSuccessNumber() {
		return successNumber;
	}

	public void setSuccessNumber(String successNumber) {
		this.successNumber = successNumber;
	}

	public String getSuccessMoney() {
		return successMoney;
	}

	public void setSuccessMoney(String successMoney) {
		this.successMoney = successMoney;
	}

	public String getIsNovice() {
		return isNovice;
	}

	public void setIsNovice(String isNovice) {
		this.isNovice = isNovice;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDailyInvestLimit() {
		return dailyInvestLimit;
	}

	public void setDailyInvestLimit(String dailyInvestLimit) {
		this.dailyInvestLimit = dailyInvestLimit;
	}

	public String getDailyWithdrawLimit() {
		return dailyWithdrawLimit;
	}

	public void setDailyWithdrawLimit(String dailyWithdrawLimit) {
		this.dailyWithdrawLimit = dailyWithdrawLimit;
	}

	public String getInterestDesc() {
		return interestDesc;
	}

	public void setInterestDesc(String interestDesc) {
		this.interestDesc = interestDesc;
	}

}
