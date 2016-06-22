package com.webwalker.framework.cache;


/**
 * @author xu.jian
 * 
 */
public class FileCacheItem extends CacheItem {

	public FileCacheItem(String key) {
		super(key);
	}

	public FileCacheItem(String key, String p) {
		super(key);
		path = p;
	}

	public FileCacheItem(String key, Object value, String p) {
		super(key);
		path = p;
	}

	public FileCacheItem(String key, Object value, Object ext, String p) {
		super(key, value, ext, CacheType.File);
		path = p;
	}

	public FileCacheItem(String key, Object value, CacheType type) {
		super(key, value, type);
	}

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
