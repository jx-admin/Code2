package com.google.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebView;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.l;
import com.google.ads.l.a;
import com.google.ads.m;
import com.google.ads.searchads.SearchAdRequest;
import com.google.ads.util.AdUtil;
import com.google.ads.util.AdUtil.a;
import com.google.ads.util.i.b;
import com.google.ads.util.i.c;
import com.google.ads.util.i.d;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;

public class c
  implements Runnable
{
  boolean a;
  private String b;
  private String c;
  private String d;
  private String e;
  private boolean f;
  private f g;
  private d h;
  private AdRequest i;
  private WebView j;
  private String k;
  private LinkedList<String> l;
  private String m;
  private AdSize n;
  private volatile boolean o;
  private boolean p;
  private AdRequest.ErrorCode q;
  private boolean r;
  private int s;
  private Thread t;
  private boolean u;
  private d v = d.b;

  protected c()
  {
  }

  public c(d paramd)
  {
    this.h = paramd;
    this.k = null;
    this.b = null;
    this.c = null;
    this.d = null;
    this.l = new LinkedList();
    this.q = null;
    this.r = false;
    this.s = -1;
    this.f = false;
    this.p = false;
    this.m = null;
    this.n = null;
    if ((Activity)paramd.h().e.a() != null)
    {
      this.j = new AdWebView(paramd.h(), null);
      this.j.setWebViewClient(i.a(paramd, a.b, false, false));
      this.j.setVisibility(8);
      this.j.setWillNotDraw(true);
      this.g = new f(this, paramd);
    }
    while (true)
    {
      return;
      this.j = null;
      this.g = null;
      com.google.ads.util.b.e("activity was null while trying to create an AdLoader.");
    }
  }

  static void a(String paramString, com.google.ads.c paramc, com.google.ads.d paramd)
  {
    if (paramString == null);
    while (true)
    {
      return;
      if ((paramString.contains("no-store")) || (paramString.contains("no-cache")))
        continue;
      Matcher localMatcher = Pattern.compile("max-age\\s*=\\s*(\\d+)").matcher(paramString);
      if (localMatcher.find())
        try
        {
          int i1 = Integer.parseInt(localMatcher.group(1));
          paramd.a(paramc, i1);
          Locale localLocale = Locale.US;
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = Integer.valueOf(i1);
          com.google.ads.util.b.c(String.format(localLocale, "Caching gWhirl configuration for: %d seconds", arrayOfObject));
        }
        catch (NumberFormatException localNumberFormatException)
        {
          com.google.ads.util.b.b("Caught exception trying to parse cache control directive. Overflow?", localNumberFormatException);
        }
      com.google.ads.util.b.c("Unrecognized cacheControlDirective: '" + paramString + "'. Not caching configuration.");
    }
  }

  private void b(String paramString1, String paramString2)
  {
    this.h.a(new c(this.j, paramString2, paramString1));
  }

  private String d()
  {
    if (this.i instanceof SearchAdRequest);
    for (String str = "AFMA_buildAdURL"; ; str = "AFMA_buildAdURL")
      return str;
  }

  private String e()
  {
    if (this.i instanceof SearchAdRequest);
    for (String str = "AFMA_getSdkConstants();"; ; str = "AFMA_getSdkConstants();")
      return str;
  }

  private String f()
  {
    if (this.i instanceof SearchAdRequest);
    for (String str = "http://www.gstatic.com/safa/"; ; str = "http://media.admob.com/")
      return str;
  }

  private String g()
  {
    if (this.i instanceof SearchAdRequest);
    for (String str = "<html><head><script src=\"http://www.gstatic.com/safa/sdk-core-v40.js\"></script><script>"; ; str = "<html><head><script src=\"http://media.admob.com/sdk-core-v40.js\"></script><script>")
      return str;
  }

  private String h()
  {
    if (this.i instanceof SearchAdRequest);
    for (String str = "</script></head><body></body></html>"; ; str = "</script></head><body></body></html>")
      return str;
  }

  private void i()
  {
    AdWebView localAdWebView = this.h.k();
    this.h.l().c(true);
    this.h.m().h();
    this.h.a(new c(localAdWebView, this.b, this.c));
  }

  private void j()
  {
    this.h.a(new e(this.h, this.j, this.l, this.s, this.p, this.m, this.n));
  }

  public String a(Map<String, Object> paramMap, Activity paramActivity)
    throws c.b
  {
    int i1 = 0;
    Context localContext = paramActivity.getApplicationContext();
    g localg = this.h.m();
    long l1 = localg.m();
    if (l1 > 0L)
      paramMap.put("prl", Long.valueOf(l1));
    long l2 = localg.n();
    if (l2 > 0L)
      paramMap.put("prnl", Long.valueOf(l2));
    String str1 = localg.l();
    if (str1 != null)
      paramMap.put("ppcl", str1);
    String str2 = localg.k();
    if (str2 != null)
      paramMap.put("pcl", str2);
    long l3 = localg.j();
    if (l3 > 0L)
      paramMap.put("pcc", Long.valueOf(l3));
    paramMap.put("preqs", Long.valueOf(localg.o()));
    paramMap.put("oar", Long.valueOf(localg.p()));
    paramMap.put("bas_on", Long.valueOf(localg.s()));
    paramMap.put("bas_off", Long.valueOf(localg.v()));
    if (localg.y())
      paramMap.put("aoi_timeout", "true");
    if (localg.A())
      paramMap.put("aoi_nofill", "true");
    String str3 = localg.D();
    if (str3 != null)
      paramMap.put("pit", str3);
    paramMap.put("ptime", Long.valueOf(g.E()));
    localg.a();
    localg.i();
    String str4;
    if (this.h.h().b())
    {
      paramMap.put("format", "interstitial_mb");
      paramMap.put("slotname", this.h.h().d.a());
      paramMap.put("js", "afma-sdk-a-v6.2.1");
      str4 = localContext.getPackageName();
    }
    while (true)
    {
      StringBuilder localStringBuilder;
      try
      {
        PackageInfo localPackageInfo = localContext.getPackageManager().getPackageInfo(str4, 0);
        int i2 = localPackageInfo.versionCode;
        String str5 = AdUtil.f(localContext);
        if (!TextUtils.isEmpty(str5))
          paramMap.put("mv", str5);
        paramMap.put("msid", localContext.getPackageName());
        paramMap.put("app_name", i2 + ".android." + localContext.getPackageName());
        paramMap.put("isu", AdUtil.a(localContext));
        String str6 = AdUtil.d(localContext);
        if (str6 == null)
          str6 = "null";
        paramMap.put("net", str6);
        String str7 = AdUtil.e(localContext);
        if ((str7 != null) && (str7.length() != 0))
          paramMap.put("cap", str7);
        paramMap.put("u_audio", Integer.valueOf(AdUtil.g(localContext).ordinal()));
        DisplayMetrics localDisplayMetrics1 = AdUtil.a(paramActivity);
        paramMap.put("u_sd", Float.valueOf(localDisplayMetrics1.density));
        paramMap.put("u_h", Integer.valueOf(AdUtil.a(localContext, localDisplayMetrics1)));
        paramMap.put("u_w", Integer.valueOf(AdUtil.b(localContext, localDisplayMetrics1)));
        paramMap.put("hl", Locale.getDefault().getLanguage());
        if ((this.h.h().i != null) && (this.h.h().i.a() != null))
        {
          AdView localAdView = (AdView)this.h.h().i.a();
          if (localAdView.getParent() != null)
          {
            int[] arrayOfInt = new int[2];
            localAdView.getLocationOnScreen(arrayOfInt);
            int i4 = arrayOfInt[0];
            int i5 = arrayOfInt[1];
            DisplayMetrics localDisplayMetrics2 = ((Context)this.h.h().f.a()).getResources().getDisplayMetrics();
            int i6 = localDisplayMetrics2.widthPixels;
            int i7 = localDisplayMetrics2.heightPixels;
            if ((!localAdView.isShown()) || (i4 + localAdView.getWidth() <= 0) || (i5 + localAdView.getHeight() <= 0) || (i4 > i6) || (i5 > i7))
              break label1511;
            i8 = 1;
            HashMap localHashMap2 = new HashMap();
            localHashMap2.put("x", Integer.valueOf(i4));
            localHashMap2.put("y", Integer.valueOf(i5));
            localHashMap2.put("width", Integer.valueOf(localAdView.getWidth()));
            localHashMap2.put("height", Integer.valueOf(localAdView.getHeight()));
            localHashMap2.put("visible", Integer.valueOf(i8));
            paramMap.put("ad_pos", localHashMap2);
          }
        }
        localStringBuilder = new StringBuilder();
        AdSize[] arrayOfAdSize = (AdSize[])this.h.h().l.a();
        if (arrayOfAdSize == null)
          break label1224;
        int i3 = arrayOfAdSize.length;
        while (true)
        {
          if (i1 >= i3)
            break label1209;
          AdSize localAdSize2 = arrayOfAdSize[i1];
          if (localStringBuilder.length() != 0)
            localStringBuilder.append("|");
          localStringBuilder.append(localAdSize2.getWidth() + "x" + localAdSize2.getHeight());
          ++i1;
        }
        AdSize localAdSize1 = ((h)this.h.h().k.a()).b();
        if (localAdSize1.isFullWidth())
          paramMap.put("smart_w", "full");
        if (localAdSize1.isAutoHeight())
          paramMap.put("smart_h", "auto");
        if (!localAdSize1.isCustomAdSize())
          paramMap.put("format", localAdSize1.toString());
        HashMap localHashMap1 = new HashMap();
        localHashMap1.put("w", Integer.valueOf(localAdSize1.getWidth()));
        localHashMap1.put("h", Integer.valueOf(localAdSize1.getHeight()));
        paramMap.put("ad_frame", localHashMap1);
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        throw new b("NameNotFoundException");
      }
      label1209: paramMap.put("sz", localStringBuilder.toString());
      label1224: TelephonyManager localTelephonyManager = (TelephonyManager)localContext.getSystemService("phone");
      paramMap.put("carrier", localTelephonyManager.getNetworkOperator());
      paramMap.put("gnt", Integer.valueOf(localTelephonyManager.getNetworkType()));
      if (AdUtil.c())
        paramMap.put("simulator", Integer.valueOf(1));
      paramMap.put("session_id", com.google.ads.b.a().b().toString());
      paramMap.put("seq_num", com.google.ads.b.a().c().toString());
      String str8 = AdUtil.a(paramMap);
      if (((Boolean)((l.a)((l)this.h.h().a.a()).a.a()).l.a()).booleanValue());
      for (String str9 = g() + d() + "(" + str8 + ");" + h(); ; str9 = g() + e() + d() + "(" + str8 + ");" + h())
      {
        com.google.ads.util.b.c("adRequestUrlHtml: " + str9);
        return str9;
      }
      label1511: int i8 = 0;
    }
  }

  protected void a()
  {
    com.google.ads.util.b.a("AdLoader cancelled.");
    if (this.j != null)
    {
      this.j.stopLoading();
      this.j.destroy();
    }
    if (this.t != null)
    {
      this.t.interrupt();
      this.t = null;
    }
    if (this.g != null)
      this.g.a();
    this.o = true;
  }

  public void a(int paramInt)
  {
    monitorenter;
    try
    {
      this.s = paramInt;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void a(AdRequest.ErrorCode paramErrorCode)
  {
    monitorenter;
    try
    {
      this.q = paramErrorCode;
      super.notify();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void a(AdRequest.ErrorCode paramErrorCode, boolean paramBoolean)
  {
    this.h.a(new a(this.h, this.j, this.g, paramErrorCode, paramBoolean));
  }

  protected void a(AdRequest paramAdRequest)
  {
    this.i = paramAdRequest;
    this.o = false;
    this.t = new Thread(this);
    this.t.start();
  }

  public void a(AdSize paramAdSize)
  {
    monitorenter;
    try
    {
      this.n = paramAdSize;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void a(d paramd)
  {
    monitorenter;
    try
    {
      this.v = paramd;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void a(String paramString)
  {
    monitorenter;
    try
    {
      this.l.add(paramString);
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void a(String paramString1, String paramString2)
  {
    monitorenter;
    try
    {
      this.b = paramString2;
      this.c = paramString1;
      super.notify();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void a(boolean paramBoolean)
  {
    monitorenter;
    try
    {
      this.f = paramBoolean;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void b()
  {
    try
    {
      if (TextUtils.isEmpty(this.e))
      {
        com.google.ads.util.b.b("Got a mediation response with no content type. Aborting mediation.");
        a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
      }
      else if (!this.e.startsWith("application/json"))
      {
        com.google.ads.util.b.b("Got a mediation response with a content type: '" + this.e + "'. Expected something starting with 'application/json'. Aborting mediation.");
        a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
      }
    }
    catch (JSONException localJSONException)
    {
      com.google.ads.util.b.b("AdLoader can't parse gWhirl server configuration.", localJSONException);
      a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
    }
    return;
    com.google.ads.c localc = com.google.ads.c.a(this.c);
    a(this.d, localc, this.h.i());
    this.h.a(new Runnable(localc)
    {
      public void run()
      {
        if (c.a(c.this) != null)
        {
          c.a(c.this).stopLoading();
          c.a(c.this).destroy();
        }
        c.c(c.this).a(c.b(c.this));
        if (c.d(c.this) != null)
          ((h)c.c(c.this).h().k.a()).b(c.d(c.this));
        c.c(c.this).a(this.a);
      }
    });
  }

  protected void b(String paramString)
  {
    monitorenter;
    try
    {
      this.e = paramString;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void b(boolean paramBoolean)
  {
    monitorenter;
    try
    {
      this.p = paramBoolean;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void c()
  {
    monitorenter;
    try
    {
      this.r = true;
      super.notify();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void c(String paramString)
  {
    monitorenter;
    try
    {
      this.d = paramString;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void c(boolean paramBoolean)
  {
    monitorenter;
    try
    {
      this.u = paramBoolean;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void d(String paramString)
  {
    monitorenter;
    try
    {
      this.k = paramString;
      super.notify();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void d(boolean paramBoolean)
  {
    monitorenter;
    try
    {
      this.a = paramBoolean;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void e(String paramString)
  {
    monitorenter;
    try
    {
      this.m = paramString;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  // ERROR //
  public void run()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 117	com/google/ads/internal/c:j	Landroid/webkit/WebView;
    //   6: ifnull +10 -> 16
    //   9: aload_0
    //   10: getfield 148	com/google/ads/internal/c:g	Lcom/google/ads/internal/f;
    //   13: ifnonnull +20 -> 33
    //   16: ldc_w 752
    //   19: invokestatic 155	com/google/ads/util/b:e	(Ljava/lang/String;)V
    //   22: aload_0
    //   23: getstatic 714	com/google/ads/AdRequest$ErrorCode:INTERNAL_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   26: iconst_0
    //   27: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   30: aload_0
    //   31: monitorexit
    //   32: return
    //   33: aload_0
    //   34: getfield 66	com/google/ads/internal/c:h	Lcom/google/ads/internal/d;
    //   37: invokevirtual 98	com/google/ads/internal/d:h	()Lcom/google/ads/m;
    //   40: getfield 103	com/google/ads/m:e	Lcom/google/ads/util/i$d;
    //   43: invokevirtual 108	com/google/ads/util/i$d:a	()Ljava/lang/Object;
    //   46: checkcast 110	android/app/Activity
    //   49: astore_3
    //   50: aload_3
    //   51: ifnonnull +27 -> 78
    //   54: ldc_w 754
    //   57: invokestatic 155	com/google/ads/util/b:e	(Ljava/lang/String;)V
    //   60: aload_0
    //   61: getstatic 714	com/google/ads/AdRequest$ErrorCode:INTERNAL_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   64: iconst_0
    //   65: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   68: aload_0
    //   69: monitorexit
    //   70: goto -38 -> 32
    //   73: astore_2
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_2
    //   77: athrow
    //   78: aload_0
    //   79: getfield 66	com/google/ads/internal/c:h	Lcom/google/ads/internal/d;
    //   82: invokevirtual 755	com/google/ads/internal/d:o	()J
    //   85: lstore 4
    //   87: invokestatic 760	android/os/SystemClock:elapsedRealtime	()J
    //   90: lstore 6
    //   92: aload_0
    //   93: getfield 252	com/google/ads/internal/c:i	Lcom/google/ads/AdRequest;
    //   96: aload_0
    //   97: getfield 66	com/google/ads/internal/c:h	Lcom/google/ads/internal/d;
    //   100: invokevirtual 98	com/google/ads/internal/d:h	()Lcom/google/ads/m;
    //   103: getfield 501	com/google/ads/m:f	Lcom/google/ads/util/i$b;
    //   106: invokevirtual 380	com/google/ads/util/i$b:a	()Ljava/lang/Object;
    //   109: checkcast 386	android/content/Context
    //   112: invokevirtual 766	com/google/ads/AdRequest:getRequestMap	(Landroid/content/Context;)Ljava/util/Map;
    //   115: astore 8
    //   117: aload 8
    //   119: ldc_w 768
    //   122: invokeinterface 772 2 0
    //   127: astore 9
    //   129: aload 9
    //   131: instanceof 305
    //   134: ifeq +142 -> 276
    //   137: aload 9
    //   139: checkcast 305	java/util/Map
    //   142: astore 31
    //   144: aload 31
    //   146: ldc_w 774
    //   149: invokeinterface 772 2 0
    //   154: astore 32
    //   156: aload 32
    //   158: instanceof 163
    //   161: ifeq +12 -> 173
    //   164: aload_0
    //   165: aload 32
    //   167: checkcast 163	java/lang/String
    //   170: putfield 70	com/google/ads/internal/c:b	Ljava/lang/String;
    //   173: aload 31
    //   175: ldc_w 776
    //   178: invokeinterface 772 2 0
    //   183: astore 33
    //   185: aload 33
    //   187: instanceof 163
    //   190: ifeq +12 -> 202
    //   193: aload_0
    //   194: aload 33
    //   196: checkcast 163	java/lang/String
    //   199: putfield 68	com/google/ads/internal/c:k	Ljava/lang/String;
    //   202: aload 31
    //   204: ldc_w 778
    //   207: invokeinterface 772 2 0
    //   212: astore 34
    //   214: aload 34
    //   216: instanceof 163
    //   219: ifeq +19 -> 238
    //   222: aload 34
    //   224: ldc_w 779
    //   227: invokevirtual 782	java/lang/Object:equals	(Ljava/lang/Object;)Z
    //   230: ifeq +127 -> 357
    //   233: aload_0
    //   234: iconst_1
    //   235: putfield 85	com/google/ads/internal/c:s	I
    //   238: aload 31
    //   240: ldc_w 784
    //   243: invokeinterface 772 2 0
    //   248: astore 35
    //   250: aload 35
    //   252: instanceof 163
    //   255: ifeq +21 -> 276
    //   258: aload 35
    //   260: ldc_w 785
    //   263: invokevirtual 782	java/lang/Object:equals	(Ljava/lang/Object;)Z
    //   266: ifeq +10 -> 276
    //   269: aload_0
    //   270: getfield 66	com/google/ads/internal/c:h	Lcom/google/ads/internal/d;
    //   273: invokevirtual 787	com/google/ads/internal/d:d	()V
    //   276: aload_0
    //   277: getfield 70	com/google/ads/internal/c:b	Ljava/lang/String;
    //   280: ifnonnull +610 -> 890
    //   283: aload_0
    //   284: getfield 68	com/google/ads/internal/c:k	Ljava/lang/String;
    //   287: astore 15
    //   289: aload 15
    //   291: ifnonnull +247 -> 538
    //   294: aload_0
    //   295: aload 8
    //   297: aload_3
    //   298: invokevirtual 789	com/google/ads/internal/c:a	(Ljava/util/Map;Landroid/app/Activity;)Ljava/lang/String;
    //   301: astore 24
    //   303: aload_0
    //   304: aload 24
    //   306: aload_0
    //   307: invokespecial 791	com/google/ads/internal/c:f	()Ljava/lang/String;
    //   310: invokespecial 793	com/google/ads/internal/c:b	(Ljava/lang/String;Ljava/lang/String;)V
    //   313: invokestatic 760	android/os/SystemClock:elapsedRealtime	()J
    //   316: lstore 25
    //   318: lload 4
    //   320: lload 25
    //   322: lload 6
    //   324: lsub
    //   325: lsub
    //   326: lstore 27
    //   328: lload 27
    //   330: lconst_0
    //   331: lcmp
    //   332: ifle +9 -> 341
    //   335: aload_0
    //   336: lload 27
    //   338: invokevirtual 797	java/lang/Object:wait	(J)V
    //   341: aload_0
    //   342: getfield 681	com/google/ads/internal/c:o	Z
    //   345: istore 29
    //   347: iload 29
    //   349: ifeq +118 -> 467
    //   352: aload_0
    //   353: monitorexit
    //   354: goto -322 -> 32
    //   357: aload 34
    //   359: ldc_w 798
    //   362: invokevirtual 782	java/lang/Object:equals	(Ljava/lang/Object;)Z
    //   365: ifeq -127 -> 238
    //   368: aload_0
    //   369: iconst_0
    //   370: putfield 85	com/google/ads/internal/c:s	I
    //   373: goto -135 -> 238
    //   376: astore_1
    //   377: ldc_w 800
    //   380: aload_1
    //   381: invokestatic 225	com/google/ads/util/b:b	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   384: aload_0
    //   385: getstatic 714	com/google/ads/AdRequest$ErrorCode:INTERNAL_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   388: iconst_1
    //   389: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   392: aload_0
    //   393: monitorexit
    //   394: goto -362 -> 32
    //   397: astore 23
    //   399: new 227	java/lang/StringBuilder
    //   402: dup
    //   403: invokespecial 228	java/lang/StringBuilder:<init>	()V
    //   406: ldc_w 802
    //   409: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   412: aload 23
    //   414: invokevirtual 805	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   417: invokevirtual 240	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   420: invokestatic 220	com/google/ads/util/b:c	(Ljava/lang/String;)V
    //   423: aload_0
    //   424: getstatic 714	com/google/ads/AdRequest$ErrorCode:INTERNAL_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   427: iconst_0
    //   428: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   431: aload_0
    //   432: monitorexit
    //   433: goto -401 -> 32
    //   436: astore 30
    //   438: new 227	java/lang/StringBuilder
    //   441: dup
    //   442: invokespecial 228	java/lang/StringBuilder:<init>	()V
    //   445: ldc_w 807
    //   448: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   451: aload 30
    //   453: invokevirtual 805	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   456: invokevirtual 240	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   459: invokestatic 665	com/google/ads/util/b:a	(Ljava/lang/String;)V
    //   462: aload_0
    //   463: monitorexit
    //   464: goto -432 -> 32
    //   467: aload_0
    //   468: getfield 81	com/google/ads/internal/c:q	Lcom/google/ads/AdRequest$ErrorCode;
    //   471: ifnull +17 -> 488
    //   474: aload_0
    //   475: aload_0
    //   476: getfield 81	com/google/ads/internal/c:q	Lcom/google/ads/AdRequest$ErrorCode;
    //   479: iconst_0
    //   480: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   483: aload_0
    //   484: monitorexit
    //   485: goto -453 -> 32
    //   488: aload_0
    //   489: getfield 68	com/google/ads/internal/c:k	Ljava/lang/String;
    //   492: ifnonnull +46 -> 538
    //   495: new 227	java/lang/StringBuilder
    //   498: dup
    //   499: invokespecial 228	java/lang/StringBuilder:<init>	()V
    //   502: ldc_w 809
    //   505: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   508: lload 4
    //   510: invokevirtual 812	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   513: ldc_w 814
    //   516: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   519: invokevirtual 240	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   522: invokestatic 220	com/google/ads/util/b:c	(Ljava/lang/String;)V
    //   525: aload_0
    //   526: getstatic 817	com/google/ads/AdRequest$ErrorCode:NETWORK_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   529: iconst_0
    //   530: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   533: aload_0
    //   534: monitorexit
    //   535: goto -503 -> 32
    //   538: aload_0
    //   539: getfield 66	com/google/ads/internal/c:h	Lcom/google/ads/internal/d;
    //   542: invokevirtual 279	com/google/ads/internal/d:m	()Lcom/google/ads/internal/g;
    //   545: astore 16
    //   547: getstatic 820	com/google/ads/internal/c$2:a	[I
    //   550: aload_0
    //   551: getfield 63	com/google/ads/internal/c:v	Lcom/google/ads/internal/c$d;
    //   554: invokevirtual 821	com/google/ads/internal/c$d:ordinal	()I
    //   557: iaload
    //   558: tableswitch	default:+30 -> 588, 1:+109->667, 2:+133->691, 3:+147->705, 4:+166->724
    //   589: getfield 745	com/google/ads/internal/c:a	Z
    //   592: ifne +264 -> 856
    //   595: ldc_w 823
    //   598: invokestatic 665	com/google/ads/util/b:a	(Ljava/lang/String;)V
    //   601: aload_0
    //   602: getfield 148	com/google/ads/internal/c:g	Lcom/google/ads/internal/f;
    //   605: aload_0
    //   606: getfield 743	com/google/ads/internal/c:u	Z
    //   609: invokevirtual 825	com/google/ads/internal/f:a	(Z)V
    //   612: aload_0
    //   613: getfield 148	com/google/ads/internal/c:g	Lcom/google/ads/internal/f;
    //   616: aload_0
    //   617: getfield 68	com/google/ads/internal/c:k	Ljava/lang/String;
    //   620: invokevirtual 826	com/google/ads/internal/f:a	(Ljava/lang/String;)V
    //   623: invokestatic 760	android/os/SystemClock:elapsedRealtime	()J
    //   626: lstore 17
    //   628: lload 4
    //   630: lload 17
    //   632: lload 6
    //   634: lsub
    //   635: lsub
    //   636: lstore 19
    //   638: lload 19
    //   640: lconst_0
    //   641: lcmp
    //   642: ifle +9 -> 651
    //   645: aload_0
    //   646: lload 19
    //   648: invokevirtual 797	java/lang/Object:wait	(J)V
    //   651: aload_0
    //   652: getfield 681	com/google/ads/internal/c:o	Z
    //   655: istore 21
    //   657: iload 21
    //   659: ifeq +126 -> 785
    //   662: aload_0
    //   663: monitorexit
    //   664: goto -632 -> 32
    //   667: aload 16
    //   669: invokevirtual 828	com/google/ads/internal/g:r	()V
    //   672: aload 16
    //   674: invokevirtual 830	com/google/ads/internal/g:u	()V
    //   677: aload 16
    //   679: invokevirtual 832	com/google/ads/internal/g:x	()V
    //   682: ldc_w 834
    //   685: invokestatic 220	com/google/ads/util/b:c	(Ljava/lang/String;)V
    //   688: goto -100 -> 588
    //   691: aload 16
    //   693: invokevirtual 836	com/google/ads/internal/g:t	()V
    //   696: ldc_w 838
    //   699: invokestatic 220	com/google/ads/util/b:c	(Ljava/lang/String;)V
    //   702: goto -114 -> 588
    //   705: aload 16
    //   707: invokevirtual 840	com/google/ads/internal/g:w	()V
    //   710: aload 16
    //   712: invokevirtual 842	com/google/ads/internal/g:q	()V
    //   715: ldc_w 844
    //   718: invokestatic 220	com/google/ads/util/b:c	(Ljava/lang/String;)V
    //   721: goto -133 -> 588
    //   724: aload 16
    //   726: invokevirtual 842	com/google/ads/internal/g:q	()V
    //   729: ldc_w 846
    //   732: invokestatic 220	com/google/ads/util/b:c	(Ljava/lang/String;)V
    //   735: ldc_w 848
    //   738: invokestatic 220	com/google/ads/util/b:c	(Ljava/lang/String;)V
    //   741: aload_0
    //   742: getstatic 817	com/google/ads/AdRequest$ErrorCode:NETWORK_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   745: iconst_0
    //   746: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   749: aload_0
    //   750: monitorexit
    //   751: goto -719 -> 32
    //   754: astore 22
    //   756: new 227	java/lang/StringBuilder
    //   759: dup
    //   760: invokespecial 228	java/lang/StringBuilder:<init>	()V
    //   763: ldc_w 850
    //   766: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   769: aload 22
    //   771: invokevirtual 805	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   774: invokevirtual 240	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   777: invokestatic 665	com/google/ads/util/b:a	(Ljava/lang/String;)V
    //   780: aload_0
    //   781: monitorexit
    //   782: goto -750 -> 32
    //   785: aload_0
    //   786: getfield 81	com/google/ads/internal/c:q	Lcom/google/ads/AdRequest$ErrorCode;
    //   789: ifnull +17 -> 806
    //   792: aload_0
    //   793: aload_0
    //   794: getfield 81	com/google/ads/internal/c:q	Lcom/google/ads/AdRequest$ErrorCode;
    //   797: iconst_0
    //   798: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   801: aload_0
    //   802: monitorexit
    //   803: goto -771 -> 32
    //   806: aload_0
    //   807: getfield 72	com/google/ads/internal/c:c	Ljava/lang/String;
    //   810: ifnonnull +80 -> 890
    //   813: new 227	java/lang/StringBuilder
    //   816: dup
    //   817: invokespecial 228	java/lang/StringBuilder:<init>	()V
    //   820: ldc_w 809
    //   823: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   826: lload 4
    //   828: invokevirtual 812	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   831: ldc_w 852
    //   834: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   837: invokevirtual 240	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   840: invokestatic 220	com/google/ads/util/b:c	(Ljava/lang/String;)V
    //   843: aload_0
    //   844: getstatic 817	com/google/ads/AdRequest$ErrorCode:NETWORK_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   847: iconst_0
    //   848: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   851: aload_0
    //   852: monitorexit
    //   853: goto -821 -> 32
    //   856: aload_0
    //   857: aload_0
    //   858: getfield 68	com/google/ads/internal/c:k	Ljava/lang/String;
    //   861: putfield 70	com/google/ads/internal/c:b	Ljava/lang/String;
    //   864: new 227	java/lang/StringBuilder
    //   867: dup
    //   868: invokespecial 228	java/lang/StringBuilder:<init>	()V
    //   871: ldc_w 854
    //   874: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   877: aload_0
    //   878: getfield 70	com/google/ads/internal/c:b	Ljava/lang/String;
    //   881: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   884: invokevirtual 240	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   887: invokestatic 665	com/google/ads/util/b:a	(Ljava/lang/String;)V
    //   890: aload_0
    //   891: getfield 745	com/google/ads/internal/c:a	Z
    //   894: ifne +235 -> 1129
    //   897: aload_0
    //   898: getfield 87	com/google/ads/internal/c:f	Z
    //   901: ifeq +20 -> 921
    //   904: aload_0
    //   905: getfield 66	com/google/ads/internal/c:h	Lcom/google/ads/internal/d;
    //   908: iconst_1
    //   909: invokevirtual 856	com/google/ads/internal/d:b	(Z)V
    //   912: aload_0
    //   913: invokevirtual 858	com/google/ads/internal/c:b	()V
    //   916: aload_0
    //   917: monitorexit
    //   918: goto -886 -> 32
    //   921: aload_0
    //   922: getfield 705	com/google/ads/internal/c:e	Ljava/lang/String;
    //   925: ifnull +74 -> 999
    //   928: aload_0
    //   929: getfield 705	com/google/ads/internal/c:e	Ljava/lang/String;
    //   932: ldc_w 718
    //   935: invokevirtual 722	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   938: ifne +16 -> 954
    //   941: aload_0
    //   942: getfield 705	com/google/ads/internal/c:e	Ljava/lang/String;
    //   945: ldc_w 860
    //   948: invokevirtual 722	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   951: ifeq +48 -> 999
    //   954: new 227	java/lang/StringBuilder
    //   957: dup
    //   958: invokespecial 228	java/lang/StringBuilder:<init>	()V
    //   961: ldc_w 862
    //   964: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   967: aload_0
    //   968: getfield 705	com/google/ads/internal/c:e	Ljava/lang/String;
    //   971: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   974: ldc_w 864
    //   977: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   980: invokevirtual 240	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   983: invokestatic 709	com/google/ads/util/b:b	(Ljava/lang/String;)V
    //   986: aload_0
    //   987: getstatic 714	com/google/ads/AdRequest$ErrorCode:INTERNAL_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   990: iconst_0
    //   991: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   994: aload_0
    //   995: monitorexit
    //   996: goto -964 -> 32
    //   999: aload_0
    //   1000: getfield 66	com/google/ads/internal/c:h	Lcom/google/ads/internal/d;
    //   1003: invokevirtual 98	com/google/ads/internal/d:h	()Lcom/google/ads/m;
    //   1006: getfield 543	com/google/ads/m:l	Lcom/google/ads/util/i$c;
    //   1009: invokevirtual 546	com/google/ads/util/i$c:a	()Ljava/lang/Object;
    //   1012: ifnull +99 -> 1111
    //   1015: aload_0
    //   1016: getfield 93	com/google/ads/internal/c:n	Lcom/google/ads/AdSize;
    //   1019: ifnonnull +22 -> 1041
    //   1022: ldc_w 866
    //   1025: invokestatic 709	com/google/ads/util/b:b	(Ljava/lang/String;)V
    //   1028: aload_0
    //   1029: getstatic 714	com/google/ads/AdRequest$ErrorCode:INTERNAL_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   1032: iconst_0
    //   1033: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   1036: aload_0
    //   1037: monitorexit
    //   1038: goto -1006 -> 32
    //   1041: aload_0
    //   1042: getfield 66	com/google/ads/internal/c:h	Lcom/google/ads/internal/d;
    //   1045: invokevirtual 98	com/google/ads/internal/d:h	()Lcom/google/ads/m;
    //   1048: getfield 543	com/google/ads/m:l	Lcom/google/ads/util/i$c;
    //   1051: invokevirtual 546	com/google/ads/util/i$c:a	()Ljava/lang/Object;
    //   1054: checkcast 868	[Ljava/lang/Object;
    //   1057: invokestatic 874	java/util/Arrays:asList	([Ljava/lang/Object;)Ljava/util/List;
    //   1060: aload_0
    //   1061: getfield 93	com/google/ads/internal/c:n	Lcom/google/ads/AdSize;
    //   1064: invokeinterface 878 2 0
    //   1069: ifne +60 -> 1129
    //   1072: new 227	java/lang/StringBuilder
    //   1075: dup
    //   1076: invokespecial 228	java/lang/StringBuilder:<init>	()V
    //   1079: ldc_w 880
    //   1082: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1085: aload_0
    //   1086: getfield 93	com/google/ads/internal/c:n	Lcom/google/ads/AdSize;
    //   1089: invokevirtual 805	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1092: invokevirtual 240	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1095: invokestatic 709	com/google/ads/util/b:b	(Ljava/lang/String;)V
    //   1098: aload_0
    //   1099: getstatic 714	com/google/ads/AdRequest$ErrorCode:INTERNAL_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   1102: iconst_0
    //   1103: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   1106: aload_0
    //   1107: monitorexit
    //   1108: goto -1076 -> 32
    //   1111: aload_0
    //   1112: getfield 93	com/google/ads/internal/c:n	Lcom/google/ads/AdSize;
    //   1115: ifnull +14 -> 1129
    //   1118: ldc_w 882
    //   1121: invokestatic 155	com/google/ads/util/b:e	(Ljava/lang/String;)V
    //   1124: aload_0
    //   1125: aconst_null
    //   1126: putfield 93	com/google/ads/internal/c:n	Lcom/google/ads/AdSize;
    //   1129: aload_0
    //   1130: getfield 66	com/google/ads/internal/c:h	Lcom/google/ads/internal/d;
    //   1133: iconst_0
    //   1134: invokevirtual 856	com/google/ads/internal/d:b	(Z)V
    //   1137: aload_0
    //   1138: invokespecial 883	com/google/ads/internal/c:i	()V
    //   1141: invokestatic 760	android/os/SystemClock:elapsedRealtime	()J
    //   1144: lstore 10
    //   1146: lload 4
    //   1148: lload 10
    //   1150: lload 6
    //   1152: lsub
    //   1153: lsub
    //   1154: lstore 12
    //   1156: lload 12
    //   1158: lconst_0
    //   1159: lcmp
    //   1160: ifle +9 -> 1169
    //   1163: aload_0
    //   1164: lload 12
    //   1166: invokevirtual 797	java/lang/Object:wait	(J)V
    //   1169: aload_0
    //   1170: getfield 83	com/google/ads/internal/c:r	Z
    //   1173: ifeq +41 -> 1214
    //   1176: aload_0
    //   1177: invokespecial 885	com/google/ads/internal/c:j	()V
    //   1180: goto -788 -> 392
    //   1183: astore 14
    //   1185: new 227	java/lang/StringBuilder
    //   1188: dup
    //   1189: invokespecial 228	java/lang/StringBuilder:<init>	()V
    //   1192: ldc_w 887
    //   1195: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1198: aload 14
    //   1200: invokevirtual 805	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1203: invokevirtual 240	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1206: invokestatic 665	com/google/ads/util/b:a	(Ljava/lang/String;)V
    //   1209: aload_0
    //   1210: monitorexit
    //   1211: goto -1179 -> 32
    //   1214: new 227	java/lang/StringBuilder
    //   1217: dup
    //   1218: invokespecial 228	java/lang/StringBuilder:<init>	()V
    //   1221: ldc_w 809
    //   1224: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1227: lload 4
    //   1229: invokevirtual 812	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   1232: ldc_w 889
    //   1235: invokevirtual 234	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1238: invokevirtual 240	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1241: invokestatic 220	com/google/ads/util/b:c	(Ljava/lang/String;)V
    //   1244: aload_0
    //   1245: getstatic 817	com/google/ads/AdRequest$ErrorCode:NETWORK_ERROR	Lcom/google/ads/AdRequest$ErrorCode;
    //   1248: iconst_1
    //   1249: invokevirtual 716	com/google/ads/internal/c:a	(Lcom/google/ads/AdRequest$ErrorCode;Z)V
    //   1252: goto -860 -> 392
    //
    // Exception table:
    //   from	to	target	type
    //   2	30	73	finally
    //   30	32	73	finally
    //   33	68	73	finally
    //   68	76	73	finally
    //   78	289	73	finally
    //   294	303	73	finally
    //   303	318	73	finally
    //   335	341	73	finally
    //   341	347	73	finally
    //   352	354	73	finally
    //   357	373	73	finally
    //   377	394	73	finally
    //   399	431	73	finally
    //   431	433	73	finally
    //   438	462	73	finally
    //   462	464	73	finally
    //   467	483	73	finally
    //   483	485	73	finally
    //   488	533	73	finally
    //   533	535	73	finally
    //   538	628	73	finally
    //   645	651	73	finally
    //   651	657	73	finally
    //   662	664	73	finally
    //   667	749	73	finally
    //   749	751	73	finally
    //   756	780	73	finally
    //   780	782	73	finally
    //   785	801	73	finally
    //   801	803	73	finally
    //   806	851	73	finally
    //   851	853	73	finally
    //   856	916	73	finally
    //   916	918	73	finally
    //   921	994	73	finally
    //   994	996	73	finally
    //   999	1036	73	finally
    //   1036	1038	73	finally
    //   1041	1106	73	finally
    //   1106	1108	73	finally
    //   1111	1146	73	finally
    //   1163	1169	73	finally
    //   1169	1209	73	finally
    //   1209	1211	73	finally
    //   1214	1252	73	finally
    //   2	30	376	java/lang/Throwable
    //   33	68	376	java/lang/Throwable
    //   78	289	376	java/lang/Throwable
    //   294	303	376	java/lang/Throwable
    //   303	318	376	java/lang/Throwable
    //   335	341	376	java/lang/Throwable
    //   341	347	376	java/lang/Throwable
    //   357	373	376	java/lang/Throwable
    //   399	431	376	java/lang/Throwable
    //   438	462	376	java/lang/Throwable
    //   467	483	376	java/lang/Throwable
    //   488	533	376	java/lang/Throwable
    //   538	628	376	java/lang/Throwable
    //   645	651	376	java/lang/Throwable
    //   651	657	376	java/lang/Throwable
    //   667	749	376	java/lang/Throwable
    //   756	780	376	java/lang/Throwable
    //   785	801	376	java/lang/Throwable
    //   806	851	376	java/lang/Throwable
    //   856	916	376	java/lang/Throwable
    //   921	994	376	java/lang/Throwable
    //   999	1036	376	java/lang/Throwable
    //   1041	1106	376	java/lang/Throwable
    //   1111	1146	376	java/lang/Throwable
    //   1163	1169	376	java/lang/Throwable
    //   1169	1209	376	java/lang/Throwable
    //   1214	1252	376	java/lang/Throwable
    //   294	303	397	com/google/ads/internal/c$b
    //   335	341	436	java/lang/InterruptedException
    //   645	651	754	java/lang/InterruptedException
    //   1163	1169	1183	java/lang/InterruptedException
  }

  private static class a
    implements Runnable
  {
    private final d a;
    private final WebView b;
    private final f c;
    private final AdRequest.ErrorCode d;
    private final boolean e;

    public a(d paramd, WebView paramWebView, f paramf, AdRequest.ErrorCode paramErrorCode, boolean paramBoolean)
    {
      this.a = paramd;
      this.b = paramWebView;
      this.c = paramf;
      this.d = paramErrorCode;
      this.e = paramBoolean;
    }

    public void run()
    {
      if (this.b != null)
      {
        this.b.stopLoading();
        this.b.destroy();
      }
      if (this.c != null)
        this.c.a();
      if (this.e)
      {
        AdWebView localAdWebView = this.a.k();
        localAdWebView.stopLoading();
        localAdWebView.setVisibility(8);
      }
      this.a.a(this.d);
    }
  }

  private class b extends Exception
  {
    public b(String arg2)
    {
      super(str);
    }
  }

  private class c
    implements Runnable
  {
    private final String b;
    private final String c;
    private final WebView d;

    public c(WebView paramString1, String paramString2, String arg4)
    {
      this.d = paramString1;
      this.b = paramString2;
      Object localObject;
      this.c = localObject;
    }

    public void run()
    {
      if (this.c != null)
        this.d.loadDataWithBaseURL(this.b, this.c, "text/html", "utf-8", null);
      while (true)
      {
        return;
        this.d.loadUrl(this.b);
      }
    }
  }

  public static enum d
  {
    public String e;

    static
    {
      d[] arrayOfd = new d[4];
      arrayOfd[0] = a;
      arrayOfd[1] = b;
      arrayOfd[2] = c;
      arrayOfd[3] = d;
      f = arrayOfd;
    }

    private d(String paramString)
    {
      this.e = paramString;
    }
  }

  private static class e
    implements Runnable
  {
    private final d a;
    private final WebView b;
    private final LinkedList<String> c;
    private final int d;
    private final boolean e;
    private final String f;
    private final AdSize g;

    public e(d paramd, WebView paramWebView, LinkedList<String> paramLinkedList, int paramInt, boolean paramBoolean, String paramString, AdSize paramAdSize)
    {
      this.a = paramd;
      this.b = paramWebView;
      this.c = paramLinkedList;
      this.d = paramInt;
      this.e = paramBoolean;
      this.f = paramString;
      this.g = paramAdSize;
    }

    public void run()
    {
      if (this.b != null)
      {
        this.b.stopLoading();
        this.b.destroy();
      }
      this.a.a(this.c);
      this.a.a(this.d);
      this.a.a(this.e);
      this.a.a(this.f);
      if (this.g != null)
      {
        ((h)this.a.h().k.a()).b(this.g);
        this.a.k().setAdSize(this.g);
      }
      this.a.C();
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.internal.c
 * JD-Core Version:    0.5.4
 */