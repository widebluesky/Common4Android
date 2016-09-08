/**
 * 
 */
package com.picperweek.common4android.util;

import com.picperweek.common4android.base.BaseApplication;
import com.picperweek.common4android.config.Constants;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author xuyi
 * 
 */
public class SharedPreferencesUtil {


	/**
	 * 
	 */
	private SharedPreferencesUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */

	private static SharedPreferences getInstance() {
		return BaseApplication.getInstance().getSharedPreferences(
				Constants.APP_NAME, Context.MODE_PRIVATE);
	}

	public static String getString(String key, String defValue) {
		return getInstance().getString(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return getInstance().getBoolean(key, defValue);
	}

	public static int getInt(String key, int defValue) {
		return getInstance().getInt(key, defValue);
	}

	public static void putString(String key, String value) {
		SharedPreferences.Editor editor = getInstance().edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = getInstance().edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void putFloat(String key, float value) {
		SharedPreferences.Editor editor = getInstance().edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static void putLong(String key, long value) {
		SharedPreferences.Editor editor = getInstance().edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void putInt(String key, int value) {
		SharedPreferences.Editor editor = getInstance().edit();
		editor.putInt(key, value);
		editor.commit();
	}

}
