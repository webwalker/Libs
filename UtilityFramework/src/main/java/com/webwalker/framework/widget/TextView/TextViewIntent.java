package com.webwalker.framework.widget.TextView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

/**
 * 可调整到指定Activity、URL、其他系统组件的文本
 * 
 * @author xu.jian
 * 
 */
public class TextViewIntent extends TextView {

	public TextViewIntent(final Context context, final Intent intent) {
		super(context);
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				context.startActivity(intent);
			}
		});
	}

}
