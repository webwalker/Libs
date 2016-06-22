/**
 * 
 */
package com.webwalker.framework.widget;

import java.util.ArrayList;
import java.util.List;

import com.webwalker.framework.beans.ListViewItem;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author xu.jian
 * 
 */
public class ListViewController {
	Context context;
	ListView listView;
	View view = null;

	public ListViewController(Context context, ListView listView) {
		this.context = context;
		this.listView = listView;
	}

	/**
	 * 获取选中的列表项
	 * 
	 * @return
	 */
	public List<ListViewItem> GetCheckList(int primaryKeyId, int checkboxId) {
		List<ListViewItem> list = new ArrayList<ListViewItem>();
		for (int index = 0; index < listView.getChildCount(); index++) {
			RelativeLayout layout = (RelativeLayout) listView.getChildAt(index);
			CheckBox checkBox = (CheckBox) layout.findViewById(checkboxId);

			if (checkBox.isChecked()) {
				ListViewItem item = new ListViewItem();
				TextView tv = (TextView) layout.findViewById(primaryKeyId);
				item.setId(tv.getText().toString());

				list.add(item);
			}
		}
		return list;
	}

	public List<ListViewItem> GetCheckList(int primaryKeyId, int appId,
			int titleId, int checkboxId) {

		TextView tvPrimary, tvAppId, tvTitle;
		CheckBox tvCheckbox;

		List<ListViewItem> list = new ArrayList<ListViewItem>();
		for (int index = 0; index < listView.getChildCount(); index++) {
			view = listView.getChildAt(index);
			if (view instanceof RelativeLayout) {
				RelativeLayout layout = (RelativeLayout) view;
				tvCheckbox = (CheckBox) layout.findViewById(checkboxId);

				if (tvCheckbox.isChecked()) {
					ListViewItem item = new ListViewItem();
					tvPrimary = (TextView) layout.findViewById(primaryKeyId);
					tvAppId = (TextView) layout.findViewById(appId);
					tvTitle = (TextView) layout.findViewById(titleId);

					item.setId(String.valueOf(tvPrimary.getText()));

					List<Object> exts = new ArrayList<Object>();
					exts.add(tvAppId.getText());
					exts.add(tvTitle.getText());
					item.setExt(exts);

					list.add(item);
				}
			}
		}
		return list;
	}

	public void CheckAll(int checkboxId) {
		for (int index = 0; index < listView.getChildCount(); index++) {
			view = listView.getChildAt(index);
			if (view instanceof RelativeLayout) {
				RelativeLayout layout = (RelativeLayout) view;
				CheckBox checkBox = (CheckBox) layout.findViewById(checkboxId);
				checkBox.setChecked(true);
			}
		}
	}

	public int getCheckedCount(int id) {
		int checkedCount = 0;
		for (int index = 0; index < listView.getChildCount(); index++) {
			view = listView.getChildAt(index);
			CheckBox checkbox = (CheckBox) view.findViewById(id);
			if (checkbox.isChecked())
				checkedCount++;
		}
		return checkedCount;
	}
}
