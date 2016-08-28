package com.picperweek.common4android.http.command;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.picperweek.common4android.api.HttpTag;
import com.picperweek.common4android.config.Constants;
import com.picperweek.common4android.util.LogUtil;

/**
 * Http数据分发类
 * 
 * @author widebluesky
 * 
 */
public class HttpTagDispatch {
	
	private static final String TAG = HttpTagDispatch.class.getSimpleName();
	
	public static Object dispatch(HttpDataRequest request, String json) throws Exception {
		Object result = json;
		HttpTag tag = request.getTag();
		switch (tag.getTagType()) {
		case Constants.TAG_TYPE_STRING:
			result = json;
			break;
		case Constants.TAG_TYPE_GSON:
			result = new Gson().fromJson(json, tag.getClass());
			break;
		case Constants.TAG_TYPE_JSON_OBJECT:
			result = new JSONObject(json);
			break;
		case Constants.TAG_TYPE_JSON_ARRAY:
			result = new JSONArray(json);
		default:
			result = json;
			break;
		}
		LogUtil.i(TAG, "ResponseData [tag=" + request.getTag() + ", json=" + json + "]");
		return result;
	}
}
