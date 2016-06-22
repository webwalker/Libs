package com.webwalker.framework.widget.web;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author xu.jian
 * 
 */
public class WebViewUtils {

	/**
	 * 初始化
	 * 
	 * @param wv
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public static WebSettings initWebView(WebView wv) {
		wv.setHorizontalScrollBarEnabled(true);
		wv.setVerticalScrollBarEnabled(true);
		wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		// wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		wv.setBackgroundColor(Color.TRANSPARENT);
		// mWebView.setBackgroundResource(Color.TRANSPARENT);
		if (wv.getBackground() != null) {
			wv.getBackground().setAlpha(2);
		}
		// mWebView.clearHistory();
		// mWebView.clearFormData();
		// mWebView.clearCache(true);

		WebSettings settings = wv.getSettings();
		setHttpCache(settings);
		delayLoadImages(settings);

		settings.setJavaScriptEnabled(true);
		// settings.setAllowFileAccess(true);

		// 让webview的宽度适应手机分辨率大屏的宽度
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);

		settings.setBuiltInZoomControls(false);
		// settings.setDisplayZoomControls(true);
		// settings.setSupportZoom(true);
		// settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		return settings;
	}

	// 设置HTTP访问缓存策略
	private static void setHttpCache(WebSettings settings) {
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		// set local storage
		settings.setDomStorageEnabled(true);
		// set app database
		settings.setDatabaseEnabled(true);
		// H5缓存
		settings.setAppCacheEnabled(true);
		// 可以不设采用默认的
		// settings.setAppCachePath("/data/data/[com.packagename]/cache");
	}

	// 避免图片过早加载进而影响CSS、JS加载，引起页面空白
	private static void delayLoadImages(WebSettings settings) {
		if (Build.VERSION.SDK_INT >= 19) {
			settings.setLoadsImagesAutomatically(true);
		} else {
			settings.setLoadsImagesAutomatically(false);
		}
	}

	// get user agent from settings
	// Mozilla/5.0 (Linux; U; Android 4.0.3; zh-cn; U8817 Build/MocorDroid4.0.3)
	// AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1
	public String getUserAgent(WebView wv) {
		return wv.getSettings().getUserAgentString();
	}

	// replace "android 4.0.3"?
	public void setUserAgent(WebView wv, String ua) {
		String agent = getUserAgent(wv);
		wv.getSettings().setUserAgentString(ua);
	}

	/**
	 * 截取webView可视区域的截图
	 * 
	 * @param webView
	 *            前提：WebView要设置webView.setDrawingCacheEnabled(true);
	 * @return
	 */
	public Bitmap captureWebViewVisibleSize(WebView webView) {
		Bitmap bmp = webView.getDrawingCache();
		return bmp;
	}

	/**
	 * 截取webView快照(webView加载的整个内容的大小)
	 * 
	 * @param webView
	 * @return
	 */
	public Bitmap captureWebView(WebView webView) {
		Picture snapShot = webView.capturePicture();

		Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),
				snapShot.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		snapShot.draw(canvas);
		return bmp;
	}

	/**
	 * 截屏
	 * 
	 * @param context
	 * @return
	 */
	public Bitmap captureScreen(Activity context) {
		View cv = context.getWindow().getDecorView();
		Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		cv.draw(canvas);
		return bmp;
	}

	// clear the cache before time numDays
	public static int clearCacheFolder(File dir, long numDays) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, numDays);
					}

					if (child.lastModified() < numDays) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	// 清除WebView数据库
	public static void clearWebView(Context c, File file) {
		try {
			if (file != null && file.exists() && file.isDirectory()) {
				for (File item : file.listFiles()) {
					item.delete();
				}
				file.delete();
			}
			c.deleteDatabase("webview.db");
			c.deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 模拟点击
	// http://stackoverflow.com/questions/24981032/android-webview-click-html-link-programatically
	public static void performClick(WebView wv, String id) {
		String js = "javascript:(function(){"
				+ "var ev = document.createEvent('MouseEvents');"
				+ "ev.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);"
				+ "document.getElementById('" + id
				+ "').dispatchEvent(ev);})()";
		wv.loadUrl(js);
	}
}
