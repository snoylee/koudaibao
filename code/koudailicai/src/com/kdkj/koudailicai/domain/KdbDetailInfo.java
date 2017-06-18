package com.kdkj.koudailicai.domain;

public class KdbDetailInfo {
	
	private int id;
	private String title;
	private String totalMoney;
	private String remainMoney;
	private String apr;
	private String bankApr;
	private String summary;
	private String status;
	private String dailyInvestLimit;
	private String dailyWithdrawLimit;
	private String interestDesc;
	private String minInvestMoney;
	private String productType;
	private String curInvestTimes;
	private String riskControlManaged;
	private String riskControlWarrant;
	private String riskControlRepay;
	private String instruction;
	
	public KdbDetailInfo() {
		super();
	}
	
	public KdbDetailInfo(int id, String title, String totalMoney, String remainMoney, String apr, String bankApr,
			String summary, String status, String dailyInvestLimit,
			String dailyWithdrawLimit, String interestDesc,
			String minInvestMoney, String productType, String curInvestTimes,
			String riskControlManaged, String riskControlWarrant,
			String riskControlRepay, String instruction) {
		super();
		this.id = id;
		this.title = title;
		this.totalMoney = totalMoney;
		this.setRemainMoney(remainMoney);
		this.apr = apr;
		this.bankApr = bankApr;
		this.summary = summary;
		this.status = status;
		this.dailyInvestLimit = dailyInvestLimit;
		this.dailyWithdrawLimit = dailyWithdrawLimit;
		this.interestDesc = interestDesc;
		this.minInvestMoney = minInvestMoney;
		this.productType = productType;
		this.curInvestTimes = curInvestTimes;
		this.riskControlManaged = riskControlManaged;
		this.riskControlWarrant = riskControlWarrant;
		this.riskControlRepay = riskControlRepay;
		this.setInstruction(instruction);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getMinInvestMoney() {
		return minInvestMoney;
	}
	public void setMinInvestMoney(String minInvestMoney) {
		this.minInvestMoney = minInvestMoney;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getCurInvestTimes() {
		return curInvestTimes;
	}
	public void setCurInvestTimes(String curInvestTimes) {
		this.curInvestTimes = curInvestTimes;
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

	@Override
	public String toString() {
		return "KdbDetailInfo [id=" + id + ", title=" + title + ", totalMoney="
				+ totalMoney + ", apr=" + apr + ", bankApr=" + bankApr+ ", summary=" + summary
				+ ", status=" + status + ", dailyInvestLimit="
				+ dailyInvestLimit + ", dailyWithdrawLimit="
				+ dailyWithdrawLimit + ", interestDesc=" + interestDesc
				+ ", minInvestMoney=" + minInvestMoney + ", productType="
				+ productType + ", curInvestTimes=" + curInvestTimes
				+ ", riskControlManaged=" + riskControlManaged
				+ ", riskControlWarrant=" + riskControlWarrant
				+ ", riskControlRepay=" + riskControlRepay + ", instruction=" + instruction + "]";
	}

	public String getRemainMoney() {
		return remainMoney;
	}

	public void setRemainMoney(String remainMoney) {
		this.remainMoney = remainMoney;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
}
