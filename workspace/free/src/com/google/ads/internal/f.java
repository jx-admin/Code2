package com.google.ads.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.m;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import com.google.ads.util.i.b;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class f
  implements Runnable
{
  private final c a;
  private final d b;
  private final a c;
  private volatile boolean d;
  private boolean e;
  private String f;
  private Thread g = null;

  f(c paramc, d paramd)
  {
    this(paramc, paramd, new a()
    {
      public HttpURLConnection a(URL paramURL)
        throws IOException
      {
        return (HttpURLConnection)paramURL.openConnection();
      }
    });
  }

  f(c paramc, d paramd, a parama)
  {
    this.a = paramc;
    this.b = paramd;
    this.c = parama;
  }

  private void a(Context paramContext, HttpURLConnection paramHttpURLConnection)
  {
    String str = PreferenceManager.getDefaultSharedPreferences(paramContext).getString("drt", "");
    if ((this.e) && (!TextUtils.isEmpty(str)))
    {
      if (AdUtil.a != 8)
        break label44;
      paramHttpURLConnection.addRequestProperty("X-Afma-drt-Cookie", str);
    }
    while (true)
    {
      return;
      label44: paramHttpURLConnection.addRequestProperty("Cookie", str);
    }
  }

  private void a(HttpURLConnection paramHttpURLConnection)
  {
    b(paramHttpURLConnection);
    f(paramHttpURLConnection);
    g(paramHttpURLConnection);
    h(paramHttpURLConnection);
    e(paramHttpURLConnection);
    i(paramHttpURLConnection);
    j(paramHttpURLConnection);
    k(paramHttpURLConnection);
    d(paramHttpURLConnection);
    c(paramHttpURLConnection);
    l(paramHttpURLConnection);
  }

  private void a(HttpURLConnection paramHttpURLConnection, int paramInt)
    throws IOException
  {
    String str2;
    if ((300 <= paramInt) && (paramInt < 400))
    {
      str2 = paramHttpURLConnection.getHeaderField("Location");
      if (str2 == null)
      {
        b.c("Could not get redirect location from a " + paramInt + " redirect.");
        this.a.a(AdRequest.ErrorCode.INTERNAL_ERROR);
        a();
      }
    }
    while (true)
    {
      return;
      a(paramHttpURLConnection);
      this.f = str2;
      continue;
      if (paramInt == 200)
      {
        a(paramHttpURLConnection);
        String str1 = AdUtil.a(new InputStreamReader(paramHttpURLConnection.getInputStream())).trim();
        b.a("Response content is: " + str1);
        if (TextUtils.isEmpty(str1))
        {
          b.a("Response message is null or zero length: " + str1);
          this.a.a(AdRequest.ErrorCode.NO_FILL);
          a();
        }
        this.a.a(str1, this.f);
        a();
      }
      if (paramInt == 400)
      {
        b.c("Bad request");
        this.a.a(AdRequest.ErrorCode.INVALID_REQUEST);
        a();
      }
      b.c("Invalid response code: " + paramInt);
      this.a.a(AdRequest.ErrorCode.INTERNAL_ERROR);
      a();
    }
  }

  private void b()
    throws MalformedURLException, IOException
  {
    while (!this.d)
    {
      URL localURL = new URL(this.f);
      HttpURLConnection localHttpURLConnection = this.c.a(localURL);
      try
      {
        a((Context)this.b.h().f.a(), localHttpURLConnection);
        AdUtil.a(localHttpURLConnection, (Context)this.b.h().f.a());
        localHttpURLConnection.setInstanceFollowRedirects(false);
        localHttpURLConnection.connect();
        a(localHttpURLConnection, localHttpURLConnection.getResponseCode());
        localHttpURLConnection.disconnect();
      }
      finally
      {
        localHttpURLConnection.disconnect();
      }
    }
  }

  private void b(HttpURLConnection paramHttpURLConnection)
  {
    String str = paramHttpURLConnection.getHeaderField("X-Afma-Debug-Dialog");
    if (TextUtils.isEmpty(str))
      return;
    this.a.e(str);
  }

  private void c(HttpURLConnection paramHttpURLConnection)
  {
    String str = paramHttpURLConnection.getHeaderField("Content-Type");
    if (TextUtils.isEmpty(str))
      return;
    this.a.b(str);
  }

  private void d(HttpURLConnection paramHttpURLConnection)
  {
    String str = paramHttpURLConnection.getHeaderField("X-Afma-Mediation");
    if (TextUtils.isEmpty(str))
      return;
    this.a.a(Boolean.valueOf(str).booleanValue());
  }

  private void e(HttpURLConnection paramHttpURLConnection)
  {
    String str = paramHttpURLConnection.getHeaderField("X-Afma-Interstitial-Timeout");
    if (!TextUtils.isEmpty(str));
    try
    {
      float f1 = Float.parseFloat(str);
      this.b.a(()(f1 * 1000.0F));
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      b.d("Could not get timeout value: " + str, localNumberFormatException);
    }
  }

  private void f(HttpURLConnection paramHttpURLConnection)
  {
    String str1 = paramHttpURLConnection.getHeaderField("X-Afma-Tracking-Urls");
    if (TextUtils.isEmpty(str1))
      return;
    for (String str2 : str1.trim().split("\\s+"))
      this.b.b(str2);
  }

  private void g(HttpURLConnection paramHttpURLConnection)
  {
    String str1 = paramHttpURLConnection.getHeaderField("X-Afma-Click-Tracking-Urls");
    if (TextUtils.isEmpty(str1))
      return;
    for (String str2 : str1.trim().split("\\s+"))
      this.a.a(str2);
  }

  private void h(HttpURLConnection paramHttpURLConnection)
  {
    String str = paramHttpURLConnection.getHeaderField("X-Afma-Refresh-Rate");
    if (!TextUtils.isEmpty(str));
    try
    {
      float f1 = Float.parseFloat(str);
      if (f1 <= 0.0F)
        break label83;
      this.b.a(f1);
      if (!this.b.s())
        this.b.f();
      label54: label83: return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      b.d("Could not get refresh value: " + str, localNumberFormatException);
      break label54:
      if (this.b.s());
      this.b.e();
    }
  }

  private void i(HttpURLConnection paramHttpURLConnection)
  {
    String str = paramHttpURLConnection.getHeaderField("X-Afma-Orientation");
    if (!TextUtils.isEmpty(str))
    {
      if (!str.equals("portrait"))
        break label36;
      this.a.a(AdUtil.b());
    }
    while (true)
    {
      return;
      label36: if (!str.equals("landscape"))
        continue;
      this.a.a(AdUtil.a());
    }
  }

  private void j(HttpURLConnection paramHttpURLConnection)
  {
    if (!TextUtils.isEmpty(paramHttpURLConnection.getHeaderField("X-Afma-Doritos-Cache-Life")));
    try
    {
      long l = Long.parseLong(paramHttpURLConnection.getHeaderField("X-Afma-Doritos-Cache-Life"));
      this.b.b(l);
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      b.e("Got bad value of Doritos cookie cache life from header: " + paramHttpURLConnection.getHeaderField("X-Afma-Doritos-Cache-Life") + ". Using default value instead.");
    }
  }

  private void k(HttpURLConnection paramHttpURLConnection)
  {
    String str = paramHttpURLConnection.getHeaderField("Cache-Control");
    if (TextUtils.isEmpty(str))
      return;
    this.a.c(str);
  }

  private void l(HttpURLConnection paramHttpURLConnection)
  {
    String str = paramHttpURLConnection.getHeaderField("X-Afma-Ad-Size");
    if (TextUtils.isEmpty(str))
      return;
    try
    {
      String[] arrayOfString = str.split("x", 2);
      if (arrayOfString.length != 2)
      {
        b.e("Could not parse size header: " + str);
      }
      else
      {
        int i = Integer.parseInt(arrayOfString[0]);
        int j = Integer.parseInt(arrayOfString[1]);
        this.a.a(new AdSize(i, j));
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      b.e("Could not parse size header: " + str);
    }
  }

  void a()
  {
    this.d = true;
  }

  void a(String paramString)
  {
    monitorenter;
    try
    {
      if (this.g == null)
      {
        this.f = paramString;
        this.d = false;
        this.g = new Thread(this);
        this.g.start();
      }
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
      throw localObject;
    }
  }

  public void a(boolean paramBoolean)
  {
    this.e = paramBoolean;
  }

  public void run()
  {
    try
    {
      b();
      return;
    }
    catch (MalformedURLException localMalformedURLException)
    {
      b.b("Received malformed ad url from javascript.", localMalformedURLException);
      this.a.a(AdRequest.ErrorCode.INTERNAL_ERROR);
    }
    catch (IOException localIOException)
    {
      b.d("IOException connecting to ad url.", localIOException);
      this.a.a(AdRequest.ErrorCode.NETWORK_ERROR);
    }
    catch (Throwable localThrowable)
    {
      b.b("An unknown error occurred in AdResponseLoader.", localThrowable);
      this.a.a(AdRequest.ErrorCode.INTERNAL_ERROR);
    }
  }

  public static abstract interface a
  {
    public abstract HttpURLConnection a(URL paramURL)
      throws IOException;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.internal.f
 * JD-Core Version:    0.5.4
 */