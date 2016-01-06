package wu.battery;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 多个apk相同功能的唯一性管理
 *
 * @author zhangjing
 */
public class ChargeLockAccessor {

    private static final String TAG = "holaPowerLockAccessor";
    private static final boolean LOGD_ENABLED = BuildConfig.DEBUG;
    public static final long INVALID_TIMESTAMP_GAP = 2 * DateUtils.SECOND_IN_MILLIS;
    private static final int VERSION = 1;
    public static final String HOLA_POWER_MANAGER_ACTION = "com.holaverse.common.POWER_CONNECTED";
    private static final String FILE_NAME = "pl.dat";

    private static long sLastReceivePowerConnectedTimestamp = 0;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    /**
     * 接收到powerConnected时，调用此方法
     *
     * @param context
     * @param callback
     */
    public static void onReceivePowerConnected(final Context context, final PowerLockCallback callback) {
        if (callback == null) {
            return;
        }
        final long currentTimestamp = System.currentTimeMillis();
        if (callback.disabled() || currentTimestamp - sLastReceivePowerConnectedTimestamp < INVALID_TIMESTAMP_GAP && currentTimestamp > sLastReceivePowerConnectedTimestamp) {
            return;
        }
        sLastReceivePowerConnectedTimestamp = currentTimestamp;

        new Thread() {
            @Override
            public void run() {
                handlePowerConnected(context, callback, currentTimestamp);
            }
        }.start();
    }

    /**
     * 关闭充电锁屏界面时，调用此方法
     *
     * @param context
     */
    public static void onPowerLockDismiss(Context context) {
        clearData(context);
    }

    private static void handlePowerConnected(final Context context, final PowerLockCallback callback, final long currentTimestamp) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(HOLA_POWER_MANAGER_ACTION);
        List<String> packages = new ArrayList<String>();
        List<ResolveInfo> infos = pm.queryBroadcastReceivers(intent, 0);
        for (ResolveInfo resolveInfo : infos) {
            if (resolveInfo.activityInfo != null) {
                packages.add(resolveInfo.activityInfo.packageName);
            }
        }

        if (LOGD_ENABLED) {
            Log.d(TAG, "packageNames:" + packages + "(" + context.getPackageName() + ")");
        }

        if (votePowerLock(context, packages)) {
            saveData(context, currentTimestamp);
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.startPowerLock();
                }
            });
        }

    }

    /**
     * 当前策略：按照系统排列的receiver顺序来尝试打开充电锁屏界面；每个应用按照自己所在的位置来进行延时判断。判断时检查前面的所有应用是否启动
     * TODO 备用策略：收到系统广播且开关不为disable的应用将收广播的时间戳记录下来；在经过一个固定延时之后，检查自己排位之前的应用时间戳是否更新；若无更新，则自己启动充电锁屏；否则，不处理
     *
     * @param context
     * @param packageNames
     * @return
     */
    private static boolean votePowerLock(Context context, List<String> packageNames) {
        if (packageNames == null || packageNames.size() == 0) {
            return false;
        }

        if (context.getPackageName().equals(packageNames.get(0))) {
            if (LOGD_ENABLED) {
                Log.i(TAG, "starts power lock for packagenames place 1:(" + context.getPackageName() + ")");
            }
            return true;
        }

        int index = packageNames.indexOf(context.getPackageName());
        if (index > 0) {
            try {
                Thread.sleep(index * 150);

                long currentTimeStamp = System.currentTimeMillis();
                for (int i = 0; i < index; i++) {
                    String packageName = packageNames.get(i);
                    String data = readData(context, packageName);
                    if (TextUtils.isEmpty(data)) {
                        continue;
                    }
                    try {
                        JSONObject json = new JSONObject(data);
                        int targetVersion = json.optInt("v", VERSION);
                        if (targetVersion > VERSION) {//如果目标的版本更新，则自己失效
                            if (LOGD_ENABLED) {
                                Log.d(TAG, packageName + " version is:" + targetVersion + " Ignore self" + "(" + context.getPackageName() + ")");
                            }
                            return false;
                        } else if (targetVersion < VERSION) {
                            continue;
                        }
                        long lastStartTimestamp = json.optLong("s");
                        if (currentTimeStamp > lastStartTimestamp && currentTimeStamp - lastStartTimestamp < INVALID_TIMESTAMP_GAP) {//是否在指定时间范围内启动过
                            if (LOGD_ENABLED) {
                                Log.d(TAG, packageName + " starts first. Ignore self" + "(" + context.getPackageName() + ")");
                            }
                            return false;
                        }

                    } catch (JSONException e) {
                    }
                }
                if (LOGD_ENABLED) {
                    Log.i(TAG, "starts power lock:(" + context.getPackageName() + ")");
                }

                saveData(context, currentTimeStamp);
                return true;
            } catch (InterruptedException e) {
                if (LOGD_ENABLED) {
                    Log.d(TAG, "thread interrupt. Ignore self" + "(" + context.getPackageName() + ")");
                }
            }
        }
        return false;
    }

    private static boolean saveData(Context context, long startTimestamp) {
        FileOutputStream stream = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("v", VERSION);
            if (startTimestamp >= 0) {
                jsonObject.put("s", startTimestamp);
            }
            stream = context.openFileOutput(FILE_NAME, Context.MODE_WORLD_READABLE);
            stream.write(jsonObject.toString().getBytes());
        } catch (Exception e) {
            return false;
        } finally {
            closeQuietly(stream);
        }
//        chmod(file);
        return true;
    }

    private static void clearData(Context context) {
        try {
            new File(getDataFile(context, context.getPackageName())).delete();
        } catch (Exception ignored) {
            return;
        }
    }

    private static String readData(Context context, String packageName) {
        InputStream in = null;
        try {
            in = new FileInputStream(new File(getDataFile(context, packageName)));
            return toString(in);
        } catch (Exception e) {
            if (LOGD_ENABLED) {
                Log.e(TAG, "read data error", e);
            }
            return null;
        } finally {
            closeQuietly(in);
        }
    }

    private static String getDataFile(Context context, String packageName) {
        return "/data/data/" + packageName + "/files/" + FILE_NAME;
    }

    private static String toString(InputStream input) throws IOException {
        StringWriter sw = new StringWriter();
        InputStreamReader in = new InputStreamReader(input);
        char[] buffer = new char[1024 * 4];
        int n = 0;
        while (-1 != (n = in.read(buffer))) {
            sw.write(buffer, 0, n);
        }
        return sw.toString();
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public interface PowerLockCallback {
        /**
         * 功能是否被关闭
         *
         * @return 关闭true，否则false.
         */
        public boolean disabled();

        /**
         * 执行功能模块如：显示界面或执行逻辑
         * 启动模块需要加入action {@link #HOLA_POWER_MANAGER_ACTION}
         */
        public void startPowerLock();
    }
}
