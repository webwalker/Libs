package com.webwalker.framework.utils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.webwalker.framework.cache.CacheItem;
import android.content.Context;

/**
 * manage cache
 * 
 * @author xu.jian
 * 
 */
public class CacheUtil {

	protected static final Map map = Collections.synchronizedMap(new HashMap());
	private static Context context = null;

	private CacheUtil() {
	}

	public static void setContext(Context c) {
		context = c;
	}

	public static Context getContext() {
		return context;
	}

	public synchronized static void set(String key, Object value) {
		if (key != null && value != null) {
			map.put(key, value);
		} else {
			new IllegalArgumentException(
					"null pointer value for key or value via Add(String key, Object value)");
		}
	}

	public synchronized static void setItem(String key, Object value) {
		if (key != null && value != null) {
			CacheItem ci = new CacheItem(key, value, new Date().getTime());
			map.put(key, ci);
		}
	}

	public static Object get(String key) {
		if (key != null) {
			return map.get(key);
		} else {
			new IllegalArgumentException(
					"null pointer value for key via get(String key)");
		}
		return null;
	}

	public static Object getItem(String key) {
		if (isExpired(key))
			return null;
		CacheItem ci = (CacheItem) map.get(key);
		return ci.getValue();
	}

	public static String getString(String key) {
		if (key != null) {
			return map.get(key).toString();
		} else {
			new IllegalArgumentException(
					"null pointer value for key via get(String key)");
		}
		return null;
	}

	public static int getInt(String key) {
		return Integer.valueOf(getString(key));
	}

	public synchronized static Object remove(String key) {
		return map.remove(key);
	}

	public static void clear() {
		map.clear();
	}

	public static boolean isExpired(String key) {
		if (!contains(key))
			return true;
		CacheItem item = (CacheItem) map.get(key);
		if (item == null)
			return true;
		long cache_Interval = new Date().getTime()
				- (item.getExpiredTime() + 5 * 60 * 1000); // 统一5分钟过期
		if (cache_Interval > 0)
			return true;
		return false;
	}

	public static boolean contains(String key) {
		return map.containsKey(key);
	}
}