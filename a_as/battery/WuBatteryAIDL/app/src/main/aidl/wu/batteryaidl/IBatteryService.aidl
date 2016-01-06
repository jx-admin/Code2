// IBattery.aidl
package wu.batteryaidl;
import wu.batteryaidl.IOnBatteryChangedListener;
import wu.battery.model.Battery;

// Declare any non-default types here with import statements

/**
 * Created by jx on 2015/12/23.
 */
interface IBatteryService {

    void addOnBatteryChangedListener(IOnBatteryChangedListener onBatteryChangedListener);

    void removeOnBatteryChangedListener(IOnBatteryChangedListener onBatteryChangedListener);

    Battery getBattery();
}
