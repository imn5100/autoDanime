/**  
*****************************************************************************
* Copyright(C)  MORETRUST Co., Ltd. 2012                                        
*****************************************************************************
* 产品名称：MT弱联盟商业平台  
* 版本信息：1.0  
* 日期：2012-8-30  
*/
package com.shaw.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtils {
	public static final String HOUR_FORMAT = "HH:mm";
	public static final String DAY_FORMAT = "MM/dd";

	/**
	 * 获得毫秒级的时间，通常用于时间戳，格式为 yyyyMMddHHmmssSSS
	 */
	public static String getMSTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return dateFormat.format(new Date());
	}

	/**
	 * 获得秒级的时间
	 */
	public static String getSSTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(new Date());
	}

	public static String getFormatTimeByFormat(Date time, String format) {
		if (time != null && StringUtils.isNotBlank(format)) {
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(time);
		}
		return null;
	}

	/**
	 * 将Long型的时间转为 yyyy-MM-dd HH:mm:ss的格式
	 */
	public static String getFormatTime(Long l) {
		if ((l == null) || (l.equals(0L))) {
			return "";
		}
		Date date = new Date(l);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 将Long型的时间转为 yyyy-MM-dd的格式
	 */
	public static String getFormatDay(Long l) {
		if ((l == null) || (l.equals(0L))) {
			return "";
		}
		Date date = new Date(l);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 将Long型的时间转为 yyyy-MM-dd HH:mm的格式
	 */
	public static String getFormatTimeYMDHM(Long l) {
		if ((l == null) || (l.equals(0L))) {
			return "";
		}
		Date date = new Date(l);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;

	}

	public static Date getDate(Long l) {

		DateFormat parser = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date;
		try {
			date = parser.parse(l.toString());
		} catch (ParseException e) {
			throw new IllegalArgumentException("时间参数：'" + l + "'不符合 yyyyMMddHHmmss的格式！");
		}

		return date;
	}

	/**
	 * 
	 * @Description: 将Date转换为long @param date 待转换的日期 @return long
	 *               转换的long类型 @throws
	 */
	public static long Date2Long(Date date, int len) {
		SimpleDateFormat sdf = null;
		if (len == 14) {
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		} else if (len == 17) {
			sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		} else if (len == 6) {
			sdf = new SimpleDateFormat("HHmmss");
		}
		return Long.valueOf(sdf.format(date));
	}

	/**
	 * @Title: getNowTime @Description: 获取今天的时间 @param @return 设定文件 @return
	 *         String 返回类型 @throws
	 */
	public static String getTime(int i) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, i);

		return sdf.format(cal.getTime());
	}

	/**
	 * @功能介绍：获取当天的时间 @创建时间：2011-8-12 yaojianjun
	 * @param dt
	 * @return
	 */
	public static String getDayTime(Date dt) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String days = dft.format(dt);
		return days;
	}

	/**
	 * @功能介绍：获取当天的时间 格式 yyyy/MM/dd HH:mm:ss @创建时间：2011-8-12 yaojianjun
	 * @param dt
	 * @return
	 */
	public static String getDayTimeFormat(Date dt) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String days = dft.format(dt);
		return days;
	}

	/**
	 * 获取本月第一天
	 * 
	 * @return
	 */
	public static Date getFirstDayOfMonth() {
		Calendar firstDate = Calendar.getInstance();
		firstDate.set(Calendar.DATE, 1);// 设为当前月的1号
		firstDate.set(Calendar.HOUR_OF_DAY, 0);
		firstDate.set(Calendar.MINUTE, 0);
		firstDate.set(Calendar.SECOND, 0);
		return firstDate.getTime();
	}

	public static Date getFirstDayOfMonth(Date date) {
		Calendar firstDate = Calendar.getInstance();
		firstDate.setTime(date);
		firstDate.set(Calendar.DATE, 1);// 设为当前月的1号
		firstDate.set(Calendar.HOUR_OF_DAY, 0);
		firstDate.set(Calendar.MINUTE, 0);
		firstDate.set(Calendar.SECOND, 0);
		firstDate.set(Calendar.MILLISECOND, 0);
		return firstDate.getTime();
	}

	/**
	 * 获取本月最后一天
	 * 
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(date);
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.roll(Calendar.DATE, -1);
		lastDate.set(Calendar.HOUR_OF_DAY, 23);
		lastDate.set(Calendar.MINUTE, 59);
		lastDate.set(Calendar.SECOND, 59);
		lastDate.set(Calendar.MILLISECOND, 0);
		return lastDate.getTime();
	}

	public static Date getLastDayOfMonth() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.roll(Calendar.DATE, -1);
		lastDate.set(Calendar.HOUR_OF_DAY, 23);
		lastDate.set(Calendar.MINUTE, 59);
		lastDate.set(Calendar.SECOND, 59);
		return lastDate.getTime();
	}

	/**
	 * 获取本年度第一天
	 * 
	 * @return
	 */
	public static Date getFirstDayOfYear() {
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		return cd.getTime();
	}

	public static Date getFirstDayOfYear(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		return cd.getTime();
	}

	/**
	 * 获取本年度最后一天
	 * 
	 * @return
	 */
	public static Date getLastDayOfYear() {
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		cd.set(Calendar.HOUR_OF_DAY, 23);
		cd.set(Calendar.MINUTE, 59);
		cd.set(Calendar.SECOND, 59);
		return cd.getTime();
	}

	public static Date getLastDayOfYear(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		cd.set(Calendar.HOUR_OF_DAY, 23);
		cd.set(Calendar.MINUTE, 59);
		cd.set(Calendar.SECOND, 59);
		return cd.getTime();
	}

	/**
	 * 获取当天的起始时间
	 * 
	 * @return
	 */
	public static Date getStartTimeOfDay() {
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		return cd.getTime();
	}

	public static Date getStartTimeOfDay(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		return cd.getTime();
	}

	/**
	 * 获取当天的结束时间
	 * 
	 * @return
	 */
	public static Date getEndTimeOfDay() {
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.HOUR_OF_DAY, 23);
		cd.set(Calendar.MINUTE, 59);
		cd.set(Calendar.SECOND, 59);
		cd.set(Calendar.MILLISECOND, 0);
		return cd.getTime();
	}

	public static Date getEndTimeOfDay(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.set(Calendar.HOUR_OF_DAY, 23);
		cd.set(Calendar.MINUTE, 59);
		cd.set(Calendar.SECOND, 59);
		cd.set(Calendar.MILLISECOND, 0);
		return cd.getTime();
	}

	/**
	 * 取得偏移给定日期指定年数的日期
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addYears(Date date, int amount) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(Calendar.YEAR, amount);
		return cd.getTime();
	}

	public static Date addYears(int amount) {
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.YEAR, amount);
		return cd.getTime();
	}

	/**
	 * 取得偏移给定日期指定月数的日期
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addMonths(Date date, int amount) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(Calendar.MONTH, amount);
		return cd.getTime();
	}

	public static Date addMonths(int amount) {
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, amount);
		return cd.getTime();
	}

	/**
	 * 取得偏移给定日期指定天数的日期
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addDays(Date date, int amount) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(Calendar.DATE, amount);
		return cd.getTime();
	}

	/**
	 * 取得偏移当前时间指定天数的日期
	 * 
	 * @param amount
	 * @return
	 */
	public static Date addDays(int amount) {
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, amount);
		return cd.getTime();
	}

	/**
	 * 取得偏移给定日期指定小时的日期
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addHours(Date date, int amount) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(Calendar.HOUR, amount);
		return cd.getTime();
	}

	/**
	 * 取得偏移当前时间指定小时的日期
	 * 
	 * @param amount
	 * @return
	 */
	public static Date addHours(int amount) {
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.HOUR, amount);
		return cd.getTime();
	}

	/**
	 * 取得偏移给定日期指定分钟的日期
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addMinute(Date date, int amount) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		cd.add(Calendar.MINUTE, amount);
		return cd.getTime();
	}

	/**
	 * 取得偏移当前时间指定分钟的日期
	 * 
	 * @param amount
	 * @return
	 */
	public static Date addMinute(int amount) {
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MINUTE, amount);
		return cd.getTime();
	}

	/**
	 * 计算给定日期所在月份总共有多少天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int daysOfMonth(int year, int month) {
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.YEAR, year);
		cd.set(Calendar.MONDAY, month - 1);
		return cd.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int daysOfMonth() {
		Calendar cd = Calendar.getInstance();
		return cd.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/***
	 * <两个时间相减得到小时> <功能详细描述>
	 * 
	 * @param
	 * @author Administrator
	 * @version 1.0, 2013-1-11
	 * @return long [返回类型说明]
	 * @exception throws
	 *                [违例类型] [违例说明]
	 */
	public static long cutDate(Date bigDate, Date smallDate) {
		// SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// sf.parse(bigDate);
		long l = bigDate.getTime() - smallDate.getTime();
		// long day=l/(24*60*60*1000);
		long hour = (l / (60 * 60 * 1000));
		return hour;
	}

	/***
	 * <两个时间相减得到分钟> <功能详细描述>
	 * 
	 * @param
	 * @author Administrator
	 * @version 1.0, 2013-1-11
	 * @return long [返回类型说明]
	 * @exception throws
	 *                [违例类型] [违例说明]
	 */
	public static long cutDateMinute(long bigDate, long smallDate) {
		// SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// sf.parse(bigDate);
		long l = bigDate - smallDate;
		// long day=l/(24*60*60*1000);
		long minute = (l / (60 * 1000));
		return minute;
	}

	/***
	 * <两个时间相减得到秒数> <功能详细描述>
	 * 
	 * @param
	 * @author Administrator
	 * @version 1.0, 2013-1-11
	 * @return long [返回类型说明]
	 * @exception throws
	 *                [违例类型] [违例说明]
	 */
	public static long cutDateSecond(long bigDate, long smallDate) {
		// SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// sf.parse(bigDate);
		long l = bigDate - smallDate;
		// long day=l/(24*60*60*1000);
		long minute = (l / (1000));
		return minute;
	}

	public static String getTime(Date dt) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		return dft.format(dt);
	}

	public static String getTime(Date date, String formatPattern) {
		SimpleDateFormat dft = new SimpleDateFormat(formatPattern);
		return dft.format(date);
	}

	public static Date formatDay(String day) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			ParsePosition pos = new ParsePosition(0);
			Date result = formatter.parse(day, pos);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date formatDate(String date, String formatPattern) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
			Date result = formatter.parse(date);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 季节
	 * 
	 * @param season
	 * @return
	 */
	public static String getSeasonName(Byte season) {
		String seasonName = null;
		switch (season) {
		case 1:
			seasonName = "春季";
			break;
		case 2:
			seasonName = "夏季";
			break;
		case 3:
			seasonName = "秋季";
			break;
		case 4:
			seasonName = "冬季";
			break;
		}
		return seasonName;
	}
}