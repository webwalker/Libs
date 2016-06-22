package com.webwalker.framework.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * cache core handler
 * 
 * @author xujian
 * 
 */
class CacheCore {

	// Cache table
	protected static final Map map = Collections.synchronizedMap(new HashMap());

	// 防止在外部实例化
	private CacheCore() {
	}

	public synchronized static void set(String key, CacheItem item) {

		// 最少使用的删除掉
		if (CacheSetting.getCacheSize() > 0
				&& map.size() <= CacheSetting.getCacheSize()) {
			// map.put(key, item);
		}

		map.put(key, item);
	}

	public static CacheItem get(String key) {
		if (isExpired(key))
			return null;
		return (CacheItem) map.get(key);
	}

	public static List<CacheItem> getAll() {

		List<CacheItem> items = new ArrayList<CacheItem>();
		for (Object item : map.values()) {
			CacheItem ci = (CacheItem) item;
			items.add(ci);
		}

		return items;
	}

	public static boolean isExpired(String key) {
		if (contains(key)) {
			CacheItem item = (CacheItem) map.get(key);
			if (item.getExpiredTime() != 0) {
				long now = item.getExpiredTime() - new Date().getTime();
				if (item == null || now <= 0) {
					remove(key);
					return true;
				}
			}
		}

		return false;
	}

	public static void remove(String key) {
		map.remove(key);
	}

	public static boolean contains(String key) {
		return map.containsKey(key);
	}

	public static void flush() {
		map.clear();
	}

	public static int count() {
		return map.size();
	}

	/**
	 * 内存大小
	 * 
	 * @return
	 */
	public static String memorySize() {
		return "";
	}
}
