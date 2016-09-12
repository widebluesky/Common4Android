package com.picperweek.common4android.http.model;

import java.util.Map;

import com.picperweek.common4android.http.HttpEngine.HttpCode;

/**
 * 网络响应结果Model
 * @author widebluesky
 *
 */
public class HttpResult {

	public static final int RES_OK = 1; 
	
	/**
	 * 结果码
	 */
	private HttpCode resultCode;
	
	/**
	 * 结果数据
	 */
	private byte[] data;
	
	/**
	 * 结果header\cookie等
	 */
	private Map<String, String> headParams = null;
	
	public HttpCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(HttpCode resultCode) {
		this.resultCode = resultCode;
	}


	public byte[] getData() {
		return data;
	}

	
	public void setData(byte[] data) {
		this.data = data;
	}

	public Map<String, String> getHeadParams() {
		return headParams;
	}

	public void setHeadParams(Map<String, String> headParams) {
		this.headParams = headParams;
	}
	
}
