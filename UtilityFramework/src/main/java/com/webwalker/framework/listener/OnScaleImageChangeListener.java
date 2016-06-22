package com.webwalker.framework.listener;

import android.graphics.Bitmap;
import android.view.View;

public interface OnScaleImageChangeListener {
	/**
	 * 设置指定控件将要被放大的图片
	 * @param view
	 * @return
	 */
	public Bitmap getViewBitmap(View view);
}
