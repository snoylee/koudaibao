package com.kdkj.koudailicai.domain;

public class HoldingAssetRecord {
	private int id;
	private String totalHoldMoney;// 总持有资产
	private String kdbTotalMoney;// 口袋宝总额
	private String kdbTotalProfits;// 口袋宝总收益
	private String dueinCapital;// 待收本金
	private String dueinProfits;// 未结算收益
	
	public HoldingAssetRecord() {
		super();
	}
	
	public HoldingAssetRecord(int id, String totalHoldMoney,
			String kdbTotalMoney, String kdbTotalProfits, String dueinCapital,
			String dueinProfits) {
		super();
		this.id = id;
		this.totalHoldMoney = totalHoldMoney;
		this.kdbTotalMoney = kdbTotalMoney;
		this.kdbTotalProfits = kdbTotalProfits;
		this.dueinCapital = dueinCapital;
		this.dueinProfits = dueinProfits;
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



	public String getDueinCapital() {
		return dueinCapital;
	}



	public void setDueinCapital(String dueinCapital) {
		this.dueinCapital = dueinCapital;
	}



	public String getDueinProfits() {
		return dueinProfits;
	}



	public void setDueinProfits(String dueinProfits) {
		this.dueinProfits = dueinProfits;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
