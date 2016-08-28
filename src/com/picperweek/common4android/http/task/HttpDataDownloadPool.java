package com.picperweek.common4android.http.task;

import java.util.ArrayList;

import com.picperweek.common4android.base.BaseApplication;
import com.picperweek.common4android.config.Constants;
import com.picperweek.common4android.http.HttpDeleteEngine;
import com.picperweek.common4android.http.HttpEngine;
import com.picperweek.common4android.http.HttpGetEngine;
import com.picperweek.common4android.http.HttpPostEngine;
import com.picperweek.common4android.http.HttpPostMutipleEngine;
import com.picperweek.common4android.http.HttpPutEngine;
import com.picperweek.common4android.http.HttpEngine.HttpCode;
import com.picperweek.common4android.http.command.HttpDataRequest;
import com.picperweek.common4android.http.command.HttpDataResponse;
import com.picperweek.common4android.http.command.HttpTagDispatch;
import com.picperweek.common4android.http.model.HttpInfo;
import com.picperweek.common4android.http.model.HttpResult;


/**
 * 数据下载线程池
 * 
 * @author widebluesky
 *
 */
public class HttpDataDownloadPool {
	
	private static final int POOL_SIZE = 2;
	private static HttpDataDownloadPool instance = null;
	private ArrayList<HttpInfo> mHttpPrepareQueue = new ArrayList<HttpInfo>();
	private HttpInfo[] mHttpProcessingArray = new HttpInfo[POOL_SIZE];
	private HttpThread[] mHttpThread = new HttpThread[POOL_SIZE];
	private boolean[] mProcessingState = { false, false };
	private static final int MAX_PREPARE_NUM = 20;
	private static final long MAX_TIME = 5 * 60 * 1000;

	public HttpDataDownloadPool(){
		
	}
	
	public static synchronized HttpDataDownloadPool getInstance() {
		if (instance == null) {
			instance = new HttpDataDownloadPool();
		}
		return instance;
	}

	public void addTask(HttpDataRequest request, HttpDataResponse response) {
		synchronized (mHttpPrepareQueue) {
			HttpInfo httpinfo = new HttpInfo(request, response);
			long thistime = System.currentTimeMillis();
			httpinfo.setStartTime(thistime);
			// 添加到任务队列中
			
			mHttpPrepareQueue.add(0,httpinfo);
			int length = mHttpPrepareQueue.size();
			if (length >= MAX_PREPARE_NUM) {
				HttpInfo removeInfo = mHttpPrepareQueue.remove(length-1);
				// 系统繁忙，任务自动被取消，返回ERROR_NET_ACCESS错误
				onRecvError(removeInfo.getRequest(), removeInfo.getResponse(), HttpCode.SYSTEM_CANCELLED, Constants.HTTP_DATA_BUSY);
			}

			// 超过五分钟的请求全部消除
			for (int i = mHttpPrepareQueue.size() - 1; i >= 0; i--) {
				long time = mHttpPrepareQueue.get(i).getStartTime();
				if (Math.abs(thistime - time) > MAX_TIME) {
					HttpInfo removeInfo = mHttpPrepareQueue.remove(i);
					// 系统繁忙，任务自动被取消，返回ERROR_NET_ACCESS错误
					onRecvError(removeInfo.getRequest(), removeInfo.getResponse(), HttpCode.ERROR_NET_ACCESS, Constants.HTTP_DATA_CONNECT_TIMEOUT);
					
					
				}
			}
		}

		for (int i = 0; i < POOL_SIZE; i++) {
			if (mProcessingState[i] == false) {
				// 开始下载任务
				mProcessingState[i] = true;
				mHttpThread[i] = new HttpThread(i);
				mHttpThread[i].setPriority(Constants.THREAD_PRIORITY);
				mHttpThread[i].start();
				break;
			}
		}
	}

	private HttpInfo pullOneTaskInHeadSync() {
		synchronized (mHttpPrepareQueue) {
			if (mHttpPrepareQueue.size() == 0) {
				return null;
			}
			HttpInfo headInfo = mHttpPrepareQueue.remove(0);
			return headInfo;
		}
	}

	/**
	 * 退出系统时使用
	 */
	public void waitThreadExit() {
		for (int i = 0; i < POOL_SIZE; i++) {
			try {
				if (mHttpThread[i] != null)
					mHttpThread[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 退出系统时使用
	 */
	public void cancelAllThread() {
		if (mHttpPrepareQueue != null && mHttpPrepareQueue.size() > 0) {
			// 备份一份数据，清除原始队列，通知所有http请求失败。
			synchronized (mHttpPrepareQueue) {
				ArrayList<HttpInfo> backupQueue = new ArrayList<HttpInfo>();
				backupQueue.addAll(mHttpPrepareQueue);
				mHttpPrepareQueue.clear();
				for (HttpInfo hi : backupQueue) {
					onRecvCancelled(hi.getRequest(), hi.getResponse());
				}
			}
		}
		for (int i = 0; i < POOL_SIZE; i++) {
			if (mProcessingState[i] && mHttpThread[i] != null) {
				// 这里的Cancel会继续运行当前任务，运行完成后会停止,所以不需要通知Error。
				mHttpThread[i].cancel();
			}
			mProcessingState[i] = false;
		}
	}

	private void onRecvOK(final HttpDataRequest request, final HttpDataResponse response, final HttpCode retCode, final Object result) {
		BaseApplication.getInstance().runOnUIThread(new Runnable() {
			public void run() {
				response.onHttpRecvOK(request.getTag(), request.getExtraInfo(), result);
			}
		});
	}

	private void onRecvError(final HttpDataRequest request, final HttpDataResponse response, final HttpCode retCode, final String msg) {
		BaseApplication.getInstance().runOnUIThread(new Runnable() {

			public void run() {
				response.onHttpRecvError(request.getTag(), retCode, msg);
			}
		});
	}

	private void onRecvCancelled(final HttpDataRequest request, final HttpDataResponse response) {
		BaseApplication.getInstance().runOnUIThread(new Runnable() {
			public void run() {
				response.onHttpRecvCancelled(request.getTag());
			}
		});
	}

	private class HttpThread extends Thread {

		private int runId = 0;
		private boolean cancel = false;

		public HttpThread(int runId) {
			super();
			this.runId = runId;
		}

		public void cancel() {
			this.cancel = true;
		}

		@Override
		public void run() {
			// Auto-generated method stub
			mProcessingState[runId] = true;
			while (!cancel) {
				mHttpProcessingArray[runId] = pullOneTaskInHeadSync();

				if (mHttpProcessingArray[runId] == null) {
					break;
				}

				HttpDataRequest request = mHttpProcessingArray[runId].getRequest();
				HttpDataResponse response = mHttpProcessingArray[runId].getResponse();
				HttpResult result = null;
				
				String sort = request.getSort();
				HttpEngine httpEngine = null;
				if (sort.equalsIgnoreCase(Constants.REQUEST_METHOD_GET)) {
					httpEngine = new HttpGetEngine(request);
				} else if (sort.equalsIgnoreCase(Constants.REQUEST_METHOD_POST)) {
					httpEngine = new HttpPostEngine(request);
				} else if (sort.equalsIgnoreCase(Constants.REQUEST_METHOD_POST_MULTIPLE)) {
					httpEngine = new HttpPostMutipleEngine(request);
				} else if (sort.equalsIgnoreCase(Constants.REQUEST_METHOD_PUT)) {
					httpEngine = new HttpPutEngine(request);
				} else if (sort.equalsIgnoreCase(Constants.REQUEST_METHOD_DELETE)) {
					httpEngine = new HttpDeleteEngine(request);
				}
				/**
				 * 完全从网络取数据
				 */
				result = httpEngine.execute();
				if (result == null) {
					onRecvError(request, response, HttpCode.ERROR_NET_ACCESS, Constants.HTTP_DATA_BUSY);
					continue;
				}
				
				if(request.isCancelled()) {
					onRecvCancelled(request, response);
					continue;
				}
				
				if (result.getResultCode() == HttpCode.STATUS_OK && result.getData() != null) {
					request.setExtraInfo(result.getHeadParams());
					try {
						// 网络访问成功
						String json = new String(result.getData());
						json = json.trim();
						final Object jsonResult = HttpTagDispatch.dispatch(request, json);
						if (jsonResult != null) {
							onRecvOK(request, response, HttpCode.STATUS_OK, jsonResult);

						} else {
							onRecvError(request, response, HttpCode.ERROR_NET_ACCESS, Constants.HTTP_DATA_FAIL);
						}
						continue;
					} catch (Exception e) {
						onRecvError(request, response, HttpCode.ERROR_NET_ACCESS, Constants.HTTP_DATA_FAIL);
						continue;
					}

				} else {
					// 列举所有网络错误
					String errorMsg = Constants.HTTP_DATA_BUSY;
					switch (result.getResultCode()) {
					case STATUS_OK:
						/**
						 * 返回状态正确，但是没有数据，也认为是访问失败了
						 */
						result.setResultCode(HttpCode.ERROR_NET_ACCESS);
					case ERROR_NO_CONNECT:
						errorMsg = Constants.HTTP_DATA_NONET;
						break;
					case ERROR_NO_REGISTER:
						errorMsg = Constants.HTTP_DATA_USER_NOCHECK;
						break;
					case ERROR_NET_TIMEOUT:
						errorMsg = Constants.HTTP_DATA_CONNECT_TIMEOUT;
						break;
					case ERROR_NET_ACCESS:
						errorMsg = Constants.HTTP_DATA_BUSY;
						break;
					case ERROR_SERVICE_ACCESS:
						errorMsg = Constants.HTTP_SERVICE_ERROR;
						break;
					default:
						break;
					}
					onRecvError(request, response, result.getResultCode(), errorMsg);
				}
			}

			mProcessingState[runId] = false;
		}

	}
}