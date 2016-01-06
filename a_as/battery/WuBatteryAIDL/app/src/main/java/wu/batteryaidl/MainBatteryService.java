package wu.batteryaidl;

import android.content.Context;
import android.content.Intent;

import wu.battery.ChargeLockAccessor;
import wu.utils.LogUtils;

/**
 * Created by jx on 2015/12/23.
 */
public class MainBatteryService extends BatteryService {
    @Override
    public void onPowerConnected() {
        super.onPowerConnected();
    }

    @Override
    public void onPowerDisConnected() {
        super.onPowerDisConnected();

    }

    @Override
    protected ChargeLockAccessor.PowerLockCallback getPowerLockCallback(final Context context) {
        return new ChargeLockAccessor.PowerLockCallback() {
            @Override
            public boolean disabled() {
                LogUtils.d("startView disabled");
                return false;
            }

            @Override
            public void startPowerLock() {
                LogUtils.d("startView startPowerLock");
                Intent i = new Intent(ChargeLockAccessor.HOLA_POWER_MANAGER_ACTION);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        };
    }
}
