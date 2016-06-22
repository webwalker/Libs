package com.webwalker.framework.http;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.webwalker.framework.utils.JsonUtil;
import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.utils.MessageUtil;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

/**
 * @author xujian
 * 
 */
public abstract class BaseVolleyHandler {
	protected Context context;
	protected String TAG = "VolleyHttp";

	public BaseVolleyHandler(Context c) {
		this.context = c;
	}

	protected abstract String getUrl(String url);

	protected MyJsonObjectRequest getRequest(String url, Object request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		return getRequest(url, request, null, listener, errorListener);
	}

	protected MyJsonObjectRequest getRequest(String url, Object request,
			Map<String, String> headers, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		JSONObject json = null;
		try {
			if (request != null) {
				if (request instanceof String) {
					json = new JSONObject(request.toString());
				} else if (request.getClass() instanceof Class) {
					json = new JSONObject(toJson(request));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getRequest(url, json, headers, listener, errorListener);
	}

	protected MyJsonObjectRequest getRequest(String url, JSONObject request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		return getRequest(url, request, null, listener, errorListener);
	}

	protected MyJsonObjectRequest getRequest(String url, JSONObject request,
			Map<String, String> headers, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		MyJsonObjectRequest req = new MyJsonObjectRequest(getUrl(url), request,
				listener, errorListener);
		if (headers != null)
			req.appendHeaders(headers);

		// request log
		StringBuilder sb = new StringBuilder();
		sb.append("http request:\r\n");
		sb.append("url:" + getUrl(url) + "\r\n");
		try {
			sb.append("headers:" + req.getHeaders().toString() + "\r\n");
		} catch (AuthFailureError e) {
			e.printStackTrace();
		}
		if (request != null)
			sb.append("request:" + request.toString());
		Loggers.d(TAG, sb.toString());

		return req;
	}

	public <T> void commit(Request<T> req) {
		BasicVolleyHttpClient.getInstance().addToRequestQueue(req);
	}

	protected <T> T fromJson(String json, boolean single) {
		if (json == null)
			return null;
		try {
			return JsonUtil.gson.fromJson(json, new TypeToken<T>() {
			}.getType());
		} catch (Exception e) {
		}
		return null;
	}

	protected String toJson(Object data) {
		try {
			return JsonUtil.gson.toJson(data);
		} catch (Exception e) {
		}
		return null;
	}

	protected void showHttpError(VolleyError error) {
		// MessageUtil.showShortToast(context, "出错了，请稍后重试。");
		error.printStackTrace();
	}

	protected void showHttpError(Exception error) {
		// MessageUtil.showShortToast(context, "出错了，请稍后重试。");
		error.printStackTrace();
	}
}
