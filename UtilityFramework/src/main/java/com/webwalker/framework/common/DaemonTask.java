package com.webwalker.framework.common;

import com.webwalker.framework.utils.Loggers;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

public abstract class DaemonTask implements Runnable {
	private static final String TAG = "DaemonTask";
	protected final long DEF_FIRST_INTERVAL = 5 * 60 * 1000;
	protected final long DEF_TASK_INTERVAL = 20 * 60 * 1000;

	protected Context mContext = null;

	protected HandlerThread mHandlerThread = null;
	protected Handler mHandler = null;
	protected boolean mbLive = false;
	protected long mFirstInterval = DEF_FIRST_INTERVAL;
	protected long mTaskInterval = DEF_TASK_INTERVAL;

	abstract public void doRun();

	@Override
	public void run() {
		Loggers.d(getTag(), "DaemonTask is running.");
		try {
			doRun();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getHandler().postDelayed(this, getTaskInterval());
	}

	public DaemonTask() {
		mHandlerThread = new HandlerThread(this.getTag());
		mHandlerThread.start();
	}

	public void setContext(Context ct) {
		mContext = ct;
	}

	public boolean isNullContext() {
		return (null == mContext);
	}

	public Handler getHandler() {
		if (null == mHandler) {
			mHandler = new Handler(mHandlerThread.getLooper());
		}
		return mHandler;
	}

	public void Trigger() {
		getHandler().post(this);
	}

	public long getFirstInterval() {
		return mFirstInterval;
	}

	public long getTaskInterval() {
		return mTaskInterval;
	}

	public String getTag() {
		return TAG;
	}

	public void start() {
		Loggers.d(getTag(), "FIRST_INTERVAL is " + getFirstInterval()
				+ ", TASK_INTERVAL is " + getTaskInterval() + ", isAlive is "
				+ mbLive);
		if (!mbLive) {
			if (getFirstInterval() > 0) {
				getHandler().postDelayed(this, getFirstInterval());
			} else {
				getHandler().post(this);
			}
			mbLive = true;
		}
	}

	public boolean isAlive() {
		return mbLive;
	}
}
