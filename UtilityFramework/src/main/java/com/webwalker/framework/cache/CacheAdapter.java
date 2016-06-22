package com.webwalker.framework.cache;


/**
 * @author xujian
 * 
 */
public abstract class CacheAdapter {

	public abstract void set(CacheItem item);

	public abstract CacheItem get(String key);

	/**
	 * used for multi parameters cache data, if cache data is null, then invoke
	 * set method to sync, eg:file, database
	 * 
	 * @param item
	 * @return
	 */
	public abstract Object get(CacheItem item);

	public abstract void remove(String key);

	/**
	 * clear all memory data and saved data, eg:database, file, remote
	 */
	public abstract void flush();

	protected CacheAdapter getMemoryAdapter() {
		return CacheSetting.getCacheAdapter(CacheType.Memory);
	}
}
