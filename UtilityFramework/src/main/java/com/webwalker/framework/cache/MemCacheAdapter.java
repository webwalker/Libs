package com.webwalker.framework.cache;

/**
 * @author xujian
 * 
 */
public class MemCacheAdapter extends CacheAdapter {

	private static MemCacheAdapter instance;

	public MemCacheAdapter() {
	}

	public static MemCacheAdapter getInstance() {
		if (instance == null) {
			instance = new MemCacheAdapter();
		}
		return instance;
	}

	@Override
	public void set(CacheItem item) {
		CacheCore.set(item.getKey(), item);
	}

	@Override
	public CacheItem get(String key) {
		return CacheCore.get(key);
	}

	@Override
	public Object get(CacheItem item) {
		return CacheCore.get(item.getKey());
	}

	@Override
	public void remove(String key) {
		CacheCore.remove(key);
	}

	@Override
	public void flush() {
		CacheCore.flush();
	}
}
