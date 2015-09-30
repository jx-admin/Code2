package com.google.ads.mediation.admob;

import com.google.ads.mediation.NetworkExtras;
import java.util.HashMap;
import java.util.Map;

public class AdMobAdapterExtras
  implements NetworkExtras
{
  private boolean a = false;
  private boolean b = false;
  private Map<String, Object> c;

  public AdMobAdapterExtras()
  {
    clearExtras();
  }

  public AdMobAdapterExtras(AdMobAdapterExtras paramAdMobAdapterExtras)
  {
    if (paramAdMobAdapterExtras == null)
      return;
    this.a = paramAdMobAdapterExtras.a;
    this.b = paramAdMobAdapterExtras.b;
    this.c.putAll(paramAdMobAdapterExtras.c);
  }

  public AdMobAdapterExtras addExtra(String paramString, Object paramObject)
  {
    this.c.put(paramString, paramObject);
    return this;
  }

  public AdMobAdapterExtras clearExtras()
  {
    this.c = new HashMap();
    return this;
  }

  public Map<String, Object> getExtras()
  {
    return this.c;
  }

  public boolean getPlusOneOptOut()
  {
    return this.a;
  }

  public boolean getUseExactAdSize()
  {
    return this.b;
  }

  public AdMobAdapterExtras setExtras(Map<String, Object> paramMap)
  {
    if (paramMap == null)
      throw new IllegalArgumentException("Argument 'extras' may not be null");
    this.c = paramMap;
    return this;
  }

  public AdMobAdapterExtras setPlusOneOptOut(boolean paramBoolean)
  {
    this.a = paramBoolean;
    return this;
  }

  public AdMobAdapterExtras setUseExactAdSize(boolean paramBoolean)
  {
    this.b = paramBoolean;
    return this;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.mediation.admob.AdMobAdapterExtras
 * JD-Core Version:    0.5.4
 */