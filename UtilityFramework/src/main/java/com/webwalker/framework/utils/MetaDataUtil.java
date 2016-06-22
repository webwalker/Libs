package com.webwalker.framework.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;

/**
 * Application MetaData数据获取
 * 
 * @author xujian
 */
public class MetaDataUtil {
	public final String APPLICATION_CHANNEL = "APPLICATION_CHANNEL";
	public final String APPLICATION_ID = "APPLICATION_ID";
	private Activity context;

	public MetaDataUtil(Activity context) {
		this.context = context;
	}

	public String getApplicationMetaData(String key) {
		String metaDataValue = null;
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			metaDataValue = appInfo.metaData.getString(key);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return metaDataValue;
	}

	public String getActivityMetaData(String key) {
		String metaDataValue = null;
		try {
			ActivityInfo info = context.getPackageManager().getActivityInfo(
					context.getComponentName(), PackageManager.GET_META_DATA);
			metaDataValue = info.metaData.getString(key);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return metaDataValue;
	}

	public String getServiceMetaData(Class<?> cls, String key) {
		String metaDataValue = null;
		try {
			ComponentName cn = new ComponentName(context, cls);
			ServiceInfo info = context.getPackageManager().getServiceInfo(cn,
					PackageManager.GET_META_DATA);
			metaDataValue = info.metaData.getString(key);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return metaDataValue;
	}

	public String getReceiverMetaData(Class<?> cls, String key) {
		String metaDataValue = null;
		try {
			ComponentName cn = new ComponentName(context, cls);
			ActivityInfo info = context.getPackageManager().getReceiverInfo(cn,
					PackageManager.GET_META_DATA);
			metaDataValue = info.metaData.getString(key);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return metaDataValue;
	}
}
