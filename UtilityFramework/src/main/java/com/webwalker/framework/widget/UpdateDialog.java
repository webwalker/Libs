package com.webwalker.framework.widget;

import com.webwalker.framework.R;
import com.webwalker.framework.common.BaseFrameworkDialog;
import com.webwalker.framework.upgrade.DownCallback;
import com.webwalker.framework.upgrade.DownCallbackEntity;
import com.webwalker.framework.upgrade.DownEntity;
import com.webwalker.framework.upgrade.UpdateController;
import com.webwalker.framework.utils.Loggers;
import com.webwalker.framework.utils.MessageUtil;
import com.webwalker.framework.utils.AppUtil;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author xujian
 */
public class UpdateDialog extends BaseFrameworkDialog {
	private Context context = null;
	private UpdateController controller = null;
	private Button downBtn = null, cancelBtn = null;
	private TextView txtVersionInfo = null;
	private UpdateProgressBar progressBar = null;
	private UpdateDialog dialog = null;

	public UpdateDialog(Context context, UpdateController c) {
		super(context, R.style.default_dialog);
		this.context = context;
		this.controller = c;
		dialog = this;
		setContentView(R.layout.dialog_update);
		initDialog();
	}

	private void initDialog() {
		downBtn = (Button) findViewById(R.id.exit_btn);
		cancelBtn = (Button) findViewById(R.id.cancle_btn);
		txtVersionInfo = (TextView) findViewById(R.id.txtVersionInfo);
		txtVersionInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
		progressBar = (UpdateProgressBar) findViewById(R.id.progressBar);
		downBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				downBtn.setVisibility(View.GONE);
				cancelBtn.requestFocus();
				txtVersionInfo.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				if (controller.getRequest().getCallback() == null)
					controller.getRequest().setCallback(getCallBack());
				controller.startUpdate();
			}
		});
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				controller.cancel();
				dismiss();
			}
		});
	}

	private DownCallback getCallBack() {
		DownCallback callback = new DownCallback() {
			@Override
			public void callBack(DownCallbackEntity entity) {
				Loggers.d(DownEntity.TAG,
						entity.getProgress() + "," + entity.getDownloadedSize());

				int progress = entity.getProgress();
				progressBar.setProgress(entity.getProgress());
				try {
					if (progress >= 100) {
						dialog.hide();
						Loggers.d(DownEntity.TAG, "下载完毕,开始安装...");
						MessageUtil.showCustomToast(context, "下载完毕，正在安装",
								Gravity.BOTTOM);
						AppUtil.installApk(context, controller.getRequest()
								.getSaveFileName());
					}
				} catch (Exception e) {
					Loggers.e(DownEntity.TAG, e.getMessage());
				}
			}
		};
		return callback;
	}
}
