
package com.mobilesdk.test;

import android.content.Context;
import android.telephony.TelephonyManager;

public class Test2Extend {
    private TelephonyManager tm;

    public Test2Extend(Context cxt) {
        tm = (TelephonyManager)cxt.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public String getPhoneNumber() {
        return tm.getLine1Number().toString();
    }
}
