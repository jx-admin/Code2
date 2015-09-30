
package com.mobilesdk.test;

import com.mobilesdk.view.MobileSDK;
import com.mobilesdk.view.MsdkInterface;
import com.mobilesdk.view.MsdkWebView;

/**
 * 继承关系的拓展
 * 
 * @author yang.c.li
 */
public class TestExtend extends MobileSDK {

    public TestExtend(MsdkInterface mInterface, MsdkWebView mWebView) {
        super(mInterface, mWebView);
    }

    @Override
    public void execNative(String commandID, String action, String param) {
        super.execNative(commandID, action, param);
        String value = null;
        int actionType = Integer.parseInt(action);

        switch (actionType) {
            case 101:
                value = "{\"response\":{\"imei\":\"123456789\"},\"commandID\":\"" + commandID
                        + "\"" + ",\"code\":\"true\"}";
                break;
            default:
                break;
        }

        if (value != null) {
//            reloadUrl(value);
        }
    }
}
