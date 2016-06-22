package com.webwalker.framework.interfaces;

import java.util.List;

import org.apache.http.NameValuePair;

import com.webwalker.framework.common.Consts.RequestType;

/**
 * @author xu.jian
 * 
 */
public interface IHttpInvoker<T> {

	RequestType getReqType();

	String getUrl();

	List<NameValuePair> getParams();

	T invoke();
}