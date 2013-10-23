package com.hiputto.common4android.superclass;

import com.hiputto.common4android.R;
import com.hiputto.common4android.manager.HP_ActivityManager;
import com.hiputto.common4android.util.HP_DeviceUtils;
import com.hiputto.common4android.util.HP_StringUtils;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class HP_BaseActivity extends ActivityGroup {
	/**
	 * 
	 */
	public HP_BaseActivity() {
		super();
	}

	private HP_ActivityManager activityManager = HP_ActivityManager
			.getInstance();

	private ProgressDialog progressDialog;
	private AlertDialog alertDialog;
	private Display display;
	private DisplayMetrics displayMetrics;

	private Toast toast;

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityManager.addActivity(this);

		String strVer = HP_DeviceUtils.getBuild_VERSION_RELEASE();
		strVer = strVer.substring(0, 3).trim();
		float fv = Float.valueOf(strVer);
		if (fv > 2.3) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																			// 就包括了磁盘读写和网络I/O
					.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
					.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
					.penaltyLog() // 打印logcat
					.penaltyDeath().build());
		}

		display = getWindowManager().getDefaultDisplay();
		displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);

		progressDialog = new ProgressDialog(this);
		alertDialog = new AlertDialog.Builder(this).create();
	};

	public void showToast(String message, int duration) {

		if (toast == null) {
			toast = Toast.makeText(this, message, duration);
		}

		toast.setText(message);
		toast.cancel();
		toast.show();
	}

	public void showProgressDialog(String title, String message,
			boolean cancelable) {
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

	public ProgressDialog getProgressDialog() {
		return this.progressDialog;
	}

	protected void showAlertDialog(String title, String message, int iconR) {
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

	public void showAlertDialog(String title, String message, Drawable icon) {
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

	public void showSuccessDialog(String message,
			DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder alertDialogBuilder = getAlertDialogBuilder();
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setNegativeButton("确定", onClickListener);
		alertDialogBuilder.create().show();
	}

	public void showErrorDialog(String message) {
		AlertDialog.Builder alertDialogBuilder = getAlertDialogBuilder();
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setNegativeButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialogBuilder.create().show();
	}

	protected void showErrorDialog(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = getAlertDialogBuilder();
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setNegativeButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialogBuilder.create().show();
	}

	protected void showErrorDialog(String message,
			DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder alertDialogBuilder = getAlertDialogBuilder();
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setNegativeButton("确定", onClickListener);
		alertDialogBuilder.create().show();
	}

	protected AlertDialog getAlertDialog() {
		return this.alertDialog;
	}

	protected AlertDialog.Builder getAlertDialogBuilder() {
		return new AlertDialog.Builder(this);
	}

	protected int getScreenWidth() {
		return display.getWidth();
	}

	protected int getScreenHeight() {
		return display.getHeight();
	}

	protected int getScreenWidthPixels() {
		return displayMetrics.widthPixels;
	}

	protected int getScreenHeightPixels() {
		return displayMetrics.heightPixels;
	}

	protected void logErrorMessage(String message) {
		if (message == null) {
			message = "message is null";
		} else if (HP_StringUtils.isEmptyString(message)) {
			message = "";
		}
		Log.e("[ERROR - " + this.getClass().getName() + "]", message);
	}

	protected void logWarnMessage(String message) {
		if (message == null) {
			message = "message is null";
		} else if (HP_StringUtils.isEmptyString(message)) {
			message = "";
		}
		Log.w("[WARN - " + this.getClass().getName() + "]", message);
	}

	protected void logInfoMessage(String message) {
		if (message == null) {
			message = "message is null";
		} else if (HP_StringUtils.isEmptyString(message)) {
			message = "";
		}
		Log.i("[INFO - " + this.getClass().getName() + "]", message);
	}

	protected void logDebugMessage(String message) {
		if (message == null) {
			message = "message is null";
		} else if (HP_StringUtils.isEmptyString(message)) {
			message = "";
		}
		Log.d("[DEBUG - " + this.getClass().getName() + "]", message);
	}

	public void goToActivity(String activityString,
			boolean isFinishCurrentActivity) {

		Intent intent = new Intent();
		intent.setClassName(this, activityString);
		startActivity(intent);

		if (isFinishCurrentActivity) {
			this.finish();
		}

	}

	public void goToActivity(Intent intent, boolean isFinishCurrentActivity) {

		startActivity(intent);
		if (isFinishCurrentActivity) {
			this.finish();
		}

	}

	public void goToActivityForResult(Intent intent, int requestCode) {
		startActivityForResult(intent, requestCode);
	}

	protected void goToBackActivity() {
		this.finish();
	}

	@Override
	public void onBackPressed() {
		goToBackActivity();
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	}

	protected String uri2filePath(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, proj, null, null, null);
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(index);
		return path;
	}

	protected void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	protected void onDestroy() {
		activityManager.getActivityList().remove(this);
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (this.getCurrentFocus() != null
				&& this.getCurrentFocus().getWindowToken() != null) {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(this.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}

		return super.dispatchTouchEvent(ev);
	}

	protected void hideIM(EditText editText) {
		InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		IBinder windowToken = editText.getWindowToken();
		if (windowToken != null) {
			im.hideSoftInputFromWindow(windowToken, 0);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
