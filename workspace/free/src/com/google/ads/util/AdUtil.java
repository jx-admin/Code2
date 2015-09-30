package com.google.ads.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.google.ads.AdActivity;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.nio.CharBuffer;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class AdUtil
{
  public static final int a = a(Build.VERSION.SDK);
  private static Boolean b = null;
  private static String c = null;
  private static String d;
  private static String e = null;
  private static AudioManager f;
  private static boolean g = true;
  private static boolean h = false;
  private static String i = null;

  public static int a()
  {
    if (a >= 9);
    for (int j = 6; ; j = 0)
      return j;
  }

  public static int a(Context paramContext, DisplayMetrics paramDisplayMetrics)
  {
    if (a >= 4);
    for (int j = e.a(paramContext, paramDisplayMetrics); ; j = paramDisplayMetrics.heightPixels)
      return j;
  }

  public static int a(String paramString)
  {
    int j;
    try
    {
      int k = Integer.parseInt(paramString);
      j = k;
      return j;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      b.e("The Android SDK version couldn't be parsed to an int: " + Build.VERSION.SDK);
      b.e("Defaulting to Android SDK version 3.");
      j = 3;
    }
  }

  public static DisplayMetrics a(Activity paramActivity)
  {
    DisplayMetrics localDisplayMetrics;
    if (paramActivity.getWindowManager() == null)
      localDisplayMetrics = null;
    while (true)
    {
      return localDisplayMetrics;
      localDisplayMetrics = new DisplayMetrics();
      paramActivity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
    }
  }

  public static String a(Context paramContext)
  {
    String str2;
    String str3;
    if (c == null)
    {
      str2 = Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
      if ((str2 == null) || (c()))
      {
        str3 = b("emulator");
        label32: if (str3 != null)
          break label48;
      }
    }
    for (String str1 = null; ; str1 = c)
    {
      return str1;
      str3 = b(str2);
      break label32:
      label48: c = str3.toUpperCase(Locale.US);
    }
  }

  public static String a(Location paramLocation)
  {
    if (paramLocation == null);
    String str1;
    for (String str2 = null; ; str2 = "e1+" + str1)
    {
      return str2;
      str1 = c(b(paramLocation));
    }
  }

  public static String a(Readable paramReadable)
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    CharBuffer localCharBuffer = CharBuffer.allocate(2048);
    while (true)
    {
      int j = paramReadable.read(localCharBuffer);
      if (j == -1)
        break;
      localCharBuffer.flip();
      localStringBuilder.append(localCharBuffer, 0, j);
    }
    return localStringBuilder.toString();
  }

  public static String a(Map<String, Object> paramMap)
  {
    Object localObject = null;
    try
    {
      String str = b(paramMap).toString();
      localObject = str;
      return localObject;
    }
    catch (JSONException localJSONException)
    {
      b.d("JsonException in serialization: ", localJSONException);
    }
  }

  public static JSONArray a(Set<Object> paramSet)
    throws JSONException
  {
    JSONArray localJSONArray1 = new JSONArray();
    if ((paramSet == null) || (paramSet.isEmpty()));
    for (JSONArray localJSONArray2 = localJSONArray1; ; localJSONArray2 = localJSONArray1)
    {
      return localJSONArray2;
      Iterator localIterator = paramSet.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        if ((localObject instanceof String) || (localObject instanceof Integer) || (localObject instanceof Double) || (localObject instanceof Long) || (localObject instanceof Float))
          localJSONArray1.put(localObject);
        if (localObject instanceof Map)
          try
          {
            localJSONArray1.put(b((Map)localObject));
          }
          catch (ClassCastException localClassCastException2)
          {
            b.d("Unknown map type in json serialization: ", localClassCastException2);
          }
        if (localObject instanceof Set)
          try
          {
            localJSONArray1.put(a((Set)localObject));
          }
          catch (ClassCastException localClassCastException1)
          {
            b.d("Unknown map type in json serialization: ", localClassCastException1);
          }
        b.e("Unknown value in json serialization: " + localObject);
      }
    }
  }

  public static void a(WebView paramWebView)
  {
    String str = i(paramWebView.getContext().getApplicationContext());
    paramWebView.getSettings().setUserAgentString(str);
  }

  public static void a(HttpURLConnection paramHttpURLConnection, Context paramContext)
  {
    paramHttpURLConnection.setRequestProperty("User-Agent", i(paramContext));
  }

  public static void a(boolean paramBoolean)
  {
    g = paramBoolean;
  }

  public static boolean a(int paramInt1, int paramInt2, String paramString)
  {
    if ((paramInt1 & paramInt2) == 0)
      b.b("The android:configChanges value of the com.google.ads.AdActivity must include " + paramString + ".");
    for (int j = 0; ; j = 1)
      return j;
  }

  public static boolean a(Intent paramIntent, Context paramContext)
  {
    if (paramContext.getPackageManager().resolveActivity(paramIntent, 65536) != null);
    for (int j = 1; ; j = 0)
      return j;
  }

  public static boolean a(Uri paramUri)
  {
    int j = 0;
    if (paramUri == null);
    while (true)
    {
      return j;
      String str = paramUri.getScheme();
      if ((!"http".equalsIgnoreCase(str)) && (!"https".equalsIgnoreCase(str)))
        continue;
      j = 1;
    }
  }

  static boolean a(d paramd)
  {
    if (paramd == null)
      paramd = d.d;
    return paramd.equals(d.e);
  }

  public static int b()
  {
    if (a >= 9);
    for (int j = 7; ; j = 1)
      return j;
  }

  public static int b(Context paramContext, DisplayMetrics paramDisplayMetrics)
  {
    if (a >= 4);
    for (int j = e.b(paramContext, paramDisplayMetrics); ; j = paramDisplayMetrics.widthPixels)
      return j;
  }

  private static String b(Location paramLocation)
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = Long.valueOf(1000L * paramLocation.getTime());
    arrayOfObject[1] = Long.valueOf(()(10000000.0D * paramLocation.getLatitude()));
    arrayOfObject[2] = Long.valueOf(()(10000000.0D * paramLocation.getLongitude()));
    arrayOfObject[3] = Long.valueOf(()(1000.0F * paramLocation.getAccuracy()));
    return String.format(localLocale, "role: 6 producer: 24 historical_role: 1 historical_producer: 12 timestamp: %d latlng < latitude_e7: %d longitude_e7: %d> radius: %d", arrayOfObject);
  }

  public static String b(String paramString)
  {
    Object localObject = null;
    if ((paramString != null) && (paramString.length() > 0));
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramString.getBytes(), 0, paramString.length());
      Locale localLocale = Locale.US;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = new BigInteger(1, localMessageDigest.digest());
      String str = String.format(localLocale, "%032X", arrayOfObject);
      localObject = str;
      return localObject;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      localObject = paramString.substring(0, 32);
    }
  }

  public static HashMap<String, String> b(Uri paramUri)
  {
    Object localObject = null;
    if (paramUri == null);
    while (true)
    {
      return localObject;
      HashMap localHashMap = new HashMap();
      String str1 = paramUri.getEncodedQuery();
      if (str1 != null)
      {
        String[] arrayOfString = str1.split("&");
        int j = arrayOfString.length;
        int k = 0;
        if (k < j)
        {
          label42: String str2 = arrayOfString[k];
          int l = str2.indexOf("=");
          if (l < 0)
            localHashMap.put(Uri.decode(str2), null);
          while (true)
          {
            ++k;
            break label42:
            localHashMap.put(Uri.decode(str2.substring(0, l)), Uri.decode(str2.substring(l + 1, str2.length())));
          }
        }
      }
      localObject = localHashMap;
    }
  }

  public static JSONObject b(Map<String, Object> paramMap)
    throws JSONException
  {
    JSONObject localJSONObject1 = new JSONObject();
    if ((paramMap == null) || (paramMap.isEmpty()));
    for (JSONObject localJSONObject2 = localJSONObject1; ; localJSONObject2 = localJSONObject1)
    {
      return localJSONObject2;
      Iterator localIterator = paramMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        Object localObject = paramMap.get(str);
        if ((localObject instanceof String) || (localObject instanceof Integer) || (localObject instanceof Double) || (localObject instanceof Long) || (localObject instanceof Float))
          localJSONObject1.put(str, localObject);
        if (localObject instanceof Map)
          try
          {
            localJSONObject1.put(str, b((Map)localObject));
          }
          catch (ClassCastException localClassCastException2)
          {
            b.d("Unknown map type in json serialization: ", localClassCastException2);
          }
        if (localObject instanceof Set)
          try
          {
            localJSONObject1.put(str, a((Set)localObject));
          }
          catch (ClassCastException localClassCastException1)
          {
            b.d("Unknown map type in json serialization: ", localClassCastException1);
          }
        b.e("Unknown value in json serialization: " + localObject);
      }
    }
  }

  public static boolean b(Context paramContext)
  {
    int j = 0;
    PackageManager localPackageManager = paramContext.getPackageManager();
    String str = paramContext.getPackageName();
    if (localPackageManager.checkPermission("android.permission.INTERNET", str) == -1)
      b.b("INTERNET permissions must be enabled in AndroidManifest.xml.");
    while (true)
    {
      return j;
      if (localPackageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE", str) == -1)
        b.b("ACCESS_NETWORK_STATE permissions must be enabled in AndroidManifest.xml.");
      j = 1;
    }
  }

  private static String c(String paramString)
  {
    String str1;
    try
    {
      Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      byte[] arrayOfByte1 = new byte[16];
      arrayOfByte1[0] = 10;
      arrayOfByte1[1] = 55;
      arrayOfByte1[2] = -112;
      arrayOfByte1[3] = -47;
      arrayOfByte1[4] = -6;
      arrayOfByte1[5] = 7;
      arrayOfByte1[6] = 11;
      arrayOfByte1[7] = 75;
      arrayOfByte1[8] = -7;
      arrayOfByte1[9] = -121;
      arrayOfByte1[10] = 121;
      arrayOfByte1[11] = 69;
      arrayOfByte1[12] = 80;
      arrayOfByte1[13] = -61;
      arrayOfByte1[14] = 15;
      arrayOfByte1[15] = 5;
      localCipher.init(1, new SecretKeySpec(arrayOfByte1, "AES"));
      byte[] arrayOfByte2 = localCipher.getIV();
      byte[] arrayOfByte3 = localCipher.doFinal(paramString.getBytes());
      byte[] arrayOfByte4 = new byte[arrayOfByte2.length + arrayOfByte3.length];
      System.arraycopy(arrayOfByte2, 0, arrayOfByte4, 0, arrayOfByte2.length);
      System.arraycopy(arrayOfByte3, 0, arrayOfByte4, arrayOfByte2.length, arrayOfByte3.length);
      String str2 = c.b(arrayOfByte4, 11);
      str1 = str2;
      return str1;
    }
    catch (GeneralSecurityException localGeneralSecurityException)
    {
      str1 = null;
    }
  }

  public static boolean c()
  {
    return a(null);
  }

  public static boolean c(Context paramContext)
  {
    boolean bool;
    if (b != null)
    {
      bool = b.booleanValue();
      label13: return bool;
    }
    ResolveInfo localResolveInfo = paramContext.getPackageManager().resolveActivity(new Intent(paramContext, AdActivity.class), 65536);
    b = Boolean.valueOf(true);
    if ((localResolveInfo == null) || (localResolveInfo.activityInfo == null))
    {
      b.b("Could not find com.google.ads.AdActivity, please make sure it is registered in AndroidManifest.xml.");
      b = Boolean.valueOf(false);
    }
    while (true)
    {
      bool = b.booleanValue();
      break label13:
      if (!a(localResolveInfo.activityInfo.configChanges, 16, "keyboard"))
        b = Boolean.valueOf(false);
      if (!a(localResolveInfo.activityInfo.configChanges, 32, "keyboardHidden"))
        b = Boolean.valueOf(false);
      if (!a(localResolveInfo.activityInfo.configChanges, 128, "orientation"))
        b = Boolean.valueOf(false);
      if (!a(localResolveInfo.activityInfo.configChanges, 256, "screenLayout"))
        b = Boolean.valueOf(false);
      if (!a(localResolveInfo.activityInfo.configChanges, 512, "uiMode"))
        b = Boolean.valueOf(false);
      if (!a(localResolveInfo.activityInfo.configChanges, 1024, "screenSize"))
        b = Boolean.valueOf(false);
      if (a(localResolveInfo.activityInfo.configChanges, 2048, "smallestScreenSize"))
        continue;
      b = Boolean.valueOf(false);
    }
  }

  public static String d(Context paramContext)
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if (localNetworkInfo == null);
    for (String str = null; ; str = "wi")
      while (true)
      {
        return str;
        switch (localNetworkInfo.getType())
        {
        default:
          str = "unknown";
          break;
        case 0:
          str = "ed";
        case 1:
        }
      }
  }

  public static boolean d()
  {
    return g;
  }

  public static String e(Context paramContext)
  {
    if (d == null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      PackageManager localPackageManager = paramContext.getPackageManager();
      List localList1 = localPackageManager.queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("geo:0,0?q=donuts")), 65536);
      if ((localList1 == null) || (localList1.size() == 0))
        localStringBuilder.append("m");
      List localList2 = localPackageManager.queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pname:com.google")), 65536);
      if ((localList2 == null) || (localList2.size() == 0))
      {
        if (localStringBuilder.length() > 0)
          localStringBuilder.append(",");
        localStringBuilder.append("a");
      }
      List localList3 = localPackageManager.queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("tel://6509313940")), 65536);
      if ((localList3 == null) || (localList3.size() == 0))
      {
        if (localStringBuilder.length() > 0)
          localStringBuilder.append(",");
        localStringBuilder.append("t");
      }
      d = localStringBuilder.toString();
    }
    return d;
  }

  public static String f(Context paramContext)
  {
    String str = null;
    if (e != null)
      str = e;
    while (true)
    {
      return str;
      try
      {
        PackageManager localPackageManager = paramContext.getPackageManager();
        ResolveInfo localResolveInfo = localPackageManager.resolveActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.ads")), 65536);
        if (localResolveInfo != null);
        ActivityInfo localActivityInfo = localResolveInfo.activityInfo;
        if (localActivityInfo != null);
        PackageInfo localPackageInfo = localPackageManager.getPackageInfo(localActivityInfo.packageName, 0);
        if (localPackageInfo != null);
        e = localPackageInfo.versionCode + "." + localActivityInfo.packageName;
        str = e;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
      }
    }
  }

  public static a g(Context paramContext)
  {
    if (f == null)
      f = (AudioManager)paramContext.getSystemService("audio");
    int j = f.getMode();
    if (c());
    for (a locala = a.e; ; locala = a.b)
      while (true)
      {
        return locala;
        if ((f.isMusicActive()) || (f.isSpeakerphoneOn()) || (j == 2) || (j == 1))
          locala = a.d;
        int k = f.getRingerMode();
        if ((k != 0) && (k != 1))
          break;
        locala = a.d;
      }
  }

  public static void h(Context paramContext)
  {
    if (h);
    while (true)
    {
      return;
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.USER_PRESENT");
      localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
      paramContext.registerReceiver(new UserActivityReceiver(), localIntentFilter);
      h = true;
    }
  }

  public static String i(Context paramContext)
  {
    if (i == null)
    {
      String str1 = new WebView(paramContext).getSettings().getUserAgentString();
      if ((str1 == null) || (str1.length() == 0) || (str1.equals("Java0")))
      {
        String str2 = System.getProperty("os.name", "Linux");
        String str3 = "Android " + Build.VERSION.RELEASE;
        Locale localLocale = Locale.getDefault();
        String str4 = localLocale.getLanguage().toLowerCase(Locale.US);
        if (str4.length() == 0)
          str4 = "en";
        String str5 = localLocale.getCountry().toLowerCase(Locale.US);
        if (str5.length() > 0)
          str4 = str4 + "-" + str5;
        String str6 = Build.MODEL + " Build/" + Build.ID;
        str1 = "Mozilla/5.0 (" + str2 + "; U; " + str3 + "; " + str4 + "; " + str6 + ") AppleWebKit/0.0 (KHTML, like " + "Gecko) Version/0.0 Mobile Safari/0.0";
      }
      i = str1 + " (Mobile; " + "afma-sdk-a-v" + "6.2.1" + ")";
    }
    return i;
  }

  public static class UserActivityReceiver extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("android.intent.action.USER_PRESENT"))
        AdUtil.a(true);
      while (true)
      {
        return;
        if (!paramIntent.getAction().equals("android.intent.action.SCREEN_OFF"))
          continue;
        AdUtil.a(false);
      }
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
 * Qualified Name:     com.google.ads.util.AdUtil
 * JD-Core Version:    0.5.4
 */