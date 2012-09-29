package com.hiputto.common4android.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;

public class HP_AsyncTaskManager {
	private List<AsyncTask<String, Integer, HashMap<String, Object>>> asyncTaskQueue = Collections
			.synchronizedList(new LinkedList<AsyncTask<String, Integer, HashMap<String, Object>>>());// 任务队列
	private static int MAX_SIZE = 5; 

	public static int getMAX_SIZE() {
		return MAX_SIZE;
	}

	public static void setMAX_SIZE(int maxSize) {
		MAX_SIZE = maxSize;
	}

	public boolean addTask(AsyncTask<String, Integer, HashMap<String, Object>> task) {
		// 对任务队列的操作要上锁
		synchronized (asyncTaskQueue) {
			if (asyncTaskQueue.size() != MAX_SIZE) {
				asyncTaskQueue.add(task);
				asyncTaskQueue.notifyAll();
				return true;
			}else{
				asyncTaskQueue.notifyAll();
				return false;
			}
		}
	}
	
	public boolean addTaskDirect(AsyncTask<String, Integer, HashMap<String, Object>> task) {
		// 对任务队列的操作要上锁
		synchronized (asyncTaskQueue) {
			if (asyncTaskQueue.size() != MAX_SIZE) {
				asyncTaskQueue.add(task);
				asyncTaskQueue.notifyAll();
				return true;
			}else{
				asyncTaskQueue.get(0).cancel(true);
				asyncTaskQueue.remove(0);
				asyncTaskQueue.add(task);
				asyncTaskQueue.notifyAll();
				return false;
			}
		}
	}

	public void removeTask(
			AsyncTask<String, Integer, HashMap<String, Object>> task) {
		synchronized (asyncTaskQueue) {
			task.cancel(true);
			asyncTaskQueue.remove(task);
			asyncTaskQueue.notifyAll();
		}
	}

	public void removeTask(int index) {
		synchronized (asyncTaskQueue) {
			asyncTaskQueue.get(index).cancel(true);
			asyncTaskQueue.remove(index);
			asyncTaskQueue.notifyAll();
		}
	}

	public void removeAllTask() {
		// 对任务队列的操作要上锁
		synchronized (asyncTaskQueue) {
			for (AsyncTask<String, Integer, HashMap<String, Object>> task : asyncTaskQueue) {
				task.cancel(true);
			}
			asyncTaskQueue.clear();
		}
	}
}
