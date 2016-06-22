package com.webwalker.framework.widget.TextView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class MqrqueTextView extends TextView {

	public MqrqueTextView(Context context) {
		super(context);
	}

	public MqrqueTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MqrqueTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
	}

}