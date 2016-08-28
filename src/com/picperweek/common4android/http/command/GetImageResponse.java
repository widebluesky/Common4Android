package com.picperweek.common4android.http.command;

import com.picperweek.common4android.http.model.ImageType;

import android.graphics.Bitmap;

/**
 * 
 * @author widebluesky
 *
 */
public interface GetImageResponse {
	void onImageRecvOK(ImageType imageType,Object tag, Bitmap bm, String path);

	void onImageRecvError(ImageType imageType, Object tag, int retCode);
}
