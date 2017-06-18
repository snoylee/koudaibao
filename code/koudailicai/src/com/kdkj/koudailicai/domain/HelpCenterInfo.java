package com.kdkj.koudailicai.domain;

import android.R.integer;

public class HelpCenterInfo {

	private int id;
    
	private String helpId;//帮助中心ID
	
	private String title;//标题
	
	private String content;//内容
    
	public HelpCenterInfo() {
		super();
	}
	public HelpCenterInfo(String helpId,String title,String content) {
		// TODO Auto-generated constructor stub
		this.helpId=helpId;
		this.title=title;
		this.content=content;
	}
	
	public String getHelpId() {
		return helpId;
	}

	public void setHelpId(String helpId) {
		this.helpId = helpId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
