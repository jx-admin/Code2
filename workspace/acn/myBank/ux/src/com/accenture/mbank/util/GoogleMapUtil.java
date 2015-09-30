
package com.accenture.mbank.util;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.maps.GeoPoint;

/**
 * @author seekting.x.zhang
 */
public class GoogleMapUtil {

    public static final int maxResult = 20;

    /**
     * @param context
     * @param locale if this param==null ,Locale.US will be used
     * @param city the city name such as:"Los Angeles"
     * @return
     */
    public static List<Address> getAddressByCityName(Context context, Locale locale, String city) {
        if (locale == null) {
            locale = Locale.US;
        }
        Geocoder geo = new Geocoder(context, locale);
        List<Address> addresses = null;
//        google.maps.Geocoder
        try {

            addresses = geo.getFromLocationName(city, 10);
            // geo.getFromLocationName(locationName, maxResults,
            // lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude,
            // upperRightLongitude)

        } catch (IOException e) {

            e.printStackTrace();
        }

        if (addresses == null || addresses.size() == 0) {
            LogManager.d("getAddressByCityName()" + "return 0");
        }
        return addresses;

    }

    public static List<Address> getAddressByHospitalName(Context context, Locale locale, String city) {
        if (locale == null) {
            locale = Locale.US;
        }
        Geocoder geo = new Geocoder(context, locale);
        List<Address> addresses = null;
        try {

            addresses = geo.getFromLocationName(city, 1);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return addresses;

    }

    /**
     * address converted to geopoint
     * 
     * @param address
     * @return
     */
    public static GeoPoint getGeoPointByAddress(Address address) {
        if (address != null) {
            GeoPoint geoPoint = new GeoPoint(

            (int)(address.getLatitude() * 1000000),

            (int)(address.getLongitude() * 1000000));
            return geoPoint;
        }
        return null;
    }

    /**
     * @param address
     * @return
     */
    public static String getDescriptionByAddress(Address address) {

        StringBuffer stringBuffer = new StringBuffer();
        String space = " , ";

        if (address == null) {
            return stringBuffer.toString();
        }
        String featureName = address.getFeatureName();
        if (featureName != null) {
            stringBuffer.append(featureName);
            stringBuffer.append(space);
        }
        String adminArea = address.getAdminArea();
        if (adminArea != null) {
            stringBuffer.append(adminArea);
            stringBuffer.append(space);
        }
        String postcode = address.getPostalCode();
        if (postcode != null) {
            stringBuffer.append(postcode);
            stringBuffer.append(space);
        }
        stringBuffer.append("La:" + address.getLatitude());
        stringBuffer.append(space);
        stringBuffer.append("Lo:" + address.getLongitude());

        return stringBuffer.toString();
    }
}
