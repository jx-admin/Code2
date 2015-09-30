package com.google.ads;

import com.google.ads.util.i;
import com.google.ads.util.i.b;
import com.google.ads.util.i.c;

public class l extends i
{
  private static final l b = new l();
  public final i.b<a> a = new i.b(this, "constants", new a());

  public static l a()
  {
    return b;
  }

  public static final class a extends i
  {
    public final i.c<Integer> a = new i.c(this, "minHwAccelerationVersionBanner", Integer.valueOf(17));
    public final i.c<Integer> b = new i.c(this, "minHwAccelerationVersionOverlay", Integer.valueOf(14));
    public final i.c<String> c = new i.c(this, "mraidBannerPath", "http://media.admob.com/mraid/v1/mraid_app_banner.js");
    public final i.c<String> d = new i.c(this, "mraidExpandedBannerPath", "http://media.admob.com/mraid/v1/mraid_app_expanded_banner.js");
    public final i.c<String> e = new i.c(this, "mraidInterstitialPath", "http://media.admob.com/mraid/v1/mraid_app_interstitial.js");
    public final i.c<Long> f = new i.c(this, "appCacheMaxSize", Long.valueOf(0L));
    public final i.c<Long> g = new i.c(this, "appCacheMaxSizePaddingInBytes", Long.valueOf(131072L));
    public final i.c<Long> h = new i.c(this, "maxTotalAppCacheQuotaInBytes", Long.valueOf(5242880L));
    public final i.c<Long> i = new i.c(this, "maxTotalDatabaseQuotaInBytes", Long.valueOf(5242880L));
    public final i.c<Long> j = new i.c(this, "maxDatabaseQuotaPerOriginInBytes", Long.valueOf(1048576L));
    public final i.c<Long> k = new i.c(this, "databaseQuotaIncreaseStepInBytes", Long.valueOf(131072L));
    public final i.c<Boolean> l = new i.c(this, "isInitialized", Boolean.valueOf(false));
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.l
 * JD-Core Version:    0.5.4
 */