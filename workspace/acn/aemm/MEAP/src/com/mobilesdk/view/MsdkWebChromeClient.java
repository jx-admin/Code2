
package com.mobilesdk.view;

import android.util.Log;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * 主要辅助WebView 拦截JS当前的处理 例如Javascript的对话框、网站图标、网站title、加载进度条等
 * 
 * @author yang.c.li
 */
public final class MsdkWebChromeClient extends WebChromeClient {

    private static final String TAG = "mWebChromeClient";

    // app所使用的View
    MsdkWebView appView;

    private MsdkInterface mInterface;

    public MsdkWebChromeClient(MsdkInterface mInterface) {
        this.mInterface = mInterface;
    }

    public MsdkWebChromeClient(MsdkInterface mInterface, MsdkWebView app) {
        this.mInterface = mInterface;
        this.appView = app;
    }

    public void setWebView(MsdkWebView view) {
        this.appView = view;
    }

    /**
     * 拦截JS alert 提示
     */
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    /**
     * 
     */
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
            JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    /**
     * 
     */
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    /**
     * 
     */
    public void onProgressChanged(WebView view, int newProgress) {
        Log.i(TAG, "new Progress = " + newProgress);
        // // 设置标题栏的进度条的百分比
        this.mInterface.getActivity().getWindow()
                .setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
        super.onProgressChanged(view, newProgress);
    }

    /**
     * 
     */
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

    /**
     * 
     */
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }

    /**
     * 
     */
    public void onHideCustomView() {
        super.onHideCustomView();
    }
}
