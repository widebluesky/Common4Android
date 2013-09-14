package com.hiputto.common4android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HP_RegexUtils {

	public static boolean isMobileNumber(String string) {
		String regExp = "^[1][3458][0-9]{9}$";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();// boolean
	}

	public static boolean isNumber(String string) {
		String regExp = "^\\d*$";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();// boolean
	}

	public static boolean isLetter(String string) {
		String regExp = "^[a-zA-Z]*$";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();// boolean
	}

	public static boolean isUpperCaseLetter(String string) {
		String regExp = "^[A-Z]*$";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();// boolean
	}

	public static boolean isLowerCaseLetter(String string) {
		String regExp = "^[a-z]*$";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();// boolean
	}

	public static boolean isHanZi(String string) {
		String regExp = "^[\u4e00-\u9fa5]*$";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();// boolean
	}

	public static boolean isURL(String string) {
		String regExp = "^[\\w]*://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();// boolean
	}

	public static boolean isIdCard(String string) {
		String regExp = "\\d{17}[[0-9],0-9xX]";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();// boolean
	}

}
