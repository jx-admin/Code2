package com.act.mbanking.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class DeviceAdminMyBankingReceiver extends DeviceAdminReceiver {

    private static final String TAG = "DeviceAdminManager";
    private static final long MS_PER_DAY = 86400 * 1000;
    private static final long MS_PER_HOUR = 3600 * 1000;
    private static final long MS_PER_MINUTE = 60 * 1000;
    


    static String PREF_PASSWORD_QUALITY = "password_quality";
    static String PREF_PASSWORD_LENGTH = "password_length";
    static String PREF_PASSWORD_MINIMUM_LETTERS = "password_minimum_letters";
    static String PREF_PASSWORD_MINIMUM_UPPERCASE = "password_minimum_uppercase";
    static String PREF_PASSWORD_MINIMUM_LOWERCASE = "password_minimum_lowercase";
    static String PREF_PASSWORD_MINIMUM_NUMERIC = "password_minimum_numeric";
    static String PREF_PASSWORD_MINIMUM_SYMBOLS = "password_minimum_symbols";
    static String PREF_PASSWORD_MINIMUM_NONLETTER = "password_minimum_nonletter";
    static String PREF_PASSWORD_HISTORY_LENGTH = "password_history_length";
    static String PREF_PASSWORD_EXPIRATION_TIMEOUT = "password_expiration_timeout";
    static String PREF_MAX_FAILED_PW = "max_failed_pw";
	
	void showToast(Context context, CharSequence msg) {
//		Log.d(TAG,"Device Admin: "+msg);
//        Toast.makeText(context, "Sample Device Admin: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "enabled");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "This is an optional message to warn the user about disabling.";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "disabled");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        showToast(context, "pw changed");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        showToast(context, "pw failed");
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        showToast(context, "pw succeeded");
    }

    static String countdownString(long time) {
        long days = time / MS_PER_DAY;
        long hours = (time / MS_PER_HOUR) % 24;
        long minutes = (time / MS_PER_MINUTE) % 60;
        return days + "d" + hours + "h" + minutes + "m";
    }

    @Override
    public void onPasswordExpiring(Context context, Intent intent) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
//        long expr = dpm.getPasswordExpiration(new ComponentName(context, DeviceAdminManager.class));
//        long delta = expr - System.currentTimeMillis();
//        boolean expired = delta < 0L;
//        String msg = expired ? "Password expired " : "Password will expire "
//                + countdownString(Math.abs(delta))
//                + (expired ? " ago" : " from now");
//        showToast(context, msg);
//        Log.v(TAG, msg);
    }

}
