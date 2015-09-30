
package com.act.mbanking.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.act.mbanking.R;
import com.act.mbanking.activity.DirectionDetailActivity;
import com.act.mbanking.bean.BranchListModel;
import com.act.mbanking.utils.LogManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MapItemizedOverlay extends ItemizedOverlay implements OnClickListener {

    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

    boolean showPop = true;

    private MapView mMapView;

    private PopView mPopView;

    private MapLayout mMapLayout;

    private MapRouteOverlay mRouteOverlay = null;

    View getDirectionBtn;

    Context context;

    public MapItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
        populate();
    }

    public MapItemizedOverlay(Drawable defaultMarker, MapLayout aMapFragment) {
        super(boundCenterBottom(defaultMarker));
        mMapLayout = aMapFragment;
        mMapView = mMapLayout.getMapView();

        mPopView = (PopView)mMapLayout.getPopView();
        getDirectionBtn = mPopView.findViewById(R.id.getdirections);
        context = mMapLayout.getContext();
        populate();

    }

    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }

    @Override
    public int size() {
        return mOverlays.size();
    }

    public void clearOverlay() {
        mOverlays.clear();
        setLastFocusedIndex(-1);
        populate();
    }

    public void populateItem() {
        setLastFocusedIndex(-1);
        populate();
    }

    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        setLastFocusedIndex(-1);
        populate();
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView) {
        LogManager.d("size-onTap1" + +this.size() + this);
        mPopView.setVisibility(View.GONE);
        return super.onTap(p, mapView);
    }

    @Override
    protected boolean onTap(int index) {
        LogManager.d("size-onTap2" + this.size() + this);
        if (!showPop) {
            return true;
        }
        OverlayItem item = mOverlays.get(index);

        GeoPoint pt = item.getPoint();

        int width = mMapLayout.getWidth();
        int height = mMapLayout.getHeight();
        int popWidth = 2 * width / 3;
        int popHeight = 2 * height / 3;
        MapView.LayoutParams layoutParams = new MapView.LayoutParams(2 * width / 3, popHeight, pt,
                MapView.LayoutParams.BOTTOM);
        mMapView.updateViewLayout(mPopView, layoutParams);
        float left = (popWidth * 100) / 479;
        float bottom = (popHeight * 20) / 274;

        System.out.println("left:" + left);
        System.out.println("bottom:" + bottom);
        getDirectionBtn.setTag(item);
        getDirectionBtn.setOnClickListener(this);
        layoutParams.x = (int)-left;
        layoutParams.y = (int)-bottom;
        showDetail(item);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.05f, 0.1f, 1.05f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);

        mPopView.startAnimation(scaleAnimation);

        return true;
    }

    private void showDetail(OverlayItem item) {

        if (item instanceof BankOverlayItem) {
            BankOverlayItem bankOverlayItem = (BankOverlayItem)item;
            BranchListModel branchListModel = bankOverlayItem.getBrankBranchListModel();
            mPopView.setBranchListModel(branchListModel);
            mPopView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        GeoPoint start = mMapLayout.searchGeoPoint;
        OverlayItem item = (OverlayItem)v.getTag();

        GeoPoint end = item.getPoint();
        if (MapLayout.debugChinaDevice) {
            int latitude = (int)(44.648837 * 1e6);
            int longitude = (int)(10.920087000000001 * 1e6);
            start = new GeoPoint(latitude, longitude);
        }

        Intent intent = new Intent(context, DirectionDetailActivity.class);
        intent.putExtra("start-latitude", start.getLatitudeE6());
        intent.putExtra("start-longtitude", start.getLongitudeE6());

        intent.putExtra("end-latitude", end.getLatitudeE6());
        intent.putExtra("end-longtitude", end.getLongitudeE6());

        context.startActivity(intent);
        // showRoute(start, end);

    }

    private void showRoute(GeoPoint start, GeoPoint end) {
        mRouteOverlay = new MapRouteOverlay(context, mMapView, start, end);
        mRouteOverlay.CalculateRoute();
        mMapView.getOverlays().add(mRouteOverlay);
        mMapView.invalidate();
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
