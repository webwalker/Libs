package com.webwalker.framework.upgrade;

import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.widget.UpdateDialog;
import android.content.Context;
import android.os.AsyncTask;

public class CheckVersionAsyncTask extends AsyncTask<String, Integer, Integer> {
	private Context context;
	private DownEntity downEntity = null;
	private UpdateController controller;

	public CheckVersionAsyncTask(Context context, DownEntity entity) {
		this.context = context;
		this.downEntity = entity;
	}

	@Override
	protected Integer doInBackground(String... updatetype) {
		Loggers.d(DownEntity.TAG, "doInBackground");
		controller = new UpdateController(context, downEntity);
		if (controller.isNeedUpdate())
			return 1;
		return 0;
	}

	@Override
	protected void onPostExecute(Integer result) {
		Loggers.d(DownEntity.TAG, "upgrade flag:" + result);
		if (result == 1)
			new UpdateDialog(context, controller).show();
	}
}
