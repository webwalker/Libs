package com.webwalker.framework.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @author xu.jian
 * 
 */
public class AnimUtil {

	private View view;

	public AnimUtil(View v) {
		this.view = v;
	}

	public void translate(long durations, final float fromX, final float toX,
			final float fromY, final float toY) {
		TranslateAnimation animation = new TranslateAnimation(fromX, toX,
				fromY, toY);
		// animation.setInterpolator(new
		// OvershootInterpolator());//AccelerateInterpolator
		animation.setDuration(durations);
		animation.setStartOffset(0);// 开始前的等待时间
		// animation.setFillEnabled(true);
		animation.setFillAfter(true); // 播放完留在执行完的状态
		// animation.setAnimationListener(new Animation.AnimationListener() {
		// @Override
		// public void onAnimationStart(Animation animation) {
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// // TranslateAnimation anim = new TranslateAnimation(0,0,0,0);
		// // view.setAnimation(anim);
		// int left = view.getLeft() + (int) (toX - fromX);
		// int top = view.getTop() + (int) (toY - fromY);
		// int width = view.getWidth();
		// int height = view.getHeight();
		// view.clearAnimation();
		// view.layout(left, top, left + width, top + height);
		// }
		// });
		view.startAnimation(animation);
	}

	public void stopTranslate() {
		if (view != null)
			view.clearAnimation();
	}

	public void alpha(long durations, final float fromAlpha, final float toAlpha) {
		final Animation alpha = new AlphaAnimation(fromAlpha, toAlpha);
		alpha.setDuration(durations);
		alpha.setRepeatCount(1000);
		alpha.setRepeatMode(Animation.REVERSE);
		view.startAnimation(alpha);
	}

	public void stopAlpha() {
		if (view != null)
			view.clearAnimation();
	}
}
