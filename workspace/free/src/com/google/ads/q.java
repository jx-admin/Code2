package com.google.ads;

import android.net.Uri;
import android.webkit.WebView;
import com.google.ads.internal.d;
import com.google.ads.internal.g;
import com.google.ads.util.b;
import java.util.HashMap;
import java.util.Locale;

public class q extends u
{
  public void a(d paramd, HashMap<String, String> paramHashMap, WebView paramWebView)
  {
    String str1 = (String)paramHashMap.get("u");
    if (str1 == null)
      b.e("Could not get URL from click gmsg.");
    while (true)
    {
      return;
      g localg = paramd.m();
      if (localg != null)
      {
        Uri localUri = Uri.parse(str1);
        String str2 = localUri.getHost();
        if ((str2 != null) && (str2.toLowerCase(Locale.US).endsWith(".admob.com")))
        {
          String str3 = null;
          String str4 = localUri.getPath();
          if (str4 != null)
          {
            String[] arrayOfString = str4.split("/");
            if (arrayOfString.length >= 4)
              str3 = arrayOfString[2] + "/" + arrayOfString[3];
          }
          localg.a(str3);
        }
      }
      super.a(paramd, paramHashMap, paramWebView);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.q
 * JD-Core Version:    0.5.4
 */