package com.google.ads;

import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.util.a;
import com.google.ads.util.b;

class j
  implements MediationBannerListener
{
  private final h a;
  private boolean b;

  public j(h paramh)
  {
    this.a = paramh;
  }

  public void onClick(MediationBannerAdapter<?, ?> paramMediationBannerAdapter)
  {
    synchronized (this.a)
    {
      a.a(this.a.c());
      this.a.j().a(this.a, this.b);
      return;
    }
  }

  public void onDismissScreen(MediationBannerAdapter<?, ?> paramMediationBannerAdapter)
  {
    synchronized (this.a)
    {
      this.a.j().b(this.a);
      return;
    }
  }

  public void onFailedToReceiveAd(MediationBannerAdapter<?, ?> paramMediationBannerAdapter, AdRequest.ErrorCode paramErrorCode)
  {
    synchronized (this.a)
    {
      a.a(paramMediationBannerAdapter, this.a.i());
      b.a("Mediation adapter " + paramMediationBannerAdapter.getClass().getName() + " failed to receive ad with error code: " + paramErrorCode);
      if (!this.a.c())
      {
        h localh2 = this.a;
        if (paramErrorCode != AdRequest.ErrorCode.NO_FILL)
          break label94;
        locala = g.a.b;
        localh2.a(false, locala);
      }
      return;
      label94: g.a locala = g.a.c;
    }
  }

  public void onLeaveApplication(MediationBannerAdapter<?, ?> paramMediationBannerAdapter)
  {
    synchronized (this.a)
    {
      this.a.j().c(this.a);
      return;
    }
  }

  public void onPresentScreen(MediationBannerAdapter<?, ?> paramMediationBannerAdapter)
  {
    synchronized (this.a)
    {
      this.a.j().a(this.a);
      return;
    }
  }

  public void onReceivedAd(MediationBannerAdapter<?, ?> paramMediationBannerAdapter)
  {
    synchronized (this.a)
    {
      a.a(paramMediationBannerAdapter, this.a.i());
      try
      {
        this.a.a(paramMediationBannerAdapter.getBannerView());
        if (this.a.c())
          break label130;
        this.b = false;
        this.a.a(true, g.a.a);
        monitorexit;
      }
      catch (Throwable localThrowable)
      {
        b.b("Error while getting banner View from adapter (" + this.a.h() + "): ", localThrowable);
        if (!this.a.c())
          this.a.a(false, g.a.f);
        monitorexit;
      }
    }
    label130: this.b = true;
    this.a.j().a(this.a, this.a.f());
    monitorexit;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.j
 * JD-Core Version:    0.5.4
 */