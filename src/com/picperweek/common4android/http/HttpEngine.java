package com.picperweek.common4android.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;

import com.picperweek.common4android.base.BaseApplication;
import com.picperweek.common4android.http.command.BaseHttpRequest;
import com.picperweek.common4android.http.model.HttpResult;
import com.picperweek.common4android.util.ApnUtil;
import com.picperweek.common4android.util.FileUtil;
import com.picperweek.common4android.util.LogUtil;

import android.content.Context;
import android.text.TextUtils;

/**
 * 
 * @author widebluesky
 *
 */
public abstract class HttpEngine {

	/**
	 * 图片和数据请求返回的状态码 服务器返回错误、JSON错误都以ERROR_NET_ACCESS返回，通过msg区分
	 */
	public enum HttpCode {
		/** 网络访问成功 */
		STATUS_OK(1),
		/** 当前无网络 */
		ERROR_NO_CONNECT(2),
		/** 需要用户登录 */
		ERROR_NO_REGISTER(3),
		/** 网络连接失败 */
		ERROR_NET_ACCESS(4),
		/** 网络连接网络超时 */
		ERROR_NET_TIMEOUT(5),
		/** 请求被取消 */
		USER_CANCELLED(6), 
		SYSTEM_CANCELLED(7),
		ERROR_SERVICE_ACCESS(8);

		HttpCode(int ni) {
			this.nativeInt = ni;
		}

		final int nativeInt;
	}

	private static final int RETRY_ONCE = 1;
	private static final int RETRY_MORE = 2;
	protected BaseHttpRequest baseRequest;
	protected HttpRequestBase requestBase;

	/** unit by milliseconds */
	private static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 30000;
	/** unit by milliseconds */
	private static final int DEFAULT_HTTP_SO_TIMEOUT = 30000;
	/** unit by milliseconds */
	private static final long DEFAULT_HTTP_CONNMGR_TIMEOUT = 30000L;

	/** 每次读取数据数目 */
	protected final static int MAX_READ = 4096;

	/** BUFFER大小 */
	protected final static int BUFFER_SIZE = 1024;

	protected String TAG = HttpEngine.class.getSimpleName();

	public HttpEngine(BaseHttpRequest request) {
		this.baseRequest = request;
		initTag();
	}

	protected HttpResult doRequest() {
		HttpResult httpResult = new HttpResult();
		HttpCode code;
		
		InputStream entityStream = null;
		HttpClient httpClient = null;
		try {
			initRequest();

			if (baseRequest.isGzip()) {
				requestBase.setHeader("Accept-Encoding", "gzip,deflate");
			}
			/**
			 * 添加头信息
			 */
			if (baseRequest.getHeadParams() != null) {
				Iterator<String> iterator = baseRequest.getHeadParams().keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String value = baseRequest.getHeadParams().get(key);
					requestBase.addHeader(key, value);
				}
			}
			setRequestParams();
			
			httpClient = getHttpClient();
			
			HttpResponse httpResponse = httpClient.execute(requestBase);
			
			int responseCode = httpResponse.getStatusLine().getStatusCode();

			LogUtil.e("HTTP response code ==>", Integer.toString(responseCode));

			if (responseCode != HttpStatus.SC_OK) {
				code = HttpCode.ERROR_SERVICE_ACCESS;
				httpResult.setResultCode(code);
				return httpResult;
			} else {
				code = HttpCode.STATUS_OK;
				httpResult.setResultCode(code);
			}

			byte[] data = new byte[MAX_READ];
			ByteArrayBuffer byteBuf = new ByteArrayBuffer(MAX_READ);
			entityStream = httpResponse.getEntity().getContent();
			while (!baseRequest.isCancelled()) {
				int bytesRead = entityStream.read(data);

				if (bytesRead == -1) {
					break;
				}
				byteBuf.append(data, 0, bytesRead);
			}
			
			Header[] headers = httpResponse.getAllHeaders();
			baseRequest.setGzip(false);
			Map<String, String> headParams = new HashMap<String, String>();
			for (int i = 0; i < headers.length; i++) {
				Header header = headers[i];
				if (header.getName().equals("Content-Encoding") && header.getValue().equals("gzip")) {
					baseRequest.setGzip(true);
				}
				headParams.put(header.getName(), header.getValue());
			}
			headParams.put("COOKIE-CUSTOM", getCookie(httpClient));
			httpResult.setHeadParams(headParams);
			
			if (baseRequest.isGzip()) {
				byte[] retData = FileUtil.gzipDecoder(byteBuf.toByteArray());
				httpResult.setData(retData);
			} else {
				byte[] retData = byteBuf.toByteArray();
				httpResult.setData(retData);
			}
		} catch (SocketTimeoutException e) {
			code = HttpCode.ERROR_NET_TIMEOUT;
			httpResult.setResultCode(code);
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
			code = HttpCode.ERROR_NET_ACCESS;
			httpResult.setResultCode(code);
			LogUtil.e(TAG, e.getMessage());
		} catch (OutOfMemoryError e) {
			code = HttpCode.ERROR_NET_ACCESS;
			httpResult.setResultCode(code);
			LogUtil.e(TAG, e.getMessage());
		} finally {
			if (entityStream != null) {
				try {
					entityStream.close();
				} catch (IOException e) {
					code = HttpCode.ERROR_NET_ACCESS;
					httpResult.setResultCode(code);
					LogUtil.e(TAG, e.getMessage());
				}
			}
			if (httpClient != null) {
				ClientConnectionManager ccm = httpClient.getConnectionManager();
				if (ccm != null) {
					ccm.closeExpiredConnections();
				}
			}
		}

		return httpResult;
	}
	
	private String getCookie(HttpClient httpClient) { 
		List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies(); 
		StringBuffer sb = new StringBuffer(); 
		for (int i = 0; i < cookies.size(); i++) { 
			Cookie cookie = cookies.get(i); 
			String cookieName = cookie.getName(); 
			String cookieValue = cookie.getValue(); 
			if (!TextUtils.isEmpty(cookieName) && !TextUtils.isEmpty(cookieValue)) { 
				sb.append(cookieName + "="); 
				sb.append(cookieValue+";"); 
			} 
		} 
		return sb.toString();
	} 

	public HttpResult execute() {
		int retryTime = RETRY_ONCE;
		/**
		 * 修改重试次数
		 */
		if (baseRequest.isRetry()) {
			retryTime = RETRY_MORE;
		}
		HttpResult result = new HttpResult();
		/**
		 * 检查，准备并整理所有请求的参数
		 */
		HttpCode code = baseRequest.prepareRequest();
		if (code != HttpCode.STATUS_OK) {
			result.setResultCode(code);
			return result;
		}
		while (retryTime > 0) {
			result = doRequest();
			if (result != null && result.getResultCode() == HttpCode.STATUS_OK) {
				break;
			}
			--retryTime;
		}
		return result;
	}

	protected HttpClient getHttpClient() {
		HttpClient client = null;
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		HttpParams localHttpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(localHttpParams, DEFAULT_HTTP_CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(localHttpParams, DEFAULT_HTTP_SO_TIMEOUT);
		ConnManagerParams.setTimeout(localHttpParams, DEFAULT_HTTP_CONNMGR_TIMEOUT);
		
		ThreadSafeClientConnManager tsccm = new ThreadSafeClientConnManager(localHttpParams, schemeRegistry);
		client = new DefaultHttpClient(tsccm, localHttpParams);
		
		Context context = BaseApplication.getInstance().getApplicationContext();
		HttpHost httpHost = ApnUtil.getHttpHost(context);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
		return client;
	}

	protected abstract void initTag();
	protected abstract void initRequest();
	protected abstract void setRequestParams() throws UnsupportedEncodingException;

}
