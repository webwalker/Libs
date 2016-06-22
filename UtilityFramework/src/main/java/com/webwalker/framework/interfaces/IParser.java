/**
 * 
 */
package com.webwalker.framework.interfaces;

/**
 * @author xu.jian
 * 
 */
public interface IParser<T, V> {

	public T parse(V data);
}
