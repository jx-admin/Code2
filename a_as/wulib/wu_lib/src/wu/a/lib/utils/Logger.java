package wu.a.lib.utils;

import java.util.List;

import android.util.Log;

/**
 * @author junxu.wang
 * @date : 2015年3月17日 下午4:08:12
 */
public class Logger {

    public static final boolean DEBUG = true;
    public static final String TAG = "Logger";

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void log(String log) {
        if (DEBUG) {
            Log.e(TAG, log);
        }
    }

    public static void log(String log, List ls) {
        if (DEBUG) {
            if (ls != null) {
                Log.d(TAG, log + " size=" + ls.size());
                for (Object obj : ls) {
                    Log.d(TAG, log + obj.toString());
                }
            } else {
                Log.d(TAG, log + ls);
            }
        }
    }
}
