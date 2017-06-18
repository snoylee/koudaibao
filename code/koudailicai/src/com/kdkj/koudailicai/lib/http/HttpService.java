package com.kdkj.koudailicai.lib.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.util.UrlHelper;

import android.content.Context;
import android.util.Log;

public abstract class HttpService extends AbstractService {
	public static Context context;
	public static enum Method {
		GET, POST
	}
	
	public static void setContext(Context context)
	{
		HttpService.context = context;
	}
	
	@Override
	public Response execute() throws Exception {
		// TODO Auto-generated method stub
		DefaultHttpClient hc = (DefaultHttpClient) MyHttpClient.getHttpClient(HttpService.context);
		HttpRequestBase request = this.getRequest();
		this.initRequest(hc, request);
		HttpResponse response = hc.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			Log.d("HttpService", "Service request success.");
			return this.parseResponse(this.getResponseContent(response));
		} else {
			throw new HttpException("Unexpected status code: "
					+ response.getStatusLine().getStatusCode());
		}
	}
	
	protected abstract Method getMethod();
	
	protected HttpRequestBase getRequest() throws UnsupportedEncodingException {
		if(this.getMethod() == Method.GET) {
			Log.d("HttpService", "url:"+UrlHelper.concatParam(this.getRequestUrl(), this.getParamString()));
			return new HttpGet(UrlHelper.concatParam(this.getRequestUrl(), this.getParamString()));
		} else {
			HttpPost request = new HttpPost(this.getRequestUrl());
			request.setEntity(new UrlEncodedFormEntity(this.getParams()));
			return request;
		}
	}

	protected void initRequest(DefaultHttpClient hc, HttpRequestBase request) {

	}
	
	protected String getResponseContent(HttpResponse response) throws  IOException {
		BufferedReader buff_reader = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		String response_content = "";
		String line = null;
		while ((line = buff_reader.readLine()) != null) {
			response_content += line;
		}
		Log.d("HttpService", "Response content: " + response_content);
		return response_content;
	}

	protected abstract String getRequestUrl();

	protected abstract List<NameValuePair> getParams();
	
	protected String getParamString() {
		List<NameValuePair> params = this.getParams();
		String result = "";
		
		if(params != null) {
			for(int i = 0; i < params.size(); i++) {
				NameValuePair p = params.get(i);
				if(i == 0) {
					result = p.getName() + "=" + p.getValue();
				} else {
					result += "&" + p.getName() + p.getValue();
				}
			}
		}
		
		return result;
	}

	protected abstract Response parseResponse(String content) throws Exception;

}
