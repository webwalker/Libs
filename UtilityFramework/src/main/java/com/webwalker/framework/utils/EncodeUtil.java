package com.webwalker.framework.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import android.util.Base64;

/**
 * 编码与解码操作工具类
 * 
 * @author webwalker
 * 
 */
public class EncodeUtil {

	static String UTF_8 = "UTF-8";

	/**
	 * 将 URL 编码
	 */
	public static String encodeURL(String str) {
		String target;
		try {
			target = URLEncoder.encode(str, UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return target;
	}

	/**
	 * 将 URL 解码
	 */
	public static String decodeURL(String str) {
		String target;
		try {
			target = URLDecoder.decode(str, UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return target;
	}

	/**
	 * 将字符串 Base64 编码
	 */
	public static String encodeBASE64(String str) {
		String target;
		try {
			target = Base64.encodeToString(str.getBytes(UTF_8), Base64.DEFAULT);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return target;
	}

	/**
	 * 将字符串 Base64 解码
	 */
	public static String decodeBASE64(String str) {
		String target;
		try {
			target = new String(Base64.decode(str, Base64.DEFAULT), UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return target;
	}

	/**
	 * 创建随机数
	 */
	public static String createRandom(int count) {
		return RandomStringUtils.randomNumeric(count);
	}

	/**
	 * 获取 UUID（32位）
	 */
	public static String createUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
