package com.google.ads;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;
import com.google.ads.internal.d;
import com.google.ads.internal.e;
import com.google.ads.util.i.b;
import com.google.ads.util.i.d;
import java.util.HashMap;

public class ai
  implements n
{
  public void a(d paramd, HashMap<String, String> paramHashMap, WebView paramWebView)
  {
    Context localContext = (Context)paramd.h().f.a();
    String str = (String)paramHashMap.get("a");
    if (str != null)
      if (str.equals("resize"))
        ag.a(paramWebView, (String)paramHashMap.get("u"));
    while (true)
    {
      return;
      if (str.equals("state"))
        ag.a((Activity)paramd.h().e.a(), paramWebView, (String)paramHashMap.get("u"));
      Intent localIntent = new Intent();
      localIntent.setComponent(new ComponentName("com.google.android.apps.plus", "com.google.android.apps.circles.platform.PlusOneActivity"));
      if (!ah.a(localIntent, localContext))
      {
        if (!ah.a(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.android.apps.plus")), localContext))
          continue;
        if ((!TextUtils.isEmpty((CharSequence)paramHashMap.get("d"))) && (!TextUtils.isEmpty((CharSequence)paramHashMap.get("o"))) && (!TextUtils.isEmpty((CharSequence)paramHashMap.get("c"))))
        {
          AlertDialog.Builder localBuilder = new AlertDialog.Builder(localContext);
          localBuilder.setMessage((CharSequence)paramHashMap.get("d")).setPositiveButton((CharSequence)paramHashMap.get("o"), new c(paramd)).setNegativeButton((CharSequence)paramHashMap.get("c"), new a());
          localBuilder.create().show();
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("u", "market://details?id=com.google.android.apps.plus");
        AdActivity.launchAdActivity(paramd, new e("intent", localHashMap));
      }
      AdActivity.launchAdActivity(paramd, new e("plusone", paramHashMap));
    }
  }

  private static class a
    implements DialogInterface.OnClickListener
  {
    public void onClick(DialogInterface paramDialogInterface, int paramInt)
    {
    }
  }

  public static enum b
  {
    public String c;

    static
    {
      b[] arrayOfb = new b[2];
      arrayOfb[0] = a;
      arrayOfb[1] = b;
      d = arrayOfb;
    }

    private b(String paramString)
    {
      this.c = paramString;
    }
  }

  private static class c
    implements DialogInterface.OnClickListener
  {
    private d a;

    public c(d paramd)
    {
      this.a = paramd;
    }

    public void onClick(DialogInterface paramDialogInterface, int paramInt)
    {
      HashMap localHashMap = new HashMap();
      localHashMap.put("u", "market://details?id=com.google.android.apps.plus");
      AdActivity.launchAdActivity(this.a, new e("intent", localHashMap));
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.ai
 * JD-Core Version:    0.5.4
 */