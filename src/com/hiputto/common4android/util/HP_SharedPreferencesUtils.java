/**
 * 
 */
package com.hiputto.common4android.util;

import android.content.SharedPreferences;

/**
 * @author xuyi
 * 
 */
public class HP_SharedPreferencesUtils {

	private static String SHARED_PREFERENCES_NAME = "Custom";

	/**
	 * 
	 */
	private HP_SharedPreferencesUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */

	private static SharedPreferences getInstance() {
//		return CustomApplication.getInstance().getSharedPreferences(
//				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		return null;
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
