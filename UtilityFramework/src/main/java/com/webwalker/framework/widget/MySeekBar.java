package com.webwalker.framework.widget;

import com.webwalker.framework.utils.Loggers;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.SeekBar;

/**
 * @author xu.jian
 * 
 */
public class MySeekBar extends SeekBar {
	private static final String TAG = "MySeekBar";
	public final static boolean DEBUG = false;

	String text;
	Paint mPaint;
	int currProgress = 0;

	private OnKeyDownListener mOnKeyDownListener;
	private OnKeyUpListener mOnKeyUpListener;

	public MySeekBar(Context context) {
		super(context);
		initText();
	}

	public MySeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initText();
	}

	public MySeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		// setText(progress);
		currProgress = progress;
		super.setProgress(progress);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// this.setText();
		try {
			if (mPaint == null || text == null || this.getMax() == 0)
				return;
			Rect rect = new Rect();
			this.mPaint.setTextSize(18);
			this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
			int x = (getWidth() - 75) * currProgress / this.getMax()
					- rect.centerX() + 38;
			Loggers.d(TAG, "compute time pos is : " + x);
			int y = 20;
			canvas.drawText(this.text, x, y, this.mPaint);
		} catch (ArithmeticException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mOnKeyDownListener != null)
			return mOnKeyDownListener.onKeyDown(keyCode, event);
		else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (mOnKeyUpListener != null)
			return mOnKeyUpListener.onKeyUp(keyCode, event);
		else
			return super.onKeyUp(keyCode, event);
	}

	public void setOnKeyDownListener(OnKeyDownListener l) {
		mOnKeyDownListener = l;
	}

	public void setOnKeyUpListener(OnKeyUpListener l) {
		mOnKeyUpListener = l;
	}

	public interface OnKeyDownListener {
		public boolean onKeyDown(int keyCode, KeyEvent event);
	}

	public interface OnKeyUpListener {
		public boolean onKeyUp(int keyCode, KeyEvent event);
	}

	public void setProgressTimeText(String time) {
		this.text = time;
	}

	// init painter
	private void initText() {
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.WHITE);

	}

	private void setText() {
		setText(this.getProgress());
	}

	public String getText() {
		return text;
	}

	// set time text
	private void setText(int progress) {
		int i = (progress * 100) / this.getMax();
		this.text = String.valueOf(i) + "%";
	}
}
