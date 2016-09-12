package com.picperweek.common4android.http.task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 线程池
 * @author widebluesky
 *
 */
public class RunnablePool {
	private static RunnablePool instance = null;
	private int maxSize=2;// >1
	private int step=1;
	private ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(maxSize);
	private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 2*1000, TimeUnit.MILLISECONDS, queue,new ThreadPoolExecutor.DiscardOldestPolicy());
	
	public static RunnablePool getInstance() {
		if (instance == null) {
			instance = new RunnablePool();
		}
		return instance;
	}

	public void addTask(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.start();
	}

	public void addTaskIntoPool(Runnable runnable) {
		executor.execute(runnable);
	}
	
	public boolean poll(int n){
		for(int i=0;i<n;++i){
			Runnable r=RunnablePool.getInstance().executor.getQueue().poll();
			if(null==r){
				return true;
			}
		}
		return 0==RunnablePool.getInstance().executor.getQueue().size();
	}
	
	public int size(){
		return RunnablePool.getInstance().executor.getQueue().size();
	}
	
	public int getMaxSize(){
		return maxSize;
	}
	
	public int getStep(){
		return RunnablePool.getInstance().step;
	}
	
	public boolean isReady(){
		return RunnablePool.getInstance().size()<RunnablePool.getInstance().getMaxSize();
	}
}
