package com.google.ads;

import com.google.ads.internal.h;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class c
{
  private static final Map<String, AdSize> a = Collections.unmodifiableMap(new c.1());
  private final String b;
  private final String c;
  private final List<a> d;
  private final Integer e;
  private final Integer f;
  private final List<String> g;
  private final List<String> h;
  private final List<String> i;

  private c(String paramString1, String paramString2, List<a> paramList, Integer paramInteger1, Integer paramInteger2, List<String> paramList1, List<String> paramList2, List<String> paramList3)
  {
    com.google.ads.util.a.a(paramString1);
    this.b = paramString1;
    this.c = paramString2;
    this.d = paramList;
    this.e = paramInteger1;
    this.f = paramInteger2;
    this.g = paramList1;
    this.h = paramList2;
    this.i = paramList3;
  }

  private static a a(JSONObject paramJSONObject)
    throws JSONException
  {
    String str1 = paramJSONObject.getString("id");
    String str2 = paramJSONObject.optString("allocation_id", null);
    JSONArray localJSONArray = paramJSONObject.getJSONArray("adapters");
    ArrayList localArrayList = new ArrayList(localJSONArray.length());
    for (int j = 0; j < localJSONArray.length(); ++j)
      localArrayList.add(localJSONArray.getString(j));
    List localList = a(paramJSONObject, "imp_urls");
    JSONObject localJSONObject = paramJSONObject.optJSONObject("data");
    HashMap localHashMap1 = new HashMap(0);
    if (localJSONObject != null)
    {
      localHashMap2 = new HashMap(localJSONObject.length());
      Iterator localIterator = localJSONObject.keys();
      while (true)
      {
        if (!localIterator.hasNext())
          break label161;
        String str3 = (String)localIterator.next();
        localHashMap2.put(str3, localJSONObject.getString(str3));
      }
    }
    HashMap localHashMap2 = localHashMap1;
    label161: return new a(str2, str1, localArrayList, localList, localHashMap2);
  }

  public static c a(String paramString)
    throws JSONException
  {
    Integer localInteger1 = null;
    JSONObject localJSONObject1 = new JSONObject(paramString);
    String str1 = localJSONObject1.getString("qdata");
    if (localJSONObject1.has("ad_type"));
    ArrayList localArrayList;
    for (String str2 = localJSONObject1.getString("ad_type"); ; str2 = null)
    {
      JSONArray localJSONArray = localJSONObject1.getJSONArray("ad_networks");
      localArrayList = new ArrayList(localJSONArray.length());
      for (int j = 0; ; ++j)
      {
        if (j >= localJSONArray.length())
          break label100;
        localArrayList.add(a(localJSONArray.getJSONObject(j)));
      }
    }
    label100: JSONObject localJSONObject2 = localJSONObject1.optJSONObject("settings");
    Integer localInteger3;
    label135: List localList2;
    List localList3;
    List localList1;
    Integer localInteger2;
    if (localJSONObject2 != null)
      if (localJSONObject2.has("refresh"))
      {
        localInteger3 = Integer.valueOf(localJSONObject2.getInt("refresh"));
        if (localJSONObject2.has("ad_network_timeout_millis"))
          localInteger1 = Integer.valueOf(localJSONObject2.getInt("ad_network_timeout_millis"));
        localList2 = a(localJSONObject2, "imp_urls");
        localList3 = a(localJSONObject2, "click_urls");
        localList1 = a(localJSONObject2, "nofill_urls");
        localInteger2 = localInteger1;
      }
    while (true)
    {
      return new c(str1, str2, localArrayList, localInteger3, localInteger2, localList2, localList3, localList1);
      localInteger3 = null;
      break label135:
      localList1 = null;
      localList2 = null;
      localList3 = null;
      localInteger2 = null;
      localInteger3 = null;
    }
  }

  private static List<String> a(JSONObject paramJSONObject, String paramString)
    throws JSONException
  {
    JSONArray localJSONArray = paramJSONObject.optJSONArray(paramString);
    ArrayList localArrayList1;
    if (localJSONArray != null)
    {
      localArrayList1 = new ArrayList(localJSONArray.length());
      for (int j = 0; j < localJSONArray.length(); ++j)
        localArrayList1.add(localJSONArray.getString(j));
    }
    for (ArrayList localArrayList2 = localArrayList1; ; localArrayList2 = null)
      return localArrayList2;
  }

  public boolean a()
  {
    if (this.f != null);
    for (int j = 1; ; j = 0)
      return j;
  }

  public int b()
  {
    return this.f.intValue();
  }

  public String c()
  {
    return this.b;
  }

  public boolean d()
  {
    if (this.e != null);
    for (int j = 1; ; j = 0)
      return j;
  }

  public int e()
  {
    return this.e.intValue();
  }

  public List<a> f()
  {
    return this.d;
  }

  public List<String> g()
  {
    return this.g;
  }

  public List<String> h()
  {
    return this.h;
  }

  public List<String> i()
  {
    return this.i;
  }

  public h j()
  {
    if (this.c == null);
    for (h localh = null; ; localh = null)
      while (true)
      {
        return localh;
        if ("interstitial".equals(this.c))
          localh = h.a;
        AdSize localAdSize = (AdSize)a.get(this.c);
        if (localAdSize == null)
          break;
        localh = h.a(localAdSize);
      }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.c
 * JD-Core Version:    0.5.4
 */