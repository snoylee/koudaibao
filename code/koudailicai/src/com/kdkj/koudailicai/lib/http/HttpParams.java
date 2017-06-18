/*
 * Created by Storm Zhang, Feb 13, 2014.
 */

package com.kdkj.koudailicai.lib.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.http.client.utils.URLEncodedUtils;

import com.kdkj.koudailicai.util.UrlHelper;

public class HttpParams extends LinkedHashMap<String, String> {
	private static final long serialVersionUID = 8112047472727256876L;

	public HttpParams add(String key, String value) {
		put(key, value);
		return this;
	}
	
	public String toString() {
		String str = "";
		for (Entry<String, String> entry: entrySet()) {
		    try {
				str += entry.getKey()+"="+URLEncoder.encode(entry.getValue(), "utf-8")+"&";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(str.length() > 0)
			str = str.substring(0, str.length()-1);
		return str;
	}
}
