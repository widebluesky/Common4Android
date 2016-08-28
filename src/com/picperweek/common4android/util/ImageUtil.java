package com.picperweek.common4android.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.picperweek.common4android.base.BaseApplication;
import com.picperweek.common4android.config.Constants;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * 图片工具类
 * @author xuyi05
 *
 */
public class ImageUtil {
	/** 图像质量高 */
	public static final int QUALITY_HIGH = 85;

	/** 图像质量中 */
	public static final int QUALITY_MIDDLE = 75;

	/** 图像质量低 */
	public static final int QUALITY_LOW = 70;

	/**
	 * 获取圆角图片
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		if (bitmap == null) {
			return null;
		}
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		} catch (OutOfMemoryError e) {
			LogUtil.e(Constants.APP_TAG, e.toString());
			return null;
		}
	}
	
	/**
	 * 获取圆形图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		if (bitmap == null){
			return null;
		}
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
	
	public static Drawable getBlackBitmap(Bitmap bm) {
		Drawable[] array = new Drawable[2];    
		array[0] = new BitmapDrawable(bm);
		array[1] = new PaintDrawable(Color.parseColor("#7f000000"));
		LayerDrawable ld = new LayerDrawable(array);
		return ld;
	}
	
	public static Drawable getBlackBitmapForExpress(Bitmap bm) {
		BitmapDrawable drawable = new BitmapDrawable(bm);
		drawable.setBounds(0, 0, drawable.getBitmap().getWidth(),
				drawable.getBitmap().getHeight());
		drawable.mutate();
		drawable.setColorFilter(0x7fffffff, Mode.MULTIPLY);
		return drawable;
	}

	/*
	 * 缩放大图的策略，缩放到固定大小，先使用不占内存的方法将图像缩小至一定范围，再进行读取缩放
	 */
	public static Bitmap reszieBigImage(String filePath, int maxLength) {

		ParcelFileDescriptor pfd;
		try {
			pfd = BaseApplication.getInstance().getContentResolver().openFileDescriptor(Uri.parse("file://" + filePath), "r");
		} catch (IOException ex) {
			return null;
		}
		java.io.FileDescriptor fd = pfd.getFileDescriptor();
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 先指定原始大小
		options.inSampleSize = 1;
		// 只进行大小判断
		options.inJustDecodeBounds = true;
		// 调用此方法得到options得到图片的大小
		BitmapFactory.decodeFileDescriptor(fd, null, options);
		// 我们的目标是在480pixel的画面上显示。
		// 所以需要调用computeSampleSize得到图片缩放的比例
		options.inSampleSize = computeSampleSize(options, maxLength);
		// OK,我们得到了缩放的比例，现在开始正式读入BitMap数据
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;

		// 根据options参数，减少所需要的内存
		Bitmap sourceBitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);

		// 再对图像进一步进行精确缩放
		double scale = maxLength * 1.0 / (Math.max(sourceBitmap.getWidth(), sourceBitmap.getHeight()));
		int newWidth = (int) (sourceBitmap.getWidth() * scale);
		int newHeight = (int) (sourceBitmap.getHeight() * scale);
		return resizeBitmapSmooth(sourceBitmap, newWidth, newHeight);
	}

	/*
	 * 计算缩放比例
	 */
	private static int computeSampleSize(BitmapFactory.Options options, int target) {
		int w = options.outWidth;
		int h = options.outHeight;

		int length = Math.max(w, h);
		int candidate = length / target;

		if (candidate == 0)
			return 1;

		if (candidate > 1) {
			if ((length > target) && (length / candidate) < target)
				candidate -= 1;
		}

		return candidate;
	}

	/*
	 * 等比缩放图像
	 */
	public static Bitmap resizeBitmapSmooth(Bitmap srcBitmap, int desWidth, int desHeight) {
		try {
			if (srcBitmap == null || desWidth < 0 || desHeight < 0) {
				return null;
			}
			Bitmap dstBitmap = Bitmap.createScaledBitmap(srcBitmap, desWidth, desHeight, true);
			return dstBitmap;
		} catch (OutOfMemoryError e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}

	}

	/*
	 * 保存图片到文件地址，并且设置图片质量（高、中、低）
	 */
	public static boolean saveBitmap(Bitmap bitmap, String path, int quality) {
		// Bitmap图片保存
		if (bitmap == null || path == null || path.equals("")) {
			return false;
		}
		BufferedOutputStream ostream = null;
		try {
			File file = new File(path);
			FileUtil.makeDIRAndCreateFile(path);
			ostream = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(CompressFormat.JPEG, quality, ostream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LogUtil.e(Constants.APP_TAG, e.toString());
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.e(Constants.APP_TAG, e.toString());
			return false;
		} finally {
			try {
				if (ostream != null) {
					ostream.flush();
					ostream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogUtil.e(Constants.APP_TAG, e.toString());
				return false;
			}
		}
		return true;
	}

	public static boolean saveBitmapPNG(Bitmap bitmap, String path, int quality) {
		// Bitmap图片保存
		if (bitmap == null || path == null || path.equals("")) {
			return false;
		}
		BufferedOutputStream ostream = null;
		try {
			File file = new File(path);
			FileUtil.makeDIRAndCreateFile(path);
			ostream = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(CompressFormat.PNG, quality, ostream);
		} catch (FileNotFoundException e) {
			LogUtil.e(Constants.APP_TAG, e.toString());
			return false;
		} catch (Exception e) {
			LogUtil.e(Constants.APP_TAG, e.toString());
			return false;
		} finally {
			try {
				if (ostream != null) {
					ostream.flush();
					ostream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogUtil.e(Constants.APP_TAG, e.toString());
				return false;
			}
		}
		return true;
	}

	/*
	 * 保存图片到文件地址，并且设置图片质量（高、中、低）
	 */
	public static void saveBitmapPng(Bitmap bitmap, String path) {
		if (bitmap == null || path == null || path.equals("")) {
			return;
		}
		if (!SDCardUtil.isSDCardExist()) {
			return;
		}
		// Bitmap图片保存
		File file = new File(path);
		OutputStream ostream = null;
		try {
			ostream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, ostream);
		} catch (Exception e) {
			LogUtil.e(Constants.APP_TAG, e.toString());
		} finally {
			try {
				if (ostream != null) {
					ostream.flush();
					ostream.close();
				}
			} catch (IOException e) {
				LogUtil.e(Constants.APP_TAG, e.toString());
			}
		}
	}

	/*
	 * 旋转图片,90度的倍数
	 */
	public static Bitmap rotateBitmap(Bitmap srcBitmap, int rotateTime) {
		int sw = srcBitmap.getWidth();
		int sh = srcBitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.setRotate(90 * rotateTime, sw / 2.0f, sh / 2.0f);
		return Bitmap.createBitmap(srcBitmap, 0, 0, sw, sh, matrix, true);
	}

	public static Bitmap FromFileToBitmap(String mPath) {
		return FromFileToBitmap(mPath, Bitmap.Config.RGB_565);
	}

	public static Bitmap FromFileToBitmap(String mPath, Config config) {
		Bitmap bitmap = null;
		File fileTmp = new File(mPath);
		if (fileTmp.exists()) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				options.inJustDecodeBounds = false;
				options.inDither = false;
				options.inPreferredConfig = config;
				bitmap = BitmapFactory.decodeFile(mPath, options);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				return null;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			}
		}
		return bitmap;
	}

	public static Bitmap FromFileToBitmap(String mPath, int maxLength) {
		Bitmap bitmap = null;
		File fileTmp = new File(mPath);
		if (fileTmp.exists()) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(mPath, options);

				options.inSampleSize = computeSampleSize(options, maxLength);
				options.inJustDecodeBounds = false;
				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				bitmap = BitmapFactory.decodeFile(mPath, options);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				return null;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			}
		}
		return bitmap;
	}

	public static Bitmap FromResToBitmap(Resources res, int id, Config config) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = config;
			bitmap = BitmapFactory.decodeResource(res, id, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	public static Bitmap FromResToBitmap(Resources res, int id) {
		return FromResToBitmap(res, id, Bitmap.Config.ARGB_8888);
	}

	public static Bitmap FromResToBitmap(Resources res, int id, int maxLength) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(res, id, options);

			options.inSampleSize = computeSampleSize(options, maxLength);
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			bitmap = BitmapFactory.decodeResource(res, id, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	public static Bitmap FromByteToBitmap(byte[] response) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			bitmap = BitmapFactory.decodeByteArray(response, 0, response.length, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	public static Bitmap FromByteToBitmap(byte[] response, int maxLength) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(response, 0, response.length, options);

			options.inSampleSize = computeSampleSize(options, maxLength);
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			bitmap = BitmapFactory.decodeByteArray(response, 0, response.length, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	public static Bitmap FromStreamToBitmap(InputStream is, Config config) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = config;
			bitmap = BitmapFactory.decodeStream(is, null, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	public static Bitmap FromStreamToBitmap(InputStream is) {
		return FromStreamToBitmap(is, Bitmap.Config.RGB_565);
	}

	public static Bitmap FromStreamToBitmap(InputStream is, Rect rect, int maxLength) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, rect, options);

			options.inSampleSize = computeSampleSize(options, maxLength);
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			bitmap = BitmapFactory.decodeStream(is, rect, options);
			;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
	
	public static void recycleBitmap(Bitmap bm) {
		if (bm != null && !bm.isRecycled()) {
			bm.recycle();
			bm = null;
		}
	}
	
	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}
}
