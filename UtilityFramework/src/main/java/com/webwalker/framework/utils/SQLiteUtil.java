package com.webwalker.framework.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author xu.jian
 * 
 */
public class SQLiteUtil {
	private static final String TAG = "SQLiteUtil";

	public static boolean prepareDatabase(Context context, String dbPath,
			String dbName, String validateSql) {
		if ((new File(dbPath + dbName)).exists() == false) {
			File file = new File(dbPath);
			if (!file.exists())
				file.mkdir();
			try {
				// copy db
				InputStream is = context.getAssets().open(dbName);
				OutputStream os = new FileOutputStream(dbPath + dbName);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				os.flush();
				os.close();
				is.close();

				// check copy status
				SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
						dbPath + dbName, null);
				if (!database.isOpen())
					return false;
				if (database != null)
					database.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static String getString(Cursor cursor, String columnName) {
		String ret = null;
		try {
			ret = cursor.getString(cursor.getColumnIndex(columnName));
		} catch (Throwable e) {
			Loggers.d(TAG, "getString(" + columnName + ") failed, because of "
					+ e.toString());
			// e.printStackTrace();
		}
		return ret;
	}

	public static int getStringToInt(Cursor cursor, String columnName, int def) {
		return StringUtil.stringToInt(getString(cursor, columnName), def);
	}

	public static int getStringToInt(Cursor cursor, String columnName) {
		return StringUtil.stringToInt(getString(cursor, columnName));
	}

	public static long getStringToLong(Cursor cursor, String columnName,
			long def) {
		return StringUtil.stringToLong(getString(cursor, columnName), def);
	}

	public static long getStringToLong(Cursor cursor, String columnName) {
		return StringUtil.stringToLong(getString(cursor, columnName));
	}

	public static boolean getStringToBoolean(Cursor cursor, String columnName,
			boolean def) {
		return StringUtil.stringToBoolean(getString(cursor, columnName), def);
	}

	public static boolean getStringToBoolean(Cursor cursor, String columnName) {
		return StringUtil.stringToBoolean(getString(cursor, columnName));
	}

	public static int exeSql(SQLiteDatabase db, String sql) {
		int ret = -1;
		try {
			Loggers.d(TAG, "exeSql : " + sql);
			db.beginTransaction();
			db.execSQL(sql);
			db.setTransactionSuccessful();
			ret = 0;
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return ret;
	}
}
