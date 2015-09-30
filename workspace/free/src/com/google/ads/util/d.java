package com.google.ads.util;

import android.os.Build;

class d
{
  static final d d = new d();
  static final d e = new d("unknown", "generic", "generic");
  public final String a;
  public final String b;
  public final String c;

  d()
  {
    this.a = Build.BOARD;
    this.b = Build.DEVICE;
    this.c = Build.BRAND;
  }

  d(String paramString1, String paramString2, String paramString3)
  {
    this.a = paramString1;
    this.b = paramString2;
    this.c = paramString3;
  }

  private static boolean a(String paramString1, String paramString2)
  {
    if (paramString1 != null);
    for (boolean bool = paramString1.equals(paramString2); ; bool = false)
      while (true)
      {
        return bool;
        if (paramString1 != paramString2)
          break;
        bool = true;
      }
  }

  public boolean equals(Object paramObject)
  {
    int i = 0;
    if (!paramObject instanceof d);
    while (true)
    {
      return i;
      d locald = (d)paramObject;
      if ((!a(this.a, locald.a)) || (!a(this.b, locald.b)) || (!a(this.c, locald.c)))
        continue;
      i = 1;
    }
  }

  public int hashCode()
  {
    int i = 0;
    if (this.a != null)
      i = 0 + this.a.hashCode();
    if (this.b != null)
      i += this.b.hashCode();
    if (this.c != null)
      i += this.c.hashCode();
    return i;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.util.d
 * JD-Core Version:    0.5.4
 */