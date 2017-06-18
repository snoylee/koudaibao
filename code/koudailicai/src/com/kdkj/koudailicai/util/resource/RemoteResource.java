package com.kdkj.koudailicai.util.resource;

import java.io.Serializable;

public class RemoteResource implements Serializable {
	
	public static final int STATUS_LOADING = 1;
	public static final int STATUS_READY = 2;
	public static final int STATUS_FAILED = 3;
	private String versionNo;
	private String path;
	
	private String url;
	
	private int status;
	
	private transient OnResourceReadyListener ready_listener = null;

	public RemoteResource(String versionNo, String path, String url, int status) {
		super();
		this.versionNo = versionNo;
		this.path = path;
		this.url = url;
		this.status = status;
	}

	public RemoteResource(String versionNo, String path, String url) {
		super();
		this.versionNo = versionNo;
		this.path = path;
		this.url = url;
		this.status = STATUS_LOADING;
	}

	public RemoteResource(String url) {
		super();
		this.url = url;
		this.status = STATUS_LOADING;
	}

	public RemoteResource() {
		super();
		this.status = STATUS_LOADING;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return status;
	}

	public synchronized void setStatus(int status) {
		this.status = status;
		if(this.isReady() && this.ready_listener != null) {
			this.ready_listener.onReady(this);
		}
	}
	
	public boolean isReady() {
		return this.status == STATUS_READY;
	}

	public synchronized void setOnResourceReadyListener(OnResourceReadyListener ready_listener) {
		this.ready_listener = ready_listener;
		if(this.isReady() && this.ready_listener != null) {
			this.ready_listener.onReady(this);
		}
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

}
