package com.anjoystudio.pandoradef;

import java.io.Serializable;

public class HightScoreMemory
  implements Serializable
{
  public static final int MAP_DUST = 2;
  public static final int MAP_FOREST = 1;
  public static final int MAP_HILL = 3;
  public static final int MAP_ICE = 0;
  public static final int MODE_TRADITIONAL = 0;
  public static final int MODE_UNLIMIT = 3;
  public static final int MODE_UNTRADITIONAL = 2;
  public static final HightScoreMemory[] SYSTEM_HIGH_SCORE;
  public int map;
  public int score;
  public int wave;

  static
  {
    HightScoreMemory[] arrayOfHightScoreMemory = new HightScoreMemory[24];
    arrayOfHightScoreMemory[0] = new HightScoreMemory(0, 0, 0);
    arrayOfHightScoreMemory[1] = new HightScoreMemory(0, 0, 0);
    arrayOfHightScoreMemory[2] = new HightScoreMemory(0, 0, 0);
    arrayOfHightScoreMemory[3] = new HightScoreMemory(0, 0, 0);
    arrayOfHightScoreMemory[4] = new HightScoreMemory(0, 0, 0);
    arrayOfHightScoreMemory[5] = new HightScoreMemory(0, 0, 0);
    arrayOfHightScoreMemory[6] = new HightScoreMemory(1, 0, 0);
    arrayOfHightScoreMemory[7] = new HightScoreMemory(1, 0, 0);
    arrayOfHightScoreMemory[8] = new HightScoreMemory(1, 0, 0);
    arrayOfHightScoreMemory[9] = new HightScoreMemory(1, 0, 0);
    arrayOfHightScoreMemory[10] = new HightScoreMemory(1, 0, 0);
    arrayOfHightScoreMemory[11] = new HightScoreMemory(1, 0, 0);
    arrayOfHightScoreMemory[12] = new HightScoreMemory(2, 0, 0);
    arrayOfHightScoreMemory[13] = new HightScoreMemory(2, 0, 0);
    arrayOfHightScoreMemory[14] = new HightScoreMemory(2, 0, 0);
    arrayOfHightScoreMemory[15] = new HightScoreMemory(2, 0, 0);
    arrayOfHightScoreMemory[16] = new HightScoreMemory(2, 0, 0);
    arrayOfHightScoreMemory[17] = new HightScoreMemory(2, 0, 0);
    arrayOfHightScoreMemory[18] = new HightScoreMemory(3, 0, 0);
    arrayOfHightScoreMemory[19] = new HightScoreMemory(3, 0, 0);
    arrayOfHightScoreMemory[20] = new HightScoreMemory(3, 0, 0);
    arrayOfHightScoreMemory[21] = new HightScoreMemory(3, 0, 0);
    arrayOfHightScoreMemory[22] = new HightScoreMemory(3, 0, 0);
    arrayOfHightScoreMemory[23] = new HightScoreMemory(3, 0, 0);
    SYSTEM_HIGH_SCORE = arrayOfHightScoreMemory;
  }

  public HightScoreMemory()
  {
  }

  public HightScoreMemory(int paramInt1, int paramInt2, int paramInt3)
  {
    this.map = paramInt1;
    this.score = paramInt2;
    this.wave = paramInt3;
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.HightScoreMemory
 * JD-Core Version:    0.5.4
 */