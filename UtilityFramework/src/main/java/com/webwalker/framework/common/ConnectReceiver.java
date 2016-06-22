package com.webwalker.framework.common;

import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.utils.NetworkUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * 监听网络状态变化
 * 
 * @author xu.jian
 * 
 */
public class ConnectReceiver {

	public static boolean hasConnected = true;

	private NetworkReceiver connectReceiver = null;

	public void start(Context ct) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		connectReceiver = new NetworkReceiver();
		ct.registerReceiver(connectReceiver, filter);
	}

	public void stop(Context ct) {
		if (connectReceiver != null)
			ct.unregisterReceiver(connectReceiver);
	}

	public class NetworkReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Loggers.d("ConnectReceiver",
					"onReceive action = " + intent.getAction());
			hasConnected = NetworkUtils.isConnected(context);
		}
	}
}
