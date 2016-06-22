package com.webwalker.framework.cache;

/**
 * {@code
 * 
	AppCache<String> cacher = new AppCache<String>(CacheType.File);
	String data = JsonUtils.gson.toJson(result);
	cacher.set(new FileCacheItem(Values.Cache_Key, data, result,
			getCacheFile()));
			
	FileCacheItem fi = new AppCache<FileCacheItem>(CacheType.File)
					.get(new FileCacheItem(Values.Cache_Key, getCacheFile()));
	if (fi.getExt() == null) {
		result = JsonUtils.gson.fromJson(fi.getValue().toString(),
				ChannelResult.class);
		fi.setExt(result);
		addExternalChannels(result.getItems());
	}
	result = (ChannelResult) fi.getExt();
 * 
 * }
 * 
 * 
 * 
 * @author xujian
 * 
 * @param <T>
 * 
 */
public class AppCache<T> extends CacheAdapter implements ICache {

	CacheType cacheType = CacheType.Memory;

	public AppCache(CacheType type) {
		cacheType = type;
	}

	public CacheAdapter getAdapter() {
		return CacheSetting.getCacheAdapter(cacheType);
	}

	@Override
	public void set(CacheItem item) {
		getAdapter().set(item);
	}

	@Override
	public CacheItem get(String key) {
		return getMemoryAdapter().get(key);
	}

	/**
	 * if cache return null, then query data from server point and set to cache
	 * again
	 */
	@Override
	public T get(CacheItem item) {
		try {
			item.setType(cacheType);
			Object cache = getMemoryAdapter().get(item);
			if (cache == null)
				return (T) getAdapter().get(item);

			return (T) cache;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void remove(String key) {
		CacheCore.remove(key);
		getAdapter().remove(key);
	}

	@Override
	public void flush() {
		CacheCore.flush();

		// clear all adapter cache
		for (CacheAdapter adapter : CacheSetting.getAdapters()) {
			adapter.flush();
		}
	}

	@Override
	public int count() {
		return CacheCore.count();
	}

	@Override
	public boolean contains(String key) {
		return CacheCore.contains(key);
	}

	@Override
	public String memorySize() {
		return CacheCore.memorySize();
	}
}
