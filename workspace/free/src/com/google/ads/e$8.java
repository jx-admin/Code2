package com.google.ads;

import android.view.View;
import com.google.ads.internal.d;
import com.google.ads.util.b;

class e$8
  implements Runnable
{
  public void run()
  {
    if (e.a(this.d, this.a))
      b.a("Trying to switch GWAdNetworkAmbassadors, but GWController().destroy() has been called. Destroying the new ambassador and terminating mediation.");
    while (true)
    {
      return;
      e.b(this.d).a(this.b, this.a, this.c, false);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.e.8
 * JD-Core Version:    0.5.4
 */