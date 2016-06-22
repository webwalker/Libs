package com.webwalker.framework.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.Gson;

/**
 * JSON 操作工具类
 * 
 * @author webwalker
 * 
 * 处理GSON不支持自定义泛型的问题
 * 
 *         refer:http://www.cnblogs.com/qq78292959/p/3781808.html
 */
public class JsonUtil {

	public static final Gson gson = new Gson();

	// public static CommonJson fromJson(String json, Class clazz) {
	// Gson gson = new Gson();
	// Type objectType = type(CommonJson.class, clazz);
	// return gson.fromJson(json, objectType);
	// }
	//
	// public String toJson(Class<T> clazz) {
	// Gson gson = new Gson();
	// Type objectType = type(CommonJson.class, clazz);
	// return gson.toJson(this, objectType);
	// }

	public static ParameterizedType type(final Class raw, final Type... args) {
		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return null;
			}
		};
	}
}
