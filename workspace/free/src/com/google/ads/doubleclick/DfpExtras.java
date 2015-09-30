package com.google.ads.doubleclick;

import com.google.ads.mediation.admob.AdMobAdapterExtras;
import java.util.Map;

public class DfpExtras extends AdMobAdapterExtras
{
  private String a;

  public DfpExtras()
  {
  }

  public DfpExtras(DfpExtras paramDfpExtras)
  {
    super(paramDfpExtras);
    if (paramDfpExtras == null)
      return;
    this.a = paramDfpExtras.a;
  }

  public DfpExtras addExtra(String paramString, Object paramObject)
  {
    super.addExtra(paramString, paramObject);
    return this;
  }

  public DfpExtras clearExtras()
  {
    super.clearExtras();
    return this;
  }

  public String getPublisherProvidedId()
  {
    return this.a;
  }

  public DfpExtras setExtras(Map<String, Object> paramMap)
  {
    super.setExtras(paramMap);
    return this;
  }

  public DfpExtras setPlusOneOptOut(boolean paramBoolean)
  {
    super.setPlusOneOptOut(paramBoolean);
    return this;
  }

  public DfpExtras setPublisherProvidedId(String paramString)
  {
    this.a = paramString;
    return this;
  }

  public DfpExtras setUseExactAdSize(boolean paramBoolean)
  {
    super.setUseExactAdSize(paramBoolean);
    return this;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.doubleclick.DfpExtras
 * JD-Core Version:    0.5.4
 */