package com.google.ads.mediation.admob;

import android.app.Activity;
import android.view.View;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.ads.util.AdUtil;

public class AdMobAdapter
  implements MediationBannerAdapter<AdMobAdapterExtras, AdMobAdapterServerParameters>, MediationInterstitialAdapter<AdMobAdapterExtras, AdMobAdapterServerParameters>
{
  private MediationBannerListener a;
  private MediationInterstitialListener b;
  private AdView c;
  private InterstitialAd d;

  private AdRequest a(Activity paramActivity, AdMobAdapterServerParameters paramAdMobAdapterServerParameters, MediationAdRequest paramMediationAdRequest, AdMobAdapterExtras paramAdMobAdapterExtras)
  {
    AdMobAdapterExtras localAdMobAdapterExtras = new AdMobAdapterExtras(paramAdMobAdapterExtras);
    localAdMobAdapterExtras.addExtra("_norefresh", "t");
    localAdMobAdapterExtras.addExtra("gw", Integer.valueOf(1));
    if (paramAdMobAdapterServerParameters.allowHouseAds != null)
      localAdMobAdapterExtras.addExtra("mad_hac", paramAdMobAdapterServerParameters.allowHouseAds);
    AdRequest localAdRequest = new AdRequest().setBirthday(paramMediationAdRequest.getBirthday()).setGender(paramMediationAdRequest.getGender()).setKeywords(paramMediationAdRequest.getKeywords()).setLocation(paramMediationAdRequest.getLocation()).setNetworkExtras(localAdMobAdapterExtras);
    if (paramMediationAdRequest.isTesting())
      localAdRequest.addTestDevice(AdUtil.a(paramActivity));
    return localAdRequest;
  }

  private void a()
  {
    if (!b())
      return;
    throw new IllegalStateException("Adapter has already been destroyed");
  }

  private boolean b()
  {
    if ((this.c == null) && (this.d == null));
    for (int i = 1; ; i = 0)
      return i;
  }

  protected AdView a(Activity paramActivity, AdSize paramAdSize, String paramString)
  {
    return new AdView(paramActivity, paramAdSize, paramString);
  }

  protected InterstitialAd a(Activity paramActivity, String paramString)
  {
    return new InterstitialAd(paramActivity, paramString);
  }

  public void destroy()
  {
    a();
    if (this.c != null)
    {
      this.c.stopLoading();
      this.c.destroy();
      this.c = null;
    }
    if (this.d == null)
      return;
    this.d.stopLoading();
    this.d = null;
  }

  public Class<AdMobAdapterExtras> getAdditionalParametersType()
  {
    return AdMobAdapterExtras.class;
  }

  public View getBannerView()
  {
    return this.c;
  }

  public Class<AdMobAdapterServerParameters> getServerParametersType()
  {
    return AdMobAdapterServerParameters.class;
  }

  public void requestBannerAd(MediationBannerListener paramMediationBannerListener, Activity paramActivity, AdMobAdapterServerParameters paramAdMobAdapterServerParameters, AdSize paramAdSize, MediationAdRequest paramMediationAdRequest, AdMobAdapterExtras paramAdMobAdapterExtras)
  {
    this.a = paramMediationBannerListener;
    if ((paramAdSize.isAutoHeight()) || (paramAdSize.isFullWidth()))
    {
      this.c = a(paramActivity, paramAdSize, paramAdMobAdapterServerParameters.adUnitId);
      this.c.setAdListener(new a(null));
      this.c.loadAd(a(paramActivity, paramAdMobAdapterServerParameters, paramMediationAdRequest, paramAdMobAdapterExtras));
    }
    while (true)
    {
      return;
      if ((paramAdMobAdapterExtras == null) || (!paramAdMobAdapterExtras.getUseExactAdSize()));
      AdSize[] arrayOfAdSize = new AdSize[5];
      arrayOfAdSize[0] = AdSize.BANNER;
      arrayOfAdSize[1] = AdSize.IAB_BANNER;
      arrayOfAdSize[2] = AdSize.IAB_LEADERBOARD;
      arrayOfAdSize[3] = AdSize.IAB_MRECT;
      arrayOfAdSize[4] = AdSize.IAB_WIDE_SKYSCRAPER;
      paramAdSize = paramAdSize.findBestSize(arrayOfAdSize);
      if (paramAdSize == null);
      paramMediationBannerListener.onFailedToReceiveAd(this, AdRequest.ErrorCode.NO_FILL);
    }
  }

  public void requestInterstitialAd(MediationInterstitialListener paramMediationInterstitialListener, Activity paramActivity, AdMobAdapterServerParameters paramAdMobAdapterServerParameters, MediationAdRequest paramMediationAdRequest, AdMobAdapterExtras paramAdMobAdapterExtras)
  {
    this.b = paramMediationInterstitialListener;
    this.d = a(paramActivity, paramAdMobAdapterServerParameters.adUnitId);
    this.d.setAdListener(new b(null));
    this.d.loadAd(a(paramActivity, paramAdMobAdapterServerParameters, paramMediationAdRequest, paramAdMobAdapterExtras));
  }

  public void showInterstitial()
  {
    this.d.show();
  }

  private class a
    implements AdListener
  {
    private a()
    {
    }

    public void onDismissScreen(Ad paramAd)
    {
      AdMobAdapter.a(AdMobAdapter.this).onDismissScreen(AdMobAdapter.this);
    }

    public void onFailedToReceiveAd(Ad paramAd, AdRequest.ErrorCode paramErrorCode)
    {
      AdMobAdapter.a(AdMobAdapter.this).onFailedToReceiveAd(AdMobAdapter.this, paramErrorCode);
    }

    public void onLeaveApplication(Ad paramAd)
    {
      AdMobAdapter.a(AdMobAdapter.this).onLeaveApplication(AdMobAdapter.this);
    }

    public void onPresentScreen(Ad paramAd)
    {
      AdMobAdapter.a(AdMobAdapter.this).onClick(AdMobAdapter.this);
      AdMobAdapter.a(AdMobAdapter.this).onPresentScreen(AdMobAdapter.this);
    }

    public void onReceiveAd(Ad paramAd)
    {
      AdMobAdapter.a(AdMobAdapter.this).onReceivedAd(AdMobAdapter.this);
    }
  }

  private class b
    implements AdListener
  {
    private b()
    {
    }

    public void onDismissScreen(Ad paramAd)
    {
      AdMobAdapter.b(AdMobAdapter.this).onDismissScreen(AdMobAdapter.this);
    }

    public void onFailedToReceiveAd(Ad paramAd, AdRequest.ErrorCode paramErrorCode)
    {
      AdMobAdapter.b(AdMobAdapter.this).onFailedToReceiveAd(AdMobAdapter.this, paramErrorCode);
    }

    public void onLeaveApplication(Ad paramAd)
    {
      AdMobAdapter.b(AdMobAdapter.this).onLeaveApplication(AdMobAdapter.this);
    }

    public void onPresentScreen(Ad paramAd)
    {
      AdMobAdapter.b(AdMobAdapter.this).onPresentScreen(AdMobAdapter.this);
    }

    public void onReceiveAd(Ad paramAd)
    {
      AdMobAdapter.b(AdMobAdapter.this).onReceivedAd(AdMobAdapter.this);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.mediation.admob.AdMobAdapter
 * JD-Core Version:    0.5.4
 */