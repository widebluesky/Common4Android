package com.picperweek.common4android.http.model;

import android.graphics.Bitmap;

public class ImageResult {
	public static final int STATUS_OK = 100;
	public static final int ERROR_NO_NET = 101;
	public static final int ERROR_NET_ACCESS = 102;
	public static final int ERROR_OOM = 103;
	public static final int ERROR_URL_NULL = 104;
	
	public static final int RES_OK = 1;
	public static final int RES_FAIL = -1;
	private int resultCode = -1;
	private Bitmap retBitmap = null;
	private String mImagePath;

	public Bitmap getRetBitmap() {
		return retBitmap;
	}

	public void setRetBitmap(Bitmap retBitmap) {
		this.retBitmap = retBitmap;
	}

	public void setResultOK() {
		resultCode = RES_OK;
	}

	public void setImagePath(String path){
		this.mImagePath = path;
	}
	
	public String getImagePath(){
		return this.mImagePath;
	}
	
	public boolean isResultFail() {
		return (resultCode == RES_FAIL);
	}
	
	public void setResultFail() {
		resultCode = RES_FAIL;
	}

	public boolean isResultOK() {
		return (resultCode == RES_OK);
	}

}
