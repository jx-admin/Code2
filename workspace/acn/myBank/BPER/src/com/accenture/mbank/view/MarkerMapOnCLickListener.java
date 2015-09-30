package com.accenture.mbank.view;

import android.app.Activity;
import android.view.View;

import com.accenture.mbank.ContactSearchActivity;
import com.accenture.mbank.MainActivity;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

public class MarkerMapOnCLickListener implements OnMarkerClickListener{

	private Activity activity;
	
	private Marker marker;
	
	public Marker getMarker() {
		return marker;
	}
	public void setMarker(Marker marker) {
		this.marker = marker;
	}
	
	public MarkerMapOnCLickListener(Activity activity){
		this.activity = activity;
	}
	@Override
	public boolean onMarkerClick(final Marker marker) {
		
		if (marker.equals(this.marker)) {
			return false;
		} else {
			if(activity instanceof ContactSearchActivity){
				View searchPoint = ((ContactSearchActivity)(this.activity)).getSearchPoint();
				((ContactSearchActivity)(this.activity)).setPopupVisible(true);
				searchPoint.setVisibility(View.GONE);
			} else if(activity instanceof MainActivity){
				View searchPoint = ((MainActivity)(this.activity)).getSearchPoint();
				((MainActivity)(this.activity)).setPopupVisible(true);
				searchPoint.setVisibility(View.GONE);
			}
			marker.showInfoWindow();
			return false;
		}
	}
	 
}
