package com.google.ads;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.webkit.WebView;
import com.google.ads.internal.a;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import com.google.ads.util.f;
import java.util.HashMap;

public class p
  implements n
{
  private static final a a = (a)a.a.b();

  public void a(d paramd, HashMap<String, String> paramHashMap, WebView paramWebView)
  {
    String str1 = (String)paramHashMap.get("urls");
    if (str1 == null)
      b.e("Could not get the urls param from canOpenURLs gmsg.");
    while (true)
    {
      return;
      String[] arrayOfString1 = str1.split(",");
      HashMap localHashMap = new HashMap();
      PackageManager localPackageManager = paramWebView.getContext().getPackageManager();
      int i = arrayOfString1.length;
      int j = 0;
      if (j < i)
      {
        label57: String str2 = arrayOfString1[j];
        String[] arrayOfString2 = str2.split(";", 2);
        String str3 = arrayOfString2[0];
        String str4;
        if (arrayOfString2.length >= 2)
        {
          str4 = arrayOfString2[1];
          label100: if (localPackageManager.resolveActivity(new Intent(str4, Uri.parse(str3)), 65536) == null)
            break label153;
        }
        for (boolean bool = true; ; bool = false)
        {
          localHashMap.put(str2, Boolean.valueOf(bool));
          ++j;
          break label57:
          str4 = "android.intent.action.VIEW";
          label153: break label100:
        }
      }
      a.a(paramWebView, localHashMap);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.p
 * JD-Core Version:    0.5.4
 */