package com.google.ads.internal;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.ads.AdActivity;
import com.google.ads.n;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import com.google.ads.util.f;
import com.google.ads.util.g.b;
import java.util.HashMap;
import java.util.Map;

public class i extends WebViewClient
{
  private static final a c = (a)a.a.b();
  protected d a;
  protected boolean b;
  private final Map<String, n> d;
  private final boolean e;
  private boolean f;
  private boolean g;
  private boolean h;

  public i(d paramd, Map<String, n> paramMap, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.a = paramd;
    this.d = paramMap;
    this.e = paramBoolean1;
    this.f = paramBoolean2;
    this.b = false;
    this.g = false;
    this.h = false;
  }

  public static i a(d paramd, Map<String, n> paramMap, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (AdUtil.a >= 11);
    for (Object localObject = new g.b(paramd, paramMap, paramBoolean1, paramBoolean2); ; localObject = new i(paramd, paramMap, paramBoolean1, paramBoolean2))
      return localObject;
  }

  public void a(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }

  public void b(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }

  public void c(boolean paramBoolean)
  {
    this.g = paramBoolean;
  }

  public void d(boolean paramBoolean)
  {
    this.h = paramBoolean;
  }

  public void onPageFinished(WebView paramWebView, String paramString)
  {
    if (this.g)
    {
      c localc = this.a.j();
      if (localc == null)
        break label48;
      localc.c();
    }
    while (true)
    {
      this.g = false;
      if (this.h)
      {
        c.a(paramWebView);
        this.h = false;
      }
      return;
      label48: b.a("adLoader was null while trying to setFinishedLoadingHtml().");
    }
  }

  public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
  {
    boolean bool = true;
    Uri localUri;
    try
    {
      b.a("shouldOverrideUrlLoading(\"" + paramString + "\")");
      localUri = Uri.parse(paramString);
      if (c.a(localUri))
        c.a(this.a, this.d, localUri, paramWebView);
      else if (this.f)
        if (AdUtil.a(localUri))
        {
          bool = super.shouldOverrideUrlLoading(paramWebView, paramString);
        }
        else
        {
          HashMap localHashMap2 = new HashMap();
          localHashMap2.put("u", paramString);
          AdActivity.launchAdActivity(this.a, new e("intent", localHashMap2));
        }
    }
    catch (Throwable localThrowable)
    {
      b.b("An unknown error occurred in shouldOverrideUrlLoading.", localThrowable);
    }
    break label214:
    if (this.e)
    {
      HashMap localHashMap1 = new HashMap();
      localHashMap1.put("u", localUri.toString());
      AdActivity.launchAdActivity(this.a, new e("intent", localHashMap1));
    }
    else
    {
      b.e("URL is not a GMSG and can't handle URL: " + paramString);
    }
    label214: return bool;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.internal.i
 * JD-Core Version:    0.5.4
 */