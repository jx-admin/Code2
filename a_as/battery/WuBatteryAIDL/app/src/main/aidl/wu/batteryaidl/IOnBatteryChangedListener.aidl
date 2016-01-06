// IOnBatteryChangedListener.aidl
package wu.batteryaidl;
import wu.battery.model.Battery;

// Declare any non-default types here with import statements

/**
 * Created by jx on 2015/12/23.
 */
interface IOnBatteryChangedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onBatteryChanged(out Battery battery);
    void onPowerConnected();
    void onPowerDisConnected();
}
