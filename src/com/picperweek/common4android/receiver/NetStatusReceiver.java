package com.picperweek.common4android.receiver;

import com.picperweek.common4android.config.Constants;
import com.picperweek.common4android.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态广播
 * 
 * 
 */
public class NetStatusReceiver extends BroadcastReceiver {

	public static final int NETSTATUS_INAVAILABLE = 0;
	public static final int NETSTATUS_WIFI = 1;
	public static final int NETSTATUS_MOBILE = 2;
	public static int netStatus = 0;

	public static boolean updateSuccess = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		// Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetInfo = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo allNetInfo = cm.getActiveNetworkInfo();

		if (allNetInfo == null) {
			if (mobileNetInfo != null
					&& (mobileNetInfo.isConnected() || mobileNetInfo
							.isConnectedOrConnecting())) {
				netStatus = NETSTATUS_MOBILE;
			} else if (wifiNetInfo.isConnected()
					|| wifiNetInfo.isConnectedOrConnecting()) {
				netStatus = NETSTATUS_WIFI;
			} else {
				netStatus = NETSTATUS_INAVAILABLE;
			}
		} else {
			if (allNetInfo.isConnected()
					|| allNetInfo.isConnectedOrConnecting()) {
				if (mobileNetInfo != null
						&& (mobileNetInfo.isConnected() || mobileNetInfo
								.isConnectedOrConnecting())) {
					netStatus = NETSTATUS_MOBILE;
				} else {
					netStatus = NETSTATUS_WIFI;
				}
			} else {
				netStatus = NETSTATUS_INAVAILABLE;
			}
		}

		if (netStatus == NETSTATUS_INAVAILABLE) {
			LogUtil.w(Constants.APP_TAG, "[System]:网络未连失败");
		} else if (netStatus == NETSTATUS_MOBILE) {
			LogUtil.w(Constants.APP_TAG, "[System]:网络处于移动网络");
		}  if (netStatus == NETSTATUS_WIFI) {
			LogUtil.w(Constants.APP_TAG, "[System]:网络处于Wifi网络");
		}else {
			LogUtil.w(Constants.APP_TAG, "[System]:网络状态未知");
		}
	}
}
