package com.hiputto.common4android.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class HP_DialogUtils {

	private static ProgressDialog progressDialog;
	private static AlertDialog alertDialog;
	private static Toast toast;

	private HP_DialogUtils() {

	}

	/**
	 * 加载框
	 * 
	 * @param context
	 * @return
	 */
	public static ProgressDialog getProgressDialog(Context context) {

		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.cancel();
			}
		}
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);

		return progressDialog;
	}

	/**
	 * 对话框
	 * 
	 * @param context
	 * @return
	 */
	public static AlertDialog getAlertDialog(Context context) {

		if (alertDialog != null) {
			if (alertDialog.isShowing()) {
				alertDialog.cancel();
			}
		}

		alertDialog = new Builder(context).create();
		alertDialog.setCanceledOnTouchOutside(false);
		return alertDialog;
	}

	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToast(Context context, String text, int duration) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}
