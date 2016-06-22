package com.webwalker.framework.common;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author xu.jian
 * 
 */
public class BaseFrameworkDialog extends Dialog {
	protected View layout;
	protected Context context;

	public BaseFrameworkDialog(Context c) {
		super(c);
		this.context = c;
	}

	public BaseFrameworkDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public BaseFrameworkDialog(Context context, int theme) {
		super(context, theme);
	}

	protected void setAttributes() {
		setCanceledOnTouchOutside(true);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
	}
}
