package com.picperweek.common4android.http;

import org.apache.http.client.methods.HttpGet;

import com.picperweek.common4android.http.command.BaseHttpRequest;

/**
 * 网络请求方法Get
 * @author widebluesky
 *
 */
public class HttpGetEngine extends HttpEngine {

	public HttpGetEngine(BaseHttpRequest request) {
		super(request);
	}

	@Override
	protected void initTag() {
		TAG = HttpGetEngine.class.getSimpleName();
	}

	@Override
	protected void initRequest() {
		requestBase = new HttpGet(baseRequest.getUrl());
	}

	@Override
	protected void setRequestParams() {
	}

}
