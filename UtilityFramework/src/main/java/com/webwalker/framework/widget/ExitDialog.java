package com.webwalker.framework.widget;

import com.webwalker.framework.common.BaseFrameworkDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class ExitDialog extends BaseFrameworkDialog {

	private Context context = null;
	private Button exitBtn = null, cancelBtn = null;

	public ExitDialog(Context context, int style, int layoutId) {
		super(context, style);
		this.context = context;
		setContentView(layoutId);
		initDialog();
	}

	private void initDialog() {
		// exitBtn = (Button) findViewById(R.id.exit_btn);
		// cancelBtn = (Button) findViewById(R.id.cancle_btn);
		exitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				System.exit(0);
			}
		});

		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
	}

}
