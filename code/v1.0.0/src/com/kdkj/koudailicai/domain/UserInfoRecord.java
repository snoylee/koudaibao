package com.kdkj.koudailicai.domain;

public class UserInfoRecord {
	private int id;
	private int uid;
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	private String realname;
	private String id_card;
	private String real_verify_status;
	private String card_bind_status;
	private String set_paypwd_status;
	private String is_novice;
	public UserInfoRecord()
	{
		
	}
	public UserInfoRecord(int id,int uid,String username,String realname,String id_card,
			String real_verify_status,String card_bind_status,String set_paypwd_status, String is_novice)
	{
		super();
		this.id=id;
		this.uid=uid;
		this.username=username;
		this.realname=realname;
		this.id_card=id_card;
		this.real_verify_status=real_verify_status;
		this.card_bind_status=card_bind_status;
		this.set_paypwd_status=set_paypwd_status;
		this.is_novice = is_novice;
	}

	public String getRealname() {
		return realname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getReal_verify_status() {
		return real_verify_status;
	}
	public void setReal_verify_status(String real_verify_status) {
		this.real_verify_status = real_verify_status;
	}
	public String getCard_bind_status() {
		return card_bind_status;
	}
	public void setCard_bind_status(String card_bind_status) {
		this.card_bind_status = card_bind_status;
	}
	public String getSet_paypwd_status() {
		return set_paypwd_status;
	}
	public void setSet_paypwd_status(String set_paypwd_status) {
		this.set_paypwd_status = set_paypwd_status;
	}
	public String getIs_novice() {
		return is_novice;
	}
	public void setIs_novice(String is_novice) {
		this.is_novice = is_novice;
	}

}
