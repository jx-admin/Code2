package com.google.ads;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import com.google.ads.doubleclick.DfpExtras;
import com.google.ads.mediation.NetworkExtras;
import com.google.ads.mediation.admob.AdMobAdapterExtras;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AdRequest
{
  public static final String LOGTAG = "Ads";
  public static final String TEST_EMULATOR;
  public static final String VERSION = "6.2.1";
  private static final SimpleDateFormat a = new SimpleDateFormat("yyyyMMdd");
  private static Method b = null;
  private static Method c = null;
  private Gender d = null;
  private Date e = null;
  private Set<String> f = null;
  private Map<String, Object> g = null;
  private final Map<Class<?>, NetworkExtras> h = new HashMap();
  private Location i = null;
  private boolean j = false;
  private boolean k = false;
  private Set<String> l = null;

  static
  {
    while (true)
    {
      int i2;
      try
      {
        Method[] arrayOfMethod = Class.forName("com.google.analytics.tracking.android.AdMobInfo").getMethods();
        int i1 = arrayOfMethod.length;
        i2 = 0;
        if (i2 >= i1)
          break label122;
        Method localMethod = arrayOfMethod[i2];
        if ((localMethod.getName().equals("getInstance")) && (localMethod.getParameterTypes().length == 0))
        {
          b = localMethod;
          break label159:
        }
        if ((!localMethod.getName().equals("getJoinIds")) || (localMethod.getParameterTypes().length != 0))
          break label159;
        label113: label122: c = localMethod;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        b.a("No Google Analytics: Library Not Found.");
        while (true)
        {
          TEST_EMULATOR = AdUtil.b("emulator");
          return;
          if ((b != null) && (c != null))
            continue;
          b = null;
          c = null;
          b.e("No Google Analytics: Library Incompatible.");
        }
      }
      catch (Throwable localThrowable)
      {
        b.a("No Google Analytics: Error Loading Library");
        break label113:
      }
      label159: ++i2;
    }
  }

  private AdMobAdapterExtras a()
  {
    monitorenter;
    try
    {
      if (getNetworkExtras(AdMobAdapterExtras.class) == null)
        setNetworkExtras(new AdMobAdapterExtras());
      AdMobAdapterExtras localAdMobAdapterExtras = (AdMobAdapterExtras)getNetworkExtras(AdMobAdapterExtras.class);
      monitorexit;
      return localAdMobAdapterExtras;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  @Deprecated
  public AdRequest addExtra(String paramString, Object paramObject)
  {
    AdMobAdapterExtras localAdMobAdapterExtras = a();
    if (localAdMobAdapterExtras.getExtras() == null)
      localAdMobAdapterExtras.setExtras(new HashMap());
    localAdMobAdapterExtras.getExtras().put(paramString, paramObject);
    return this;
  }

  public AdRequest addKeyword(String paramString)
  {
    if (this.f == null)
      this.f = new HashSet();
    this.f.add(paramString);
    return this;
  }

  public AdRequest addKeywords(Set<String> paramSet)
  {
    if (this.f == null)
      this.f = new HashSet();
    this.f.addAll(paramSet);
    return this;
  }

  public AdRequest addMediationExtra(String paramString, Object paramObject)
  {
    if (this.g == null)
      this.g = new HashMap();
    this.g.put(paramString, paramObject);
    return this;
  }

  public AdRequest addTestDevice(String paramString)
  {
    if (this.l == null)
      this.l = new HashSet();
    this.l.add(paramString);
    return this;
  }

  public AdRequest clearBirthday()
  {
    this.e = null;
    return this;
  }

  public Date getBirthday()
  {
    return this.e;
  }

  public Gender getGender()
  {
    return this.d;
  }

  public Set<String> getKeywords()
  {
    if (this.f == null);
    for (Set localSet = null; ; localSet = Collections.unmodifiableSet(this.f))
      return localSet;
  }

  public Location getLocation()
  {
    return this.i;
  }

  public <T> T getNetworkExtras(Class<T> paramClass)
  {
    return this.h.get(paramClass);
  }

  @Deprecated
  public boolean getPlusOneOptOut()
  {
    return a().getPlusOneOptOut();
  }

  public Map<String, Object> getRequestMap(Context paramContext)
  {
    int i1 = 1;
    HashMap localHashMap = new HashMap();
    if (this.f != null)
      localHashMap.put("kw", this.f);
    if (this.d != null)
      localHashMap.put("cust_gender", Integer.valueOf(this.d.ordinal()));
    if (this.e != null)
      localHashMap.put("cust_age", a.format(this.e));
    if (this.i != null)
      localHashMap.put("uule", AdUtil.a(this.i));
    if (this.j)
      localHashMap.put("testing", Integer.valueOf(i1));
    label144: AdMobAdapterExtras localAdMobAdapterExtras;
    label181: DfpExtras localDfpExtras;
    if (isTestDevice(paramContext))
    {
      localHashMap.put("adtest", "on");
      localAdMobAdapterExtras = (AdMobAdapterExtras)getNetworkExtras(AdMobAdapterExtras.class);
      if ((localAdMobAdapterExtras == null) || (!localAdMobAdapterExtras.getPlusOneOptOut()))
        break label441;
      localHashMap.put("pto", Integer.valueOf(i1));
      localDfpExtras = (DfpExtras)getNetworkExtras(DfpExtras.class);
      if ((localDfpExtras == null) || (localDfpExtras.getExtras() == null) || (localDfpExtras.getExtras().isEmpty()))
        break label470;
      localHashMap.put("extras", localDfpExtras.getExtras());
    }
    while (true)
    {
      if (localDfpExtras != null)
      {
        String str2 = localDfpExtras.getPublisherProvidedId();
        if (!TextUtils.isEmpty(str2))
          localHashMap.put("ppid", str2);
      }
      if (this.g != null)
        localHashMap.put("mediation_extras", this.g);
      try
      {
        if (b != null)
        {
          Object localObject = b.invoke(null, new Object[0]);
          Map localMap = (Map)c.invoke(localObject, new Object[0]);
          if ((localMap != null) && (localMap.size() > 0))
            localHashMap.put("analytics_join_id", localMap);
        }
        label349: return localHashMap;
        if (!this.k);
        if (AdUtil.c());
        for (String str1 = "AdRequest.TEST_EMULATOR"; ; str1 = "\"" + AdUtil.a(paramContext) + "\"")
        {
          b.c("To get test ads on this device, call adRequest.addTestDevice(" + str1 + ");");
          this.k = i1;
          break label144:
        }
        label441: if (ah.a(paramContext));
        while (true)
        {
          localHashMap.put("cipa", Integer.valueOf(i1));
          break label181:
          i1 = 0;
        }
        label470: if ((localAdMobAdapterExtras != null) && (localAdMobAdapterExtras.getExtras() != null) && (!localAdMobAdapterExtras.getExtras().isEmpty()));
        localHashMap.put("extras", localAdMobAdapterExtras.getExtras());
      }
      catch (Throwable localThrowable)
      {
        b.c("Internal Analytics Error:", localThrowable);
        break label349:
      }
    }
  }

  public boolean isTestDevice(Context paramContext)
  {
    int i1 = 0;
    String str;
    if (this.l != null)
    {
      str = AdUtil.a(paramContext);
      if (str != null)
        break label20;
    }
    while (true)
    {
      return i1;
      label20: if (!this.l.contains(str))
        continue;
      i1 = 1;
    }
  }

  public AdRequest removeNetworkExtras(Class<?> paramClass)
  {
    this.h.remove(paramClass);
    return this;
  }

  @Deprecated
  public AdRequest setBirthday(String paramString)
  {
    if ((paramString == "") || (paramString == null))
      this.e = null;
    while (true)
    {
      return this;
      try
      {
        this.e = a.parse(paramString);
      }
      catch (ParseException localParseException)
      {
        b.b("Birthday format invalid.  Expected 'YYYYMMDD' where 'YYYY' is a 4 digit year, 'MM' is a two digit month, and 'DD' is a two digit day.  Birthday value ignored");
        this.e = null;
      }
    }
  }

  public AdRequest setBirthday(Calendar paramCalendar)
  {
    if (paramCalendar == null)
      this.e = null;
    while (true)
    {
      return this;
      setBirthday(paramCalendar.getTime());
    }
  }

  public AdRequest setBirthday(Date paramDate)
  {
    if (paramDate == null);
    for (this.e = null; ; this.e = new Date(paramDate.getTime()))
      return this;
  }

  @Deprecated
  public AdRequest setExtras(Map<String, Object> paramMap)
  {
    a().setExtras(paramMap);
    return this;
  }

  public AdRequest setGender(Gender paramGender)
  {
    this.d = paramGender;
    return this;
  }

  public AdRequest setKeywords(Set<String> paramSet)
  {
    this.f = paramSet;
    return this;
  }

  public AdRequest setLocation(Location paramLocation)
  {
    this.i = paramLocation;
    return this;
  }

  public AdRequest setMediationExtras(Map<String, Object> paramMap)
  {
    this.g = paramMap;
    return this;
  }

  public AdRequest setNetworkExtras(NetworkExtras paramNetworkExtras)
  {
    if (paramNetworkExtras != null)
      this.h.put(paramNetworkExtras.getClass(), paramNetworkExtras);
    return this;
  }

  @Deprecated
  public AdRequest setPlusOneOptOut(boolean paramBoolean)
  {
    a().setPlusOneOptOut(paramBoolean);
    return this;
  }

  public AdRequest setTestDevices(Set<String> paramSet)
  {
    this.l = paramSet;
    return this;
  }

  @Deprecated
  public AdRequest setTesting(boolean paramBoolean)
  {
    this.j = paramBoolean;
    return this;
  }

  public static enum ErrorCode
  {
    private final String a;

    static
    {
      NETWORK_ERROR = new ErrorCode("NETWORK_ERROR", 2, "A network error occurred.");
      INTERNAL_ERROR = new ErrorCode("INTERNAL_ERROR", 3, "There was an internal error.");
      ErrorCode[] arrayOfErrorCode = new ErrorCode[4];
      arrayOfErrorCode[0] = INVALID_REQUEST;
      arrayOfErrorCode[1] = NO_FILL;
      arrayOfErrorCode[2] = NETWORK_ERROR;
      arrayOfErrorCode[3] = INTERNAL_ERROR;
      b = arrayOfErrorCode;
    }

    private ErrorCode(String paramString)
    {
      this.a = paramString;
    }

    public String toString()
    {
      return this.a;
    }
  }

  public static enum Gender
  {
    static
    {
      MALE = new Gender("MALE", 1);
      FEMALE = new Gender("FEMALE", 2);
      Gender[] arrayOfGender = new Gender[3];
      arrayOfGender[0] = UNKNOWN;
      arrayOfGender[1] = MALE;
      arrayOfGender[2] = FEMALE;
      a = arrayOfGender;
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.AdRequest
 * JD-Core Version:    0.5.4
 */