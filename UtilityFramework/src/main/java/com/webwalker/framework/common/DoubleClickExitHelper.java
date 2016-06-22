/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.webwalker.framework.common;

import com.webwalker.framework.interfaces.ICallback;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 类名 DoubleClickExitHelper.java</br> 创建日期 2014年4月28日</br>
 * 
 * @author LeonLee (http://my.oschina.net/lendylongli)</br> Email
 *         lendylongli@gmail.com</br> 更新时间 2014年4月28日 上午12:14:06</br> 最后更新者
 *         LeonLee</br>
 * 
 *         说明 双击退出
 */
public class DoubleClickExitHelper {

	private final Activity mActivity;

	private boolean isOnKeyBacking;
	private Handler mHandler;
	private Toast mBackToast;
	private int tipId = 0;
	private boolean systemExit = false;

	public DoubleClickExitHelper(Activity activity, int tid) {
		mActivity = activity;
		mHandler = new Handler(Looper.getMainLooper());
		this.tipId = tid;
	}

	public void iSystemExit(boolean exit) {
		systemExit = exit;
	}

	/**
	 * Activity onKeyDown事件
	 * */
	public boolean onKeyDown(int keyCode, KeyEvent event,
			ICallback<Void> callback) {
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return false;
		}
		if (isOnKeyBacking) {
			mHandler.removeCallbacks(onBackTimeRunnable);
			if (mBackToast != null) {
				mBackToast.cancel();
			}
			if (callback != null)
				callback.action(null);
			if (systemExit)
				System.exit(0);
			else
				mActivity.finish();
			return true;
		} else {
			isOnKeyBacking = true;
			if (mBackToast == null) {
				mBackToast = Toast.makeText(mActivity, tipId, 3000);
			}
			mBackToast.show();
			mHandler.postDelayed(onBackTimeRunnable, 3000);
			return true;
		}
	}

	private Runnable onBackTimeRunnable = new Runnable() {

		@Override
		public void run() {
			isOnKeyBacking = false;
			if (mBackToast != null) {
				mBackToast.cancel();
			}
		}
	};
}
