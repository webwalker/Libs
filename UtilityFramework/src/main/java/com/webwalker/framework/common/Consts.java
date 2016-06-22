package com.webwalker.framework.common;

/**
 * const value defines
 * 
 * @author xu.jian
 * 
 */
public class Consts {

	public static String TAG = "UtilityFramework";
	public static final int timeOut = 30000;
	public static final int success = 0;
	public static final String dateSpliter = "-";
	public static final String dateFormat = "yyyy-MM-dd";

	public static void setTag(String tag) {
		TAG = tag;
	}

	public class MessageWhat {
		public static final int SHOW_SKIP = 1;
	}

	public class Keys {
	}

	public class Values {
		public static final int CONNECTION_TIME_OUT = 30000;
		public static final String Empty = "";
		public static final String CONTEXT_SHARED_PREFERENCE = "context_shared_preference";
	}

	public class Regexs {
		public final static String phoneNumber = "^1\\d{10}$";
		public final static String validateCode = "^\\d{6}$";
	}

	public class Errors {
		public static final String RequestException = "091001";
	}

	public enum RequestType {
		Get, Post
	}
}
