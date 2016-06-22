package com.webwalker.framework.system.support;

import com.webwalker.framework.interfaces.ICallback;
import com.webwalker.framework.utils.AppUtil;
import com.webwalker.framework.utils.Loggers;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <p>
 * {@code <receiver android:name=".BootReceiver" android:label="@string/app_name">   
            <intent-filter>  
             <action android:name="android.intent.action.PACKAGE_ADDED" /> 
             <action android:name="android.intent.action.PACKAGE_REMOVED" />  
              <data android:scheme="package" />  
            </intent-filter>  
    </receiver>  
    <uses-permission android:name="android.permission.RESTART_PACKAGES"/>  
    }
 * </p>
 * 
 * 监听应用包安装、重新安装、移除等操作
 * 
 * @author xu.jian
 * 
 */
public class PackageReceiver extends BroadcastReceiver {

	private static ICallback<PackageActionType> callback = null;

	public static void setCallback(ICallback<PackageActionType> c) {
		callback = c;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String receivePackageName = intent.getDataString();
		String appPackageName = AppUtil.getPackageName(context);
		String action = intent.getAction().toUpperCase();

		Loggers.d("package action, app package:" + appPackageName
				+ "receive package:" + receivePackageName + ", receive action:"
				+ action);
		if (!appPackageName.equals(receivePackageName))
			return;

		PackageActionType type = PackageActionType.getByValue(action);
		if (callback != null)
			callback.action(type);

		// setResultData(getResultData());
	}
}
