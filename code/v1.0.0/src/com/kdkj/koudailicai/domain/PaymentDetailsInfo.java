package com.kdkj.koudailicai.domain;

public class PaymentDetailsInfo {
	/*"id": "223",
    "money": "710000",
    "tag": "+",
    "type": "10",
    "title": "债权转让余额收入",
    "created_at": "1417441426"*/
	private int id;
	private String money;
	private String tag;
	private String type;
	private String title;
	private String createdAt;
	public PaymentDetailsInfo() {
		super();
	}
	public PaymentDetailsInfo(String title,String createdAt,String tag,String money) {
		this.title = title;
		this.createdAt = createdAt;
		this.tag = tag;
		this.money = money;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
}
