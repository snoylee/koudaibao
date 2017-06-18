package com.kdkj.koudailicai.domain;

public class ShareInfo {
	String title;
	String desc;
	String url;
	String summary;
	String androidDownloadUrl;
	public ShareInfo(){
		super();
	}
	
	public ShareInfo(String title, String desc, String url, String summary,
			String androidDownloadUrl) {
		super();
		this.title = title;
		this.desc = desc;
		this.url = url;
		this.summary = summary;
		this.androidDownloadUrl = androidDownloadUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAndroidDownloadUrl() {
		return androidDownloadUrl;
	}

	public void setAndroidDownloadUrl(String androidDownloadUrl) {
		this.androidDownloadUrl = androidDownloadUrl;
	}
	
}
