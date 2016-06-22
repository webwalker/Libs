package com.webwalker.framework.upgrade;

import android.content.Context;
import android.widget.ProgressBar;

/**
 * @author Administrator
 * 
 */
public class DownEntity {
	private Context context;
	private String url;
	private String saveFileName = "";
	private int threadCount = 5;
	private String saveFilePath = "";
	private ProgressBar progressBar;
	private DownCallback callback = null;

	public final static String TAG = "FrameworkUpgrade";

	public DownEntity(Context c) {
		this.context = c;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return saveFileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.saveFileName = fileName;
	}

	public String getSaveFileName() {
		if (saveFileName.equals(""))
			saveFileName = "temp.apk";
		return saveFilePath + "/" + saveFileName;
	}

	/**
	 * @return the threadCount
	 */
	public int getThreadCount() {
		return threadCount;
	}

	/**
	 * @param threadCount
	 *            the threadCount to set
	 */
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	/**
	 * @return the filePath
	 */
	public String getDownPath() {
		if (saveFilePath.equals(""))
			saveFilePath = context.getCacheDir().getAbsolutePath();
		return saveFilePath;
	}

	/**
	 * 以SD卡为根目录
	 */
	public void setDownPath(String filePath) {
		saveFilePath = filePath;
	}

	/**
	 * @return the progressBar
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @param progressBar
	 *            the progressBar to set
	 */
	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public DownCallback getCallback() {
		return callback;
	}

	public void setCallback(DownCallback callback) {
		this.callback = callback;
	}
}
