package com.picperweek.common4android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

/**
 * 外部存储设备状态广播
 * 
 *
 */
public class ExternalStorageReceiver extends BroadcastReceiver {
	
	public static boolean isSDCardMounted = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {
			isSDCardMounted = true;
		} else if (intent.getAction().equals("android.intent.action.MEDIA_REMOVED")
				|| intent.getAction().equals("android.intent.action.ACTION_MEDIA_UNMOUNTED")
				|| intent.getAction().equals("android.intent.action.ACTION_MEDIA_BAD_REMOVAL")) {
			isSDCardMounted = false;
		} else {
			ExternalStorageReceiver.isSDCardMounted = Environment.MEDIA_MOUNTED.equals(
					Environment.getExternalStorageState());
		}
	}
}
