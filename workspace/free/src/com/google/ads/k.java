package com.google.ads;

import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.ads.util.a;
import com.google.ads.util.b;

class k
  implements MediationInterstitialListener
{
  private final h a;

  k(h paramh)
  {
    this.a = paramh;
  }

  public void onDismissScreen(MediationInterstitialAdapter<?, ?> paramMediationInterstitialAdapter)
  {
    synchronized (this.a)
    {
      this.a.j().b(this.a);
      return;
    }
  }

  // ERROR //
  public void onFailedToReceiveAd(MediationInterstitialAdapter<?, ?> paramMediationInterstitialAdapter, AdRequest.ErrorCode paramErrorCode)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 15	com/google/ads/k:a	Lcom/google/ads/h;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: aload_1
    //   8: aload_0
    //   9: getfield 15	com/google/ads/k:a	Lcom/google/ads/h;
    //   12: invokevirtual 34	com/google/ads/h:i	()Lcom/google/ads/mediation/MediationAdapter;
    //   15: invokestatic 39	com/google/ads/util/a:a	(Ljava/lang/Object;Ljava/lang/Object;)V
    //   18: new 41	java/lang/StringBuilder
    //   21: dup
    //   22: invokespecial 42	java/lang/StringBuilder:<init>	()V
    //   25: ldc 44
    //   27: invokevirtual 48	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   30: aload_1
    //   31: invokevirtual 52	java/lang/Object:getClass	()Ljava/lang/Class;
    //   34: invokevirtual 58	java/lang/Class:getName	()Ljava/lang/String;
    //   37: invokevirtual 48	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: ldc 60
    //   42: invokevirtual 48	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: aload_2
    //   46: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   49: invokevirtual 66	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   52: invokestatic 71	com/google/ads/util/b:a	(Ljava/lang/String;)V
    //   55: aload_0
    //   56: getfield 15	com/google/ads/k:a	Lcom/google/ads/h;
    //   59: invokevirtual 75	com/google/ads/h:c	()Z
    //   62: ifeq +11 -> 73
    //   65: ldc 77
    //   67: invokestatic 79	com/google/ads/util/b:b	(Ljava/lang/String;)V
    //   70: aload_3
    //   71: monitorexit
    //   72: return
    //   73: aload_0
    //   74: getfield 15	com/google/ads/k:a	Lcom/google/ads/h;
    //   77: astore 5
    //   79: aload_2
    //   80: getstatic 85	com/google/ads/AdRequest$ErrorCode:NO_FILL	Lcom/google/ads/AdRequest$ErrorCode;
    //   83: if_acmpne +26 -> 109
    //   86: getstatic 90	com/google/ads/g$a:b	Lcom/google/ads/g$a;
    //   89: astore 6
    //   91: aload 5
    //   93: iconst_0
    //   94: aload 6
    //   96: invokevirtual 93	com/google/ads/h:a	(ZLcom/google/ads/g$a;)V
    //   99: goto -29 -> 70
    //   102: astore 4
    //   104: aload_3
    //   105: monitorexit
    //   106: aload 4
    //   108: athrow
    //   109: getstatic 95	com/google/ads/g$a:c	Lcom/google/ads/g$a;
    //   112: astore 6
    //   114: goto -23 -> 91
    //
    // Exception table:
    //   from	to	target	type
    //   7	106	102	finally
    //   109	114	102	finally
  }

  public void onLeaveApplication(MediationInterstitialAdapter<?, ?> paramMediationInterstitialAdapter)
  {
    synchronized (this.a)
    {
      this.a.j().c(this.a);
      return;
    }
  }

  public void onPresentScreen(MediationInterstitialAdapter<?, ?> paramMediationInterstitialAdapter)
  {
    synchronized (this.a)
    {
      this.a.j().a(this.a);
      return;
    }
  }

  public void onReceivedAd(MediationInterstitialAdapter<?, ?> paramMediationInterstitialAdapter)
  {
    synchronized (this.a)
    {
      a.a(paramMediationInterstitialAdapter, this.a.i());
      if (this.a.c())
      {
        b.b("Got an onReceivedAd() callback after loadAdTask is done from an interstitial adapter. Ignoring callback.");
        return;
      }
      this.a.a(true, g.a.a);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.k
 * JD-Core Version:    0.5.4
 */