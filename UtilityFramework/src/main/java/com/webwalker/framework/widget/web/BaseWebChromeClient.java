package com.webwalker.framework.widget.web;

import com.webwalker.framework.utils.Loggers;
import android.os.Message;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * 处理事件交互的基类
 * 
 * @author xu.jian
 * 
 */
public class BaseWebChromeClient extends WebChromeClient {
	private static final String TAG = "WebChromeClient";
	public WebChromeClientProgListener progressListener = null;

	public interface WebChromeClientProgListener {
		void onWebChromeClientProg(int progress);
	}

	public void setProgListener(WebChromeClientProgListener listener) {
		progressListener = listener;
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		// Loggers.d(TAG, "enter onProgressChanged(" + view + ", " + newProgress
		// +
		// ").");
		super.onProgressChanged(view, newProgress);
		if (null != progressListener) {
			progressListener.onWebChromeClientProg(newProgress);
		}
		// Loggers.d(TAG, "leave onProgressChanged.");
	}

	@Override
	public void onGeolocationPermissionsShowPrompt(String origin,
			Callback callback) {
		super.onGeolocationPermissionsShowPrompt(origin, callback);
	}

	@Override
	// prompt if get location premised.
	public boolean onCreateWindow(WebView view, boolean isDialog,
			boolean isUserGesture, Message resultMsg) {
		return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
	}

	@Override
	public boolean onConsoleMessage(ConsoleMessage cm) {
		String message = cm.message();
		int lineNumber = cm.lineNumber();
		String sourceID = cm.sourceId();
		String messageLevel = cm.message();

		Loggers.d(TAG, String.format(
				"[%s] sourceID: %s lineNumber: %n message: %s", messageLevel,
				sourceID, lineNumber, message));
		return super.onConsoleMessage(cm);
	}
}
