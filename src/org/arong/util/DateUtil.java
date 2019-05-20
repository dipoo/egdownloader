package org.arong.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class DateUtil {
	public final static String YYYY_MM_DD = "yyyy-MM-dd"; 
	public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public final static SimpleDateFormat YYYY_MM_DD_FORMAT = new SimpleDateFormat(YYYY_MM_DD);
	public final static SimpleDateFormat YYYY_MM_DD_HH_MM_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
	public final static SimpleDateFormat YYYY_MM_DD_HH_MM_SS_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
	/**
	 * 返回当前年份(年)
	 * @return String
	 */
	public static String getStringYear() {
	    return showDate("yyyy");
	}
	/**
	 * 返回当前日期(年月日形式)
	 * @return String
	 */
	public static String getStringToday() {
	    return showDate("yyyy-MM-dd");
	}
	/**
	 * 返回当前日期(年月日时分秒形式)
	 * @return String
	 */
	public static String getStringTime() {
	    return showDate("yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 根据参数pattern返回相应的日期格式
	 * @param pattern
	 * @return
	 */
	public static String showDate(String pattern){
	    SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	    String dateString = formatter.format(new Date());
	    return dateString;
	}
	/**
	 * 将日期形式的字符串转换为Date
	 * 
	 * @param s
	 * @return
	 */
	public static Date String2Date(String s) {
		Date date = null;
		Locale locale = Locale.CHINA;
		DateFormat format = null;
		if (Pattern.matches("\\d{4}.\\d{1,2}.\\d{1,2}", s)) {
			format = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		} else if (Pattern.matches("\\d{2}.\\d{2}.\\d{2}", s)) {
			format = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
		} else if (Pattern.matches(
				"\\d{4}.\\d{1,2}.\\d{1,2}[ ]\\d{1,2}.\\d{1,2}.\\d{1,2}", s)) {
			format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
					DateFormat.MEDIUM, locale);
		} else {
			format = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			date = format.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}