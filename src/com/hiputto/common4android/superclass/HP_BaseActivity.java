package com.hiputto.common4android.superclass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Display;

public class HP_BaseActivity extends Activity {
	/**
	 * 
	 */
	public HP_BaseActivity() {
		super();
	}

	private ProgressDialog progressDialog;
	private AlertDialog alertDialog;
	private Display display;
	private DisplayMetrics displayMetrics;

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//StrictMode
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        
		display = getWindowManager().getDefaultDisplay();
		displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		
		progressDialog = new ProgressDialog(this);
		alertDialog = new AlertDialog.Builder(this).create();
	};

	protected void showProgressDialog(String title, String message) {
		if (title != null) {
			progressDialog.setTitle(title);
		}

		if (message != null) {
			progressDialog.setMessage(message);
		}
		progressDialog.show();
	}

	protected ProgressDialog getProgressDialog() {
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

	protected void showAlertDialog(String title, String message, Drawable icon) {
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

	protected AlertDialog getAlertDialog() {
		return this.alertDialog;
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
}
