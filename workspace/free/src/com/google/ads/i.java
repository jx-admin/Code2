package com.google.ads;

import android.app.Activity;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.MediationServerParameters.MappingException;
import com.google.ads.mediation.NetworkExtras;
import com.google.ads.util.b;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

class i
  implements Runnable
{
  private final h a;
  private final String b;
  private final AdRequest c;
  private final HashMap<String, String> d;
  private final boolean e;
  private final WeakReference<Activity> f;

  public i(h paramh, Activity paramActivity, String paramString, AdRequest paramAdRequest, HashMap<String, String> paramHashMap)
  {
    this.a = paramh;
    this.b = paramString;
    this.f = new WeakReference(paramActivity);
    this.c = paramAdRequest;
    this.d = new HashMap(paramHashMap);
    this.e = a(this.d);
  }

  private <T extends NetworkExtras, U extends MediationServerParameters> void a(MediationAdapter<T, U> paramMediationAdapter)
    throws MediationServerParameters.MappingException, i.a, IllegalAccessException, InstantiationException
  {
    Activity localActivity = (Activity)this.f.get();
    if (localActivity == null)
      throw new a("Activity became null while trying to instantiate adapter.");
    this.a.a(paramMediationAdapter);
    Class localClass1 = paramMediationAdapter.getServerParametersType();
    MediationServerParameters localMediationServerParameters2;
    if (localClass1 != null)
    {
      localMediationServerParameters2 = (MediationServerParameters)localClass1.newInstance();
      localMediationServerParameters2.load(this.d);
    }
    for (MediationServerParameters localMediationServerParameters1 = localMediationServerParameters2; ; localMediationServerParameters1 = null)
    {
      Class localClass2 = paramMediationAdapter.getAdditionalParametersType();
      if (localClass2 != null);
      for (NetworkExtras localNetworkExtras = (NetworkExtras)this.c.getNetworkExtras(localClass2); ; localNetworkExtras = null)
      {
        MediationAdRequest localMediationAdRequest = new MediationAdRequest(this.c, localActivity, this.e);
        if (this.a.a.a())
        {
          if (!paramMediationAdapter instanceof MediationInterstitialAdapter)
            throw new a("Adapter " + this.b + " doesn't support the MediationInterstitialAdapter" + " interface.");
          ((MediationInterstitialAdapter)paramMediationAdapter).requestInterstitialAd(new k(this.a), localActivity, localMediationServerParameters1, localMediationAdRequest, localNetworkExtras);
        }
        while (true)
        {
          this.a.k();
          return;
          if (!paramMediationAdapter instanceof MediationBannerAdapter)
            throw new a("Adapter " + this.b + " doesn't support the MediationBannerAdapter interface");
          MediationBannerAdapter localMediationBannerAdapter = (MediationBannerAdapter)paramMediationAdapter;
          j localj = new j(this.a);
          AdSize localAdSize = this.a.a.b();
          localMediationBannerAdapter.requestBannerAd(localj, localActivity, localMediationServerParameters1, localAdSize, localMediationAdRequest, localNetworkExtras);
        }
      }
    }
  }

  private void a(String paramString, Throwable paramThrowable, g.a parama)
  {
    b.b(paramString, paramThrowable);
    this.a.a(false, parama);
  }

  private static boolean a(Map<String, String> paramMap)
  {
    String str = (String)paramMap.remove("gwhirl_share_location");
    if ("1".equals(str));
    for (int i = 1; ; i = 0)
    {
      return i;
      if ((str == null) || ("0".equals(str)))
        continue;
      b.b("Received an illegal value, '" + str + "', for the special share location parameter from mediation server" + " (expected '0' or '1'). Will not share the location.");
    }
  }

  public void run()
  {
    try
    {
      b.a("Trying to instantiate: " + this.b);
      a((MediationAdapter)g.a(this.b, MediationAdapter.class));
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      a("Cannot find adapter class '" + this.b + "'. Did you link the ad network's mediation adapter? Skipping ad network.", localClassNotFoundException, g.a.e);
    }
    catch (Throwable localThrowable)
    {
      a("Error while creating adapter and loading ad from ad network. Skipping ad network.", localThrowable, g.a.f);
    }
  }

  private static class a extends Exception
  {
    public a(String paramString)
    {
      super(paramString);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.i
 * JD-Core Version:    0.5.4
 */