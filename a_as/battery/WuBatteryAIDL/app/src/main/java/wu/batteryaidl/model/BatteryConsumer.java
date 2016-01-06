package wu.batteryaidl.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by jx on 2015/12/23.
 */
public class BatteryConsumer implements Parcelable, Serializable {
    public static final int TYPE_ALL = 0;
    public static final int TYPE_PHONE = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_USB = 3;
    public static final int TYPE_AC = 4;
    public static final int TYPE_WARELESS = 5;
    public static final int TYPE_DISCHARGING = 6;
    private static float DEF_AVERAGE = 1;
    private int type;
    private long totalTime;//
    private long totalCost;
    private transient long lastTime;
    private transient long lastLevel;
    private transient long startTime;
    private transient long startLevel;
    private transient boolean isStart;

    public BatteryConsumer(int type) {
        this.type = type;
    }

    public void init(long totalTime, long totalCost) {
        this.totalTime = totalTime;
        this.totalCost = totalCost;
    }

    public void add(long time, long level) {
        if (level == lastLevel) {
            return;
        }
        lastTime = time;
        lastLevel = level;
        if (startTime == 0 || 0 == startLevel) {
            //首次得到电量和时间，不可用用于计算，因为这并不是充电节点上
            startTime = time;
            startLevel = level;
            return;
        }
        if (!isStart) {
            isStart = true;
            //得到变化的开始
            startTime = time;
            startLevel = level;
        }
    }

    public void onStop() {
        if (isStart && lastTime > startTime && lastLevel != startLevel) {
            totalTime += lastTime - startTime;
            totalCost += lastLevel - startLevel;
        }
    }

    /**
     * 最后一次统计的单位电量平均变化时间
     *
     * @return Millis
     */
    public float getAverageTime() {
        if (isStart && lastTime > startTime && lastLevel != startLevel) {
            return (lastTime - startTime) / (lastLevel - startLevel);
        }
        return 0;
    }

    /**
     * 总的统计：单位电量平均变化时间
     *
     * @return Millis
     */
    public float getTotalAverageTime() {
        if (totalTime > 0 && totalCost > 0) {
            return totalTime / totalCost;
        }
        return 0;
    }

    /**
     * 默认的：单位电量平均变化时间
     *
     * @return Millis
     */
    public float getDefaultAverageTime() {
        return DEF_AVERAGE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append('[');
        sb.append("type:").append(type).append(',');
        sb.append("totalTime:").append(totalTime).append(',');
        sb.append("totalCost:").append(totalCost).append(',');
        sb.append("lastTime:").append(lastTime).append(',');
        sb.append("lastLevel:").append(lastLevel).append(',');
        sb.append("startTime:").append(startTime).append(',');
        sb.append("startLevel:").append(startLevel).append(']');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeLong(totalTime);
        dest.writeLong(totalCost);
        dest.writeLong(lastTime);
        dest.writeLong(lastLevel);
        dest.writeLong(startTime);
        dest.writeLong(startLevel);
        dest.writeByte((byte) (isStart ? 1 : 0));
    }

    public void readFromParcel(Parcel in) {
        type = in.readInt();
        totalTime = in.readLong();
        totalCost = in.readLong();
        lastTime = in.readLong();
        lastLevel = in.readLong();
        startTime = in.readLong();
        startLevel = in.readLong();
        isStart = in.readByte() != 0;
    }

    public static final Creator<BatteryConsumer> CREATOR = new Creator<BatteryConsumer>() {
        public BatteryConsumer createFromParcel(Parcel in) {
            return new BatteryConsumer(in);
        }

        public BatteryConsumer[] newArray(int size) {
            return new BatteryConsumer[size];
        }
    };

    public BatteryConsumer(Parcel in) {
        readFromParcel(in);
    }
}
