package com.google.ads;

import android.os.SystemClock;
import java.util.concurrent.TimeUnit;

public class d
{
  private c a = null;
  private long b = -1L;

  public void a(c paramc, int paramInt)
  {
    this.a = paramc;
    this.b = (TimeUnit.MILLISECONDS.convert(paramInt, TimeUnit.SECONDS) + SystemClock.elapsedRealtime());
  }

  public boolean a()
  {
    if ((this.a != null) && (SystemClock.elapsedRealtime() < this.b));
    for (int i = 1; ; i = 0)
      return i;
  }

  public c b()
  {
    return this.a;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.d
 * JD-Core Version:    0.5.4
 */