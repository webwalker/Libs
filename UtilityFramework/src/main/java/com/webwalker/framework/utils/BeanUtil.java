/**
 * 
 */
package com.webwalker.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.json.JSONObject;

import com.google.gson.Gson;

import android.util.Log;

/**
 * @author xu.jian
 * 
 */
public class BeanUtil {

	/**
	 * 序列号json数据类
	 */
	public static Gson gson = new Gson();

	public static String toJson(Object data) {
		return gson.toJson(data);
	}

	/**
	 * 以源为准初始化对象
	 * 
	 * @param src
	 * @param target
	 * @throws Exception
	 */
	public static void copyBeans2Beans(Object src, Object target)
			throws Exception {
		Field[] srcFields = src.getClass().getDeclaredFields();
		Method[] methods = target.getClass().getMethods();
		for (Field field : srcFields) {
			// 不检查访问修饰符,如果有私有变量请一定要设置
			field.setAccessible(true);
			String methodName = "set"
					+ field.getName().substring(0, 1).toUpperCase()
					+ field.getName().substring(1);
			for (Method method : methods) {
				if (methodName.equals(method.getName()))
					method.invoke(target, field.get(src));
			}
		}

	}

	/**
	 * 以目标为准初始化对象
	 * 
	 * @param src
	 * @param target
	 * @throws Exception
	 */
	public static void initBeans(JSONObject src, Object target) {
		Field[] targetFields = target.getClass().getDeclaredFields();
		for (Field field : targetFields) {
			String name = field.getName();
			Object value = null;
			try {
				if (src.has(name)) {
					value = src.get(name);
					if (value == null) {
						Log.i("BeanUtil", "initBeans以目标为准初始化对象时" + name
								+ "为null");
						continue;
					}
					if (value != null
							&& String.class.toString().equals(
									field.getGenericType().toString()))
						value = value + "";

					field.setAccessible(true); // 设置私有属性范围
					field.set(target, value);
				}
			} catch (Exception e) {
				Log.e("BeanUtil",
						"initBeans以目标为准初始化对象时出错了 ERROR:" + field.getName()
								+ "|" + value + "|"
								+ field.getGenericType().toString());
			}
		}
	}

	/**
	 * 以目标为准初始化对象
	 * 
	 * @param src
	 * @param target
	 * @throws Exception
	 */
	public static void bean2Json(Object srcObj, JSONObject target) {
		Field[] srcFields = srcObj.getClass().getDeclaredFields();
		for (Field field : srcFields) {
			field.setAccessible(true);
			String name = field.getName();
			try {
				Object value = field.get(srcObj);
				target.put(name, value);
			} catch (Exception e) {
				Log.e("BeanUtil",
						"bean2Json以目标为准初始化对象时出错了 ERROR:" + field.getName()
								+ "|" + field.getGenericType().toString());
			}
		}
	}
}
