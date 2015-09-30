
package com.act.mbanking.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.act.mbanking.utils.DialogManager;
import com.act.mbanking.utils.LogManager;
import com.google.android.maps.MapActivity;

public class BaseActivity extends MapActivity {

    public static int screen_width_orientation = 0;

    public static int screen_height_orientation = 0;

    public static int screen_width = 0;

    public static int screen_height = 0;

    protected Handler baseHandler;
    
    protected boolean isVertical = true;

    public static final int ADV_NEWS = 0x00;

    public static final int NATIFICATION = 0x01;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogManager.d(getClass().getSimpleName() + "onCreate() invoked!!");
        WindowManager windowManager = getWindowManager();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        screen_width_orientation = windowManager.getDefaultDisplay().getWidth();
        screen_height_orientation = windowManager.getDefaultDisplay().getHeight();
        screen_width = Math.min(windowManager.getDefaultDisplay().getWidth(), windowManager
                .getDefaultDisplay().getHeight());
        screen_height = Math.max(windowManager.getDefaultDisplay().getWidth(), windowManager
                .getDefaultDisplay().getHeight());
        baseHandler = new Handler();
        isVertical = windowManager.getDefaultDisplay().getWidth() < windowManager
                .getDefaultDisplay().getHeight();
    }

    /**
     * 通过Dialog来显示提示信息
     * 
     * @param errordec
     */
    public void displayErrorMessage(final String errordec) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogManager.createMessageDialog(errordec, BaseActivity.this).show();
            }
        });
    }

    /**
     * 通过Toast 来显示提示信息
     * 
     * @param errordec
     */
    public void displayToastMessage(final String errordec) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, errordec, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 设置是否能转屏
     * 
     * @param flag
     */
    public void setCanOrientation(boolean flag) {
        if (flag) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        WindowManager windowManager = getWindowManager();
        screen_width_orientation = windowManager.getDefaultDisplay().getWidth();
        screen_height_orientation = windowManager.getDefaultDisplay().getHeight();
        isVertical = windowManager.getDefaultDisplay().getWidth() < windowManager
                .getDefaultDisplay().getHeight();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        LogManager.d(this + "onDestroy");
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
