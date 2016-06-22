package com.webwalker.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期操作工具类
 * 
 * @author webwalker
 * 
 */
public class DateUtil {
	public static SimpleDateFormat format1 = new SimpleDateFormat(
			"yyyyMMdd_HHmmss", Locale.getDefault());
	public static final SimpleDateFormat defaultTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static SimpleDateFormat sdf_simple = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.getDefault());
	public static final SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	public static SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");

	public static SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm");

	public static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat MMdd_YR = new SimpleDateFormat("MM月dd日");
	public static SimpleDateFormat MMdd_Line = new SimpleDateFormat("MM/dd");
	public static SimpleDateFormat MMdd_Spliter = new SimpleDateFormat("MM-dd");
	public static SimpleDateFormat dateFormatToDd = new SimpleDateFormat(
			"yyyyMMdd");
	public static final String currentDateFormat = "yyyy/MM/dd hh:mm:ss:SSS";

	public enum DayType {
		Unkown, Today, Yesterday, Tommorow, beforeYesterday
	}

	public static String formatDate(long timestamp, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timestamp));
	}

	public static String formatCurrentDate(String format) {
		String date = new SimpleDateFormat(format).format(new Date());
		return date;
	}

	/**
	 * 格式化日期
	 */
	public static String formatToyyyy_MM_dd(long timestamp) {
		return yyyy_MM_dd.format(new Date(timestamp));
	}

	/**
	 * 格式化为时分秒
	 * 
	 * @param millisecond
	 * @return
	 */
	public static String formatMillisecond(long millisecond) {
		long days = millisecond / (1000 * 60 * 60 * 24);
		long hours = (millisecond % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (millisecond % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (millisecond % (1000 * 60)) / 1000;
		StringBuffer sb = new StringBuffer();
		if (days > 0) {
			sb.append(days + "天");
		}
		if (days > 0 || hours > 0) {
			sb.append(hours + "时");
		}
		if (days > 0 || hours > 0 || minutes > 0) {
			sb.append(minutes + "分");
		}
		sb.append(seconds + "秒");
		return sb.toString();
	}

	/**
	 * 格式化为分秒：2分30秒
	 * */
	public static String formatToSecond(long second) {
		long min = 0;
		long sec = 0;
		String str = "";
		if (second < 60) {
			min = 0;
			sec = second;
			str = sec + "秒";
		} else {
			min = second / 60;
			sec = second % 60;
			str = min + "分" + sec + "秒";
		}
		return str;
	}

	public static String formatToYYYY_MM_dd(Date time) {
		return yyyy_MM_dd.format(time);
	}

	/** 将long 转化为HH:MM:ss格式 */
	public static String formatToHHmmss(long l) {
		try {
			String s = HHmmss.format(new Date(l));
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 格式化日期与时间
	 */
	public static String getCurrentyyyy_MM_dd_HHmmss(long timestamp) {
		return defaultTimeFormat.format(new Date(timestamp));
	}

	/**
	 * 获取当前日期与时间
	 */
	public static String getCurrentyyyy_MM_dd_HHmmss() {
		return defaultTimeFormat.format(new Date());
	}

	/**
	 * 获取当前日期与时间
	 */
	public static String getCurrentyyyyMMddHHmmss() {
		return yyyyMMddHHmmss.format(new Date());
	}

	/**
	 * 获取当前日期
	 */
	public static String getCurrentyyyy_MM_dd() {
		return yyyy_MM_dd.format(new Date());
	}

	/**
	 * 获取当前时间
	 */
	public static String getCurrentHHmmss() {
		return HHmmss.format(new Date());
	}

	public static Date getYYYY_MM_dd(String dates) {
		try {
			return yyyy_MM_dd.parse(dates);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getTimeString(Date date) {
		return date.getTime() + "";
	}

	/** 将yyyymmddHHMMss 转化为HH：MM：ss格式 */
	public static String getTime(String time) {
		String ttime = "";
		try {
			Date starttime = yyyy_MM_dd.parse(time);
			ttime = HHmmss.format(starttime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ttime;
	}

	public static Date getCurrentTime() {
		return new Date();
	}

	/**
	 * 和当前日期比较日期相差天数
	 * 
	 * @param date
	 * @return 相差天数
	 */
	public static int getDaysFromNow(String date, SimpleDateFormat format) {
		int result = -1;
		try {
			if (format == null)
				format = dateFormatToDd;
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			result = (int) (calendar.getTimeInMillis() - format.parse(date)
					.getTime()) / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Long getDaysBetween(long startDate, long endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTimeInMillis(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTimeInMillis(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (toCalendar.getTime().getTime() - fromCalendar.getTime()
				.getTime()) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 将HHMMss转换为秒数
	 * 
	 * @param time
	 * @return
	 */
	public static long getSeconds(String time) {
		if (time.isEmpty())
			return 0;
		String[] ts = time.split(":");
		int hour = Integer.parseInt(ts[0]);
		int min = Integer.parseInt(ts[1]);
		int sec = Integer.parseInt(ts[2]);

		return hour * 3600 + min * 60 + sec;
	}

	public static String getHHmmss(long seconds) {
		long hour = seconds / 3600;
		long minute = seconds % 3600 / 60;
		long second = seconds % 60;

		return (hour < 10 ? "0" + hour : hour) + ":"
				+ (minute < 10 ? "0" + minute : minute) + ":"
				+ (second < 10 ? "0" + second : second);
	}

	// 将时间戳转化为HH:MM:ss
	public static String getTimestampString(long l) {
		try {
			String s = HHmmss.format(new Date(l * 1000));
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static DayType getDayType(long time) {
		try {
			Calendar now = Calendar.getInstance();
			long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
					+ now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 毫秒数
			long ms_now = now.getTimeInMillis();
			if (ms_now - time < ms)
				return DayType.Today;
			else if (ms_now - time < (ms + 24 * 3600 * 1000))
				return DayType.Yesterday;
			else if (ms_now - time < (ms + 24 * 3600 * 1000 * 2))
				return DayType.beforeYesterday;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return DayType.Unkown;
	}

	/** 将yyyymmddHHMMss 转化为long */
	public static long getTimelong(String time) {
		if (time.isEmpty())
			return 0;
		long l = 0;
		try {
			l = yyyy_MM_dd.parse(time).getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return l;
	}

	/**
	 * 解析时间
	 */
	public static Date parseHHmmss(String str) {
		Date date = null;
		try {
			date = HHmmss.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 解析日期与时间
	 */
	public static Date parseyyyy_MM_dd_HHmmss(String str) {
		Date date = null;
		try {
			date = defaultTimeFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/** HH:MM:ss时间自增加 */
	public static String addTime(String time, long addtime) {
		if (time.isEmpty())
			return "";
		time = formatToHHmmss(Long.valueOf(time) + addtime);
		return time;
	}

	public static String second2HHMM(long l) {
		String s = HHmmss.format(l * 1000
				- TimeZone.getDefault().getRawOffset());
		return s;
	}

	public static String addTimeBySeconds(String time, long seconds) {
		try {
			long s = getSeconds(time);
			s += seconds;
			return getHHmmss(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String addStampTime(String time, long seconds) {
		try {
			if (time.isEmpty())
				return "";
			long s = Long.valueOf(time);
			s += seconds;
			return formatToHHmmss(s * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
