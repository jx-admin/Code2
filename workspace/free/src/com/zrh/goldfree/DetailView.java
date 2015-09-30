package com.zrh.goldfree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class DetailView extends Activity
{
  private Button btnBack;
  private Button btnRefresh;
  private WebView webView;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903041);
    ((AdView)findViewById(2131230728)).loadAd(new AdRequest());
    Bundle localBundle = getIntent().getExtras();
    this.webView = ((WebView)findViewById(2131230729));
    this.webView.loadUrl(localBundle.getString("link"));
    this.webView.getSettings().setJavaScriptEnabled(true);
    this.webView.getSettings().setSaveFormData(true);
    this.webView.getSettings().setSavePassword(true);
    this.webView.getSettings().setSupportZoom(true);
    this.webView.getSettings().setBuiltInZoomControls(true);
    DetailView.1 local1 = new DetailView.1(this);
    this.webView.setWebViewClient(local1);
    this.btnRefresh = ((Button)findViewById(2131230727));
    this.btnRefresh.setOnClickListener(new DetailView.2(this));
    this.btnBack = ((Button)findViewById(2131230725));
    this.btnBack.setOnClickListener(new DetailView.3(this));
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.DetailView
 * JD-Core Version:    0.5.4
 */