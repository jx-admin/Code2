package com.google.ads;

import android.webkit.WebView;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;

public class r
  implements n
{
  public void a(d paramd, HashMap<String, String> paramHashMap, WebView paramWebView)
  {
    if (paramWebView instanceof AdWebView)
      ((AdWebView)paramWebView).a();
    while (true)
    {
      return;
      b.b("Trying to close WebView that isn't an AdWebView");
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.r
 * JD-Core Version:    0.5.4
 */