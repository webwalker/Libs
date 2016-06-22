package com.webwalker.framework.common;

import java.lang.Thread.UncaughtExceptionHandler;

import com.webwalker.framework.utils.Loggers;

/**
 * @author xujian
 * 
 */
public class UncaughtException implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		long threadId = thread.getId();
		Loggers.d("Thread.getName()=" + thread.getName() + " id=" + threadId
				+ " state=" + thread.getState());
		Loggers.d("Error[" + ex.getMessage() + "]");
	}
}
