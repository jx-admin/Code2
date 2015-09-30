package com.google.ads;

import android.app.Activity;
import android.view.View;
import com.google.ads.internal.d;
import com.google.ads.internal.g;
import com.google.ads.util.b;
import com.google.ads.util.i.b;
import com.google.ads.util.i.d;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class e
{
  private final d a;
  private h b = null;
  private Object c = new Object();
  private Thread d = null;
  private Object e = new Object();
  private boolean f = false;
  private Object g = new Object();

  protected e()
  {
    this.a = null;
  }

  public e(d paramd)
  {
    com.google.ads.util.a.b(paramd);
    this.a = paramd;
  }

  public static boolean a(c paramc, d paramd)
  {
    if (paramc.j() == null);
    for (int i = 1; ; i = 1)
      while (true)
      {
        return i;
        if (paramd.h().b())
        {
          if (!paramc.j().a())
          {
            b.e("InterstitialAd received a mediation response corresponding to a non-interstitial ad. Make sure you specify 'interstitial' as the ad-type in the mediation UI.");
            i = 0;
          }
          i = 1;
        }
        AdSize localAdSize1 = ((com.google.ads.internal.h)paramd.h().k.a()).b();
        if (paramc.j().a())
        {
          b.e("AdView received a mediation response corresponding to an interstitial ad. Make sure you specify the banner ad size corresponding to the AdSize you used in your AdView  (" + localAdSize1 + ") in the ad-type field in the mediation UI.");
          i = 0;
        }
        AdSize localAdSize2 = paramc.j().b();
        if (localAdSize2 == localAdSize1)
          break;
        b.e("Mediation server returned ad size: '" + localAdSize2 + "', while the AdView was created with ad size: '" + localAdSize1 + "'. Using the ad-size passed to the AdView on creation.");
        i = 0;
      }
  }

  private boolean a(h paramh, String paramString)
  {
    if (e() != paramh)
      b.c("GWController: ignoring callback to " + paramString + " from non showing ambassador with adapter class: '" + paramh.h() + "'.");
    for (int i = 0; ; i = 1)
      return i;
  }

  private boolean a(String paramString, Activity paramActivity, AdRequest paramAdRequest, f paramf, HashMap<String, String> paramHashMap, long paramLong)
  {
    label110: int i;
    synchronized (new h(this, (com.google.ads.internal.h)this.a.h().k.a(), paramf, paramString, paramAdRequest, paramHashMap))
    {
      ???.a(paramActivity);
    }
    label219: return i;
  }

  private void b(c paramc, AdRequest paramAdRequest)
  {
    while (true)
    {
      HashMap localHashMap;
      f localf;
      String str4;
      Activity localActivity;
      synchronized (this.e)
      {
        com.google.ads.util.a.a(Thread.currentThread(), this.d);
        List localList1 = paramc.f();
        if (paramc.a())
        {
          l = paramc.b();
          Iterator localIterator1 = localList1.iterator();
          Iterator localIterator2;
          do
          {
            if (!localIterator1.hasNext())
              break label295;
            a locala = (a)localIterator1.next();
            b.a("Looking to fetch ads from network: " + locala.b());
            List localList2 = locala.c();
            localHashMap = locala.e();
            localList3 = locala.d();
            String str1 = locala.a();
            String str2 = locala.b();
            String str3 = paramc.c();
            if (localList3 == null)
              break label242;
            localf = new f(str1, str2, str3, localList3, paramc.h(), paramc.i());
            localIterator2 = localList2.iterator();
          }
          while (!localIterator2.hasNext());
          str4 = (String)localIterator2.next();
          localActivity = (Activity)this.a.h().e.a();
          if (localActivity != null)
            break label251;
          b.a("Activity is null while mediating.  Terminating mediation thread.");
          return;
        }
      }
      long l = 10000L;
      continue;
      label242: List localList3 = paramc.g();
      continue;
      label251: this.a.m().c();
      if (a(str4, localActivity, paramAdRequest, localf, localHashMap, l))
        continue;
      if (!d())
        continue;
      b.a("GWController.destroy() called. Terminating mediation thread.");
      continue;
      label295: this.a.a(new e.7(this, paramc));
    }
  }

  private boolean d()
  {
    synchronized (this.g)
    {
      boolean bool = this.f;
      return bool;
    }
  }

  private h e()
  {
    synchronized (this.c)
    {
      h localh = this.b;
      return localh;
    }
  }

  private boolean e(h paramh)
  {
    int i;
    synchronized (this.g)
    {
      if (d())
      {
        paramh.b();
        i = 1;
      }
      else
      {
        i = 0;
      }
    }
    return i;
  }

  public void a(c paramc, AdRequest paramAdRequest)
  {
    synchronized (this.e)
    {
      if (a())
      {
        b.c("Mediation thread is not done executing previous mediation  request. Ignoring new mediation request");
      }
      else
      {
        a(paramc, this.a);
        this.d = new Thread(new e.1(this, paramc, paramAdRequest));
        this.d.start();
      }
    }
  }

  public void a(h paramh)
  {
    if (!a(paramh, "onPresentScreen"));
    while (true)
    {
      return;
      this.a.a(new e.4(this));
    }
  }

  public void a(h paramh, View paramView)
  {
    if (e() != paramh)
      b.c("GWController: ignoring onAdRefreshed() callback from non-showing ambassador (adapter class name is '" + paramh.h() + "').");
    while (true)
    {
      return;
      this.a.m().a(g.a.a);
      f localf = this.b.a();
      this.a.a(new e.3(this, paramView, localf));
    }
  }

  public void a(h paramh, boolean paramBoolean)
  {
    if (!a(paramh, "onAdClicked()"));
    while (true)
    {
      return;
      f localf = paramh.a();
      this.a.a(new e.2(this, localf, paramBoolean));
    }
  }

  public boolean a()
  {
    while (true)
    {
      synchronized (this.e)
      {
        if (this.d == null)
          break label25;
        i = 1;
        return i;
      }
      label25: int i = 0;
    }
  }

  public void b()
  {
    synchronized (this.g)
    {
      this.f = true;
      d(null);
      synchronized (this.e)
      {
        if (this.d != null)
          this.d.interrupt();
        return;
      }
    }
  }

  public void b(h paramh)
  {
    if (!a(paramh, "onDismissScreen"));
    while (true)
    {
      return;
      this.a.a(new e.5(this));
    }
  }

  public void c(h paramh)
  {
    if (!a(paramh, "onLeaveApplication"));
    while (true)
    {
      return;
      this.a.a(new e.6(this));
    }
  }

  public boolean c()
  {
    com.google.ads.util.a.a(this.a.h().b());
    h localh = e();
    if (localh != null)
      localh.g();
    for (int i = 1; ; i = 0)
    {
      return i;
      b.b("There is no ad ready to show.");
    }
  }

  public void d(h paramh)
  {
    synchronized (this.c)
    {
      if (this.b != paramh)
      {
        if (this.b != null)
          this.b.b();
        this.b = paramh;
      }
      return;
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.e
 * JD-Core Version:    0.5.4
 */