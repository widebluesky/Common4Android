package com.picperweek.common4android.base;

import com.picperweek.common4android.R;
import com.picperweek.common4android.config.Constants;
import com.picperweek.common4android.manager.ActivityManager;
import com.picperweek.common4android.manager.SystemBarTintManager;
import com.picperweek.common4android.util.DeviceUtil;
import com.picperweek.common4android.util.DialogUtil;
import com.picperweek.common4android.util.LogUtil;
import com.picperweek.common4android.widget.SwipeBackLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Window;

abstract public class BaseActivity extends FragmentActivity {

	private ActivityManager activityManager = ActivityManager.getInstance();

	private SystemBarTintManager mTintManager;
	
	private SwipeBackLayout mSwipeBackLayout;
	
	/**
	 * 是否可以滑动返回变量
	 */
	private boolean mCanSlideBack = false;
	private boolean mCanBackPress = true;
	private float mDownX;
	private float mDownY;
	private float mUpX;
	private float mUpY;

	/**
	 * 判断是否需要沉浸式
	 * 
	 * @return
	 */
	abstract public boolean needTranslucent();

	/**
	 * 设置ContentView
	 */
	abstract public void setContentView();

	/**
	 * 初始化静态数据
	 */
	abstract public void initStaticData();

	/**
	 * 初始化动态数据
	 */
	abstract public void initData();

	/**
	 * 初始化界面
	 */
	abstract public void initView();

	/**
	 * 初始化监听器
	 */
	abstract public void initListener();

	@Override
	final protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activityManager.addActivity(this);
		if (needTranslucent()) {
			mTintManager = new SystemBarTintManager(this);
			mTintManager.setStatusBarTintEnabled(true);
			mTintManager.setNavigationBarTintEnabled(true);
		}
		setContentView();
		initStaticData();
		initView();
		initListener();
		initData();
	};

	/**
	 * 判断是否允许滑动返回
	 * 
	 * @return
	 */
	public boolean isCanSlideBack() {
		return mCanSlideBack;
	}

	/**
	 * 设置是否可以滑动返回
	 * 
	 * @param mCanSlideBack
	 */
	public void setCanSlideBack(boolean mCanSlideBack) {
		if (mCanSlideBack) {
			mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
					com.picperweek.common4android.R.layout.layout_swipe_back_base, null);
			mSwipeBackLayout.attachToActivity(this);
		}
		this.mCanSlideBack = mCanSlideBack;
	}

	/**
	 * 判断是否允许点击返回键结束Activity
	 * 
	 * @return
	 */
	public boolean isCanBackPress() {
		return mCanBackPress;
	}

	/**
	 * 设置是否可以点击返回键结束Activity
	 * 
	 * @param mCanBackPress
	 */
	public void setCanBackPress(boolean mCanBackPress) {
		this.mCanBackPress = mCanBackPress;
	}

	/**
	 * 显示Toast
	 * 
	 * @param message
	 * @param duration
	 */
	public void showToast(String message, int duration) {
		DialogUtil.showToast(this, message, duration);
	}

	/**
	 * 显示Progress
	 * 
	 * @param title
	 * @param message
	 * @param cancelable
	 */
	public void showProgressDialog(String title, String message, boolean cancelable) {
		DialogUtil.showProgressDialog(this, title, message, cancelable);
	}

	/**
	 * 获得ProgressDialog
	 * 
	 * @return
	 */
	public ProgressDialog getProgressDialog() {
		return DialogUtil.getProgressDialog(this);
	}

	/**
	 * 显示AlertDialog
	 * 
	 * @param title
	 * @param message
	 * @param iconR
	 */
	protected void showAlertDialog(String title, String message, int iconR) {
		DialogUtil.showAlertDialog(this, title, message, iconR);
	}

	/**
	 * 显示AlertDialog
	 * 
	 * @param title
	 * @param message
	 * @param icon
	 */
	public void showAlertDialog(String title, String message, Drawable icon) {
		DialogUtil.showAlertDialog(this, title, message, icon);
	}

	/**
	 * 获得AlertDialog
	 * 
	 * @return
	 */
	protected AlertDialog getAlertDialog() {
		return DialogUtil.getAlertDialog(this);
	}

	/**
	 * 获得AlertDialogBuilder
	 * 
	 * @return
	 */
	protected AlertDialog.Builder getAlertDialogBuilder() {
		return DialogUtil.getAlertDialogBuilder(this);
	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @return
	 */
	protected int getScreenWidth() {
		return DeviceUtil.getScreenWidth(this);
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @return
	 */
	protected int getScreenHeight() {
		return DeviceUtil.getScreenHeight(this);
	}

	/**
	 * Log错误信息
	 * 
	 * @param message
	 */
	protected void logErrorMessage(String message) {
		LogUtil.e(Constants.APP_TAG, message);
	}

	/**
	 * Log警告信息
	 * 
	 * @param message
	 */
	protected void logWarnMessage(String message) {
		LogUtil.w(Constants.APP_TAG, message);
	}

	/**
	 * Log信息
	 * 
	 * @param message
	 */
	protected void logInfoMessage(String message) {
		LogUtil.i(Constants.APP_TAG, message);
	}

	/**
	 * Log Debug信息
	 * 
	 * @param message
	 */
	protected void logDebugMessage(String message) {
		LogUtil.d(Constants.APP_TAG, message);
	}

	/**
	 * 跳转到目标 Activity
	 * 
	 * @param activityString
	 * @param isFinishCurrentActivity
	 *            是否关闭当前Activity
	 */
	public void goToActivity(String activityString, boolean isFinishCurrentActivity) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setClassName(this, activityString);
		startActivity(intent);
		if (isFinishCurrentActivity) {
			this.finish();
		}

	}

	/**
	 * 跳转到目标Activity
	 * 
	 * @param intent
	 * @param isFinishCurrentActivity
	 */
	public void goToActivity(Intent intent, boolean isFinishCurrentActivity) {
		startActivity(intent);
		if (isFinishCurrentActivity) {
			this.finish();
		}

	}

	/**
	 * 跳转到目标Activity
	 * 
	 * @param intent
	 * @param requestCode
	 */
	public void goToActivityForResult(Intent intent, int requestCode) {
		startActivityForResult(intent, requestCode);
	}

	/**
	 * 结束Activity
	 */
	protected void goToBackActivity() {
		this.finish();
	}

	@Override
	public void onBackPressed() {
		if (isCanBackPress()) {
			goToBackActivity();
		}
	}

	@Override
	protected void onDestroy() {
		activityManager.getActivityList().remove(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (isCanSlideBack()) {
			float x = ev.getX();
			float y = ev.getY();
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				mDownX = x;
				mDownY = y;
			} else if (ev.getAction() == MotionEvent.ACTION_UP) {
				mUpX = x;
				mUpY = y;
				if (mUpX > mDownX && Math.abs(mUpX - mDownX) > DeviceUtil.dip2px(this, 100)
						&& Math.abs(mUpX - mDownX) > Math.abs(mUpY - mDownY)) {
					goToBackActivity();
					overridePendingTransition(R.anim.scale_in, R.anim.slide_out_to_right);
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 设置状态栏颜色
	 * 
	 * @param alpha
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setStatusBarColor(int alpha, int red, int green, int blue) {
		if (needTranslucent() && mTintManager != null) {
			int color = Color.argb(alpha, Color.red(red), Color.green(green), Color.blue(blue));
			mTintManager.setTintColor(color);
		}
	}

	/**
	 * 设置状态栏显示资源
	 * 
	 * @param res
	 */
	public void setStatusBarResource(int res) {
		if (needTranslucent() && mTintManager != null) {
			mTintManager.setStatusBarTintResource(res);
		}
	}

	/**
	 * 设置状态栏透明度
	 * 
	 * @param res
	 */
	public void setStatusBarAlpha(int alpha) {
		if (needTranslucent() && mTintManager != null) {
			mTintManager.setStatusBarAlpha(alpha);

		}
	}

}
