package com.webwalker.framework.system.log;

import java.util.ArrayList;

import com.webwalker.framework.utils.AppUtil;
import com.webwalker.framework.utils.DateUtil;
import com.webwalker.framework.utils.FileUtil;
import com.webwalker.framework.utils.MessageUtil;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 开发调试日志辅助，也可借鉴开源项目实时查看
 * 
 * @author xu.jian
 * 
 */
public class LogcatUtil {
	private static String logPath = "";
	private static Process mProcess = null;

	public static void su() {
		AppUtil.execute("su");
	}

	public static void clear() {
		AppUtil.execute("logcat -c");
	}

	public static void start(Context context) {
		su();
		clear();
		stopLog();
		// avoid log cache memory overflow
		FileUtil.deleteDir(getLogPath(context));
		startLog(context, "");
	}

	public static void start(Context context, String tag) {
		su();
		clear();
		stopLog();
		startLog(context, tag);
	}

	public static void showActLogs(Context context) {
		Intent it = new Intent(context, DebugActivity.class);
		it.putExtra("path", logPath);
		context.startActivity(it);
		stopLog();
	}

	public static void showLogs(Context context, String tag, View v) {
		String logs = FileUtil.readFile(logPath);
		if (TextUtils.isEmpty(logs)) {
			MessageUtil.showShortToast(context, "获取控制台日志信息错误!");
			return;
		}

		if (v instanceof TextView) {
			TextView tv = (TextView) v;
			tv.setText(logs);
		}
		if (v instanceof EditText) {
			EditText et = (EditText) v;
			et.setText(logs);
		}
		stopLog();
	}

	private static String startLog(Context context, String tag) {
		logPath = getLogFileName(context);
		try {
			if (!FileUtil.fileExisted(logPath))
				FileUtil.createDirIfNotExist(logPath);
			ArrayList<String> cmd = new ArrayList<String>();
			cmd.add("logcat");
			cmd.add("-v");
			cmd.add("time");
			if (!TextUtils.isEmpty(tag))
				cmd.add("-s " + tag);
			cmd.add("-f");
			cmd.add(logPath);
			mProcess = AppUtil.execute(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logPath;
	}

	public static void stopLog() {
		if (null != mProcess) {
			mProcess.destroy();
			mProcess = null;
		}
	}

	private static String getLogPath(Context context) {
		String logPath = AppUtil.getAppPath(context) + "/logs/";
		return logPath;
	}

	private static String getLogFileName(Context context) {
		return getLogPath(context) + DateUtil.getCurrentyyyyMMddHHmmss()
				+ ".log";
	}

}
