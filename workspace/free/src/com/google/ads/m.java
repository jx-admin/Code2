package com.google.ads;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import com.google.ads.internal.h;
import com.google.ads.internal.state.AdState;
import com.google.ads.util.i;
import com.google.ads.util.i.b;
import com.google.ads.util.i.c;
import com.google.ads.util.i.d;

public class m extends i
{
  public final i.b<l> a;
  public final i.c<AdState> b = new i.c(this, "currentAd", null);
  public final i.c<AdState> c = new i.c(this, "nextAd", null);
  public final i.b<String> d;
  public final i.d<Activity> e;
  public final i.b<Context> f;
  public final i.b<ViewGroup> g;
  public final i.b<Ad> h;
  public final i.b<AdView> i;
  public final i.b<InterstitialAd> j;
  public final i.b<h> k;
  public final i.c<AdSize[]> l;
  public final i.c<AdListener> m = new i.c(this, "adListener");
  public final i.c<AppEventListener> n = new i.c(this, "appEventListener");

  public m(l paraml, Ad paramAd, AdView paramAdView, InterstitialAd paramInterstitialAd, String paramString, Activity paramActivity, Context paramContext, ViewGroup paramViewGroup, h paramh)
  {
    this.a = new i.b(this, "appState", paraml);
    this.h = new i.b(this, "ad", paramAd);
    this.i = new i.b(this, "adView", paramAdView);
    this.k = new i.b(this, "adType", paramh);
    this.d = new i.b(this, "adUnitId", paramString);
    this.e = new i.d(this, "activity", paramActivity);
    this.j = new i.b(this, "interstitialAd", paramInterstitialAd);
    this.g = new i.b(this, "bannerContainer", paramViewGroup);
    this.f = new i.b(this, "applicationContext", paramContext);
    this.l = new i.c(this, "adSizes", null);
  }

  public boolean a()
  {
    if (!b());
    for (int i1 = 1; ; i1 = 0)
      return i1;
  }

  public boolean b()
  {
    return ((h)this.k.a()).a();
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.m
 * JD-Core Version:    0.5.4
 */