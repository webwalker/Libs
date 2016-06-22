package com.webwalker.framework.cache;

import java.util.List;

/**
 * @author xujian
 * 
 */
public class CacheSetting {
	public static CacheAdapter getCacheAdapter(CacheType type) {
		switch (type) {
		case Memory:
			return MemCacheAdapter.getInstance();
		case File:
			return FileCacheAdapter.getInstance();
		default:
			break;
		}
		return MemCacheAdapter.getInstance();
	}

	private CacheType cacheType;
	// 缓存池大小, 超出缓存池，最小使用的则删除掉
	private static int cacheSize;

	private static List<CacheAdapter> adapters = null;

	public CacheType getCacheType() {
		return cacheType;
	}

	public void setCacheType(CacheType cacheType) {
		this.cacheType = cacheType;
	}

	public static int getCacheSize() {
		return cacheSize;
	}

	public static void setCacheSize(int size) {
		cacheSize = size;
	}

	public static List<CacheAdapter> getAdapters() {
		return adapters;
	}

	public synchronized static void addAdapters(CacheAdapter adapter) {
		if (!adapters.contains(adapters)) {
			adapters.add(adapter);
		}
	}
}
