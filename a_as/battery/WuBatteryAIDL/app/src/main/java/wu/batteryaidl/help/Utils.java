package wu.batteryaidl.help;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by lj on 15-12-23.
 */
public class Utils {

    private static final String TAG = "▇(＞﹏＜)▇!!!";

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void wakeLockScreen(Context context, int timeout) {
        Log.e("@@@", "wakelock acquire");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "wk" + System.currentTimeMillis());
        wakeLock.acquire();
        wakeLock.setReferenceCounted(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("@@@", "wakelock release");
                if (wakeLock != null && wakeLock.isHeld()) {
                    wakeLock.release();
                }
            }
        }, timeout);
    }

    public static void unlockScreen(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        kl.disableKeyguard();
    }

    public static void unlockAndWakeLockScreen(Context context, int timeout) {
        Utils.wakeLockScreen(context, timeout);
        Utils.unlockScreen(context);
    }

    public static Bitmap getIconByPackageName(Context context, Intent intent) {
        try {
            List<ResolveInfo> packages = context.getPackageManager().queryIntentActivities(intent, 0);
            Drawable dr;
            for (ResolveInfo ri : packages) {
                dr = ri.activityInfo.loadIcon(context.getPackageManager());
                if (dr != null) {
                    return ((BitmapDrawable) dr).getBitmap();
                }
            }
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
        return null;
    }

    public static final Intent getMSMIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        return intent;
    }

    public static final Intent getCallIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL_BUTTON);
        return intent;
    }
}
