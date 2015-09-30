package com.google.ads.internal;

public final class b extends Exception
{
  public final boolean a;

  public b(String paramString, boolean paramBoolean)
  {
    super(paramString);
    this.a = paramBoolean;
  }

  public b(String paramString, boolean paramBoolean, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
    this.a = paramBoolean;
  }

  public void a(String paramString)
  {
    com.google.ads.util.b.b(c(paramString));
    com.google.ads.util.b.a(null, this);
  }

  public void b(String paramString)
  {
    String str = c(paramString);
    if (this.a);
    while (true)
    {
      throw new RuntimeException(str, this);
      this = null;
    }
  }

  public String c(String paramString)
  {
    if (this.a)
      paramString = paramString + ": " + getMessage();
    return paramString;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.internal.b
 * JD-Core Version:    0.5.4
 */