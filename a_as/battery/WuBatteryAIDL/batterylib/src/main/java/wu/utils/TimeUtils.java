package wu.utils;

/**
 * Created by Administrator on 2015/12/22.
 */
public class TimeUtils {

    public static String formatTimeDuration(long duration) {
        // 毫秒
        long ssec = duration % 1000;
        // 秒
        long sec = (duration / 1000) % 60;
        // 分钟
        long min = (duration / 1000 / 60) % 60;
        // 小时
        long hour = (duration / 1000 / 60 / 60) % 24;
        // 天
        long day = duration / 1000 / 60 / 60 / 24;

        if (day > 0) {
            return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
        }

        if (hour > 0) {
            return hour + "小时" + min + "分钟" + sec + "秒";
        }

        if (min > 0) {
            return min + "分钟" + sec + "秒";
        }

        return sec + "秒";
    }
}
