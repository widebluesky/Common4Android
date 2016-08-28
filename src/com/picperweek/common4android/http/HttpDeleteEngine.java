package com.picperweek.common4android.http;

import org.apache.http.client.methods.HttpDelete;

import com.picperweek.common4android.http.command.BaseHttpRequest;

/**
 * 
 * @author widebluesky
 *
 */
public class HttpDeleteEngine extends HttpEngine {

	public HttpDeleteEngine(BaseHttpRequest request) {
		super(request);
	}


	@Override
	protected void initTag() {
		TAG = HttpDeleteEngine.class.getSimpleName();
	}

	@Override
	protected void initRequest() {
		requestBase = new HttpDelete(baseRequest.getUrl());
	}

	@Override
	protected void setRequestParams() {
	}

}
