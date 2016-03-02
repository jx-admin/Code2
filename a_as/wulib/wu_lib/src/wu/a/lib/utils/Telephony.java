package wu.a.lib.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

public final class Telephony {
    private final static String TAG = "Telephony";
    private final static boolean DEBUG = true;

    private static final String HANGOUTS_PACKAGE = "com.google.android.talk";
    private static final String HANGOUTS_SMS_RECEIVER
            = "com.google.android.apps.babel.sms.SmsReceiver";

    /**
     * 判断手机是否是飞行模式
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressWarnings("deprecation")
    public static boolean isAirplaneModeOn(Context context){
        ContentResolver cr = context.getContentResolver();
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                ? Settings.Global.getInt(cr, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
                : Settings.System.getInt(cr, Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    /**
     * 设置手机飞行模式
     */
    @SuppressWarnings("deprecation")
    public static void setAirplaneMode(Context context, boolean on) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Settings.System.putInt(context.getContentResolver()
                    , Settings.System.AIRPLANE_MODE_ON, on ? 1 : 0)) {
                return;
            }
            // 设置写入失败时也与Jelly Bean MR1一样打开设置界面
        }

        QlUtils.startSettingsActivity(context, Settings.ACTION_AIRPLANE_MODE_SETTINGS);
    }

    /**
     * Get the unique device ID, for example,
     * the IMEI for GSM and the MEID or ESN for CDMA phones.
     *
     * Requires permission: READ_PHONE_STATE
     */
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String getSimId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String sim = tm.getSimSerialNumber();
            String subscriberId = tm.getSubscriberId();

            if (sim == null) {
                sim = "";
            }

            if (subscriberId == null) {
                subscriberId = "";
            }
            sim = sim + "_" + subscriberId;
            if (sim.length() > 61) {
                sim = sim.substring(0, 61);
            }
            return sim;
        } catch (Exception ignored) {
        }
        return "";
    }

    public static boolean isSimLocked(Context context) {
        int simState = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getSimState();
        boolean result = TelephonyManager.SIM_STATE_PIN_REQUIRED == simState
                || TelephonyManager.SIM_STATE_PUK_REQUIRED == simState
                || TelephonyManager.SIM_STATE_NETWORK_LOCKED == simState;
        if (DEBUG) {
            Trace.d(TAG, "Sim state: " + simState + ", locked? " + result);
        }
        return result;
    }

    /**
     * 判断正在运行的前台应用是否是电话应用
     *
     * @param context
     * @param index 如果是从服务中调用，index需要为0；参看CoreService
     *              如果是在Activity中调用，index可能需要为1，因为那时可能正处于应用切换时期。
     *              可参看LockscreenActivity.onNewIntent()
     * @return
     */
    public static boolean isPhoneShowing(Context context, int index) {
        return isPhonePackage(context, QlUtils.getRunningActivity(context, index));
    }

    public static boolean isPhonePackage(Context context, ComponentName name) {
        if (null == name) return false;

        String clsName = name.getClassName();
        String pkgName = name.getPackageName();
        boolean isPhonePackage = false;
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(pkgName, 0);
            if (DEBUG) {
                Trace.d(TAG, "Top package's sharedUserId: "
                        + pi.sharedUserId + " class: " + clsName);
            }
            if ("android.uid.phone".equals(pi.sharedUserId)
                    && !clsName.toLowerCase().contains("settings")
                    && !clsName.toLowerCase().contains("engmode")) {
                // 处于移动网络设置时或者工程模式时，不应认为当前是Phone
                // com.android.phone/com.android.phone.MobileNetworkSettings
                // com.android.phone/com.android.phone.MiuiMobileNetworkSettings
                isPhonePackage = true;
            }
        } catch (Throwable e) {
            Trace.w(TAG, "Could not check whether Phone is running foreground", e);
        }
        return isPhonePackage;
    }

    /**
     * 获取手机基准号
     */
    public static int getCellLocation(Context context){
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        // 返回值MCC + MNC
        String operator = tm.getNetworkOperator();
        try {
            if (!TextUtils.isEmpty(operator)) {
                int mnc = Integer.parseInt(operator.substring(3));
                if (2 == mnc) {
                    // 中国电信获取LAC、CID的方式
                    CdmaCellLocation location = (CdmaCellLocation) tm.getCellLocation();
                    return location.getBaseStationId();
                } else {
                    // 中国移动和中国联通获取LAC、CID的方式
                    GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
                    return location.getCid();
                }
            }
        } catch (Exception e) {
            Trace.w(TAG, "Could not get cell location", e);
        }
        return -1;
    }

    public static Intent getSmsAppLaunchIntent(Context context) {
        String smsPackageName = getSmsAppPackage(context);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        if (null != smsPackageName) {
            intent.setPackage(smsPackageName);
        } else {
            intent.setType("vnd.android-dir/mms-sms");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * @return package name of the default SMS app; can be null if Hangouts is not configured
     * to be the SMS app on pre Android KitKat devices
     */
    @SuppressLint("NewApi")
	public static String getSmsAppPackage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return android.provider.Telephony.Sms.getDefaultSmsPackage(context);
        } else if (isHangoutsSmsEnabled(context)) {
            return HANGOUTS_PACKAGE;
        }
        return null;
    }

    /**
     * Check if Sms is enabled in Hangouts
     *
     * @return true if Sms is turned on in Hangouts, otherwise return false
     */
    public static boolean isHangoutsSmsEnabled(Context context) {
        try {
            return PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    == context.getPackageManager().getComponentEnabledSetting(
                    new ComponentName(HANGOUTS_PACKAGE, HANGOUTS_SMS_RECEIVER));
        } catch (Exception ignored) {
        }
        return false;
    }

    public static String getSafeSmsAppPackage(Context context) {
        String packageName = getSmsAppPackage(context);
        return null == packageName ? "com.android.mms" : packageName;
    }

}
