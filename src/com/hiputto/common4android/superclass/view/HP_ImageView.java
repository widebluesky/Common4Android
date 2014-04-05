package com.hiputto.common4android.superclass.view;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.hiputto.common4android.manager.HP_DefaultThreadPool;
import com.hiputto.common4android.manager.HP_ImageFileCache;
import com.hiputto.common4android.manager.HP_ImageMemoryCache;
import com.hiputto.common4android.util.HP_NetUtils;
import com.hiputto.common4android.util.HP_NetWorkUtils;
import com.hiputto.common4android.util.HP_StringUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class HP_ImageView extends ImageView {

//	private HP_NetWorkAsyncTask netWorkAsyncTask = null;
	private Thread mThread = null;
	private String mUrl = null;
	
	private final int LOAD_SUCCESS = 0x001;
	private final int LOAD_FAILURE = 0x002;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_SUCCESS:
				HP_ImageView.this.setImageBitmap((Bitmap) msg.obj);
				break;
			case LOAD_FAILURE:
				
				break;
			}
		};
	};

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
//		if (HP_StringUtils.isEmptyString(url)) {
//			setImageDrawable(null);
//			return;
//		}
			//如果正在下载
        if ((mUrl == null) || (!mUrl.equals(url))) {
            	//如果当前下载的URL与滚动到的Item URL不同 
        	setImageDrawable(null);
            HP_DefaultThreadPool.getInstance().removeTaskFromQueue(mThread);
        } else {
        	return;
        }
		doAsyncPostRequestBitmap(url, onImageRequest);
	}

	private void doAsyncPostRequestBitmap(final String url,
			final OnImageRequest onImageRequest) {
		mUrl = url;
		final HP_ImageMemoryCache memoryCache = new HP_ImageMemoryCache(getContext());
		final HP_ImageFileCache fileCache = new HP_ImageFileCache();
		Bitmap bitmap = memoryCache.getBitmapFromCache(url);
		if (bitmap == null) {
			// 文件缓存中获取
			bitmap = fileCache.getImage(url);
			if (bitmap == null) {
				// 从网络获取
				HP_NetUtils netUtils = new HP_NetUtils();
				mThread = netUtils.doAsyncGetRequestBitmap(url, new HP_NetUtils.OnRequestBitmapFinished() {
					@Override
					public void onSuccess(HttpRequestBase httpRequest,
							HttpResponse httpResponse, Bitmap bitmap) throws Exception {
						System.out.println("mUrl:" + mUrl);
						System.out.println("url:" + url);
						System.out.println(httpRequest.getURI().toString());
						if (bitmap != null) {
							fileCache.saveBitmap(bitmap, httpRequest.getURI().toString());
							memoryCache.addBitmapToCache(httpRequest.getURI().toString(),bitmap);
						}
						onImageRequest.onSuccess(httpRequest, httpResponse, bitmap);
						
						if (getTag().equals(httpRequest.getURI().toString())) {
							mUrl = httpRequest.getURI().toString();
							Message message = new Message();
							message.what = LOAD_SUCCESS;
							message.obj = bitmap;
							mHandler.sendMessage(message);
						}
					}
					
					@Override
					public void onFailure(HttpRequestBase httpRequest,
							HttpResponse httpResponse, Exception e) {
						onImageRequest.onFailure(httpRequest,
								httpResponse, e);
						Log.e(this.getClass().getName(),
								e.getMessage());
						mHandler.sendEmptyMessage(LOAD_FAILURE);
						
					}
				});
				
			} else {
				// 添加到内存缓存
				memoryCache.addBitmapToCache(url, bitmap);
				onImageRequest.onSuccess(null, null, bitmap);
				HP_ImageView.this.setImageBitmap(bitmap);
			}
		} else {
			onImageRequest.onSuccess(null, null, bitmap);
			HP_ImageView.this.setImageBitmap(bitmap);
		}

	}

	public void cancelImageRequest(boolean mayInterruptIfRunning) {
		HP_DefaultThreadPool.getInstance().removeTaskFromQueue(mThread);
//		if (netWorkAsyncTask != null && !netWorkAsyncTask.isCancelled()) {
//			netWorkAsyncTask.cancel(mayInterruptIfRunning);
//		}
	}

}
