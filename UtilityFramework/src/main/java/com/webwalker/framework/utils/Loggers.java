package com.webwalker.framework.utils;

import android.util.Log;

/**
 * @author xu.jian
 * 
 */
public class Loggers {

	public static void d(String msg) {
		Log.d("", msg);
	}

	public static void e(String msg) {
		Log.e("", msg);
	}

	public static void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	public static void i(String tag, String msg) {
		Log.i(tag, msg);
	}

	public static void w(String msg) {
		Log.w("", msg);
	}

	public static void w(String tag, String msg) {
		Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}

	public static void e(String tag, Exception e) {
		Log.e(tag, e.getMessage());
	}
}
