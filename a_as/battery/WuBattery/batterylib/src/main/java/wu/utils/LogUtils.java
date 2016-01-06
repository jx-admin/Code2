package wu.utils;


import android.util.Log;

import wu.battery.BuildConfig;

/**
 * Created by jx on 2015/12/23.
 */
public class LogUtils {
    private static final String TAG = "battery_hola";

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }
}
