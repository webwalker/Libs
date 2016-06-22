package com.webwalker.framework.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadPoolUtils {

	private static ThreadPoolUtils threadPoolUtils;
	private static ExecutorService executor;
	private static ScheduledExecutorService scheduleExecutor;

	/**
	 * 获得线程池实例
	 * 
	 * @return
	 */
	public synchronized static ThreadPoolUtils getInstance() {
		if (threadPoolUtils == null) {
			threadPoolUtils = new ThreadPoolUtils();
		}
		return threadPoolUtils;
	}

	private ThreadPoolUtils() {
		executor = Executors.newCachedThreadPool();
	}

	public ExecutorService getExecutor(int threadCount) {
		if (executor == null) {
			executor = Executors.newFixedThreadPool(threadCount);
		}
		return executor;
	}

	public void execute(Runnable task) {
		executor.execute(task);
	}

	public Future submit(Runnable task) {
		return executor.submit(task);
	}

	public ExecutorService getCacheExecutor() {
		if (executor == null)
			executor = Executors.newCachedThreadPool();
		return executor;
	}

	public ScheduledExecutorService getScheduleExecutor(int threadCount) {
		if (scheduleExecutor == null)
			scheduleExecutor = Executors.newScheduledThreadPool(threadCount);
		return scheduleExecutor;
	}

}
