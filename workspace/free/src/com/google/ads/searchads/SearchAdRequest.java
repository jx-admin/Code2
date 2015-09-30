package com.google.ads.searchads;

import android.content.Context;
import android.graphics.Color;
import com.google.ads.AdRequest;
import com.google.ads.mediation.admob.AdMobAdapterExtras;
import java.util.Locale;
import java.util.Map;

public class SearchAdRequest extends AdRequest
{
  private String a;
  private int b;
  private int c;
  private int d;
  private int e;
  private int f;
  private int g;
  private String h;
  private int i;
  private int j;
  private BorderType k;
  private int l;
  private String m;

  private String a(int paramInt)
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(0xFFFFFF & paramInt);
    return String.format(localLocale, "#%06x", arrayOfObject);
  }

  public Map<String, Object> getRequestMap(Context paramContext)
  {
    AdMobAdapterExtras localAdMobAdapterExtras = (AdMobAdapterExtras)getNetworkExtras(AdMobAdapterExtras.class);
    if (localAdMobAdapterExtras == null)
    {
      localAdMobAdapterExtras = new AdMobAdapterExtras();
      setNetworkExtras(localAdMobAdapterExtras);
    }
    if (this.a != null)
      localAdMobAdapterExtras.getExtras().put("q", this.a);
    if (Color.alpha(this.b) != 0)
      localAdMobAdapterExtras.getExtras().put("bgcolor", a(this.b));
    if ((Color.alpha(this.c) == 255) && (Color.alpha(this.d) == 255))
    {
      localAdMobAdapterExtras.getExtras().put("gradientfrom", a(this.c));
      localAdMobAdapterExtras.getExtras().put("gradientto", a(this.d));
    }
    if (Color.alpha(this.e) != 0)
      localAdMobAdapterExtras.getExtras().put("hcolor", a(this.e));
    if (Color.alpha(this.f) != 0)
      localAdMobAdapterExtras.getExtras().put("dcolor", a(this.f));
    if (Color.alpha(this.g) != 0)
      localAdMobAdapterExtras.getExtras().put("acolor", a(this.g));
    if (this.h != null)
      localAdMobAdapterExtras.getExtras().put("font", this.h);
    localAdMobAdapterExtras.getExtras().put("headersize", Integer.toString(this.i));
    if (Color.alpha(this.j) != 0)
      localAdMobAdapterExtras.getExtras().put("bcolor", a(this.j));
    if (this.k != null)
      localAdMobAdapterExtras.getExtras().put("btype", this.k.toString());
    localAdMobAdapterExtras.getExtras().put("bthick", Integer.toString(this.l));
    if (this.m != null)
      localAdMobAdapterExtras.getExtras().put("channel", this.m);
    return super.getRequestMap(paramContext);
  }

  public void setAnchorTextColor(int paramInt)
  {
    this.g = paramInt;
  }

  public void setBackgroundColor(int paramInt)
  {
    if (Color.alpha(paramInt) != 255)
      return;
    this.b = paramInt;
    this.c = 0;
    this.d = 0;
  }

  public void setBackgroundGradient(int paramInt1, int paramInt2)
  {
    if ((Color.alpha(paramInt1) != 255) || (Color.alpha(paramInt2) != 255))
      return;
    this.b = Color.argb(0, 0, 0, 0);
    this.c = paramInt1;
    this.d = paramInt2;
  }

  public void setBorderColor(int paramInt)
  {
    this.j = paramInt;
  }

  public void setBorderThickness(int paramInt)
  {
    this.l = paramInt;
  }

  public void setBorderType(BorderType paramBorderType)
  {
    this.k = paramBorderType;
  }

  public void setCustomChannels(String paramString)
  {
    this.m = paramString;
  }

  public void setDescriptionTextColor(int paramInt)
  {
    this.f = paramInt;
  }

  public void setFontFace(String paramString)
  {
    this.h = paramString;
  }

  public void setHeaderTextColor(int paramInt)
  {
    this.e = paramInt;
  }

  public void setHeaderTextSize(int paramInt)
  {
    this.i = paramInt;
  }

  public void setQuery(String paramString)
  {
    this.a = paramString;
  }

  public static enum BorderType
  {
    private String a;

    static
    {
      DASHED = new BorderType("DASHED", 1, "dashed");
      DOTTED = new BorderType("DOTTED", 2, "dotted");
      SOLID = new BorderType("SOLID", 3, "solid");
      BorderType[] arrayOfBorderType = new BorderType[4];
      arrayOfBorderType[0] = NONE;
      arrayOfBorderType[1] = DASHED;
      arrayOfBorderType[2] = DOTTED;
      arrayOfBorderType[3] = SOLID;
      b = arrayOfBorderType;
    }

    private BorderType(String paramString)
    {
      this.a = paramString;
    }

    public String toString()
    {
      return this.a;
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.searchads.SearchAdRequest
 * JD-Core Version:    0.5.4
 */