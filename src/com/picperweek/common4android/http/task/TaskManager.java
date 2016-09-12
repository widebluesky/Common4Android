package com.picperweek.common4android.http.task;

import java.io.File;
import java.lang.ref.SoftReference;

import com.picperweek.common4android.base.BaseApplication;
import com.picperweek.common4android.http.command.GetImageRequest;
import com.picperweek.common4android.http.command.GetImageResponse;
import com.picperweek.common4android.http.command.HttpDataRequest;
import com.picperweek.common4android.http.command.HttpDataResponse;
import com.picperweek.common4android.http.model.ImageResult;
import com.picperweek.common4android.http.model.ImageType;
import com.picperweek.common4android.manager.LRUCache;
import com.picperweek.common4android.receiver.NetStatusReceiver;
import com.picperweek.common4android.util.ImageCacheNameUtil;
import com.picperweek.common4android.util.ImageUtil;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * 任务管理器
 * @author widebluesky
 *
 */
public class TaskManager {
	
	private static LRUCache<String, SoftReference<Bitmap>> smallImageCache = null;
	private static LRUCache<String, SoftReference<Bitmap>> largeImageCache = null;

	static{
		int memClass = ((ActivityManager)BaseApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass(); 
		smallImageCache = new LRUCache<String, SoftReference<Bitmap>>(memClass/8);
		largeImageCache = new LRUCache<String, SoftReference<Bitmap>>(memClass/16);	
	}
	
	public static synchronized void clearImageCache() {
		smallImageCache.clear();
		largeImageCache.clear();
	}
	
	public static synchronized void removeImageCache(String key) {
		smallImageCache.remove(key);
		largeImageCache.remove(key);
	}

	/*
	 * 开始网络请求任务
	 */
	public static void startHttpDataRequset(HttpDataRequest request, HttpDataResponse response) {
		HttpDataDownloadPool.getInstance().addTask(request, response);
	}

	public static void startRunnableRequest(Runnable runnable) {
		RunnablePool.getInstance().addTask(runnable);
	}

	public static void startRunnableRequestInPool(Runnable runnable) {
		RunnablePool.getInstance().addTaskIntoPool(runnable);
	}

	/*
	 * 开始一个新的任务，获取小图片
	 */
	public static ImageResult startSmallImageTask(GetImageRequest request, GetImageResponse response) {
		return startImageTask(ImageType.SMALL_IMAGE, request, response, true);
	}
	
	/*
	 * 开始一个新的任务，获取大图片
	 */
	public static ImageResult startLargeImageTask(GetImageRequest request, GetImageResponse response) {
		return startImageTask(ImageType.LARGE_IMAGE, request, response, true);
	}

	/*
	 * 开始一个新的任务，获取png图片
	 */
	public static ImageResult startPngImageTask(GetImageRequest request, GetImageResponse response) {
		return startImageTask(ImageType.PNG_IMAGE, request, response, true);
	}

	/*
	 * 开始一个新的任务，获取splash图片
	 */
	public static ImageResult startSplashImageTask(GetImageRequest request, GetImageResponse response) {
		return startImageTask(ImageType.SPLASH_IMAGE, request, response, true);
	}

	public static ImageResult getLocalIconImage(GetImageRequest request, GetImageResponse response) {
		return startImageTask(ImageType.SMALL_IMAGE, request, response, false);
	}
	
	public static ImageResult getLocalPngImage(GetImageRequest request, GetImageResponse response) {
		return startImageTask(ImageType.PNG_IMAGE, request, response, false);
	}
	

	/**
	 * 
	 * @param imageType
	 * @param request
	 * @param response
	 * @param fromNet
	 *            是否发起网络请求
	 * @return
	 */
	private static ImageResult startImageTask(ImageType imageType, GetImageRequest request, GetImageResponse response, boolean fromNet) {
		ImageResult result = new ImageResult();

		if (request.getUrl() == null) {
			response.onImageRecvError(imageType, request.getTag(), ImageResult.ERROR_URL_NULL);
			result.setResultFail();
			return result;
		}

		String filePath = getImageFilePath(imageType, request.getUrl());
		request.setFilePath(filePath);
		Bitmap retBitmap = null;
		LRUCache<String, SoftReference<Bitmap>> localCache = getImageArrayCache(imageType);
	
		// 1取本地缓存
		if (localCache.containsKey(filePath)) {
			SoftReference<Bitmap> sfBitmap = localCache.get(filePath);
			if (sfBitmap != null) {
				retBitmap = sfBitmap.get();
			}
		
			if (retBitmap != null && !retBitmap.isRecycled()) {
				if (response != null) {
					result.setResultOK();
					result.setRetBitmap(retBitmap);
					result.setImagePath(filePath);
					return result;
				}
			}
		}
		
		// 2取本地保存数据
		File fileTmp = new File(filePath);
		if (fileTmp.exists()) {
			retBitmap = ImageUtil.FromFileToBitmap(filePath);
	
			if (retBitmap != null) {
				putImageInCache(imageType, filePath, retBitmap);
				result.setResultOK();
				result.setImagePath(filePath);
				result.setRetBitmap(retBitmap);
				return result;
			}
		}
		
		
		/**
		 * 是否发起网络请求
		 */
		if (fromNet) {
			// 3访问网络数据
			if (NetStatusReceiver.netStatus == NetStatusReceiver.NETSTATUS_INAVAILABLE) {
				response.onImageRecvError(imageType, request.getTag(), ImageResult.ERROR_NO_NET);
				result.setResultFail();
				return result;
			}
			// 添加图片任务
			switch (imageType) {
			case SMALL_IMAGE:
			case PNG_IMAGE:
				PngImageDownloadPool.getInstance().addTask(imageType, request, response);
				break;
			case SPLASH_IMAGE:
			case LARGE_IMAGE:
				ImageDownloadPool.getInstance().addTask(imageType, request, response);
				break;
			default:
				break;
			}
		}

		return result;
	}

	public static void putImageInCache(ImageType type, String pathKey, Bitmap value) {
		switch (type) {
		case SMALL_IMAGE:
			smallImageCache.put(pathKey, new SoftReference<Bitmap>(value));
			break;
		case SPLASH_IMAGE:
		case PNG_IMAGE:
		case LARGE_IMAGE:
			largeImageCache.put(pathKey, new SoftReference<Bitmap>(value));
			break;
		default:
			break;
		}
	}

	private static String getImageFilePath(ImageType imageType, String url) {

		String filePath = null;

		switch (imageType) {
		case SPLASH_IMAGE:
			filePath = ImageCacheNameUtil.getSplashImageFileName(url);
			break;
		case PNG_IMAGE:
			filePath = ImageCacheNameUtil.getPngImageFileName(url);
			break;
		case SMALL_IMAGE:
			filePath = ImageCacheNameUtil.getSmallImageFileName(url);
			break;
		case LARGE_IMAGE:
			filePath = ImageCacheNameUtil.getLargeImageFileName(url);
			break;
		default:
			break;
		}
		return filePath;
	}

	private static LRUCache<String, SoftReference<Bitmap>> getImageArrayCache(ImageType type) {
		switch (type) {
		case SMALL_IMAGE:
			return smallImageCache;
		case SPLASH_IMAGE:
		case PNG_IMAGE:
		case LARGE_IMAGE:
			return largeImageCache;
		default:
			break;
		}
		return null;
	}

	public static void cancelAllThread() {
		ImageDownloadPool.getInstance().cancelAllThread();
		cancelAllHttpThread();
	}

	public static void cancelAllImageThread() {
		ImageDownloadPool.getInstance().cancelAllThread();
	}

	/**
	 * 停止一个正在访问的请求
	 * @param request
	 */
	public static void cancelOneHttpRequest(HttpDataRequest request) {
		request.setCancelled(true);
	}

	public static void cancelAllHttpThread() {
		HttpDataDownloadPool.getInstance().cancelAllThread();
	}

	public static void waitAllThreadExit() {
		HttpDataDownloadPool.getInstance().waitThreadExit();
		ImageDownloadPool.getInstance().waitThreadExit();
		PngImageDownloadPool.getInstance().waitThreadExit();
	}
}
