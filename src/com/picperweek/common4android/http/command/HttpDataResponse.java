package com.picperweek.common4android.http.command;

import com.picperweek.common4android.api.HttpTag;
import com.picperweek.common4android.http.HttpEngine.HttpCode;

/**
 * 
 * @author widebluesky
 *
 */
public interface HttpDataResponse {

	/**
	 * 网络请求完成
	 * @param tag
	 * @param extraInfo
	 * @param result
	 */
	void onHttpRecvOK(HttpTag tag, Object extraInfo, Object result);

	/**
	 * 网络请求异常
	 * @param tag
	 * @param retCode
	 * @param msg
	 */
	void onHttpRecvError(HttpTag tag, HttpCode retCode, String msg);

	/**
	 * 网络请求退出
	 * @param tag
	 */
	void onHttpRecvCancelled(HttpTag tag);
	
	
}
