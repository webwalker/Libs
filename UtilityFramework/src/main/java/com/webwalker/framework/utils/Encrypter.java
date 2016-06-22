/**
 * 
 */
package com.webwalker.framework.utils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypter {

	private Key key;
	// 算法名称
	private String KEY_ALGORITHM = "DES";
	// 算法名称/加密模式/填充方式
	private String CIPHER_ALGORITHM = "";
	private static final String ENCODING_UTF8 = "UTF-8";

	// 长度8位
	private static String EncryptKey = "web123!#";
	private static byte[] iv = { 0, 1, 2, 3, 4, 5, 6, 7 };

	public Encrypter() {

	}

	public Encrypter(Algorithm a) {
		KEY_ALGORITHM = getAlgorithm(a);
		// CIPHER_ALGORITHM = getAlgorithmCipher(a);
	}

	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	public static String encryptMD5(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString().toUpperCase();
	}

	/**
	 * MD5签名
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String MD5Encrypt(String key, String value) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(KEY_ALGORITHM);
			messageDigest.reset();
			messageDigest.update(value.getBytes());
		} catch (NoSuchAlgorithmException e) {
		}
		byte[] byteArray = null;
		if (value == null)
			byteArray = messageDigest.digest();
		else
			byteArray = messageDigest.digest(key.getBytes());
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	/**
	 * 加密以 byte[] 明文输入 ,byte[] 密文输出
	 * 
	 * @param byteS
	 * @return
	 */
	byte[] encryptByte(byte[] byteS) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(KEY_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error initializing SqlMap class. Cause: " + e);
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 解密以 byte[] 密文输入 , 以 byte[] 明文输出
	 * 
	 * @param byteD
	 * @return
	 */
	byte[] decryptByte(byte[] byteD) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance(KEY_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error initializing SqlMap class. Cause: " + e);
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 
	 * DESEDE 三重DES
	 * 
	 */
	public enum Algorithm {
		DES, AES, DESEDE, IDEA, MD5
	}

	private String getAlgorithm(Algorithm a) {
		switch (a) {
		case DES:
			return "DES";
		case DESEDE:
			return "DESede";
		case AES:
			return "AES";
		case IDEA:
			return "IDEA";
		case MD5:
			return "MD5";
		}
		return "DES";
	}

	private String getAlgorithmCipher(Algorithm a) {
		switch (a) {
		case DES:
			return "DES/ECB/PKCS5Padding";
		case DESEDE:
			return "DESede/ECB/PKCS5Padding";
		case AES:
			return "AES/ECB/PKCS5Padding";
		case IDEA:
			return "IDEA/ECB/PKCS5Padding";
		}

		return "DES";
	}

	// good
	public static String encryptDES(String encryptString) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(EncryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString
				.getBytes(ENCODING_UTF8));

		return EncrypterBase64.encode(encryptedData);
	}

	public static String decryptDES(String decryptString) throws Exception {
		byte[] byteMi = EncrypterBase64.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(EncryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);

		return new String(decryptedData, ENCODING_UTF8);
	}

	public static String getEncryptKey() {
		return EncryptKey;
	}

	public static void setEncryptKey(String encryptKey) {
		EncryptKey = encryptKey;
	}

	public static byte[] getIv() {
		return iv;
	}

	public static void setIv(byte[] i) {
		iv = i;
	}
}