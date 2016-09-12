package com.picperweek.common4android.http.command;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.util.ByteArrayBuffer;

import com.picperweek.common4android.util.FileUtil;
import com.picperweek.common4android.util.LogUtil;
import com.picperweek.common4android.util.StringUtil;


/**
 * 
 * Post方式网络请求
 * 
 * 用的时候把post参数用setBodyParams方法设置到bodyParams里，把读取的图片set到image里，没有图片时不用管image
 * picKey是服务器要求的传图片时的参数key，请根据实际情况改动
 * 
 * @author widebluesky
 * 
 */
public class HttpPostRequest extends HttpDataRequest {
	private static final String TAG = HttpPostRequest.class.getSimpleName();
	private static String BR = "\r\n";
	public static String BOUNDARY = "----37531613912423";
	private static String DEVIDER = "--";
	private static String CONTENT_DISPOSITION_PRE = BR + "Content-Disposition: form-data; name=\"";
	private static String CONTENT_DISPOSITION_SUF = "\"" + BR + BR;
	private String picKey = "pic";
	String pic = DEVIDER + BOUNDARY + CONTENT_DISPOSITION_PRE + picKey + "\"; filename=\"postpic.jpg\"\r\nContent-Type: image/jpeg\r\n\r\n";
	String endData = (BR + DEVIDER + BOUNDARY + DEVIDER + BR);

	private Map<String, String> bodyParams;
	private Map<String, File> fileParams;
	
	public void setBodyParams(Map<String, String> bodyParams) {
		this.bodyParams = bodyParams;
	}

	public Map<String, String> getBodyParams() {
		return bodyParams;
	}
	
	public Map<String, File> getFileParams() {
		return fileParams;
	}

	public void setFileParams(Map<String, File> fileParams) {
		this.fileParams = fileParams;
	}

	/**
	 * 向bodyParams添加一个数据
	 * 
	 * @param key
	 * @param value
	 */
	public void addBodyParams(String key, String value) {
		if (this.bodyParams == null) {
			this.bodyParams = new HashMap<String, String>();
		}
		this.bodyParams.put(key, value);
	}

	/**
	 * 向fileParams添加一个数据
	 * 
	 * @param key
	 * @param value
	 */
	public void addFileParams(String key, File file) {
		if (this.fileParams == null) {
			this.fileParams = new HashMap<String, File>();
		}
		this.fileParams.put(key, file);
	}
	
	public byte[] getBytes() {
		ByteArrayBuffer bytes = new ByteArrayBuffer(0);
		byte[] keyValueBytes = map2KeyValueBytes(bodyParams);
		bytes.append(keyValueBytes, 0, keyValueBytes.length);
		
		if (fileParams != null) {
			Set<String> keySet = fileParams.keySet();
			Iterator<String> it = keySet.iterator();
			int i = 0;
			while (it.hasNext()) {
				String key = it.next();
				File file = fileParams.get(key);
				String fileHeadLine = getFileHeadLine(key, file.getName());
				String fileEndLine = getFileEndLine((i == (keySet.size() - 1)) ? true : false);
				byte[] fileHeadBytes = fileHeadLine.getBytes();
				byte[] fileBytes = FileUtil.getBytes(file.getAbsolutePath());
				byte[] endBytes = fileEndLine.getBytes();
				bytes.append(fileHeadBytes, 0, fileHeadBytes.length);
				bytes.append(fileBytes, 0, fileBytes.length);
				bytes.append(endBytes, 0, endBytes.length);
				i ++ ;
			}
		}
		return bytes.toByteArray();
	}

	/**
	 * 获得文件头
	 * @param key
	 * @param fileName
	 * @return
	 */
	private String getFileHeadLine(String key, String fileName) {
		String fileLine = DEVIDER + BOUNDARY + CONTENT_DISPOSITION_PRE + key + "\"; filename=\"" + fileName + "\"\r\nContent-Type: image/jpeg\r\n\r\n";
		return fileLine;
	}
	
	/**
	 * 获得文件结尾
	 * @param hasDevider
	 * @return
	 */
	private String getFileEndLine(boolean hasDevider) {
		return (BR + DEVIDER + BOUNDARY + (hasDevider ? DEVIDER : "") + BR);
		
	}

	private byte[] map2KeyValueBytes(Map<String, String> map) {
		if (map == null) {
			return "".getBytes();
		}
		StringBuffer str = new StringBuffer("");
		Set<String> keySet = map.keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			str.append(CONTENT_DISPOSITION_PRE + key + CONTENT_DISPOSITION_SUF);
			try {
				str.append(StringUtil.urlEncode(value) + BR);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				LogUtil.e(TAG, e.getMessage());
			}
		}
		return str.toString().getBytes();
	}

	public String getString() {
		return map2KeyValueString(bodyParams);
	}

	private String map2KeyValueString(Map<String, String> map) {
		if (map == null) {
			return "";
		}
		StringBuffer str = new StringBuffer("");
		Set<String> keySet = map.keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			try {
				value = StringUtil.urlEncode(value);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				LogUtil.e(TAG, e.getMessage());
			}
			str.append(key + "=" + value + "&");
		}
		if (str.length() > 0) {
			str = str.deleteCharAt(str.length() - 1);
		}
		return str.toString();
	}
	
}
