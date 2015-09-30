package com.zrh.goldfree;

import android.webkit.WebView;
import android.webkit.WebViewClient;

class DetailView$1 extends WebViewClient
{
  public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
  {
    paramWebView.loadUrl(paramString);
    return true;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.DetailView.1
 * JD-Core Version:    0.5.4
 */