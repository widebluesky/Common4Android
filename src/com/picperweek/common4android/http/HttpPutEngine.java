package com.picperweek.common4android.http;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPut;

import com.picperweek.common4android.http.command.BaseHttpRequest;

/**
 * 
 * @author widebluesky
 *
 */
public class HttpPutEngine extends HttpEngine {

	public HttpPutEngine(BaseHttpRequest request) {
		super(request);
	}

	@Override
	protected void initTag() {
		TAG = HttpPutEngine.class.getSimpleName();
	}

	@Override
	protected void initRequest() {
		requestBase = new HttpPut(baseRequest.getUrl());
	}

	@Override
	protected void setRequestParams() throws UnsupportedEncodingException {
	}

}
