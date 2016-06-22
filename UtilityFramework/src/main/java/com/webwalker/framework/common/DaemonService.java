package com.webwalker.framework.common;

import java.util.List;

import com.webwalker.framework.utils.Loggers;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class DaemonService extends Service {
	private static final String TAG="DaemonService";
	
	abstract public List<DaemonTask> getDaemonTasks() ;
	
	public String getTag() {
		return TAG;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Loggers.d(getTag(), "run DaemonService.");
		try {
			List<DaemonTask> tasks = getDaemonTasks();
			if (null != tasks) {
				for (DaemonTask task : tasks) {
					if (!task.isAlive()) {
						Loggers.d(getTag(), "start Daemon task.");
						task.setContext(this.getApplicationContext());
						task.start();
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		this.stopSelf(startId);
		return START_REDELIVER_INTENT;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
