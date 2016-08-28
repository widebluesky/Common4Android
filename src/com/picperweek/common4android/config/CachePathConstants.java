package com.picperweek.common4android.config;

import java.io.File;

import com.picperweek.common4android.base.BaseApplication;
import com.picperweek.common4android.util.AppInfoUtil;
import com.picperweek.common4android.util.SDCardUtil;


/**
 * 缓存路径常量类
 * 
 * @author widebluesky
 *
 */
public class CachePathConstants {

	public static final String CHARACTER_DIVIDER = File.separator;
	
	public static final String APP_MAIN_PATH = Constants.APP_MAIN_PATH;
	
	public static final String APP_ALL_PATH = SDCardUtil.getSDCardPath() + APP_MAIN_PATH + CHARACTER_DIVIDER + Constants.APP_NAME;
	
	public static final String CACHE_DIR = APP_ALL_PATH + "/data/";
	
	public static final String CACHE_IMAGE_DIR = CACHE_DIR + "image/";
	
	public static final String VERSION_CACHE_PATH = CACHE_DIR + AppInfoUtil.getVersionCode(BaseApplication.getInstance().getApplicationContext()) + "/";

	public static final String SMALL_IMAGE_DIRECTION = "small-image-cache/";
	
	public static final String LARGE_IMAGE_DIRECTION = "large-image-cache/";
	
	public static final String PNG_IMAGE_DIRECTION = "png_image_cache/";
	
	public static final String SAVE_IMAGE_DIRECTION = "download/";
	
	public static final String SAVE_SPLASH_DIRECTION = "splash/";
	
	public static final String SAVE_PORTRIT_DIRECTION = "portrit/";

}
