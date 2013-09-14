package com.hiputto.common4android.superclass.view;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.hiputto.common4android.manager.ImageFileCache;
import com.hiputto.common4android.manager.ImageMemoryCache;
import com.hiputto.common4android.util.HP_NetWorkAsyncTask;
import com.hiputto.common4android.util.HP_NetWorkUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestBitmapFinished;
import com.hiputto.common4android.util.HP_NetWorkUtils.OnRequestDrawableFinished;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class HP_ImageView extends ImageView {

	private boolean isImageRequestSuccess = false;
	private HP_NetWorkAsyncTask netWorkAsyncTask = null;

	public HP_ImageView(Context context) {
		super(context);
	}

	public HP_ImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HP_ImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public interface OnImageRequest {
		public void onSuccess(HttpRequestBase httpRequest,
				HttpResponse httpResponse, Bitmap bitmap);

		public void onFailure(HttpRequestBase httpRequest,
				HttpResponse httpResponse, Exception e);
	};

	public void setImageBitmapWithURL(String url,
			final OnImageRequest onImageRequest) {
		doAsyncPostRequestBitmap(url, onImageRequest);
	}

	private void doRequestDrawable(String url, OnImageRequest onImageRequest) {
		HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();
		netWorkAsyncTask = (HP_NetWorkAsyncTask) netWorkUtils
				.doAsyncRequestDrawable(url, new OnRequestDrawableFinished() {

					@Override
					public void onSuccess(Drawable drawable) throws Exception {
						// onImageRequest.onSuccess(httpRequest, httpResponse,
						// bitmap);
						HP_ImageView.this.setImageDrawable(drawable);
						isImageRequestSuccess = true;
					}

					@Override
					public void onFailure(Exception e) {
						// onImageRequest.onFailure(httpRequest, httpResponse,
						// e);
						Log.e(this.getClass().getName(), e.getMessage());
						isImageRequestSuccess = false;
						if (netWorkAsyncTask != null
								&& !netWorkAsyncTask.isCancelled()) {
							netWorkAsyncTask.cancel(true);
						}

					}
				});

	}

	private void doAsyncPostRequestBitmap(final String url,
			final OnImageRequest onImageRequest) {

		final ImageMemoryCache memoryCache = new ImageMemoryCache(getContext());
		final ImageFileCache fileCache = new ImageFileCache();

		Bitmap bitmap = memoryCache.getBitmapFromCache(url);
		if (bitmap == null) {
			// 文件缓存中获取
			bitmap = fileCache.getImage(url);
			if (bitmap == null) {
				// 从网络获取

				HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();
				netWorkAsyncTask = (HP_NetWorkAsyncTask) netWorkUtils
						.doAsyncPostRequestBitmap(url,
								new OnRequestBitmapFinished() {

									@Override
									public void onSuccess(
											HttpRequestBase httpRequest,
											HttpResponse httpResponse,
											Bitmap bitmap) throws Exception {

										if (bitmap != null) {
											fileCache.saveBitmap(bitmap, url);
											memoryCache.addBitmapToCache(url,
													bitmap);
										}

										onImageRequest.onSuccess(httpRequest,
												httpResponse, bitmap);
										HP_ImageView.this
												.setImageBitmap(bitmap);
										isImageRequestSuccess = true;
									}

									@Override
									public void onFailure(
											HttpRequestBase httpRequest,
											HttpResponse httpResponse,
											Exception e) {
										onImageRequest.onFailure(httpRequest,
												httpResponse, e);
										Log.e(this.getClass().getName(),
												e.getMessage());
										isImageRequestSuccess = false;
										if (netWorkAsyncTask != null
												&& !netWorkAsyncTask
														.isCancelled()) {
											netWorkAsyncTask.cancel(true);
										}
									}
								});

			} else {
				// 添加到内存缓存
				memoryCache.addBitmapToCache(url, bitmap);

				onImageRequest.onSuccess(null, null, bitmap);
				HP_ImageView.this.setImageBitmap(bitmap);
				isImageRequestSuccess = true;
			}
		} else {
			onImageRequest.onSuccess(null, null, bitmap);
			HP_ImageView.this.setImageBitmap(bitmap);
			isImageRequestSuccess = true;
		}

	}

	public void cancelImageRequest(boolean mayInterruptIfRunning) {
		isImageRequestSuccess = false;
		if (netWorkAsyncTask != null && !netWorkAsyncTask.isCancelled()) {
			netWorkAsyncTask.cancel(mayInterruptIfRunning);
		}
	}

	public boolean isImageRequestSuccess() {
		return isImageRequestSuccess;
	}

	public void setImageRequestSuccess(boolean isImageRequestSuccess) {
		this.isImageRequestSuccess = isImageRequestSuccess;
	}

}
