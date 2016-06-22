package com.webwalker.framework.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import android.util.Base64;

/**
 * @author drZhang
 * 
 * @version 1.0
 * 
 */
public class FileHashUtil {
	private static char hexChar[] = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public FileHashUtil() {
	}

	public static String getFileMD5(String filename) {
		String str = "";
		try {
			str = getHash(filename, "MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getFileSHA1(String filename) {
		String str = "";
		try {
			str = getHash(filename, "SHA1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getFileSHA256(String filename) {
		String str = "";
		try {
			str = getHash(filename, "SHA-256");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getFileSHA384(String filename) {
		String str = "";
		try {
			str = getHash(filename, "SHA-384");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String getFileSHA512(String filename) {
		String str = "";
		try {
			str = getHash(filename, "SHA-512");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	private static String getHash(String fileName, String hashType)
			throws Exception {
		InputStream fis = new FileInputStream(fileName);
		byte buffer[] = new byte[1024];
		MessageDigest md5 = MessageDigest.getInstance(hashType);
		for (int numRead = 0; (numRead = fis.read(buffer)) > 0;) {
			md5.update(buffer, 0, numRead);
		}

		fis.close();
		return toHexString(md5.digest());
	}

	private static String toHexString(byte b[]) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0xf]);
		}

		return sb.toString();
	}

	public static String getBase64(String arg) {
		String result = null;
		try {
			if (arg != null && !arg.isEmpty()) {
				byte[] buf;
				try {
					buf = arg.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					buf = arg.getBytes();
				}
				result = Base64.encodeToString(buf, Base64.DEFAULT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String encryptMD5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data);

		return toHexString(md5.digest());
	}

	public static String getBase64(byte[] arg) {
		String result = null;
		try {
			result = Base64.encodeToString(arg, Base64.DEFAULT);
		} catch (Exception e) {
		}
		return result;
	}
}
