package com.webwalker.framework.cache;

import java.util.List;

import com.webwalker.framework.utils.FileUtil;

/**
 * firstly get from memory, then get from local, once local file isn't exists
 * then get it from server，also include image etc. cache files via softreference
 * 
 * @author xujian
 * 
 */
public class FileCacheAdapter extends CacheAdapter {

	private static FileCacheAdapter instance;

	protected FileCacheAdapter() {
	}

	public static FileCacheAdapter getInstance() {
		if (instance == null) {
			instance = new FileCacheAdapter();
		}
		return instance;
	}

	@Override
	public void set(CacheItem item) {
		// sync save to local file, if exist then delete it
		try {
			FileCacheItem fi = (FileCacheItem) item;
			if (FileUtil.fileExisted(fi.getPath())) {
				FileUtil.deleteFile(fi.getPath());
			}
			if (fi.getValue() != null) {
				FileUtil.writeFile(fi.getValue().toString(), fi.getPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		getMemoryAdapter().set(item);
	}

	@Override
	public CacheItem get(String key) {
		return getMemoryAdapter().get(key);
	}

	@Override
	public Object get(CacheItem item) {
		if (get(item.getKey()) == null) {
			FileCacheItem fi = (FileCacheItem) item;
			item.setValue(FileUtil.readFile(fi.getPath()));
			getMemoryAdapter().set(item);
		}

		return getMemoryAdapter().get(item);
	}

	@Override
	public void remove(String key) {
		try {
			FileCacheItem fi = (FileCacheItem) CacheSetting.getCacheAdapter(
					CacheType.Memory).get(key);
			FileUtil.deleteFile(fi.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * delete file
	 */
	@Override
	public void flush() {
		try {
			FileCacheItem fi = null;
			List<CacheItem> items = CacheCore.getAll();
			for (CacheItem ci : items) {
				if (ci.getType() == CacheType.File) {
					fi = (FileCacheItem) ci;
					FileUtil.deleteFile(fi.getPath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
