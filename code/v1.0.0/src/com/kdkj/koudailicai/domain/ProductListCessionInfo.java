package com.kdkj.koudailicai.domain;

import java.util.ArrayList;
import java.util.List;

public class ProductListCessionInfo {

	private int  id;
	private int infoType;
	private String accumulatedCount ;//累计转让比数
	private String accumulatedAmount ;//累计转让金额
	private List<CessionTradeInfo> dealList;
	private String cessionId;
	private String investId;
	private String projectType; // "1"
	private String assginFee ; // 转让价格
	private String assginRate; // 转让预期利率
	private String restDays;//剩余天数
	private String userName;//所属人
	private String oldId;//"11"
	private String apr; // 项目原的利率
	private String name; // 项目名称
	private String noCession;
	
	public ProductListCessionInfo() {
		super();
		dealList = new ArrayList<CessionTradeInfo>();
	}
	
	public ProductListCessionInfo(int infoType, String accumulatedCount, String accumulatedAmount) {
		super();
		dealList = new ArrayList<CessionTradeInfo>();
		this.accumulatedCount = accumulatedCount;
		this.accumulatedAmount = accumulatedAmount;
		this.infoType = infoType;
	}
	
	public ProductListCessionInfo(int infoType) {
		super();
		dealList = new ArrayList<CessionTradeInfo>();
		this.infoType = infoType;
		// TODO Auto-generated constructor stub
	}
	
	public void addDealInfo(CessionTradeInfo detailRecordInfo) {
		dealList.add(detailRecordInfo);
	}
	
	public ProductListCessionInfo(int infoType, String cessionId, String investId, String projectType,
								  String assginFee, String assginRate, String restDays, String oldId,
								  String apr, String name,String useName) {
		super();
		dealList = new ArrayList<CessionTradeInfo>();
		this.infoType = infoType;
		this.investId = investId;
		this.cessionId = cessionId;
		this.projectType = projectType;
		this.assginFee = assginFee;
		this.assginRate = assginRate;
		this.restDays = restDays;
		this.oldId = oldId;
		this.apr = apr;
		this.name = name;
		this.userName = useName;
	}	

	public List<CessionTradeInfo> getDealList() {
		return dealList;
	}
	public void setDealList(List<CessionTradeInfo> dealList) {
		this.dealList = dealList;
	}
	public String getAccumulatedCount() {
		return accumulatedCount;
	}
	public void setAccumulatedCount(String accumulatedCount) {
		this.accumulatedCount = accumulatedCount;
	}
	public String getAccumulatedAmount() {
		return accumulatedAmount;
	}
	public void setAccumulatedAmount(String accumulatedAmount) {
		this.accumulatedAmount = accumulatedAmount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getAssginFee() {
		return assginFee;
	}

	public void setAssginFee(String assginFee) {
		this.assginFee = assginFee;
	}

	public String getAssginRate() {
		return assginRate;
	}

	public void setAssginRate(String assginRate) {
		this.assginRate = assginRate;
	}

	public String getRestDays() {
		return restDays;
	}

	public void setRestDays(String restDays) {
		this.restDays = restDays;
	}

	public String getApr() {
		return apr;
	}

	public void setApr(String apr) {
		this.apr = apr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInfoType() {
		return infoType;
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

	public String getCessionId() {
		return cessionId;
	}

	public void setCessionId(String cessionId) {
		this.cessionId = cessionId;
	}
	
	public String getInvestId() {
		return investId;
	}

	public void setInvestId(String investId) {
		this.investId = investId;
	}
	
	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	public String getNoCession() {
		return noCession;
	}

	public void setNoCession(String noCession) {
		this.noCession = noCession;
	}

}
