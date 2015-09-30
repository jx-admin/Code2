package com.google.ads.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;
import com.google.ads.AdActivity;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;

class AdWebView$1
  implements DownloadListener
{
  public void onDownloadStart(String paramString1, String paramString2, String paramString3, String paramString4, long paramLong)
  {
    try
    {
      Intent localIntent = new Intent("android.intent.action.VIEW");
      localIntent.setDataAndType(Uri.parse(paramString1), paramString4);
      AdActivity localAdActivity = this.a.d();
      if ((localAdActivity != null) && (AdUtil.a(localIntent, localAdActivity)))
        localAdActivity.startActivity(localIntent);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      b.a("Couldn't find an Activity to view url/mimetype: " + paramString1 + " / " + paramString4);
    }
    catch (Throwable localThrowable)
    {
      b.b("Unknown error trying to start activity to view URL: " + paramString1, localThrowable);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.internal.AdWebView.1
 * JD-Core Version:    0.5.4
 */