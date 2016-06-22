/**
 * 
 */
package com.webwalker.framework.upgrade;

/**
 * @author Administrator
 * 
 */
public class DownCallbackEntity {

	public DownCallbackEntity(int code, String message, int progress,
			int downloadedSize, String file) {
		super();
		Code = code;
		Message = message;
		Progress = progress;
		DownloadedSize = downloadedSize;
		fileName = file;
	}

	private int Code;

	/**
	 * @return the code
	 */
	public int getCode() {
		return Code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		Code = code;
	}

	private String Message;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return Message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		Message = message;
	}

	private int Progress;

	/**
	 * @return the progress
	 */
	public int getProgress() {
		return Progress;
	}

	/**
	 * @param progress
	 *            the progress to set
	 */
	public void setProgress(int progress) {
		Progress = progress;
	}

	private int DownloadedSize;

	/**
	 * @return the downloadedSize
	 */
	public int getDownloadedSize() {
		return DownloadedSize;
	}

	/**
	 * @param downloadedSize
	 *            the downloadedSize to set
	 */
	public void setDownloadedSize(int downloadedSize) {
		DownloadedSize = downloadedSize;
	}

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
