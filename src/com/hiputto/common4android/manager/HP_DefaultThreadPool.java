package com.hiputto.common4android.manager;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

/**
 * 线程池 、缓冲队列
 * 
 * @author xuyi
 * 
 */
public class HP_DefaultThreadPool {

	/**
	 * 线程池维护线程的最小数量
	 */
	private static int corePoolSize = 10;

	/**
	 * 线程池维护线程的最大数量
	 */
	private static int maxinumPoolSize = 20;

	/**
	 * 线程池维护线程所允许的空闲时间
	 */
	private static long keepAliveTime = 15L;

	/**
	 * 线程池维护线程所允许的空闲时间的单位
	 */
	private static TimeUnit unit = TimeUnit.SECONDS;

	/**
	 * BaseRequest任务队列
	 */
	private static ArrayBlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(
			15);
	/**
	 * 线程池
	 */
	static AbstractExecutorService pool = new ThreadPoolExecutor(corePoolSize,
			maxinumPoolSize, keepAliveTime, unit, blockingQueue,
			new ThreadPoolExecutor.DiscardOldestPolicy());

	private static HP_DefaultThreadPool instance = null;

	public static HP_DefaultThreadPool getInstance() {
		if (instance == null) {
			instance = new HP_DefaultThreadPool();
		}
		return instance;
	}

	public void execute(Runnable r) {
		pool.execute(r);
	}

	/**
	 * 关闭，并等待任务执行完成，不接受新任务
	 */
	public static void shutdown() {
		if (pool != null) {
			pool.shutdown();
			Log.i(HP_DefaultThreadPool.class.getName(),
					"DefaultThreadPool shutdown");
		}
	}

	/**
	 * 关闭，立即关闭，并挂起所有正在执行的线程，不接受新任务
	 */
	public static void shutdownRightnow() {
		if (pool != null) {
			pool.shutdownNow();
			try {
				// 设置超时极短，强制关闭所有任务
				pool.awaitTermination(1, TimeUnit.MICROSECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Log.i(HP_DefaultThreadPool.class.getName(),
					"DefaultThreadPool shutdownRightnow");
		}
	}

	/**
	 * 删除将要执行的线程
	 * 
	 * @param runnable
	 */
	public static void removeTaskFromQueue(Runnable runnable) {
		if (blockingQueue.contains(runnable)) {
			blockingQueue.remove(runnable);
		}
	}
}
