/**
 * 
 */
package com.webwalker.framework.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.net.Uri;

/**
 * 
 * @author webwalker
 */
public class ShortCutUtil {
	private final String INTENT_ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	private final String INTENT_ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
	private Activity context;

	public ShortCutUtil(Activity context) {
		this.context = context;
	}

	/*
	 * 为程序创建桌面快捷方式
	 */
	public void addShortcut(String appName, int iconId) {
		// 实例化具有安装快捷方式动作的Intent对象
		Intent shortcut = new Intent(this.INTENT_ACTION_INSTALL_SHORTCUT);
		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		shortcut.putExtra("duplicate", false); // 不允许重复创建

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
		ComponentName comp = new ComponentName(context.getPackageName(), "."
				+ context.getLocalClassName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));
		// 快捷方式的图标
		ShortcutIconResource iconRes = ShortcutIconResource.fromContext(
				context, iconId);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		// 发送创建快捷方式的广播
		context.sendBroadcast(shortcut);
	}

	public boolean hasShortCut(int appNameId) {
		// String url = "";
		// System.out.println(getSystemVersion());
		// if (getSystemVersion() < 8) {
		// url =
		// "content://com.android.launcher.settings/favorites?notify=true";
		// } else {
		// url =
		// "content://com.android.launcher2.settings/favorites?notify=true";
		// }
		// ContentResolver resolver = context.getContentResolver();
		// Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",
		// new String[] { context.getString(R.string.app_name) }, null);
		//
		// // if (cursor != null && cursor.moveToFirst()) {
		// // cursor.close();
		// // return true;
		// // }
		// if (cursor == null) {
		// // 注:
		// 2.1update和2.2版本的真机上测试无法访问com.android.launcher.settings，2.1update1的模拟器上可以
		// // ERROR/ActivityThread(1136): Failed to find provider info for
		// com.android.launcher.settings
		// return false;
		// }
		// while (cursor.moveToNext()) {
		// String intentstring =
		// cursor.getString(cursor.getColumnIndex("intent"));
		// if (intentstring == null) {
		// continue;
		// }
		// String componentString = getComponentString(intentstring);
		// if (componentString.startsWith(context.getPackageName())) {
		// return true;
		// }
		// }
		// return false;

		String url = "";
		System.out.println(getSystemVersion());
		if (getSystemVersion() < 8) {
			url = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			url = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",
				new String[] { context.getString(appNameId) }, null);

		if (cursor != null && cursor.moveToFirst()) {
			cursor.close();
			return true;
		}

		return false;
	}

	private static String getComponentString(String intentInfo) {
		// intent info
		// 的格式:intent=#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10200000;component=com.allstar.tanzhi/.activities.StartActivity;end
		int start = intentInfo.indexOf("component") + 9 + 1;
		int end = intentInfo.indexOf(";", start);
		return intentInfo.substring(start, end);
	}

	private static int getSystemVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/*
	 * 删除程序的快捷方式
	 */
	public void delShortcut() {
		Intent shortcut = new Intent(this.INTENT_ACTION_UNINSTALL_SHORTCUT);

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "盛付通");

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
		String appClass = context.getPackageName() + "."
				+ context.getLocalClassName();
		ComponentName comp = new ComponentName(context.getPackageName(),
				appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));
		// 发送卸载快捷方式的图标
		context.sendBroadcast(shortcut);

	}
}
