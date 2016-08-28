package com.picperweek.common4android.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.picperweek.common4android.config.Constants;

/**
 * 
 * @author xuyi05
 *
 */

public class StringUtil {

	public static boolean isEmptyString(String string) {
		return (string == null || string.length() == 0 || string.equals(""));
	}

	public static int getStringLength(String string) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < string.length(); i++) {
			String temp = string.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}

	public static String getStringByLength(String string, int length) {
		StringBuffer stringBuffer = new StringBuffer();

		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";

		for (int i = 0; i < string.length(); i++) {

			String temp = string.substring(i, i + 1);
			if (temp.matches(chinese)) {
				if (valueLength >= (length - 1))
					break;
				valueLength += 2;

			} else {
				if (valueLength >= length)
					break;
				valueLength += 1;

			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}

	public static String urlEncode(String str) throws UnsupportedEncodingException {
		if (str == null) {
			str = "";
		}
		return URLEncoder.encode(str, Constants.UTF8).replaceAll("\\+", "%20").replaceAll("%7E", "~").replaceAll("\\*",
				"%2A");
	}

	// MD5 加密
	public static String toMd5(String src) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(src.getBytes());
			return toHexString(algorithm.digest(), "");
		} catch (NoSuchAlgorithmException e) {
			LogUtil.e("Md5 encode failed!", e.getMessage());
			return "error";
		}
	}

	public static String toHexString(byte[] bytes, String separator) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			hexString.append(Integer.toHexString(0xFF & b)).append(separator);
		}
		return hexString.toString();
	}
}
