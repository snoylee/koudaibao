package com.kdkj.koudailicai.domain;

public class BankUserInfo {
	private int id;
	private String bank_id;
	private String bank_name;
	private String card_no;
	
	public BankUserInfo() {
		super();
	}
	
	public BankUserInfo(String bank_id, String bank_name, String card_no) {
		super();
		this.bank_id = bank_id;
		this.bank_name = bank_name;
		this.card_no = card_no;
	}
	
	public BankUserInfo(int id, String bank_id, String bank_name, String card_no) {
		super();
		this.id = id;
		this.bank_id = bank_id;
		this.bank_name = bank_name;
		this.card_no = card_no;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBank_id() {
		return bank_id;
	}
	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
}
