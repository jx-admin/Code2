
package com.accenture.mbank.util;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class LocationInfo {
    private double mLatitude;

    private double mLongitude;
    
    private Context mContext;

    public LocationInfo(Context context) {
        mContext = context;
        GetLocationInfo();
    }

    private void GetLocationInfo() {
        // 获取到LocationManager对象
        LocationManager locationManager = (LocationManager)mContext.getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        // 创建一个Criteria对象
        Criteria criteria = new Criteria();
        // 设置粗略精确度
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        // 设置是否需要返回海拔信息
        criteria.setAltitudeRequired(false);
        // 设置是否需要返回方位信息
        criteria.setBearingRequired(false);
        // 设置是否允许付费服务
        criteria.setCostAllowed(true);
        // 设置电量消耗等级
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        // 设置是否需要返回速度信息
        criteria.setSpeedRequired(false);
        // 根据设置的Criteria对象，获取最符合此标准的provider对象
        String currentProvider = locationManager.getBestProvider(criteria, true);
        if (currentProvider == null) {
            List<String> list = locationManager.getProviders(true);
            if (list.size() > 0)
                currentProvider = list.get(0);

            if (currentProvider == null)
                return;
        }
        LogManager.d("LocationInfo currentProvider: " + currentProvider);
        // 根据当前provider对象获取最后一次位置信息

        Location currentLocation = locationManager.getLastKnownLocation(currentProvider);
        // 如果位置信息为null，则请求更新位置信息
        if (currentLocation != null) {
            mLatitude = currentLocation.getLatitude();
            mLongitude = currentLocation.getLongitude();
        }
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getTrackLocationData() {
        if (mLongitude > 0 && mLatitude > 0)
            return mLongitude + "," + mLatitude;
        return "";
    }

}
