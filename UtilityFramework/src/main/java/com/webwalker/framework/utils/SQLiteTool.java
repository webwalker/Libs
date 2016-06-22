package com.webwalker.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.database.Cursor;
import android.util.Log;


public class SQLiteTool {
	private final static String SET_HEAD = "set";
	private final static Map<String, Integer> typeMap = new HashMap<String, Integer>();
	private static final String TAG = "SQLiteTool";
	static {
		typeMap.put("short", 0);
		typeMap.put("int", 1);
		typeMap.put("long", 2);
		typeMap.put("float", 3);
		typeMap.put("double", 4);
		typeMap.put("class java.lang.String", 5);
	}

	public static void attributeCopy(Cursor cursor, Object desc,String[]excludes) {
		@SuppressWarnings("rawtypes")
		Class descClass = desc.getClass();
		Field[] descFields = descClass.getDeclaredFields();
		Set<String> set = new HashSet<String>();
		if(!StringUtil.isNull(excludes)){
			for(String key:excludes)
				set.add(key);
		}
		
		for (Field field : descFields) {
			if(set.contains(field.getName())){
				continue;
			}
			Object value = null;
			try {
				value = valueByType(cursor, field);
				if (null != value) {
					
					Method setMethod = descClass.getDeclaredMethod(SET_HEAD
							+ firstUpperCase(field.getName()), new Class[] { field.getType() });
					setMethod.invoke(desc, value);
				}
			} catch (Exception e) {
				Log.w(TAG,"auto set value fail.name=" + field.getName() + "  type="
						+ field.getType() + "  value=" + value + "  object="
						+ (value==null?null:value.getClass()));
			}
		}
	}

	private static String firstUpperCase(String arg) {
		return Character.toUpperCase(arg.charAt(0)) + arg.substring(1);
	}

	private static Object valueByType(Cursor cursor, Field field) {
		Object value = null;
		int index = cursor.getColumnIndex(field.getName());
		switch (typeMap.get(field.getGenericType().toString())) {
		case 0:
			value = cursor.getShort(index);
			break;
		case 1:
			value = cursor.getInt(index);
			break;
		case 2:
			value = cursor.getLong(index);
			break;
		case 3:
			value = cursor.getFloat(index);
			break;
		case 4:
			value = cursor.getDouble(index);
			break;
		case 5:
			value = cursor.getString(index);
			break;
		default:
			Log.d(TAG,"Does not support the type of data.type="
					+ field.getGenericType().toString());
			break;
		}

		return value;
	}
}
