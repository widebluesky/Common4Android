package com.hiputto.common4android.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class HP_AppUtils {
	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 * @throws Exception
	 */
	public String getVersionName(Context context) throws Exception {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
		String versionName = info.versionName;
		return versionName;
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 * @throws Exception
	 */
	public int getVersionCode(Context context) throws Exception {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
		int versionCode = info.versionCode;
		return versionCode;
	}

}
