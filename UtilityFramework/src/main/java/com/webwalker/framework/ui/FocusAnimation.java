package com.webwalker.framework.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webwalker.framework.R;
import com.webwalker.framework.listener.OnScaleImageChangeListener;
import android.R.interpolator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 用于绘制Foucs效果
 * 
 * mFocusAnim = new FocusAnimation(BesTV_Record.this, mSurfaceView,
 * mZoomFrameLayout);
 * 
 */
public class FocusAnimation implements Callback {
	private static final String TAG = "FocusAnimation";
	private static final int MSG_SURFACE_CREATE = 0x1000;
	private Map<String, View> mUnDraw = null;
	private String mCurViewID = "";
	private Context mContext;
	/**
	 * 用来画普通Focus框的SurfaceView
	 */
	private SurfaceView mSurfaceView = null;
	/**
	 * 用来画选中放大效果，最上层的FrameLayout
	 */
	private FrameLayout mFrameLayout = null;
	/**
	 * 设置有焦点功能，但没有呼吸框及移动动画
	 * 注意：设置了这个，使用时必须要实现FoucsInterface中FoucsEvent方法，因为你设置的VIEW获得焦点后，会调用这个方法
	 */
	private List<View> mNoAniViews = new ArrayList<View>();
	private List<View> mNoScaleViews = new ArrayList<View>();
	/**
	 * 放大的图片
	 */
	private ImageView mScaleUpImage = null;
	/**
	 * 放大的焦点图片
	 */
	private ImageView mScaleUpFoucs = null;
	/**
	 * 放大效果的层
	 */
	private FrameLayout mScaleUpFrame = null;
	/**
	 * 焦点离开缩小的图片
	 */
	private ImageView mScaleDownImage = null;
	/**
	 * 速效效果的层
	 */
	private FrameLayout mScaleDownFrame = null;
	private ScaleAnimation mScaleUpAnimation = null;
	private ScaleAnimation mScaleDownAnimation = null;
	/**
	 * 放大率
	 */
	private float mScaleRate = 1.08f;
	/**
	 * 自定义Foucs图片的偏移量
	 */
	private int mFoucsOffset = 18;
	/**
	 * Foucs框透明度
	 */
	private int mFoucsAlpha = 255;
	// private Canvas mCanvas = null;
	/**
	 * FoucsView图片
	 */
	private NinePatch mFoucsImage = null;
	/**
	 * 焦点框资源ID
	 */
	private int mFoucsResID = R.mipmap.record_bound2;
	private OnScaleImageChangeListener mOnScaleImageChangeListener = null;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SURFACE_CREATE:
				if (TextUtils.isEmpty(mCurViewID)) {
					View v = mUnDraw.get(mCurViewID);
					if (v != null) {
						Log.d(TAG, "redraw the foucs of:" + mCurViewID);
						boolean isDraw = drawFoucsView(v);
						if (!isDraw) {
							mUnDraw.put(mCurViewID, v);
						} else {
							mUnDraw.clear();
						}
					}
				}
				break;

			default:
				break;
			}
		}

	};

	public FocusAnimation(final Context context, final SurfaceView surfaceView,
			final FrameLayout frameLayout) {
		this.mContext = context;
		mSurfaceView = surfaceView;
		mFrameLayout = frameLayout;
		mUnDraw = new HashMap<String, View>();
		// 画布背景透明
		mSurfaceView.setZOrderOnTop(true);
		mSurfaceView.getHolder().addCallback(this);
		mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mFoucsImage = getNinePatch(R.mipmap.record_bound2);
		mScaleUpImage = new ImageView(mContext);
		mScaleUpFoucs = new ImageView(mContext);
		mScaleUpFrame = new FrameLayout(mContext);
		mScaleDownImage = new ImageView(mContext);
		mScaleDownFrame = new FrameLayout(mContext);
		mScaleUpAnimation = new ScaleAnimation(1.0f, mScaleRate, 1.0f,
				mScaleRate, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		mScaleUpAnimation.setInterpolator(mContext,
				interpolator.accelerate_decelerate);
		mScaleUpAnimation.setFillEnabled(true);
		mScaleUpAnimation.setFillAfter(true);
		mScaleUpAnimation.setDuration(100);
		// mScaleUpAnimation.setAnimationListener(new AnimationListener() {
		// public void onAnimationStart(Animation animation) {
		// }
		//
		// public void onAnimationRepeat(Animation animation) {
		// }
		//
		// public void onAnimationEnd(Animation animation) {
		// }
		// });
		mScaleUpFoucs.setScaleType(ScaleType.FIT_XY);
		mScaleUpImage.setScaleType(ScaleType.FIT_XY);
	}

	// 设置焦点框偏移量
	public void setOffset(int offset) {
		Log.d(TAG, "setOffset : " + offset);
		mFoucsOffset = offset;
	}

	public void setSurfaceVisible() {
		mSurfaceView.setVisibility(View.VISIBLE);
	}

	public void setSurfaceInvisible() {
		mSurfaceView.setVisibility(View.INVISIBLE);
	}

	public void setFrameLayoutVisible() {
		mFrameLayout.setVisibility(View.VISIBLE);
	}

	public void setFrameLayoutInvisible() {
		mFrameLayout.setVisibility(View.INVISIBLE);
	}

	/**
	 * 传入的ViewGroup下所有的View都会有Foucs效果
	 * 
	 * @param views
	 */
	public void setAllViewChangeFoucs(ViewGroup views) {
		for (int i = 0; i < views.getChildCount(); i++) {
			View v = views.getChildAt(i);
			foucsChange(v);
			if (v instanceof ViewGroup) {
				setAllViewChangeFoucs((ViewGroup) v);
			}
		}
	}

	/**
	 * 使该View会有Foucs效果
	 * 
	 * @param v
	 */
	public void foucsChange(View v) {
		if (v.isFocusable()) {
			v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					((FocusInterface) mContext).FoucsEvent(v, hasFocus);
					if (hasFocus) {
						foucsUp(v);
					} else {
						foucsDown(v);
					}
				}
			});
		}
	}

	/**
	 * 在Restart或者Resume中调用
	 * 
	 * @param v
	 */
	public void onReFoucs(View v) {
		mCurViewID = v.toString();
		mUnDraw.clear();
		for (int i = 0; i < mNoAniViews.size(); i++) {
			if (v == mNoAniViews.get(i)) {
				clearSurface();
				return;
			}
		}
		// if (v instanceof EditText) {
		// stopAnim();
		// ((EditText) v).setSelection(((EditText) v)
		// .getText().toString().length());
		// ((EditText) v).selectAll();
		// return;
		// }

		if (mOnScaleImageChangeListener.getViewBitmap(v) == null) {
			boolean isDraw = drawFoucsView(v);
			if (!isDraw) {
				mUnDraw.put(mCurViewID, v);
			}
		}

	}

	private void foucsUp(View v) {
		mCurViewID = v.toString();
		mUnDraw.clear();
		for (int i = 0; i < mNoAniViews.size(); i++) {
			if (v == mNoAniViews.get(i)) {
				clearSurface();
				return;
			}
		}
		// if (v instanceof EditText) {
		// stopAnim();
		// ((EditText) v).setSelection(((EditText) v)
		// .getText().toString().length());
		// ((EditText) v).selectAll();
		// return;
		// }

		// 放大效果自带了Foucs框，不需要再画。
		if (scaleUp(v)) {
			clearSurface();
		} else {
			boolean isDraw = drawFoucsView(v);
			if (!isDraw) {
				mUnDraw.put(mCurViewID, v);
			}
		}
	}

	private void foucsDown(View v) {
		mScaleUpFrame.clearAnimation();
		scaleDown(v);
	}

	/**
	 * 添加没有Foucs效果的控件
	 * 
	 * @param v
	 */
	public void addNoAnimationView(View v) {
		mNoAniViews.add(v);
	}

	/**
	 * 设置Foucs框图片，必须为9.png类型
	 * 
	 * @param resource
	 */
	public void setFoucsImage(int resource) {
		mFoucsResID = resource;
		mFoucsImage = getNinePatch(resource);
	}

	// 通过资源ID取9图
	private NinePatch getNinePatch(int resource) {
		Bitmap bitmap = BitmapFactory.decodeResource(
				this.mContext.getResources(), resource);
		return (bitmap != null) ? new NinePatch(bitmap,
				bitmap.getNinePatchChunk(), null) : null;
	}

	/**
	 * 画焦不需要放大的点框效果
	 * 
	 * @param view
	 * @param isScaleUp
	 */
	private boolean drawFoucsView(View view) {
		Log.d(TAG, "enter drawFoucsView");
		boolean flag = false;
		if (mFoucsImage == null) {
			Log.e(TAG, "mFoucsImage is null!");
			return flag;
		}

		Canvas canvas = mSurfaceView.getHolder().lockCanvas();
		if (canvas == null) {
			Log.e(TAG, "Canvas is null!");
			return flag;
		}
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		Paint p = new Paint(); // 创建画笔
		int x = 0, y = 0;
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		x = location[0];
		y = location[1];
		int left = 0;
		int top = 0;
		int right = 0;
		int bottom = 0;
		left = x - mFoucsOffset;
		top = y - mFoucsOffset;
		right = view.getWidth() + x + mFoucsOffset;
		bottom = view.getHeight() + y + mFoucsOffset;
		Log.d(TAG, "foucsview :Rect(" + left + "," + top + "," + right + ","
				+ bottom + ")");
		Rect rcDes = new Rect(left, top, right, bottom);
		p.setAlpha(mFoucsAlpha);
		mFoucsImage.draw(canvas, rcDes, p);
		mSurfaceView.getHolder().unlockCanvasAndPost(canvas);// 结束锁定画图，并提交改变
		flag = true;
		Log.d(TAG, "leave drawFoucsView");
		return flag;
	}

	/**
	 * 清除Surface上的图画
	 */
	private void clearSurface() {
		Log.d(TAG, "clearSurface");
		Canvas canvas = mSurfaceView.getHolder().lockCanvas();
		if (canvas == null) {
			Log.e(TAG, "Canvas @ clearSurface is null!");
			return;
		}
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		canvas.drawPaint(paint);
		mSurfaceView.getHolder().unlockCanvasAndPost(canvas);
	}

	/**
	 * 设置放大率
	 * 
	 * @param scaleRate
	 */
	public void setScaleRate(float scaleRate) {
		mScaleRate = scaleRate;
	}

	/**
	 * 设置控件缩放图片资源的监听
	 * 
	 * @param listener
	 */
	public void setScaleImageListener(OnScaleImageChangeListener listener) {
		mOnScaleImageChangeListener = listener;
	}

	/**
	 * 放大特效
	 * 
	 * @param v
	 */
	private boolean scaleUp(View v) {
		boolean flag = false;
		Log.d(TAG, "enter scaleUp ");
		if (mFrameLayout == null || v == null) {
			Log.e(TAG, "zoom framelayout or view is null");
			return flag;
		}
		if (mOnScaleImageChangeListener == null) {
			Log.e(TAG, "mOnScaleImageChangeListener is null");
			return flag;
		}
		for (View noScale : mNoScaleViews) {
			if (noScale == v) {
				Log.d(TAG, "this view needn't scale up");
				return flag;
			}
		}
		mFrameLayout.removeView(mScaleUpFrame);
		// 取控件当前状态截图
		Bitmap bitmap = null;
		bitmap = mOnScaleImageChangeListener.getViewBitmap(v);
		if (bitmap == null) {
			Log.d(TAG, "scale bitmap is null needn't scaleup");
			return flag;
		}

		// 取画图位置
		Rect rect = new Rect();
		v.getGlobalVisibleRect(rect);

		int top = rect.top;
		int left = rect.left;
		// int right = rect.right;
		// int bottom = rect.bottom;
		// int cx = left + Math.max((right - left), 0) / 2;
		// int cy = top + Math.max((bottom - top), 0) / 2;
		int width = rect.width();
		int height = rect.height();

		mScaleUpImage.setImageBitmap(bitmap);
		mScaleUpFoucs.setImageResource(mFoucsResID);
		mScaleUpFrame.removeAllViews();
		FrameLayout.LayoutParams frameLP = (FrameLayout.LayoutParams) mScaleUpFrame
				.getLayoutParams();
		if (frameLP == null) {
			frameLP = new FrameLayout.LayoutParams(width, height);
		}
		frameLP.width = width + mFoucsOffset * 2;
		frameLP.height = height + mFoucsOffset * 2;
		frameLP.leftMargin = Math.max(left - mFoucsOffset, 0);
		frameLP.topMargin = Math.max(top - mFoucsOffset, 0);

		FrameLayout.LayoutParams imageLP = (FrameLayout.LayoutParams) mScaleUpImage
				.getLayoutParams();
		if (imageLP == null) {
			imageLP = new FrameLayout.LayoutParams(width, height);
		}
		imageLP.width = width;
		imageLP.height = height;
		imageLP.gravity = Gravity.CENTER;

		FrameLayout.LayoutParams foucsLP = (FrameLayout.LayoutParams) mScaleUpFoucs
				.getLayoutParams();
		if (foucsLP == null) {
			foucsLP = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		}
		foucsLP.width = FrameLayout.LayoutParams.MATCH_PARENT;
		foucsLP.height = FrameLayout.LayoutParams.MATCH_PARENT;
		mScaleUpFrame.addView(mScaleUpFoucs, foucsLP);
		mScaleUpFrame.addView(mScaleUpImage, imageLP);
		mFrameLayout.addView(mScaleUpFrame, frameLP);

		mScaleUpFrame.startAnimation(mScaleUpAnimation);
		flag = true;
		return flag;
	}

	/**
	 * 刷新控件放大后的图片（用于海报加载完成等界面变化）
	 * 
	 * @param v
	 * @return
	 */
	public boolean refreshScaleImage(View v) {
		boolean flag = false;
		// clearScale();
		Log.d(TAG, "enter scaleUp ");
		if (mFrameLayout == null || v == null) {
			Log.e(TAG, "zoom framelayout or view is null");
			return flag;
		}
		if (mOnScaleImageChangeListener == null) {
			Log.e(TAG, "mOnScaleImageChangeListener is null");
			return flag;
		}
		for (View noScale : mNoScaleViews) {
			if (noScale == v) {
				Log.d(TAG, "this view needn't scale up");
				return flag;
			}
		}

		// 取控件当前状态截图
		Bitmap bitmap = null;
		bitmap = mOnScaleImageChangeListener.getViewBitmap(v);
		if (bitmap == null) {
			Log.d(TAG, "scale bitmap is null needn't scaleup");
			return flag;
		}

		mScaleUpImage.setImageBitmap(bitmap);

		flag = true;
		return flag;
	}

	/**
	 * 缩小效果
	 * 
	 * @param v
	 * @return
	 */
	private boolean scaleDown(View v) {
		boolean flag = false;
		Log.d(TAG, "enter scaleDown ");
		if (mFrameLayout == null || v == null) {
			Log.e(TAG, "zoom framelayout or view is null");
			return flag;
		}
		if (mOnScaleImageChangeListener == null) {
			Log.e(TAG, "mOnScaleImageChangeListener is null");
			return flag;
		}
		for (View noScale : mNoScaleViews) {
			if (noScale == v) {
				// Log.d(TAG, "this view needn't scale down");
				return flag;
			}
		}
		if (mScaleDownFrame != null) {
			mScaleDownFrame.clearAnimation();
			mFrameLayout.removeView(mScaleDownFrame);
		}

		Bitmap bitmap = null;
		bitmap = mOnScaleImageChangeListener.getViewBitmap(v);
		if (bitmap == null) {
			// Log.d(TAG, "scale bitmap is null needn't scaledown");
			return flag;
		}

		Rect rect = new Rect();
		v.getGlobalVisibleRect(rect);
		int top = rect.top;
		int left = rect.left;
		int right = rect.right;
		int bottom = rect.bottom;
		int cx = left + Math.max((right - left), 0) / 2;
		int cy = top + Math.max((bottom - top), 0) / 2;
		int width = rect.width();
		int height = rect.height();

		mScaleDownImage.setImageBitmap(bitmap);
		mScaleDownFrame.removeAllViews();
		FrameLayout.LayoutParams frameLP = (FrameLayout.LayoutParams) mScaleDownFrame
				.getLayoutParams();
		if (frameLP == null) {
			frameLP = new FrameLayout.LayoutParams(width, height);
		}
		frameLP.width = width + mFoucsOffset * 2;
		frameLP.height = height + mFoucsOffset * 2;
		frameLP.leftMargin = Math.max((cx - width / 2) - mFoucsOffset, 0);
		frameLP.topMargin = Math.max((cy - height / 2) - mFoucsOffset, 0);

		FrameLayout.LayoutParams imageLP = (FrameLayout.LayoutParams) mScaleUpImage
				.getLayoutParams();
		if (imageLP == null) {
			imageLP = new FrameLayout.LayoutParams(width, height);
		}
		imageLP.width = width;
		imageLP.height = height;
		imageLP.gravity = Gravity.CENTER;
		mScaleDownFrame.addView(mScaleDownImage, imageLP);
		mFrameLayout.addView(mScaleDownFrame, frameLP);

		mScaleDownAnimation = new ScaleAnimation(mScaleRate, 1.0f, mScaleRate,
				1.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		mScaleDownAnimation.setFillEnabled(true);
		mScaleDownAnimation.setFillAfter(true);
		mScaleDownAnimation.setDuration(100);
		mScaleDownAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mScaleDownFrame.removeAllViews();
			}
		});
		mScaleDownFrame.startAnimation(mScaleDownAnimation);
		flag = true;
		return flag;
	}

	/**
	 * 清除屏幕上的缩放效果
	 */
	public void clearScale() {
		if (mFrameLayout == null) {
			Log.e(TAG, "mFrameLayout is null!");
			return;
		}
		if (mScaleDownFrame != null) {
			mScaleDownFrame.clearAnimation();
			mScaleDownFrame.removeAllViews();
		}
		if (mScaleUpFrame != null) {
			mScaleUpFrame.clearAnimation();
			mScaleUpFrame.removeAllViews();
		}
		mFrameLayout.removeAllViews();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		if (mUnDraw.size() > 0) {
			Log.d(TAG, "has undraw view , try to redraw");
			mHandler.sendEmptyMessage(MSG_SURFACE_CREATE);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}