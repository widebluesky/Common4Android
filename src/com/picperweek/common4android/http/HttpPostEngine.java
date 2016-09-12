package com.picperweek.common4android.http;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;

import com.picperweek.common4android.http.command.BaseHttpRequest;
import com.picperweek.common4android.http.command.HttpPostRequest;

/**
 * 网络请求方法Post
 * @author widebluesky
 *
 */
public class HttpPostEngine extends HttpEngine {

	public HttpPostEngine(BaseHttpRequest request) {
		super(request);
	}

	@Override
	protected void initTag() {
		TAG = HttpPostEngine.class.getSimpleName();
	}

	@Override
	protected void initRequest() {
		requestBase = new HttpPost(baseRequest.getUrl());
	}

	@Override
	protected void setRequestParams() throws UnsupportedEncodingException {
		HttpPostRequest postRequest = (HttpPostRequest) baseRequest;
		String entityString = postRequest.getString();
		AbstractHttpEntity entity = new StringEntity(entityString);
		entity.setContentType("application/x-www-form-urlencoded");
    	((HttpPost) requestBase).setEntity(entity); 
	}
}
