package com.google.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.google.ads.Ad;
import com.google.ads.AdActivity;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.AppEventListener;
import com.google.ads.InterstitialAd;
import com.google.ads.ac;
import com.google.ads.ae;
import com.google.ads.ag;
import com.google.ads.f;
import com.google.ads.l;
import com.google.ads.l.a;
import com.google.ads.m;
import com.google.ads.util.AdUtil;
import com.google.ads.util.i.b;
import com.google.ads.util.i.c;
import com.google.ads.util.i.d;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class d
{
  private static final Object a = new Object();
  private final m b;
  private c c;
  private AdRequest d;
  private g e;
  private AdWebView f;
  private i g;
  private Handler h;
  private long i;
  private boolean j;
  private boolean k;
  private boolean l;
  private boolean m;
  private boolean n;
  private SharedPreferences o;
  private long p;
  private ae q;
  private boolean r;
  private LinkedList<String> s;
  private LinkedList<String> t;
  private int u = -1;
  private Boolean v;
  private com.google.ads.d w;
  private com.google.ads.e x;
  private f y;
  private String z = null;

  public d(Ad paramAd, Activity paramActivity, AdSize paramAdSize, String paramString, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    this.r = paramBoolean;
    this.e = new g();
    this.c = null;
    this.d = null;
    this.k = false;
    this.h = new Handler();
    this.p = 60000L;
    this.l = false;
    this.n = false;
    this.m = true;
    if (paramActivity == null)
    {
      l locall2 = l.a();
      AdView localAdView2;
      label102: InterstitialAd localInterstitialAd2;
      if (paramAd instanceof AdView)
      {
        localAdView2 = (AdView)paramAd;
        if (!paramAd instanceof InterstitialAd)
          break label157;
        localInterstitialAd2 = (InterstitialAd)paramAd;
        label115: if (paramAdSize != null)
          break label163;
      }
      for (h localh2 = h.a; ; localh2 = h.a(paramAdSize))
      {
        this.b = new m(locall2, paramAd, localAdView2, localInterstitialAd2, paramString, null, null, paramViewGroup, localh2);
        return;
        localAdView2 = null;
        break label102:
        label157: localInterstitialAd2 = null;
        label163: break label115:
      }
    }
    while (true)
    {
      synchronized (a)
      {
        this.o = paramActivity.getApplicationContext().getSharedPreferences("GoogleAdMobAdsPrefs", 0);
        if (paramBoolean)
        {
          long l1 = this.o.getLong("Timeout" + paramString, -1L);
          if (l1 < 0L)
          {
            this.i = 5000L;
            l locall1 = l.a();
            if (!paramAd instanceof AdView)
              break label440;
            localAdView1 = (AdView)paramAd;
            if (!paramAd instanceof InterstitialAd)
              break label446;
            localInterstitialAd1 = (InterstitialAd)paramAd;
            Context localContext = paramActivity.getApplicationContext();
            if (paramAdSize != null)
              break label452;
            localh1 = h.a;
            this.b = new m(locall1, paramAd, localAdView1, localInterstitialAd1, paramString, paramActivity, localContext, paramViewGroup, localh1);
            this.q = new ae(this);
            this.s = new LinkedList();
            this.t = new LinkedList();
            a();
            AdUtil.h((Context)this.b.f.a());
            this.w = new com.google.ads.d();
            this.x = new com.google.ads.e(this);
            this.v = null;
            this.y = null;
          }
          this.i = l1;
        }
      }
      this.i = 60000L;
      continue;
      label440: AdView localAdView1 = null;
      continue;
      label446: InterstitialAd localInterstitialAd1 = null;
      continue;
      label452: h localh1 = h.a(paramAdSize, paramActivity.getApplicationContext());
    }
  }

  private void a(f paramf, Boolean paramBoolean)
  {
    Object localObject = paramf.d();
    if (localObject == null)
    {
      localObject = new ArrayList();
      ((List)localObject).add("http://e.admob.com/imp?ad_loc=@gw_adlocid@&qdata=@gw_qdata@&ad_network_id=@gw_adnetid@&js=@gw_sdkver@&session_id=@gw_sessid@&seq_num=@gw_seqnum@&nr=@gw_adnetrefresh@&adt=@gw_adt@&aec=@gw_aec@");
    }
    String str = paramf.b();
    a((List)localObject, paramf.a(), str, paramf.c(), paramBoolean, this.e.d(), this.e.e());
  }

  private void a(List<String> paramList, String paramString)
  {
    Object localObject;
    if (paramList == null)
    {
      localObject = new ArrayList();
      ((List)localObject).add("http://e.admob.com/nofill?ad_loc=@gw_adlocid@&qdata=@gw_qdata@&js=@gw_sdkver@&session_id=@gw_sessid@&seq_num=@gw_seqnum@&adt=@gw_adt@&aec=@gw_aec@");
    }
    while (true)
    {
      a((List)localObject, null, null, paramString, null, this.e.d(), this.e.e());
      return;
      localObject = paramList;
    }
  }

  private void a(List<String> paramList, String paramString1, String paramString2, String paramString3, Boolean paramBoolean, String paramString4, String paramString5)
  {
    String str1 = AdUtil.a((Context)this.b.f.a());
    com.google.ads.b localb = com.google.ads.b.a();
    String str2 = localb.b().toString();
    String str3 = localb.c().toString();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
      new Thread(new ac(com.google.ads.g.a((String)localIterator.next(), (String)this.b.d.a(), paramBoolean, str1, paramString1, paramString2, paramString3, str2, str3, paramString4, paramString5), (Context)this.b.f.a())).start();
    this.e.b();
  }

  private void b(f paramf, Boolean paramBoolean)
  {
    Object localObject = paramf.e();
    if (localObject == null)
    {
      localObject = new ArrayList();
      ((List)localObject).add("http://e.admob.com/clk?ad_loc=@gw_adlocid@&qdata=@gw_qdata@&ad_network_id=@gw_adnetid@&js=@gw_sdkver@&session_id=@gw_sessid@&seq_num=@gw_seqnum@&nr=@gw_adnetrefresh@");
    }
    String str = paramf.b();
    a((List)localObject, paramf.a(), str, paramf.c(), paramBoolean, null, null);
  }

  public void A()
  {
    monitorenter;
    try
    {
      if (this.c != null)
      {
        this.c.a();
        this.c = null;
      }
      if (this.f != null)
        this.f.stopLoading();
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

  protected void B()
  {
    monitorenter;
    Activity localActivity;
    Iterator localIterator;
    try
    {
      localActivity = (Activity)this.b.e.a();
      if (localActivity == null)
        com.google.ads.util.b.e("activity was null while trying to ping click tracking URLs.");
      do
      {
        return;
        localIterator = this.t.iterator();
      }
      while (!localIterator.hasNext());
    }
    finally
    {
      monitorexit;
    }
  }

  protected void C()
  {
    monitorenter;
    try
    {
      this.c = null;
      this.k = true;
      this.f.setVisibility(0);
      if (this.b.a())
        a(this.f);
      this.e.g();
      if (this.b.a())
        x();
      com.google.ads.util.b.c("onReceiveAd()");
      AdListener localAdListener = (AdListener)this.b.m.a();
      if (localAdListener != null)
        localAdListener.onReceiveAd((Ad)this.b.h.a());
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

  public void a()
  {
    monitorenter;
    try
    {
      this.f = new AdWebView(this.b, ((h)this.b.k.a()).b());
      this.f.setVisibility(8);
      this.g = i.a(this, a.c, true, this.b.b());
      this.f.setWebViewClient(this.g);
      l.a locala = (l.a)((l)this.b.a.a()).a.a();
      if ((AdUtil.a < ((Integer)locala.a.a()).intValue()) && (!((h)this.b.k.a()).a()))
      {
        com.google.ads.util.b.a("Disabling hardware acceleration for a banner.");
        this.f.b();
      }
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

  public void a(float paramFloat)
  {
    monitorenter;
    try
    {
      long l1 = this.p;
      this.p = ()(1000.0F * paramFloat);
      if ((s()) && (this.p != l1))
      {
        e();
        f();
      }
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

  public void a(int paramInt)
  {
    monitorenter;
    try
    {
      this.u = paramInt;
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

  public void a(long paramLong)
  {
    synchronized (a)
    {
      SharedPreferences.Editor localEditor = this.o.edit();
      localEditor.putLong("Timeout" + this.b.d, paramLong);
      localEditor.commit();
      if (this.r)
        this.i = paramLong;
      return;
    }
  }

  public void a(View paramView)
  {
    ((ViewGroup)this.b.g.a()).removeAllViews();
    ((ViewGroup)this.b.g.a()).addView(paramView);
  }

  public void a(View paramView, com.google.ads.h paramh, f paramf, boolean paramBoolean)
  {
    monitorenter;
    try
    {
      com.google.ads.util.b.a("AdManager.onReceiveGWhirlAd() called.");
      this.k = true;
      this.y = paramf;
      if (this.b.a())
      {
        a(paramView);
        a(paramf, Boolean.valueOf(paramBoolean));
      }
      this.x.d(paramh);
      AdListener localAdListener = (AdListener)this.b.m.a();
      if (localAdListener != null)
        localAdListener.onReceiveAd((Ad)this.b.h.a());
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
      this.c = null;
      if (paramErrorCode == AdRequest.ErrorCode.NETWORK_ERROR)
      {
        a(60.0F);
        if (!s())
          g();
      }
      if (this.b.b())
      {
        if (paramErrorCode != AdRequest.ErrorCode.NO_FILL)
          break label126;
        this.e.B();
      }
      do
      {
        com.google.ads.util.b.c("onFailedToReceiveAd(" + paramErrorCode + ")");
        AdListener localAdListener = (AdListener)this.b.m.a();
        if (localAdListener != null)
          localAdListener.onFailedToReceiveAd((Ad)this.b.h.a(), paramErrorCode);
        label126: return;
      }
      while (paramErrorCode != AdRequest.ErrorCode.NETWORK_ERROR);
    }
    finally
    {
      monitorexit;
    }
  }

  public void a(AdRequest paramAdRequest)
  {
    monitorenter;
    while (true)
    {
      try
      {
        if (p())
        {
          com.google.ads.util.b.e("loadAd called while the ad is already loading, so aborting.");
          return;
        }
        if (!AdActivity.isShowing())
          break label38;
      }
      finally
      {
        monitorexit;
      }
      label38: if ((!AdUtil.c((Context)this.b.f.a())) || (!AdUtil.b((Context)this.b.f.a())))
        continue;
      long l1 = this.o.getLong("GoogleAdMobDoritosLife", 60000L);
      if (ag.a((Context)this.b.f.a(), l1))
        ag.a((Activity)this.b.e.a());
      this.k = false;
      this.s.clear();
      this.d = paramAdRequest;
      if (this.w.a())
        this.x.a(this.w.b(), paramAdRequest);
      this.c = new c(this);
      this.c.a(paramAdRequest);
    }
  }

  public void a(com.google.ads.c paramc)
  {
    monitorenter;
    try
    {
      this.c = null;
      if (paramc.d())
      {
        a(paramc.e());
        if (!this.l)
          f();
      }
      do
      {
        this.x.a(paramc, this.d);
        return;
      }
      while (!this.l);
    }
    finally
    {
      monitorexit;
    }
  }

  public void a(f paramf, boolean paramBoolean)
  {
    monitorenter;
    try
    {
      Locale localLocale = Locale.US;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Boolean.valueOf(paramBoolean);
      com.google.ads.util.b.a(String.format(localLocale, "AdManager.onGWhirlAdClicked(%b) called.", arrayOfObject));
      b(paramf, Boolean.valueOf(paramBoolean));
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

  public void a(Runnable paramRunnable)
  {
    this.h.post(paramRunnable);
  }

  public void a(String paramString)
  {
    Uri localUri = new Uri.Builder().encodedQuery(paramString).build();
    StringBuilder localStringBuilder = new StringBuilder();
    HashMap localHashMap = AdUtil.b(localUri);
    Iterator localIterator = localHashMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localStringBuilder.append(str).append(" = ").append((String)localHashMap.get(str)).append("\n");
    }
    this.z = localStringBuilder.toString().trim();
    if (!TextUtils.isEmpty(this.z))
      return;
    this.z = null;
  }

  public void a(String paramString1, String paramString2)
  {
    monitorenter;
    try
    {
      AppEventListener localAppEventListener = (AppEventListener)this.b.n.a();
      if (localAppEventListener != null)
        localAppEventListener.onAppEvent((Ad)this.b.h.a(), paramString1, paramString2);
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

  protected void a(LinkedList<String> paramLinkedList)
  {
    monitorenter;
    String str;
    try
    {
      Iterator localIterator = paramLinkedList.iterator();
      if (!localIterator.hasNext())
        break label59;
      str = (String)localIterator.next();
    }
    finally
    {
      monitorexit;
    }
    label59: this.t = paramLinkedList;
    monitorexit;
  }

  public void a(boolean paramBoolean)
  {
    monitorenter;
    try
    {
      this.j = paramBoolean;
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

  public void b()
  {
    monitorenter;
    try
    {
      if (this.x != null)
        this.x.b();
      this.b.m.a(null);
      this.b.n.a(null);
      A();
      if (this.f != null)
        this.f.destroy();
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

  public void b(long paramLong)
  {
    monitorenter;
    if (paramLong > 0L);
    try
    {
      this.o.edit().putLong("GoogleAdMobDoritosLife", paramLong).commit();
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

  public void b(com.google.ads.c paramc)
  {
    monitorenter;
    try
    {
      com.google.ads.util.b.a("AdManager.onGWhirlNoFill() called.");
      a(paramc.i(), paramc.c());
      AdListener localAdListener = (AdListener)this.b.m.a();
      if (localAdListener != null)
        localAdListener.onFailedToReceiveAd((Ad)this.b.h.a(), AdRequest.ErrorCode.NO_FILL);
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

  protected void b(String paramString)
  {
    monitorenter;
    try
    {
      com.google.ads.util.b.a("Adding a tracking URL: " + paramString);
      this.s.add(paramString);
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
    this.v = Boolean.valueOf(paramBoolean);
  }

  public String c()
  {
    return this.z;
  }

  public void d()
  {
    monitorenter;
    try
    {
      this.m = false;
      com.google.ads.util.b.a("Refreshing is no longer allowed on this AdView.");
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

  public void e()
  {
    monitorenter;
    while (true)
      try
      {
        if (this.l)
        {
          com.google.ads.util.b.a("Disabling refreshing.");
          this.h.removeCallbacks(this.q);
          this.l = false;
          return;
        }
      }
      finally
      {
        monitorexit;
      }
  }

  public void f()
  {
    monitorenter;
    while (true)
    {
      try
      {
        this.n = false;
        if (!this.b.a())
          break label110;
        if (!this.m)
          break label101;
        if (!this.l)
        {
          com.google.ads.util.b.a("Enabling refreshing every " + this.p + " milliseconds.");
          this.h.postDelayed(this.q, this.p);
          this.l = true;
          return;
        }
      }
      finally
      {
        monitorexit;
      }
      label101: com.google.ads.util.b.a("Refreshing disabled on this AdView");
      continue;
      label110: com.google.ads.util.b.a("Tried to enable refreshing on something other than an AdView.");
    }
  }

  public void g()
  {
    f();
    this.n = true;
  }

  public m h()
  {
    return this.b;
  }

  public com.google.ads.d i()
  {
    monitorenter;
    try
    {
      com.google.ads.d locald = this.w;
      monitorexit;
      return locald;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public c j()
  {
    monitorenter;
    try
    {
      c localc = this.c;
      monitorexit;
      return localc;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public AdWebView k()
  {
    monitorenter;
    try
    {
      AdWebView localAdWebView = this.f;
      monitorexit;
      return localAdWebView;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public i l()
  {
    monitorenter;
    try
    {
      i locali = this.g;
      monitorexit;
      return locali;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public g m()
  {
    return this.e;
  }

  public int n()
  {
    monitorenter;
    try
    {
      int i1 = this.u;
      monitorexit;
      return i1;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public long o()
  {
    return this.i;
  }

  public boolean p()
  {
    monitorenter;
    try
    {
      c localc = this.c;
      if (localc != null)
      {
        i1 = 1;
        return i1;
      }
      int i1 = 0;
    }
    finally
    {
      monitorexit;
    }
  }

  public boolean q()
  {
    monitorenter;
    try
    {
      boolean bool = this.j;
      monitorexit;
      return bool;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public boolean r()
  {
    monitorenter;
    try
    {
      boolean bool = this.k;
      monitorexit;
      return bool;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public boolean s()
  {
    monitorenter;
    try
    {
      boolean bool = this.l;
      monitorexit;
      return bool;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void t()
  {
    monitorenter;
    try
    {
      this.e.C();
      com.google.ads.util.b.c("onDismissScreen()");
      AdListener localAdListener = (AdListener)this.b.m.a();
      if (localAdListener != null)
        localAdListener.onDismissScreen((Ad)this.b.h.a());
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

  public void u()
  {
    monitorenter;
    try
    {
      com.google.ads.util.b.c("onPresentScreen()");
      AdListener localAdListener = (AdListener)this.b.m.a();
      if (localAdListener != null)
        localAdListener.onPresentScreen((Ad)this.b.h.a());
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

  public void v()
  {
    monitorenter;
    try
    {
      com.google.ads.util.b.c("onLeaveApplication()");
      AdListener localAdListener = (AdListener)this.b.m.a();
      if (localAdListener != null)
        localAdListener.onLeaveApplication((Ad)this.b.h.a());
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

  public void w()
  {
    this.e.f();
    B();
  }

  public void x()
  {
    monitorenter;
    Activity localActivity;
    Iterator localIterator;
    try
    {
      localActivity = (Activity)this.b.e.a();
      if (localActivity == null)
        com.google.ads.util.b.e("activity was null while trying to ping tracking URLs.");
      do
      {
        return;
        localIterator = this.s.iterator();
      }
      while (!localIterator.hasNext());
    }
    finally
    {
      monitorexit;
    }
  }

  public void y()
  {
    monitorenter;
    while (true)
    {
      try
      {
        if (this.d == null)
          break label114;
        if (!this.b.a())
          break label105;
        if ((((AdView)this.b.i.a()).isShown()) && (AdUtil.d()))
        {
          com.google.ads.util.b.c("Refreshing ad.");
          a(this.d);
          if (!this.n)
            break label86;
          e();
          return;
        }
      }
      finally
      {
        monitorexit;
      }
      label86: this.h.postDelayed(this.q, this.p);
      continue;
      label105: com.google.ads.util.b.a("Tried to refresh an ad that wasn't an AdView.");
      continue;
      label114: com.google.ads.util.b.a("Tried to refresh before calling loadAd().");
    }
  }

  public void z()
  {
    monitorenter;
    while (true)
    {
      try
      {
        com.google.ads.util.a.a(this.b.b());
        if (!this.k)
          break label101;
        this.k = false;
        if (this.v == null)
          com.google.ads.util.b.b("isMediationFlag is null in show() with isReady() true. we should have an ad and know whether this is a mediation request or not. ");
        do
        {
          return;
          if (!this.v.booleanValue())
            break label80;
        }
        while (!this.x.c());
      }
      finally
      {
        monitorexit;
      }
      label80: AdActivity.launchAdActivity(this, new e("interstitial"));
      x();
      continue;
      label101: com.google.ads.util.b.c("Cannot show interstitial because it is not loaded and ready.");
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.internal.d
 * JD-Core Version:    0.5.4
 */