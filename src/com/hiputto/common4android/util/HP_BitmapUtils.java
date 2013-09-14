package com.hiputto.common4android.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hiputto.common4android.util.HP_AsyncTaskUtils.AsyncTaskSteps;

import android.content.Context;
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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

public class HP_BitmapUtils {

	/**
	 * Bitmap to Drawable
	 * 
	 * @param bitmap
	 *            Bitmap类型的图片
	 * 
	 * @return 返回Dawable类型的图片
	 * 
	 * */
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		return bitmapDrawable;
	}

	/**
	 * Bitmap from Drawable
	 * 
	 * @param drawable
	 *            Drawable类型的图片
	 * 
	 * @return 返回Bitmap类型的图片
	 * 
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
	 * 
	 * @param bitmap
	 *            Bitmap类型的图片
	 * 
	 * @return 返回Byte数组数据
	 * @throws Exception
	 * 
	 * */
	public static byte[] bitmap2Bytes(Bitmap bitmap,Bitmap.CompressFormat format) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(format, 100, baos);
		byte[] data = baos.toByteArray();
		return data;
	}

	/**
	 * Bitmap转换自Byte数组
	 * 
	 * @param data
	 *            Byte数组的数据
	 * 
	 * @return 返回Bitmap类型的图片
	 * 
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
	 * 
	 * @param bitmap
	 *            Bitmap类型的图片
	 * 
	 * @param width
	 *            Resize后的图片宽度
	 * 
	 * @param height
	 *            Resize后的图片高度
	 * 
	 * @return 返回Bitmap类型的图片
	 * 
	 * */
	public static Bitmap bitmap2Resize(Bitmap bitmap, int width, int height) {
		if (bitmap==null) {
			return null;
		}
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
	 * 
	 * @param bitmap
	 *            Bitmap类型的图片
	 * 
	 * @param roundPx
	 *            圆角的大小
	 * 
	 * @return Bitmap类型的图片
	 * 
	 * */
	public static Bitmap bitmap2Round(Bitmap bitmap, float roundPx) {
		if (bitmap==null) {
			return null;
		}
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
	 * 
	 * @param bitmap
	 *            Bitmap类型的图片
	 * 
	 * @param reflectionImageDistance
	 *            倒影与图片的间距
	 * 
	 * @return 返回Bitmap类型的图片
	 * 
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
	 * 
	 * @param bitmap
	 *            Bitmap类型的图片
	 * 
	 * @param watermark
	 *            Bitmap类型的水印图片
	 * 
	 * @return Bitmap类型的图片
	 * 
	 * */
	public static Bitmap bitmap2WatermarkImage(Bitmap bitmap, Bitmap watermark,
			int x, int y) {
		if (bitmap == null) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		// 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(bitmap, 0, 0, null);// 在 0，0坐标开始画入src
		Paint paint = new Paint();
		// 加入图片
		if (watermark != null) {
			int ww = watermark.getWidth();
			int wh = watermark.getHeight();
			// paint.setAlpha(1010);
			cv.drawBitmap(watermark, x, y, paint);// 在src的右下角画入水印
		}

		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		return newb;
	}

	/**
	 * Bitmap设置水印文字
	 * 
	 * @param bitmap
	 *            Bitmap类型的图片
	 * 
	 * @param text
	 *            String类型的字符串
	 * 
	 * @param textColor
	 *            int类型的文字颜色
	 * 
	 * @param textSize
	 *            int类型的文字大小
	 * 
	 * @param x
	 *            int类型的文字x坐标
	 * 
	 * @param y
	 *            int类型的文字y坐标
	 * 
	 * @param isHorizentalCenter
	 *            文字是否居中显示
	 * 
	 * @return 返回Bitmap类型的图片
	 * 
	 * */
	public static Bitmap bitmap2WatermarkText(Bitmap bitmap, String text,
			int textColor, int textSize, int x, int y, int width,
			boolean isHorizentalCenter) {
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
			// String familyName = "宋体";
			// Typeface font = Typeface.create(familyName, Typeface.BOLD);
			TextPaint textPaint = new TextPaint();
			textPaint.setColor(textColor);
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
			textPaint.setTextSize(textSize);

			// 这里是自动换行的
			cv.translate(x, y);

			StaticLayout layout = new StaticLayout(text, textPaint, width,
					isHorizentalCenter ? Alignment.ALIGN_CENTER
							: Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
			layout.draw(cv);

		}
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储

		return newb;
	}

	public static Bitmap bitmap2CompressBitmap(Bitmap bitmap, int scale,Bitmap.CompressFormat format) {

		byte[] data = null;
		try {
			data = HP_BitmapUtils.bitmap2Bytes(bitmap,format);
		} catch (Exception e) {
			Log.e("asdf", e.getLocalizedMessage());
		}

		BitmapFactory.Options option = new BitmapFactory.Options();

		option.inSampleSize = scale; // 将图片设为原来宽高的1/2，防止内存溢出

		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, option);

		return bitmap;
	}

	public static void getBitmapByDecodeFile(String pathName,
			final OnDecodeFileFinished onDecodeFileFinished) {

		new HP_AsyncTaskUtils(new AsyncTaskSteps() {

			@Override
			public void onProgressUpdate(Integer... values) {

			}

			@Override
			public void onPreExecute() {

			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {

				Bitmap bitmap = (Bitmap) hashMap.get("bitmap");
				onDecodeFileFinished.onDecodeFileFinished(bitmap);
			}

			@Override
			public void onCancelled() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {

				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("bitmap", BitmapFactory.decodeFile(params[0]));
				return hashMap;
			}
		}).execute(pathName);

	}

	public static void getBitmapByDecodeFile(String[] pathName,
			final OnDecodeFileListFinished onDecodeFileListFinished) {

		new HP_AsyncTaskUtils(new AsyncTaskSteps() {

			@Override
			public void onProgressUpdate(Integer... values) {

			}

			@Override
			public void onPreExecute() {

			}

			@Override
			public void onPostExecute(HashMap<String, Object> hashMap) {

				@SuppressWarnings("unchecked")
				List<Bitmap> bitmapList = (List<Bitmap>) hashMap
						.get("bitmapList");
				onDecodeFileListFinished.onDecodeFileListFinished(bitmapList);

			}

			@Override
			public void onCancelled() {

			}

			@Override
			public HashMap<String, Object> doInBackground(String... params) {

				List<Bitmap> bitmapList = new ArrayList<Bitmap>();

				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				for (int i = 0; i < params.length; i++) {
					bitmapList.add(BitmapFactory.decodeFile(params[i]));
				}
				hashMap.put("bitmapList", bitmapList);

				return hashMap;
			}
		}).execute(pathName);

	}

	public interface OnDecodeFileFinished {
		public void onDecodeFileFinished(Bitmap bitmap);

	}

	public interface OnDecodeFileListFinished {

		public void onDecodeFileListFinished(List<Bitmap> bitmapList);
	}

	public static Bitmap revitionImageSize(Context context, String path,
			int size) throws Exception {
		// 取得图片

		InputStream temp = context.getAssets().open(path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
		options.inJustDecodeBounds = true;
		// 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
		BitmapFactory.decodeStream(temp, null, options);
		// 关闭流
		temp.close();

		// 生成压缩的图片
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满足
			if ((options.outWidth >> i <= size)
					&& (options.outHeight >> i <= size)) {
				// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
				temp = context.getAssets().open(path);
				// 这个参数表示 新生成的图片为原始图片的几分之一。
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;

				bitmap = BitmapFactory.decodeStream(temp, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	@SuppressWarnings("unused")
	private InputStream bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
		return sbs;
	}

	public static Bitmap compressJPEGImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap compressPNGImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	/**
     * make sure the color data size not more than 5M
     * 
     * @param rect
     * @return
     */
    public static boolean makesureSizeNotTooLarge(Rect rect) {
        final int FIVE_M = 5 * 1024 * 1024;
        if ( rect.width() * rect.height() * 2 > FIVE_M ) {
            // 不能超过5M
            return false;
        }
        return true;
    }
    
    public static int getSampleSizeOfNotTooLarge( Rect rect ) {
        final int FIVE_M = 5 * 1024 * 1024;
        double ratio = ( ( double ) rect.width() ) * rect.height() * 2 / FIVE_M;
        return ratio >= 1 ? (int)ratio : 1;
    }

    /**
     * 自适应屏幕大小 得到最大的smapleSize
     * 同时达到此目标： 自动旋转 以适应view的宽高后, 不影响界面显示效果
     * @param vWidth view width
     * @param vHeight view height
     * @param bWidth bitmap width
     * @param bHeight bitmap height
     * @return
     */
    public static int getSampleSizeAutoFitToScreen( int vWidth, int vHeight, int bWidth, int bHeight ) {
        if( vHeight == 0 || vWidth == 0 ) {
            return 1;
        }

        int ratio = Math.max( bWidth / vWidth, bHeight / vHeight );

        int ratioAfterRotate = Math.max( bHeight / vWidth, bWidth / vHeight );

        return Math.min( ratio, ratioAfterRotate );
    }
    
    /**
     * 检测是否可以解析成位图
     * 
     * @param datas
     * @return
     */
    public static boolean verifyBitmap(byte[] datas) {
        return verifyBitmap(new ByteArrayInputStream(datas));
    }

    /**
     * 检测是否可以解析成位图
     * 
     * @param input
     * @return
     */
    public static boolean verifyBitmap(InputStream input) {
        if (input == null) {
            return false;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        input = input instanceof BufferedInputStream ? input
                : new BufferedInputStream(input);
        BitmapFactory.decodeStream(input, null, options);
        try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return (options.outHeight > 0) && (options.outWidth > 0);
    }

    /**
     * 检测是否可以解析成位图
     * 
     * @param path
     * @return
     */
    public static boolean verifyBitmap(String path) {
        try {
            return verifyBitmap(new FileInputStream(path));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

	/*
	 * private Bitmap getimage(String srcPath) { BitmapFactory.Options newOpts =
	 * new BitmapFactory.Options(); // 开始读入图片，此时把options.inJustDecodeBounds
	 * 设回true了 newOpts.inJustDecodeBounds = true; Bitmap bitmap =
	 * BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
	 * 
	 * newOpts.inJustDecodeBounds = false; int w = newOpts.outWidth; int h =
	 * newOpts.outHeight; // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为 float hh = 800f;//
	 * 这里设置高度为800f float ww = 480f;// 这里设置宽度为480f //
	 * 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可 int be = 1;// be=1表示不缩放 if (w > h && w >
	 * ww) {// 如果宽度大的话根据宽度固定大小缩放 be = (int) (newOpts.outWidth / ww); } else if
	 * (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放 be = (int) (newOpts.outHeight /
	 * hh); } if (be <= 0) be = 1; newOpts.inSampleSize = be;// 设置缩放比例 //
	 * 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了 bitmap =
	 * BitmapFactory.decodeFile(srcPath, newOpts); return bitmap;//
	 * 压缩好比例大小后再进行质量压缩 }
	 * 
	 * private Bitmap comp(Bitmap image) {
	 * 
	 * ByteArrayOutputStream baos = new ByteArrayOutputStream();
	 * image.compress(Bitmap.CompressFormat.JPEG, 100, baos); if
	 * (baos.toByteArray().length / 1024 > 1024) {//
	 * 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出 baos.reset();//
	 * 重置baos即清空baos image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//
	 * 这里压缩50%，把压缩后的数据存放到baos中 } ByteArrayInputStream isBm = new
	 * ByteArrayInputStream(baos.toByteArray()); BitmapFactory.Options newOpts =
	 * new BitmapFactory.Options(); // 开始读入图片，此时把options.inJustDecodeBounds
	 * 设回true了 newOpts.inJustDecodeBounds = true; Bitmap bitmap =
	 * BitmapFactory.decodeStream(isBm, null, newOpts);
	 * newOpts.inJustDecodeBounds = false; int w = newOpts.outWidth; int h =
	 * newOpts.outHeight; // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为 float hh = 800f;//
	 * 这里设置高度为800f float ww = 480f;// 这里设置宽度为480f //
	 * 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可 int be = 1;// be=1表示不缩放 if (w > h && w >
	 * ww) {// 如果宽度大的话根据宽度固定大小缩放 be = (int) (newOpts.outWidth / ww); } else if
	 * (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放 be = (int) (newOpts.outHeight /
	 * hh); } if (be <= 0) be = 1; newOpts.inSampleSize = be;// 设置缩放比例 //
	 * 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了 isBm = new
	 * ByteArrayInputStream(baos.toByteArray()); bitmap =
	 * BitmapFactory.decodeStream(isBm, null, newOpts); return bitmap;//
	 * 压缩好比例大小后再进行质量压缩 }
	 */
	

}
