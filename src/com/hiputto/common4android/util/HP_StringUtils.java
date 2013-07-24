package com.hiputto.common4android.util;

public class HP_StringUtils {

	public static boolean isEmptyString(String string) {
		return (string.length() == 0 || string.equals(""));
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

	/**
	 * 字符串按字节截取
	 * 
	 * @param str
	 *            原字符
	 * @param len
	 *            截取长度
	 * @return String
	 */
	public static String splitString(String str, int len) {
		return splitString(str, len, "...");
	}

	/**
	 * 字符串按字节截取
	 * 
	 * @param str
	 *            原字符
	 * @param len
	 *            截取长度
	 * @param elide
	 *            省略符
	 * @return String
	 */

	public static String splitString(String str, int len, String elide) {
		if (str == null) {
			return "";
		}

		byte[] strByte = str.getBytes();
		int strLen = strByte.length;
		int elideLen = (elide.trim().length() == 0) ? 0
				: elide.getBytes().length;
		if (len >= strLen || len < 1) {
			return str;
		}
		if (len - elideLen > 0) {
			len = len - elideLen;
		}
		int count = 0;
		for (int i = 0; i < len; i++) {
			int value = (int) strByte[i];
			if (value < 0) {
				count++;
			}
		}
		if (count % 2 != 0) {
			len = (len == 1) ? len + 1 : len - 1;
		}
		return new String(strByte, 0, len) + elide.trim();
	}

}
