package com.google.ads;

import android.webkit.WebView;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;

public class s
  implements n
{
  public void a(d paramd, HashMap<String, String> paramHashMap, WebView paramWebView)
  {
    if (paramWebView instanceof AdWebView)
      ((AdWebView)paramWebView).setCustomClose("1".equals(paramHashMap.get("custom_close")));
    while (true)
    {
      return;
      b.b("Trying to set a custom close icon on a WebView that isn't an AdWebView");
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.s
 * JD-Core Version:    0.5.4
 */