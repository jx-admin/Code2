package com.anjoystudio.pandoradef;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.io.Serializable;

public abstract class Sprite
  implements Serializable
{
  public int col;
  public int row;
  public int x;
  public int y;

  abstract int getCenterX();

  abstract int getCenterY();

  abstract void logic();

  protected void paint(Canvas paramCanvas, Paint paramPaint)
  {
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.Sprite
 * JD-Core Version:    0.5.4
 */