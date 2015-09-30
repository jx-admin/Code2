package com.zrh.goldfree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class YinZoushiView extends Activity
{
  private int mCurrRadio;
  private int mNian;
  private int mRi;
  private int mYue;
  private WebView webView;

  private String inttostr(int paramInt)
  {
    if (paramInt < 10);
    for (String str = "0" + paramInt; ; str = paramInt)
      return str;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903050);
    this.webView = ((WebView)findViewById(2131230745));
    this.webView.getSettings().setJavaScriptEnabled(true);
    this.webView.getSettings().setSaveFormData(true);
    this.webView.getSettings().setSavePassword(true);
    this.webView.getSettings().setSupportZoom(true);
    this.webView.getSettings().setBuiltInZoomControls(true);
    Bundle localBundle = getIntent().getExtras();
    this.mCurrRadio = localBundle.getInt("radio");
    this.mNian = localBundle.getInt("nian");
    this.mYue = localBundle.getInt("yue");
    this.mRi = localBundle.getInt("ri");
    String str = null;
    if (this.mCurrRadio == 1);
    for (str = "http://www.kitco.cn/cn/search_charts/silver/ag_LF_" + this.mNian + ".gif"; ; str = "http://www.kitco.cn/cn/hist_charts/silver/24_hours/" + this.mNian + "/cn_ag" + inttostr(this.mYue) + inttostr(this.mRi) + this.mNian + ".gif")
      do
        while (true)
        {
          this.webView.loadUrl(str);
          return;
          if (this.mCurrRadio != 2)
            break;
          str = "http://www.kitco.cn/cn/search_charts//silver/ag_lf_" + this.mNian + "_" + this.mYue + ".gif";
        }
      while (this.mCurrRadio != 3);
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.YinZoushiView
 * JD-Core Version:    0.5.4
 */