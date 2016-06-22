package com.webwalker.framework.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

/**
 * 
 * 判断是否按照QQ,UC浏览器的包工具类
 * 
 * @author webwalker
 */
public class BrowserUtil {
	public static final String UC = "com.uc.browser"; // UC浏览器
	public static final String QQ = "com.tencent.mtt"; // QQ浏览器
	public static final String OPERA = "com.opera.mini.android"; // Opera浏览器
	public static final String DOLPHIN = "mobi.mgeek.TunnyBrowser"; // Dolphin
																	// Browser(不支持WAP)
	public static final String SKYFIRE = "com.skyfire.browser"; // Skyfire
																// Browser(不支持WAP)
	public static final String STEEL = "com.kolbysoft.steel"; // Steel
																// Browser(不支持WAP)
	public static final String DEFAULT_BROWSER = "com.android.browser"; // 系统浏览器
	private Context context;
	private String visitUrl;

	public BrowserUtil(Context context, String visitUrl) {
		this.context = context;
		this.visitUrl = visitUrl;
	}

	public boolean isInstalled(String packageName) {
		// final PackageManager packageManager = context.getPackageManager();
		// final Intent intent = new Intent(intentActionName);
		// //检索所有可用于给定的意图进行的活动。如果没有匹配的活动，则返回一个空列表。
		// List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
		// PackageManager.MATCH_DEFAULT_ONLY);
		// return list.size() > 0;
		PackageManager packageMgr = context.getPackageManager();
		List<PackageInfo> list = packageMgr.getInstalledPackages(0);
		for (int i = 0; i < list.size(); i++) {
			PackageInfo info = list.get(i);
			String temp = info.packageName;
			if (temp.equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	public Intent getIntentByOrder() {
		if (isInstalled(UC)) {
			Log.i("PackageUtil", "获取到UC浏览器,开始UC浏览器");
			return this.getUCBrowser();
		} else if (isInstalled(QQ)) {
			Log.i("PackageUtil", "获取到QQ浏览器,开始QQ浏览器");
			return this.getQQBrowser();
		} else {
			Log.i("PackageUtil", "获取到默认浏览器,开始默认浏览器");
			return this.getDefault();
		}
	}

	public Intent getExtraBrowser() {
		if (isInstalled(UC)) {
			Log.i("PackageUtil", "获取到UC浏览器,开始UC浏览器");
			return this.getUCBrowser();
		} else if (isInstalled(QQ)) {
			Log.i("PackageUtil", "获取到QQ浏览器,开始QQ浏览器");
			return this.getQQBrowser();
		}
		return null;
	}

	public Intent getIntent() {
		Intent intent = null;
		PackageManager packageMgr = context.getPackageManager();
		List<PackageInfo> list = packageMgr.getInstalledPackages(0);
		for (int i = 0; i < list.size(); i++) {
			PackageInfo info = list.get(i);
			String temp = info.packageName;
			if (temp.equals("com.uc.browser")) {
				intent = this.getUCBrowser();
			} else if (temp.equals("com.tencent.mtt")) {
				intent = this.getQQBrowser();
			} else if (temp.equals("com.opera.mini.android")) {
				intent = this.getOperaBrowser();
			} else if (temp.equals("mobi.mgeek.TunnyBrowser")) {
				intent = this.getDolphinBrowser();
			} else if (temp.equals("com.skyfire.browser")) {
				intent = this.getSkyfireBrowser();
			} else if (temp.equals("com.kolbysoft.steel")) {
				intent = this.getSteelBrowser();
			} else if (temp.equals("com.android.browser")) {
				intent = this.getDefault();
			}
		}
		return intent;
	}

	private Intent gotoUrl(String packageName, String url,
			PackageManager packageMgr) {
		try {
			Intent intent;
			intent = packageMgr.getLaunchIntentForPackage(packageName);
			intent.setAction(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setData(Uri.parse(url));
			return intent;
		} catch (Exception e) {
			// 在1.5及以前版本会要求catch(android.content.pm.PackageManager.NameNotFoundException)异常，
			// 该异常在1.5以后版本已取消。
			e.printStackTrace();
			return null;
		}
	}

	public Intent getCustomerSelect() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(visitUrl));
		return intent;
	}

	public Intent getDefault() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(visitUrl));
		intent.setClassName("com.android.browser",
				"com.android.browser.BrowserActivity");
		return intent;
	}

	/** 直接启动UC，用于验证测试。 */
	private Intent getUCBrowser() {
		Intent intent = new Intent();
		intent.setClassName("com.uc.browser", "com.uc.browser.ActivityUpdate");
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse(visitUrl));
		return intent;
	}

	/** 直接启动QQ，用于验证测试。 */
	private Intent getQQBrowser() {
		Intent intent = new Intent();
		intent.setClassName("com.tencent.mtt", "com.tencent.mtt.MainActivity");
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse(visitUrl));
		return intent;
	}

	/** 直接启动Opera，用于验证测试。 */
	private Intent getOperaBrowser() {
		Intent intent = new Intent();
		intent.setClassName("com.opera.mini.android",
				"com.opera.mini.android.Browser");
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse(visitUrl));
		return intent;
	}

	/** 直接启动Dolphin Browser，用于验证测试。 */
	private Intent getDolphinBrowser() {
		// 方法一：
		// Intent intent = new Intent();
		// intent.setClassName("mobi.mgeek.TunnyBrowser",
		// "mobi.mgeek.TunnyBrowser.BrowserActivity");
		// intent.setAction(Intent.ACTION_VIEW);
		// intent.addCategory(Intent.CATEGORY_DEFAULT);
		// intent.setData(Uri.parse(visitUrl));
		// return intent;
		// 方法二：
		return gotoUrl("mobi.mgeek.TunnyBrowser", visitUrl,
				context.getPackageManager());
	}

	/** 直接启动Skyfire Browser，用于验证测试。 */
	private Intent getSkyfireBrowser() {
		// 方法一：
		Intent intent = new Intent();
		intent.setClassName("com.skyfire.browser",
				"com.skyfire.browser.core.Main");
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse(visitUrl));
		return intent;
		// 方法二：
		// gotoUrl("com.skyfire.browser", visitUrl, getPackageManager());
	}

	/** 直接启动Steel Browser，用于验证测试。 */
	private Intent getSteelBrowser() {
		// 方法一：
		// Intent intent = new Intent();
		// intent.setClassName("com.kolbysoft.steel",
		// "com.kolbysoft.steel.Steel");
		// intent.setAction(Intent.ACTION_VIEW);
		// intent.addCategory(Intent.CATEGORY_DEFAULT);
		// intent.setData(Uri.parse(visitUrl));
		// return intent;
		// 方法二：
		return gotoUrl("com.kolbysoft.steel", visitUrl,
				context.getPackageManager());
	}
}
