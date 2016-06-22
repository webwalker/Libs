package com.webwalker.framework.http;

import com.webwalker.framework.cache.LruImageCache;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * @author xujian
 * 
 */
public class VolleyImageUtil {
	private Context context;

	public VolleyImageUtil(Context c) {
		this.context = c;
	}

	public void load(NetworkImageView iv, String url) {
		RequestQueue mQueue = BasicVolleyHttpClient.getInstance()
				.getRequestQueue();
		LruImageCache lruImageCache = LruImageCache.instance();
		ImageLoader imageLoader = new ImageLoader(mQueue, lruImageCache);
		
		iv.setImageUrl(url, imageLoader);
	}
}
