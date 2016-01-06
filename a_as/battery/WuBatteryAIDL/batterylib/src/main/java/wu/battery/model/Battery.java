package wu.battery.model;

import android.os.BatteryManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import java.io.Serializable;

/**
 * Created by jx on 2015/12/23.
 */
public class Battery implements Parcelable, Serializable {

    /**
     * 未充电模式
     */
    public static final int CHARGING_MODEL_NONE = -1;
    /**
     * 快速充电模式
     */
    public static final int CHARGING_MODEL_QUICK = 0;
    /**
     * 正常充电模式
     */
    public static final int CHARGING_MODEL_NOMAL = 1;
    /**
     * 涓流充电模式
     */
    public static final int CHARGING_MODEL_SLOW = 2;
    /**
     * 充电完成模式
     */
    public static final int CHARGING_MODEL_DONE = 3;
    /**
     * 涓流充电时间，单位：毫秒
     */
    public static final long CHARGING_SLOW_TIME = 10 * 60 * 1000;
    public static final long SPEED_AC = (long) (1.3f * 60 * 1000);
    public static final long SPEED_USB = (long) (1.5f * 60 * 1000);
    public static final long SPEED_WIRELESS = 2 * 60 * 1000;
    public int level;
    public int scale;
    public int plugger;
    public int status;
    public int health;
    public int temperature;
    public String technology;
    public boolean present;
    public int voltage;

    /**
     * minute
     */
    public long remainTime;
    /**
     * 充电模式
     */
    public int chargingModel;

    public boolean isCharging() {
        return status == BatteryManager.BATTERY_STATUS_CHARGING || plugger == BatteryManager.BATTERY_PLUGGED_AC || plugger == BatteryManager.BATTERY_PLUGGED_USB || plugger == BatteryManager.BATTERY_PLUGGED_AC || BatteryManager.BATTERY_PLUGGED_WIRELESS == plugger;
    }

    public boolean isAC() {
        return BatteryManager.BATTERY_PLUGGED_AC == plugger;
    }

    public boolean isUSB() {
        return BatteryManager.BATTERY_PLUGGED_USB == plugger;
    }

    public boolean isWireLess() {
        return BatteryManager.BATTERY_PLUGGED_WIRELESS == plugger;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(plugger);
        dest.writeInt(level);
        dest.writeInt(scale);
        dest.writeInt(status);
        dest.writeLong(remainTime);
        dest.writeInt(chargingModel);
    }

    public void readFromParcel(Parcel in) {
        plugger = in.readInt();
        level = in.readInt();
        scale = in.readInt();
        status = in.readInt();
        remainTime = in.readLong();
        chargingModel = in.readInt();
    }

    public static final Creator<Battery> CREATOR = new Creator<Battery>() {
        public Battery createFromParcel(Parcel in) {
            return new Battery(in);
        }

        public Battery[] newArray(int size) {
            return new Battery[size];
        }
    };

    private Battery(Parcel in) {
        readFromParcel(in);
    }

    public Battery() {
    }

//    sb.append("开机时间：").append(TimeUtils.formatTimeDuration(SystemClock.elapsedRealtime())).append('\n');
//    sb.append("运行时间：").append(TimeUtils.formatTimeDuration(SystemClock.uptimeMillis()));
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append('[');
        float batteryPct = level / (float) scale;
        sb.append("status:").append(parseStatus(status)).append(',');
        sb.append("plugger:").append(parsePlugged(plugger)).append(',');
        sb.append("level:").append(level).append(',');
        sb.append("scale:").append(scale).append(',');
        sb.append("batteryPct:").append(batteryPct).append(',');
        sb.append("health:").append(parseHealth(health)).append(',');
        sb.append("temperature:").append(temperature).append("℃,");
        sb.append("technology:").append(technology).append(',');
        sb.append("电池独立:").append(present).append(',');
        sb.append("voltage:").append(voltage).append("mV,");
        sb.append("chargingModel:").append(chargingModel).append(',');
        sb.append("remainTime:").append(remainTime).append(']');
        return sb.toString();
    }

    /**
     * 充电方式
     *
     * @param plugged
     * @return
     */
    public static final String parsePlugged(int plugged) {
        String name;
        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                name = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                name = "USB";
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                name = "无线";
                break;
            default:
                name = "未插电";
        }
        return name;
    }

    /**
     * 电池状态，返回是一个数字
     * BatteryManager.BATTERY_STATUS_CHARGING 表示是充电状态
     * BatteryManager.BATTERY_STATUS_DISCHARGING 放电中
     * BatteryManager.BATTERY_STATUS_NOT_CHARGING 未充电
     * BatteryManager.BATTERY_STATUS_FULL 电池满
     *
     * @param status
     * @return
     */
    public static final String parseStatus(int status) {

        String name = null;
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                name = "充电状态";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                name = "放电中";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                name = "未充电";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                name = "电池满";
                break;
        }
        return name;
    }


    /**
     * 电池健康情况，返回也是一个数字
     * BatteryManager.BATTERY_HEALTH_GOOD 良好
     * BatteryManager.BATTERY_HEALTH_OVERHEAT 过热
     * BatteryManager.BATTERY_HEALTH_DEAD 没电
     * BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE 过电压
     * BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE 未知错误
     *
     * @param health
     * @return
     */
    public static final String parseHealth(int health) {
        String name = null;
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                name = "良好";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                name = "过热";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                name = "没电";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                name = "过电压";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                name = "未知错误";
                break;
        }
        return name;
    }
}
