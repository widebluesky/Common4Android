package com.picperweek.common4android.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityManager {

	private volatile static ActivityManager mActivityManager;

	private static List<Activity> mActivityList = new ArrayList<Activity>();

	static {
		mActivityList = new ArrayList<Activity>();
	}

	private ActivityManager() {

	}

	/**
	 * 获取ActiivtyManager单例
	 * 
	 * @return
	 */
	public static ActivityManager getInstance() {
		if (mActivityManager == null) {
			synchronized (ActivityManager.class) {
				if (mActivityManager == null) {
					mActivityManager = new ActivityManager();
				}
			}
		}
		return mActivityManager;
	}

	/**
	 * 加入Activity
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		if (mActivityList != null) {
			synchronized (mActivityList) {
				mActivityList.add(activity);
			}
		}
	}

	/**
	 * 删除Activity，根据索引
	 * 
	 * @param index
	 * @return
	 */
	public Activity removeActivity(int index) {
		Activity activity = null;
		if (mActivityList != null) {
			synchronized (mActivityList) {
				if (!mActivityList.isEmpty()) {
					activity = mActivityList.remove(index);
				}
			}
		}
		return activity;
	}

	/**
	 * 删除Activity，根据对象
	 * 
	 * @param activity
	 * @return
	 */
	public boolean removeActivity(Activity activity) {
		boolean hasRemoved = false;
		if (mActivityList != null) {
			synchronized (mActivityList) {
				if (!mActivityList.isEmpty()) {
					hasRemoved = mActivityList.remove(activity);
				}
			}
		}
		return hasRemoved;
	}

	/**
	 * 获得Activity，根据索引
	 * 
	 * @param index
	 * @return
	 */
	public Activity getActivity(int index) {
		Activity activity = null;
		if (mActivityList != null) {
			synchronized (mActivityList) {
				if (!mActivityList.isEmpty()) {
					activity = mActivityList.get(index);
				}
			}
		}
		return activity;
	}

	/**
	 * 获得Activity列表
	 * 
	 * @return
	 */
	public List<Activity> getActivityList() {
		if (mActivityList != null) {
			synchronized (mActivityList) {
				return mActivityList;
			}
		}
		return null;
	}

	/**
	 * 获得Activity，通过ActivityName
	 * 
	 * @param activityName
	 * @return
	 */
	public List<Activity> getActivityListByName(String activityName) {
		List<Activity> list = new ArrayList<Activity>();
		if (mActivityList != null) {
			for (Activity activity : mActivityList) {
				if (activity.getClass().getName().equals(activityName)) {
					list.add(activity);
				}
			}
		}
		return list;
	}

	/**
	 * 结束所有Activity，清空Activity列表
	 */
	public void clearActivities() {
		if (mActivityList != null) {
			synchronized (mActivityList) {
				mActivityList.clear();
			}
		}
	}
}
