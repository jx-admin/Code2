package com.google.ads;

class e$1
  implements Runnable
{
  public void run()
  {
    e.a(this.c, this.a, this.b);
    synchronized (e.a(this.c))
    {
      e.a(this.c, null);
      return;
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.e.1
 * JD-Core Version:    0.5.4
 */