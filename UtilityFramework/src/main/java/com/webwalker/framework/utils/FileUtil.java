package com.webwalker.framework.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;

/**
 * 文件操作工具类
 * 
 * @author webwalker
 * 
 */
public class FileUtil {
	private static final String TAG = "FileUtils";
	public static final String FILE_SEPARATOR = "/";
	public final static String TEMP_SUFFIX = ".temp";
	private static final int BYTE_BUFFER_SIZE = 1024 * 64;

	public static final int FILE_SUCCESS_WITH_WRITE_FILE_EXISTED = 0x01;
	public static final int FILE_SUCCESS = 0x00;
	public static final int FILE_ERROR_READ_PARAM = (0 - 0x01);
	public static final int FILE_ERROR_WRITE_PARAM = (0 - 0x02);
	public static final int FILE_ERROR_READ_FILE_NOT_FOUND = (0 - 0x03);
	public static final int FILE_ERROR_WRITE_FILE_NOT_FOUND = (0 - 0x04);
	public static final int FILE_ERROR_READ_IO_EXCEPTION = (0 - 0x05);
	public static final int FILE_ERROR_WRITE_IO_EXCEPTION = (0 - 0x06);
	public static final int FILE_ERROR_CREATE_DIR = (0 - 0x10);
	public static final int FILE_ERROR_NOT_ENOUGH_SPACE = (0 - 0x20);
	public static final int FILE_ERROR_UNKNOWN = (0 - 0xFF);

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

	public static void deleteDir(String path, boolean bDelRoot) {
		File dir = new File(path);
		if (dir.exists() && dir.isDirectory()) {
			File[] tmp = dir.listFiles();
			if (null != tmp) {
				for (int i = 0; i < tmp.length; i++) {
					if (tmp[i].isDirectory()) {
						deleteDir(path + "/" + tmp[i].getName(), true);
					} else {
						tmp[i].delete();
					}
				}
			}
			if (bDelRoot) {
				dir.delete();
			}
		}
	}

	public static BufferedOutputStream getOutputStream(File file)
			throws IOException {
		return new BufferedOutputStream(new FileOutputStream(file), 64 * 1024);
	}

	public static BufferedOutputStream getOutputStream(String file)
			throws IOException {
		return getOutputStream(new File(file));
	}

	public static void close(Closeable in) {
		if (null != in) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			in = null;
		}
	}

	public static boolean createFileIfNotExist(String filePath) {
		boolean ret = false;
		File file = new File(filePath);
		if (!file.isFile()) {
			try {
				Loggers.d(TAG, "create file.file=" + file.getAbsolutePath());
				createDirIfNotExist(file);
				ret = file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static boolean createDirIfNotExist(File file) {
		boolean ret = true;
		if (null == file) {
			ret = false;
			return ret;
		}
		if (!file.exists()) {
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				ret = parentFile.mkdirs();
				// chmod777(parentFile, null);
			}
		}
		return ret;
	}

	public static boolean createDirIfNotExist(String filePath) {
		File file = new File(filePath);
		return createDirIfNotExist(file);
	}

	public static boolean dirExisted(String path) {
		boolean ret = false;
		File file = new File(path);
		if (file.isDirectory() && file.exists()) {
			ret = true;
		}
		return ret;
	}

	public static boolean fileExisted(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists())
			return true;

		return false;
	}

	public static boolean fileExisted(String filePath, boolean hasContent) {
		boolean ret = false;
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			ret = true;
			if (hasContent && (file.length() <= 0)) {
				ret = false;
			}
		}
		return ret;
	}

	public static void writeFile(String str, String descFile, boolean append)
			throws Exception {
		if ((null == str) || (null == descFile)) {
			return;
		}

		createDirIfNotExist(descFile);

		BufferedOutputStream out = null;

		try {
			byte[] src = str.getBytes("UTF-8");
			out = new BufferedOutputStream(new FileOutputStream(descFile,
					append), 1024 * 64);
			out.write(src);
			out.flush();
		} finally {
			close(out);
		}
	}

	public static void appendFile(String str, String descFile) throws Exception {
		writeFile(str, descFile, true);
	}

	public static String getTempName(String file) {
		return file + TEMP_SUFFIX;
	}

	public static int randomWriteBigFile(InputStream inputStream,
			String descFile, long offset) {
		Loggers.d(TAG, "enter randomWriteBigFile(" + inputStream + ", "
				+ descFile + ", " + offset + ")");
		int ret = FILE_SUCCESS;
		RandomAccessFile random = null;

		try {
			do {
				if (null == inputStream) {
					ret = FILE_ERROR_READ_PARAM;
					Loggers.d(TAG, "error param inputStream is null.");
					break;
				}
				if (StringUtil.isNull(descFile)) {
					ret = FILE_ERROR_WRITE_PARAM;
					Loggers.d(TAG, "error param descFile=" + descFile);
					break;
				}

				createDirIfNotExist(descFile);
				File tempFile = new File(descFile + TEMP_SUFFIX);
				try {
					random = new RandomAccessFile(tempFile, "rwd");
					random.seek(offset);
				} catch (FileNotFoundException e) {
					ret = FILE_ERROR_WRITE_FILE_NOT_FOUND;
					break;
				} catch (IOException e) {
					ret = FILE_ERROR_WRITE_IO_EXCEPTION;
					break;
				}
				byte[] buffer = new byte[BYTE_BUFFER_SIZE];
				int count = -1;
				try {
					while ((count = inputStream.read(buffer)) != -1) {
						try {
							random.write(buffer, 0, count);
						} catch (IOException e) {
							ret = FILE_ERROR_WRITE_IO_EXCEPTION;
							break;
						}
					}
				} catch (IOException e) {
					ret = FILE_ERROR_READ_IO_EXCEPTION;
					break;
				}
			} while (false);
		} catch (Throwable e) {
			e.printStackTrace();
			ret = FILE_ERROR_UNKNOWN;
		} finally {
			close(random);
		}

		return ret;
	}

	public static void chmod777(File file, String root) {
		try {
			if (null == file || !file.exists()) {
				Loggers.d(TAG, "chmod777 param error. file=" + file);
				return;
			}

			Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
			File tempFile = file.getParentFile();
			String tempName = tempFile.getName();
			if (tempFile.getName() == null || "".equals(tempName)) {
				Loggers.d(TAG, "chmod777 to the root directory. return");
				return;
			} else if (!StringUtil.isNull(root) && root.equals(tempName)) {
				Runtime.getRuntime().exec(
						"chmod 777 " + tempFile.getAbsolutePath());
				Loggers.d(TAG, "chmod777 match return. root=" + root);
				return;
			}
			chmod777(file.getParentFile(), root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void chmod777(String file, String root) {
		Loggers.d(TAG, "enter chmod777 : " + file + ", " + root);
		chmod777(new File(file), root);
	}

	public static long getFreeSpaceInKB(String path) {
		long result = -1;
		try {
			File device = new File(path);
			if (device.exists()) {
				StatFs statfs = new StatFs(path);
				int nBlocSize = statfs.getBlockSize();
				int nAvailaBlock = statfs.getAvailableBlocks();
				result = nAvailaBlock * nBlocSize / 1024;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.d(TAG, result + "------");
		return result;
	}

	public static void clearDir(File file, String[] exceptSuffix) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (null != files) {
				for (File tempFile : files) {
					deleteFileEx(tempFile, exceptSuffix);
				}
			}
		} else {
			Loggers.d(TAG, "Is not a valid directory!" + file.getAbsolutePath());
		}
	}

	private static void deleteFileEx(File file, String[] exceptSuffix) {
		if (file.isFile()) {
			boolean del = true;
			if (null != exceptSuffix) {
				for (String suffix : exceptSuffix) {
					if ((null != suffix) && (file.getName().endsWith(suffix))) {
						del = false;
						break;
					}
				}
			}
			if (del) {
				Loggers.d(TAG, "delete file : " + file.getAbsolutePath());
				file.delete();
			}
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (null == files || files.length == 0) {
				file.delete();
			} else {
				for (File tempFile : files) {
					deleteFileEx(tempFile, exceptSuffix);
				}
				file.delete();
			}
		}
	}

	/**
	 * 解压文件
	 * 
	 * 如解压文件存放路径不是有效目录，则解压后文件存放在待解压文件同级目录里
	 * 
	 * @param srcFile
	 *            待解压文件
	 * @param destFile
	 *            解压文件存放路径
	 * @throws Exception
	 */
	public static void decompress(File srcFile, File destFile) throws Exception {

		if (null == srcFile || !srcFile.isFile()) {
			return;
		}

		if (null == destFile || !destFile.isDirectory()) {

			destFile = srcFile.getParentFile();
		}

		ZipInputStream in = null;
		BufferedOutputStream out = null;

		try {
			in = new ZipInputStream(new FileInputStream(srcFile));
			ZipEntry entry = null;
			byte[] b = new byte[BYTE_BUFFER_SIZE];
			int len = -1;

			while (null != (entry = in.getNextEntry())) {
				File file = new File(destFile.getAbsolutePath()
						+ FILE_SEPARATOR + entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
					continue;
				}

				if (!file.getParentFile().isDirectory()) {
					file.getParentFile().mkdirs();
				}
				out = new BufferedOutputStream(new FileOutputStream(file));
				while (-1 != (len = in.read(b))) {
					out.write(b, 0, len);
				}
				out.close();
				out = null;
			}

		} finally {
			close(out);
			close(in);
		}
	}

	public static String getFileNameUrl(String url) {
		Pattern pattern = Pattern.compile("([^\\\\/]+\\.\\w+)$");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}

	public static String getFileName(String url) {
		File file = new File(url);
		return file.getName();
	}

	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameByPath(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	public static String getFileSuffix(String url) {
		String ret = "";
		String fileName = getFileName(url);
		Loggers.d(TAG, "file name is " + fileName);
		ret = fileName.substring(fileName.lastIndexOf(".") + 1);
		Loggers.d(TAG, "file suffix is " + ret);
		return ret;
	}

	public static boolean isZipFile(String url) {
		boolean ret = false;
		final String suffixZip = "zip";
		final String suffixRar = "rar";
		final String suffix7Z = "7z";
		String suffix = getFileSuffix(url);
		if ((0 == suffixZip.compareToIgnoreCase(suffix))
				|| (0 == suffixRar.compareToIgnoreCase(suffix))
				|| (0 == suffix7Z.compareToIgnoreCase(suffix))) {
			ret = true;
		}
		return ret;
	}

	public static Drawable loadImageFromNetwork(String imageUrl) {
		Drawable drawable = null;
		try {
			if (isZipFile(imageUrl)) {
				return drawable;
			}
			String srcImg = "upgrade_desc" + getFileSuffix(imageUrl);
			// 可以在这里通过文件名来判断，是否本地有此图片
			drawable = Drawable.createFromStream(
					new URL(imageUrl).openStream(), srcImg);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		Loggers.d(TAG, "loadImageFromNetwork(" + imageUrl + ") return "
				+ drawable);

		return drawable;
	}

	public static String readFile(String srcFile) {
		String ret = "";
		try {
			File file = new File(srcFile);
			if (file.exists()) {
				ret = readByInputStream(new FileInputStream(file));
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String readByInputStream(InputStream is) {
		StringBuffer sb = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			sb = new StringBuffer();
			String line = "";
			while (null != (line = br.readLine())) {
				sb.append(line);
			}
		} catch (IOException e) {
			sb = null;
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != isr) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (null != sb) {
			return sb.toString();
		}

		return null;
	}

	/**
	 * 创建目录
	 */
	public static File createDir(String dirPath) {
		File dir;
		try {
			dir = new File(dirPath);
			if (!dir.exists()) {
				FileUtils.forceMkdir(dir);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dir;
	}

	/**
	 * 创建文件
	 */
	public static File createFile(String filePath) {
		File file;
		try {
			file = new File(filePath);
			File parentDir = file.getParentFile();
			if (!parentDir.exists()) {
				FileUtils.forceMkdir(parentDir);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return file;
	}

	/**
	 * 复制目录（不会复制空目录）
	 */
	public static void copyDir(String srcPath, String destPath) {
		try {
			File srcDir = new File(srcPath);
			File destDir = new File(destPath);
			if (srcDir.exists() && srcDir.isDirectory()) {
				FileUtils.copyDirectoryToDirectory(srcDir, destDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 复制文件
	 */
	public static void copyFile(String srcPath, String destPath) {
		try {
			File srcFile = new File(srcPath);
			File destDir = new File(destPath);
			if (srcFile.exists() && srcFile.isFile()) {
				FileUtils.copyFileToDirectory(srcFile, destDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 删除目录
	 */
	public static void deleteDir(String dirPath) {
		try {
			File dir = new File(dirPath);
			if (dir.exists() && dir.isDirectory()) {
				FileUtils.deleteDirectory(dir);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 重命名文件
	 */
	public static void renameFile(String srcPath, String destPath) {
		File srcFile = new File(srcPath);
		if (srcFile.exists()) {
			File newFile = new File(destPath);
			boolean result = srcFile.renameTo(newFile);
			if (!result) {
				throw new RuntimeException("重命名文件出错！" + newFile);
			}
		}
	}

	/**
	 * 将字符串写入文件
	 */
	public static void writeFile(String filePath, String fileContent) {
		OutputStream os = null;
		Writer w = null;
		try {
			FileUtil.createFile(filePath);
			os = new BufferedOutputStream(new FileOutputStream(filePath));
			w = new OutputStreamWriter(os, "UTF-8");
			w.write(fileContent);
			w.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (w != null) {
					w.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取真实文件名（去掉文件路径）
	 */
	public static String getRealFileName(String fileName) {
		return FilenameUtils.getName(fileName);
	}

	public static void close(OutputStream out) {
		if (null != out) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out = null;
		}
	}

	public static void close(InputStream in) {
		if (null != in) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			in = null;
		}
	}

	public static String getFileNames(String url) {
		Pattern pattern = Pattern.compile("([^\\\\/]+\\.\\w+)$");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
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

	/**
	 * 根据文件名去获取文件后缀名
	 * 
	 * @param fileName
	 *            文件名
	 * */
	public static String getExtFromFileName(String fileName) {
		// 获取文件的扩展名
		String expName = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length()).toLowerCase();
		return expName;
	}

	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getPath() + "/";
	}

	/**
	 * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * 
	 * @param context
	 * @param msg
	 */
	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";

		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(content.getBytes());

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文本文件
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String read(Context context, String fileName) {
		try {
			FileInputStream in = context.openFileInput(fileName);
			return readInStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static String readFileByLines(String fileName) {
		StringBuilder sb = new StringBuilder();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = "";
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString + "\r\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 随机读取文件内容
	 */
	public static void readFileByRandomAccess(String fileName) {
		RandomAccessFile randomFile = null;
		try {
			System.out.println("随机读取一段文件内容：");
			// 打开一个随机访问文件流，按只读方式
			randomFile = new RandomAccessFile(fileName, "r");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 读文件的起始位置
			int beginIndex = (fileLength > 4) ? 4 : 0;
			// 将读文件的开始位置移到beginIndex位置。
			randomFile.seek(beginIndex);
			byte[] bytes = new byte[10];
			int byteread = 0;
			// 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
			// 将一次读取的字节数赋给byteread
			while ((byteread = randomFile.read(bytes)) != -1) {
				System.out.write(bytes, 0, byteread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/**
	 * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
	 */
	public static void readFileByBytes(String fileName) {
		File file = new File(fileName);
		InputStream in = null;
		try {
			System.out.println("以字节为单位读取文件内容，一次读一个字节：");
			// 一次读一个字节
			in = new FileInputStream(file);
			int tempbyte;
			while ((tempbyte = in.read()) != -1) {
				System.out.write(tempbyte);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			System.out.println("以字节为单位读取文件内容，一次读多个字节：");
			// 一次读多个字节
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			in = new FileInputStream(fileName);
			showAvailableBytes(in);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	/**
	 * 显示输入流中还剩的字节数
	 */
	private static void showAvailableBytes(InputStream in) {
		try {
			System.out.println("当前字节输入流中的字节数为:" + in.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件
	 */
	public static void readFileByChars(String fileName) {
		File file = new File(fileName);
		Reader reader = null;
		try {
			System.out.println("以字符为单位读取文件内容，一次读一个字节：");
			// 一次读一个字符
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				// 对于windows下，\r\n这两个字符在一起时，表示一个换行。
				// 但如果这两个字符分开显示时，会换两次行。
				// 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
				if (((char) tempchar) != '\r') {
					System.out.print((char) tempchar);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println("以字符为单位读取文件内容，一次读多个字节：");
			// 一次读多个字符
			char[] tempchars = new char[30];
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(fileName));
			// 读入多个字符到字符数组中，charread为一次读取字符数
			while ((charread = reader.read(tempchars)) != -1) {
				// 同样屏蔽掉\r不显示
				if ((charread == tempchars.length)
						&& (tempchars[tempchars.length - 1] != '\r')) {
					System.out.print(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == '\r') {
							continue;
						} else {
							System.out.print(tempchars[i]);
						}
					}
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static String readInStream(InputStream inStream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}

			outStream.close();
			inStream.close();
			return outStream.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * A方法追加文件：使用RandomAccessFile
	 */
	public static void appendMethodA(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * B方法追加文件：使用FileWriter
	 */
	public static void appendMethodB(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createFile(String folderPath, String fileName) {
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		return new File(folderPath, fileName + fileName);
	}

	/**
	 * 向手机写图片
	 * 
	 * @param buffer
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static boolean writeFile(byte[] buffer, String folder,
			String fileName) {
		boolean writeSucc = false;

		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

		String folderPath = "";
		if (sdCardExist) {
			folderPath = Environment.getExternalStorageDirectory()
					+ File.separator + folder + File.separator;
		} else {
			writeSucc = false;
		}

		File fileDir = new File(folderPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File file = new File(folderPath + fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return writeSucc;
	}

	/**
	 * 根据文件的绝对路径获取文件名但不包含扩展名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
				point);
	}

	/**
	 * 获取文件名，不加上扩展名
	 * 
	 * @param name
	 *            文件全名
	 * @return
	 * */
	public static String getNameFromFileName(String name) {
		int index = name.lastIndexOf(".");
		if (index != -1) {
			name = name.substring(0, index);
		}
		return name;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param size
	 *            字节
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float) size / 1024;
		if (temp >= 1024) {
			return df.format(temp / 1024) + "M";
		} else {
			return df.format(temp) + "K";
		}
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}

	/**
	 * 获取目录文件个数
	 * 
	 * @param f
	 * @return
	 */
	public long getFileList(File dir) {
		long count = 0;
		File[] files = dir.listFiles();
		count = files.length;
		for (File file : files) {
			if (file.isDirectory()) {
				count = count + getFileList(file);// 递归
				count--;
			}
		}
		return count;
	}

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String name) {
		boolean status;
		if (!name.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + name);
			status = newPath.exists();
		} else {
			status = false;
		}
		return status;
	}

	/**
	 * 检查路径是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean checkFilePathExists(String path) {
		return new File(path).exists();
	}

	/**
	 * 计算SD卡的剩余空间
	 * 
	 * @return 返回-1，说明没有安装sd卡
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = availableBlocks * blockSize / 1024;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		return (freeSpace);
	}

	/**
	 * 新建SD卡中子目录
	 * 
	 * @param directoryName
	 * @return
	 */
	public static boolean createDirectory(String directoryName) {
		boolean status;
		if (!directoryName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			status = newPath.mkdir();
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 检查是否安装SD卡
	 * 
	 * @return
	 */
	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 检查是否安装外置的SD卡
	 * 
	 * @return
	 */
	public static boolean checkExternalSDExists() {

		Map<String, String> evn = System.getenv();
		return evn.containsKey("SECONDARY_STORAGE");
	}

	/**
	 * 删除目录(包括：目录里的所有文件)
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteDirectory(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				// delete all files within the specified directory and then
				// delete the directory
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + "/"
								+ listfile[i].toString());
						deletedFile.delete();
					}
					newPath.delete();
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}

			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isFile()) {
				try {
					newPath.delete();
					status = true;
				} catch (SecurityException se) {
					se.printStackTrace();
					status = false;
				}
			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * 删除空目录
	 * 
	 * 返回 0代表成功 ,1 代表没有删除权限, 2代表不是空目录,3 代表未知错误
	 * 
	 * @return
	 */
	public static int deleteBlankPath(String path) {
		File f = new File(path);
		if (!f.canWrite()) {
			return 1;
		}
		if (f.list() != null && f.list().length > 0) {
			return 2;
		}
		if (f.delete()) {
			return 0;
		}
		return 3;
	}

	/**
	 * 重命名
	 * 
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public static boolean reNamePath(String oldName, String newName) {
		File f = new File(oldName);
		return f.renameTo(new File(newName));
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static boolean deleteFileWithPath(String filePath) {
		SecurityManager checker = new SecurityManager();
		File f = new File(filePath);
		checker.checkDelete(filePath);
		if (f.isFile()) {
			f.delete();
			return true;
		}
		return false;
	}

	/**
	 * 获取SD卡的根目录
	 * 
	 * @return
	 */
	public static String getSDRoot() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 获取手机外置SD卡的根目录
	 * 
	 * @return
	 */
	public static String getExternalSDRoot() {

		Map<String, String> evn = System.getenv();

		return evn.get("SECONDARY_STORAGE");
	}

	/**
	 * 列出root目录下所有子目录
	 * 
	 * @param path
	 * @return 绝对路径
	 */
	public static List<String> listPath(String root) {
		List<String> allDir = new ArrayList<String>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		// 过滤掉以.开始的文件夹
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				if (f.isDirectory() && !f.getName().startsWith(".")) {
					allDir.add(f.getAbsolutePath());
				}
			}
		}
		return allDir;
	}

	/**
	 * 获取一个文件夹下的所有文件
	 * 
	 * @param root
	 * @return
	 */
	public static List<File> listPathFiles(String root) {
		List<File> allDir = new ArrayList<File>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		File[] files = path.listFiles();
		for (File f : files) {
			if (f.isFile())
				allDir.add(f);
			else
				listPath(f.getAbsolutePath());
		}
		return allDir;
	}

	public enum PathStatus {
		SUCCESS, EXITS, ERROR
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 */
	public static PathStatus createPath(String newPath) {
		File path = new File(newPath);
		if (path.exists()) {
			return PathStatus.EXITS;
		}
		if (path.mkdir()) {
			return PathStatus.SUCCESS;
		} else {
			return PathStatus.ERROR;
		}
	}

	/**
	 * 截取路径名
	 * 
	 * @return
	 */
	public static String getPathName(String absolutePath) {
		int start = absolutePath.lastIndexOf(File.separator) + 1;
		int end = absolutePath.length();
		return absolutePath.substring(start, end);
	}

	/**
	 * 获取应用程序缓存文件夹下的指定目录
	 * 
	 * @param context
	 * @param dir
	 * @return
	 */
	public static String getAppCache(Context context, String dir) {
		String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir
				+ "/";
		File savedir = new File(savePath);
		if (!savedir.exists()) {
			savedir.mkdirs();
		}
		savedir = null;
		return savePath;
	}

	/**
	 * 计算一个文件或文件夹下的总大小
	 * 
	 * @param file
	 *            要计算的文件或文件夹
	 * */
	public static long getFilesSize(File file) {
		long size = 0;
		if (file.isDirectory() == true) {
			File[] files = file.listFiles();
			for (File f : files) {
				size += getFilesSize(f);
			}
		} else {
			size = file.length();
		}
		return size;
	}

	/**
	 * 删除文件或文件夹下的所有文件
	 * 
	 * @param file要删除的文件或文件夹
	 * */
	public static boolean deleteFiles(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles())
				deleteFiles(f);
		}
		return file.delete();
	}

	/** 检查读写权限 */
	public static boolean checkCanWrite(String path) {
		File file = new File(path);
		return file.canWrite();
	}
}
