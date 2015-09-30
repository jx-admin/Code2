
package com.accenture.mbank;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.accenture.mbank.view.GPSListener;

public class BaseMapActivity extends FragmentActivity {
    protected View back;
    private LocationManager locationManager;
	private GPSListener listener;
	
    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);

        back = (View)findViewById(R.id.back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }

    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected String getResourceString(int id) {
        String str = getResources().getString(id);
        return str;

    }
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter();  
        filter.addAction(LOGOUT);  
        registerReceiver(this.broadcastReceiver, filter); // 注册  
	}
    
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {  

        @Override 

        public void onReceive(Context context, Intent intent) {  

            close();  

            unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错  

        }  

    };  
    public static final String LOGOUT="logout_bper";
    public void close() {  
        finish();  
    }

    public Location getLocation() {
		Location location = getLastKnownLocation();
		
		if(location==null) {
			location = new Location(LocationManager.PASSIVE_PROVIDER);
			location.setLatitude(0);
			location.setLongitude(0);
		}

		return location;
	}
    
    

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		this.listener = new GPSListener(locationManager);
	}

	private Location getLastKnownLocation() {
		locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
		locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null);
		locationManager.requestSingleUpdate(LocationManager.PASSIVE_PROVIDER, listener, null);
		
		List<String> providers = locationManager.getProviders(true);

		/*
		 * Loop over the array backwards, and if you get an accurate location,
		 * then break out the loop
		 */
		Location location = null;
		for (int i=providers.size()-1; i>=0; i--) {
			location = locationManager.getLastKnownLocation(providers.get(i));
			if (location != null)
				break;
		}
		return location;
	}
}
