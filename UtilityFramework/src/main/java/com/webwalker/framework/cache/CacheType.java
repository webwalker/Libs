/**
 * 
 */
package com.webwalker.framework.cache;

/**
 * @author xu.jian
 * 
 */
public enum CacheType {

	Memory(1, "Memory"),

	File(2, "File"),

	Database(3, "Database"),

	Server(4, "Server");

	private final int code;
	private final String message;

	CacheType(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public static CacheType getByCode(int code) {
		for (CacheType resultCode : CacheType.values()) {
			if (resultCode.getCode() == code) {
				return resultCode;
			}
		}

		return null;
	}

	public static CacheType getByMessage(String message) {
		for (CacheType resultCode : CacheType.values()) {
			if (resultCode.getMessage().toUpperCase() == message.toUpperCase()) {
				return resultCode;
			}
		}

		return null;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
