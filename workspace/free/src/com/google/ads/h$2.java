package com.google.ads;

import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.util.b;

class h$2
  implements Runnable
{
  public void run()
  {
    try
    {
      this.a.showInterstitial();
      return;
    }
    catch (Throwable localThrowable)
    {
      b.b("Error while telling adapter (" + this.b.h() + ") ad to show interstitial: ", localThrowable);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.h.2
 * JD-Core Version:    0.5.4
 */