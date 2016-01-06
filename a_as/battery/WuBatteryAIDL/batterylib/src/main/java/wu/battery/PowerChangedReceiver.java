package wu.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import wu.base.BaseBroadcastReceiver;
import wu.battery.model.Battery;
import wu.utils.LogUtils;


/**
 * Created by jx on 2015/12/23.
 * 在Manifests里面注册 Intent.ACTION_POWER_CONNECTED 和ChargeLockAccessor.HOLA_POWER_MANAGER_ACTION
 */
public abstract class PowerChangedReceiver extends BaseBroadcastReceiver {

    @Override
    public IntentFilter getintentFilter() {
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(ChargeLockAccessor.HOLA_POWER_MANAGER_ACTION);
//        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        return ifilter;
    }

    public void start(Context context) {
        LogUtils.d("startView from batteryActivity");
        ChargeLockAccessor.onReceivePowerConnected(context, getPowerLockCallback(context));
    }

    protected abstract ChargeLockAccessor.PowerLockCallback getPowerLockCallback(Context context);

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO 判断时间间隔
        String action = intent.getAction();
        if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            start(context);
        } else if (ChargeLockAccessor.HOLA_POWER_MANAGER_ACTION.equals(action)) {
            Battery battery = BatteryHelper.getBatteryStatus(context.getApplicationContext());
            if (battery != null && battery.isCharging()) {
                start(context);
            }
        }
        if (BuildConfig.DEBUG) {
            LogUtils.d("re= " + action);
        }
    }
}
