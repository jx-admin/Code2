package com.google.ads.internal;

import android.net.Uri;
import android.webkit.WebView;
import com.google.ads.n;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import com.google.ads.util.f;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class a
{
  public static final f<a> a;
  public static final Map<String, n> b;
  public static final Map<String, n> c;
  private static final a d = new a();

  static
  {
    a = new a.2();
    b = Collections.unmodifiableMap(new a.3());
    c = Collections.unmodifiableMap(new a.1());
  }

  public String a(Uri paramUri, HashMap<String, String> paramHashMap)
  {
    String str1 = null;
    String str2;
    if (c(paramUri))
    {
      str2 = paramUri.getHost();
      if (str2 == null)
        b.e("An error occurred while parsing the AMSG parameters.");
    }
    while (true)
    {
      return str1;
      if (str2.equals("launch"))
      {
        paramHashMap.put("a", "intent");
        paramHashMap.put("u", paramHashMap.get("url"));
        paramHashMap.remove("url");
        str1 = "/open";
      }
      if (str2.equals("closecanvas"))
        str1 = "/close";
      if (str2.equals("log"))
        str1 = "/log";
      b.e("An error occurred while parsing the AMSG: " + paramUri.toString());
      continue;
      if (b(paramUri))
        str1 = paramUri.getPath();
      b.e("Message was neither a GMSG nor an AMSG.");
    }
  }

  public void a(WebView paramWebView)
  {
    a(paramWebView, "onshow", "{'version': 'afma-sdk-a-v6.2.1'}");
  }

  public void a(WebView paramWebView, String paramString)
  {
    b.a("Sending JS to a WebView: " + paramString);
    paramWebView.loadUrl("javascript:" + paramString);
  }

  public void a(WebView paramWebView, String paramString1, String paramString2)
  {
    if (paramString2 != null)
      a(paramWebView, "AFMA_ReceiveMessage" + "('" + paramString1 + "', " + paramString2 + ");");
    while (true)
    {
      return;
      a(paramWebView, "AFMA_ReceiveMessage" + "('" + paramString1 + "');");
    }
  }

  public void a(WebView paramWebView, Map<String, Boolean> paramMap)
  {
    a(paramWebView, "openableURLs", new JSONObject(paramMap).toString());
  }

  public void a(d paramd, Map<String, n> paramMap, Uri paramUri, WebView paramWebView)
  {
    HashMap localHashMap = AdUtil.b(paramUri);
    if (localHashMap == null)
      b.e("An error occurred while parsing the message parameters.");
    while (true)
    {
      return;
      String str = a(paramUri, localHashMap);
      if (str == null)
        b.e("An error occurred while parsing the message.");
      n localn = (n)paramMap.get(str);
      if (localn == null)
        b.e("No AdResponse found, <message: " + str + ">");
      localn.a(paramd, localHashMap, paramWebView);
    }
  }

  public boolean a(Uri paramUri)
  {
    int i = 0;
    if ((paramUri == null) || (!paramUri.isHierarchical()));
    while (true)
    {
      return i;
      if ((!b(paramUri)) && (!c(paramUri)))
        continue;
      i = 1;
    }
  }

  public void b(WebView paramWebView)
  {
    a(paramWebView, "onhide", null);
  }

  public boolean b(Uri paramUri)
  {
    int i = 0;
    String str1 = paramUri.getScheme();
    if ((str1 == null) || (!str1.equals("gmsg")));
    while (true)
    {
      return i;
      String str2 = paramUri.getAuthority();
      if ((str2 == null) || (!str2.equals("mobileads.google.com")))
        continue;
      i = 1;
    }
  }

  public boolean c(Uri paramUri)
  {
    String str = paramUri.getScheme();
    if ((str == null) || (!str.equals("admob")));
    for (int i = 0; ; i = 1)
      return i;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.internal.a
 * JD-Core Version:    0.5.4
 */