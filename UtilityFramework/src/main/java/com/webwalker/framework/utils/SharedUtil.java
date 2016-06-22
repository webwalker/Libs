package com.webwalker.framework.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 本地属性存储
 * 
 * @author xujian
 * 
 */
public class SharedUtil {
	private static final String CONTEXT_SHARED_PREFERENCE = "context_shared_preference";

	private static SharedUtil _instance = null;

	public static SharedUtil getInstance(Context context) {
		if (_instance == null)
			_instance = new SharedUtil(context);
		return _instance;
	}

	private Context _Context = null;

	public SharedUtil(Context context) {
		_Context = context;
	}

	public String getString(String key) {
		SharedPreferences sp = _Context.getSharedPreferences(
				CONTEXT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}

	public int getInt(String key) {
		SharedPreferences sp = _Context.getSharedPreferences(
				CONTEXT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sp.getInt(key, -1);
	}

	public boolean getBoolean(String key) {
		SharedPreferences sp = _Context.getSharedPreferences(
				CONTEXT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

	public float getFloat(String key) {
		SharedPreferences sp = _Context.getSharedPreferences(
				CONTEXT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sp.getFloat(key, 0);
	}

	public float getLong(String key) {
		SharedPreferences sp = _Context.getSharedPreferences(
				CONTEXT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sp.getLong(key, 0);
	}

	public List<String> getList(String key) {
		String jsonStr = (String) getString(key);
		List<String> list = new ArrayList<String>();
		try {
			JSONArray jsonArr = new JSONArray(jsonStr);
			for (int i = jsonArr.length() - 1; i >= 0; i--) {
				String str = jsonArr.getString(i);
				list.add(str);
			}
		} catch (Exception e) {
		}
		return list;
	}

	public <T> void set(String key, T value) {
		SharedPreferences sp = _Context.getSharedPreferences(
				CONTEXT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		if (value instanceof String) {
			editor.putString(key, value.toString());
		} else if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (Float) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
		}
		editor.commit();
	}

	public <T> void set(String key, List<T> list) {
		JSONArray array = new JSONArray();
		for (T str : list) {
			array.put(str);
		}

		set(key, array.toString());
	}

	public void remove(String key) {
		SharedPreferences sp = _Context.getSharedPreferences(
				CONTEXT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}

	public void clear() {
		SharedPreferences sp = _Context.getSharedPreferences(
				CONTEXT_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		Map<String, ?> map = sp.getAll();
		if (map != null) {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				edit.remove(it.next());
			}
		}
		edit.clear();
		edit.commit();
	}
}
