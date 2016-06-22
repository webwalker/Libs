package com.webwalker.framework.widget.web;

import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.utils.NetworkUtils;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 处理视图渲染的基类
 * 
 * @author xu.jian
 * 
 */
public class BaseWebViewClient extends WebViewClient {
	private static final String TAG = "WebViewClient";
	public boolean hasLoadRes = false;
	private String url = null;
	public WebViewClientStatusListener statusListener = null;

	public int status = WEB_VIEW_CLIENT_STATUS_IDLE;
	public static final int WEB_VIEW_CLIENT_STATUS_IDLE = 0;
	public static final int WEB_VIEW_CLIENT_STATUS_CHECK_NETWORK = 1;
	public static final int WEB_VIEW_CLIENT_STATUS_LOADING = 2;
	public static final int WEB_VIEW_CLIENT_STATUS_LOADED = 3;
	public static final int WEB_VIEW_CLIENT_STATUS_ERR = 0xFF;

	public BaseWebViewClient() {
	}

	public interface WebViewClientStatusListener {
		void onWebViewClientStatus(String url, int status, int code);
	}

	public void setStatusListener(WebViewClientStatusListener listener) {
		statusListener = listener;
	}

	@Override
	// special redirect
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// open all urls in current window, set view.loadUrl and return true
		// default false, if true means handled

		// check invalid url via http://
		
		
		return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		Loggers.d(TAG, "onPageStarted(" + view + ", " + url + ")");

		// check status
		status = WEB_VIEW_CLIENT_STATUS_CHECK_NETWORK;
		reportStatus(url, status, 0);
		if (!NetworkUtils.isConnected(view.getContext())) {
			status = WEB_VIEW_CLIENT_STATUS_ERR;
			reportStatus(url, status, -1);
			view.stopLoading();
			return;
		}

		super.onPageStarted(view, url, favicon);

		// report status
		this.url = url;
		hasLoadRes = false;
		status = WEB_VIEW_CLIENT_STATUS_LOADING;
		reportStatus(url, status, 0);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		Loggers.d(TAG, "onPageFinished(" + view + ", " + url + ")");

		// 加快加载速度
		if (!view.getSettings().getLoadsImagesAutomatically()) {
			view.getSettings().setLoadsImagesAutomatically(true);
		}

		if (WEB_VIEW_CLIENT_STATUS_ERR == status)
			return;
		if (!hasLoadRes) {
			status = WEB_VIEW_CLIENT_STATUS_ERR;
			reportStatus(url, status, 0);
			return;
		}

		super.onPageFinished(view, url);
		status = WEB_VIEW_CLIENT_STATUS_LOADED;
		if (null != statusListener)
			statusListener.onWebViewClientStatus(url, status, 0);
	}

	@Override
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);
		hasLoadRes = true;
	}

	@Override
	// Bypasses and logs errors.
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		Loggers.d(TAG, "onReceivedError(" + view + ", " + errorCode + ", "
				+ description + ", " + failingUrl + ")");

		// 清除掉默认出错内容
		view.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);

		// 显示自定义错误
		status = WEB_VIEW_CLIENT_STATUS_ERR;
		if (null != statusListener)
			statusListener.onWebViewClientStatus(failingUrl, status, errorCode);
	}

	@Override
	// Ignores and logs SSL errors.
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
		Loggers.d(TAG, "onReceivedSslError : SSL error = " + error);
		handler.proceed();
	}

	public void reportStatus(String url, int status, int statusCode) {
		if (null != statusListener) {
			statusListener.onWebViewClientStatus(url, status, statusCode);
		}
	}

	// 返回当前URL
	public String getUrl() {
		return url;
	}

	public boolean isValid() {
		return status != WEB_VIEW_CLIENT_STATUS_ERR;
	}
}
