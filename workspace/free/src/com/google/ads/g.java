package com.google.ads;

public final class g
{
  public static <T> T a(String paramString, Class<T> paramClass)
    throws ClassNotFoundException, ClassCastException, IllegalAccessException, InstantiationException, LinkageError, ExceptionInInitializerError
  {
    return paramClass.cast(Class.forName(paramString).newInstance());
  }

  public static String a(String paramString1, String paramString2, Boolean paramBoolean, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10)
  {
    String str1 = paramString1.replaceAll("@gw_adlocid@", paramString2).replaceAll("@gw_qdata@", paramString6).replaceAll("@gw_sdkver@", "afma-sdk-a-v6.2.1").replaceAll("@gw_sessid@", paramString7).replaceAll("@gw_seqnum@", paramString8).replaceAll("@gw_devid@", paramString3);
    if (paramString5 != null)
      str1 = str1.replaceAll("@gw_adnetid@", paramString5);
    if (paramString4 != null)
      str1 = str1.replaceAll("@gw_allocid@", paramString4);
    if (paramString9 != null)
      str1 = str1.replaceAll("@gw_adt@", paramString9);
    if (paramString10 != null)
      str1 = str1.replaceAll("@gw_aec@", paramString10);
    String str3;
    if (paramBoolean != null)
      if (paramBoolean.booleanValue())
        str3 = "1";
    for (String str2 = str1.replaceAll("@gw_adnetrefresh@", str3); ; str2 = str1)
    {
      label122: return str2;
      str3 = "0";
      break label122:
    }
  }

  public static enum a
  {
    static
    {
      a[] arrayOfa = new a[6];
      arrayOfa[0] = a;
      arrayOfa[1] = b;
      arrayOfa[2] = c;
      arrayOfa[3] = d;
      arrayOfa[4] = e;
      arrayOfa[5] = f;
      g = arrayOfa;
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.g
 * JD-Core Version:    0.5.4
 */