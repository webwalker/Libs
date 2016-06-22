package com.webwalker.framework.cache;

/**
 * @author xujian
 * 
 */
public class CacheItem {
	private String key;
	private Object value;
	private Object ext;
	private int count;
	private CacheType type = CacheType.Memory;
	private long expiredTime;

	public CacheItem(String key) {
		this.key = key;
	}

	public CacheItem(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public CacheItem(String key, Object value, CacheType type) {
		this.key = key;
		this.value = value;
		this.type = type;
	}

	public CacheItem(String key, Object value, long time) {
		this.key = key;
		this.value = value;
		this.expiredTime = time;
	}

	public CacheItem(String key, Object value, Object ext, CacheType type) {
		this.key = key;
		this.value = value;
		this.type = type;
		this.ext = ext;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(Object ext) {
		this.ext = ext;
	}

	public CacheType getType() {
		return type;
	}

	public void setType(CacheType type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}

}
