package com.picperweek.common4android.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

/**
 * Dialog工具类
 * @author widebluesky
 *
 */
public class DialogUtil {

	private static ProgressDialog progressDialog;
	private static AlertDialog alertDialog;
	private static Toast toast;

	private DialogUtil() {

	}

	/**
	 * 关闭全部对话框
	 */
	private static void cancelAll() {

		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.cancel();
			}
		}

		if (alertDialog != null) {
			if (alertDialog.isShowing()) {
				alertDialog.cancel();
			}
		}
	}

	/**
	 * 加载框
	 * 
	 * @param context
	 * @return
	 */
	public static ProgressDialog getProgressDialog(Context context) {
		cancelAll();
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
		cancelAll();
		alertDialog = getAlertDialogBuilder(context).create();
		alertDialog.setCanceledOnTouchOutside(false);
		return alertDialog;
	}

	/**
	 * 获得AlertDialogBuilder
	 * 
	 * @param context
	 * @return
	 */
	public static AlertDialog.Builder getAlertDialogBuilder(Context context) {
		return new AlertDialog.Builder(context);
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

	/**
	 * 显示Progress
	 * 
	 * @param title
	 * @param message
	 * @param cancelable
	 */
	public static void showProgressDialog(Context context, String title,
			String message, boolean cancelable) {
		ProgressDialog progressDialog = getProgressDialog(context);

		if (title != null) {
			progressDialog.setTitle(title);
		}

		if (message != null) {
			progressDialog.setMessage(message);
		}

		progressDialog.setCancelable(cancelable);
		progressDialog.setCanceledOnTouchOutside(cancelable);

		progressDialog.show();
	}

	/**
	 * 显示AlertDialog
	 * 
	 * @param title
	 * @param message
	 * @param iconR
	 */
	public static void showAlertDialog(Context context, String title,
			String message, int iconR) {
		AlertDialog alertDialog = getAlertDialog(context);
		if (title != null) {
			alertDialog.setTitle(title);
		}
		if (message != null) {
			alertDialog.setMessage(message);
		}
		if (iconR != 0) {
			alertDialog.setIcon(iconR);
		}
		alertDialog.show();
	}

	/**
	 * 显示AlertDialog
	 * 
	 * @param title
	 * @param message
	 * @param icon
	 */
	public static void showAlertDialog(Context context, String title,
			String message, Drawable icon) {
		AlertDialog alertDialog = DialogUtil.getAlertDialog(context);
		if (title != null) {
			alertDialog.setTitle(title);
		}
		if (message != null) {
			alertDialog.setMessage(message);
		}
		if (icon != null) {
			alertDialog.setIcon(icon);
		}
		alertDialog.show();
	}

}
