package com.google.ads;

import android.text.TextUtils;
import android.webkit.WebView;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.d;
import com.google.ads.internal.h;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import com.google.ads.util.g;
import com.google.ads.util.i.b;
import com.google.ads.util.i.c;
import java.util.HashMap;

public class x
  implements n
{
  private void a(HashMap<String, String> paramHashMap, String paramString, i.c<Integer> paramc)
  {
    try
    {
      String str = (String)paramHashMap.get(paramString);
      if (!TextUtils.isEmpty(str))
        paramc.a(Integer.valueOf(str));
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      b.a("Could not parse \"" + paramString + "\" constant.");
    }
  }

  private void b(HashMap<String, String> paramHashMap, String paramString, i.c<Long> paramc)
  {
    try
    {
      String str = (String)paramHashMap.get(paramString);
      if (!TextUtils.isEmpty(str))
        paramc.a(new Long(str));
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      b.a("Could not parse \"" + paramString + "\" constant.");
    }
  }

  private void c(HashMap<String, String> paramHashMap, String paramString, i.c<String> paramc)
  {
    String str = (String)paramHashMap.get(paramString);
    if (TextUtils.isEmpty(str))
      return;
    paramc.a(str);
  }

  public void a(d paramd, HashMap<String, String> paramHashMap, WebView paramWebView)
  {
    m localm = paramd.h();
    l.a locala = (l.a)((l)localm.a.a()).a.a();
    a(paramHashMap, "min_hwa_banner", locala.a);
    a(paramHashMap, "min_hwa_overlay", locala.b);
    c(paramHashMap, "mraid_banner_path", locala.c);
    c(paramHashMap, "mraid_expanded_banner_path", locala.d);
    c(paramHashMap, "mraid_interstitial_path", locala.e);
    b(paramHashMap, "ac_max_size", locala.f);
    b(paramHashMap, "ac_padding", locala.g);
    b(paramHashMap, "ac_total_quota", locala.h);
    b(paramHashMap, "db_total_quota", locala.i);
    b(paramHashMap, "db_quota_per_origin", locala.j);
    b(paramHashMap, "db_quota_step_size", locala.k);
    AdWebView localAdWebView = paramd.k();
    if (AdUtil.a >= 11)
    {
      g.a(localAdWebView.getSettings(), localm);
      g.a(paramWebView.getSettings(), localm);
    }
    boolean bool;
    int i;
    if (!((h)localm.k.a()).a())
    {
      bool = localAdWebView.f();
      if (AdUtil.a >= ((Integer)locala.a.a()).intValue())
        break label273;
      i = 1;
      label240: if ((i != 0) || (!bool))
        break label279;
      b.a("Re-enabling hardware acceleration for a banner after reading constants.");
      localAdWebView.c();
    }
    while (true)
    {
      locala.l.a(Boolean.valueOf(true));
      return;
      label273: i = 0;
      break label240:
      label279: if ((i == 0) || (bool))
        continue;
      b.a("Disabling hardware acceleration for a banner after reading constants.");
      localAdWebView.b();
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.x
 * JD-Core Version:    0.5.4
 */