package com.anjoystudio.pandoradef;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.commons.codec.binary.Base64;

public class Tool
  implements Serializable
{
  public Context con;

  public Tool(Context paramContext)
  {
    this.con = paramContext;
  }

  public static HightScoreMemory[] Sort(HightScoreMemory[] paramArrayOfHightScoreMemory, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = paramInt2;
    int k = paramArrayOfHightScoreMemory[((paramInt1 + paramInt2) / 2)].score;
    if ((paramArrayOfHightScoreMemory[i].score >= k) || (i >= paramInt2));
    while (true)
    {
      if ((paramArrayOfHightScoreMemory[j].score <= k) || (j <= paramInt1))
      {
        if (i <= j)
        {
          HightScoreMemory localHightScoreMemory = paramArrayOfHightScoreMemory[i];
          paramArrayOfHightScoreMemory[i] = paramArrayOfHightScoreMemory[j];
          paramArrayOfHightScoreMemory[j] = localHightScoreMemory;
          ++i;
        }
        if (i >= --j);
        if (paramInt1 < j)
          Sort(paramArrayOfHightScoreMemory, paramInt1, j);
        if (paramInt2 > i)
          Sort(paramArrayOfHightScoreMemory, i, paramInt2);
        return paramArrayOfHightScoreMemory;
        ++i;
      }
      --j;
    }
  }

  public static Sprite[] Sort(Sprite[] paramArrayOfSprite, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = paramInt2;
    int k = paramArrayOfSprite[((paramInt1 + paramInt2) / 2)].getCenterY();
    if ((paramArrayOfSprite[i].getCenterY() >= k) || (i >= paramInt2));
    while (true)
    {
      if ((paramArrayOfSprite[j].getCenterY() <= k) || (j <= paramInt1))
      {
        if (i <= j)
        {
          Sprite localSprite = paramArrayOfSprite[i];
          paramArrayOfSprite[i] = paramArrayOfSprite[j];
          paramArrayOfSprite[j] = localSprite;
          ++i;
        }
        if (i >= --j);
        if (paramInt1 < j)
          Sort(paramArrayOfSprite, paramInt1, j);
        if (paramInt2 > i)
          Sort(paramArrayOfSprite, i, paramInt2);
        return paramArrayOfSprite;
        ++i;
      }
      --j;
    }
  }

  public static void buildTower(int paramInt1, int paramInt2, int paramInt3)
  {
    GameView.map[paramInt1][paramInt2] = (byte)(paramInt3 + 1);
  }

  public static void drawImageNumber(Canvas paramCanvas, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, Paint paramPaint)
  {
    int i = 1;
    int j = 1;
    int k = paramBitmap.getWidth() / 10;
    int l = paramBitmap.getHeight();
    while (true)
    {
      i *= 10;
      if (paramInt1 < i)
        break;
      ++j;
    }
    setClip(paramCanvas, paramInt2 + k * (j - 1), paramInt3, k, l);
    paramCanvas.drawBitmap(paramBitmap, paramInt2 + k * (j - 1) - k * (paramInt1 % 10), paramInt3, paramPaint);
    paramCanvas.restore();
    int i1 = 10;
    for (int i2 = 1; ; ++i2)
    {
      if (i2 >= j)
        return;
      i1 *= 10;
      setClip(paramCanvas, paramInt2 + k * (j - (i2 + 1)), paramInt3, k, l);
      paramCanvas.drawBitmap(paramBitmap, paramInt2 + k * (j - (i2 + 1)) - k * (paramInt1 % i1 / (i1 / 10)), paramInt3, paramPaint);
      paramCanvas.restore();
    }
  }

  public static void draw_bitmap(Canvas paramCanvas, Paint paramPaint, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3)
  {
    switch (paramInt3)
    {
    default:
    case 0:
    case 3:
    case 33:
    case 40:
    }
    while (true)
    {
      try
      {
        throw new IllegalAccessException();
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        return;
      }
      paramCanvas.drawBitmap(paramBitmap, paramInt1, paramInt2, paramPaint);
      continue;
      paramCanvas.drawBitmap(paramBitmap, paramInt1 - (paramBitmap.getWidth() >> 1), paramInt2 - (paramBitmap.getHeight() >> 1), paramPaint);
      continue;
      paramCanvas.drawBitmap(paramBitmap, paramInt1 - (paramBitmap.getWidth() >> 1), paramInt2 - paramBitmap.getHeight(), paramPaint);
      continue;
      paramCanvas.drawBitmap(paramBitmap, paramInt1 - paramBitmap.getWidth(), paramInt2 - paramBitmap.getHeight(), paramPaint);
    }
  }

  public static void draw_bitmap_clip(Canvas paramCanvas, Paint paramPaint, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    paramCanvas.save();
    set_clip(paramInt1, paramInt2, paramInt5, paramInt6, paramCanvas);
    draw_bitmap(paramCanvas, paramPaint, paramBitmap, paramInt1 - paramInt3, paramInt2 - paramInt4, paramInt7);
    paramCanvas.restore();
  }

  public static int getImageNumberWidth(int paramInt, Bitmap paramBitmap)
  {
    int i = paramBitmap.getWidth() / 10;
    int j = 1;
    for (int k = 1; ; ++k)
    {
      j *= 10;
      if (paramInt < j)
        break;
    }
    return k * i;
  }

  public static int get_x_in_other_screen(int paramInt)
  {
    return (int)(paramInt * GameView.scr_w / 480);
  }

  public static int get_y_in_other_screen(int paramInt)
  {
    return (int)(paramInt * GameView.scr_h / 320);
  }

  public static Bitmap initBitmapRedAndGreen(Bitmap paramBitmap, int paramInt, Canvas paramCanvas, Paint paramPaint)
  {
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_4444);
    paramCanvas.setBitmap(localBitmap);
    paramPaint.setColor(paramInt);
    paramCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, paramPaint);
    paramPaint.setAlpha(95);
    paramCanvas.drawBitmap(paramBitmap.extractAlpha(), 0.0F, 0.0F, paramPaint);
    paramPaint.reset();
    return localBitmap;
  }

  public static boolean is_collide_point_cercle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = Math.abs(paramInt3 - paramInt1);
    int j = Math.abs(paramInt4 - paramInt2);
    if (i * i + j * j <= paramInt5 * paramInt5);
    for (int k = 1; ; k = 0)
      return k;
  }

  public static boolean is_collide_rect_rect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    if ((paramInt1 + paramInt3 < paramInt5) || (paramInt5 + paramInt7 < paramInt1) || (paramInt2 + paramInt4 < paramInt6) || (paramInt6 + paramInt8 < paramInt2));
    for (int i = 0; ; i = 1)
      return i;
  }

  public static boolean isin(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    int i = 0;
    if ((paramInt1 >= paramInt3) && (paramInt1 <= paramInt3 + paramInt5) && (paramInt2 >= paramInt4) && (paramInt2 <= paramInt4 + paramInt6))
      i = 1;
    return i;
  }

  public static boolean prsButton(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    int i = paramInt3 + (GameView.scr_w - 480 >> 1);
    int j = paramInt5 + (GameView.scr_w - 480 >> 1);
    int k = paramInt4 + (GameView.scr_h - 320 >> 1);
    int l = paramInt6 + (GameView.scr_h - 320 >> 1);
    if ((paramInt1 < i) || (paramInt1 > j) || (paramInt2 < k) || (paramInt2 > l));
    for (int i1 = 0; ; i1 = 1)
      return i1;
  }

  public static void saveData(String paramString1, String paramString2, Object paramObject)
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
      localObjectOutputStream.writeObject(paramObject);
      SharedPreferences localSharedPreferences = MainActivity.mainActivity.getSharedPreferences(paramString1, 0);
      String str = new String(Base64.encodeBase64(localByteArrayOutputStream.toByteArray()));
      SharedPreferences.Editor localEditor = localSharedPreferences.edit();
      localEditor.putString(paramString2, str);
      localEditor.commit();
      localObjectOutputStream.close();
      return;
    }
    catch (IOException localIOException)
    {
      Log.d("save err  ", localIOException.getMessage());
    }
  }

  public static void setClip(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramCanvas.save();
    paramCanvas.clipRect(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }

  public static void set_clip(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Canvas paramCanvas)
  {
    paramCanvas.clipRect(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }

  public MediaPlayer creatMediaPlayer(int paramInt)
  {
    if (this.con == null)
      this.con = MainActivity.mainActivity;
    return MediaPlayer.create(this.con, paramInt);
  }

  public Bitmap creat_bitmap(int paramInt)
  {
    return BitmapFactory.decodeResource(MainActivity.mainActivity.getResources(), paramInt);
  }

  public Bitmap creat_bitmap_single_color(Bitmap paramBitmap)
  {
    return paramBitmap.extractAlpha();
  }

  public Bitmap initBitmap(int paramInt1, int paramInt2, int paramInt3)
  {
    Canvas localCanvas = new Canvas();
    Bitmap localBitmap = Bitmap.createBitmap(paramInt2, paramInt3, Bitmap.Config.ARGB_8888);
    localCanvas.setBitmap(localBitmap);
    localCanvas.drawBitmap(creat_bitmap(paramInt1), null, new Rect(0, 0, paramInt2, paramInt3), new Paint());
    return localBitmap;
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.Tool
 * JD-Core Version:    0.5.4
 */