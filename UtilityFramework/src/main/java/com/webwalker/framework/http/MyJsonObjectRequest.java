package com.webwalker.framework.http;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.webwalker.framework.utils.Loggers;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * 可自定义请求
 * 
 * @author xujian
 * 
 */
public class MyJsonObjectRequest extends JsonObjectRequest {
	private final static String TAG = "VolleyHttp";
	private Map<String, String> headers;

	public void appendHeaders(Map<String, String> map) {
		headers = buildHeaders();
		headers.putAll(map);
	}

	public MyJsonObjectRequest(String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
		Loggers.d(TAG, url + "\r\n" + jsonRequest);
		this.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return buildHeaders();
	}

	private Map<String, String> buildHeaders() {
		if (headers == null) {
			headers = new HashMap<String, String>();
			headers.put("Accept", "application/json");
			// map.put("Content-Type", "application/json;charset=utf-8");
		}
		return headers;
	}
}
