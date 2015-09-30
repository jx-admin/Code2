package com.zrh.goldfree;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class HomeView extends Activity
{
  private Button btnGold;
  private Button btnPladium;
  private Button btnPlatium;
  private Button btnRefresh;
  private Button btnSilver;
  private int mIsZongHe;
  private WebView webView;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903042);
    ((AdView)findViewById(2131230728)).loadAd(new AdRequest());
    this.webView = ((WebView)findViewById(2131230729));
    this.webView.getSettings().setJavaScriptEnabled(true);
    this.webView.getSettings().setSaveFormData(true);
    this.webView.getSettings().setSavePassword(true);
    this.webView.getSettings().setSupportZoom(true);
    this.webView.getSettings().setBuiltInZoomControls(true);
    this.webView.loadUrl("http://www.kitco.cn/cn/live_charts/goldcn.gif");
    this.mIsZongHe = 0;
    HomeView.1 local1 = new HomeView.1(this);
    this.webView.setWebViewClient(local1);
    this.btnRefresh = ((Button)findViewById(2131230727));
    this.btnRefresh.setOnClickListener(new HomeView.2(this));
    this.btnGold = ((Button)findViewById(2131230730));
    this.btnGold.setOnClickListener(new HomeView.3(this));
    this.btnSilver = ((Button)findViewById(2131230732));
    this.btnSilver.setOnClickListener(new HomeView.4(this));
    this.btnPlatium = ((Button)findViewById(2131230731));
    this.btnPlatium.setOnClickListener(new HomeView.5(this));
    this.btnPladium = ((Button)findViewById(2131230733));
    this.btnPladium.setOnClickListener(new HomeView.6(this));
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.HomeView
 * JD-Core Version:    0.5.4
 */