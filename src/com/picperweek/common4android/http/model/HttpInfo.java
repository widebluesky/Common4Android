package com.picperweek.common4android.http.model;

import com.picperweek.common4android.http.command.HttpDataRequest;
import com.picperweek.common4android.http.command.HttpDataResponse;

/**
 * Http网络信息Model
 * @author widebluesky
 *
 */
public class HttpInfo {
	private HttpDataRequest request;
	private HttpDataResponse response;
	
	/**
	 * 用于记录该请求开始的时间，超过五分钟的请求，一律从队列中消除
	 */
	private long startTime;
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public HttpDataRequest getRequest() {
		return request;
	}

	public void setRequest(HttpDataRequest request) {
		this.request = request;
	}

	public HttpDataResponse getResponse() {
		return response;
	}

	public void setResponse(HttpDataResponse response) {
		this.response = response;
	}

	public HttpInfo(HttpDataRequest request, HttpDataResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof HttpInfo) {
			HttpInfo key = (HttpInfo) other;
			return (key.request.getUrl().equals(this.request.getUrl()));
		}
		return false;
	}
}
