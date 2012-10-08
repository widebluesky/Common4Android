package com.hiputto.common4android.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;

public class HP_BitmapUtils {

	/**
	 * Bitmap to Drawable
	 * */
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		return bitmapDrawable;
	}

	/**
	 * Bitmap from Drawable
	 * */
	public static Bitmap bitmapFromDrawable(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap转换Byte数组
	 * */
	public static byte[] bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * Bitmap转换自Byte数组
	 * */
	public static Bitmap bitmapFromBytes(byte[] data) {
		if (data.length != 0) {
			return BitmapFactory.decodeByteArray(data, 0, data.length);
		} else {
			return null;
		}
	}

	/**
	 * Bitmap缩放
	 * */
	public static Bitmap bitmap2Resize(Bitmap bitmap, int width, int height) {
		int tempWidth = bitmap.getWidth();
		int tempHeight = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / tempWidth);
		float scaleHeight = ((float) height / tempHeight);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, tempWidth,
				tempHeight, matrix, true);
		return newBitmap;
	}

	/**
	 * Bitmap设置圆角
	 * */
	public static Bitmap bitmap2Round(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * Bitmap设置倒影
	 * */
	public static Bitmap bitmap2ReflectionImage(Bitmap bitmap,
			int reflectionImageDistance) {
		final int reflectionGap = reflectionImageDistance;// 图片和倒影的距离

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);

		canvas.drawBitmap(bitmap, 0, 0, null);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();

		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, Color.WHITE,
				0x00ffffff, TileMode.CLAMP);
		// Color.WHITE 交换 0x70ffffff
		paint.setShader(shader);

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		return bitmapWithReflection;
	}

	/**
	 * Bitmap设置水印图片
	 * */
	public static Bitmap bitmap2WatermarkImage(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		// 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		Paint paint = new Paint();
		// 加入图片
		if (watermark != null) {
			int ww = watermark.getWidth();
			int wh = watermark.getHeight();
			paint.setAlpha(50);
			cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// 在src的右下角画入水印
		}

		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		return newb;
	}
	
	/**
	 * Bitmap设置水印文字
	 * */
	public static Bitmap bitmap2WatermarkText(Bitmap bitmap, String text,
			int textColor, int textSize, int x, int y,boolean isHorizentalCenter) {
		if (bitmap == null) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(bitmap, 0, 0, null);// 在 0，0坐标开始画入src

		// 加入文字
		if (text != null) {
//			String familyName = "宋体";
//			Typeface font = Typeface.create(familyName, Typeface.BOLD);
			TextPaint textPaint = new TextPaint();
			textPaint.setColor(textColor);
//			textPaint.setTypeface(font);
			textPaint.setTextSize(textSize);
			
			// 这里是自动换行的
			cv.translate(x,y);
			StaticLayout layout = new StaticLayout(text, textPaint, w,
					isHorizentalCenter?Alignment.ALIGN_CENTER:Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
			layout.draw(cv);
			
		}
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储

		return newb;
	}

}
