package com.android.accenture.aemm.express;

//package com.terry.device;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class deviceAdminReceiver extends DeviceAdminReceiver {

    /**
     * 获取设备存储的数值
     * 
     * @param context
     * @return
     */
    public static SharedPreferences getDevicePreference(Context context) {
        return context.getSharedPreferences(
                DeviceAdminReceiver.class.getName(), 0);
    }

    // 密码的特点
    public static String PREF_PASSWORD_QUALITY = "password_quality";
    // 密码的长度
    public static String PREF_PASSWORD_LENGTH = "password_length";

    public static String PREF_MAX_FAILED_PW = "max_failed_pw";

    void showToast(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
    	//CharSequence str = context.getText(R.string.device_showToast_onEnabled);
        //showToast(context, str);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
    	//CharSequence str = context.getText(R.string.device_showToast_onDisabled);
        //showToast(context, str);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub
    	CharSequence str = context.getText(R.string.device_showToast_onDisableRequested);
        return str;
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        // TODO Auto-generated method stub
    	//CharSequence str = context.getText(R.string.device_showToast_onPasswordChanged);
        //showToast(context, str);
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        // TODO Auto-generated method stub
    	//CharSequence str = context.getText(R.string.device_showToast_onPasswordFailed);
        //showToast(context, str);
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        // TODO Auto-generated method stub
    	//CharSequence str = context.getText(R.string.device_showToast_onPasswordSucceeded);
        //showToast(context, str);
    }

}
