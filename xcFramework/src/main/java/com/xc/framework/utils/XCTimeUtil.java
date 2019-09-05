package com.xc.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author ZhangXuanChen
 * @date 2015-11-20
 * @package com.xc.framework.utils
 * @description 时间工具类
 */
public class XCTimeUtil {
	/** 日期格式（yyyy-MM-dd） */
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	/** 日期格式（HH:mm:ss） */
	public static final String FORMAT_TIME = "HH:mm:ss";
	/** 日期格式（yyyy-MM-dd HH:mm:ss） */
	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式（yyyy-MM-dd E） */
	public static final String FORMAT_DATE_WEEK = "yyyy-MM-dd E";
	/** 日期格式（yyyy-MM-dd E HH:mm） */
	public static final String FORMAT_DATE_WEEK_TIME = "yyyy-MM-dd E HH:mm";

	/**
	 * 获取当前时间
	 * 
	 * @param dateFormat
	 *            时间格式
	 * @return 时间格式字符串
	 */
	public static String getCurrentTime(String dateFormat) {
		return getTime(null, dateFormat);
	}

	/**
	 * 获取时间
	 * 
	 * @param milliseconds
	 *            时间戳字符串
	 * @param dateFormat
	 *            时间格式
	 * @return 时间格式字符串
	 */
	public static String getTime(String milliseconds, String dateFormat) {
		if (!XCStringUtil.isEmpty(milliseconds)) {
			return getTime(Long.parseLong(milliseconds), dateFormat);
		}
		return "";
	}
	/**
	 * 获取时间
	 * 
	 * @param milliseconds
	 *            时间戳
	 * @param dateFormat
	 *            时间格式
	 * @return 时间格式字符串
	 */
	public static String getTime(long milliseconds, String dateFormat) {
		try {
			if (milliseconds <= 0) {
				milliseconds = System.currentTimeMillis();
			}
			if (XCStringUtil.isEmpty(dateFormat)) {
				dateFormat = FORMAT_DATE_TIME;
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.CHINESE);
			return simpleDateFormat.format(new Date(milliseconds));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取当前星期
	 * 
	 * @return 例:星期一
	 */
	public static String getCurrentWeek() {
		return getWeek(getCurrentTime(null));
	}

	/**
	 * 获取星期
	 * 
	 * @param date
	 *            日期格式yyyy-MM-dd
	 * @return 例:星期一
	 */
	public static String getWeek(String date) {
		String week = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE, Locale.CHINESE);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(format.parse(date));
			int weekKey = calendar.get(Calendar.DAY_OF_WEEK);
			switch (weekKey) {
			case 1:
				week = "星期天";
				break;
			case 2:
				week = "星期一";
				break;
			case 3:
				week = "星期二";
				break;
			case 4:
				week = "星期三";
				break;
			case 5:
				week = "星期四";
				break;
			case 6:
				week = "星期五";
				break;
			case 7:
				week = "星期六";
				break;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return week;
	}
}
