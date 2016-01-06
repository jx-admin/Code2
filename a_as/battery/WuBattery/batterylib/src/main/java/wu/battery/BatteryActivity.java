package wu.battery;

import android.content.Context;
import android.os.Bundle;

import wu.base.BaseActivity;
import wu.battery.model.Battery;
import wu.utils.LogUtils;

/**
 * Created by jx on 2015/12/23.
 */
public abstract class BatteryActivity extends BaseActivity implements IOnBatteryChangedListener {


    private BatteryHelper mBatteryHepler;

    public void start(Context context) {
        LogUtils.d("startView from batteryActivity");
        ChargeLockAccessor.onReceivePowerConnected(context, getPowerLockCallback(context));
    }

    protected abstract ChargeLockAccessor.PowerLockCallback getPowerLockCallback(Context context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBatteryHepler == null) {
            mBatteryHepler = new BatteryHelper();
            mBatteryHepler.create(this);
            mBatteryHepler.addOnBatteryChangedListener(this);
        } else {
            onBatteryChanged(mBatteryHepler.getBattery());
        }
    }

    @Override
    protected void onDestroy() {
        mBatteryHepler.destroy();
        mBatteryHepler.removeOnBatteryChangedListener(this);
        ChargeLockAccessor.onPowerLockDismiss(this);
        super.onDestroy();
    }


    public void onBatteryChanged(Battery battery) {

    }

    public void onPowerConnected() {

    }

    public void onPowerDisConnected() {

    }
}
