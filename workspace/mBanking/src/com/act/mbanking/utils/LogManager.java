
package com.act.mbanking.utils;

import android.util.Log;

/**
 * We use this class to print the log, by modifying the LogType when the final
 * release, you can log Close(code:LogType=HIDE_LOG), released a beta version
 * you can open the log(code:LogType=SHOW_LOG).
 * 
 * @author seekting.x.zhang
 */
public class LogManager {

    public static final boolean SHOW_LOG = true;

    public static final boolean HIDE_LOG = false;

    public static boolean LogType = SHOW_LOG;

    public static final String TAG = "mBanking";

    public static void v(String msg) {
        if (LogType) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (LogType) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (LogType) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (LogType) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (LogType) {
            Log.e(TAG, msg);
        }
    }

}
