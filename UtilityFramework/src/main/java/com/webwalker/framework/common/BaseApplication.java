package com.webwalker.framework.common;

import java.lang.Thread.UncaughtExceptionHandler;

import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.utils.MessageUtil;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

/**
 * @author xu.jian
 * 
 */
public class BaseApplication extends Application {
	private static Context context = null;
	private String TAG = "";
	private Class rebootActivity;

	public BaseApplication() {
	}

	public BaseApplication(Context c) {
		context = c;
	}

	public Context getContext() {
		if (context == null)
			return getApplicationContext();
		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	protected void initUncaughtHandler(String tag, Class c) {
		this.TAG = tag;
		this.rebootActivity = c;
		Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程
	}

	public UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Loggers.d(TAG, ex.getMessage());
			MessageUtil.showShortToast(getApplicationContext(),
					"很抱歉，程序出现异常，即将退出并重启.");

			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Intent intent = new Intent(getApplicationContext(), rebootActivity);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);// 异常退出
		}
	};
}
