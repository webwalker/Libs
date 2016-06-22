package com.webwalker.framework.widget.web;

import com.webwalker.framework.utils.Loggers;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;

/**
 * 封装WebView，用以解决CPU占用较高的问题
 * 
 * @author xu.jian
 * 
 */
public class MyWebView extends WebView {
	private boolean is_gone = false;
	public static boolean isPause = false;
	private static final String TAG = "MyWebView";

	public MyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyWebView(Context context) {
		super(context);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onPause() {
		super.onPause();
		isPause = true;
		Loggers.d(TAG, "onPause");
	}

	@Override
	// download listener and handler
	public void setDownloadListener(DownloadListener listener) {
		super.setDownloadListener(listener);
	}

	@Override
	// if back pressed, this won't invoked, need to handle activity onOnKeyDown
	// pass to webview onKeyDown
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// examples
		// if (keyCode == KeyEvent.KEYCODE_BACK && this.canGoBack()) {
		// this.goBack();
		// return true;
		// }
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == View.GONE) {
			try {
				WebView.class.getMethod("onPause").invoke(this);// stop flash
			} catch (Exception e) {
			}
			Loggers.d(TAG, "pause webview timers.");
			this.pauseTimers();
			this.is_gone = true;
		} else if (visibility == View.VISIBLE) {
			try {
				WebView.class.getMethod("onResume").invoke(this);// resume flash
			} catch (Exception e) {
			}
			Loggers.d(TAG, "resume webview timers.");
			this.resumeTimers();
			this.is_gone = false;
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		if (this.is_gone) {
			try {
				this.destroy();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 是否可加载下一页
	 * 
	 * @return
	 */
	public boolean existVerticalScrollbar() {
		return computeVerticalScrollRange() > computeVerticalScrollExtent();
	}
}
