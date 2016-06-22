package com.webwalker.framework.system.support;

/**
 * 包处理行为类型
 * 
 * @author xu.jian
 * 
 */
public enum PackageActionType {
	NULL(0, ""),

	PACKAGE_ADDED(1, "android.intent.action.PACKAGE_ADDED"),

	PACKAGE_REMOVED(2, "android.intent.action.PACKAGE_REMOVED"),

	PACKAGE_REPLACED(3, "android.intent.action.PACKAGE_REPLACED"),

	PACKAGE_DATA_CLEARED(4, "android.intent.action.PACKAGE_DATA_CLEARED"),

	PACKAGE_FULLY_REMOVED(5, "android.intent.action.PACKAGE_FULLY_REMOVED");

	private final int code;
	private final String value;

	PackageActionType(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public static PackageActionType getByCode(int code) {
		for (PackageActionType resultCode : PackageActionType.values()) {
			if (resultCode.getCode() == code) {
				return resultCode;
			}
		}
		return null;
	}

	public static PackageActionType getByValue(String value) {
		for (PackageActionType type : PackageActionType.values()) {
			if (type.getValue().toUpperCase().equals(value.toUpperCase())) {
				return type;
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
}
