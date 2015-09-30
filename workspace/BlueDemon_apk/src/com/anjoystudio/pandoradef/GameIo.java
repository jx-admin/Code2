package com.anjoystudio.pandoradef;

import java.io.Serializable;
import java.util.ArrayList;

public class GameIo
  implements Serializable
{
  public int choice_map = GameView.choice_map;
  public int difficulty = GameView.difficulty;
  public int fresh_time = GameView.fresh_time;
  public int heart = GameView.heart;
  public boolean is_faster = GameView.is_faster;
  public int kind = GameView.kind;
  public byte[][] map = GameView.map;
  public int mode = GameView.mode;
  public int money = GameView.money;
  public boolean next_wave = GameView.next_wave;
  public int[][][] npc_actionFile;
  public int[] npc_attack_kind = new int[20];
  public int[] npc_blood = new int[20];
  public int[] npc_col = new int[20];
  public int[] npc_dest_col = new int[20];
  public int[] npc_dest_row = new int[20];
  public int[] npc_die_time_count = new int[20];
  public int[] npc_dir = new int[20];
  public int[] npc_dir_last = new int[20];
  public int[] npc_in_direction = new int[20];
  public int[] npc_kind = new int[20];
  public int[] npc_kind2 = new int[20];
  public int[] npc_next_col = new int[20];
  public int[] npc_next_dir = new int[20];
  public int[] npc_next_row = new int[20];
  public int[] npc_off_blood = new int[20];
  public int[] npc_out_direction = new int[20];
  public int[][][] npc_path;
  public int[] npc_row = new int[20];
  public int[] npc_show_blood_time = new int[20];
  public int[] npc_slow_down_time = new int[20];
  public int[] npc_speed = new int[20];
  public int[] npc_speed_down = new int[20];
  public int[] npc_state = new int[20];
  public int[] npc_time = new int[20];
  public int[] npc_x = new int[20];
  public int[] npc_y = new int[20];
  public int off_x = GameView.off_x;
  public int off_y = GameView.off_y;
  public int refresh_time = GameView.refresh_time;
  public boolean sale_to_regetpath;
  public int score = GameView.score;
  public int startX = GameView.startX;
  public int startY = GameView.startY;
  public int towerCount = GameView.towerCount;
  public boolean[] tower_canBuild;
  public int[] tower_col;
  public int[] tower_count;
  public int[] tower_currentDir;
  public int[] tower_currentFrame;
  public boolean[] tower_isChangeDir;
  public boolean[] tower_isPressTower;
  public int[] tower_row;
  public int[] tower_shotCon;
  public int tower_size;
  public int[] tower_stage;
  public int[] tower_targetDir;
  public int[] tower_targetFrame;
  public int[] tower_targetX;
  public int[] tower_targetY;
  public int[] tower_type;
  public int[] tower_x;
  public int[] tower_y;
  public int wave = GameView.wave;
  public boolean will_check_path;

  public GameIo(GameView paramGameView)
  {
    this.will_check_path = paramGameView.will_check_path;
    this.sale_to_regetpath = GameView.sale_to_regetpath;
    this.npc_path = new int[GameView.npc.length][][];
    this.npc_actionFile = new int[GameView.npc.length][][];
    int i = 0;
    if (i >= GameView.npc.length)
    {
      this.tower_size = GameView.towers.size();
      this.tower_x = new int[this.tower_size];
      this.tower_y = new int[this.tower_size];
      this.tower_row = new int[this.tower_size];
      this.tower_col = new int[this.tower_size];
      this.tower_type = new int[this.tower_size];
      this.tower_stage = new int[this.tower_size];
      this.tower_currentDir = new int[this.tower_size];
      this.tower_targetDir = new int[this.tower_size];
      this.tower_isPressTower = new boolean[this.tower_size];
      this.tower_canBuild = new boolean[this.tower_size];
      this.tower_isChangeDir = new boolean[this.tower_size];
      this.tower_targetX = new int[this.tower_size];
      this.tower_targetY = new int[this.tower_size];
      this.tower_shotCon = new int[this.tower_size];
      this.tower_count = new int[this.tower_size];
      this.tower_currentFrame = new int[this.tower_size];
      this.tower_targetFrame = new int[this.tower_size];
    }
    int k;
    for (int j = 0; ; ++j)
    {
      if (j >= this.tower_size)
      {
        k = 0;
        label570: if (k < GameView.towers.size())
          break;
        return;
        this.npc_path[i] = GameView.npc[i].path;
        this.npc_actionFile[i] = GameView.npc[i].actionFile;
        this.npc_x[i] = GameView.npc[i].x;
        this.npc_y[i] = GameView.npc[i].y;
        this.npc_row[i] = GameView.npc[i].row;
        this.npc_col[i] = GameView.npc[i].col;
        this.npc_next_row[i] = GameView.npc[i].next_row;
        this.npc_next_col[i] = GameView.npc[i].next_col;
        this.npc_state[i] = GameView.npc[i].state;
        this.npc_next_dir[i] = GameView.npc[i].next_dir;
        this.npc_dir[i] = GameView.npc[i].dir;
        this.npc_time[i] = GameView.npc[i].time;
        this.npc_dest_row[i] = GameView.npc[i].dest_row;
        this.npc_dest_col[i] = GameView.npc[i].dest_col;
        this.npc_dir_last[i] = GameView.npc[i].dir_last;
        this.npc_blood[i] = GameView.npc[i].blood;
        this.npc_off_blood[i] = GameView.npc[i].off_blood;
        this.npc_speed_down[i] = GameView.npc[i].speed_down;
        this.npc_speed[i] = GameView.npc[i].speed;
        this.npc_show_blood_time[i] = GameView.npc[i].show_blood_time;
        this.npc_slow_down_time[i] = GameView.npc[i].slow_down_time;
        this.npc_attack_kind[i] = GameView.npc[i].attack_kind;
        this.npc_die_time_count[i] = GameView.npc[i].die_time_count;
        this.npc_kind[i] = GameView.npc[i].kind;
        this.npc_kind2[i] = GameView.npc[i].kind2;
        this.npc_in_direction[i] = GameView.npc[i].in_direction;
        this.npc_out_direction[i] = GameView.npc[i].out_direction;
        ++i;
      }
      this.tower_x[j] = -10000;
    }
    Tower localTower = (Tower)GameView.towers.get(k);
    if (localTower == null);
    while (true)
    {
      ++k;
      break label570:
      for (int l = 0; ; ++l)
      {
        if (l < this.tower_size);
        if (this.tower_x[l] == -10000)
          break;
      }
      this.tower_x[l] = localTower.x;
      this.tower_y[l] = localTower.y;
      this.tower_row[l] = localTower.row;
      this.tower_col[l] = localTower.col;
      this.tower_type[l] = localTower.type;
      this.tower_stage[l] = localTower.stage;
      this.tower_currentDir[l] = localTower.currentDir;
      this.tower_targetDir[l] = localTower.targetDir;
      this.tower_isPressTower[l] = localTower.isPressTower;
      this.tower_canBuild[l] = localTower.canBuild;
      this.tower_isChangeDir[l] = localTower.isChangeDir;
      this.tower_targetX[l] = localTower.targetX;
      this.tower_targetY[l] = localTower.targetY;
      this.tower_shotCon[l] = localTower.shotCon;
      this.tower_count[l] = localTower.count;
      this.tower_currentFrame[l] = localTower.currentFrame;
      this.tower_targetFrame[l] = localTower.targetFrame;
    }
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.GameIo
 * JD-Core Version:    0.5.4
 */