
package com.mobilesdk.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * API封装了一层WebView 便于开放一些接口给JS 可以通过这些接口来进行设置WebView的一些属性
 * 
 * @author yang.c.li
 */
public final class MsdkWebView extends WebView {

    private static final String TAG = "mWebView";

    private MsdkWebViewClient viewClient;

    private MsdkWebChromeClient chromeClient;

    private MsdkInterface mInterface;

    public Handler mhandler = new Handler();

    public MsdkWebView(Context context) {
        super(context);
        if (MsdkInterface.class.isInstance(context)) {
            this.mInterface = ((MsdkInterface)context);
        } else {
            Log.d(TAG, "Your activity must implement CordovaInterface to work");
        }
        initSetting();
    }

    public MsdkWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (MsdkInterface.class.isInstance(context)) {
            this.mInterface = ((MsdkInterface)context);
        } else {
            Log.d(TAG, "Your activity must implement CordovaInterface to work");
        }
        setWebViewClient(mInterface);
        setWebChromeClient(new MsdkWebChromeClient(mInterface, this));
        initSetting();
    }

    public MsdkWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (MsdkInterface.class.isInstance(context)) {
            this.mInterface = ((MsdkInterface)context);
        } else {
            Log.d(TAG, "Your activity must implement CordovaInterface to work");
        }
        setWebChromeClient(new MsdkWebChromeClient(mInterface, this));
        initSetting();
    }

    public MsdkWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
        super(context, attrs, defStyle, privateBrowsing);
        if (MsdkInterface.class.isInstance(context)) {
            this.mInterface = ((MsdkInterface)context);
        } else {
            Log.d(TAG, "Your activity must implement CordovaInterface to work");
        }
        setWebViewClient(mInterface);
        setWebChromeClient(new MsdkWebChromeClient(mInterface, this));
        initSetting();
    }

    /**
     * 开发者可以通过这个方法重新设置WebViewClient
     * 
     * @param client
     */
    public void setWebViewClient(MsdkWebViewClient client) {
        this.viewClient = client;
        super.setWebViewClient(client);
    }

    /**
     * 开发者可以通过这个方法重新设置WebChromeClient
     * 
     * @param client
     */
    public void setWebChromeClient(MsdkWebChromeClient client) {
        this.chromeClient = client;
        super.setWebChromeClient(client);
    }

    public MsdkWebViewClient getWebViewClient() {
        return this.viewClient;
    }

    public MsdkWebChromeClient getWebChromeClient() {
        return this.chromeClient;
    }

    /**
     * 主要帮助WebView处理各种通知、请求事件的
     * 
     * @param act
     */
    private void setWebViewClient(MsdkInterface act) {
        setWebViewClient(new MsdkWebViewClient(this.mInterface, this));
    }

    /**
     * 装载一些webView默认设置
     */
    private void initSetting() {
        setInitialScale(0);
        setVerticalScrollBarEnabled(false);
        requestFocusFromTouch();

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        initNative();
    }

    private void initNative() {
        addJavascriptInterface(new MobileSDK(mInterface, this), "MobileSDKKernel");
    }

    /**
     * @param obj
     * @param interfaceName
     */
    public void addJavascriptInterface(Object obj, String interfaceName) {
        super.addJavascriptInterface(obj, interfaceName);
    }

    /**
     * @return the mInterface
     */
    public MsdkInterface getInterface() {
        return mInterface;
    }
}
