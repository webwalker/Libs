package com.webwalker.framework.system.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webwalker.framework.beans.CallLogBean;
import com.webwalker.framework.interfaces.ICallback;
import com.webwalker.framework.utils.Loggers;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

/**
 * 通话记录
 * 
 * @author xu.jian
 * 
 */
public class DialRecordWrapper {
	private AsyncQueryHandler asyncQuery;
	private List<CallLogBean> callLogs = Collections
			.synchronizedList(new ArrayList<CallLogBean>());
	private ICallback<List<CallLogBean>> callback;
	private Map<String, Integer> callCounter = Collections
			.synchronizedMap(new HashMap<String, Integer>());
	private static DialRecordWrapper _instance = null;

	public static DialRecordWrapper getInstance() {
		if (_instance == null) {
			_instance = new DialRecordWrapper();
		}
		return _instance;
	}

	public List<CallLogBean> getLogs() {
		return callLogs;
	}

	public void queryDialRecord(Context context) {
		Loggers.d("queryDialRecord...");
		asyncQuery = new MyAsyncQueryHandler(context.getContentResolver());
		Uri uri = CallLog.Calls.CONTENT_URI;
		String[] projection = { CallLog.Calls.DATE, // 日期
				CallLog.Calls.NUMBER, // 号码
				CallLog.Calls.TYPE, // 类型
				CallLog.Calls.CACHED_NAME, // 名字
				CallLog.Calls._ID, // id
		};
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
	}

	public void queryDialRecord(Context context, ICallback<List<CallLogBean>> cb) {
		this.callback = cb;
		queryDialRecord(context);
	}

	private class MyAsyncQueryHandler extends AsyncQueryHandler {
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			try {
				Loggers.d("queryDialRecord:onQueryComplete");
				if (cursor != null && cursor.getCount() > 0) {
					Date date;
					callLogs.clear();
					callCounter.clear();
					cursor.moveToFirst(); // 游标移动到第一项
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						date = new Date(cursor.getLong(cursor
								.getColumnIndex(CallLog.Calls.DATE)));
						String number = cursor.getString(cursor
								.getColumnIndex(CallLog.Calls.NUMBER));
						int type = cursor.getInt(cursor
								.getColumnIndex(CallLog.Calls.TYPE));
						String cachedName = cursor.getString(cursor
								.getColumnIndex(CallLog.Calls.CACHED_NAME));
						int id = cursor.getInt(cursor
								.getColumnIndex(CallLog.Calls._ID));

						CallLogBean callLogBean = new CallLogBean();
						callLogBean.setId(id);
						callLogBean.setPhone(number);
						callLogBean.setName(cachedName);
						if (null == cachedName || "".equals(cachedName)) {
							callLogBean.setName(number);
						}
						callLogBean.setType(type);
						callLogBean.setDate(date);

						if (callCounter.containsKey(number)) {
							callCounter
									.put(number, callCounter.get(number) + 1);
						} else {
							callCounter.put(number, 1);
							callLogs.add(callLogBean);
						}
					}
					// sync call times
					for (CallLogBean c : callLogs) {
						c.setCount(callCounter.get(c.getPhone()));
					}
					if (callback != null)
						callback.action(callLogs);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null)
					cursor.close();
			}
			super.onQueryComplete(token, cookie, cursor);
		}
	}

	/**
	 * 保存通话记录
	 * 
	 * @param context
	 * @param number
	 * @param name
	 */
	public void saveCalllog(Context context, String number, String name) {
		try {
			ContentValues values = new ContentValues();
			values.put(CallLog.Calls.NUMBER, number);
			values.put(CallLog.Calls.CACHED_NAME, name);
			values.put(CallLog.Calls.TYPE, CallLog.Calls.OUTGOING_TYPE);
			values.put(CallLog.Calls.DATE, System.currentTimeMillis());
			values.put(CallLog.Calls.DURATION, 1);
			context.getContentResolver().insert(CallLog.Calls.CONTENT_URI,
					values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
