
package com.accenture.mbank;



import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;

import com.accenture.mbank.util.Contants;



public class MobileBankApplication extends Application {
	
    public static Context applicationContext;

    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        handler = new Handler();
        Contants.userAgent = System.getProperty("http.agent");
        Contants.userAgent = Contants.userAgent + ";android";
        Contants.publicModel.setUserAgent(Contants.userAgent); 
        
        /*//将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/BAUHS93.ttf");*/
        try {
            PackageManager pm = getPackageManager();

            PackageInfo pinfo = pm.getPackageInfo(getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            int versionCode = pinfo.versionCode;
            String versionName = pinfo.versionName;
            Contants.Ver = versionName;
        } catch (NameNotFoundException e) {
        	Build bd = new Build();
        	String model = bd.MODEL;
        	Contants.publicModel.setUserAgent("I can't find userAgent!" + "android device:" + model + android.os.Build.VERSION.RELEASE); 
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
