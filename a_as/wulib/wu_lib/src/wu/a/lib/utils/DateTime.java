package wu.a.lib.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateTime {
    private static final Object LOCK = new Object();

    private static SimpleDateFormat sTimeFormat12;
    private static SimpleDateFormat sTimeFormat24;

    /**
     * @see #formatDate(java.util.Date)
     */
    public static String formatDate(long millis) {
        return formatDate(new Date(millis));
    }

    public static String formatDate(long millis, int style) {
        return formatDateTime(new Date(millis), style);
    }

    public static String formatDate(long millis, Locale locale) {
        return formatDateTime(new Date(millis), locale);
    }

    /**
     * 2014年8月12日
     */
    public static String formatDate(Date date) {
        return formatDate(date, Locale.getDefault());
    }

    public static String formatDate(Date date, int style) {
        return formatDate(date, style, Locale.getDefault());
    }

    public static String formatDate(Date date, Locale locale) {
        return formatDate(date, DateFormat.DEFAULT, locale);
    }

    public static String formatDate(Date date, int style, Locale locale) {
        return formatDateTime(DateFormat.getDateInstance(style, locale), date);
    }

    /**
     * @see #formatTime(java.util.Date)
     */
    public static String formatTime(long millis) {
        return formatTime(new Date(millis));
    }

    public static String formatTime(long millis, int style) {
        return formatDateTime(new Date(millis), style);
    }

    public static String formatTime(long millis, Locale locale) {
        return formatDateTime(new Date(millis), locale);
    }

    /**
     * PM 12:48:54
     */
    public static String formatTime(Date date) {
        return formatTime(date, Locale.getDefault());
    }

    public static String formatTime(Date date, int style) {
        return formatTime(date, style, Locale.getDefault());
    }

    public static String formatTime(Date date, Locale locale) {
        return formatTime(date, DateFormat.DEFAULT, locale);
    }

    public static String formatTime(Date date, int style, Locale locale) {
        return formatDateTime(DateFormat.getTimeInstance(style, locale), date);
    }

    /**
     * @see #formatDateTime(java.util.Date)
     */
    public static String formatDateTime(long millis) {
        return formatDateTime(millis, Locale.getDefault());
    }

    public static String formatDateTime(long millis, int style) {
        return formatDateTime(new Date(millis), style);
    }

    public static String formatDateTime(long millis, Locale locale) {
        return formatDateTime(new Date(millis), locale);
    }

    /**
     * 2014年8月12日 PM 12:48:54
     */
    public static String formatDateTime(Date date) {
        return formatDateTime(date, DateFormat.DEFAULT);
    }

    public static String formatDateTime(Date date, int style) {
        return formatDateTime(date, style, Locale.getDefault());
    }

    public static String formatDateTime(Date date, Locale locale) {
        return formatDateTime(date, DateFormat.DEFAULT, locale);
    }

    public static String formatDateTime(Date date, int style, Locale locale) {
        return formatDateTime(DateFormat.getDateTimeInstance(style, style, locale), date);
    }

    /**
     * @see #formatDateTime(java.text.DateFormat, java.util.Date)
     */
    public static String formatDateTime(String format, long millis) {
        return formatDateTime(format, millis, Locale.getDefault());
    }

    public static String formatDateTime(String format, long millis, Locale locale) {
        return formatDateTime(new SimpleDateFormat(format, locale), new Date(millis));
    }

    /**
     * @see #formatDateTime(java.text.DateFormat, java.util.Date)
     */
    public static String formatDateTime(DateFormat format, long millis) {
        return formatDateTime(format, new Date(millis));
    }

    /**
     * 以指定的格式格式给定的日期时间
     */
    public static String formatDateTime(DateFormat format, Date date) {
        return format.format(date);
    }

    /**
     * 08-12 12:48:54.789
     */
    public static String formatDateTimeForLogcat(long millis) {
        return formatDateTime("MM-dd HH:mm:ss:SSS", millis);
    }

    public static SimpleDateFormat getTimeFormat(Context context) {
        return getTimeFormat(context, Locale.getDefault());
    }

    /**
     * 获取格式化时间为“小时:分钟”的DateFormat；12小时制时，AM/PM显示在分钟后面，中间以一个空格分开
     */
    public static SimpleDateFormat getTimeFormat(Context context, Locale locale) {
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            synchronized (LOCK) {
                if (null == sTimeFormat24) {
                    sTimeFormat24 = new SimpleDateFormat("HH:mm", locale);
                }
            }
            if (null != sTimeFormat12) {
                sTimeFormat12 = null;
            }
            return sTimeFormat24;
        } else {
            synchronized (LOCK) {
                if (null == sTimeFormat12) {
                    sTimeFormat12 = new SimpleDateFormat("hh:mm a", locale);
                }
            }
            if (null != sTimeFormat24) {
                sTimeFormat24 = null;
            }
            return sTimeFormat12;
        }
    }

    /**
     * @see #getDayOfWeekFormat(int, Locale)
     */
    public static SimpleDateFormat getDayOfWeekFormat() {
        return getDayOfWeekFormat(DateFormat.DEFAULT);
    }

    /**
     * @see #getDayOfWeekFormat(int, Locale)
     */
    public static SimpleDateFormat getDayOfWeekFormat(int style) {
        return getDayOfWeekFormat(style, Locale.getDefault());
    }

    public static SimpleDateFormat getDayOfWeekFormat(Locale locale) {
        return getDayOfWeekFormat(DateFormat.DEFAULT, locale);
    }

    /**
     * 获取星期几的format
     *
     * @param style Default is #DateFormat.MEDIUM
     *              #DateFormat.MEDIUM: Tue
     *              #DateFormat.LONG:   Tuesday
     *              #DateFormat.SHORT:  T
     */
    public static SimpleDateFormat getDayOfWeekFormat(int style, Locale locale) {
        String pattern;
        switch (style) {
            case DateFormat.SHORT:
                pattern = "EEEEE";
                break;
            case DateFormat.LONG:
                pattern = "EEEE";
                break;
            case DateFormat.MEDIUM:
            default:
                pattern = "E";
                break;
        }
        return new SimpleDateFormat(pattern, locale);
    }

    public static SimpleDateFormat getMonthFormat() {
        return getMonthFormat(DateFormat.DEFAULT, Locale.getDefault());
    }

    public static SimpleDateFormat getMonthFormat(int style) {
        return getMonthFormat(style, Locale.getDefault());
    }

    public static SimpleDateFormat getMonthFormat(Locale locale) {
        return getMonthFormat(DateFormat.DEFAULT, Locale.getDefault());
    }

    public static SimpleDateFormat getMonthFormat(int style, Locale locale) {
        String pattern;
        switch (style) {
            case DateFormat.SHORT:
                pattern = "LLLLL";
                break;
            case DateFormat.LONG:
                pattern = "LLLL";
                break;
            case DateFormat.MEDIUM:
            default:
                pattern = "LLL";
                break;
        }
        return new SimpleDateFormat(pattern, locale);
    }

    public static SimpleDateFormat getYearFormat() {
        return getYearFormat(DateFormat.DEFAULT);
    }

    public static SimpleDateFormat getYearFormat(int style) {
        return new SimpleDateFormat(DateFormat.SHORT == style ? "yy" : "yyyy");
    }
}
