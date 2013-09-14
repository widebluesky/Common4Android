package com.hiputto.common4android.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;

public class HP_ActivityManager {

	private static HP_ActivityManager activityManager;

	private List<ActivityGroup> activityList = new ArrayList<ActivityGroup>();

	private HP_ActivityManager() {

	}

	public static HP_ActivityManager getInstance() {
		if (activityManager == null) {
			activityManager = new HP_ActivityManager();
		}
		return activityManager;
	}

	public void addActivity(ActivityGroup activity) {
		activityList.add(activity);
	}

	public ActivityGroup removeActivity(int index) {
		return activityList.remove(index);
	}

	public boolean removeActivity(ActivityGroup activity) {
		return activityList.remove(activity);
	}

	public ActivityGroup getActivity(int index) {
		return activityList.get(index);
	}

	public List<ActivityGroup> getActivityList() {
		return activityList;
	}

	private void setActivityList(List<ActivityGroup> activityList) {
		this.activityList = activityList;
	}

	public List<ActivityGroup> getActivityListByName(String activityName) {
		List<ActivityGroup> list = new ArrayList<ActivityGroup>();

		for (ActivityGroup activityGroup : this.activityList) {
			if (activityGroup.getClass().getName().equals(activityName)) {
				list.add(activityGroup);
			}
		}
		return list;
	}

}
