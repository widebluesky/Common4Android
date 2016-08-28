package com.picperweek.common4android.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date getDateFromDateString(String dateString, String formatString) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
		Date date = simpleDateFormat.parse(dateString);// 提取格式中的日期
		return date;
	}

	public static String getDateStringFromDate(Date date, String formatString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
		String dateString = simpleDateFormat.format(date); // 改变格式

		return dateString;
	}

	public static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };

	public static final String[] constellationArr = { "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
			"天蝎座", "射手座", "魔羯座" };

	public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };

	/**
	 * 根据日期获取生肖
	 * 
	 * @return
	 */
	public static String date2Zodica(Calendar time) {
		return zodiacArr[time.get(Calendar.YEAR) % 12];
	}

	/**
	 * 根据日期获取星座
	 * 
	 * @param time
	 * @return
	 */
	public static String date2Constellation(Calendar time) {
		int month = time.get(Calendar.MONTH);
		int day = time.get(Calendar.DAY_OF_MONTH);
		if (day < constellationEdgeDay[month]) {
			month = month - 1;
		}
		if (month >= 0) {
			return constellationArr[month];
		}
		// default to return 魔羯
		return constellationArr[11];
	}

	public static Date dateFromCalendar(Calendar calendar) {
		return calendar.getTime();
	}

	public static Calendar date2Calendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static int date2Age(Date date) {
		Calendar mycalendar = Calendar.getInstance();// 获取现在时间
		int year = mycalendar.get(Calendar.YEAR);// 获取年份
		return year - date.getYear() - 1900;
	}

	public static long javaTime2PhpTime(long time) {
		String javaTime = String.valueOf(time);
		String phpTime = javaTime.substring(0, javaTime.length() - 3);
		return Long.valueOf(phpTime);
	}

	public static long phpTime2JavaTime(long time) {
		return time * 1000;
	}
}
