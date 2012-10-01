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

public class HP_BitmapUtils {

	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		return bitmapDrawable;
	}

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

	public static byte[] bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap bitmapFromBytes(byte[] data) {
		if (data.length != 0) {
			return BitmapFactory.decodeByteArray(data, 0, data.length);
		} else {
			return null;
		}
	}

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

	public static Bitmap bitmap2ReflectionImage(Bitmap bitmap,int reflectionImageDistance) {
		final int reflectionGap = reflectionImageDistance;//图片和倒影的距离
		
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
//		Color.WHITE 交换 0x70ffffff
		paint.setShader(shader);
		
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		return bitmapWithReflection;
	}

}
