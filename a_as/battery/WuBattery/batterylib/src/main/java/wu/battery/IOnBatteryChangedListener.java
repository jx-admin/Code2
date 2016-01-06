package wu.battery;


import wu.battery.model.Battery;

/**
 * Created by jx on 2015/12/29.
 */
public interface IOnBatteryChangedListener {
    void onBatteryChanged(Battery battery);

    void onPowerConnected();

    void onPowerDisConnected();
}
