
package com.mobilesdk.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobilesdk.meap.MsdkGap;
import com.mobilesdk.view.MsdkInterface;
import com.mobilesdk.view.MsdkWebView;

/**
 * 测试Activity
 * 
 * @author yang.c.li
 */
public class MainActivity extends MsdkGap implements OnClickListener, MsdkInterface {

    private MsdkWebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mWebView = (MsdkWebView)findViewById(R.id.webView1);
        // mWebView.addJavascriptInterface(new Device(this), "native");
        // mWebView.loadUrl("http://sina.cn");
        // mWebView.loadUrl("file:///android_asset/bridge.html");
        // mWebView.loadUrl("file:///android_asset/test.html");
        mWebView.addJavascriptInterface(new TestExtend(mWebView.getInterface(), mWebView),
                "MobileSDK");
        mWebView.addJavascriptInterface(new Test2Extend(this), "Test2Extend");
        mWebView.loadUrl("file:///android_asset/index.html");
        // mWebView.loadUrl("https://wx.qq.com/"/*"https://www.icloud.com/"*/);
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View v) {

    }
}
