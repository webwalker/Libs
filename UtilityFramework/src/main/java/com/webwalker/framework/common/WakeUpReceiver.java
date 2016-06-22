package com.webwalker.framework.common;

import com.webwalker.framework.utils.Loggers;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author xu.jian
 * 
 */
public class WakeUpReceiver extends BroadcastReceiver {

	/**
	 * 休眠广播
	 */

	public static final String SUSPEND = "android.intent.action.SCREEN_OFF";
	/**
	 * 唤醒广播
	 */
	public static final String WAKEUP = "android.intent.action.SCREEN_ON";

	public WakeUpReceiver() {
		Loggers.d("WakeUpReceiver");
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		Loggers.d("WakeUpReceiver onReceive");

		// if (action.equals(SUSPEND)) {
		// setPlayStatus(true);
		// } else if (action.equals(WAKEUP)) {
		// setPlayStatus(false);
		// }
	}

	private void setPlayStatus(boolean pause) {

	}

}
