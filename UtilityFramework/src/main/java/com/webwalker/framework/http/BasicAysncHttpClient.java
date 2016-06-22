package com.webwalker.framework.http;

import java.util.Map;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * AsyncHttpClient 封装
 * 
 * @author xu.jian
 * 
 */
public class BasicAysncHttpClient {
	/**
	 * 需要首先初始化设定
	 */
	public static String BASE_URL = "http://219.234.5.4:8080/NP";

	public void setBaseUrl(String url) {
		BASE_URL = url;
	}

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(Context context, String url,
			Map<String, String> headers,
			AsyncHttpResponseHandler responseHandler) {
		get(context, getAbsoluteUrl(url), null, null, responseHandler);
	}

	public static void get(Context context, String url, RequestParams params,
			Map<String, String> headers,
			AsyncHttpResponseHandler responseHandler) {
		for (String key : headers.keySet()) {
			client.addHeader(key, headers.get(key));
		}
		client.get(context, getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		post(context, url, params, null, responseHandler);
	}

	public static void post(Context context, String url, RequestParams params,
			Map<String, String> headers,
			AsyncHttpResponseHandler responseHandler) {
		for (String key : headers.keySet()) {
			client.addHeader(key, headers.get(key));
		}
		client.post(context, getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
