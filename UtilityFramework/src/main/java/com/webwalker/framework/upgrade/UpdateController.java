package com.webwalker.framework.upgrade;

import org.json.JSONObject;

import com.webwalker.framework.utils.HttpUtil;
import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.utils.AppUtil;
import android.content.Context;

public class UpdateController {
	private int currentVersionCode = 0;
	private Context context;
	private DownEntity request = null;
	private UpdateEntity update = null;
	private HttpDownload down = null;

	public UpdateController(Context context, DownEntity entity) {
		this.context = context;
		request = entity;
	}

	private void getApkVersionInfo() {
		if (request == null)
			return;
		Loggers.d(DownEntity.TAG, "apkInfoUrl:" + request.getUrl());
		String apkInfo = HttpUtil.getAction(request.getUrl());
		if (apkInfo == null)
			return;
		try {
			JSONObject obj = new JSONObject(apkInfo);
			update = new UpdateEntity();
			update.status = obj.getString("status");
			update.versionCode = obj.getInt("version-code");
			update.minVersionCode = obj.getInt("min-version-code");
			update.versionName = obj.getString("version-name");
			update.versionInfo = obj.getString("version-info");
			update.updateTime = obj.getString("update-time");
			update.debugUrl = obj.getString("url-debug");
			update.releaseUrl = obj.getString("url-release");
			Loggers.d(DownEntity.TAG, "fetch latest apk successfully!");
		} catch (Exception ex) {
			Loggers.e(DownEntity.TAG, "Error occurred on refreshLatestApk(),"
					+ ex.getMessage());
		}
	}

	public void startUpdate() {
		down = new HttpDownload(context, this.getRequest(), this.getRequest()
				.getCallback());
		down.Start();
	}

	public void cancel() {
		down.Cancel();
	}

	public boolean isMustUpdate() {
		int currentVersionCode = AppUtil.getVersionCode(context);
		if (currentVersionCode < update.minVersionCode) {
			update.isMustUpdate = true;
			return true;
		}
		return false;
	}

	public boolean isNeedUpdate() {
		getApkVersionInfo();
		if (update == null)
			return false;

		this.currentVersionCode = AppUtil.getVersionCode(context);
		if (currentVersionCode < update.versionCode) {
			update.isNeedUpdate = true;
			return true;
		} else {
			return false;
		}
	}

	public DownEntity getRequest() {
		request.setUrl(update.getReleaseUrl());
		return request;
	}

	public void setRequest(DownEntity request) {
		this.request = request;
	}

	public UpdateEntity getUpdate() {
		return update;
	}

	public void setUpdate(UpdateEntity update) {
		this.update = update;
	}

	/**
	 * 有应用商店的不需要升级
	 */
	public void isNeedCheck() {

	}
}
