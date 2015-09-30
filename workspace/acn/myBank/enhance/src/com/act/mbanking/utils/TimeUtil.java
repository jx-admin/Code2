
package com.act.mbanking.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;
import android.util.Log;

public class TimeUtil {

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    /**
     * 15/04/2012
     */
    public static final String dateFormat1 = "dd/MM/yyyy";

    /**
     * 2012-11-15 10:00:00
     */
    public static final String dateFormat2 = "yyyy-MM-dd HH:mm:ss";

    // public static final String dateFormat3 = "yyyy-MM-dd";//
    /**
     * MM.dd.yyyy
     */
    public static final String dateFormat3 = "MM.dd.yyyy";//

    /**
     * yyyy-MM-dd
     */
    public static final String dateFormat4 = "yyyy-MM-dd";

    /**
     * MM.dd.yy
     */
    public static final String dateFormat5 = "MM.dd.yy";

    /**
     * 12 March 2013
     */
    public static final String dateFormat6 = "dd MMMM yyyy";

    public static final String detaFormat6Split = ".";

    /**
     * feb.2013
     */
    public static final String dateFormat7 = "MMM" + detaFormat6Split + "yyyy";

    // public static final String dateFormat7 = "MM.yyyy";

    /**
     * 
     */
    public static final String dateFormat8 = "MM-dd-yyyy";

    /**
     * dd.MM.yyyy
     */
    public static final String dateFormat9 = "dd.MM.yyyy";// /**

    /**
     * MMM.ddth
     */
    public static final String dateFormat10 = "MMM.dd'th'";//

    public static final String dateFormat11 = "dd";

    public static final String dateFormat12 = "MMM";
    
    public static final String dateFormat14="MMM\ndd";
    
    /**
     * 15/04/2012
     */
    public static final String dateFormat13 = "MM/dd/yyyy";

    public static String getDateString(long milliSeconds, String dateFormat) {
        DateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    
    public static String getDateString(Context context,long milliSeconds, String dateFormat) {
        DateFormat formatter = new SimpleDateFormat(dateFormat,context.getResources().getConfiguration().locale);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getNowTime() {
        DateFormat formatter = new SimpleDateFormat(dateFormat4, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        formatter.format(calendar.getTime());
        return formatter.format(calendar.getTime());
    }

    public static long nowTimeMillis() {
        return System.currentTimeMillis();
    }

    // public static boolean timeCompare(String time){
    // try {
    // Date nowdate=new Date();
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
    // Date d; d = sdf.parse(time);
    // boolean flag = d.before(nowdate);
    //
    // } catch (ParseException e) {
    // e.printStackTrace();
    // }
    // return
    // }

    public static long getMillis(String str) {
        long m = 0;
        try {
            Date date = new SimpleDateFormat(dateFormat2).parse(str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            m = cal.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }

    public static long getTimeByString(String time, String format) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.parse(time).getTime();
    }

    public static String changeFormattrString(String srcTime, String srcFormat, String targetFormat) {
        String result = "";
        try {
            long time = TimeUtil.getTimeByString(srcTime, srcFormat);

            result = TimeUtil.getDateString(time, targetFormat);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
     
    /**
     * @param monthAgo the value that how many month ago from now
     * @return time in millis 
     */
    public static long monthAgo(int monthAgo){
    	Date d = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.add(Calendar.MONTH, -monthAgo);
        gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), 1);
        return gc.getTimeInMillis();
    }

    public static String getMonthAgo(int monthAgo) {
    	String str=TimeUtil.getDateString(TimeUtil.monthAgo(monthAgo), TimeUtil.dateFormat2);
    	Log.d("Time","monthAgo "+str);
    	return str;
    }
}
