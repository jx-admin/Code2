
package com.seekting;

import android.app.Application;

import com.seekting.util.LOGManager;

public class SeektingApplication extends Application {

    @Override
    public void onCreate() {
        
        super.onCreate();
        LOGManager.d(getClass().getSimpleName() + ".onCreate() invoked!!");

    }

}
