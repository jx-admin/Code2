
package com.mobilesdk.plugin;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mobilesdk.util.MsdkResponseModel;
import com.mobilesdk.view.MsdkInterface;
import com.mobilesdk.view.MsdkWebView;

/**
 * android android设备信息类
 * 
 * @author yang.c.li
 */
public class Device extends MsdkPlugin {
    // 设备型号
    public static final String MODEL = "model";

    // 系统平台
    public static final String PLATFORM = "platform";

    // 系统版本号
    public static final String VERSION = "version";

    // 运营商信息
    public static final String OPERATOR = "operator";

    TelephonyManager tm;

    BroadcastReceiver telephonyReceiver;

    public Device(MsdkInterface mInterface, MsdkWebView mWebView) {
        super(mInterface, mWebView);
        initTelephonyReceiver();
        tm = (TelephonyManager)mInterface.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        Log.d("Device", "Device插件初始化完毕");
    }

    /**
     * 注册权限
     */
    public void initTelephonyReceiver() {
        super.initTelephonyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        this.telephonyReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if ((intent == null)
                        || (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
                        || (!intent.hasExtra("state"))) {
                    return;
                }
            }
        };
        this.mInterface.getActivity().registerReceiver(this.telephonyReceiver, intentFilter);
    }

    /**
     * 获取设备名称
     * 
     * @param key
     * @param commandID
     * @return
     */
    public MsdkResponseModel getModelName(String key, String commandID) {
        MsdkResponseModel msdkModel = new MsdkResponseModel();
        String value = android.os.Build.MODEL;
        msdkModel = createResult(key, value, commandID);
        return msdkModel;
    }

    /**
     * 获取系统平台
     * 
     * @param key
     * @param commandID
     * @return
     */
    public MsdkResponseModel getPlatform(String key, String commandID) {
        MsdkResponseModel msdkModel = new MsdkResponseModel();
        String value = "Android";
        msdkModel = createResult(key, value, commandID);
        return msdkModel;
    }

    /**
     * 获取操 作系统版本
     * 
     * @param key
     * @param commandID
     * @return
     */
    public MsdkResponseModel getPlatformVersion(String key, String commandID) {
        MsdkResponseModel msdkModel = new MsdkResponseModel();
        String value = android.os.Build.VERSION.RELEASE;
        msdkModel = createResult(key, value, commandID);
        return msdkModel;
    }

    /**
     * 获取运营商信息
     * 
     * @param key
     * @param commandID
     * @return
     */
    public MsdkResponseModel getOperator(String key, String commandID) {
        MsdkResponseModel msdkModel = new MsdkResponseModel();
        String operator = tm.getSimOperator();
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                // 中国移动
                operator = "China Mobile";
            } else if (operator.equals("46001")) {
                // 中国联通
                operator = "China Unicom";
            } else if (operator.equals("46003")) {
                // 中国电信
                operator = "China Telecom";
            }
            msdkModel = createResult(key, operator, commandID);
        }
        return msdkModel;
    }
}
