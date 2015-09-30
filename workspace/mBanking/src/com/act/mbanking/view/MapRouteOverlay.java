
package com.act.mbanking.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MapRouteOverlay extends Overlay {

    private List<GeoPoint> mPoints = null;

    private GeoPoint mStartPoint;

    private GeoPoint mEndPoint;

    private Context mContext;

    private MapView mMapView;

    public MapRouteOverlay(Context context, MapView aMapView, GeoPoint aStartPoint,
            GeoPoint aEndPoint) {
        Log.e("RouteOverlay", "Construct");
        mContext = context;
        mStartPoint = aStartPoint;
        mEndPoint = aEndPoint;
        mMapView = aMapView;
    }

    public void CalculateRoute() {
        ProgressOverlay overlay = new ProgressOverlay(mContext);
        overlay.show("Calculating Route", new OnProgressEvent() {
            public void onProgress() {
                mPoints = CalculateRoutePoints(mStartPoint, mEndPoint);
                mMapView.postInvalidate();
            }
        });
    }

    @Override
    public void draw(Canvas aCanvas, MapView aMapView, boolean val) {
        // TODO Auto-generated method stub
        Log.e("RouteOverlay", "Draw");
        super.draw(aCanvas, aMapView, val);
        if (mPoints != null) {
            Log.e("RouteOverlay", "Draw Route");
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setDither(true);
            paint.setAntiAlias(true);
            paint.setAlpha(50);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(10);
            Projection projection = aMapView.getProjection();
            Path path = new Path();
            GeoPoint routeStartGeoPoint = mPoints.get(0);
            Point routeStartPoint = new Point();
            projection.toPixels(routeStartGeoPoint, routeStartPoint);
            path.moveTo(routeStartPoint.x, routeStartPoint.y);

            Point routePoint = new Point();
            Iterator<GeoPoint> it = mPoints.iterator();
            while (it.hasNext()) {
                GeoPoint routeGeoPoint = it.next();
                projection.toPixels(routeGeoPoint, routePoint);
                path.lineTo(routePoint.x, routePoint.y);
            }
            aCanvas.drawPath(path, paint);
        }
    }

    public List<GeoPoint> CalculateRoutePoints(GeoPoint aStartPoint, GeoPoint aEndPoint) {
        // TODO Auto-generated method stub

        // Get Points from Google direction API
        // "http://maps.google.com/maps/api/directions/xml?origin=39.928774,116.460801&destination=39.918774,116.458801&sensor=false&mode=walking";
        String url = "http://maps.google.com/maps/api/directions/xml?origin="
                + aStartPoint.getLatitudeE6() / 1e6 + "," + aStartPoint.getLongitudeE6() / 1e6
                + "&destination=" + aEndPoint.getLatitudeE6() / 1e6 + ","
                + aEndPoint.getLongitudeE6() / 1e6 + "&sensor=false";
        Log.e("Route Overlay", "URL:" + url);

        HttpGet get = new HttpGet(url);
        String strResult = "";
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);

            HttpResponse httpResponse = null;
            httpResponse = httpClient.execute(get);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (Exception e) {
            Log.d("Route Overlay", "Exception");
            return null;
        }

        if (-1 == strResult.indexOf("<status>OK</status>")) {
            Log.e("Route Overlay", "Direction API Error");
            return null;
        }

        int pos = strResult.indexOf("<overview_polyline>");
        pos = strResult.indexOf("<points>", pos + 1);
        int pos2 = strResult.indexOf("</points>", pos);
        strResult = strResult.substring(pos + 8, pos2);

        return decodePoly(strResult);
    }

    // Decode poly data
    private List<GeoPoint> decodePoly(String encoded) {
        List<GeoPoint> poly = new ArrayList<GeoPoint>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            GeoPoint p = new GeoPoint((int)(((double)lat / 1E5) * 1E6),
                    (int)(((double)lng / 1E5) * 1E6));
            poly.add(p);
        }
        return poly;
    }

}
