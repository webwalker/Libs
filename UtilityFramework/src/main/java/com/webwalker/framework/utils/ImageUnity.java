package com.webwalker.framework.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * 通用图片处理框架，可根据不同的需要自定义不同的Options
 * 
 * http:// file:/// content:// assets:// drawable://
 * 
 * @author xujian
 * 
 *         http://www.cnblogs.com/kissazi2/p/3886563.html
 * 
 *         http://www.eoeandroid.com/thread-320929-1-1.html
 * 
 *         内存溢出： http://blog.csdn.net/cjj7905150/article/details/18959077
 * 
 */
public class ImageUnity {
	ImageLoader loader = null;
	static DisplayImageOptions options = null;
	ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public ImageUnity() {
		loader = getLoader();
	}

	public static ImageLoader getLoader() {
		return ImageLoader.getInstance();
	}

	/**
	 * 图片加载第一次显示监听器
	 * 
	 * @author Administrator
	 * 
	 */
	public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	// 初始化图片加载设置
	public static void initLoader(Context context) {
		// File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				// 缓存显示不同大小的同一张图片
				.denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 并行还是线性
				// .taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
				// .taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// 线程池的大小 默认为3
				.threadPoolSize(5)
				// 保存每个缓存图片的最大长和宽
				// .memoryCacheExtraOptions(640, 480)
				// .discCacheExtraOptions 压缩类型
				// .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				// .memoryCacheSize(2 * 1024 * 1024)// 设置缓存的最大字节
				// .discCache(new LimitedAgeDiscCache(cacheDir, 12 * 3600))
				// .discCache(new UnlimitedDiscCache(cacheDir))
				// 20MB，一般不要写，保留原有默认值
				.discCacheSize(50 * 1024 * 1024)
				// 大小和数量不可同时调用
				// .discCacheFileCount(100)
				// .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 超时时间，单位秒
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
				.writeDebugLogs().defaultDisplayImageOptions(getOptions())
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	static DisplayImageOptions getOptions() {
		if (null == options) {
			options = new DisplayImageOptions.Builder()
			// 设置图片Uri为空或是错误的时候显示的图片
					.showImageForEmptyUri(0)
					// 设置图片加载或解码过程中发生错误显示的图片
					.showImageOnFail(0)
					// 设置下载的图片是否缓存在内存中
					.cacheInMemory(false)
					// 设置下载的图片是否缓存在SD卡中
					.cacheOnDisc(true).imageScaleType(ImageScaleType.NONE)
					// 是否图片加载好后渐入的动画时间
					.displayer(new FadeInBitmapDisplayer(100))
					// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
					.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		}
		return options;
	}
}
