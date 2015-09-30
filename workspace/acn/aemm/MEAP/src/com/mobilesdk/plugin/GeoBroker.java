
package com.mobilesdk.plugin;

import android.util.Log;

import com.mobilesdk.view.MsdkInterface;
import com.mobilesdk.view.MsdkWebView;

public class GeoBroker extends MsdkPlugin {

    public GeoBroker(MsdkInterface mInterface, MsdkWebView mWebView) {
        super(mInterface, mWebView);
        initTelephonyReceiver();
        Log.d("GeoBroker", "GeoBroker插件初始化完毕");
    }

}
