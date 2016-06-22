package com.webwalker.framework.utils;

import java.util.UUID;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @author xu.jian
 * 
 */
public class SystemUtil {
	private static String TAG = "INIT";
	public static String UA = null;
	public static String IMEI = null;

	public static String getIMEI() {
		return IMEI;
	}

	public static String getModel() {
		try {
			return android.os.Build.MODEL;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getOSVersion() {
		try {
			return android.os.Build.VERSION.RELEASE;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取初始化信息 注意：初始化信息项有可变性
	 * 
	 * @return
	 */
	public static String initUA(Context context) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("{");
			// 1.品牌HTC
			String brand = android.os.Build.BRAND;
			buffer.append("BRAND:" + brand + ",");

			// 2.手机型号 HTC Desire
			String model = android.os.Build.MODEL;
			buffer.append("MODEL:" + model + ",");

			// 3.os版本（如android）
			buffer.append("OSVERSION:Android,");

			// 4.固件版本（如2.2）
			String release = android.os.Build.VERSION.RELEASE;
			buffer.append("RELEASE:" + release + ",");

			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
				// IMSI:国际移动用户识别码
				String IMSI = tm.getSubscriberId();
				System.out.println("UA: IMSI=" + IMSI);
				buffer.append("IMSI:" + IMSI + ",");

				// 获取手机号
				String phoneNum = tm.getLine1Number();
				System.out.println("UA: phone=" + phoneNum);
				buffer.append("Line1Number:" + phoneNum + ",");

				// IMEI:手机串号
				String _IMEI = tm.getSimSerialNumber();
				System.out.println("UA:SN" + _IMEI);
				buffer.append("IMEI:" + _IMEI + ",");
				IMEI = _IMEI;

				// 取得SIM卡驱动的ID号
				String deviceid = tm.getDeviceId();
				System.out.println("UA:DEVID  " + deviceid);
				buffer.append("DeviceId:" + deviceid + ",");

				// 取得SIM状态信息
				String simstate = getSimState(tm);
				System.out.println("UA:state  " + simstate);
				buffer.append("SimState:" + simstate + ",");

				// 取得当前使用的网络类型：
				String networkType = getNetworkType(tm);
				System.out.println("UA:" + networkType);
				buffer.append("NetWorkType:" + networkType + ",");

				// 取得信号类型
				String phoneType = getPhoneType(tm);
				buffer.append("PhoneType:" + phoneType + ",");

				// 取得网络国别
				String networkCountry = tm.getNetworkCountryIso();
				System.out.println("UA:" + networkCountry);
				buffer.append("NetworkCountryIso:" + networkCountry + ",");

				// 取得网络运营商代码
				String networkOperator = tm.getNetworkOperator();
				System.out.println("UA:" + networkOperator);
				buffer.append("NetworkOperator:" + networkOperator + ",");

				// 取得网络运营商名称
				String networkOpName = tm.getNetworkOperatorName();
				System.out.println("UA:" + networkOpName);
				buffer.append("NetworkOperatorName:" + networkOpName + ",");

				// 取得SIM卡国别
				String simCountry = tm.getSimCountryIso();
				System.out.println("UA:" + simCountry);
				buffer.append("SimCountryIso:" + simCountry + ",");

				// 取得SIM卡供货商代码
				String simOperatorCode = tm.getSimOperator();
				System.out.println("UA:" + simOperatorCode);
				buffer.append("SimOperator:" + simOperatorCode + ",");

				// 取得SIM卡供货商名称
				String simOperatorName = tm.getSimOperatorName();
				System.out.println("UA:" + simOperatorName);
				if (simOperatorName == null || simOperatorName.equals("")) {
					simOperatorName = null;
				}
				buffer.append("SimOperatorName:" + simOperatorName);

			}

			buffer.append("}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		UA = buffer.toString();
		return UA;
	}

	/**
	 * 获取SIM状态
	 * 
	 * @param tm
	 * @return
	 */
	public static String getSimState(TelephonyManager tm) {
		int simState = tm.getSimState();

		String state = null;
		if (simState == TelephonyManager.SIM_STATE_READY) {
			state = "良好";
		} else if (simState == TelephonyManager.SIM_STATE_ABSENT) {
			state = "无SIM卡";
		} else if (simState == TelephonyManager.SIM_STATE_NETWORK_LOCKED) {
			state = "需要NetWork PIN 解锁";
		} else if (simState == TelephonyManager.SIM_STATE_PIN_REQUIRED) {
			state = "需要SIM卡的PIN解锁";
		} else if (simState == TelephonyManager.SIM_STATE_PUK_REQUIRED) {
			state = "需要SIM卡的PUK解锁";
		} else if (simState == TelephonyManager.SIM_STATE_UNKNOWN) {
			state = "SIM卡状态未知";
		}

		// state = "{simstate:"+state+"}";
		return state;
	}

	/**
	 * 匹配网络类型
	 * 
	 * @param tm
	 * @return
	 */
	public static String getNetworkType(TelephonyManager tm) {
		int networkType = tm.getNetworkType();
		String type = null;
		if (networkType == TelephonyManager.NETWORK_TYPE_GPRS) {
			type = "NETWORK_TYPE_GPRS";
		} else if (networkType == TelephonyManager.NETWORK_TYPE_EDGE) {
			type = "NETWORK_TYPE_EDGE";
		} else if (networkType == TelephonyManager.NETWORK_TYPE_UMTS) {
			type = "NETWORK_TYPE_UMTS";
		} else if (networkType == TelephonyManager.NETWORK_TYPE_HSDPA) {
			type = "NETWORK_TYPE_HSDPA";
		} else if (networkType == TelephonyManager.NETWORK_TYPE_HSUPA) {
			type = "NETWORK_TYPE_HSUPA";
		} else if (networkType == TelephonyManager.NETWORK_TYPE_HSPA) {
			type = "NETWORK_TYPE_HSPA";
		} else if (networkType == TelephonyManager.NETWORK_TYPE_CDMA) {
			type = "NETWORK_TYPE_CDMA";
		} else {
			type = "EVDO | 1xRTT";
		}

		return type;
	}

	/**
	 * 获取手机信号类型
	 * 
	 * @param tm
	 * @return
	 */
	public static String getPhoneType(TelephonyManager tm) {
		int phoneType = tm.getPhoneType();
		String type = null;

		if (phoneType == TelephonyManager.PHONE_TYPE_GSM) {
			type = "PHONE_TYPE_GSM";
		} else if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
			type = "PHONE_TYPE_CDMA";
		} else {
			type = "PHONE_TYPE_NONE";// 无信号
		}

		return type;
	}

	/**
	 * 生成唯一标识
	 * 
	 * @return
	 */
	public static String createMUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
