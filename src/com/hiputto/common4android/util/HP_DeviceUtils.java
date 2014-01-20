package com.hiputto.common4android.util;

import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class HP_DeviceUtils {

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		new DisplayMetrics();
		return context.getApplicationContext().getResources()
				.getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		new DisplayMetrics();
		return context.getApplicationContext().getResources()
				.getDisplayMetrics().heightPixels;
	}

	/**
	 * 判断GPS是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGPSEnabled(Context context) {
		LocationManager lm = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = lm.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	/**
	 * 判断WIFI是否打开
	 * 
	 * @param context
	 * @return
	 */
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

	public static String getBuild_PRODUCT() {
		return android.os.Build.PRODUCT;
	}

	public static String getBuild_CPU_ABI() {
		return android.os.Build.CPU_ABI;
	}

	public static String getBuild_TAGS() {
		return android.os.Build.TAGS;
	}

	public static int getBuild_VERSION_CODES_BASE() {
		return android.os.Build.VERSION_CODES.BASE;
	}

	public static String getBuild_MODEL() {
		return android.os.Build.MODEL;
	}

	public static String getBuild_VERSION_SDK() {
		return android.os.Build.VERSION.SDK;
	}

	public static String getBuild_VERSION_RELEASE() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getBuild_DEVICE() {
		return android.os.Build.DEVICE;
	}

	public static String getBuild_DISPLAY() {
		return android.os.Build.DISPLAY;
	}

	public static String getBuild_BRAND() {
		return android.os.Build.BRAND;
	}

	public static String getBuild_BOARD() {
		return android.os.Build.BOARD;
	}

	public static String getBuild_FINGERPRINT() {
		return android.os.Build.FINGERPRINT;
	}

	public static String getBuild_ID() {
		return android.os.Build.ID;
	}

	public static String getBuild_MANUFACTURER() {
		return android.os.Build.MANUFACTURER;
	}

	public static String getBuild_USER() {
		return android.os.Build.USER;
	}

	/**
	 * 获取mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMACAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

}
