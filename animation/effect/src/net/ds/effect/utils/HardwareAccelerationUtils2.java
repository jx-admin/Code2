package net.ds.effect.utils;

import net.ds.effect.BuildConfig;
import android.os.Build;
import android.util.Log;
import android.view.View;

public class HardwareAccelerationUtils2 {

    private static final String TAG = "Launcher.HardwareAccelerationUtils2";

    private static final boolean LOGD_ENABLED = BuildConfig.DEBUG;

    /**
     * 从 Utils.java搬过来
     */
    private static Boolean sHardwareAccelerated = null;

    public static void enableHardwareAccelerated() {
        sHardwareAccelerated = true;
    }

    /**
     * 从 Utils.java搬过来
     * 判断view是否启用了硬件加速
     * @param view
     * @return
     */
    public static boolean isHardwareAccelerated(View view) {
        if (Build.VERSION.SDK_INT < 14) {
            return false;
        }
        if (sHardwareAccelerated == null) {
            try {
                sHardwareAccelerated = (Boolean) CommonUtils.forceInvoke( view, "isHardwareAccelerated", null, null);
                if (LOGD_ENABLED) {
                    Log.d(TAG, "isHardwareAccelerated: " + sHardwareAccelerated);
                }
            } catch (Exception e) {
                if (LOGD_ENABLED) {
                    Log.e(TAG, "Failed to invoke the isHardwareAccelerated method.", e);
                }
                sHardwareAccelerated = false;
            }
        }
        return sHardwareAccelerated;
    }

}
