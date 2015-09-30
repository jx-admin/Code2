package com.google.ads.util;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.ads.AdActivity;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.c;
import com.google.ads.internal.d;
import com.google.ads.internal.i;
import com.google.ads.l;
import com.google.ads.l.a;
import com.google.ads.m;
import com.google.ads.n;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@TargetApi(11)
public class g
{
  public static void a(View paramView)
  {
    paramView.setLayerType(1, null);
  }

  public static void a(Window paramWindow)
  {
    paramWindow.setFlags(16777216, 16777216);
  }

  public static void a(WebSettings paramWebSettings, m paramm)
  {
    Context localContext = (Context)paramm.f.a();
    l.a locala = (l.a)((l)paramm.a.a()).a.a();
    paramWebSettings.setAppCacheEnabled(true);
    paramWebSettings.setAppCacheMaxSize(((Long)locala.f.a()).longValue());
    paramWebSettings.setAppCachePath(new File(localContext.getCacheDir(), "admob").getAbsolutePath());
    paramWebSettings.setDatabaseEnabled(true);
    paramWebSettings.setDatabasePath(localContext.getDatabasePath("admob").getAbsolutePath());
    paramWebSettings.setDomStorageEnabled(true);
    paramWebSettings.setSupportZoom(true);
    paramWebSettings.setBuiltInZoomControls(true);
    paramWebSettings.setDisplayZoomControls(false);
  }

  public static void b(View paramView)
  {
    paramView.setLayerType(0, null);
  }

  public static class a extends WebChromeClient
  {
    private final m a;

    public a(m paramm)
    {
      this.a = paramm;
    }

    private static void a(AlertDialog.Builder paramBuilder, Context paramContext, String paramString1, String paramString2, JsPromptResult paramJsPromptResult)
    {
      LinearLayout localLinearLayout = new LinearLayout(paramContext);
      localLinearLayout.setOrientation(1);
      TextView localTextView = new TextView(paramContext);
      localTextView.setText(paramString1);
      EditText localEditText = new EditText(paramContext);
      localEditText.setText(paramString2);
      localLinearLayout.addView(localTextView);
      localLinearLayout.addView(localEditText);
      paramBuilder.setView(localLinearLayout).setPositiveButton(17039370, new g.a.6(paramJsPromptResult, localEditText)).setNegativeButton(17039360, new g.a.5(paramJsPromptResult)).setOnCancelListener(new g.a.4(paramJsPromptResult)).create().show();
    }

    private static void a(AlertDialog.Builder paramBuilder, String paramString, JsResult paramJsResult)
    {
      paramBuilder.setMessage(paramString).setPositiveButton(17039370, new g.a.3(paramJsResult)).setNegativeButton(17039360, new g.a.2(paramJsResult)).setOnCancelListener(new g.a.1(paramJsResult)).create().show();
    }

    private static boolean a(WebView paramWebView, String paramString1, String paramString2, String paramString3, JsResult paramJsResult, JsPromptResult paramJsPromptResult, boolean paramBoolean)
    {
      AlertDialog.Builder localBuilder;
      if (paramWebView instanceof AdWebView)
      {
        AdActivity localAdActivity = ((AdWebView)paramWebView).d();
        if (localAdActivity != null)
        {
          localBuilder = new AlertDialog.Builder(localAdActivity);
          localBuilder.setTitle(paramString1);
          if (paramBoolean)
            a(localBuilder, localAdActivity, paramString2, paramString3, paramJsPromptResult);
        }
      }
      for (int i = 1; ; i = 0)
      {
        label55: return i;
        a(localBuilder, paramString2, paramJsResult);
        break label55:
      }
    }

    public void onCloseWindow(WebView paramWebView)
    {
      if (!paramWebView instanceof AdWebView)
        return;
      ((AdWebView)paramWebView).a();
    }

    public boolean onConsoleMessage(ConsoleMessage paramConsoleMessage)
    {
      String str = "JS: " + paramConsoleMessage.message() + " (" + paramConsoleMessage.sourceId() + ":" + paramConsoleMessage.lineNumber() + ")";
      switch (g.1.a[paramConsoleMessage.messageLevel().ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      }
      while (true)
      {
        return super.onConsoleMessage(paramConsoleMessage);
        b.b(str);
        continue;
        b.e(str);
        continue;
        b.c(str);
        continue;
        b.a(str);
      }
    }

    public void onExceededDatabaseQuota(String paramString1, String paramString2, long paramLong1, long paramLong2, long paramLong3, WebStorage.QuotaUpdater paramQuotaUpdater)
    {
      l.a locala = (l.a)((l)this.a.a.a()).a.a();
      long l = ((Long)locala.i.a()).longValue() - paramLong3;
      if (l <= 0L)
      {
        paramQuotaUpdater.updateQuota(paramLong1);
        label58: return;
      }
      if (paramLong1 == 0L)
        if ((paramLong2 > l) || (paramLong2 > ((Long)locala.j.a()).longValue()));
      while (true)
      {
        paramQuotaUpdater.updateQuota(paramLong2);
        break label58:
        paramLong2 = 0L;
        continue;
        if (paramLong2 == 0L)
          paramLong2 = Math.min(paramLong1 + Math.min(((Long)locala.k.a()).longValue(), l), ((Long)locala.j.a()).longValue());
        if (paramLong2 <= Math.min(((Long)locala.j.a()).longValue() - paramLong1, l))
          paramLong1 += paramLong2;
        paramLong2 = paramLong1;
      }
    }

    public boolean onJsAlert(WebView paramWebView, String paramString1, String paramString2, JsResult paramJsResult)
    {
      return a(paramWebView, paramString1, paramString2, null, paramJsResult, null, false);
    }

    public boolean onJsBeforeUnload(WebView paramWebView, String paramString1, String paramString2, JsResult paramJsResult)
    {
      return a(paramWebView, paramString1, paramString2, null, paramJsResult, null, false);
    }

    public boolean onJsConfirm(WebView paramWebView, String paramString1, String paramString2, JsResult paramJsResult)
    {
      return a(paramWebView, paramString1, paramString2, null, paramJsResult, null, false);
    }

    public boolean onJsPrompt(WebView paramWebView, String paramString1, String paramString2, String paramString3, JsPromptResult paramJsPromptResult)
    {
      return a(paramWebView, paramString1, paramString2, paramString3, null, paramJsPromptResult, true);
    }

    public void onReachedMaxAppCacheSize(long paramLong1, long paramLong2, WebStorage.QuotaUpdater paramQuotaUpdater)
    {
      l.a locala = (l.a)((l)this.a.a.a()).a.a();
      long l1 = ((Long)locala.h.a()).longValue() - paramLong2;
      long l2 = paramLong1 + ((Long)locala.g.a()).longValue();
      if (l1 < l2)
        paramQuotaUpdater.updateQuota(0L);
      while (true)
      {
        return;
        paramQuotaUpdater.updateQuota(l2);
      }
    }

    public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
    {
      paramCustomViewCallback.onCustomViewHidden();
    }
  }

  public static class b extends i
  {
    public b(d paramd, Map<String, n> paramMap, boolean paramBoolean1, boolean paramBoolean2)
    {
      super(paramd, paramMap, paramBoolean1, paramBoolean2);
    }

    private static WebResourceResponse a(String paramString, Context paramContext)
      throws IOException
    {
      HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(paramString).openConnection();
      try
      {
        AdUtil.a(localHttpURLConnection, paramContext.getApplicationContext());
        localHttpURLConnection.connect();
        WebResourceResponse localWebResourceResponse = new WebResourceResponse("application/javascript", "UTF-8", new ByteArrayInputStream(AdUtil.a(new InputStreamReader(localHttpURLConnection.getInputStream())).getBytes("UTF-8")));
        return localWebResourceResponse;
      }
      finally
      {
        localHttpURLConnection.disconnect();
      }
    }

    public WebResourceResponse shouldInterceptRequest(WebView paramWebView, String paramString)
    {
      l.a locala;
      Object localObject;
      try
      {
        if (!"mraid.js".equalsIgnoreCase(new File(paramString).getName()))
          break label161;
        c localc = this.a.j();
        if (localc != null)
        {
          localc.b(true);
          locala = (l.a)((l)this.a.h().a.a()).a.a();
          if (this.a.h().b())
            break label227;
          if (!this.b)
            break label172;
          String str3 = (String)locala.d.a();
          b.a("shouldInterceptRequest(" + str3 + ")");
          localObject = a(str3, paramWebView.getContext());
          break label296:
        }
        label161: label172: label227: this.a.a(true);
      }
      catch (IOException localIOException)
      {
        b.d("IOException fetching MRAID JS.", localIOException);
        localObject = super.shouldInterceptRequest(paramWebView, paramString);
        break label296:
        String str2 = (String)locala.c.a();
        b.a("shouldInterceptRequest(" + str2 + ")");
        localObject = a(str2, paramWebView.getContext());
        break label296:
        String str1 = (String)locala.e.a();
        b.a("shouldInterceptRequest(" + str1 + ")");
        WebResourceResponse localWebResourceResponse = a(str1, paramWebView.getContext());
        localObject = localWebResourceResponse;
        break label296:
      }
      catch (Throwable localThrowable)
      {
        b.b("An unknown error occurred fetching MRAID JS.", localThrowable);
        break label161:
      }
      label296: return (WebResourceResponse)localObject;
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.util.g
 * JD-Core Version:    0.5.4
 */