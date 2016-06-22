package com.webwalker.framework.utils;

import com.webwalker.framework.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

/**
 * @author xu.jian
 * 
 */
public class NotifyUtil {
	private static final int NOTIFICATION_ID_1 = 0;
	private static final int NOTIFICATION_ID_2 = 1;
	private static final int NOTIFICATION_ID_3 = 2;
	private static final int NOTIFICATION_ID_4 = 3;
	private static final int NOTIFICATION_ID_5 = 4;
	private static final int NOTIFICATION_ID_6 = 5;
	private static final int NOTIFICATION_ID_7 = 6;
	private static final int NOTIFICATION_ID_8 = 7;
	private static int messageNum = 0;
	private NotificationManager manager;
	private Context context;
	private Bitmap icon;

	public NotifyUtil(Context c) {
		this.context = c;
		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void showNotify(int iconId, String title, String desc, int number) {
		icon = BitmapFactory.decodeResource(context.getResources(), iconId);
		Notification notification = new NotificationCompat.Builder(context)
				.setLargeIcon(icon).setSmallIcon(R.mipmap.ic_launcher)
				.setTicker("showNormal").setContentInfo("contentInfo")
				.setContentTitle(title).setContentText(desc).setNumber(number)
				.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
				// .setContentIntent(intent)
				.build();
		manager.notify(NOTIFICATION_ID_1, notification);
	}

	public void showNotify(Context context, int resourceId, String title,
			String body, String appTitle) {
		Notification notification = new Notification(resourceId, appTitle,
				System.currentTimeMillis());

		// notification.defaults |= Notification.DEFAULT_SOUND;
		// notification.sound = Uri
		// .parse("file:///sdcard/notification/ringer.mp3");

		notification.defaults |= Notification.DEFAULT_VIBRATE;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;

		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;

		notification.flags |= notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(), 0);
		notification.setLatestEventInfo(context, title, body, pendingIntent);
		notificationManager.notify(0, notification);
	}
}
