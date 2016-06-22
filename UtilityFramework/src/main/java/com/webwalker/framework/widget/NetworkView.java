package com.webwalker.framework.widget;

import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.utils.NetworkUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * 当网络连接不通时自动提示给用户，当用户点击时自动刷新
 * 
 * @author xu.jian
 * 
 */
public class NetworkView extends View implements OnClickListener {

	private String text = "网络连接不通，请点击重试";
	private Runnable runnable = null;

	// 重写所有的构造方法比较保险。
	@SuppressLint("NewApi")
	public NetworkView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public NetworkView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public NetworkView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NetworkView(Context context) {
		super(context);
		init();
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	public void init() {
		if (!NetworkUtils.isNetworkAvailable(getContext())) {
			this.setVisibility(View.VISIBLE);
		} else {
			this.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		Loggers.d("retry http request.");
		if (runnable != null) {
			this.post(runnable);
		}
	}
}
