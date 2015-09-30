package android.support.v4.util;

import java.io.PrintWriter;

public class TimeUtils
{
  public static final int HUNDRED_DAY_FIELD_LEN = 19;
  private static final int SECONDS_PER_DAY = 86400;
  private static final int SECONDS_PER_HOUR = 3600;
  private static final int SECONDS_PER_MINUTE = 60;
  private static char[] sFormatStr;
  private static final Object sFormatSync = new Object();

  static
  {
    sFormatStr = new char[24];
  }

  private static int accumField(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    if ((paramInt1 > 99) || ((paramBoolean) && (paramInt3 >= 3)));
    for (int i = paramInt2 + 3; ; i = 0)
      while (true)
      {
        return i;
        if ((paramInt1 > 9) || ((paramBoolean) && (paramInt3 >= 2)))
          i = paramInt2 + 2;
        if ((!paramBoolean) && (paramInt1 <= 0))
          break;
        i = paramInt2 + 1;
      }
  }

  public static void formatDuration(long paramLong1, long paramLong2, PrintWriter paramPrintWriter)
  {
    if (paramLong1 == 0L)
      paramPrintWriter.print("--");
    while (true)
    {
      return;
      formatDuration(paramLong1 - paramLong2, paramPrintWriter, 0);
    }
  }

  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter)
  {
    formatDuration(paramLong, paramPrintWriter, 0);
  }

  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter, int paramInt)
  {
    synchronized (sFormatSync)
    {
      int i = formatDurationLocked(paramLong, paramInt);
      paramPrintWriter.print(new String(sFormatStr, 0, i));
      return;
    }
  }

  public static void formatDuration(long paramLong, StringBuilder paramStringBuilder)
  {
    synchronized (sFormatSync)
    {
      int i = formatDurationLocked(paramLong, 0);
      paramStringBuilder.append(sFormatStr, 0, i);
      return;
    }
  }

  private static int formatDurationLocked(long paramLong, int paramInt)
  {
    if (sFormatStr.length < paramInt)
      sFormatStr = new char[paramInt];
    char[] arrayOfChar = sFormatStr;
    int i15;
    if (paramLong == 0L)
    {
      int i22 = paramInt - 1;
      while (i22 < 0)
        arrayOfChar[0] = ' ';
      arrayOfChar[0] = '0';
      i15 = 1;
      label50: return i15;
    }
    int i;
    label63: int j;
    int k;
    int l;
    int i1;
    int i2;
    int i3;
    boolean bool4;
    label192: boolean bool5;
    label214: boolean bool6;
    label236: int i19;
    if (paramLong > 0L)
    {
      i = 43;
      j = (int)(paramLong % 1000L);
      k = (int)Math.floor(paramLong / 1000L);
      l = 0;
      i1 = 0;
      i2 = 0;
      if (k > 86400)
      {
        l = k / 86400;
        k -= 86400 * l;
      }
      if (k > 3600)
      {
        i1 = k / 3600;
        k -= i1 * 3600;
      }
      if (k > 60)
      {
        i2 = k / 60;
        k -= i2 * 60;
      }
      i3 = 0;
      if (paramInt == 0)
        break label329;
      int i16 = accumField(l, 1, false, 0);
      if (i16 <= 0)
        break label305;
      bool4 = true;
      int i17 = i16 + accumField(i1, 1, bool4, 2);
      if (i17 <= 0)
        break label311;
      bool5 = true;
      int i18 = i17 + accumField(i2, 1, bool5, 2);
      if (i18 <= 0)
        break label317;
      bool6 = true;
      i19 = i18 + accumField(k, 1, bool6, 2);
      if (i19 <= 0)
        break label323;
    }
    for (int i20 = 3; ; i20 = 0)
    {
      for (int i21 = i19 + (1 + accumField(j, 2, true, i20)); ; ++i21)
      {
        if (i21 >= paramInt)
          break label329;
        arrayOfChar[i3] = ' ';
        ++i3;
      }
      i = 45;
      paramLong = -paramLong;
      break label63:
      label305: bool4 = false;
      break label192:
      label311: bool5 = false;
      break label214:
      label317: bool6 = false;
      label323: break label236:
    }
    label329: arrayOfChar[i3] = i;
    int i4 = i3 + 1;
    int i5;
    label348: boolean bool1;
    label372: int i7;
    label380: boolean bool2;
    label406: int i9;
    label414: boolean bool3;
    label440: int i11;
    label448: int i12;
    if (paramInt != 0)
    {
      i5 = 1;
      int i6 = printField(arrayOfChar, l, 'd', i4, false, 0);
      if (i6 == i4)
        break label515;
      bool1 = true;
      if (i5 == 0)
        break label521;
      i7 = 2;
      int i8 = printField(arrayOfChar, i1, 'h', i6, bool1, i7);
      if (i8 == i4)
        break label527;
      bool2 = true;
      if (i5 == 0)
        break label533;
      i9 = 2;
      int i10 = printField(arrayOfChar, i2, 'm', i8, bool2, i9);
      if (i10 == i4)
        break label539;
      bool3 = true;
      if (i5 == 0)
        break label545;
      i11 = 2;
      i12 = printField(arrayOfChar, k, 's', i10, bool3, i11);
      if ((i5 == 0) || (i12 == i4))
        break label551;
    }
    for (int i13 = 3; ; i13 = 0)
    {
      int i14 = printField(arrayOfChar, j, 'm', i12, true, i13);
      arrayOfChar[i14] = 's';
      i15 = i14 + 1;
      break label50:
      i5 = 0;
      break label348:
      label515: bool1 = false;
      break label372:
      label521: i7 = 0;
      break label380:
      label527: bool2 = false;
      break label406:
      label533: i9 = 0;
      break label414:
      label539: bool3 = false;
      break label440:
      label545: i11 = 0;
      label551: break label448:
    }
  }

  private static int printField(char[] paramArrayOfChar, int paramInt1, char paramChar, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    if ((paramBoolean) || (paramInt1 > 0))
    {
      int i = paramInt2;
      if (((paramBoolean) && (paramInt3 >= 3)) || (paramInt1 > 99))
      {
        int l = paramInt1 / 100;
        paramArrayOfChar[paramInt2] = (char)(l + 48);
        ++paramInt2;
        paramInt1 -= l * 100;
      }
      if (((paramBoolean) && (paramInt3 >= 2)) || (paramInt1 > 9) || (i != paramInt2))
      {
        int j = paramInt1 / 10;
        paramArrayOfChar[paramInt2] = (char)(j + 48);
        ++paramInt2;
        paramInt1 -= j * 10;
      }
      paramArrayOfChar[paramInt2] = (char)(paramInt1 + 48);
      int k = paramInt2 + 1;
      paramArrayOfChar[k] = paramChar;
      paramInt2 = k + 1;
    }
    return paramInt2;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.util.TimeUtils
 * JD-Core Version:    0.5.4
 */