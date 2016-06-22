package com.webwalker.framework.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/**
 * @author xu.jian
 * 
 */
public class StorageUtil {

	public static boolean hasSDCardMounted() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static boolean hasSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getPath() + "/";
	}

	public static File getSDCardFile(String fileName) {
		return new File(getSDCardPath() + fileName);
	}

	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	public static String getInternalSDCardDir() {
		String ret = "/mnt/sdcard";
		File file = new File(ret);
		if (!(file.exists() && file.canWrite())) {
			ret = null;
		}
		return ret;
	}

	public static String getUsbDir() {
		String ret = null;
		File file = new File("/mnt");
		if (file.isDirectory()) {
			String[] subfiles = file.list();
			for (int index = 0; index < subfiles.length; index++) {
				if (subfiles[index].toLowerCase().startsWith("sd")
						&& (0 != subfiles[index].compareToIgnoreCase("sdcard"))) {
					ret = subfiles[index];
					break;
				}
			}
		}
		if (null != ret) {
			ret = "/mnt/" + ret;
		}
		return ret;
	}

	/**
	 * 返回/data目录的大小。
	 */
	static long getDataDirectorySize() {
		File tmpFile = Environment.getDataDirectory();
		if (tmpFile == null) {
			return 0l;
		}
		String strDataDirectoryPath = tmpFile.getPath();
		StatFs localStatFs = new StatFs(strDataDirectoryPath);
		long size = localStatFs.getBlockSize() * localStatFs.getBlockCount();
		return size;
	}

	public static String getFormatSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return String.format("%d B", size);
	}
}
