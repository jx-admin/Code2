package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;

public class ContextCompat
{
  public static boolean startActivities(Context paramContext, Intent[] paramArrayOfIntent)
  {
    return startActivities(paramContext, paramArrayOfIntent, null);
  }

  public static boolean startActivities(Context paramContext, Intent[] paramArrayOfIntent, Bundle paramBundle)
  {
    int i = 1;
    int j = Build.VERSION.SDK_INT;
    if (j >= 16)
      ContextCompatJellybean.startActivities(paramContext, paramArrayOfIntent, paramBundle);
    while (true)
    {
      return i;
      if (j >= 11)
        ContextCompatHoneycomb.startActivities(paramContext, paramArrayOfIntent);
      i = 0;
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.content.ContextCompat
 * JD-Core Version:    0.5.4
 */