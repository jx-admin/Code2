package wu.a.lib.device;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import wu.a.lib.utils.NetUtils;
import wu.a.lib.utils.QlUtils;
import wu.a.lib.utils.ReflectionUtils;
import wu.a.lib.utils.Telephony;

public final class DeviceUtils {
    private final static String TAG = "DeviceUtils";
    private final static boolean DEBUG=true;

    public static final boolean CAN_START_FOREGROUND_SERVICE;

    public static final int WINDOW_OPTION_MODE_XIAOMI = 1;
    public static final int WINDOW_OPTION_MODE_HUAWEI = 2;
    public static final int WINDOW_OPTION_MODE_ALIYUNOS = 3;
    public static final int WINDOW_OPTION_MODE_OPPOCOLOR_OS = 4;
    public static final int WINDOW_OPTION_MODE_OPPOCOLOR_OS_PURE = 5;
    //    public static final int WINDOW_OPTION_MODE_VERSION_CODES_LOLLIPOP   =   6;
    private static final String[] PM_USAGE_STATS_PERMISSION = new String[]{
            "android.permission.PACKAGE_USAGE_STATS" // Manifest.permission.PACKAGE_USAGE_STATS
    };

    static {
        // 不启动前台服务的机型：一加One A0001; Galaxy S3 (m0) on Android 4.4
        CAN_START_FOREGROUND_SERVICE
                = !(Build.MANUFACTURER.equalsIgnoreCase("OnePlus")
                && (Build.MODEL.equals("A0001") || Build.DEVICE.equals("A0001")))

                && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.MANUFACTURER.equalsIgnoreCase("samsung")
                && (Build.MODEL.equals("m0") || Build.DEVICE.equals("m0")))
        ;

        if (DEBUG) {
            Log.v(TAG, "Can start foreground service? " + CAN_START_FOREGROUND_SERVICE);
        }
    }

    /**
     * "ANDROID_ID"_"DeviceID"_"WIFI MAC Address"
     */
    public static String getDeviceId(Context context) {
        String androidId = "";
        try {
            androidId = Settings.Secure.getString(
                    context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ignored) {
        }
        if (null == androidId) androidId = "";

        String deviceId = Telephony.getDeviceId(context);
        if (deviceId == null || deviceId.equals(androidId)) {
            deviceId = "";
        }
        String wifiAddress = null;
        if (TextUtils.isEmpty(androidId) || TextUtils.isEmpty(deviceId)) {
            wifiAddress = NetUtils.getWifiMacAddress(context);
        }
        if (wifiAddress == null) {
            wifiAddress = "";
        }
        return androidId + "_" + deviceId + "_" + wifiAddress;
    }

    /**
     * Cited from CM; get the device id in the form of xxxx-xxxx-xxxx-xxxx
     */
    public static String getDeviceUuid(Context context) {
        String id = getUniqueID(context);
        if (id == null) {
            id = Settings.Secure.getString(
                    context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        if (DEBUG) {
            Log.d(TAG, "[" + context.getPackageName() + "] Device UUID: " + id);
        }
        return id;
    }

    private static String getUniqueID(Context context) {
        String telephonyDeviceId = "NoTelephonyId";
        String androidDeviceId = "NoAndroidId";

        // get telephony id
        try {
            final TelephonyManager tm = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyDeviceId = tm.getDeviceId();
            if (telephonyDeviceId == null) {
                telephonyDeviceId = "NoTelephonyId";
            }
        } catch (Exception ignored) {
        }

        // get internal android device id
        try {
            androidDeviceId = Settings.Secure.getString(
                    context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (androidDeviceId == null) {
                androidDeviceId = "NoAndroidId";
            }
        } catch (Exception ignored) {
        }

        // build up the uuid
        try {
            String id = getStringIntegerHexBlocks(androidDeviceId.hashCode())
                    + "-"
                    + getStringIntegerHexBlocks(telephonyDeviceId.hashCode());

            return id;
        } catch (Exception ignored) {
            return "0000-0000-1111-1111";
        }
    }

    private static String getStringIntegerHexBlocks(int value) {
        String result = "";
        String string = Integer.toHexString(value);

        int remain = 8 - string.length();
        char[] chars = new char[remain];
        Arrays.fill(chars, '0');
        string = new String(chars) + string;

        int count = 0;
        for (int i = string.length() - 1; i >= 0; i--) {
            count++;
            result = string.substring(i, i + 1) + result;
            if (count == 4) {
                result = "-" + result;
                count = 0;
            }
        }

        if (result.startsWith("-")) {
            result = result.substring(1, result.length());
        }

        return result;
    }


    private static final String PRODUCT = Build.PRODUCT.toLowerCase();
    private static final String MODEL = Build.MODEL.toLowerCase();
    private static final String MANUFACTURER = Build.MANUFACTURER.toLowerCase();
    private static final String DISPLAY = Build.DISPLAY.toLowerCase();

    public static boolean isCoolpad5Series() {
        return getDeviceInfoString().contains("coolpad")
                || getDeviceInfoString().contains("yulong")
                && PRODUCT.matches("(?:coolpad|yulong)?5[0-9]+");
    }

    public static boolean isMotoG() {
        return Build.MODEL.startsWith("XT103") && Build.MANUFACTURER.equals("motorola");
    }

    public static boolean isMotoQuard() {
        return Build.MODEL.equals("XT1254") && Build.MANUFACTURER.equals("motorola");
    }

    public static boolean isHuaWei() {
        return MANUFACTURER.equalsIgnoreCase("huawei");
    }

    public static boolean isHuaWeiP6() {
        return MANUFACTURER.equalsIgnoreCase("huawei") && MODEL.contains("p6-u06");
    }

    public static boolean isHuaWeiG700() {
        return MANUFACTURER.equalsIgnoreCase("huawei") && MODEL.contains("g700-t00");
    }

    public static boolean isHuaWei8500() {
        return MANUFACTURER.equalsIgnoreCase("huawei") && MODEL.contains("c8500");
    }

    public static boolean isHuaWeiRongYao() {
        return MANUFACTURER.equalsIgnoreCase("huawei") && MODEL.contains("u9508");
    }

    public static boolean isHuaWeiRongYao3C() {
        return MANUFACTURER.equalsIgnoreCase("huawei") && (MODEL.contains("h30-t10")
                || MODEL.contains("h30-t00"));
    }

    public static boolean isHuaWeiC8813Q() {
        return MANUFACTURER.equalsIgnoreCase("huawei") && MODEL.contains("c8813q");
    }

    public static boolean isHuaWeiMT7() {
        return MANUFACTURER.equalsIgnoreCase("huawei") && MODEL.contains("mt7-tl00");
    }

    public static boolean isMeizu() {
        return MANUFACTURER.equals("meizu");
    }

    public static boolean isMeizuNote1() {
        return PRODUCT.contains("m1 note");
    }

    public static boolean isMeizuMX2() {
        return PRODUCT.contains("meizu_mx2");
    }

    public static boolean isMeizuMX3() {
        return PRODUCT.contains("meizu_mx3");
    }

    public static boolean isMeizuMX4() {
        return PRODUCT.contains("meizu_mx4");
    }

    public static boolean isMeizuM9() {
        return PRODUCT.contains("meizu_m9") && MODEL.contains("m9");
    }

    public static boolean isMeizuMX() {
        return PRODUCT.contains("meizu_mx");
    }

    public static boolean isMeizuMXs() {
        return isMeizu() || PRODUCT.trim().equals("m1") || isMeizuNote1()
                || isMeizuMX2() || isMeizuMX3() || isMeizuM9() || isMeizuMX4()
                || isMeizuMX();
    }

    public static boolean isHTCX710S() {
        return MANUFACTURER.equals("htc") && MODEL.contains("velocity 4g x710s");
    }

    public static boolean isHTC609D() {
        return MANUFACTURER.equals("htc") && MODEL.contains("htc 609d");
    }

    public static boolean isHtcM7() {
        return MODEL.contains("htc 801e");
    }

    public static boolean isVivoX3t() {
        return MODEL.equalsIgnoreCase("vivo x3t");
    }

    /**
     * 判断是否 Android 4.1
     */
    public static boolean isApiLevel16() {
        return Build.VERSION.SDK_INT >= 16;
    }

    public static boolean isIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= 14;
    }

    public static boolean isHoneycomb() {
        return Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 14;
    }

    /**
     * if android sdk is lower than 3.0
     */
    public static boolean isBeforeHoneycomb() {
        return Build.VERSION.SDK_INT < 11;
    }

    /**
     * if android sdk is 2.2
     */
    public static boolean isFroyo() {
        return Build.VERSION.SDK_INT == 8;
    }

    /**
     * if android sdk is after 2.2
     */
    public static boolean isAfterFroyo() {
        return Build.VERSION.SDK_INT > 8;
    }

    /**
     * if android sdk is 2.1
     */
    public static boolean isEclair() {
        return Build.VERSION.SDK_INT == 7;
    }

    /**
     * if android sdk is after 2.1
     */
    public static boolean isAfterEclair() {
        return Build.VERSION.SDK_INT > 7;
    }

    public static final boolean isJellyBean() {
        return Build.VERSION.SDK_INT >= 16;
    }

    /**
     * whether the folder 'res/[drawable|values|layout|...]-sw&lt;N&gt;dp' works
     */
    public static boolean isSWNDPValuesSupported() {
        return Build.VERSION.SDK_INT >= 13;
    }

    public static boolean isSendBroadcastDirectlySupported() {
        return Build.VERSION.SDK_INT < 12;
    }

    /**
     * 5.0
     *
     * @return
     */
    public static boolean isAfterApiLevel21() {
        return Build.VERSION.SDK_INT >= 21;
    }

    /**
     * 夏新大V
     *
     * @return
     */
    public static boolean isBigV() {
        return ("amoi n826".equalsIgnoreCase(MODEL)
                || "amoi n821".equalsIgnoreCase(MODEL)
                || "amoi n820".equalsIgnoreCase(MODEL));
    }

    public static boolean isGalaxyNexusAndApiLevel16() {
        return MODEL.equalsIgnoreCase("Galaxy Nexus") && Build.VERSION.SDK_INT == 16;
    }

    public static boolean isNexus5() {
        return MODEL.contains("nexus 5");
    }

    public static boolean isCoolpad5950() {
        return MANUFACTURER.equals("yulong") && PRODUCT.contains("5950");
    }

    public static boolean isSamsungButNotGalaxy() {
        return MANUFACTURER.contains("samsung") && !MODEL.equalsIgnoreCase("Galaxy Nexus");
    }

    public static boolean isSamsung() {
        return MANUFACTURER.contains("samsung");
    }

    public static boolean isXiaomi() {
        return MANUFACTURER.equals("xiaomi");
    }

    /**
     * 小米V5.V6
     * <p>
     * MANUFACTURER: Xiaomi, PRODUCT: aries ID:JRO03L, brand:Xiaomi,
     * display:JRO03L, MODEL: MI 2, screen: 720*1280 DPI:320 CODENAME = REL,
     * INCREMENTAL = 3.4.5, RELEASE =4.1.1, SDK = 16
     * <strong>不是很确定此函数的判断依据是否准确，未来如果有更靠谱的判断标准则再修正</strong>
     * 已改正，方法来源：MIUI论坛，<MIUI论坛-MIUI专区-应用开发者-小米应用开发者文档> 如有错误，可从论坛参考
     * </p>
     */
    public static boolean isMiuiV5() {
        String miuiVersionName = QlUtils.getSystemProperty("ro.miui.ui.version.name");
        if (null == miuiVersionName) miuiVersionName = "";
        return miuiVersionName.equalsIgnoreCase("V5") || miuiVersionName.equalsIgnoreCase("V6");
    }

    public static boolean isMiuiRom() {
        return DISPLAY.contains("miui") || DISPLAY.contains("mione");
    }

    /**
     * 小米V5.V6
     * <p>
     * MANUFACTURER: Xiaomi, PRODUCT: aries ID:JRO03L, brand:Xiaomi, display:JRO03L, MODEL: MI 2, screen: 720*1280 DPI:320
     * CODENAME = REL,  INCREMENTAL = 3.4.5, RELEASE =4.1.1, SDK = 16
     * <strong>不是很确定此函数的判断依据是否准确，未来如果有更靠谱的判断标准则再修正</strong>
     * <p/>
     * 已改正，方法来源：MIUI论坛，<MIUI论坛-MIUI专区-应用开发者-小米应用开发者文档>
     * 如有错误，可从论坛参考
     * </p>
     *
     * @return
     */
    public static boolean isMiuiV5OrV6() {
        String miuiVersionName = QlUtils.getSystemProperty("ro.miui.ui.version.name");
        return null != miuiVersionName
                && (miuiVersionName.equalsIgnoreCase("V5")
                || miuiVersionName.equalsIgnoreCase("V6"));
    }

    public static boolean isMiuiV6() {
        String miuiVersionName = QlUtils.getSystemProperty("ro.miui.ui.version.name");
        return null != miuiVersionName && miuiVersionName.equalsIgnoreCase("V6");
    }

    /**
     * oppo R8007
     */
    public static boolean isOppoR8007() {
        return MANUFACTURER.equals("oppo") && MODEL.contains("r8007");
    }

    /**
     * 4.2
     */
    public static boolean isAfterApiLevel17() {
        return Build.VERSION.SDK_INT >= 17;
    }

    /**
     * 4.3
     */
    public static boolean isAfterApiLevel18() {
        return Build.VERSION.SDK_INT >= 18;
    }

    /**
     * 4.4
     */
    public static boolean isAfterApiLevel19() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static boolean isCoolpad8297() {
        return MANUFACTURER.equals("coolpad") && PRODUCT.contains("8297");
    }

    public static boolean isCoolpad7298A() {
        return MANUFACTURER.equals("coolpad") && PRODUCT.contains("7298a");
    }

    public static boolean isCoolpad7295() {
        return MANUFACTURER.equals("coolpad") && MODEL.contains("7295");
    }

    public static boolean isCoolpad8675() {
        return MANUFACTURER.equals("yulong") && PRODUCT.contains("8675");
    }

    public static boolean isAliYunOs() {
        return !TextUtils.isEmpty(QlUtils.getSystemProperty("ro.yunos.version"));
    }

    public static boolean isOppoColorOs() {
        return !TextUtils.isEmpty(QlUtils.getSystemProperty("ro.build.version.opporom"));
    }

    public static boolean huaWeiSystemManagerExists(Context context) {
        return QlUtils.packageExists(context, "com.huawei.systemmanager");
    }

    public static boolean isEmui30() {
        String version = QlUtils.getSystemProperty("persist.sys.launcher.emuiver");
        return QlUtils.equals(version, "EmotionUI_3.0");
    }

    public static boolean isEmui23() {
        return QlUtils.equals(QlUtils.getSystemProperty("ro.build.version.emui"), "EmotionUI_2.3")
                || Build.DISPLAY.startsWith("EMUI2.3");
    }

    public static boolean isMe525() {
        return MODEL.startsWith("me525");
    }

    public static boolean isGTP1000() {
        return MODEL.equalsIgnoreCase("gt-p1000");
    }

    public static boolean isDEOVOV5() {
        return MODEL.equalsIgnoreCase("deovo v5");
    }

    public static boolean isZTEU985() {
        return MANUFACTURER.equals("zte") && MODEL.contains("zte u985");
    }

    public static boolean isMe860() {
        return MODEL.startsWith("me860");
    }

    public static boolean isHTCOneX() {
        return MANUFACTURER.equals("htc") && MODEL.contains("htc one x");
    }

    public static boolean isU9200() {
        return MODEL.startsWith("u9200");
    }

    public static boolean isGtS5830i() {
        return MODEL.equalsIgnoreCase("gt-s5830i");
    }

    public static boolean isGtS5830() {
        return MODEL.equalsIgnoreCase("gt-s5830");
    }

    public static boolean isI9100() {
        return MANUFACTURER.equals("samsung") && MODEL.equals("gt-i9100");
    }

    public static boolean isW2013() {
        return MANUFACTURER.equals("samsung") && MODEL.equals("sch-w2013");
    }

    public static boolean isHtcDevice() {
        return MODEL.contains("htc") || MODEL.contains("desire");
    }

    public static boolean isLephoneDevice() {
        return PRODUCT.contains("lephone");
    }

    public static boolean isZTEU880() {
        return MANUFACTURER.equals("zte") && MODEL.contains("blade");
    }

    /**
     * 制造商：ZTE
     * 型号：ZTE-U V880
     *
     * @return
     */
    public static boolean isZTEUV880() {
        return MANUFACTURER.equals("zte") && MODEL.contains("zte-u v880");
    }

    public static boolean isHTCHD2() {
        return MANUFACTURER.equals("htc") && MODEL.contains("hd2");
    }

    public static boolean isHTCOne() {
        return MANUFACTURER.equals("htc") && MODEL.contains("htc 802w");
    }

    public static boolean isMiOne() {
        return MODEL.startsWith("mi-one");
    }

    public static boolean isMiTwo() {
        return MODEL.startsWith("mi 2");
    }

    public static boolean isMiThree() {
        return MODEL.startsWith("mi 3");
    }

    public static boolean isHongMi() {
        return MANUFACTURER.equals("xiaomi") && Build.DEVICE.startsWith("HM");
    }

    public static boolean isMb525() {
        return MODEL.startsWith("mb525");
    }

    public static boolean isMb526() {
        return MODEL.startsWith("mb526");
    }

    public static boolean isMe526() {
        return MODEL.startsWith("me526");
    }

    public static boolean isMe865() {
        return MODEL.startsWith("me865");
    }

    public static boolean isXT882() {
        return MODEL.startsWith("xt882");
    }

    public static boolean isYulong() {
        return MANUFACTURER.equalsIgnoreCase("yulong");
    }

    public static boolean isKindleFire() {
        return MODEL.contains("kindle fire");
    }

    public static boolean isLGP970() {
        return MODEL.startsWith("lg-p970");
    }

    public static boolean isU8800() {
        return MODEL.startsWith("u8800");
    }

    public static boolean isC8650E() {
        return PRODUCT.equalsIgnoreCase("c8650e") && MANUFACTURER.equalsIgnoreCase("huawei");
    }

    public static boolean isMt15i() {
        return MODEL.startsWith("mt15i");
    }

    public static boolean isSonyE15i() {
        return MODEL.startsWith("e15i");
    }

    public static boolean isSonyLT29i() {
        return MODEL.startsWith("lt29i");
    }

    public static boolean isSonyST18i() {
        return MODEL.startsWith("st18i");
    }

    public static boolean isSonyC2105() {
        return MANUFACTURER.equals("sony") && MODEL.startsWith("c2105");
    }

    public static boolean isXT702() {
        return MODEL.startsWith("xt702");
    }

    public static boolean isLenovoA390t() {
        return MODEL.equalsIgnoreCase("Lenovo A390t");
    }

    public static boolean isLenovoK860i() {
        return MODEL.equalsIgnoreCase("lenovo k860i");
    }

    public static boolean isLenovoA850() {
        return MODEL.contains("lenovo a850");
    }

    public static boolean isLenovoP770() {
        return MODEL.equalsIgnoreCase("lenovo p770");
    }

    public static boolean isLenovoA789() {
        return MODEL.equalsIgnoreCase("lenovo a789");
    }

    public static boolean isHongZ() {
        return MODEL.equalsIgnoreCase("z8050") && MANUFACTURER.equalsIgnoreCase("cellon");
    }

    public static boolean isOnePlus() {
        return MODEL.equalsIgnoreCase("a0001") && MANUFACTURER.equalsIgnoreCase("oneplus");
    }

    public static String getDeviceInfoString() {
        return "PRODUCT: " + PRODUCT
                + " MODEL: " + MODEL + " MANUFACTURER: " + MANUFACTURER + " DISPLAY: " + DISPLAY;
    }

    public static void printDeviceInfo() {
        Log.d(TAG, "PRODUCT = " + PRODUCT
                + ", MODEL = " + MODEL + ", MANUFACTURER = " + MANUFACTURER);
    }


    /**
     * 判断MIUI的悬浮窗权限
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isFloatWindowOpAllowed(Context context, String pagName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return checkOp(context, 24, pagName);
        } else {
            int flags = context.getApplicationInfo().flags;
            String temp = Integer.toBinaryString(flags);
            if (DEBUG) {
                Log.d(TAG, "Application info flags: " + flags + ", " + temp);
            }
            int i = temp.length() - 28;
            if (i >= 0 && temp.charAt(i) == '1') {
                return true;
            } else {
                return false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op, String pagName) {
        @SuppressWarnings("ResourceType")
        AppOpsManager manager = (AppOpsManager)
                context.getSystemService(Context.APP_OPS_SERVICE);
        Method checkOp = ReflectionUtils.getDeclaredMethod(AppOpsManager.class, "checkOp"
                , new Class[]{int.class, int.class, String.class});
        try {
            if (AppOpsManager.MODE_ALLOWED == (Integer) ReflectionUtils.invokeMethod(manager
                    , checkOp, op, Binder.getCallingUid(), pagName)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, "checkOp failed", e);
            }
        }
        return false;
    }


    public static void startMiuiPermissionActivity(Context context, String pkgName) {
        Intent intent;
        if (isMiuiV6()) {
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter"
                    , "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", pkgName);
        } else/* if (isMiuiV5())*/ {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Uri uri = Uri.fromParts("package", pkgName, null);
            intent.setData(uri);
//            PackageInfo pInfo = null;
//            try {
//                pInfo = context.getPackageManager().getPackageInfo(BuildConfig., 0);
//            } catch (PackageManager.NameNotFoundException ignored) {
//            }
//            intent.setClassName("com.android.settings"
//                    , "com.miui.securitycenter.permission.AppPermissionsEditor");
//            intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);
        }
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "startMiuiPermissionActivity failed, no activity found: " + intent);
            }
        }
    }

    public static void startHuaWeiNotificationManagementActivity(Context context) {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", isEmui23()
                    ? "com.huawei.systemmanager.SystemManagerMainActivity"
                    : "com.huawei.notificationmanager.ui.NotificationManagmentActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, "Failed to launch HuaWei NotificationManagementActivity: "
                        + DeviceUtils.getDeviceInfoString());
            }
        }
    }

    public static void startAliSecurityCenter(Context context) {
        try {
            Intent intent = new Intent()
                    .setClassName("com.aliyun.SecurityCenter"
                            , "com.aliyun.SecurityCenter.ui.SecurityCenterActivity")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, "Failed to launch Ali SecurityCenterActivity: " + getDeviceInfoString());
            }
        }
    }

    public static void startOppoFloatWindowManagement(Context context) {
        QlUtils.startActivity(context, new Intent().setClassName(
                "com.oppo.safe", "com.oppo.safe.permission.PermissionTopActivity"));
    }

    public static void startOppoPureBackground(Context context) {
        try {
            Intent intent = new Intent()
                    .setClassName("com.oppo.purebackground"
                            , "com.oppo.purebackground.PurebackgroundTopActivity")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, "Failed to launch Oppo pure background: "
                        + DeviceUtils.getDeviceInfoString());
            }
        }
    }

//    public static boolean isUsageAccessPermissionGranted(Context context) {
//        if (BuildConfig.DEBUG) {
//            Log.d(TAG, "Checking if we have usage access permission...");
//        }
//
//        //noinspection ResourceType
//        AppOpsManager aom = (AppOpsManager)
//                context.getSystemService(Context.APP_OPS_SERVICE);
//        try {
//            int opMode = aom.checkOp("android:get_usage_stats"
//                    , Binder.getCallingUid(), BuildConfig.);
//            if (BuildConfig.DEBUG) {
//                Log.d(TAG, "Op mode (0--Allowed; 3--Default): " + opMode);
//            }
//            if (AppOpsManager.MODE_ALLOWED == opMode) {
//                if (BuildConfig.DEBUG) {
//                    Log.d(TAG, "Usage access permission granted");
//                }
//                return true;
//            }
//
//            if (3 == opMode) {
//                // Load the packages that have been granted the PACKAGE_USAGE_STATS permission.
//                List<PackageInfo> packageInfos = context.getPackageManager()
//                        .getPackagesHoldingPermissions(PM_USAGE_STATS_PERMISSION, 0);
//                int packageInfoCount = packageInfos != null ? packageInfos.size() : 0;
//                for (int i = 0; i < packageInfoCount; i++) {
//                    PackageInfo packageInfo = packageInfos.get(i);
//                    if (BuildConfig..equals(packageInfo.packageName)) {
//                        if (BuildConfig.DEBUG) {
//                            Log.d(TAG, "Usage access permission granted");
//                        }
//                        return true;
//                    }
//                }
//            }
//        } catch (Throwable t) {
//            Log.w(TAG, "Failed to check if we were granted the usage access permission: "
//                    + t.getMessage());
//        }
//        return false;
//    }

    /**
     * 获得设置悬浮窗的模式，0 代表不需要设置，其他根据模式来区分
     */
    public static int getWindowOptionMode(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                || (isXiaomi() && isMiuiV5OrV6())) {
            boolean isAliYunOs = isAliYunOs();
            if (isXiaomi()) {
                return WINDOW_OPTION_MODE_XIAOMI;
            } else if (isHuaWei() && huaWeiSystemManagerExists(context)) {
                return WINDOW_OPTION_MODE_HUAWEI;
            } else if (isAliYunOs) {
                return WINDOW_OPTION_MODE_ALIYUNOS;
            } else if (DeviceUtils.isOppoColorOs()) {
                return WINDOW_OPTION_MODE_OPPOCOLOR_OS;
            }
        }

        // Oppo纯净后台
        if (isOppoColorOs()) {
            return WINDOW_OPTION_MODE_OPPOCOLOR_OS_PURE;
        }

//        // Android 5.0检查Usage access
//        if (Build.VERSION.SDK_INT >= 21 && !isUsageAccessPermissionGranted(context)) {
//            return WINDOW_OPTION_MODE_VERSION_CODES_LOLLIPOP;
//        }
        return 0;
    }

    /**
     * 根据模式打开不同机型的悬浮窗设置。
     *
     * @param mode
     */
    public static void startWindowOptionByMode(Context context, int mode, String packName) {
        switch (mode) {
            case WINDOW_OPTION_MODE_XIAOMI:
                startMiuiPermissionActivity(context, packName);
                break;
            case WINDOW_OPTION_MODE_HUAWEI:
                startHuaWeiNotificationManagementActivity(context);
                break;
            case WINDOW_OPTION_MODE_ALIYUNOS:
                startAliSecurityCenter(context);
                break;
            case WINDOW_OPTION_MODE_OPPOCOLOR_OS:
                startOppoFloatWindowManagement(context);
                break;
            case WINDOW_OPTION_MODE_OPPOCOLOR_OS_PURE:
                startOppoPureBackground(context);
                break;
//            case WINDOW_OPTION_MODE_VERSION_CODES_LOLLIPOP:
//                QlUtils.startUsageAccessSettings(context);
//                break;
        }
    }

    public static boolean isCheckSysOp(int mode) {
        return mode > 0 && !isOppoPureBackground(mode);
    }

    public static boolean isOppoPureBackground(int mode) {
        return mode == WINDOW_OPTION_MODE_OPPOCOLOR_OS_PURE;
    }

    public static void startHuaWeiStartupManagerActivity(Context context) {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager"
                    , "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    public static boolean startMiuiAutoStartManagementActivity(Context context, String pkgName) {
        Intent intent = new Intent().setClassName("com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity");
        if (!isMiuiV6() || !startActivity(context, intent)) {
            startMiuiPermissionActivity(context, pkgName);
            return false;
        }
        return true;
    }

    private static boolean startActivity(Context context, Intent intent) {
        try {
            if (!(context instanceof Activity)
                    && 0 == (intent.getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            return true;
        } catch (Throwable t) {
        }
        return false;
    }

    public static void startHuaWeiProtectedAppsManagerActivity(Context context) {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager"
                    , "com.huawei.systemmanager.optimize.process.ProtectActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 判断是否是平板
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int getSmallestScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.min(metrics.widthPixels, metrics.heightPixels);
    }

    public static int getLargestShortAxisWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.max(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * 某些机型的 PackageManager.resolveActivity 以及 PackageManager.queryIntentActivities 返回为空
     * 此处对这些机型做特殊处理
     *
     * @param action
     * @param uri
     * @return
     */
    public static ComponentName resolveActivity(String action, String uri) {
        if (isZTEUV880()) {
            if (Intent.ACTION_VIEW.equals(action)
                    && "content://com.android.contacts/contacts".equals(uri)) {
                ComponentName cn = new ComponentName(
                        "com.android.contacts"
                        , "com.android.contacts.DialtactsContactsEntryActivity");
                return cn;
            }
        }
        return null;
    }

    public static boolean hasLightSensor(Context context) {
        if (isLGP970()) {
            return true;
        }

        SensorManager sensorService = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);

        if (sensorService != null) {
            List<Sensor> lightSensors = sensorService.getSensorList(Sensor.TYPE_LIGHT);
            return lightSensors != null && !lightSensors.isEmpty();
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getExternalAvailableBytes() {
        if (externalMemoryAvailable()) {
            File pathFile = android.os.Environment.getExternalStorageDirectory();
            android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
            if (DeviceUtils.isAfterApiLevel18()) {
                return statfs.getAvailableBytes();
            } else {
                return statfs.getBlockSize() * statfs.getAvailableBlocks();
            }
        }
        return 0;
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * sdcard是否存在
     *
     * @return
     */
    public static boolean externalMemoryAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


}
