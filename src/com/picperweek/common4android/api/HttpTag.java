package com.picperweek.common4android.api;

import com.picperweek.common4android.config.Constants;

public enum HttpTag {

	TEST(Constants.TAG_TYPE_STRING, 1, "http://www.baidu.com", null);

	/**
	 * HttpTag
	 * @param tagType 标签类型 1:string, 2:gson, 3:jsonObject, 4:jsonArray
	 * @param httpTag Tag标签，用于接收响应数据
	 * @param httpUrl 请求Url
	 * @param parseClass GSON类型使用，用于转换GSON数据
	 */
	HttpTag(int tagType, int httpTag, String httpUrl, Class<?> parseClass) {
		this.mTagType = tagType;
		this.mHttpTag = httpTag;
		this.mHttpUrl = httpUrl;
		this.mParseClass = parseClass;
	}

	private final int mTagType;
	private final int mHttpTag;
	private final String mHttpUrl;
	private final Class<?> mParseClass;
	
	
	public int getTagType() {
		return mTagType;
	}

	public int getHttpTag() {
		return mHttpTag;
	}

	public String getHttpUrl() {
		return mHttpUrl;
	}

	public Class<?> getParseClass() {
		return mParseClass;
	}

	@Override
	public String toString() {
		String toString = "[tag=" + mHttpTag + "][url=" + mHttpUrl + "][parseClass=" + mParseClass + "]";
		return toString;
	}

}
