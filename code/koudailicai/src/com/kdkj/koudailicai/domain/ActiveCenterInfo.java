package com.kdkj.koudailicai.domain;

public class ActiveCenterInfo {

	private int id;// ID
    
	private String activeId;
	
	private String title;// 活动中心标题

	private String timeString;// 时间

	private String imageString;// 图片地址
    
	private String contentAbstract;//摘要
	public ActiveCenterInfo()
	{
		
	}
	public ActiveCenterInfo(String activeId,String imageString, String timeString,String contentAbstract,String title) {
		this.activeId=activeId;
		this.imageString = imageString;
		this.timeString = timeString;
		this.contentAbstract=contentAbstract;
		this.title = title;
	}
    
	public String getContentAbstract() {
		return contentAbstract;
	}

	public void setContentAbstract(String contentAbstract) {
		this.contentAbstract = contentAbstract;
	}

	public String getActiveId() {
		return activeId;
	}

	public void setActiveId(String activeId) {
		this.activeId = activeId;
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

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public String getImageString() {
		return imageString;
	}

	public void setImageString(String imageString) {
		this.imageString = imageString;
	}

}
