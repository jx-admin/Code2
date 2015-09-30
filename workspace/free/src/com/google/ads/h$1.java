package com.google.ads;

import com.google.ads.mediation.MediationAdapter;
import com.google.ads.util.a;
import com.google.ads.util.b;

class h$1
  implements Runnable
{
  public void run()
  {
    if (this.a.l())
      a.b(h.a(this.a));
    try
    {
      h.a(this.a).destroy();
      b.a("Called destroy() for adapter with class: " + h.a(this.a).getClass().getName());
      return;
    }
    catch (Throwable localThrowable)
    {
      b.b("Error while destroying adapter (" + this.a.h() + "):", localThrowable);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.h.1
 * JD-Core Version:    0.5.4
 */