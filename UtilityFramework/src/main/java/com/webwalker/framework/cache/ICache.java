package com.webwalker.framework.cache;

/**
 * common event handler
 * 
 * @author xu.jian
 * 
 */
public interface ICache {

	int count();

	boolean contains(String key);

	String memorySize();
}
