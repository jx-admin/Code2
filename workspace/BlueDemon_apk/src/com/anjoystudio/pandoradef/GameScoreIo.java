package com.anjoystudio.pandoradef;

import java.io.Serializable;

public class GameScoreIo
  implements Serializable
{
  public int[] map = new int[24];
  public int[] score = new int[24];
  public int[] wave = new int[24];

  public GameScoreIo(GameView paramGameView)
  {
    for (int i = 0; ; ++i)
    {
      if (i >= this.score.length)
        return;
      this.score[i] = paramGameView.hiscoMemory[i].score;
      this.wave[i] = paramGameView.hiscoMemory[i].wave;
      this.map[i] = paramGameView.hiscoMemory[i].map;
    }
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.GameScoreIo
 * JD-Core Version:    0.5.4
 */