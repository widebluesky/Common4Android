package com.picperweek.common4android.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Bitmap特效工具类
 * @author widebluesky
 *
 */
public class BitmapEffectUtil {

	@SuppressWarnings("unused")
	private static float[] defaultColorMatrixArray = { 1, 0, 0, 0, 0, 0, 1, 0,
			0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };

	/**
	 * 图片变亮
	 * 
	 * @param bitmap
	 * @param brightness
	 * @return
	 */
	public static Bitmap setBrightness(Bitmap bitmap, int brightness) {
		// -255 ~ 0 ~ 255
		Bitmap bm = Bitmap.createBitmap(bitmap);
		Paint paint = new Paint();
		paint.setColorFilter(null);
		Canvas canvas = new Canvas(bm);
		ColorMatrix cm = new ColorMatrix();
		// 设置颜色矩阵
		float[] array = { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0,
				1, 0, brightness, 0, 0, 0, 1, 0 };
		cm.set(array);

		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(bm, 0, 0, paint);
		return bm;
	}

	/**
	 * 设置rgb偏移
	 * 
	 * @param bitmap
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static Bitmap setRGB(Bitmap bitmap, int red, int green, int blue) {
		// -255 ~ 0 ~ 255
		Bitmap bm = Bitmap.createBitmap(bitmap);

		Paint paint = new Paint();
		paint.setColorFilter(null);
		Canvas canvas = new Canvas(bm);
		ColorMatrix cm = new ColorMatrix();
		// 设置颜色矩阵
		float[] array = { 1, 0, 0, 0, red, 0, 1, 0, 0, green, 0, 0, 1, 0, blue,
				0, 0, 0, 1, 0 };
		cm.set(array);

		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(bm, 0, 0, paint);

		return bm;
	}

	/**
	 * 老照片效果
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap oldRemeberBitmap(Bitmap bitmap) {

		Bitmap bm = Bitmap.createBitmap(bitmap);

		Paint paint = new Paint();
		paint.setColorFilter(null);
		Canvas canvas = new Canvas(bm);
		ColorMatrix cm = new ColorMatrix();
		// 设置颜色矩阵
		float[] array = { (float) 0.393, (float) 0.769, (float) 0.189, 0, 0,
				(float) 0.349, (float) 0.686, (float) 0.168, 0, 0,
				(float) 0.272, (float) 0.534, (float) 0.131, 0, 0, 0, 0, 0, 1,
				0 };
		cm.set(array);

		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(bm, 0, 0, paint);

		return bm;
	}

}
