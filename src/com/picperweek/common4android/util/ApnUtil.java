package com.picperweek.common4android.util;

import org.apache.http.HttpHost;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Apn工具类
 * @author widebluesky
 *
 */
public class ApnUtil {
	/** TODO */
	public static final String WAP = "wap";

	/** TODO */
	public static final String CM_NET = "cmnet";

	/** TODO */
	public static final String CM_WAP = "cmwap";

	/** TODO */
	public static final String CT_NET = "ctnet";

	/** TODO */
	public static final String CT_WAP = "ctwap";

	/** TODO */
	public static final String UN_3G_NET = "3gnet";

	/** TODO */
	public static final String UN_3G_WAP = "3gwap";

	/** TODO */
	public static final String UN_NET = "uninet";

	/** TODO */
	public static final String UN_WAP = "uniwap";

	/** TODO */
	public static final String CMWAP_PROXY = "10.0.0.172";

	/** TODO */
	public static final String CTWAP_PROXY = "10.0.0.200";

	/** TODO */
	public static final String UNWAP_PROXY = "xxx";

	/** TODO */
	public static final int DEFAULT_PORT = 80;

	public static HttpHost getHttpHost(Context context) {
		HttpHost proxy = null;
		String apnName = getActivityApnType(context);
		boolean isConnected = ApnUtil.isActivityApnConnected(context);
		if (apnName != null && isConnected) {
			if (apnName.equalsIgnoreCase(CM_WAP)) {
				proxy = new HttpHost(ApnUtil.CMWAP_PROXY, ApnUtil.DEFAULT_PORT);
			} else if (apnName.equalsIgnoreCase(CT_WAP)) {
				proxy = new HttpHost(ApnUtil.CTWAP_PROXY, ApnUtil.DEFAULT_PORT);
			} else if (apnName.equalsIgnoreCase(UN_WAP)) {
				proxy = new HttpHost(ApnUtil.CMWAP_PROXY, ApnUtil.DEFAULT_PORT);
			} else if (apnName.equalsIgnoreCase(UN_3G_WAP)) {
				proxy = new HttpHost(ApnUtil.CMWAP_PROXY, ApnUtil.DEFAULT_PORT);
			}
		}

		return proxy;
	}

	/**
	 * 获取网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getActivityApnType(Context context) {
		String apnType = null;
		ConnectivityManager conMgr = (ConnectivityManager) (context.getSystemService(Context.CONNECTIVITY_SERVICE));
		NetworkInfo info = conMgr.getActiveNetworkInfo();

		if (null == info) {
			apnType = "";
		} else {
			apnType = info.getExtraInfo();
		}

		return apnType;
	}

	/**
	 * 获取运营商名称
	 * @param context
	 * @return
	 */
	public static String getCarrierName(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm == null ? "" : tm.getSimOperatorName();
	}

	public static boolean isActivityApnConnected(Context context) {
		boolean isConnected = false;
		ConnectivityManager conMgr = (ConnectivityManager) (context.getSystemService(Context.CONNECTIVITY_SERVICE));
		NetworkInfo info = conMgr.getActiveNetworkInfo();

		if (null != info) {
			isConnected = info.isConnected();
		}

		return isConnected;
	}

	public static boolean isWapApnConnected(Context context) {
		String apnName = getActivityApnType(context);
		boolean isConnected = ApnUtil.isActivityApnConnected(context);
		if (apnName != null && isConnected) {
			if (apnName.equalsIgnoreCase(CM_WAP)) {
				return true;
			} else if (apnName.equalsIgnoreCase(CT_WAP)) {
				return true;
			} else if (apnName.equalsIgnoreCase(UN_WAP)) {
				return true;
			} else if (apnName.equalsIgnoreCase(UN_3G_WAP)) {
				return true;
			}
		}
		return false;
	}
}
