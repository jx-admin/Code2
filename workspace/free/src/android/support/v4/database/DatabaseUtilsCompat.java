package android.support.v4.database;

import android.text.TextUtils;

public class DatabaseUtilsCompat
{
  public static String[] appendSelectionArgs(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    String[] arrayOfString;
    if ((paramArrayOfString1 == null) || (paramArrayOfString1.length == 0))
      arrayOfString = paramArrayOfString2;
    while (true)
    {
      return arrayOfString;
      arrayOfString = new String[paramArrayOfString1.length + paramArrayOfString2.length];
      System.arraycopy(paramArrayOfString1, 0, arrayOfString, 0, paramArrayOfString1.length);
      System.arraycopy(paramArrayOfString2, 0, arrayOfString, paramArrayOfString1.length, paramArrayOfString2.length);
    }
  }

  public static String concatenateWhere(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1));
    while (true)
    {
      return paramString2;
      if (TextUtils.isEmpty(paramString2))
        paramString2 = paramString1;
      paramString2 = "(" + paramString1 + ") AND (" + paramString2 + ")";
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.database.DatabaseUtilsCompat
 * JD-Core Version:    0.5.4
 */