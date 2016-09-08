package com.picperweek.common4android.manager;

import java.util.ArrayList;

import com.picperweek.common4android.config.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 夜间模式做了全局的管理,程序考虑到了通用性,若以后版本需本地换肤 功能可继续使用此类，只需进行简单扩展
 * 
 * @author widebluesky
 */
public class ThemeSettingsHelper {

	// 默认模式主题
	public static final int THEME_DEFAULT = Constants.SETTING_THEME_DEFAULT_MODE;

	// 夜间模式主题
	public static final int THEME_NIGHT = Constants.SETTING_THEME_NIGHT_MODE;

	private static ThemeSettingsHelper sThemeSettingsHelper;

	// 存放注册进来的Activity
	private final ArrayList<ThemeCallback> mCallbacks = new ArrayList<ThemeCallback>();

	// 当前主题包名
	private static int mThemePackageName;

	private ThemeSettingsHelper(Context context) {
		initTheme(context, PreferenceManager.getDefaultSharedPreferences(context).getInt(Constants.SETTING_THEME,
				Constants.SETTING_THEME_DEFAULT_MODE));
	}

	/**
	 * 单例模式供外部获得ThemeSettingsHelper对象实例
	 * 
	 * @param context
	 * @return ThemeSettingsHelper对象
	 */

	public static synchronized ThemeSettingsHelper getThemeSettingsHelper(Context context) {
		if (sThemeSettingsHelper == null) {
			sThemeSettingsHelper = new ThemeSettingsHelper(context);
		}
		return sThemeSettingsHelper;
	}

	private Object getDefaultResourceValue(Context paramContext, int paramInt) {
		Resources localResources = paramContext.getResources();
		return getResourceValue(localResources, paramInt, localResources.getResourceTypeName(paramInt));
	}

	/**
	 * @param paramResources
	 *            res资源对象
	 * @param resourceId
	 *            资源Id
	 * @param resourceTypeName
	 *            资源类型
	 * @return 指定资源对象
	 */
	private Object getResourceValue(Resources paramResources, int resourceId, String resourceTypeName) {
		Object tempResourceValue = null;
		if (!"drawable".equals(resourceTypeName)) {
			if ("color".equals(resourceTypeName)) {
				tempResourceValue = paramResources.getColor(resourceId);// 返回颜色对象
			}
		} else {
			tempResourceValue = paramResources.getDrawable(resourceId);// 返回Drawable对象
		}
		return tempResourceValue;
	}

	/**
	 * @param context
	 * @param resourceName
	 *            资源名称
	 * @param resourceTypeName
	 *            资源类型
	 * @return 指定资源对象
	 */
	private Object getResourceValueByName(Context context, String resourceName, String resourceTypeName) {
		Object tempResourceValue = null;
		String str = resourceName.trim(); // 资源名称
		Resources mResources = context.getResources();
		int resourceId = mResources.getIdentifier(str, resourceTypeName, context.getPackageName());// 资源Id
		if (resourceId == 0) {
			tempResourceValue = null;
		} else {
			tempResourceValue = getResourceValue(mResources, resourceId, resourceTypeName);
		}
		return tempResourceValue;
	}

	private int getResourceIdByName(Context context, String resourceName, String resourceTypeName) {
		int resourceId = 0;
		String str = resourceName.trim(); // 资源名称
		Resources mResources = context.getResources();
		resourceId = mResources.getIdentifier(str, resourceTypeName, context.getPackageName());// 资源Id
		return resourceId;
	}

	/**
	 * 获得主题资源
	 * 
	 * @param paramContext
	 * @param paramInt
	 * @return Drawable object
	 */
	private Object getThemeResource(Context paramContext, int paramInt) {
		Object tempResourceValue = null;
		Resources localResources = paramContext.getResources();

		String str = "";
		if (THEME_NIGHT == mThemePackageName) {
			str = "night_" + localResources.getResourceEntryName(paramInt);
		}
		if (THEME_DEFAULT == mThemePackageName) {
			str = localResources.getResourceEntryName(paramInt);
		}

		String typeName = localResources.getResourceTypeName(paramInt);
		tempResourceValue = getResourceValueByName(paramContext, str, typeName);

		if (tempResourceValue != null) {
			return tempResourceValue;
		}

		return getDefaultResourceValue(paramContext, paramInt);
	}

	/**
	 * 设置此时的context对象从属哪个Activity此处主要用于资源文件操作时使用
	 * 
	 * @param paramContext
	 * @param paramString
	 *            private void initTheme(Context context, String
	 *            mThemePackageName) { this.mThemeContext =
	 *            getThemeContext(context, mThemePackageName); }
	 */

	private void initTheme(Context context, int mThemePackageName) {
		ThemeSettingsHelper.mThemePackageName = mThemePackageName;
	}

	/**
	 * 提供外部调用换肤入口
	 * 
	 * @param paramContext
	 * @param paramString
	 */
	public synchronized void changeTheme(Context context, int mThemePackageName) {
		// 每次切换都要持久化
		if (ThemeSettingsHelper.mThemePackageName != mThemePackageName) {
			Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
			editor.putInt(Constants.SETTING_THEME, mThemePackageName);
			editor.commit();
			initTheme(context, mThemePackageName);
		}
		// 遍历堆栈中的使用主题的Activity然后调用其applyTheme()
		if (mCallbacks != null && mCallbacks.size() > 0) {
			for (ThemeCallback tempWeakReferenceThemeCallback : mCallbacks) {
				if (tempWeakReferenceThemeCallback != null) {
					tempWeakReferenceThemeCallback.applyTheme();
				}
			}
		}

	}

	public synchronized void loadDefaultTheme(ThemeCallback Callback) {
		if (Callback != null) {
			Callback.applyTheme();
		}
	}

	/**
	 * 暂时没有使用到
	 * 
	 * @return
	 * 
	 * 		public String getCurrentThemePackage() { return
	 *         mThemePackageName; }
	 */

	public int getCurrentThemePackage() {
		return mThemePackageName;
	}

	/*
	 * 根据日间模式的资源ID获取对应的夜间模式资源ID 参数说明： paramInt: 日间模式的资源ID
	 */
	public int getThemeResourceID(Context paramContext, int paramInt) {
		Resources localResources = paramContext.getResources();
		String str = "";
		// 如果是夜间模式，需要获取对应的夜间模式资源ID
		if (THEME_NIGHT == mThemePackageName) {
			str = "night_" + localResources.getResourceEntryName(paramInt);
			String typeName = localResources.getResourceTypeName(paramInt);
			return (getResourceIdByName(paramContext, str, typeName));
		} else {
			return paramInt;
		}
	}

	/**
	 * 通用获取颜色
	 * 
	 * @param paramContext
	 * @param paramInt
	 * @return
	 */
	public Integer getThemeColor(Context paramContext, int paramInt) {
		Integer themeColor = (Integer) getThemeResource(paramContext, paramInt);
		return themeColor;
	}

	/**
	 * 通用获取Drawable
	 * 
	 * @param paramContext
	 * @param paramInt
	 * @return
	 */
	public Drawable getThemeDrawable(Context paramContext, int paramInt) {
		return (Drawable) getThemeResource(paramContext, paramInt);
	}

	public boolean isDefaultTheme() {
		// return THEME_DEFAULT.equals(mThemePackageName);
		if (mThemePackageName == THEME_DEFAULT) {
			return true;
		}
		return false;
	}

	public boolean isNightTheme() {
		if (mThemePackageName == Constants.SETTING_THEME_NIGHT_MODE) {
			return true;
		}
		return false;
	}

	/**
	 * 将需要设置主题的Activity注册到该管理对象中
	 * 
	 * @param paramThemeCallback
	 *            实现ThemeCallback接口的Activity
	 */
	public void registerThemeCallback(ThemeCallback themeCallback) {
		mCallbacks.add(themeCallback);
	}

	/**
	 * 对ImageView控件的Src属性切换资源文件
	 * 
	 * @param context
	 * @param ImageView
	 *            控件对象
	 * @param paramInt
	 *            要切换的资源id
	 */
	public void setImageViewSrc(Context context, ImageView imageView, int paramInt) {
		// 添加
		if (imageView == null) {
			return;
		}
		imageView.setImageDrawable(getThemeDrawable(context, paramInt));
	}

	public void setImageButtonSrc(Context context, ImageButton imageBtn, int paramInt) {
		// 添加
		if (imageBtn == null) {
			return;
		}
		imageBtn.setImageDrawable(getThemeDrawable(context, paramInt));
	}

	/**
	 * 对ListView控件的Divider属性切换资源文件
	 * 
	 * @param context
	 * @param listView
	 * @param paramInt
	 */
	public void setListViewDivider(Context context, ListView listView, int paramInt) {
		if (listView == null) {
			return;
		}
		int nPosition = listView.getFirstVisiblePosition();
		listView.setDivider(getThemeDrawable(context, paramInt));
		listView.setSelection(nPosition);
	}

	/**
	 * 对ListView的Selector属性切换资源文件
	 * 
	 * @param context
	 * @param listView
	 * @param paramInt
	 */
	public void setListViewSelector(Context context, ListView listView, int paramInt) {
		if (listView == null) {
			return;
		}
		listView.setSelector(getThemeDrawable(context, paramInt));
	}

	/**
	 * 对Listview设置CacheColorHint
	 * 
	 * @param context
	 * @param view
	 * @param paramInt
	 */
	public void setListViewCacheColorHit(Context paramContext, ListView paramTextView, int paramInt) {
		if (paramTextView == null) {
			return;
		}
		paramTextView.setCacheColorHint(getThemeColor(paramContext, paramInt));
	}

	/**
	 * 对textView控件的背景颜色进行切换
	 * 
	 * @param context
	 * @param textView
	 * @param paramInt
	 */
	public void setTextViewColor(Context paramContext, TextView paramTextView, int paramInt) {
		if (paramTextView == null) {
			return;
		}
		paramTextView.setTextColor(getThemeColor(paramContext, paramInt));
	}

	public void setButtonTextColor(Context paramContext, Button paramButton, int paramInt) {
		if (paramButton == null) {
			return;
		}
		paramButton.setTextColor(getThemeColor(paramContext, paramInt));
	}

	/**
	 * 对View控件进行背景资源文件的切换
	 * 
	 * @param context
	 * @param view
	 * @param paramInt
	 */
	public void setViewBackgroud(Context paramContext, View view, int paramInt) {
		if (view == null) {
			return;
		}
		Drawable drawable = getThemeDrawable(paramContext, paramInt);
		view.setBackgroundDrawable(drawable);
	}

	public void setViewBackgroudKeepPadding(Context paramContext, View view, int paramInt) {
		if (view == null) {
			return;
		}

		int bottom = view.getPaddingBottom();
		int top = view.getPaddingTop();
		int left = view.getPaddingLeft();
		int right = view.getPaddingRight();

		Drawable drawable = getThemeDrawable(paramContext, paramInt);
		view.setBackgroundDrawable(drawable);
		view.setPadding(left, top, right, bottom);
	}

	/**
	 * 对View控件的背景颜色惊醒切换
	 * 
	 * @param context
	 * @param view
	 * @param paramInt
	 */
	public void setViewBackgroudColor(Context paramContext, View paramView, int paramInt) {
		if (paramView == null) {
			return;
		}
		paramView.setBackgroundColor(getThemeColor(paramContext, paramInt));
	}

	/**
	 * 对CheckBox控件Button进行切换
	 * 
	 * @param context
	 * @param view
	 * @param paramInt
	 */
	public void setCheckBoxBackgroud(Context paramContext, CheckBox paramView, int paramInt) {
		if (paramView == null) {
			return;
		}
		paramView.setButtonDrawable(getThemeDrawable(paramContext, paramInt));
	}

	/**
	 * 对PopuoWindow控件背景进行切换
	 * 
	 * @param context
	 * @param view
	 * @param paramInt
	 */
	public void setPopupWindowBackgroud(Context paramContext, PopupWindow paramView, int paramInt) {
		if (paramView == null) {
			return;
		}
		paramView.setBackgroundDrawable(getThemeDrawable(paramContext, paramInt));
	}

	/**
	 * 对EditText控件提示的文字颜色
	 * 
	 * @param context
	 * @param view
	 * @param paramInt
	 */
	public void setEditTextHitColor(Context paramContext, EditText paramView, int paramInt) {
		if (paramView == null) {
			return;
		}
		paramView.setHintTextColor(getThemeColor(paramContext, paramInt));
	}

	public void setEditTextDrawableLeft(Context paramContext, EditText paramView, int paramInt) {
		if (paramView == null) {
			return;
		}

		paramView.setCompoundDrawablesWithIntrinsicBounds(getThemeDrawable(paramContext, paramInt), null, null, null);
	}

	/**
	 * 对窗口的背景资源进行切换
	 * 
	 * @param paramActivity
	 * @param paramInt
	 */
	public void setWindowBackgroud(Activity paramActivity, int paramInt) {
		paramActivity.getWindow().setBackgroundDrawable(getThemeDrawable(paramActivity, paramInt));
	}

	/**
	 * 设置viewpager间隔线背景
	 */

	public void setViewPagerMargin(Context paramContext, ViewPager paramView, int paramInt) {
		if (paramView == null) {
			return;
		}
		paramView.setPageMarginDrawable(getThemeDrawable(paramContext, paramInt));
	}

	/**
	 * 将换肤Activity注册进来
	 * 
	 * @param themeCallback
	 */
	public void unRegisterThemeCallback(ThemeCallback themeCallback) {// 返注册ThemeCallback对象
		ArrayList<ThemeCallback> tempCallbacks = this.mCallbacks;
		tempCallbacks.remove(themeCallback);
	}

	public void popupToMainActivity(ThemeCallback themeCallback) {
		if (mCallbacks != null && mCallbacks.size() > 0) {
			for (ThemeCallback tmpCallback : mCallbacks) {
				if (tmpCallback != null) {
					boolean isActivity = (tmpCallback instanceof Activity);
					boolean isMainActivity = isActivity
							&& tmpCallback.toString().toLowerCase().contains("mainactivity");
					boolean isSelf = (themeCallback == tmpCallback);

					if (isActivity && isMainActivity == false && isSelf == false) {
						((Activity) tmpCallback).finish();
					}
				}
			}
		}
	}

	/**
	 * 为需使用主题模式切换的Activity提供设置主题的接口
	 */
	public static abstract interface ThemeCallback {
		public abstract void applyTheme();
	}

}
