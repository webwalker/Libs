package com.webwalker.framework.system.log;

import com.webwalker.framework.R;
import com.webwalker.framework.utils.FileUtil;
import com.webwalker.framework.utils.UriUtil;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

public class DebugActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);

		final EditText et = (EditText) findViewById(R.id.editText1);
		et.setHorizontallyScrolling(true);

		final String logs = UriUtil.getStringValue(DebugActivity.this, "logs");
		final String path = UriUtil.getStringValue(DebugActivity.this, "path");

		// HandlerThread thread = new HandlerThread("netthread");
		// thread.start();
		// Handler r = new Handler(thread.getLooper());
		// r.post(new Runnable() {
		// @Override
		// public void run() {
		// try {
		if (!TextUtils.isEmpty(path)) {
			et.setText(FileUtil.readFileByLines(path));
		}
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// });
	}
}
