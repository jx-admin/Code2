package com.google.ads.mediation.customevent;

import com.google.ads.mediation.NetworkExtras;
import java.util.HashMap;

public class CustomEventExtras
  implements NetworkExtras
{
  private final HashMap<String, Object> a = new HashMap();

  public CustomEventExtras addExtra(String paramString, Object paramObject)
  {
    this.a.put(paramString, paramObject);
    return this;
  }

  public CustomEventExtras clearExtras()
  {
    this.a.clear();
    return this;
  }

  public Object getExtra(String paramString)
  {
    return this.a.get(paramString);
  }

  public Object removeExtra(String paramString)
  {
    return this.a.remove(paramString);
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.mediation.customevent.CustomEventExtras
 * JD-Core Version:    0.5.4
 */