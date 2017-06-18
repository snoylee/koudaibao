package com.kdkj.koudailicai.lib.http;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.kdkj.koudailicai.util.Tool;
import com.kdkj.koudailicai.view.KDLCApplication;

public  class HttpRequestGet extends Request<JSONObject>{
    private Listener<JSONObject>  mListener;
    private Map<String, String> mHeaders=new HashMap<String, String>();

    public HttpRequestGet(String url,Listener<JSONObject> listener, ErrorListener errorListener) {
        super(Request.Method.GET, url, errorListener);
        if(!Tool.isBlank(KDLCApplication.app.getSessionId())) {
        	setCookie("SESSIONID="+KDLCApplication.app.getSessionId());
        }
        mListener=listener;
    }
    
    public void setCookie(String cookie) {
        mHeaders.put("Cookie", cookie);
    }
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
    	return mHeaders;
    }

   @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
            mListener.onResponse(response);  
    }

}