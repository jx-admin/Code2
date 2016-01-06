package wu.batteryaidl;

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.IBinder;
import android.os.RemoteException;


import java.util.ArrayList;
import java.util.List;

import wu.base.BaseSerive;
import wu.battery.BatteryHelper;
import wu.battery.ChargeLockAccessor;
import wu.battery.model.Battery;
import wu.batteryaidl.model.BatteryConsumer;
import wu.utils.LogUtils;
import wu.utils.SharedPreferencesUtils;


/**
 * Created by jx on 2015/12/23.
 */
public abstract class BatteryService extends BaseSerive {

    public static final int CMD_DEFAULT = 0;
    public static final int CMD_ACTION_POWER_CONNECTED = 1;
    public static final int CMD_ACTION_POWER_DISCONNECTED = 2;
    public static final int CMD_CALL_STATE_RINGING = 3;
    public static final int CMD_CALL_STATE_OFFHOOK = 4;
    public static final int CMD_CALL_STATE_IDLE = 5;
    public static final int CMD_ACTION_NEW_OUTGOING_CALL = 6;
    public static final int CMD_ACTION_BATTERY_CHANGED = 7;
    public static final int CMD_ACTION_BATTERY_LOW = 8;
    public static final int CMD_ACTION_BATTERY_OKAY = 9;
    public static final String CMD_TYPE = "cmd_type";
    public static final String BATTERY_LOG = "BATTERY_LOG";

    private BatteryHelper mBatteryHelper;
    private SharedPreferencesUtils spu;
    private BatteryConsumer batteryConsumer;
    private BatteryConsumer batteryCallingConsumer;
    private BatteryConsumer batteryACConsumer;
    private BatteryConsumer batteryUSBConsumer;
    private BatteryConsumer batteryWirelessConsumer;
    private List<IOnBatteryChangedListener> onBatteryChangedListeners = new ArrayList<IOnBatteryChangedListener>();

    private IBatteryService.Stub binder = new IBatteryService.Stub() {

        @Override
        public void addOnBatteryChangedListener(wu.batteryaidl.IOnBatteryChangedListener onBatteryChangedListener) throws RemoteException {
            if (onBatteryChangedListener == null) {
                LogUtils.d("addOnBatteryChangedListener null");
                return;
            }
            onBatteryChangedListeners.add(onBatteryChangedListener);
            onBatteryChangedListener.onBatteryChanged(mBatteryHelper.getBattery());
        }

        @Override
        public void removeOnBatteryChangedListener(wu.batteryaidl.IOnBatteryChangedListener onBatteryChangedListener) throws RemoteException {
            onBatteryChangedListeners.remove(onBatteryChangedListener);
        }

        @Override
        public Battery getBattery() throws RemoteException {
            return mBatteryHelper.getBattery();
        }
    };
    private wu.battery.IOnBatteryChangedListener mIOnBatteryChangedListener = new wu.battery.IOnBatteryChangedListener() {
        @Override
        public void onBatteryChanged(wu.battery.model.Battery battery) {
            if (BuildConfig.DEBUG) {
                long curMinlis = System.currentTimeMillis();
                spu.put(Long.toString(curMinlis), battery);
                switch (battery.plugger) {
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        batteryACConsumer.add(curMinlis, battery.level);
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        batteryUSBConsumer.add(curMinlis, battery.level);
                        break;
                    case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                        batteryWirelessConsumer.add(curMinlis, battery.level);
                        break;
                    default:
                        batteryConsumer.add(curMinlis, battery.level);
                        break;
                }
                LogUtils.d(batteryConsumer.toString());
                LogUtils.d(batteryUSBConsumer.toString());
                LogUtils.d(batteryACConsumer.toString());
                LogUtils.d(batteryWirelessConsumer.toString());
                LogUtils.d(batteryCallingConsumer.toString());
            }
        }

        @Override
        public void onPowerConnected() {

        }

        @Override
        public void onPowerDisConnected() {

        }
    };

    public static void start(Context context, int type) {
        Intent i = new Intent(context, MainBatteryService.class);
        i.putExtra(BatteryService.CMD_TYPE, type);
        context.startService(i);
    }

    @Override
    public IBinder onBind(Intent intent) {
        onStart(intent);
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("onCreate");
        mBatteryHelper = new BatteryHelper();
        mBatteryHelper.create(this);
        spu = SharedPreferencesUtils.getSharedPreferencesUtils(this, BATTERY_LOG);
        if (BuildConfig.DEBUG) {
            batteryConsumer = SharedPreferencesUtils.get(this, "battery", null);
            batteryCallingConsumer = SharedPreferencesUtils.get(this, "batteryCallingConsumer", null);
            batteryACConsumer = SharedPreferencesUtils.get(this, "batteryACConsumer", null);
            batteryUSBConsumer = SharedPreferencesUtils.get(this, "batteryUSBConsumer", null);
            batteryWirelessConsumer = SharedPreferencesUtils.get(this, "batteryWirelessConsumer", null);
            if (batteryConsumer == null) {
                batteryConsumer = new BatteryConsumer(BatteryConsumer.TYPE_ALL);
            }
            if (batteryUSBConsumer == null) {
                batteryUSBConsumer = new BatteryConsumer(BatteryConsumer.TYPE_USB);
            }
            if (batteryACConsumer == null) {
                batteryACConsumer = new BatteryConsumer(BatteryConsumer.TYPE_AC);
            }
            if (batteryWirelessConsumer == null) {
                batteryWirelessConsumer = new BatteryConsumer(BatteryConsumer.TYPE_WARELESS);
            }
            if (batteryCallingConsumer == null) {
                batteryCallingConsumer = new BatteryConsumer(BatteryConsumer.TYPE_PHONE);
            }
            LogUtils.d(batteryConsumer.toString());
            LogUtils.d(batteryUSBConsumer.toString());
            LogUtils.d(batteryACConsumer.toString());
            LogUtils.d(batteryWirelessConsumer.toString());
            LogUtils.d(batteryCallingConsumer.toString());
            spu.put("service", "create:" + System.currentTimeMillis());
        }
        mBatteryHelper.addOnBatteryChangedListener(mIOnBatteryChangedListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int cmd = intent.getIntExtra(CMD_TYPE, CMD_DEFAULT);
            LogUtils.d("BatteryService onStart=" + cmd);
            switch (cmd) {
                case CMD_DEFAULT:
                    break;
                case CMD_ACTION_NEW_OUTGOING_CALL:
                    break;
                case CMD_CALL_STATE_IDLE:
                    break;
                case CMD_CALL_STATE_OFFHOOK:
                    break;
                case CMD_CALL_STATE_RINGING:
                    break;
                case CMD_ACTION_POWER_CONNECTED:
                    if (BuildConfig.DEBUG) {
                        spu.put(Long.toString(System.currentTimeMillis()), "CMD_ACTION_POWER_CONNECTED");
                    }
                    onPowerConnected();
                    break;
                case CMD_ACTION_POWER_DISCONNECTED:
                    if (BuildConfig.DEBUG) {
                        spu.put(Long.toString(System.currentTimeMillis()), "CMD_ACTION_POWER_DISCONNECTED");
                    }
                    onPowerDisConnected();
                    break;
                case CMD_ACTION_BATTERY_CHANGED:
                    break;
                case CMD_ACTION_BATTERY_LOW:
                    if (BuildConfig.DEBUG) {
                        spu.put(Long.toString(System.currentTimeMillis()), "CMD_ACTION_BATTERY_LOW");
                    }
                    break;
                case CMD_ACTION_BATTERY_OKAY:
                    if (BuildConfig.DEBUG) {
                        spu.put(Long.toString(System.currentTimeMillis()), "CMD_ACTION_BATTERY_OKAY");
                    }
                    break;
            }
        }
        onStart(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBatteryHelper.destroy();
        if (BuildConfig.DEBUG) {
            batteryConsumer.onStop();
            batteryCallingConsumer.onStop();
            batteryACConsumer.onStop();
            batteryUSBConsumer.onStop();
            batteryWirelessConsumer.onStop();
            SharedPreferencesUtils.put(this, "battery", batteryConsumer);
            SharedPreferencesUtils.put(this, "batteryCallingConsumer", batteryCallingConsumer);
            SharedPreferencesUtils.put(this, "batteryACConsumer", batteryACConsumer);
            SharedPreferencesUtils.put(this, "batteryUSBConsumer", batteryUSBConsumer);
            SharedPreferencesUtils.put(this, "batteryWirelessConsumer", batteryWirelessConsumer);
            spu.put(Long.toString(System.currentTimeMillis()), "onDestroy:" + System.currentTimeMillis());
        }
        LogUtils.d("onDestroy");
    }

    public void onPowerConnected() {
        try {
            for (wu.batteryaidl.IOnBatteryChangedListener onBatteryChangedListener : onBatteryChangedListeners) {
                onBatteryChangedListener.onPowerConnected();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        start(this);
    }

    public void onPowerDisConnected() {
        try {
            for (wu.batteryaidl.IOnBatteryChangedListener onBatteryChangedListener : onBatteryChangedListeners) {
                onBatteryChangedListener.onPowerDisConnected();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void broadOnBatteryChanged(Battery battery) {
        try {
            for (wu.batteryaidl.IOnBatteryChangedListener onBatteryChangedListener : onBatteryChangedListeners) {
                onBatteryChangedListener.onBatteryChanged(battery);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void start(Context context) {
        LogUtils.d("startView from batteryActivity");
        ChargeLockAccessor.onReceivePowerConnected(context, getPowerLockCallback(context));
    }

    protected abstract ChargeLockAccessor.PowerLockCallback getPowerLockCallback(Context context);


}
