package com.webwalker.framework.widget;

import com.webwalker.framework.R;
import com.webwalker.framework.common.BaseFrameworkDialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

/**
 * @author xujian
 * 
 */
public class LoadingDialog extends BaseFrameworkDialog {
	ProgressBar bar;

	public LoadingDialog(Context context) {
		super(context, R.style.dialogNoBg);
		setContentView(R.layout.loading);
		// setCanceledOnTouchOutside(true);
		bar = (ProgressBar) findViewById(R.id.loadingBar);
	}

	public void showLoading() {
		bar.startAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.loads));
		this.show();
	}

	public void hideLoading() {
		bar.clearAnimation();
		this.dismiss();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.hideLoading();
		return super.onTouchEvent(event);
	}

}
