package com.zrh.goldfree;

import android.webkit.WebView;
import android.webkit.WebViewClient;

class HomeView$1 extends WebViewClient
{
  public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
  {
    if (HomeView.access$0(this.this$0) != 1)
      paramWebView.loadUrl(paramString);
    return true;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.HomeView.1
 * JD-Core Version:    0.5.4
 */