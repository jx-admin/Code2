package com.google.ads;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import java.util.List;

public class ah
{
  public static boolean a(Context paramContext)
  {
    Intent localIntent = new Intent();
    localIntent.setComponent(new ComponentName("com.google.android.apps.plus", "com.google.android.apps.circles.platform.PlusOneActivity"));
    return a(localIntent, paramContext);
  }

  public static boolean a(Intent paramIntent, Context paramContext)
  {
    if (paramContext.getPackageManager().queryIntentActivities(paramIntent, 65536).size() > 0);
    for (int i = 1; ; i = 0)
      return i;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.google.ads.ah
 * JD-Core Version:    0.5.4
 */