package com.webwalker.framework.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * @author xu.jian
 * 
 */
public class AppUtil {
	private static final String LOG_TAG = "PackageUtils";
	public static int apkVersion = 0;
	public static String apkVersionName = "";
	private static String deviceId;
	private Context context;

	public AppUtil(Context context) {
		this.context = context;
	}

	public static int getVersionCode(Context context) {
		if (apkVersion > 0)
			return apkVersion;
		try {
			String name = getPackageName(context);
			apkVersion = context.getPackageManager().getPackageInfo(name, 0).versionCode;
		} catch (NameNotFoundException e) {
			Loggers.e(LOG_TAG, e.getMessage());
		}
		return apkVersion;
	}

	public static List<String[]> getVersionCodes(Context context) {
		List<String[]> appList = new ArrayList<String[]>(); // 用来存储获取的应用信息数据

		List<PackageInfo> packages = context.getPackageManager()
				.getInstalledPackages(0);

		for (int i = 0; i < packages.size(); i++) {
			String[] tmpInfo = new String[5];
			PackageInfo packageInfo = packages.get(i);
			tmpInfo[0] = packageInfo.applicationInfo.loadLabel(
					context.getPackageManager()).toString();// .appName
			tmpInfo[1] = packageInfo.packageName;// .packageName
			tmpInfo[2] = packageInfo.versionName;// .versionName
			tmpInfo[3] = packageInfo.versionCode + "";// .versionCode
			appList.add(tmpInfo);

		}
		return appList;
	}

	public static String getVersionName(Context context) {
		if (!apkVersionName.equals(""))
			return apkVersionName;
		try {
			String name = getPackageName(context);
			apkVersionName = context.getPackageManager()
					.getPackageInfo(name, 0).versionName;
		} catch (NameNotFoundException e) {
			Loggers.e(LOG_TAG, e.getMessage());
		}
		return apkVersionName;
	}

	public static String getPackageName(Context context) {
		return context.getPackageName();
	}

	public static String getDeviceID(Context context) {
		try {
			String id = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
			if (id == null || id.equals("")) {
				id = Build.SERIAL;
			}
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getNewDeviceID(Context context) {

		if (deviceId == null) {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String tmDevice, tmSerial, tmPhone, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = ""
					+ Secure.getString(
							context.getContentResolver(),
							Secure.ANDROID_ID);

			UUID deviceUuid = new UUID(androidId.hashCode(),
					((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			deviceId = deviceUuid.toString();
			System.err.println(deviceId);
		}
		return deviceId;
	}

	/**
	 * 获取手机的IMEI值
	 * */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = null;
		try {
			imei = tm.getDeviceId();
		} catch (Exception e) {
		}
		return imei;
	}

	/**
	 * 获取手机的IMSI值
	 * */
	public static String getSubscriberId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = null;
		try {
			imsi = tm.getSubscriberId();
		} catch (Exception e) {
		}
		return imsi;
	}

	/**
	 * 获取手机型号
	 * */
	public static String getModel() {
		return Build.MODEL;
	}

	/**
	 * sdk 4 android 1.6
	 * */
	public static boolean hasDonut() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT;
	}

	/**
	 * sdk 5 android 2.0
	 * */
	public static boolean hasEclair() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	}

	/**
	 * sdk 7 android 2.1
	 * */
	public static boolean hasEclair_MR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1;
	}

	/**
	 * sdk 8 android 2.2
	 * */
	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * sdk 9 android 2.3
	 * */
	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * sdk 11 android 3.0
	 * */
	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * sdk 12 android 3.1
	 * */
	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/**
	 * sdk 16 android 4.1
	 * */
	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	/**
	 * sdk 17 android 4.2
	 * */
	public static boolean hasJellyBean_MR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
	}

	/**
	 * sdk 18 android 4.3
	 * */
	public static boolean hasJellyBean_MR2() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
	}

	/**
	 * sdk 19 android 4.4
	 * */
	public static boolean hasKitkat() {
		return Build.VERSION.SDK_INT >= 19;
	}

	public static String getFormatSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return String.format("%d B", size);
	}

	public static boolean isExistApp(Context context, String packageName) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo pInfo = packageManager.getPackageInfo(packageName,
					PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
			if (pInfo != null)
				return true;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 安装apk
	 */
	public static void installApk(Context context, String fileName) {
		try {
			File apkfile = new File(fileName);
			if (!apkfile.exists()) {
				return;
			}
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.fromFile(apkfile),
					"application/vnd.android.package-archive");
			context.startActivity(i);
		} catch (Exception e) {
		}
	}

	public static void installApks(Context context, String fileName) {
		try {
			Loggers.i(LOG_TAG, "installApk:" + fileName);
			File apkfile = new File(fileName);
			if (!apkfile.exists()) {
				return;
			}

			chmod("777", fileName);

			Intent i = new Intent(Intent.ACTION_VIEW);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setDataAndType(Uri.fromFile(apkfile),
					"application/vnd.android.package-archive");
			context.startActivity(i);
		} catch (Exception e) {
			Loggers.e(LOG_TAG, e);
		}
	}

	public static void startAppByPackageName(Context context, String packageName) {
		startAppByPackageName(context, packageName, null);
	}

	// 启动APP
	public static void startAppByPackageName(Context context,
			String packageName, Intent intent) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);

		List<ResolveInfo> apps = context.getPackageManager()
				.queryIntentActivities(resolveIntent, 0);

		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			String packageName1 = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;

			if (intent == null) {
				intent = new Intent(Intent.ACTION_MAIN);
			}
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			ComponentName cn = new ComponentName(packageName1, className);

			intent.setComponent(cn);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}

	}

	/**
	 * 杀进程
	 */
	public static boolean killBackgroundProcess(Context context, String pkgName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		boolean hasRunning = false;
		// 获得正在运行的所有进程
		List<ActivityManager.RunningAppProcessInfo> processes = am
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo info : processes) {
			if (info != null && info.processName != null
					&& info.processName.length() > 0
					&& info.processName.equals(pkgName)) {
				hasRunning = true;
			}
		}

		if (hasRunning && !(pkgName.startsWith("com.webwalker"))) {
			am.killBackgroundProcesses(pkgName);
			return hasRunning;
		}

		return hasRunning;
	}

	/**
	 * 杀掉所有后台进程
	 * 
	 * @param context
	 */
	void killAllProcess() {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processes = am
				.getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo info : processes) {
			if (info != null && info.processName != null
					&& info.processName.length() > 0) {
				String pkgName = info.processName;
				if (!("system".equals(pkgName)
						|| "android.process.media".equals(pkgName)
						|| "android.process.acore".equals(pkgName)
						|| "com.android.phone".equals(pkgName) || pkgName
							.startsWith("com.lefter"))) {
					try {
						am.killBackgroundProcesses(pkgName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static boolean isRooted() {
		// 检测是否ROOT过
		DataInputStream stream;
		boolean flag = false;
		try {
			stream = command("ls /data/");
			// 目录哪都行，不一定要需要ROOT权限的
			if (stream.readLine() != null)
				flag = true;
			// 根据是否有返回来判断是否有root权限
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}

		return flag;
	}

	public static boolean isInstalled(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		for (PackageInfo ai : pm.getInstalledPackages(0)) {
			if (packageName.equals(ai.packageName)) {
				return true;
			}
		}
		return false;
	}

	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有应用的<activity>、<receiver>、<service>节点信息等
	 * 
	 * @param context
	 * @return
	 */
	public static List<PackageInfo> getAppsInfo(PackageManager pm) {

		List<PackageInfo> pkgInfo = pm.getInstalledPackages(0);
		// for (PackageInfo info : pkgInfo) {
		// Log.d(TAG, "name : " + info.packageName);
		// }
		return pkgInfo;
	}

	/**
	 * 获取包信息
	 * 
	 * @param pm
	 * @param pkgName
	 * @return
	 */
	public static PackageInfo getAppPkgInfo(PackageManager pm, String pkgName) {
		PackageInfo pinfo = null;
		try {
			pinfo = pm.getPackageInfo(pkgName,
					PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// Log.e(TAG, "getAppPkgInfo", e);
		}
		return pinfo;
	}

	/**
	 * 获取apk文件的图标
	 * 
	 * @param context
	 * @param path
	 *            apk文件路径
	 * */
	public static Drawable getApkIcon(Context context, String path) {
		PackageManager manager = context.getPackageManager();
		PackageInfo packageInfo = manager.getPackageArchiveInfo(path,
				PackageManager.GET_ACTIVITIES);
		if (null != packageInfo) {
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			try {
				return manager.getApplicationIcon(appInfo);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 获取apk文件的图标
	public static Bitmap getAPKIcon(Context context, String apkPath) {
		String PATH_PackageParser = "android.content.pm.PackageParser";
		String PATH_AssetManager = "android.content.res.AssetManager";
		Drawable draw = null;
		Resources mResources = context.getResources();
		try {
			// apk包的文件路径
			// 这是一个Package 解释器, 是隐藏的
			// 构造函数的参数只有一个, apk文件的路径
			// PackageParser packageParser = new PackageParser(apkPath);
			Class<?>[] typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Constructor<?> pkgParserCt = Class.forName(PATH_PackageParser)
					.getConstructor(typeArgs);
			Object[] valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			Object pkgParser = pkgParserCt.newInstance(valueArgs);
			// 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();

			typeArgs = new Class[4];
			typeArgs[0] = File.class;
			typeArgs[1] = String.class;
			typeArgs[2] = DisplayMetrics.class;
			typeArgs[3] = Integer.TYPE;
			Method pkgParser_parsePackageMtd = Class
					.forName(PATH_PackageParser).getDeclaredMethod(
							"parsePackage", typeArgs);
			valueArgs = new Object[4];
			valueArgs[0] = new File(apkPath);
			valueArgs[1] = apkPath;
			valueArgs[2] = metrics;
			valueArgs[3] = 0;
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);
			// 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
			// ApplicationInfo info = mPkgInfo.applicationInfo;
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"applicationInfo");
			ApplicationInfo info = (ApplicationInfo) appInfoFld
					.get(pkgParserPkg);
			// uid 输出为"-1"，原因是未安装，系统未分配其Uid。

			Class<?> assetMagCls = Class.forName(PATH_AssetManager);
			Constructor<?> assetMagCt = assetMagCls
					.getConstructor((Class[]) null);
			Object assetMag = assetMagCt.newInstance((Object[]) null);
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
					"addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);

			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = mResources.getDisplayMetrics().getClass();
			typeArgs[2] = mResources.getConfiguration().getClass();
			Constructor<?> resCt = Resources.class.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = mResources.getDisplayMetrics();
			valueArgs[2] = mResources.getConfiguration();
			mResources = (Resources) resCt.newInstance(valueArgs);

			// 这里就是读取一个apk程序的图标
			if (info.icon != 0) {
				draw = mResources.getDrawable(info.icon);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == draw)
			return null;
		return ((BitmapDrawable) draw).getBitmap();
	}

	/**
	 * App是否在运行 需要权限<uses-permission android:name="android.permission.GET_TASKS"
	 * />
	 * 
	 * @param pkgName
	 * @param context
	 * @return
	 */
	public static boolean isAppRunning(String pkgName, Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = am.getRunningTasks(30);
		boolean isAppRunning = false;
		for (RunningTaskInfo info : runningTaskInfos) {
			// Log.d(TAG, info.topActivity.getPackageName());
			if (info.topActivity.getPackageName().equals(pkgName)
					|| info.baseActivity.getPackageName().equals(pkgName)) {
				isAppRunning = true;
				break;
			}
		}
		if (!isAppRunning) {
			List<RunningServiceInfo> runningSrvInfos = am
					.getRunningServices(30);
			for (RunningServiceInfo info : runningSrvInfos) {
				// Log.d(TAG, info.service.getPackageName());
				if (info.service.getPackageName().equals(pkgName)) {
					isAppRunning = true;
					break;
				}
			}
		}
		return isAppRunning;
	}

	public static String doExec(String cmd) {
		StringBuilder s = new StringBuilder();
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				s.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Loggers.d("doExec '" + cmd + "' return " + s);
		return s.toString();
	}

	public static Process execute(String cmd) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	public static Process execute(String[] cmd) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	public static Process execute(ArrayList<String> cmd) {
		return execute(cmd.toArray(new String[cmd.size()]));
	}

	static DataInputStream command(String command) throws Exception {
		Process process = Runtime.getRuntime().exec("su");
		// 执行到这，Superuser会跳出来，选择是否允许获取最高权限
		OutputStream outstream = process.getOutputStream();
		DataOutputStream DOPS = new DataOutputStream(outstream);
		InputStream instream = process.getInputStream();
		DataInputStream DIPS = new DataInputStream(instream);
		String temp = command + "\n";
		// 加回车
		DOPS.writeBytes(temp);
		// 执行
		DOPS.flush();
		// 刷新，确保都发送到outputstream
		DOPS.writeBytes("exit\n");
		// 退出
		DOPS.flush();
		process.waitFor();
		return DIPS;
	}

	public static String getAppPath(Context context) {
		String name = getPackageName(context);
		return "/data/data/" + name;
	}
}
