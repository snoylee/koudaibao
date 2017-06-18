package com.kdkj.koudailicai.domain;

public class CessionTradeInfo {
	
	private int id;
	private String tradeId;
	private String investUid;
	private String profitsUid;
	private String projectApr;
	private String tradeTime;
	private String dueinCapital;
	private String creditItemsCount;
	
	private int infoType;
	
	public CessionTradeInfo(String creditItemsCount) {
		// TODO Auto-generated constructor stub
		super();
		infoType = 0;
		this.creditItemsCount = creditItemsCount;
	}

	public CessionTradeInfo() {
		// TODO Auto-generated constructor stub
		super();
	}

	
	public CessionTradeInfo(int id, String tradeId, String investUid, String profitsUid, 
			  String tradeTime, String dueinCapital, String projectApr) {
		super();
		this.id = id;
		this.tradeId = tradeId;
		this.investUid = investUid;
		this.profitsUid = profitsUid;
		this.tradeTime = tradeTime;
		this.dueinCapital = dueinCapital;
		this.projectApr = projectApr;
	}
	
	public CessionTradeInfo(String tradeId, String investUid, String profitsUid, 
						  String tradeTime, String dueinCapital, String projectApr) {
		super();
		this.tradeId = tradeId;
		this.investUid = investUid;
		this.profitsUid = profitsUid;
		this.tradeTime = tradeTime;
		this.dueinCapital = dueinCapital;
		this.projectApr = projectApr;
	}
	
	public CessionTradeInfo(String tradeTime,String dueinCapital,String projectApr){
		super();
		infoType = 1;
		this.tradeTime = tradeTime;
		this.dueinCapital = dueinCapital;
		this.projectApr = projectApr;
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

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getInvestUid() {
		return investUid;
	}

	public void setInvestUid(String investUid) {
		this.investUid = investUid;
	}

	public String getProfitsUid() {
		return profitsUid;
	}

	public void setProfitsUid(String profitsUid) {
		this.profitsUid = profitsUid;
	}

	public String getProjectApr() {
		return projectApr;
	}

	public void setProjectApr(String projectApr) {
		this.projectApr = projectApr;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getDueinCapital() {
		return dueinCapital;
	}

	public void setDueinCapital(String dueinCapital) {
		this.dueinCapital = dueinCapital;
	}

	public String getCreditItemsCount() {
		return creditItemsCount;
	}

	public void setCreditItemsCount(String creditItemsCount) {
		this.creditItemsCount = creditItemsCount;
	}
	
}
