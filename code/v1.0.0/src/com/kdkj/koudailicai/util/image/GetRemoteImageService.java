package com.kdkj.koudailicai.util.image;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.kdkj.koudailicai.lib.http.AbstractService;
import com.kdkj.koudailicai.lib.http.HttpService;

public class GetRemoteImageService extends HttpService{
	
	private String url;
	private String configVersion;
	
	
	public static class ImageResponse extends AbstractService.Response {
		private String imgUrl;
		private String curConfVersion;
		private boolean isNew = false;
		public boolean isNew() {
			return isNew;
		}
		public String getUrl() {
			return imgUrl;
		}
		public String getVersion() {
			return curConfVersion;
		}
	}
	
	public GetRemoteImageService(String url, String configVersion) {
		this.url = url;
		this.configVersion = configVersion;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getConfigVersion() {
		return configVersion;
	}
	
	public void setConfigVersion(String configVersion) {
		this.configVersion = configVersion;
	}
	
	@Override
	public Response execute() throws Exception {
		// TODO Auto-generated method stub
		return (Response) super.execute();
	}
	
	@Override
	protected Method getMethod() {
		// TODO Auto-generated method stub
		return Method.GET;
	}

	@Override
	protected String getRequestUrl() {
		// TODO Auto-generated method stub
		return this.url;
	}

	@Override
	protected List<NameValuePair> getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Response parseResponse(String content) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Log.d("Ser","Parse Image Response Start");
		ImageResponse resp = new ImageResponse();
		try{
			JSONObject confRoot = new JSONObject(content);
			resp.imgUrl = confRoot.getString("url");
			resp.curConfVersion = confRoot.getString("version");
			if(!this.configVersion.equals(confRoot.getString("version"))) {
				resp.isNew = true;
				Log.d("Ser", "is new");
			} else {
				resp.isNew = false;
				Log.d("Ser", "is not new");
			}
		}catch(Exception e) {
			resp.isNew = false;
			e.printStackTrace();
		}
		Log.d("Ser","Parse Image Response End");
		return resp;
	}
	
}
