package com.hiputto.common4android.util;


public class HP_StringUtils {

	public static boolean isEmptyString(String string) {
		return (string==null||string.length() == 0 || string.equals(""));
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


}
