package wu.wubattery;

import android.content.Context;
import android.content.Intent;

import wu.battery.ChargeLockAccessor;
import wu.battery.PowerChangedReceiver;
import wu.utils.LogUtils;

/**
 * Created by jx on 2015/12/23.
 * 最好在Manifests里面注册 Intent.ACTION_POWER_CONNECTED
 */
public class MyPowerChangedReceiver extends PowerChangedReceiver {

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