package com.accenture.mbank.util;

import it.gruppobper.ams.android.bper.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;

public class TimeUtil {
	static Locale mLocale=Locale.ITALIAN;
	/**
	 * March 12,2013
	 */
	private static final String dateFormat9 = "MMMM dd,yyyy";

	/**
     * 
     */
	private static final String dateFormat8 = "MM-dd-yyyy";

	/**
	 * 12 March 2013
	 */
	private static final String dateFormat6 = "dd MMMM yyyy";

	/**
	 * 15/04/2012
	 */
	private static final String dateFormat1 = "MM/dd/yyyy";

	/*
	 * "15/03/2014 00:00:00"
	 */
	public static final String dateFormat2a= "dd/MM/yyyy HH:mm:ss";
	/**
	 * 2012-11-15 10:00:00
	 */
	public static final String dateFormat2 = "yyyy-MM-dd HH:mm:ss";

	private static final String dateFormat11 = "yyyy-MM-dd";
	/**
	 * MM.dd.yyyy
	 */
	private static final String dateFormat3 = "MM.dd.yyyy";//

	/**
	 * yyyy-MM-dd
	 */
	private static final String dateFormat4 = "yyyy-MM-dd";

	/**
     * 
     */
	public static final String dateFormat5 = "dd.MM.yy";

	private static final String dateFormat10 = "MM.dd.yy";

	private static final String dateFormat7 = "MM.yyyy";

	public static final String detaFormat6Split = ".";
	private static final String rotateTime = "MM YYYY";
	public static final String dateFormat_mm_yy = "MM.yy";

	/**
	 * feb.2013
	 */
	public static final String detaFormat6 = "MMM" + detaFormat6Split + "yyyy";
	/**
	 * March 2013
	 */
	public static final String detaFormat7 = "MMMM yyyy";

	public static String dateFomateLocale(long milliSeconds, String dateFormat) {
		DateFormat formatter = new SimpleDateFormat(dateFormat,mLocale);
		return formatter.format(milliSeconds);
	}

	public static String getDateString(long milliSeconds, String dateFormat) {
		DateFormat formatter = new SimpleDateFormat(dateFormat, mLocale);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());

	}

	public static long getTimeByString(String time, String format){
		try {
			DateFormat formatter = new SimpleDateFormat(format, Locale.US);
			
			return formatter.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String changeFormattrString(String srcTime, String srcFormat, String targetFormat) {
		String result = "";
		long time = TimeUtil.getTimeByString(srcTime, srcFormat);
		
		result = TimeUtil.getDateString(time, targetFormat);
		
		return result;
	}

	public static String changeChartFormattrString(Context context, String srcTime, String srcFormat, String targetFormat) {
		String result = "";
		long time = TimeUtil.getTimeByString(srcTime, srcFormat);
		
		result = TimeUtil.getChartDateString(context, time, targetFormat);
		
		return result;
	}

	private static String getChartDateString(Context context,
			long milliSeconds, String dateFormat) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		int month = calendar.get(Calendar.MONTH);
		String[] stringMonth = context.getResources().getStringArray(
				R.array.chart_month_string);
		return stringMonth[month] + detaFormat6Split
				+ calendar.get(Calendar.YEAR);
	}
}
