package com.aess.aemm.update;

import com.aess.aemm.function.SingleProfessionalFunction;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}

	@Override
	public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new UpdateAdapter(getApplicationContext(), true);
            }
        }
		super.onCreate();
		
		//start professional function service for prohibiting enterprise or no-enterprise application.
		SingleProfessionalFunction.GetProfessionalFunction(getApplicationContext()).beginService();
	}
	
	public void onDestry(){
		//stop professional function service
		SingleProfessionalFunction.GetProfessionalFunction(getApplicationContext()).stopService();
		super.onDestroy();
	}
	
    private static final Object sSyncAdapterLock = new Object();
    private static UpdateAdapter sSyncAdapter = null;
}
