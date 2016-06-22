package com.webwalker.framework.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.zxing.WriterException;

/**
 * @author xu.jian
 * 
 */
public class QRCodeUtil {
	public static BitmapDrawable createQRCode(Resources res, String content) {
		try {
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode(content, 190);
			BitmapDrawable drawable = new BitmapDrawable(res, qrCodeBitmap);
			return drawable;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

}
