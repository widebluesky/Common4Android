package com.picperweek.common4android.util;

import java.io.File;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import com.picperweek.common4android.base.BaseApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DeviceUtil {

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

	/**
	 * 获取手机状态栏高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			statusBarHeight = 38;
		}
		return statusBarHeight;
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param activity
	 */
	public static void hideKeyboard(Activity activity) {
		InputMethodManager localInputMethodManager = (InputMethodManager) activity
				.getSystemService("input_method");

		if (activity.getCurrentFocus() != null) {
			IBinder localIBinder = activity.getCurrentFocus().getWindowToken();
			localInputMethodManager.hideSoftInputFromWindow(localIBinder, 0);
		}
	}

	/**
	 * 显示隐藏软件键盘
	 * 
	 * @param show
	 * @param edittext
	 */
	public static void showInputKeyboard(boolean show, View edittext) {
		InputMethodManager imm = (InputMethodManager) BaseApplication.getInstance().getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (show) {
			edittext.setFocusable(true);
			edittext.setFocusableInTouchMode(true);
			edittext.requestFocus();
			edittext.requestFocusFromTouch();
			imm.showSoftInput(edittext, 0);
		} else {
			imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
		}
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
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 * @throws Exception
	 */
	public static String getVersionName(Context context) {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String versionName = info.versionName;
		return versionName;
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 * @throws Exception
	 */
	public static int getVersionCode(Context context) {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		int versionCode = info.versionCode;
		return versionCode;
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 * @throws Exception
	 */
	public static String getPackageName(Context context) {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String packageName = info.packageName;
		return packageName;
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

	/**
	 * 获得 IME - GSM、WCDMA /MEID - CDMA
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * 获得MetaData
	 * 
	 * @param name
	 * @return
	 */
	public static String getMetaDataValue(Context context, String key,
			String def) {
		Object value = null;
		PackageManager packageManager = context.getPackageManager();
		try {
			ApplicationInfo applicationInfo = packageManager
					.getApplicationInfo(context.getPackageName(), 128);
			if (applicationInfo != null && applicationInfo.metaData != null) {
				value = applicationInfo.metaData.get(key);
			}
		} catch (Exception e) {
			return value.toString();
		}
		return value.toString();
	}

	/**
	 * 获得网路类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentNetType(Context context) {
		String type = "";
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			type = "null";
		} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			type = "wifi";
		} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			int subType = info.getSubtype();
			if (subType == TelephonyManager.NETWORK_TYPE_CDMA
					|| subType == TelephonyManager.NETWORK_TYPE_GPRS
					|| subType == TelephonyManager.NETWORK_TYPE_EDGE) {
				type = "2g";
			} else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
					|| subType == TelephonyManager.NETWORK_TYPE_HSDPA
					|| subType == TelephonyManager.NETWORK_TYPE_HSPAP
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_0
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
				type = "3g";
			} else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
				type = "4g";
			} else {
				type = info.getSubtypeName();
			}
		}
		return type;
	}

	/**
	 * 获得IP地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalIpAddress(Context context) {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (wifiManager.isWifiEnabled()) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			return intToIp(ipAddress);
		} else {
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface
						.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							return inetAddress.getHostAddress().toString();
						}
					}
				}
			} catch (SocketException ex) {
				LogUtil.e("WifiPreference IpAddress", ex.toString());
			}
		}
		return null;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	/**
	 * 获得安装时间
	 * 
	 * @param context
	 * @return
	 */
	public static long getAppLastModifiedTime(Context context) {

		String packageName = DeviceUtil.getPackageName(context);
		PackageManager packageManager = context.getPackageManager();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);
		ResolveInfo targetInfo = new ResolveInfo();
		for (int j = 0; j < appList.size(); j++) {
			ResolveInfo resolveInfo = appList.get(j);
			String sourceDir = resolveInfo.activityInfo.applicationInfo.sourceDir;
			if (sourceDir != null && sourceDir.contains(packageName)) {
				targetInfo = resolveInfo;
				break;
			}
		}

		if ((targetInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			File file = new File(
					targetInfo.activityInfo.applicationInfo.sourceDir);
			return file.lastModified();
		} else {
			return 0;
		}
	}
}
