package com.webwalker.framework.upgrade;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;

import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.utils.MessageUtil;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;

/**
 * @author xujian
 * 
 */
public class HttpDownload {
	private DownCallback callback;
	private DownEntity downRequest = null;
	private Thread mainThread = null;
	private int downloadedSize = 0;
	private int fileSize = 0;
	private static boolean isDownloading = false;
	private DownThread[] subThreads = null;
	private boolean cancel = false;
	private Context context = null;

	public HttpDownload(Context c, DownEntity entity, DownCallback back) {
		this.context = c;
		downRequest = entity;
		callback = back;
	}

	/**
	 * 下载、重新下载
	 */
	public void Start() {
		Loggers.d(DownEntity.TAG, "pls waiting...");
		if (isDownloading) {
			Loggers.d(DownEntity.TAG, "has existed download task");
			return;
		}
		File file = new File(downRequest.getDownPath());
		// 创建下载目录
		if (!file.exists()) {
			Loggers.d(DownEntity.TAG, "create directory...");
			file.mkdirs();
		}
		// 启动文件下载线程
		mainThread = new downloadTask(downRequest);
		mainThread.start();
		Loggers.d(DownEntity.TAG, "main thread started!");
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 当收到更新视图消息时，计算已完成下载百分比，同时更新进度条信息
			int progress = (Double
					.valueOf((downloadedSize * 1.0 / fileSize * 100)))
					.intValue();
			if (progress == 100) {
				Loggers.d(DownEntity.TAG,
						"file downloaded! invoking callback function...");
			}
			DownCallbackEntity entity = new DownCallbackEntity(0, "ok",
					progress, downloadedSize, downRequest.getSaveFileName());
			callback.callBack(entity);
		}
	};

	/**
	 * 主下载线程
	 */
	public class downloadTask extends Thread {
		private DownEntity request = null;
		private int blockSize, downloadSizeMore;
		String threadNo;

		public downloadTask(DownEntity entity) {
			request = entity;
			subThreads = new DownThread[request.getThreadCount()];
		}

		@Override
		public void run() {
			try {
				SetStatus(true);
				URL url = new URL(request.getUrl());
				URLConnection conn = url.openConnection();
				// 线程数
				int threadCount = request.getThreadCount();
				// 获取下载文件的总大小
				fileSize = conn.getContentLength();
				// 计算每个线程要下载的数据量
				blockSize = fileSize / threadCount;
				// 解决整除后百分比计算误差
				downloadSizeMore = (fileSize % request.getThreadCount());
				File file = new File(request.getSaveFileName());

				int startPosition = 0;
				int endPosition = 0;
				for (int i = 0; i < threadCount; i++) {
					// 启动线程，分别下载自己需要下载的部分
					startPosition = i * blockSize;
					endPosition = (i + 1) * blockSize - 1;

					if (threadCount == 1) {
						endPosition = blockSize;
					}
					// 将剩余的大小放到最后一个线程下载
					if (i == threadCount - 1) {
						endPosition += downloadSizeMore;
					}

					DownThread dt = new DownThread(url, file, startPosition,
							endPosition);
					dt.setName("Thread" + i);
					dt.start();
					subThreads[i] = dt;
					Loggers.d(DownEntity.TAG, dt.getName() + " started!");
				}
				boolean finished = false;
				while (!finished) {
					if (cancel)
						break;
					// 先把整除的余数搞定
					downloadedSize = downloadSizeMore;
					finished = true;
					for (int i = 0; i < subThreads.length; i++) {
						downloadedSize += subThreads[i].getDownloadSize();
						if (!subThreads[i].isFinish()) {
							finished = false;
						}
					}
					// 通知handler去更新视图组件
					handler.sendEmptyMessageDelayed(0, 0);
					// 休息1秒后再读取下载进度
					sleep(500);
				}
				SetStatus(false);
			} catch (Exception e) {
				Cancel();
				SetStatus(false);
			}
		}
	}

	/**
	 * 取消下载
	 */
	public void Cancel() {
		try {
			for (int i = 0; i < subThreads.length; i++) {
				if (subThreads[i].isAlive())
					subThreads[i].interrupt();
			}
			if (mainThread.isAlive())
				mainThread.interrupt();

			isDownloading = false;
			Loggers.d(DownEntity.TAG, "task has been canceled!");
			MessageUtil.showCustomToast(context, "升级已取消", Gravity.BOTTOM);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 继续下载
	 */
	public void Resume() {
	}

	/**
	 * 暂停下载
	 */
	public void Pause() {
	}

	void SetStatus(boolean status) {
		isDownloading = status;
	}
}
