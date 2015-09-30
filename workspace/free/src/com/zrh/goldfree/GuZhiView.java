package com.zrh.goldfree;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class GuZhiView extends Activity
{
  private Button btnBack;
  private Button btnRefresh;
  private WebView webView;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903041);
    this.webView = ((WebView)findViewById(2131230729));
    this.webView.loadUrl("http://news.baidu.com/resource/html/bdhx.html?v=20120502");
    this.webView.getSettings().setJavaScriptEnabled(true);
    this.webView.getSettings().setSaveFormData(true);
    this.webView.getSettings().setSavePassword(true);
    this.webView.getSettings().setSupportZoom(true);
    this.webView.getSettings().setBuiltInZoomControls(true);
    GuZhiView.1 local1 = new GuZhiView.1(this);
    this.webView.setWebViewClient(local1);
    this.btnRefresh = ((Button)findViewById(2131230727));
    this.btnRefresh.setOnClickListener(new GuZhiView.2(this));
    this.btnBack = ((Button)findViewById(2131230725));
    this.btnBack.setOnClickListener(new GuZhiView.3(this));
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.GuZhiView
 * JD-Core Version:    0.5.4
 */