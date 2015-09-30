package com.google.ads.internal;

import android.os.SystemClock;
import com.google.ads.g.a;
import com.google.ads.util.b;
import java.util.Iterator;
import java.util.LinkedList;

public class g
{
  private static long f = 0L;
  private static long g = 0L;
  private static long h = 0L;
  private static long i = 0L;
  private static long j = -1L;
  private final LinkedList<Long> a = new LinkedList();
  private long b;
  private long c;
  private long d;
  private final LinkedList<Long> e = new LinkedList();
  private boolean k = false;
  private boolean l = false;
  private String m;
  private long n;
  private final LinkedList<Long> o = new LinkedList();
  private final LinkedList<g.a> p = new LinkedList();

  public g()
  {
    a();
  }

  public static long E()
  {
    long l1;
    if (j == -1L)
    {
      j = SystemClock.elapsedRealtime();
      l1 = 0L;
    }
    while (true)
    {
      return l1;
      l1 = SystemClock.elapsedRealtime() - j;
    }
  }

  protected boolean A()
  {
    return this.l;
  }

  protected void B()
  {
    b.d("Interstitial no fill.");
    this.l = true;
  }

  public void C()
  {
    b.d("Landing page dismissed.");
    this.e.add(Long.valueOf(SystemClock.elapsedRealtime()));
  }

  protected String D()
  {
    return this.m;
  }

  protected void a()
  {
    monitorenter;
    try
    {
      this.a.clear();
      this.b = 0L;
      this.c = 0L;
      this.d = 0L;
      this.e.clear();
      this.n = -1L;
      this.o.clear();
      this.p.clear();
      this.k = false;
      this.l = false;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void a(g.a parama)
  {
    monitorenter;
    try
    {
      this.o.add(Long.valueOf(SystemClock.elapsedRealtime() - this.n));
      this.p.add(parama);
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void a(String paramString)
  {
    b.d("Prior impression ticket = " + paramString);
    this.m = paramString;
  }

  public void b()
  {
    monitorenter;
    try
    {
      this.o.clear();
      this.p.clear();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void c()
  {
    monitorenter;
    try
    {
      this.n = SystemClock.elapsedRealtime();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public String d()
  {
    monitorenter;
    StringBuilder localStringBuilder;
    long l1;
    try
    {
      localStringBuilder = new StringBuilder();
      Iterator localIterator = this.o.iterator();
      if (!localIterator.hasNext())
        break label70;
      l1 = ((Long)localIterator.next()).longValue();
      if (localStringBuilder.length() > 0)
        localStringBuilder.append(",");
    }
    finally
    {
      monitorexit;
    }
    label70: String str = localStringBuilder.toString();
    monitorexit;
    return str;
  }

  public String e()
  {
    monitorenter;
    StringBuilder localStringBuilder;
    g.a locala;
    try
    {
      localStringBuilder = new StringBuilder();
      Iterator localIterator = this.p.iterator();
      if (!localIterator.hasNext())
        break label70;
      locala = (g.a)localIterator.next();
      if (localStringBuilder.length() > 0)
        localStringBuilder.append(",");
    }
    finally
    {
      monitorexit;
    }
    label70: String str = localStringBuilder.toString();
    monitorexit;
    return str;
  }

  protected void f()
  {
    b.d("Ad clicked.");
    this.a.add(Long.valueOf(SystemClock.elapsedRealtime()));
  }

  protected void g()
  {
    b.d("Ad request loaded.");
    this.b = SystemClock.elapsedRealtime();
  }

  protected void h()
  {
    monitorenter;
    try
    {
      b.d("Ad request before rendering.");
      this.c = SystemClock.elapsedRealtime();
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void i()
  {
    b.d("Ad request started.");
    this.d = SystemClock.elapsedRealtime();
    f = 1L + f;
  }

  protected long j()
  {
    long l1;
    if (this.a.size() != this.e.size())
      l1 = -1L;
    while (true)
    {
      return l1;
      l1 = this.a.size();
    }
  }

  protected String k()
  {
    if ((this.a.isEmpty()) || (this.a.size() != this.e.size()));
    StringBuilder localStringBuilder;
    for (String str = null; ; str = localStringBuilder.toString())
    {
      return str;
      localStringBuilder = new StringBuilder();
      for (int i1 = 0; i1 < this.a.size(); ++i1)
      {
        if (i1 != 0)
          localStringBuilder.append(",");
        localStringBuilder.append(Long.toString(((Long)this.e.get(i1)).longValue() - ((Long)this.a.get(i1)).longValue()));
      }
    }
  }

  protected String l()
  {
    if (this.a.isEmpty());
    StringBuilder localStringBuilder;
    for (String str = null; ; str = localStringBuilder.toString())
    {
      return str;
      localStringBuilder = new StringBuilder();
      for (int i1 = 0; i1 < this.a.size(); ++i1)
      {
        if (i1 != 0)
          localStringBuilder.append(",");
        localStringBuilder.append(Long.toString(((Long)this.a.get(i1)).longValue() - this.b));
      }
    }
  }

  protected long m()
  {
    return this.b - this.d;
  }

  protected long n()
  {
    monitorenter;
    try
    {
      long l1 = this.c;
      long l2 = this.d;
      long l3 = l1 - l2;
      monitorexit;
      return l3;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected long o()
  {
    return f;
  }

  protected long p()
  {
    monitorenter;
    try
    {
      long l1 = g;
      monitorexit;
      return l1;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void q()
  {
    monitorenter;
    try
    {
      b.d("Ad request network error");
      g = 1L + g;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void r()
  {
    monitorenter;
    try
    {
      g = 0L;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected long s()
  {
    monitorenter;
    try
    {
      long l1 = h;
      monitorexit;
      return l1;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void t()
  {
    monitorenter;
    try
    {
      h = 1L + h;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void u()
  {
    monitorenter;
    try
    {
      h = 0L;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected long v()
  {
    monitorenter;
    try
    {
      long l1 = i;
      monitorexit;
      return l1;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void w()
  {
    monitorenter;
    try
    {
      i = 1L + i;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected void x()
  {
    monitorenter;
    try
    {
      i = 0L;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  protected boolean y()
  {
    return this.k;
  }

  protected void z()
  {
    b.d("Interstitial network error.");
    this.k = true;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.internal.g
 * JD-Core Version:    0.5.4
 */