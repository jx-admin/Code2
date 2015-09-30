package com.anjoystudio.pandoradef;

import java.io.Serializable;

public class GameMenuIo
  implements Serializable
{
  public boolean is_mapDust_open = GameView.is_mapDust_open;
  public boolean is_mapDust_unlimit_open = GameView.is_mapDust_unlimit_open;
  public boolean is_mapForest_open = GameView.is_mapForest_open;
  public boolean is_mapForest_unlimit_open = GameView.is_mapForest_unlimit_open;
  public boolean is_mapHill_open = GameView.is_mapHill_open;
  public boolean is_mapHill_unlimit_open = GameView.is_mapHill_unlimit_open;
  public boolean is_mapICC_unlimit_open = GameView.is_mapICC_unlimit_open;
  public boolean is_playmusic = GameView.is_playmusic;
  public boolean is_shake = GameView.is_shake;
  public boolean will_music = GameView.will_music;

  public GameMenuIo(GameView paramGameView)
  {
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.GameMenuIo
 * JD-Core Version:    0.5.4
 */