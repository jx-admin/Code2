
package com.mobilesdk.view;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 主要帮助WebView处理各种通知、请求事件的
 * 
 * @author yang.c.li
 */
public final class MsdkWebViewClient extends WebViewClient {

    // app所使用的View
    MsdkWebView appView;
    
    MsdkInterface mInterface;

    /**
     * @param mInterface
     */
    public MsdkWebViewClient(MsdkInterface mInterface) {
        this.mInterface = mInterface;
    }

    /**
     * @param mInterface
     * @param app
     */
    public MsdkWebViewClient(MsdkInterface mInterface, MsdkWebView app) {
        this.mInterface = mInterface;
        this.appView = app;
    }

    /**
     * @param view
     */
    public void setWebView(MsdkWebView view) {
        this.appView = view;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host,
            String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
    }
}
