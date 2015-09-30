package com.zrh.goldfree;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class KXianView extends Activity
{
  private Button btnBack;
  private Button btnRefresh;
  private WebView webView;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903041);
    this.webView = ((WebView)findViewById(2131230729));
    this.webView.loadUrl("http://www.zhijinwang.com/k/k.htm");
    this.webView.getSettings().setJavaScriptEnabled(true);
    this.webView.getSettings().setSaveFormData(true);
    this.webView.getSettings().setSavePassword(true);
    this.webView.getSettings().setSupportZoom(true);
    this.webView.getSettings().setBuiltInZoomControls(true);
    KXianView.1 local1 = new KXianView.1(this);
    this.webView.setWebViewClient(local1);
    this.btnRefresh = ((Button)findViewById(2131230727));
    this.btnRefresh.setOnClickListener(new KXianView.2(this));
    this.btnBack = ((Button)findViewById(2131230725));
    this.btnBack.setOnClickListener(new KXianView.3(this));
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.KXianView
 * JD-Core Version:    0.5.4
 */