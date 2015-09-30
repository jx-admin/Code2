package com.google.ads;

import android.webkit.WebView;
import com.google.ads.internal.c;
import com.google.ads.internal.c.d;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;

public class w
  implements n
{
  public void a(d paramd, HashMap<String, String> paramHashMap, WebView paramWebView)
  {
    String str1 = (String)paramHashMap.get("url");
    String str2 = (String)paramHashMap.get("type");
    String str3 = (String)paramHashMap.get("afma_notify_dt");
    boolean bool1 = "1".equals(paramHashMap.get("drt_include"));
    String str4 = (String)paramHashMap.get("request_scenario");
    boolean bool2 = "1".equals(paramHashMap.get("use_webview_loadurl"));
    if (c.d.d.e.equals(str4));
    for (c.d locald = c.d.d; ; locald = c.d.b)
      while (true)
      {
        b.c("Received ad url: <url: \"" + str1 + "\" type: \"" + str2 + "\" afmaNotifyDt: \"" + str3 + "\" useWebViewLoadUrl: \"" + bool2 + "\">");
        c localc = paramd.j();
        if (localc != null)
        {
          localc.c(bool1);
          localc.a(locald);
          localc.d(bool2);
          localc.d(str1);
        }
        return;
        if (c.d.c.e.equals(str4))
          locald = c.d.c;
        if (!c.d.a.e.equals(str4))
          break;
        locald = c.d.a;
      }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.w
 * JD-Core Version:    0.5.4
 */