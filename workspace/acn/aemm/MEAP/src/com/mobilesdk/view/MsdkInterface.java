
package com.mobilesdk.view;

import android.app.Activity;

public abstract interface MsdkInterface {
    /**
     * 可以通过这个接口拿到当前的activity
     * 
     * @return
     */
    public abstract Activity getActivity();
}
