package com.webwalker.framework.widget;

import com.webwalker.framework.interfaces.IAction;
import com.webwalker.framework.interfaces.ICallback2;
import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.utils.StringUtil;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

/**
 * send sms widget
 * 
 * @author xu.jian
 * 
 */
public class ButtonSender<T> {

	private Button button;
	private int timeCounter = 30;
	private int currentTime = timeCounter;
	private String originText = "";
	private String textFormat = "{0}";
	private int firstDrawable, lastDrawable;
	private volatile boolean running = true;
	private View focusWidget = null;// when button pressed, focus this view
	private AsyncTask<Object, Integer, Integer> asyncTask;

	public ButtonSender(String originText, String textFormat,
			int firstDrawable, int lastDrawable) {
		this.originText = originText;
		this.textFormat = textFormat;
		this.firstDrawable = firstDrawable;
		this.lastDrawable = lastDrawable;
	}

	public void bindButton(Button btn, View focusView) {
		button = btn;
		focusWidget = focusView;
	}

	public void sendSmsCode(Button btn, String mobile,
			IAction<T, String> action, ICallback2<Void, Object> callback) {
		sendSmsCode(btn, null, mobile, action, callback);
	}

	public void sendSmsCode(Button btn, View focusView, String mobile,
			IAction<T, String> action, ICallback2<Void, Object> callback) {
		bindButton(btn, focusView);
		if (focusView != null)
			focusView.requestFocus();
		asyncTask = new AsyncSender(action, callback).execute(mobile);
	}

	public void setTextFormat(String textFormat) {
		this.textFormat = textFormat;
	}

	public void setTimeCounter(int timeCounter) {
		this.timeCounter = timeCounter;
	}

	public void setRunning(boolean r) {
		running = r;
	}

	public class AsyncSender extends AsyncTask<Object, Integer, Integer> {
		ICallback2<T, String> action = null;
		ICallback2<Void, Object> callback = null;

		public AsyncSender(ICallback2<T, String> action,
				ICallback2<Void, Object> callback) {
			this.action = action;
			this.callback = callback;
		}

		@Override
		protected void onPreExecute() {
			Loggers.d("onPreExecute...");
			if (lastDrawable != 0)
				button.setBackgroundResource(lastDrawable);
			button.setEnabled(false);
			currentTime = timeCounter;
			running = true;
		}

		@Override
		protected Integer doInBackground(final Object... params) {

			Loggers.d("doInBackground...");
			new Thread(new Runnable() {
				public void run() {
					// 发送
					T result = action.action(params[0].toString());
					if (callback != null)
						callback.action(result);
				}
			}).start();

			while (running) {
				try {
					Loggers.d("status:" + this.getStatus().toString() + ","
							+ currentTime);
					if (currentTime == 0)
						break;
					this.publishProgress(currentTime);
					Thread.sleep(1000);
					currentTime--;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			button.setEnabled(true);
			button.setText(originText);
			if (firstDrawable != 0)
				button.setBackgroundResource(firstDrawable);
			currentTime = 1;
			running = false;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			button.setText(StringUtil.format(textFormat, values[0] + ""));
		}
	}
}