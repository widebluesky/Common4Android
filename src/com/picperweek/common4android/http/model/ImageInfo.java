package com.picperweek.common4android.http.model;

import com.picperweek.common4android.http.command.GetImageRequest;
import com.picperweek.common4android.http.command.GetImageResponse;


public class ImageInfo {
	private ImageType imageType;
	private GetImageRequest request;
	private GetImageResponse response;

	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

	public GetImageRequest getRequest() {
		return request;
	}

	public void setRequest(GetImageRequest request) {
		this.request = request;
	}

	public GetImageResponse getResponse() {
		return response;
	}

	public void setResponse(GetImageResponse response) {
		this.response = response;
	}

	public ImageInfo(ImageType imageType, GetImageRequest request, GetImageResponse response) {
		this.imageType = imageType;
		this.request = request;
		this.response = response;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ImageInfo) {
			ImageInfo key = (ImageInfo) other;
			return (key.request.getUrl().equals(this.request.getUrl()));
		}
		return false;
	}
}
