package com.webwalker.framework.widget;

import java.util.Calendar;

import com.webwalker.framework.R;
import com.webwalker.framework.utils.UIUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * 功能描述：实现日期时间选择器
 * 
 * @author xujian
 */
public class MyDateTimePicker {

	private EditText etTime;
	private Context context;

	public MyDateTimePicker(Context c) {
		this.context = c;
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			View view = UIUtils.getView(context, R.layout.date_time_dialog);
			final DatePicker datePicker = (DatePicker) view
					.findViewById(R.id.date_picker);
			final TimePicker timePicker = (TimePicker) view
					.findViewById(R.id.time_picker);
			builder.setView(view);

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH), null);

			timePicker.setIs24HourView(true);
			timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(Calendar.MINUTE);

			final int inType = etTime.getInputType();
			etTime.setInputType(InputType.TYPE_NULL);
			etTime.onTouchEvent(event);
			etTime.setInputType(inType);
			etTime.setSelection(etTime.getText().length());

			builder.setTitle("选取起始时间");
			builder.setPositiveButton("确  定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							StringBuffer sb = new StringBuffer();
							sb.append(String.format("%d-%02d-%02d",
									datePicker.getYear(),
									datePicker.getMonth() + 1,
									datePicker.getDayOfMonth()));
							sb.append("  ");
							sb.append(timePicker.getCurrentHour()).append(":")
									.append(timePicker.getCurrentMinute());
							etTime.setText(sb);
							dialog.cancel();
						}
					});
			Dialog dialog = builder.create();
			dialog.show();
		}
		return true;
	}

	public EditText getEtTime() {
		return etTime;
	}

	public void setEtTime(EditText etTime) {
		this.etTime = etTime;
	}

}