package com.google.ads;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.util.a;
import com.google.ads.util.b;
import java.util.HashMap;

public class h
{
  final com.google.ads.internal.h a;
  private final f b;
  private boolean c;
  private boolean d;
  private g.a e;
  private final e f;
  private MediationAdapter<?, ?> g;
  private boolean h;
  private boolean i;
  private View j;
  private final Handler k;
  private final String l;
  private final AdRequest m;
  private final HashMap<String, String> n;

  public h(e parame, com.google.ads.internal.h paramh, f paramf, String paramString, AdRequest paramAdRequest, HashMap<String, String> paramHashMap)
  {
    a.b(TextUtils.isEmpty(paramString));
    this.f = parame;
    this.a = paramh;
    this.b = paramf;
    this.l = paramString;
    this.m = paramAdRequest;
    this.n = paramHashMap;
    this.c = false;
    this.d = false;
    this.e = null;
    this.g = null;
    this.h = false;
    this.i = false;
    this.j = null;
    this.k = new Handler(Looper.getMainLooper());
  }

  public f a()
  {
    return this.b;
  }

  public void a(Activity paramActivity)
  {
    monitorenter;
    try
    {
      a.b(this.h, "startLoadAdTask has already been called.");
      this.h = true;
      this.k.post(new i(this, paramActivity, this.l, this.m, this.n));
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

  void a(View paramView)
  {
    monitorenter;
    try
    {
      this.j = paramView;
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

  void a(MediationAdapter<?, ?> paramMediationAdapter)
  {
    monitorenter;
    try
    {
      this.g = paramMediationAdapter;
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

  void a(boolean paramBoolean, g.a parama)
  {
    monitorenter;
    try
    {
      this.d = paramBoolean;
      this.c = true;
      this.e = parama;
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

  public void b()
  {
    monitorenter;
    try
    {
      a.a(this.h, "destroy() called but startLoadAdTask has not been called.");
      this.k.post(new h.1(this));
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

  public boolean c()
  {
    monitorenter;
    try
    {
      boolean bool = this.c;
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

  public boolean d()
  {
    monitorenter;
    try
    {
      a.a(this.c, "isLoadAdTaskSuccessful() called when isLoadAdTaskDone() is false.");
      boolean bool = this.d;
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

  public g.a e()
  {
    monitorenter;
    while (true)
    {
      g.a locala;
      try
      {
        if (this.e == null)
        {
          locala = g.a.d;
          return locala;
        }
      }
      finally
      {
        monitorexit;
      }
    }
  }

  public View f()
  {
    monitorenter;
    try
    {
      a.a(this.c, "getAdView() called when isLoadAdTaskDone() is false.");
      View localView = this.j;
      monitorexit;
      return localView;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void g()
  {
    monitorenter;
    try
    {
      a.a(this.a.a());
    }
    finally
    {
      try
      {
        MediationInterstitialAdapter localMediationInterstitialAdapter = (MediationInterstitialAdapter)this.g;
        this.k.post(new h.2(this, localMediationInterstitialAdapter));
        label37: monitorexit;
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        b.b("In Ambassador.show(): ambassador.adapter does not implement the MediationInterstitialAdapter interface.", localClassCastException);
        break label37:
        localObject = finally;
        monitorexit;
        throw localObject;
      }
    }
  }

  public String h()
  {
    monitorenter;
    while (true)
    {
      String str1;
      try
      {
        if (this.g != null)
        {
          String str2 = this.g.getClass().getName();
          str1 = str2;
          return str1;
        }
      }
      finally
      {
        monitorexit;
      }
    }
  }

  MediationAdapter<?, ?> i()
  {
    monitorenter;
    try
    {
      MediationAdapter localMediationAdapter = this.g;
      monitorexit;
      return localMediationAdapter;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  e j()
  {
    return this.f;
  }

  void k()
  {
    monitorenter;
    try
    {
      this.i = true;
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

  boolean l()
  {
    monitorenter;
    try
    {
      boolean bool = this.i;
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
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.h
 * JD-Core Version:    0.5.4
 */