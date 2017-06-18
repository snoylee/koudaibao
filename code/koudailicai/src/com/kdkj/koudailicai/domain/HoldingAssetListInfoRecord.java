package com.kdkj.koudailicai.domain;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class HoldingAssetListInfoRecord {

	private int id;
	private int    infoType;
	private String totalHoldMoney;// 总持有资产
	private String kdbTotalMoney;// 口袋宝总额
	private String kdbTotalProfits;// 口袋宝总收益
	private String kdbDueinCapital;// 待收本金
	private String kdbDueinProfits;// 未结算收益
	private String kdbInvestingMoney;//申购冻结
	private String projectId;
	private String investId;
	private String projectName;
	private String projectApr;
	private String dueinCapiTal;
	private String dueinProfits;
	private String inerestAtartDate;
	private String lastRepayDate; 
	private String isTransfer; 
	private String status;
	private String statusLabel;
	private String btnType;
	
	public HoldingAssetListInfoRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HoldingAssetListInfoRecord(String totalHoldMoney,
			String kdbTotalMoney, String kdbTotalProfits,
			String kdbDueinCapital, String kdbDueinProfits, String kdbInvestingMoney) {
		super();
		this.infoType = 1;
		this.totalHoldMoney = totalHoldMoney;
		this.kdbTotalMoney = kdbTotalMoney;
		this.kdbTotalProfits = kdbTotalProfits;
		this.kdbDueinCapital = kdbDueinCapital;
		this.kdbDueinProfits = kdbDueinProfits;
		this.kdbInvestingMoney = kdbInvestingMoney;
	}

	public HoldingAssetListInfoRecord(String projectId, String investId,
			String projectName, String projectApr, String dueinCapiTal,
			String dueinProfits, String inerestAtartDate, String lastRepayDate,
			String isTransfer, String status, String statusLabel, String btnType) {
		super();
		this.infoType = 2;
		this.projectId = projectId;
		this.investId = investId;
		this.projectName = projectName;
		this.projectApr = projectApr;
		this.dueinCapiTal = dueinCapiTal;
		this.dueinProfits = dueinProfits;
		this.inerestAtartDate = inerestAtartDate;
		this.lastRepayDate = lastRepayDate;
		this.isTransfer = isTransfer;
		this.status = status;
		this.statusLabel = statusLabel;
		this.btnType = btnType;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTotalHoldMoney() {
		return totalHoldMoney;
	}
	public void setTotalHoldMoney(String totalHoldMoney) {
		this.totalHoldMoney = totalHoldMoney;
	}
	public String getKdbTotalMoney() {
		return kdbTotalMoney;
	}
	public void setKdbTotalMoney(String kdbTotalMoney) {
		this.kdbTotalMoney = kdbTotalMoney;
	}
	public String getKdbTotalProfits() {
		return kdbTotalProfits;
	}
	public void setKdbTotalProfits(String kdbTotalProfits) {
		this.kdbTotalProfits = kdbTotalProfits;
	}
	public String getKdbDueinCapital() {
		return kdbDueinCapital;
	}
	public void setKdbDueinCapital(String kdbDueinCapital) {
		this.kdbDueinCapital = kdbDueinCapital;
	}
	public String getKdbDueinProfits() {
		return kdbDueinProfits;
	}
	public void setKdbDueinProfits(String kdbDueinProfits) {
		this.kdbDueinProfits = kdbDueinProfits;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getInvestId() {
		return investId;
	}
	public void setInvestId(String investId) {
		this.investId = investId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectApr() {
		return projectApr;
	}
	public void setProjectApr(String projectApr) {
		this.projectApr = projectApr;
	}
	public String getDueinCapiTal() {
		return dueinCapiTal;
	}
	public void setDueinCapiTal(String dueinCapiTal) {
		this.dueinCapiTal = dueinCapiTal;
	}
	public String getDueinProfits() {
		return dueinProfits;
	}
	public void setDueinProfits(String dueinProfits) {
		this.dueinProfits = dueinProfits;
	}
	public String getInerestAtartDate() {
		return inerestAtartDate;
	}
	public void setInerestAtartDate(String inerestAtartDate) {
		this.inerestAtartDate = inerestAtartDate;
	}
	public String getLastRepayDate() {
		return lastRepayDate;
	}
	public void setLastRepayDate(String lastRepayDate) {
		this.lastRepayDate = lastRepayDate;
	}
	public String getIsTransfer() {
		return isTransfer;
	}
	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}


	public int getInfoType() {
		return infoType;
	}


	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

	public String getBtnType() {
		return btnType;
	}

	public void setBtnType(String btnType) {
		this.btnType = btnType;
	}

	public String getKdbInvestingMoney() {
		return kdbInvestingMoney;
	}

	public void setKdbInvestingMoney(String kdbInvestingMoney) {
		this.kdbInvestingMoney = kdbInvestingMoney;
	}
}
