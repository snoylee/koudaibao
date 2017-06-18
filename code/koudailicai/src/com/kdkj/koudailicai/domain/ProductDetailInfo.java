package com.kdkj.koudailicai.domain;

public class ProductDetailInfo {
	
	private int id;
	private int productId;
	private String name;
	private String type;
	private String productType;
	private String status;
	private String period;
	private String isDay;
	private String publishAt;
	private String effectTime;
	private String totalMoney;
	private String successMoney;
	private String successNum;	
	private String minInvestMoney;
	private String apr;
	private String bankApr;
	private String summary;
	private String isNovice;
	private String repaymentMark;
	private String interestDate;
	private String repayDate;
	private String riskControlManaged;
	private String riskControlWarrant;
	private String riskControlRepay;
	private String lastRepayDate;
	public ProductDetailInfo() {
		super();
	}
	
	public ProductDetailInfo(int id, int productId, String name, String type, String productType,
			String status, String period, String isDay, String publishAt,
			String effectTime, String totalMoney, String successMoney,
			String successNum, String minInvestMoney, String apr, String bankApr,
			String summary, String isNovice, String repaymentMark,
			String interestDate, String repayDate, String riskControlManaged,
			String riskControlWarrant, String riskControlRepay, String lastRepayDate) {
		super();
		this.id = id;
		this.productId = productId;
		this.name = name;
		this.type = type;
		this.productType = productType;
		this.status = status;
		this.period = period;
		this.isDay = isDay;
		this.publishAt = publishAt;
		this.effectTime = effectTime;
		this.totalMoney = totalMoney;
		this.successMoney = successMoney;
		this.successNum = successNum;
		this.minInvestMoney = minInvestMoney;
		this.apr = apr;
		this.bankApr = bankApr;
		this.summary = summary;
		this.isNovice = isNovice;
		this.repaymentMark = repaymentMark;
		this.interestDate = interestDate;
		this.repayDate = repayDate;
		this.riskControlManaged = riskControlManaged;
		this.riskControlWarrant = riskControlWarrant;
		this.riskControlRepay = riskControlRepay;
		this.lastRepayDate = lastRepayDate;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getPublishAt() {
		return publishAt;
	}
	public void setPublishAt(String publishAt) {
		this.publishAt = publishAt;
	}
	public String getEffectTime() {
		return effectTime;
	}
	public void setEffectTime(String effectTime) {
		this.effectTime = effectTime;
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
	public String getSuccessNum() {
		return successNum;
	}
	public void setSuccessNum(String successNum) {
		this.successNum = successNum;
	}
	public String getMinInvestMoney() {
		return minInvestMoney;
	}
	public void setMinInvestMoney(String minInvestMoney) {
		this.minInvestMoney = minInvestMoney;
	}
	public String getApr() {
		return apr;
	}
	public void setApr(String apr) {
		this.apr = apr;
	}
	public String getBankApr() {
		return bankApr;
	}
	public void setBankApr(String bankApr) {
		this.bankApr = bankApr;
	}	
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getIsNovice() {
		return isNovice;
	}
	public void setIsNovice(String isNovice) {
		this.isNovice = isNovice;
	}
	public String getRepaymentMark() {
		return repaymentMark;
	}
	public void setRepaymentMark(String repaymentMark) {
		this.repaymentMark = repaymentMark;
	}
	public String getInterestDate() {
		return interestDate;
	}
	public void setInterestDate(String interestDate) {
		this.interestDate = interestDate;
	}
	public String getRepayDate() {
		return repayDate;
	}
	public void setRepayDate(String repayDate) {
		this.repayDate = repayDate;
	}
	public String getRiskControlManaged() {
		return riskControlManaged;
	}
	public void setRiskControlManaged(String riskControlManaged) {
		this.riskControlManaged = riskControlManaged;
	}
	public String getRiskControlWarrant() {
		return riskControlWarrant;
	}
	public void setRiskControlWarrant(String riskControlWarrant) {
		this.riskControlWarrant = riskControlWarrant;
	}
	public String getRiskControlRepay() {
		return riskControlRepay;
	}
	public void setRiskControlRepay(String riskControlRepay) {
		this.riskControlRepay = riskControlRepay;
	}
	public String getLastRepayDate() {
		return lastRepayDate;
	}
	public void setLastRepayDate(String lastRepayDate) {
		this.lastRepayDate = lastRepayDate;
	}
	
	
	@Override
	public String toString() {
		return "ProductDetailInfo [id=" + id + ", productId=" + productId + ", name=" + name + ", type=" + type
				+ ", productType=" + productType + ", status=" + status
				+ ", period=" + period + ", isDay=" + isDay + ", publishAt="
				+ publishAt + ", effectTime=" + effectTime + ", totalMoney="
				+ totalMoney + ", successMoney=" + successMoney
				+ ", successNum=" + successNum + ", minInvestMoney="
				+ minInvestMoney + ", apr=" + apr + ", bankApr=" + bankApr + ", summary=" + summary
				+ ", isNovice=" + isNovice + ", repaymentMark=" + repaymentMark
				+ ", interestDate=" + interestDate + ", repayDate=" + repayDate
				+ ", riskControlManaged=" + riskControlManaged
				+ ", riskControlWarrant=" + riskControlWarrant
				+ ", riskControlRepay=" + riskControlRepay +", lastRepayDate"+ lastRepayDate + "]";
	}

}
