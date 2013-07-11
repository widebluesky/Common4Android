package com.hiputto.common4android.util;

import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class HP_DeviceUtils {

	// 获取设备型号
	public static String getModel() {
		return android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
	}

	// 获取操作版本
	public static String getOperatingSystem() {
		return "Android " + android.os.Build.VERSION.RELEASE;
	}

	public static String getProduct() {
		return "Product: " + android.os.Build.PRODUCT;
	}

	// String phoneInfo = "Product: " + android.os.Build.PRODUCT;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "CPU_ABI: " + android.os.Build.CPU_ABI;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "TAGS: " + android.os.Build.TAGS;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "MODEL: " + android.os.Build.MODEL;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "SDK: " + android.os.Build.VERSION.SDK;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "DEVICE: " + android.os.Build.DEVICE;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "DISPLAY: " + android.os.Build.DISPLAY;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "BRAND: " + android.os.Build.BRAND;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "BOARD: " + android.os.Build.BOARD;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "FINGERPRINT: " + android.os.Build.FINGERPRINT;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "ID: " + android.os.Build.ID;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "MANUFACTURER: " + android.os.Build.MANUFACTURER;
	// logErrorMessage(phoneInfo);
	// phoneInfo = "USER: " + android.os.Build.USER;
	// logErrorMessage(phoneInfo);

	// //获取mac地址
	// public static String getMACAddress(Context context){
	// WifiManager wifi = (WifiManager)
	// context.getSystemService(Context.WIFI_SERVICE);
	// WifiInfo info = wifi.getConnectionInfo();
	// return info.getMacAddress();
	// }

	// 获取屏幕宽度
	public static int getScreenWidth(Context context) {
		new DisplayMetrics();
		return context.getApplicationContext().getResources()
				.getDisplayMetrics().widthPixels;
	}

	// 获取屏幕高度
	public static int getScreenHeight(Context context) {
		new DisplayMetrics();
		return context.getApplicationContext().getResources()
				.getDisplayMetrics().heightPixels;
	}

	// 判断GPS是否打开
	public static boolean isGPSEnabled(Context context) {
		LocationManager lm = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = lm.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	// 判断WIFI是否打开
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
