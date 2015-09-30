
package com.act.mbanking.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class GeocodeResult {

    double lat;

    double lng;

    public void setValues(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray array = jsonObject.optJSONArray("results");
            if (array != null && array.length() > 0) {

                JSONObject address = array.optJSONObject(0);
                JSONObject geometry = address.optJSONObject("geometry");
                if (geometry != null) {

                    JSONObject location = geometry.optJSONObject("location");
                    if (location != null) {
                        lat = location.optDouble("lat");
                        lng = location.optDouble("lng");

                    }
                }

            }

            System.out.println(array.length() + " ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // JsonArray jsonArray=r
    }

    public GeoPoint getGeoPoint() {
        int newLat = (int)(lat * 1000000);
        int newLng = (int)(lng * 1000000);
        GeoPoint geoPoint = new GeoPoint(newLat, newLng);
        return geoPoint;
    }
}
