package com.google.ads;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.google.ads.internal.AdVideoView;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.a;
import com.google.ads.internal.d;
import com.google.ads.internal.e;
import com.google.ads.internal.i;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import com.google.ads.util.f;
import com.google.ads.util.g;
import com.google.ads.util.i.b;
import com.google.ads.util.i.c;
import com.google.ads.util.i.d;
import java.util.HashMap;
import java.util.Map;

public class AdActivity extends Activity
  implements View.OnClickListener
{
  public static final String BASE_URL_PARAM = "baseurl";
  public static final String CUSTOM_CLOSE_PARAM = "custom_close";
  public static final String HTML_PARAM = "html";
  public static final String INTENT_ACTION_PARAM = "i";
  public static final String ORIENTATION_PARAM = "o";
  public static final String TYPE_PARAM = "m";
  public static final String URL_PARAM = "u";
  private static final a a = (a)a.a.b();
  private static final Object b = new Object();
  private static AdActivity c = null;
  private static d d = null;
  private static AdActivity e = null;
  private static AdActivity f = null;
  private static final StaticMethodWrapper g = new StaticMethodWrapper();
  private AdWebView h;
  private FrameLayout i;
  private int j;
  private ViewGroup k = null;
  private boolean l;
  private long m;
  private RelativeLayout n;
  private AdActivity o = null;
  private boolean p;
  private boolean q;
  private boolean r;
  private boolean s;
  private AdVideoView t;

  private RelativeLayout.LayoutParams a(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(paramInt3, paramInt4);
    localLayoutParams.setMargins(paramInt1, paramInt2, 0, 0);
    localLayoutParams.addRule(10);
    localLayoutParams.addRule(9);
    return localLayoutParams;
  }

  private void a(String paramString)
  {
    b.b(paramString);
    finish();
  }

  private void a(String paramString, Throwable paramThrowable)
  {
    b.b(paramString, paramThrowable);
    finish();
  }

  private void d()
  {
    if (!this.l)
    {
      if (this.h != null)
      {
        a.b(this.h);
        this.h.setAdActivity(null);
        this.h.setIsExpandedMraid(false);
        if ((!this.q) && (this.n != null) && (this.k != null))
        {
          if ((!this.r) || (this.s))
            break label238;
          b.a("Disabling hardware acceleration on collapsing MRAID WebView.");
          this.h.b();
          label87: this.n.removeView(this.h);
          this.k.addView(this.h);
        }
      }
      if (this.t != null)
      {
        this.t.e();
        this.t = null;
      }
      if (this == c)
        c = null;
      f = this.o;
    }
    synchronized (b)
    {
      if ((d != null) && (this.q) && (this.h != null))
      {
        if (this.h == d.k())
          d.a();
        this.h.stopLoading();
      }
      if (this == e)
      {
        e = null;
        if (d == null)
          break label267;
        d.t();
        d = null;
      }
      this.l = true;
      b.a("AdActivity is closing.");
      return;
      label238: if ((!this.r) && (this.s));
      b.a("Re-enabling hardware acceleration on collapsing MRAID WebView.");
      this.h.c();
      break label87:
      label267: b.e("currentAdManager is null while trying to destroy AdActivity.");
    }
  }

  public static boolean isShowing()
  {
    return g.isShowing();
  }

  public static void launchAdActivity(d paramd, e parame)
  {
    g.launchAdActivity(paramd, parame);
  }

  protected View a(int paramInt, boolean paramBoolean)
  {
    this.j = (int)TypedValue.applyDimension(1, paramInt, getResources().getDisplayMetrics());
    this.i = new FrameLayout(getApplicationContext());
    this.i.setMinimumWidth(this.j);
    this.i.setMinimumHeight(this.j);
    this.i.setOnClickListener(this);
    setCustomClose(paramBoolean);
    return this.i;
  }

  protected AdVideoView a(Activity paramActivity)
  {
    return new AdVideoView(paramActivity, this.h);
  }

  protected void a(AdWebView paramAdWebView, boolean paramBoolean1, int paramInt, boolean paramBoolean2, boolean paramBoolean3)
  {
    requestWindowFeature(1);
    Window localWindow = getWindow();
    localWindow.setFlags(1024, 1024);
    if (AdUtil.a >= 11)
    {
      if (!this.r)
        break label105;
      b.a("Enabling hardware acceleration on the AdActivity window.");
      g.a(localWindow);
    }
    label49: ViewParent localViewParent = paramAdWebView.getParent();
    if (localViewParent != null)
    {
      if (!paramBoolean2)
        break label128;
      if (!localViewParent instanceof ViewGroup)
        break label118;
      this.k = ((ViewGroup)localViewParent);
      this.k.removeView(paramAdWebView);
    }
    if (paramAdWebView.d() != null)
    {
      a("Interstitial created with an AdWebView that is already in use by another AdActivity.");
      while (true)
      {
        label104: return;
        label105: b.a("Disabling hardware acceleration on the AdActivity WebView.");
        paramAdWebView.b();
        break label49:
        label118: a("MRAID banner was not a child of a ViewGroup.");
        continue;
        label128: a("Interstitial created with an AdWebView that has a parent.");
      }
    }
    setRequestedOrientation(paramInt);
    paramAdWebView.setAdActivity(this);
    int i1;
    label157: View localView;
    RelativeLayout.LayoutParams localLayoutParams;
    if (paramBoolean2)
    {
      i1 = 50;
      localView = a(i1, paramBoolean3);
      this.n.addView(paramAdWebView, -1, -1);
      localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
      if (!paramBoolean2)
        break label272;
      localLayoutParams.addRule(10);
      localLayoutParams.addRule(11);
    }
    while (true)
    {
      this.n.addView(localView, localLayoutParams);
      this.n.setKeepScreenOn(true);
      setContentView(this.n);
      this.n.getRootView().setBackgroundColor(-16777216);
      if (paramBoolean1);
      a.a(paramAdWebView);
      break label104:
      i1 = 32;
      break label157:
      label272: localLayoutParams.addRule(10);
      localLayoutParams.addRule(9);
    }
  }

  protected void a(d paramd)
  {
    this.h = null;
    this.m = SystemClock.elapsedRealtime();
    this.p = true;
    synchronized (b)
    {
      if (c == null)
      {
        c = this;
        paramd.v();
      }
      return;
    }
  }

  protected void a(HashMap<String, String> paramHashMap, d paramd)
  {
    Intent localIntent = new Intent();
    localIntent.setComponent(new ComponentName("com.google.android.apps.plus", "com.google.android.apps.circles.platform.PlusOneActivity"));
    localIntent.addCategory("android.intent.category.LAUNCHER");
    localIntent.putExtras(getIntent().getExtras());
    localIntent.putExtra("com.google.circles.platform.intent.extra.ENTITY", (String)paramHashMap.get("u"));
    localIntent.putExtra("com.google.circles.platform.intent.extra.ENTITY_TYPE", ai.b.a.c);
    localIntent.putExtra("com.google.circles.platform.intent.extra.ACTION", (String)paramHashMap.get("a"));
    a(paramd);
    try
    {
      b.a("Launching Google+ intent from AdActivity.");
      startActivityForResult(localIntent, 0);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      a(localActivityNotFoundException.getMessage(), localActivityNotFoundException);
    }
  }

  protected void b(HashMap<String, String> paramHashMap, d paramd)
  {
    if (paramHashMap == null)
      a("Could not get the paramMap in launchIntent()");
    label11: String str1;
    while (true)
    {
      return;
      str1 = (String)paramHashMap.get("u");
      if (str1 != null)
        break;
      a("Could not get the URL parameter in launchIntent().");
    }
    String str2 = (String)paramHashMap.get("i");
    String str3 = (String)paramHashMap.get("m");
    Uri localUri = Uri.parse(str1);
    if (str2 == null);
    for (Intent localIntent = new Intent("android.intent.action.VIEW", localUri); ; localIntent = new Intent(str2, localUri))
    {
      while (true)
      {
        if (str3 != null)
          localIntent.setDataAndType(localUri, str3);
        a(paramd);
        try
        {
          b.a("Launching an intent from AdActivity: " + localIntent.getAction() + " - " + localUri);
          startActivity(localIntent);
        }
        catch (ActivityNotFoundException localActivityNotFoundException)
        {
          a(localActivityNotFoundException.getMessage(), localActivityNotFoundException);
        }
      }
      break label11:
    }
  }

  public AdVideoView getAdVideoView()
  {
    return this.t;
  }

  // ERROR //
  public AdWebView getOpeningAdWebView()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: getfield 98	com/google/ads/AdActivity:o	Lcom/google/ads/AdActivity;
    //   6: ifnull +13 -> 19
    //   9: aload_0
    //   10: getfield 98	com/google/ads/AdActivity:o	Lcom/google/ads/AdActivity;
    //   13: getfield 130	com/google/ads/AdActivity:h	Lcom/google/ads/internal/AdWebView;
    //   16: astore_1
    //   17: aload_1
    //   18: areturn
    //   19: getstatic 82	com/google/ads/AdActivity:b	Ljava/lang/Object;
    //   22: astore_2
    //   23: aload_2
    //   24: monitorenter
    //   25: getstatic 86	com/google/ads/AdActivity:d	Lcom/google/ads/internal/d;
    //   28: ifnonnull +19 -> 47
    //   31: ldc_w 460
    //   34: invokestatic 196	com/google/ads/util/b:e	(Ljava/lang/String;)V
    //   37: aload_2
    //   38: monitorexit
    //   39: goto -22 -> 17
    //   42: astore_3
    //   43: aload_2
    //   44: monitorexit
    //   45: aload_3
    //   46: athrow
    //   47: getstatic 86	com/google/ads/AdActivity:d	Lcom/google/ads/internal/d;
    //   50: invokevirtual 179	com/google/ads/internal/d:k	()Lcom/google/ads/internal/AdWebView;
    //   53: astore 4
    //   55: aload 4
    //   57: aload_0
    //   58: getfield 130	com/google/ads/AdActivity:h	Lcom/google/ads/internal/AdWebView;
    //   61: if_acmpeq +11 -> 72
    //   64: aload_2
    //   65: monitorexit
    //   66: aload 4
    //   68: astore_1
    //   69: goto -52 -> 17
    //   72: aload_2
    //   73: monitorexit
    //   74: goto -57 -> 17
    //
    // Exception table:
    //   from	to	target	type
    //   25	45	42	finally
    //   47	74	42	finally
  }

  public void moveAdVideoView(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.t == null)
      return;
    this.t.setLayoutParams(a(paramInt1, paramInt2, paramInt3, paramInt4));
    this.t.requestLayout();
  }

  public void newAdVideoView(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.t != null)
      return;
    this.t = a(this);
    this.n.addView(this.t, 0, a(paramInt1, paramInt2, paramInt3, paramInt4));
    synchronized (b)
    {
      if (d == null)
        b.e("currentAdManager was null while trying to get the opening AdWebView.");
      else
        d.l().b(false);
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    String str2;
    if ((getOpeningAdWebView() != null) && (paramIntent != null) && (paramIntent.getExtras() != null) && (paramIntent.getExtras().getString("com.google.circles.platform.result.extra.CONFIRMATION") != null) && (paramIntent.getExtras().getString("com.google.circles.platform.result.extra.ACTION") != null))
    {
      String str1 = paramIntent.getExtras().getString("com.google.circles.platform.result.extra.CONFIRMATION");
      str2 = paramIntent.getExtras().getString("com.google.circles.platform.result.extra.ACTION");
      if (str1.equals("yes"))
      {
        if (!str2.equals("insert"))
          break label110;
        ag.a(getOpeningAdWebView(), true);
      }
    }
    while (true)
    {
      finish();
      return;
      label110: if (!str2.equals("delete"))
        continue;
      ag.a(getOpeningAdWebView(), false);
    }
  }

  public void onClick(View paramView)
  {
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    boolean bool1 = false;
    super.onCreate(paramBundle);
    this.l = false;
    d locald;
    boolean bool2;
    label248: String str1;
    label268: label274: label280: HashMap localHashMap;
    while (true)
    {
      Bundle localBundle;
      synchronized (b)
      {
        if (d != null)
        {
          locald = d;
          if (e == null)
          {
            e = this;
            locald.u();
          }
          if ((this.o == null) && (f != null))
            this.o = f;
          f = this;
          if (((locald.h().a()) && (e == this)) || ((locald.h().b()) && (this.o == e)))
            locald.w();
          bool2 = locald.q();
          l.a locala = (l.a)((l)locald.h().a.a()).a.a();
          if (AdUtil.a < ((Integer)locala.a.a()).intValue())
            break label268;
          i1 = 1;
          this.s = i1;
          if (AdUtil.a < ((Integer)locala.b.a()).intValue())
            break label274;
          i2 = 1;
          this.r = i2;
          this.n = null;
          this.p = false;
          this.q = true;
          this.t = null;
          localBundle = getIntent().getBundleExtra("com.google.ads.AdOpener");
          if (localBundle != null)
            break label280;
          a("Could not get the Bundle used to create AdActivity.");
          return;
        }
        a("Could not get currentAdManager.");
      }
      int i1 = 0;
      continue;
      int i2 = 0;
      continue;
      e locale = new e(localBundle);
      str1 = locale.b();
      localHashMap = locale.c();
      if (str1.equals("plusone"))
        a(localHashMap, locald);
      if (!str1.equals("intent"))
        break;
      b(localHashMap, locald);
    }
    this.n = new RelativeLayout(getApplicationContext());
    if (str1.equals("webapp"))
    {
      this.h = new AdWebView(locald.h(), null);
      Map localMap = a.c;
      boolean bool4;
      label405: String str3;
      String str4;
      String str5;
      int i4;
      label521: AdWebView localAdWebView;
      if (!bool2)
      {
        bool4 = true;
        i locali = i.a(locald, localMap, true, bool4);
        locali.d(true);
        if (bool2)
          locali.a(true);
        this.h.setWebViewClient(locali);
        String str2 = (String)localHashMap.get("u");
        str3 = (String)localHashMap.get("baseurl");
        str4 = (String)localHashMap.get("html");
        if (str2 == null)
          break label573;
        this.h.loadUrl(str2);
        str5 = (String)localHashMap.get("o");
        if (!"p".equals(str5))
          break label609;
        i4 = AdUtil.b();
        localAdWebView = this.h;
        if ((localHashMap == null) || (!"1".equals(localHashMap.get("custom_close"))))
          break label652;
      }
      for (boolean bool5 = true; ; bool5 = false)
      {
        a(localAdWebView, false, i4, bool2, bool5);
        break label248:
        bool4 = false;
        break label405:
        if (str4 != null)
          label573: this.h.loadDataWithBaseURL(str3, str4, "text/html", "utf-8", null);
        a("Could not get the URL or HTML parameter to show a web app.");
        break label248:
        if ("l".equals(str5))
          label609: i4 = AdUtil.a();
        if (this == e)
          i4 = locald.n();
        i4 = -1;
        label652: break label521:
      }
    }
    int i3;
    if ((str1.equals("interstitial")) || (str1.equals("expand")))
    {
      this.h = locald.k();
      i3 = locald.n();
      if (str1.equals("expand"))
      {
        this.h.setIsExpandedMraid(true);
        this.q = false;
        if ((localHashMap != null) && ("1".equals(localHashMap.get("custom_close"))))
          bool1 = true;
        if ((!this.r) || (this.s))
          break label837;
        b.a("Re-enabling hardware acceleration on expanding MRAID WebView.");
        this.h.c();
      }
    }
    for (boolean bool3 = bool1; ; bool3 = bool1)
    {
      while (true)
      {
        a(this.h, true, i3, bool2, bool3);
        break label248:
        bool3 = this.h.e();
      }
      a("Unknown AdOpener, <action: " + str1 + ">");
      label837: break label248:
    }
  }

  public void onDestroy()
  {
    if (this.n != null)
      this.n.removeAllViews();
    if (isFinishing())
    {
      d();
      if ((this.q) && (this.h != null))
      {
        this.h.stopLoading();
        this.h.destroy();
        this.h = null;
      }
    }
    super.onDestroy();
  }

  public void onPause()
  {
    if (isFinishing())
      d();
    super.onPause();
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    if ((this.p) && (paramBoolean) && (SystemClock.elapsedRealtime() - this.m > 250L))
    {
      b.d("Launcher AdActivity got focus and is closing.");
      finish();
    }
    super.onWindowFocusChanged(paramBoolean);
  }

  public void setCustomClose(boolean paramBoolean)
  {
    if (this.i == null)
      return;
    this.i.removeAllViews();
    if (paramBoolean)
      return;
    ImageButton localImageButton = new ImageButton(this);
    localImageButton.setImageResource(17301527);
    localImageButton.setBackgroundColor(0);
    localImageButton.setOnClickListener(this);
    localImageButton.setPadding(0, 0, 0, 0);
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(this.j, this.j, 17);
    this.i.addView(localImageButton, localLayoutParams);
  }

  public static class StaticMethodWrapper
  {
    public boolean isShowing()
    {
      while (true)
      {
        synchronized (AdActivity.a())
        {
          if (AdActivity.b() == null)
            break label23;
          i = 1;
          return i;
        }
        label23: int i = 0;
      }
    }

    public void launchAdActivity(d paramd, e parame)
    {
      while (true)
      {
        Activity localActivity;
        synchronized (AdActivity.a())
        {
          if (AdActivity.c() == null)
            AdActivity.b(paramd);
          do
          {
            localActivity = (Activity)paramd.h().e.a();
            if (localActivity != null)
              break label69;
            b.e("activity was null while launching an AdActivity.");
            return;
          }
          while (AdActivity.c() == paramd);
          b.b("Tried to launch a new AdActivity with a different AdManager.");
        }
        label69: Intent localIntent = new Intent(localActivity.getApplicationContext(), AdActivity.class);
        localIntent.putExtra("com.google.ads.AdOpener", parame.a());
        try
        {
          b.a("Launching AdActivity.");
          localActivity.startActivity(localIntent);
        }
        catch (ActivityNotFoundException localActivityNotFoundException)
        {
          b.b("Activity not found.", localActivityNotFoundException);
        }
      }
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.AdActivity
 * JD-Core Version:    0.5.4
 */