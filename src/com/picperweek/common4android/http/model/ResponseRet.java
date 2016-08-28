package com.picperweek.common4android.http.model;


public class ResponseRet {
	
	public static final int RET_OK = 0;
	public static final int RET_FAIL = -1;
	
	public static final int STATUS_OK = 100;
	public static final int COOKIE_INVALID = 102; // Cookie失效

	int retcode;
	
	String retmsg;
	
	int status;

	public int getRetcode() {
		return retcode;
	}

	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
