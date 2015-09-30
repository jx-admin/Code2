package com.google.ads.internal;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.ads.AdActivity;
import com.google.ads.AdSize;
import com.google.ads.m;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import com.google.ads.util.g;
import com.google.ads.util.g.a;
import com.google.ads.util.h.a;
import com.google.ads.util.i.b;
import java.lang.ref.WeakReference;

public class AdWebView extends WebView
{
  private WeakReference<AdActivity> a;
  private AdSize b;
  private boolean c;
  private boolean d;
  private boolean e;

  public AdWebView(m paramm, AdSize paramAdSize)
  {
    super((Context)paramm.f.a());
    this.b = paramAdSize;
    this.a = null;
    this.c = false;
    this.d = false;
    this.e = false;
    setBackgroundColor(0);
    AdUtil.a(this);
    WebSettings localWebSettings = getSettings();
    localWebSettings.setSupportMultipleWindows(false);
    localWebSettings.setJavaScriptEnabled(true);
    localWebSettings.setSavePassword(false);
    setDownloadListener(new AdWebView.1(this));
    if (AdUtil.a >= 11)
      g.a(localWebSettings, paramm);
    setScrollBarStyle(33554432);
    if (AdUtil.a >= 14)
      setWebChromeClient(new h.a(paramm));
    while (true)
    {
      return;
      if (AdUtil.a < 11)
        continue;
      setWebChromeClient(new g.a(paramm));
    }
  }

  public void a()
  {
    AdActivity localAdActivity = d();
    if (localAdActivity == null)
      return;
    localAdActivity.finish();
  }

  public void b()
  {
    if (AdUtil.a >= 11)
      g.a(this);
    this.d = true;
  }

  public void c()
  {
    if ((this.d) && (AdUtil.a >= 11))
      g.b(this);
    this.d = false;
  }

  public AdActivity d()
  {
    if (this.a != null);
    for (AdActivity localAdActivity = (AdActivity)this.a.get(); ; localAdActivity = null)
      return localAdActivity;
  }

  public void destroy()
  {
    try
    {
      super.destroy();
      setWebViewClient(new WebViewClient());
      return;
    }
    catch (Throwable localThrowable)
    {
      b.b("An error occurred while destroying an AdWebView:", localThrowable);
    }
  }

  public boolean e()
  {
    return this.e;
  }

  public boolean f()
  {
    return this.d;
  }

  public void loadDataWithBaseURL(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    try
    {
      super.loadDataWithBaseURL(paramString1, paramString2, paramString3, paramString4, paramString5);
      return;
    }
    catch (Throwable localThrowable)
    {
      b.b("An error occurred while loading data in AdWebView:", localThrowable);
    }
  }

  public void loadUrl(String paramString)
  {
    try
    {
      super.loadUrl(paramString);
      return;
    }
    catch (Throwable localThrowable)
    {
      b.b("An error occurred while loading a URL in AdWebView:", localThrowable);
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = 2147483647;
    monitorenter;
    while (true)
      try
      {
        if (isInEditMode())
        {
          super.onMeasure(paramInt1, paramInt2);
          label18: return;
        }
        if (this.b != null)
          if (!this.c)
            break label51;
      }
      finally
      {
        monitorexit;
      }
    label51: int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getSize(paramInt1);
    int l = View.MeasureSpec.getMode(paramInt2);
    int i1 = View.MeasureSpec.getSize(paramInt2);
    float f = getContext().getResources().getDisplayMetrics().density;
    int i2 = (int)(f * this.b.getWidth());
    int i3 = (int)(f * this.b.getHeight());
    if (j != -2147483648)
      if (j != 1073741824);
    while (true)
    {
      b.e("Not enough space to show ad! Wants: <" + i2 + ", " + i3 + ">, Has: <" + k + ", " + i1 + ">");
      setVisibility(8);
      setMeasuredDimension(k, i1);
      break label18:
      do
      {
        setMeasuredDimension(i2, i3);
        break label18:
        int i4 = i;
        break label231:
        i4 = k;
        if ((l == -2147483648) || (l == 1073741824))
          label231: i = i1;
        if (i2 - f * 6.0F > i4);
      }
      while (i3 <= i);
    }
  }

  public void setAdActivity(AdActivity paramAdActivity)
  {
    this.a = new WeakReference(paramAdActivity);
  }

  public void setAdSize(AdSize paramAdSize)
  {
    monitorenter;
    try
    {
      this.b = paramAdSize;
      requestLayout();
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

  public void setCustomClose(boolean paramBoolean)
  {
    this.e = paramBoolean;
    if (this.a == null)
      return;
    AdActivity localAdActivity = (AdActivity)this.a.get();
    if (localAdActivity == null)
      return;
    localAdActivity.setCustomClose(paramBoolean);
  }

  public void setIsExpandedMraid(boolean paramBoolean)
  {
    this.c = paramBoolean;
  }

  public void stopLoading()
  {
    try
    {
      super.stopLoading();
      return;
    }
    catch (Throwable localThrowable)
    {
      b.d("An error occurred while stopping loading in AdWebView:", localThrowable);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.internal.AdWebView
 * JD-Core Version:    0.5.4
 */