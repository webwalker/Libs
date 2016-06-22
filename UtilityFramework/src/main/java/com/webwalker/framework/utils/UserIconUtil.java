package com.webwalker.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author xu.jian
 * 
 */
public class UserIconUtil {
	/**
	 * 拍照
	 */
	private static final int PHOTO_REQUEST_CAMERA = 1;
	/**
	 * 从相册中选择
	 */
	private static final int PHOTO_REQUEST_GALLERY = 2;
	/**
	 * 结果
	 */
	private static final int PHOTO_REQUEST_CUT = 3;
	private static final String PHOTO_FILE_NAME = "_temp_photo.jpg";
	public Bitmap bitmap;
	private File tempFile;
	private boolean saveFile = false;
	private static Activity act;

	public UserIconUtil(Activity activity) {
		this.act = activity;
	}

	public File getTempFile() {
		return tempFile;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

	public boolean isSaveFile() {
		return saveFile;
	}

	public void setSaveFile(boolean saveFile) {
		this.saveFile = saveFile;
	}

	/**
	 * 打开系统相册
	 * 
	 * @param act
	 */
	public void getFromImage() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		// intent.setType("image/*");
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		act.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/**
	 * 从相机获取
	 * 
	 * @param view
	 */
	public void getFromCamera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (StorageUtil.hasSDCard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		act.startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	/**
	 * 上传图片
	 * 
	 * @param bitmap
	 * @param url
	 * @param paraName
	 */
	public void upload(Bitmap bitmap, String url, String paraName) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			byte[] buffer = out.toByteArray();
			byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
			String photo = new String(encode);

			RequestParams params = new RequestParams();
			params.put(paraName, photo);
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {
							MessageUtil.showShortToast(act, "头像上传成功!");
						} else {
							MessageUtil.showShortToast(act, "头像上传失败，网络错误码："
									+ statusCode);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					MessageUtil.showShortToast(act, "头像上传失败，网络错误码："
							+ statusCode);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 回调，调用结束后需要手动调用super.onActivityResult
	 * 
	 * @param iv
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(ImageView iv, int requestCode, int resultCode,
			Intent data) {
		switch (requestCode) {
		case PHOTO_REQUEST_GALLERY:
			if (data == null || iv == null) {
				act.finish();
				return;
			}
			// 得到图片的全路径
			Uri uri = data.getData();
			startImageAction(uri, 250, 250, PHOTO_REQUEST_CUT, true);
			break;
		case PHOTO_REQUEST_CAMERA:
			if (!StorageUtil.hasSDCard()) {
				MessageUtil.showShortToast(act, "未找到存储卡，无法存储照片！");
				return;
			}
			tempFile = StorageUtil.getSDCardFile(PHOTO_FILE_NAME);
			startImageAction(Uri.fromFile(tempFile), 250, 250,
					PHOTO_REQUEST_CUT, true);
			break;
		case PHOTO_REQUEST_CUT:
			try {
				bitmap = data.getParcelableExtra("data");
				iv.setImageBitmap(bitmap);
				if (saveFile) {
					Loggers.d("save Image...");
					String temPath = ImageUtil.saveImage(act, "temp_photo.png",
							bitmap);
					tempFile = new File(temPath);
				}
				// boolean delete = tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 * @param outputX
	 * @param outputY
	 * @param requestCode
	 *            eg:PHOTO_REQUEST_CUT
	 * @param isCrop
	 */
	public void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// 图片格式
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		// 取消人脸识别
		intent.putExtra("noFaceDetection", true);
		// true:不返回uri，false：返回uri
		intent.putExtra("return-data", true);
		act.startActivityForResult(intent, requestCode);
	}

	/**
	 * 将图片变为圆角
	 * 
	 * @param bitmap
	 *            原Bitmap图片
	 * @param pixels
	 *            图片圆角的弧度(单位:像素(px))
	 * @return 带有圆角的图片(Bitmap 类型)
	 */
	public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 保存裁剪的头像
	 * 
	 * @param data
	 */
	public void saveCropAvator(Intent data, ImageView iv) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				bitmap = toRoundCorner(bitmap, 10);// 调用圆角处理方法
				iv.setImageBitmap(bitmap);
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}
}
