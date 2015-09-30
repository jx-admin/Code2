
package com.accenture.mbank;



import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;

import com.accenture.mbank.util.Contants;
import com.accenture.mbank.view.payment.PaymentsManager;



public class MobileBankApplication extends Application {
	
    public static Context applicationContext;

    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        handler = new Handler();

        try {
            PackageManager pm = getPackageManager();

            PackageInfo pinfo = pm.getPackageInfo(getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            int versionCode = pinfo.versionCode;
            String versionName = pinfo.versionName;
            Contants.Ver = versionName;
        } catch (NameNotFoundException e) {
        }
    }

    /**
     * 注销
     */
    public static void logOut() {

       // PaymentsManager.onDestroyed();
        Contants.clearAll();

    }
}
