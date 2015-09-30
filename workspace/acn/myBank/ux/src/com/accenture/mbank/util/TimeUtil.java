
package com.accenture.mbank.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;

public class TimeUtil {
    /**
     * March 12,2013
     */
    public static final String dateFormat9 = "MMMM dd,yyyy";
    
    /**
     * 
     */
    public static final String dateFormat8 = "MM-dd-yyyy";
    
    /**
     * 12 March 2013
     */
    public static final String dateFormat6 = "dd MMMM yyyy";

    /**
     * 15/04/2012
     */
    public static final String dateFormat1 = "MM/dd/yyyy";

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
     * 
     */
    public static final String dateFormat5 = "MM.dd.yy";
    
    public static final String dateFormat7 = "MM.yyyy";
    
    public static final String detaFormat6Split = ".";
    public static final String rotateTime="MM YYYY";

    /**
     * feb.2013
     */
    public static final String detaFormat6 = "MMM" + detaFormat6Split + "yyyy";

    public static String getDateString(long milliSeconds, String dateFormat) {
        DateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

    }
    public static String getDateString(Context context,long milliSeconds, String dateFormat) {
        DateFormat formatter = new SimpleDateFormat(dateFormat, context.getResources().getConfiguration().locale);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

    }

    public static long getTimeByString(String time, String format) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(format, Locale.US);

        return formatter.parse(time).getTime();

    }

    public static String dateToMMYYYY(String srcTime) {
        String value = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat2);
            int month = sdf.parse(srcTime).getMonth();
            int year = sdf.parse(srcTime).getYear();

            value = month + "." + year;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
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
    public static String changeFormattrString(Context context,String srcTime, String srcFormat, String targetFormat) {
        String result = "";
        try {
            long time = TimeUtil.getTimeByString(srcTime, srcFormat);

            result = TimeUtil.getDateString(context,time, targetFormat);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
}
