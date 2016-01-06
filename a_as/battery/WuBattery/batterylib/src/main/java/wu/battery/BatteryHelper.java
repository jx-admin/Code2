package wu.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wu.base.BaseBroadcastReceiver;
import wu.battery.model.Battery;
import wu.utils.LogUtils;
import wu.utils.SharedPreferencesUtils;

/**
 * Created by jx on 2015/12/31.
 */
public class BatteryHelper {
    private Context context;

    //数据持久化
    public static final String BATTERY_LOG = "BATTERY_LOG";
    private static final String SP_KEY_SLOW_MODEL_START_TIME = "modelSlowStartTime";
    private SharedPreferencesUtils spu;

    //
    private static Battery battery;
    private int lastChargingModel;
    //涓流充电时间
    private static long modelSlowStartTime;
    private Timer timer;
    private MyTimerTask mTask;
    private List<IOnBatteryChangedListener> onBatteryChangedListeners = new ArrayList<IOnBatteryChangedListener>();
    private BatteryChangedReceiver mBatteryChangedReceiver = new BatteryChangedReceiver();

    public void addOnBatteryChangedListener(IOnBatteryChangedListener onBatteryChangedListener) {
        if (onBatteryChangedListener == null) {
            LogUtils.d("addOnBatteryChangedListener null");
            return;
        }
        onBatteryChangedListeners.add(onBatteryChangedListener);
        if (battery == null) {
            battery = getBatteryStatus(context);
        }
        if (battery != null) {
            onBatteryChangedListener.onBatteryChanged(battery);
        }
    }

    public void removeOnBatteryChangedListener(IOnBatteryChangedListener onBatteryChangedListener) {
        onBatteryChangedListeners.remove(onBatteryChangedListener);
    }

    public Battery getBattery() {
        return battery;
    }

    public void create(Context context) {
        LogUtils.d("onCreate");
        this.context = context;
        spu = SharedPreferencesUtils.getSharedPreferencesUtils(context, BATTERY_LOG);
        readChargingSlowTime();
        mBatteryChangedReceiver.register(context);
    }

    public void destroy() {
        if (mTask != null) {
            mTask.cancel();
        }
        if (null != timer) timer.cancel();
        mBatteryChangedReceiver.unRegister();
        LogUtils.d("onDestroy");
    }


    public void onPowerConnected() {
        for (IOnBatteryChangedListener onBatteryChangedListener : onBatteryChangedListeners) {
            onBatteryChangedListener.onPowerConnected();
        }
    }

    public void onPowerDisConnected() {
        for (IOnBatteryChangedListener onBatteryChangedListener : onBatteryChangedListeners) {
            onBatteryChangedListener.onPowerDisConnected();
        }
    }

    private static void calcBattery(Battery battery) {
        if (battery.scale != 100) {
            battery.level = Math.round(100f * battery.level / battery.scale);
            battery.scale = 100;
        }

        long chargingSpeed = 0;
        //充电剩余时间
        switch (battery.plugger) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                chargingSpeed = Battery.SPEED_AC;
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                chargingSpeed = Battery.SPEED_USB;
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                chargingSpeed = Battery.SPEED_WIRELESS;
                break;
        }
        if (!battery.isCharging()) {
            battery.chargingModel = Battery.CHARGING_MODEL_NONE;
        } else if (battery.level <= 10) {
            battery.chargingModel = Battery.CHARGING_MODEL_QUICK;
            battery.remainTime = (battery.scale - battery.level) * chargingSpeed;
        } else if (battery.level < 100) {
            battery.chargingModel = Battery.CHARGING_MODEL_NOMAL;
            battery.remainTime = (battery.scale - battery.level) * chargingSpeed;
        } else if ((System.currentTimeMillis() - modelSlowStartTime) < Battery.CHARGING_SLOW_TIME) {
            battery.chargingModel = Battery.CHARGING_MODEL_SLOW;
            //涓流充电剩余时间
            battery.remainTime = Battery.CHARGING_SLOW_TIME - (System.currentTimeMillis() - modelSlowStartTime);
        } else {
            battery.chargingModel = Battery.CHARGING_MODEL_DONE;
        }
    }

    public void onBatteryChanged(Battery battery) {
        if (battery != null) {
            LogUtils.d("onBatteryChanged BatteryActivity");
            onChargingModelChanged(battery.chargingModel);
            broadOnBatteryChanged(battery);
        }
    }

    private void broadOnBatteryChanged(Battery battery) {
        for (IOnBatteryChangedListener onBatteryChangedListener : onBatteryChangedListeners) {
            onBatteryChangedListener.onBatteryChanged(battery);
        }
    }

    public void onChargingModelChanged(int model) {
        if (this.lastChargingModel != model) {
            lastChargingModel = model;
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }

            if (lastChargingModel == Battery.CHARGING_MODEL_SLOW) {
                if (modelSlowStartTime <= 0) {
                    modelSlowStartTime = System.currentTimeMillis();
                    setChargingSlowTime(modelSlowStartTime);
                }
                if (timer == null) {
                    timer = new Timer();
                }
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                }
                mTask = new MyTimerTask();
                if (BuildConfig.DEBUG) {
                    timer.schedule(mTask, 0, 1000);
                } else {
                    timer.schedule(mTask, 0, 60 * 1000);
                }
            } else if (lastChargingModel == Battery.CHARGING_MODEL_DONE) {

            } else {
                modelSlowStartTime = 0;
                setChargingSlowTime(modelSlowStartTime);
            }
            battery.chargingModel = lastChargingModel;
            broadOnBatteryChanged(battery);
        }
    }

    class MyTimerTask extends TimerTask {

        public MyTimerTask() {
        }

        @Override
        public void run() {
            long modelSlowTime = (System.currentTimeMillis() - modelSlowStartTime);
            if (modelSlowTime + 500 >= Battery.CHARGING_SLOW_TIME) {
                battery.remainTime = 0;
                mTask.cancel();
                onChargingModelChanged(Battery.CHARGING_MODEL_DONE);
            } else {
                battery.chargingModel = lastChargingModel;
                battery.remainTime = Battery.CHARGING_SLOW_TIME - modelSlowTime;
                broadOnBatteryChanged(battery);
            }
        }
    }

    private void setChargingSlowTime(long time) {
        spu.put(SP_KEY_SLOW_MODEL_START_TIME, time);
    }

    private void readChargingSlowTime() {
        modelSlowStartTime = spu.get(SP_KEY_SLOW_MODEL_START_TIME, 0l);
        LogUtils.d("readChargingSlowTime " + modelSlowStartTime);
    }

    public static Battery getBatteryStatus(Context context) {
        LogUtils.d("getBatteryStatus batteyAcitivyt");
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        return parseBattery(batteryStatus);
    }

    private static Battery parseBattery(Intent intent) {
        if (intent == null) {
            return null;
        }
        Battery battery = new Battery();
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        battery.status = status;
        battery.plugger = chargePlug;
        battery.level = level;
        battery.scale = scale;
        battery.voltage = voltage;
        battery.present = present;
        battery.health = health;
        battery.temperature = temperature;
        battery.technology = technology;
        BatteryHelper.battery = battery;
        calcBattery(battery);
        return battery;
    }

    /**
     * 电池状态监听，只能动态注册
     */
    class BatteryChangedReceiver extends BaseBroadcastReceiver {

        @Override
        public IntentFilter getintentFilter() {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            ifilter.addAction(Intent.ACTION_BATTERY_LOW);
            ifilter.addAction(Intent.ACTION_BATTERY_OKAY);
            ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
            ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            return ifilter;
        }

        @Override
        public void onReceive(Context context, final Intent intent) {

            new Thread() {
                public void run() {
                    String action = intent.getAction();
                    if (BuildConfig.DEBUG) {
                        LogUtils.d("BatteryChangedReceiver <--" + activeCount());
                    }
                    if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                        final Battery battery = parseBattery(intent);
                        onBatteryChanged(battery);
                    } else if (Intent.ACTION_BATTERY_LOW.equals(action)) {
                        onChargingModelChanged(Battery.CHARGING_MODEL_QUICK);
                    } else if (Intent.ACTION_BATTERY_OKAY.equals(action)) {
                        onChargingModelChanged(Battery.CHARGING_MODEL_NOMAL);
                    } else if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
                        onPowerConnected();
                    } else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
                        onPowerDisConnected();
                    }
                }
            }.start();
        }
    }
}
