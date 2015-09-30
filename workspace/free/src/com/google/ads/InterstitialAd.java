package com.google.ads;

import android.app.Activity;
import com.google.ads.internal.d;
import com.google.ads.util.i.c;

public class InterstitialAd
  implements Ad
{
  private d a;

  public InterstitialAd(Activity paramActivity, String paramString)
  {
    this(paramActivity, paramString, false);
  }

  public InterstitialAd(Activity paramActivity, String paramString, boolean paramBoolean)
  {
    this.a = new d(this, paramActivity, null, paramString, null, paramBoolean);
  }

  public boolean isReady()
  {
    return this.a.r();
  }

  public void loadAd(AdRequest paramAdRequest)
  {
    this.a.a(paramAdRequest);
  }

  public void setAdListener(AdListener paramAdListener)
  {
    this.a.h().m.a(paramAdListener);
  }

  protected void setAppEventListener(AppEventListener paramAppEventListener)
  {
    this.a.h().n.a(paramAppEventListener);
  }

  public void show()
  {
    this.a.z();
  }

  public void stopLoading()
  {
    this.a.A();
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.InterstitialAd
 * JD-Core Version:    0.5.4
 */