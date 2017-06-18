package com.kdkj.koudailicai.domain;

public class BankSupportInfo {
	private int id;
	private String code;
	private String name;
	
	public BankSupportInfo() {
		super();
	}
	
	public BankSupportInfo(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}


	public BankSupportInfo(int id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
    @Override
    public String toString()
    {
        return code + ":" + name;
    }
	
}
