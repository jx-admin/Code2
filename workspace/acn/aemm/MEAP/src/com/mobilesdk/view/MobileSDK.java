
package com.mobilesdk.view;

import android.os.Handler;
import android.util.Log;

import com.mobilesdk.plugin.Device;
import com.mobilesdk.util.ActionType;
import com.mobilesdk.util.JSONHelper;
import com.mobilesdk.util.MsdkResponseModel;

/**
 * @author yang.c.li
 */
public class MobileSDK {
    MsdkInterface mInterface;

    private MsdkWebView mWebView;

    private int actionType;

    private final String TAG = "MobileSDK";

    private Device deviceInfo;

    private Handler mHandler;

    public MobileSDK(MsdkInterface mInterface, MsdkWebView mWebView) {
        this.mInterface = mInterface;
        this.mWebView = mWebView;
        mHandler = new Handler();
        deviceInfo = new Device(mInterface, mWebView);
    }

    /**
     * @param commandID
     * @param action
     * @param param
     */
    public void execNative(String commandID, String action, String param) {
        MsdkResponseModel msdkResponseModel = new MsdkResponseModel();
        actionType = Integer.parseInt(action);
        Log.d(TAG, "actionType ======> " + actionType);
        Log.d(TAG, "commandID ======> " + commandID);
        try {
            String key = ActionType.getActionKey(actionType); // 通过actionType查找到相对应的key
            switch (actionType) {
                case ActionType.MODELNAME:
                    msdkResponseModel = deviceInfo.getModelName(key, commandID);
                    break;
                case ActionType.PLATFORM:
                    msdkResponseModel = deviceInfo.getPlatform(key, commandID);
                    break;
                case ActionType.PLATFORMVERSION:
                    msdkResponseModel = deviceInfo.getPlatformVersion(key, commandID);
                    break;
                case ActionType.SIMCARRIERNAME:
                    msdkResponseModel = deviceInfo.getOperator(key, commandID);
                    break;
                default:
                    break;
            }
            String value = JSONHelper.toJSON(msdkResponseModel.getValue()); // 将对象序列化成json格式的字符串
            msdkResponseModel.setResponse(value);
            reloadUrl(msdkResponseModel.getCommandID(), msdkResponseModel.getCode(),
                    msdkResponseModel.getResponse());
        } catch (Throwable throwable) {
            throwable.getLocalizedMessage();
        }
    }

    /**
     * @param value
     */
    public void reloadUrl(final String commnadID, final int code, final String response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:callbackHandler(" + commnadID + "," + code + ","
                        + response + ");");
            }
        });
    }
}
