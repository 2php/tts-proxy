package com.tinet.ttssc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 日期Util类。
 * 
 * 转换日期时，如果不指定日期格式，则使用默认的日期格式。<br>
 * 默认的日期格式可以在文件messages_zh_CN.properties中通过属性date.default_format来指定。不指定的情况下默认使用
 * yyyy-MM-dd作为日期格式，日期时间格式默认使用yyyy-MM-dd HH:mm:ss
 *<p>
 * 文件名： DateUtil.java
 *<p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD.  All rights reserved.
 * @author 周营昭
 * @since 1.0
 * @version 1.0
 */
public class DateUtil {
	
	/** 格式yyyy-MM-dd */
	public static final String FMT_DATE_YYYY_MM_DD = "yyyy-MM-dd";

	/** 格式yyyy-MM-dd HH:mm:ss */
	public static final String FMT_DATE_YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	
	/** 格式yyyyMMddHHmmss */
	public static final String FMT_DATE_YYYYMMDDHHmmss = "yyyyMMddHHmmss";
	/** 格式HH:mm:ss */
	public static final String FMT_DATE_HH_mm_ss = "HH:mm:ss";
	
	/** 格式HH:mm */
	public static final String FMT_DATE_HH_mm = "HH:mm";
	
	/** 格式yyyy */
	public static final String FMT_DATE_YYYY = "yyyy";

	/** 格式yyyy-MM */
	public static final String FMT_DATE_YYYY_MM = "yyyy-MM";

	/** 格式MM */
	public static final String FMT_DATE_MM = "MM";

	/** 格式DD */
	public static final String FMT_DATE_DD = "DD";
	
	public static final String FMT_DATE_dd = "dd";
	
	private static String defaultDatePattern = "yyyy-MM-dd";

	public static final String FMT_DATE_MM_DD_HH_mm = "MM-dd HH:mm";
	
	static {
		// 尝试试从messages_zh_Cn.properties中获取defaultDatePattner.
	}

	/**
	 * 获得默认的 date pattern
	 */
	public static String getDatePattern() {
		return defaultDatePattern;
	}

	public static String getDateTimePattern() {
		return DateUtil.getDatePattern() + " HH:mm:ss";
	}

	/**
	 * 返回预设Format的当前应用服务器运行的日期字符串。
	 */
	public static String getToday() {
		Date today = new Date();
		return format(today);
	}
	
	/**
	 * 返回Format的当前应用服务器运行的日期时间字符串。
	 */
	public static String getTodayTime() {
		Date today = new Date();
		return DateUtil.format(today, DateUtil.getDateTimePattern());
	}

	/**
	 * 使用默认Format格式化Date成字符串。
	 */
	public static String format(Date date) {
		return date == null ? "" : format(date, getDatePattern());
	}

	/**
	 * 使用参数Format格式化Date成字符串。如果参数date为null，则返回空字符串""。
	 * 
	 * @param date 待转换日期。
	 * @param pattern 指定的转换格式。
	 */
	public static String format(Date date, String pattern) {
		return date == null ? "" : new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 将秒数格式化为指定的字符串，如  1350456532  转化为  2012-10-17 14：48:52
	 * @param time
	 * @param pattern1
	 * @return
	 */
	public static String format(String time,String pattern1){
		Date date = new Date(Long.parseLong(time)*1000);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern1);
		String sDateTime=sdf.format(date);
		return sDateTime;
		
	}
	/**
	 * 使用预设格式将字符串解析为Date。
	 */
	public static Date parse(String strDate) throws ParseException {
		return StringUtil.isEmpty(strDate) ? null : parse(strDate,
				getDatePattern());
	}

	/**
	 * 使用指定格式Format将字符串解析为Date。如果参数strDate为null，则返回null。
	 * 
	 * @param strDate 待解析日期字符串。
	 * @param pattern 指定的转换格式。
	 */
	public static Date parse(String strDate, String pattern)
			throws ParseException {
		return StringUtil.isEmpty(strDate) ? null : new SimpleDateFormat(
				pattern).parse(strDate);
	}

	/**
	 * 在日期上增加数个整月。
	 * 
	 * @param date 待操作日期。
	 * @param n 要增加的月数。
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	/**
	 * 在日期上增加数个整天。
	 * 
	 * @param date 待操作日期。
	 * @param n 要增加的天数。
	 */
	public static Date addDay(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, n);
		return cal.getTime();
	}

	/**
	 * 在日期上增加数个小时。
	 * 
	 * @param date 待操作日期。
	 * @param n 要增加的小时数。
	 */
	public static Date addHour(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, n);
		return cal.getTime();
	}
	
	/**
	 * 在日期上增加数个分钟。
	 * 
	 * @param date 待操作日期。
	 * @param n 要增加的分钟数。
	 */
	public static Date addMinute(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, n);
		return cal.getTime();
	}
	
	/**
	 * 在日期上增加数个秒。
	 * 
	 * @param date 待操作日期。
	 * @param n 要增加的秒数。
	 */
	public static Date addSecond(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, n);
		return cal.getTime();
	}
	
	/**
	 * 两个Date之间秒数
	 * 
	 * @param date1 日期1。
	 * @param date2 日期2。
	 */
	public static int diffSecond(Date date1, Date date2) {
		return (int)((date2.getTime() - date1.getTime())/1000);
	}
	
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	private static String timePattern = "HH:mm";

	// ~ Methods
	// ================================================================

	/**
	 * 将一个日期转换成默认的日期格式。
	 * 
	 * @param aDate 待转换日期。
	 * @return 格式化后的日期。
	 */
	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return returnValue;
	}

	/**
	 * 将一个字符串按照指定的格式转换成日期。
	 * 
	 * @param aMask 格式。
	 * @param strDate 转换的日期字符串。
	 * @return 转换后的日期对象。
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		if (logger.isDebugEnabled()) {
			logger.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
		}

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * 使用默认的时间格式(HH:mm)转换字符串。
	 * 
	 * @param theTime 待转换的日期对象。
	 * @return 格式化后的字符串。
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(timePattern, theTime);
	}

	/**
	 * 将指定日期按照指定格式转换成字符串。
	 * 
	 * @param aMask 指定的格式。
	 * @param aDate 待转换日期对象。
	 * @return 格式化的日期字符串。
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			logger.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 按照默认的日期格式将指定日期对象转换成字符串。
	 * 
	 * @param aDate 待转换的日期。
	 * @return 转换后的字符串。
	 */
	public static final String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	/**
	 * 使用DateUtil默认的格式来转换日期字符串为日期对象。
	 * 
	 * @param strDate 日期字符串表示。
	 * @return 转换后的日期对象。
	 * 
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate)
			throws ParseException {
		Date aDate = null;

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("converting date with pattern: " + getDatePattern());
			}

			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException pe) {
			logger.error("Could not convert '" + strDate
					+ "' to a date, throwing exception", pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return aDate;
	}
	/**
	 * 日期格式化方法。
	 * 
	 * @param date 要格式化的日期。
	 * @param nFmt 格式化样式，使用<code>DateUtil.FMT_DATE_YYYY_MM_DD</code>等来指定。
	 * @see DateUtil#FMT_DATE_YYYY_MM_DD
	 * @return
	 */
	public static String formatDate(Date date, String nFmt) {
		SimpleDateFormat fmtDate = new SimpleDateFormat(nFmt);
		return fmtDate.format(date);
	}

	/**
	 * 获取当前应用服务器日期时间的字符串表示，格式由{@link DateUtil#FMT_DATE_YYYY_MM_DD_HH_mm_ss}来指定。
	 */
	public static String getCurrentDateTime() {
		return DateUtil.formatDate(new Date(), DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);
	}

	/**
	 * 获取当前应用服务器时间的字符串表示，格式由{@link DateUtil#FMT_DATE_HH_mm_ss}来指定。
	 */
	public static String getCurrentTime() {
		return DateUtil.formatDate(new Date(), DateUtil.FMT_DATE_HH_mm_ss);
	}

	/**
	 * 获取当前应用服务器日期的字符串表示，格式由{@link DateUtil#FMT_DATE_YYYYMMDD}来指定。
	 */
	public static String getCurrentDate() {
		return DateUtil.formatDate(new Date(), DateUtil.FMT_DATE_YYYY_MM_DD);
	}

	/**
	 * 获取当前应用服务器日期的字符串表示，格式由{@link DateUtil#FMT_DATE_YYYYMM}来指定。
	 */
	public static String getCurrentYYYYMM() {
		return DateUtil.formatDate(new Date(), DateUtil.FMT_DATE_YYYY_MM);
	}
	
	/**
	 * 获取当前应用服务器日期的字符串表示，格式由{@link DateUtil#FMT_DATE_MM}来指定。
	 */
	public static String getCurrentMM() {
		return DateUtil.formatDate(new Date(), DateUtil.FMT_DATE_MM);
	}
	
	/**
	 * 获取当前应用服务器日期的字符串表示，格式由{@link DateUtil#FMT_DATE_DD}来指定。
	 */
	public static String getCurrentDD() {
		return DateUtil.formatDate(new Date(), DateUtil.FMT_DATE_DD);
	}
	
	/**
	 * 获取当前应用服务器日期的字符串表示，格式由{@link DateUtil#FMT_DATE_YYYY}来指定。
	 */
	public static String getCurrentYYYY() {
		return DateUtil.formatDate(new Date(), DateUtil.FMT_DATE_YYYY);
	}

	/**
	 * 将一个日期时间的字符串表示从inFormat格式转换为outFormat格式。
	 * 
	 * @param dStr 日期时间的字符串。
	 * @param inFormat 原始格式。
	 * @param outFormat 转换后的格式。
	 * @return
	 */
	public static String convert(String dStr, String inFormat,String outFormat) {

		SimpleDateFormat sdf = new SimpleDateFormat(inFormat);
		Date d = null;
		try {
			d = sdf.parse(dStr);
		} catch (ParseException pe) {
			System.out.println(pe.getMessage());
		}
		
		return dateToString(d,outFormat);

	}
	/**
	 * 按给出的格式将输入的日期转换为字符串。
	 * 
	 * @param currdate
	 *            输入的date对象。
	 * @param strFormat
	 *            约定的格式。
	 * @return 按输入时间及约定格式返回的字符串
	 */
	public static final String dateToString(java.util.Date currdate,
			String strFormat) {
		String returnDate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
			if (currdate == null)
				return returnDate;
			else
				returnDate = sdf.format(currdate);
		} catch (NullPointerException e) {
		}
		return returnDate;
	}

	/**
	 * 判读一个字符串是否是日期格式。
	 * <p>
	 * 判断的规则是：
	 * <li>如果长度为4，判断是否是yyyy格式。 </li>
	 * <li>如果长度为6，判断是否是yyyyMM格式。 </li>
	 * <li>如果长度为8，判断是否是yyyyMMdd格式。 </li>
	 * <li>如果长度为10，判断是否是yyyyMMddHH格式。 </li>
	 * <li>如果长度为12，判断是否是yyyyMMddHHmm格式。 </li>
	 * 
	 * @param currdate 输入的时间
	 * @param strFormat 约字的格式
	 * @return 按输入时间及约定格式返回的字符串
	 */
	public static final boolean checkIsDate(String strDate) {
		Date returnDate = null;
		try {
			if (strDate.length() == 4) {
				returnDate = DateUtil.parse(strDate, "yyyy");
			} else if (strDate.length() == 6) {
				returnDate = DateUtil.parse(strDate, "yyyyMM");
			} else if (strDate.length() == 8) {
				returnDate = DateUtil.parse(strDate, "yyyyMMdd");
			} else if (strDate.length() == 10) {
				returnDate = DateUtil.parse(strDate, "yyyyMMddHH");
			} else if (strDate.length() == 12) {
				returnDate = DateUtil.parse(strDate, "yyyyMMddHHmm");
			}
		} catch (Exception e) {
			return false;
		}
		if (returnDate != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取某周周几的日期字符串
	 * @param anyDayOfWeek 一周中任意某天的日期
	 * @param dayOfWeek 一周中的周几，取值为1,2,...,7,分别对应周一,周二,...,周日。
	 * @return 周几那天的日期字符串，格式是yyyy-MM-dd。
	 */
	public static String getDayInWeek(Date anyDayOfWeek,int dayOfWeek) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(anyDayOfWeek);
		
		// 将星期一设为一周的开始
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		
		// 在Calendar类中默认周日为一周的开始，对应值为1，周一对应值为2，以此类推周六对应值为7。
		if (dayOfWeek<7) {
			cal.set(Calendar.DAY_OF_WEEK, dayOfWeek+1);
		} else if(dayOfWeek==7){
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		}else{
			return "";
		}
		String dateStr = format(cal.getTime(), FMT_DATE_YYYY_MM_DD);
		return dateStr;  
	}
	
	/**
	 * 获得某月的第一天或最后一天的日期字符串。
	 * @param anyDayOfMonth 某月中的任意一天。
	 * @param flag flag=true，返回某月中的第一天，否则返回最后一天。
	 * @return 返回某月的第一天或最后一天的日期字符串。
	 */
	public static String getDayInMonth(Date anyDayOfMonth, boolean flag){
		String dateStr = "";
		Calendar cal=Calendar.getInstance();
		cal.setTime(anyDayOfMonth);
		if (flag) {
			dateStr = format(cal.getTime(), "yyyy-MM")+"-01";
		} else {
			cal.add(Calendar.MONTH, 1);
			try {
				Date firstDayOfNextMonth = convertStringToDate(format(cal.getTime(), "yyyy-MM")+"-01");
				dateStr = convertDateToString(addDay(firstDayOfNextMonth, -1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dateStr;
	}
	
	/**
	 * 获取某日在年中属于第几周。
	 * @param dateStr 日期，格式：yyyy-MM-dd。
	 * @return 返回一年的第几周。
	 */
	public static int getWeekOfYear(String dateStr){
		Calendar calendar = Calendar.getInstance();
		try {
			Date date = parse(dateStr);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setMinimalDaysInFirstWeek(3);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * 根据给定的秒数时长返回"hh:mm:ss"格式的时长。
	 * @param seconds 秒数时长。
	 * @return 返回格式为"hh:mm:ss"的时长。
	 */
	public static String getDuration(Integer seconds){
		Integer min = seconds/60;
		Integer sec = seconds%60;
		Integer hour = new Integer(0);
		String minStr = new String();
		String secStr =new String();
		String hourStr = new String();
		String timeStr = new String();
		secStr = sec.toString();
		
		if(sec<10){
			secStr = "0"+secStr;
		}
		if(min<60){
			if(min<10){
				minStr = "0";
			}
			minStr += min.toString();
			timeStr = "00:"+minStr+":"+secStr;
		
		} else {
			Integer day = 0;
			String dayStr = "";
			hour = min/60;
			day = hour/24;
			min = min%60;
			if(min<10){
				minStr = "0";
			}
			minStr += min.toString();
			if (day<24) {
				if(hour<10){
					hourStr = "0";
				}
				hourStr += hour.toString();
				timeStr = hourStr+":"+minStr+":"+secStr;
			} else {
				hour = hour%24;
				if(hour<10){
					hourStr = "0";
				}
				hourStr += hour.toString();
				if (day==1) {
					dayStr = day+" day";
				} else {
					dayStr = day+" days";
				}
				timeStr = dayStr+" "+hourStr+":"+minStr+":"+secStr;
			}
			
		}
		return timeStr;
	}
	
	/**
	 * 根据已知毫秒数返回日期时间串。
	 * @param milliseconds 毫秒数
	 * @return 返回格式为"yyyy-MM-dd hh:mm:ss"日期时间串。
	 */
	public static Date getDateFromMils(long milliseconds){
		Calendar cc = Calendar.getInstance();
		cc.setTimeInMillis(milliseconds);
		return cc.getTime();
	}
}
