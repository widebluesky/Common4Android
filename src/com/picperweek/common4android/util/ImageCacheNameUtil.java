package com.picperweek.common4android.util;

import java.io.File;

import com.picperweek.common4android.config.CachePathConstants;




/**
 * 获取图片缓存名字工具
 * 
 * @author xuyi05
 *
 */
public class ImageCacheNameUtil {
	
	private static final String SMALL_IMAGE_SUFFIX = ".sic";
	private static final String LARGE_IMAGE_SUFFIX = ".lic";
	private static final String PNG_IMAGE_SUFFIX = ".pic";
	private static final String SAVE_IMAGE_SUFFIX = ".jpg";
	

	public static String getSmallImageFileName(String url) {
		String path = CachePathConstants.CACHE_IMAGE_DIR + CachePathConstants.SMALL_IMAGE_DIRECTION + StringUtil.toMd5(url) + SMALL_IMAGE_SUFFIX;
		return path;
	}

	public static File getSmallImageFileDir() {
		String path = CachePathConstants.CACHE_IMAGE_DIR + CachePathConstants.SMALL_IMAGE_DIRECTION;
		return new File(path);
	}

	public static String getLargeImageFileName(String url) {
		String path = CachePathConstants.CACHE_IMAGE_DIR + CachePathConstants.LARGE_IMAGE_DIRECTION + StringUtil.toMd5(url) + LARGE_IMAGE_SUFFIX;
		return path;
	}

	public static String getLargeImageFileDir() {
		String path = CachePathConstants.CACHE_IMAGE_DIR + CachePathConstants.LARGE_IMAGE_DIRECTION;
		return path;
	}

	public static String getPngImageFileName(String url) {
		String path = CachePathConstants.CACHE_IMAGE_DIR + CachePathConstants.PNG_IMAGE_DIRECTION + StringUtil.toMd5(url) + PNG_IMAGE_SUFFIX;
		return path;
	}

	public static File getPngImageFileDir() {
		String path = CachePathConstants.CACHE_IMAGE_DIR + CachePathConstants.PNG_IMAGE_DIRECTION;
		return new File(path);
	}

	public static String getSaveImageFileName(String url) {
		String path = CachePathConstants.CACHE_IMAGE_DIR + CachePathConstants.SAVE_IMAGE_DIRECTION + StringUtil.toMd5(url) + SAVE_IMAGE_SUFFIX;
		return path;
	}

	public static String getSplashImageFileName(String url) {
		String path = CachePathConstants.APP_ALL_PATH + CachePathConstants.CHARACTER_DIVIDER + CachePathConstants.SAVE_SPLASH_DIRECTION + StringUtil.toMd5(url) + PNG_IMAGE_SUFFIX;
		return path;
	}
	
	public static File getSplashImageFileDir() {
		String path = CachePathConstants.APP_ALL_PATH + CachePathConstants.CHARACTER_DIVIDER + CachePathConstants.SAVE_SPLASH_DIRECTION;
		return new File(path);
	}
	
	/**
	 * 获取头像文件地址 
	 * @param isBig
	 * @return
	 */
	public static String getPortritImageFileName(boolean isBig){
		String path = CachePathConstants.APP_ALL_PATH + CachePathConstants.CHARACTER_DIVIDER + CachePathConstants.SAVE_PORTRIT_DIRECTION + (isBig ? "portrit_180" : "portrit_90") + SAVE_IMAGE_SUFFIX;
		return path;
	}

}
