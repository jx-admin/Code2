
package com.accenture.mbank;

import java.util.List;

import it.gruppobper.ams.android.bper.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.accenture.mbank.util.Contants;
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
		showBankLogo();
		
		IntentFilter filter = new IntentFilter();  
        filter.addAction(LOGOUT);  
        registerReceiver(this.broadcastReceiver, filter); // 注册  
	}

    protected void showBankLogo() {
		ImageButton btn = (ImageButton) findViewById(R.id.bank_logo);
		if (btn == null)
			return;

		final SharedPreferences settings = this.getSharedPreferences(
				Contants.SETTING_FILE_NAME, MODE_PRIVATE);
		String strBankCode = settings.getString(Contants.BANK_CODE, "");

		for (int i = 0; i < Contants.idBankButton.length; i++) {
			if (strBankCode.equals(Contants.strBankCode[i])) {
				btn.setImageDrawable(getResources().getDrawable(
						Contants.idBankLogo[i]));
				break;
			}
		}
	}
	
	Handler mHandler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		setMenuVisibility(msg.what);
    	};
    };
    
    void setMenuVisibility(int visibility){
    	if(visibility==View.VISIBLE){
    		findViewById(R.id.menu).setVisibility(visibility);
    		findViewById(R.id.menu_highter).setVisibility(View.INVISIBLE);
//    		Toast.makeText(this, "BaseMapActivity keyboard hide & menu show", Toast.LENGTH_SHORT).show();
    	}else{
    		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        		findViewById(R.id.menu).setVisibility(visibility);
    			findViewById(R.id.menu_highter).setVisibility(visibility);
//    			Toast.makeText(this, "BaseMapActivity keyboard show & menu hide", Toast.LENGTH_SHORT).show();
    		}
    	}
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
