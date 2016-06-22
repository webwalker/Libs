package com.webwalker.framework.system.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webwalker.framework.beans.ContractPersion;
import com.webwalker.framework.utils.Loggers;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * 联系人
 * 
 * @author xu.jian
 * 
 */
public class ContactWrapper {
	private List<ContractPersion> persons = Collections
			.synchronizedList(new ArrayList<ContractPersion>());
	private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
	private Map<Integer, ContractPersion> contactIdMap = Collections
			.synchronizedMap(new HashMap<Integer, ContractPersion>());
	private static ContactWrapper _instance = null;

	public static ContactWrapper getInstance() {
		if (_instance == null) {
			_instance = new ContactWrapper();
		}
		return _instance;
	}

	public List<ContractPersion> getPersons() {
		return persons;
	}

	public void queryContracts(Context context) {
		Loggers.d("queryContracts...");
		asyncQueryHandler = new MyAsyncQueryHandler(
				context.getContentResolver());
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
		// 查询的字段
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
		// 按照sort_key升序查詢
		asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");
	}

	private class MyAsyncQueryHandler extends AsyncQueryHandler {
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			try {
				Loggers.d("queryContracts:onQueryComplete");
				if (cursor != null && cursor.getCount() > 0) {
					persons.clear();
					contactIdMap.clear();
					cursor.moveToFirst(); // 游标移动到第一项
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						String name = cursor.getString(1);
						String number = cursor.getString(2);
						String sortKey = cursor.getString(3);
						int contactId = cursor.getInt(4);
						Long photoId = cursor.getLong(5);
						String lookUpKey = cursor.getString(6);

						if (contactIdMap.containsKey(contactId)) {
							// 无操作
						} else {
							// 创建联系人对象
							ContractPersion contact = new ContractPersion();
							contact.setName(name);
							contact.setPhone(number);
							contact.setSortKey(sortKey);
							contact.setIconId(photoId);
							contact.setLookUpKey(lookUpKey);
							if (!persons.contains(contact)) {
								persons.add(contact);
								contactIdMap.put(contactId, contact);
							}
						}
					}
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
}
