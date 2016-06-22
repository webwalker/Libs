package com.webwalker.framework.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigUtils {
	private static final String TAG = "ConfigUtils";

	public static Properties getConfigProp(String file) {
		Properties pro = null;
		try {
			pro = getConfigProp(new FileInputStream(file));
		} catch (Throwable e) {
			Loggers.d(TAG, "fail to load config, because of " + e.toString());
		}
		return pro;
	}

	public static Properties getConfigProp(FileInputStream in) {
		Properties pro = new Properties();
		try {
			pro.load(in);
		} catch (Throwable e) {
			Loggers.d(TAG, "fail to load config, because of " + e.toString());
		}
		return pro;
	}

	public static String getString(Properties prop, String name,
			String defaulValue) {
		String result = defaulValue;

		if (prop != null) {
			Object object = prop.get(name);
			if (null == object) {
				Loggers.d(TAG, "get config value is null! param name:=" + name);
			} else {
				result = object.toString();
			}
		}
		return result;
	}

	public static boolean getBoolean(Properties prop, String name,
			boolean defaultValue) {
		int defaultInt = defaultValue ? 1 : 0;
		int value = getInt(prop, name, defaultInt, 10);
		return ((value != 0) ? true : false);
	}

	public static int getInt(Properties prop, String name, int defaultValue) {
		return getInt(prop, name, defaultValue, 10);
	}

	public static int getIntFromHex(Properties prop, String name,
			int defaultValue) {
		return getInt(prop, name, defaultValue, 16);
	}

	public static int getInt(Properties prop, String name, int defaultValue,
			int radix) {
		int result = defaultValue;
		if (null != prop) {
			Object object = prop.get(name);
			if (null == object) {
				// Logger.d(TAG, "get config value is null! param name:=" +
				// name);
			} else {
				try {
					// Logger.d(TAG, name + "=" + object.toString());
					result = Integer.parseInt(object.toString().trim(), radix);
				} catch (Exception e) {
					Loggers.d(TAG, "param is error :" + object.toString());
				}
			}
		}
		return result;
	}
}
