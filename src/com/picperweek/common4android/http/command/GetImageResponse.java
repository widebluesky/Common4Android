package com.picperweek.common4android.http.command;

import com.picperweek.common4android.http.model.ImageType;

import android.graphics.Bitmap;

/**
 * 
 * 图片下载响应接口
 * @author widebluesky
 *
 */
public interface GetImageResponse {
	void onImageRecvOK(ImageType imageType,Object tag, Bitmap bm, String path);

	void onImageRecvError(ImageType imageType, Object tag, int retCode);
}
