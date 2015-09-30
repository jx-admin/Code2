
package com.mobilesdk.plugin;

import java.util.HashMap;
import java.util.Map;

import com.mobilesdk.util.MsdkResponseModel;
import com.mobilesdk.view.MsdkInterface;
import com.mobilesdk.view.MsdkWebView;

public class MsdkPlugin {

    MsdkInterface mInterface;

    MsdkWebView mWebView;

    public static final int SUCCESS = 0;

    public static final int FAIL = 1;

    public MsdkPlugin(MsdkInterface mInterface, MsdkWebView mWebView) {
        this.mInterface = mInterface;
        this.mWebView = mWebView;
    }

    public void initTelephonyReceiver() {

    }

    /**
     * 生成一个对象返回
     * 
     * @param key
     * @param value
     * @param commandID
     * @return
     */
    public MsdkResponseModel createResult(String key, String value, String commandID) {
        MsdkResponseModel msdkModel = new MsdkResponseModel();
        if (value != null && !value.equals("")) {
            Map<String, String> dictionary = createDictonary(key, value);
            if (dictionary.size() != 0 && dictionary != null) {
                msdkModel.setValue(dictionary);
                msdkModel.setCode(SUCCESS);
            }
        } else {
            msdkModel.setCode(FAIL);
        }
        msdkModel.setCommandID(commandID);
        return msdkModel;
    }

    /**
     * 制作一个字典 将key,value赋值进去
     * 
     * @param key
     * @param value
     * @return
     */
    public Map<String, String> createDictonary(String key, String value) {
        if ((value != null && !value.equals("")) && (key != null && !key.equals(""))) {
            Map<String, String> dictionary = new HashMap<String, String>();
            dictionary.put(key, value);
            return dictionary;
        }
        return null;
    }
}
