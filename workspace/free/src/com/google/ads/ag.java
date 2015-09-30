package com.google.ads;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.webkit.WebView;
import com.google.ads.internal.a;
import com.google.ads.util.b;
import com.google.ads.util.f;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Locale;

public final class ag
{
  private static final a a = (a)a.a.b();

  public static void a(Activity paramActivity)
  {
    new Thread(new a(paramActivity)).start();
  }

  public static void a(Activity paramActivity, WebView paramWebView, String paramString)
  {
    new Thread(new b(paramActivity, paramWebView, paramString)).start();
  }

  public static void a(WebView paramWebView, String paramString)
  {
    a locala = a;
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramString;
    locala.a(paramWebView, String.format(localLocale, "(G_resizeIframe(%s))", arrayOfObject));
  }

  public static void a(WebView paramWebView, boolean paramBoolean)
  {
    a locala = a;
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Boolean.valueOf(paramBoolean);
    locala.a(paramWebView, String.format(localLocale, "(G_updatePlusOne(%b))", arrayOfObject));
  }

  public static boolean a(Context paramContext, long paramLong)
  {
    return a(paramContext, paramLong, PreferenceManager.getDefaultSharedPreferences(paramContext.getApplicationContext()));
  }

  static boolean a(Context paramContext, long paramLong, SharedPreferences paramSharedPreferences)
  {
    if ((ah.a(paramContext)) && (((!paramSharedPreferences.contains("drt")) || (!paramSharedPreferences.contains("drt_ts")) || (paramSharedPreferences.getLong("drt_ts", 0L) < new Date().getTime() - paramLong))));
    for (int i = 1; ; i = 0)
      return i;
  }

  private static class a
    implements Runnable
  {
    private final WeakReference<Activity> a;
    private final SharedPreferences.Editor b;

    public a(Activity paramActivity)
    {
      this(paramActivity, null);
    }

    a(Activity paramActivity, SharedPreferences.Editor paramEditor)
    {
      this.a = new WeakReference(paramActivity);
      this.b = paramEditor;
    }

    private SharedPreferences.Editor a(Activity paramActivity)
    {
      if (this.b == null);
      for (SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(paramActivity.getApplicationContext()).edit(); ; localEditor = this.b)
        return localEditor;
    }

    public void run()
    {
      while (true)
      {
        SharedPreferences.Editor localEditor;
        try
        {
          Activity localActivity = (Activity)this.a.get();
          if (localActivity == null)
          {
            b.a("Activity was null while making a doritos cookie request.");
          }
          else
          {
            Cursor localCursor = localActivity.getContentResolver().query(af.b, af.d, null, null, null);
            if ((localCursor != null) && (localCursor.moveToFirst()) && (localCursor.getColumnNames().length > 0))
            {
              str = localCursor.getString(localCursor.getColumnIndex(localCursor.getColumnName(0)));
              localEditor = a(localActivity);
              if (TextUtils.isEmpty(str))
                break label163;
              localEditor.putString("drt", str);
              localEditor.putLong("drt_ts", new Date().getTime());
              localEditor.commit();
            }
          }
        }
        catch (Throwable localThrowable)
        {
          b.b("An unknown error occurred while sending a doritos request.", localThrowable);
        }
        return;
        b.a("Google+ app not installed, not storing doritos cookie");
        String str = null;
        continue;
        label163: localEditor.putString("drt", "");
        localEditor.putLong("drt_ts", 0L);
      }
    }
  }

  private static class b
    implements Runnable
  {
    private final WeakReference<Activity> a;
    private final WebView b;
    private final String c;

    public b(Activity paramActivity, WebView paramWebView, String paramString)
    {
      this.a = new WeakReference(paramActivity);
      this.c = paramString;
      this.b = paramWebView;
    }

    public void run()
    {
      while (true)
      {
        try
        {
          Uri localUri = Uri.withAppendedPath(af.a, this.c);
          Activity localActivity = (Activity)this.a.get();
          if (localActivity == null)
          {
            b.a("Activity was null while getting the +1 button state.");
          }
          else
          {
            Cursor localCursor = localActivity.getContentResolver().query(localUri, af.c, null, null, null);
            if ((localCursor != null) && (localCursor.moveToFirst()))
              if (localCursor.getInt(localCursor.getColumnIndex("has_plus1")) == 1)
              {
                bool = true;
                this.b.post(new ag.c(this.b, bool));
              }
          }
        }
        catch (Throwable localThrowable)
        {
          b.b("An unknown error occurred while updating the +1 state.", localThrowable);
        }
        return;
        boolean bool = false;
        continue;
        b.a("Google+ app not installed, showing ad as not +1'd");
        bool = false;
      }
    }
  }

  static class c
    implements Runnable
  {
    private final boolean a;
    private final WebView b;

    public c(WebView paramWebView, boolean paramBoolean)
    {
      this.b = paramWebView;
      this.a = paramBoolean;
    }

    public void run()
    {
      ag.a(this.b, this.a);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.ag
 * JD-Core Version:    0.5.4
 */