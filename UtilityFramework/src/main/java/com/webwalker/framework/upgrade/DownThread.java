/**
 * 
 */
package com.webwalker.framework.upgrade;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

/**
 * @author xujian
 * 
 */
public class DownThread extends Thread {

	private static final int BUFFER_SIZE = 1024;
	private URL url;
	private File file;
	private int startPosition;
	private int endPosition;
	private int curPosition;
	private boolean isFinish = false;
	private int downloadSize = 0;
	//线程信号量,控制线程
	private boolean Flag = false;

	public DownThread(URL url, File file, int startPosition, int endPosition) {
		this.url = url;
		this.file = file;
		this.startPosition = startPosition;
		this.curPosition = startPosition;
		this.endPosition = endPosition;
	}

	@Override
	public void run() {
		BufferedInputStream bis = null;
		RandomAccessFile fos = null;
		byte[] buf = new byte[BUFFER_SIZE];
		URLConnection con = null;
		try {
			con = url.openConnection();
			Log.v(DownEntity.TAG, GetMessage("Connected!"));
			con.setAllowUserInteraction(true);
			// 设置当前线程下载的起点，终点
			con.setRequestProperty("Range", "bytes=" + startPosition + "-"
					+ endPosition);
			// 使用java中的RandomAccessFile 对文件进行随机读写操作
			fos = new RandomAccessFile(file, "rw");
			// 设置开始写文件的位置
			fos.seek(startPosition);
			Log.v(DownEntity.TAG, GetMessage("get input stream..."));
			bis = new BufferedInputStream(con.getInputStream());
			// 开始循环以流的形式读写文件
			while (curPosition < endPosition
					&& !Thread.currentThread().interrupted()) {
				int len = bis.read(buf, 0, BUFFER_SIZE);
				if (len == -1) {
					break;
				}
				fos.write(buf, 0, len);
				curPosition = curPosition + len;
				if (curPosition > endPosition) {
					downloadSize += len - (curPosition - endPosition) + 1;
				} else {
					downloadSize += len;
				}
			}
			// 下载完成设为true
			this.isFinish = true;
			Log.v(DownEntity.TAG, GetMessage("download finished!"));
			bis.close();
			fos.close();
		} catch (IOException e) {
			Log.e(DownEntity.TAG, GetMessage("error:" + e.getMessage()));
		}
	}	

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}

	@Override
	public void interrupt() {
		// TODO Auto-generated method stub
		super.interrupt();
	}

	private String GetMessage(String msg) {
		return getName() + "," + msg;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public int getDownloadSize() {
		return downloadSize;
	}
}