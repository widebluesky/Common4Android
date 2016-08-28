package com.picperweek.common4android.base;

import com.picperweek.common4android.receiver.ExternalStorageReceiver;
import com.picperweek.common4android.receiver.NetStatusReceiver;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;

public class BaseApplication extends Application {

	private static BaseApplication mBaseApplication;
	private Handler mObservableHandler = new Handler();

	public BaseApplication() {
		super();
	}

	/**
	 * 获取SDKApi单例
	 * 
	 * @return
	 */
	public static BaseApplication getInstance() {
		if (mBaseApplication == null) {
			synchronized (BaseApplication.class) {
				if (mBaseApplication == null) {
					mBaseApplication = new BaseApplication();
				}
			}
		}
		return mBaseApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mBaseApplication = this;
		initNetAndSDCard();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public void runOnUIThread(Runnable r) {
		mObservableHandler.post(r);
	}
	
	/**
	 * 初始化SD、网络状态
	 */
	public void initNetAndSDCard() {
		ConnectivityManager cm = (ConnectivityManager) mBaseApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo allNetInfo = cm.getActiveNetworkInfo();
		if (allNetInfo == null) {
			if (mobileNetInfo != null
					&& (mobileNetInfo.isConnected() || mobileNetInfo
							.isConnectedOrConnecting())) {
				NetStatusReceiver.netStatus = NetStatusReceiver.NETSTATUS_MOBILE;
			} else if (wifiNetInfo != null
					&& (wifiNetInfo.isConnected() || wifiNetInfo
							.isConnectedOrConnecting())) {
				NetStatusReceiver.netStatus = NetStatusReceiver.NETSTATUS_WIFI;
			} else {
				NetStatusReceiver.netStatus = NetStatusReceiver.NETSTATUS_INAVAILABLE;
			}
		} else {
			if (allNetInfo.isConnected()
					|| allNetInfo.isConnectedOrConnecting()) {
				if (mobileNetInfo != null
						&& (mobileNetInfo.isConnected() || mobileNetInfo
								.isConnectedOrConnecting())) {
					NetStatusReceiver.netStatus = NetStatusReceiver.NETSTATUS_MOBILE;
				} else {
					NetStatusReceiver.netStatus = NetStatusReceiver.NETSTATUS_WIFI;
				}
			} else {
				NetStatusReceiver.netStatus = NetStatusReceiver.NETSTATUS_INAVAILABLE;
			}
		}

		ExternalStorageReceiver.isSDCardMounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
		IntentFilter filterNet = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		NetStatusReceiver netReceiver = new NetStatusReceiver();
		mBaseApplication.registerReceiver(netReceiver, filterNet);
		IntentFilter filterStorage = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
		filterStorage.addDataScheme("file");
		filterStorage.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		filterStorage.addAction(Intent.ACTION_MEDIA_REMOVED);
		filterStorage.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		ExternalStorageReceiver storageReceiver = new ExternalStorageReceiver();
		mBaseApplication.registerReceiver(storageReceiver, filterStorage);
	}
}
