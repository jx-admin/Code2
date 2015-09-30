package com.anjoystudio.pandoradef;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Vibrator;
import android.util.Log;
import java.io.Serializable;
import java.util.Random;

public class Npc extends Sprite
  implements Serializable
{
  public static final int ATTACKED_BOOM = 2;
  public static final int ATTACKED_CEMENT = 5;
  public static final int ATTACKED_FIRED = 4;
  public static final int ATTACKED_FLOOD = 3;
  public static final int ATTACKED_GUN = 0;
  public static final int ATTACKED_SLOWDOWN = 1;
  public static final transient int DOWN = 0;
  public static final transient int LEFT = 3;
  public static final transient int RIGHT = 1;
  public static final transient int STATE_DIED = 3;
  public static final transient int STATE_FALLDOWN = 2;
  public static final transient int STATE_MOVE = 1;
  public static final transient int STATE_UNREFRESHED = 0;
  public static final transient int UP = 2;
  public static final int[][] speed_init;
  public transient Action act_npc;
  public int[][] actionFile;
  public transient AStar astar;
  public int attack_kind = 100;
  public int blood;
  public transient Bitmap[][] bm_npc_green;
  public transient Bitmap[][] bm_npc_normal;
  public transient Bitmap[][] bm_npc_red;
  public int col;
  public int dest_col;
  public int dest_row;
  public int die_time_count = 0;
  public int dir;
  public int dir_last;
  public int in_direction;
  public int kind;
  public int kind2;
  public int next_col;
  public int next_dir;
  public int next_row;
  public int off_blood;
  public int out_direction;
  transient RectF oval;
  public int[][] path;
  public transient Resources res;
  public int row;
  public int show_blood_time = -1;
  public int slow_down_time = -1;
  public int speed;
  public int speed_down;
  public int state;
  public int time;
  public int x = -1000;
  public int y = -1000;

  static
  {
    int[][] arrayOfInt = new int[3][];
    int[] arrayOfInt1 = new int[6];
    arrayOfInt1[0] = 3;
    arrayOfInt1[1] = 2;
    arrayOfInt1[2] = 3;
    arrayOfInt1[3] = 3;
    arrayOfInt1[4] = 2;
    arrayOfInt1[5] = 3;
    arrayOfInt[0] = arrayOfInt1;
    int[] arrayOfInt2 = new int[6];
    arrayOfInt2[0] = 4;
    arrayOfInt2[1] = 2;
    arrayOfInt2[2] = 5;
    arrayOfInt2[3] = 4;
    arrayOfInt2[4] = 2;
    arrayOfInt2[5] = 4;
    arrayOfInt[1] = arrayOfInt2;
    int[] arrayOfInt3 = new int[6];
    arrayOfInt3[0] = 5;
    arrayOfInt3[1] = 3;
    arrayOfInt3[2] = 6;
    arrayOfInt3[3] = 5;
    arrayOfInt3[4] = 3;
    arrayOfInt3[5] = 5;
    arrayOfInt[2] = arrayOfInt3;
    speed_init = arrayOfInt;
  }

  public Npc(Resources paramResources, Bitmap[][] paramArrayOfBitmap1, Bitmap[][] paramArrayOfBitmap2, Bitmap[][] paramArrayOfBitmap3, int[][] paramArrayOfInt)
  {
    this.res = paramResources;
    this.bm_npc_normal = paramArrayOfBitmap1;
    this.bm_npc_red = paramArrayOfBitmap2;
    this.bm_npc_green = paramArrayOfBitmap3;
    this.actionFile = paramArrayOfInt;
    this.act_npc = new Action(paramResources, paramArrayOfBitmap1[0][0], this.actionFile[0][0]);
    this.astar = new AStar();
    this.oval = new RectF();
  }

  public void attacked(int paramInt1, int paramInt2)
  {
    this.attack_kind = paramInt1;
    switch (paramInt1)
    {
    case 0:
    case 2:
    case 3:
    default:
    case 1:
    case 4:
    }
    while (true)
    {
      this.blood -= Data.POWER[paramInt1][paramInt2] + (paramInt1 + 4) * GameView.difficulty + (paramInt2 + 1) * GameView.difficulty;
      this.show_blood_time = 0;
      return;
      if (this.speed_down == 0)
        this.speed_down += Data.SLOW_DOWN[paramInt2];
      if (this.speed_down > this.speed - 1)
        this.speed_down = (this.speed - 1);
      this.act_npc.setBitmap(this.bm_npc_green[this.kind][this.kind2]);
      this.slow_down_time = 0;
      continue;
      this.act_npc.setBitmap(this.bm_npc_red[this.kind][this.kind2]);
    }
  }

  public int getCenterX()
  {
    return 84 + this.x + GameView.off_x;
  }

  public int getCenterY()
  {
    return 72 + this.y + GameView.off_y - 30;
  }

  public int getNowPath()
  {
    for (int i = 0; ; ++i)
    {
      if (i >= this.path.length);
      for (int j = 0; ; j = i)
      {
        return j;
        if ((this.path[i][0] != this.row) || (this.path[i][1] != this.col))
          break;
      }
    }
  }

  public int getX()
  {
    return 72 + this.x;
  }

  public int getY()
  {
    return 72 + this.y;
  }

  public int get_col(int paramInt)
  {
    if (paramInt < 0);
    for (int i = -1; ; i = this.col)
    {
      while (true)
      {
        return i;
        if (paramInt <= 48 * GameView.map[0].length)
          break;
        i = 48 * (1 + GameView.map[0].length);
      }
      this.col = (paramInt / 48);
    }
  }

  public int[][] get_path()
  {
    Log.v("[@lcnb]", "get_path");
    Object localObject;
    if ((this.col == this.dest_col) && (this.row == this.dest_row))
    {
      int[][] arrayOfInt = new int[2][];
      arrayOfInt[0] = new int[2];
      arrayOfInt[1] = new int[2];
      localObject = arrayOfInt;
      label54: return localObject;
    }
    int i;
    if (this.row < 0)
    {
      i = 0;
      label66: if (this.col >= 0)
        break label168;
    }
    for (int j = 0; ; j = this.col)
      while (true)
      {
        this.astar.setMap(GameView.map, GameView.map[0].length, GameView.map.length);
        this.path = this.astar.getPath(j, i, this.dest_col, this.dest_row);
        if (this.path == null)
          this.state = 3;
        localObject = this.path;
        break label54:
        if (this.row > GameView.map.length - 1)
          i = GameView.map.length - 1;
        i = this.row;
        break label66:
        label168: if (this.col <= GameView.map[0].length - 1)
          break;
        j = GameView.map[0].length - 1;
      }
  }

  public int get_row(int paramInt)
  {
    if (paramInt < 0);
    for (int i = -1; ; i = this.row)
    {
      while (true)
      {
        return i;
        if (paramInt <= 48 * GameView.map.length)
          break;
        i = 48 * GameView.map.length;
      }
      this.row = (paramInt / 48);
    }
  }

  public void init(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    this.out_direction = paramInt7;
    this.in_direction = paramInt6;
    if (paramInt6 == 0)
    {
      this.row = paramInt1;
      this.col = (paramInt2 - 3);
      label29: this.dest_col = paramInt4;
      this.dest_row = paramInt3;
      this.kind = paramInt5;
      this.show_blood_time = -1;
      this.slow_down_time = -1;
      int i = Data.NPC_BASE_BLOOD[Data.NPC_ORDER[paramInt8]] + GameView.difficulty * Data.NPC_BASE_BLOOD[Data.NPC_ORDER[paramInt8]] + (paramInt8 * paramInt8 << 1);
      this.off_blood = i;
      this.blood = i;
      this.path = null;
      this.x = (24 + 48 * this.col);
      this.y = (24 + 48 * this.row);
      switch (paramInt5)
      {
      default:
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      }
    }
    while (true)
    {
      label180: this.speed = speed_init[GameView.difficulty][paramInt5];
      this.act_npc.setActionFile(this.actionFile[paramInt5][this.kind2]);
      this.act_npc.setBitmap(this.bm_npc_normal[paramInt5][this.kind2]);
      move();
      this.act_npc.setAlpha(255);
      this.act_npc.setAction(this.next_dir);
      this.state = 1;
      this.speed_down = 0;
      try
      {
        this.astar.setMap(GameView.map, GameView.map[0].length, GameView.map.length);
        label287: return;
        this.row = (paramInt1 - 3);
        this.col = paramInt2;
        break label29:
        this.kind2 = (GameView.random.nextInt() % 2);
        this.kind2 = Math.abs(this.kind2);
        break label180:
        this.kind2 = 0;
        break label180:
        this.kind2 = (GameView.random.nextInt() % 2);
        this.kind2 = Math.abs(this.kind2);
        break label180:
        this.kind2 = (GameView.random.nextInt() % 2);
        this.kind2 = Math.abs(this.kind2);
        break label180:
        this.kind2 = 0;
        break label180:
        this.kind2 = 0;
      }
      catch (Exception localException)
      {
        break label287:
      }
    }
  }

  public void logic()
  {
    if ((this.state == 0) || (this.state == 3));
    do
    {
      label15: return;
      this.time = (1 + this.time);
      this.time %= 210;
      if (this.state != 2)
        break label278;
      if ((this.time % 3 == 0) && (!this.act_npc.isActionEnd()))
        this.act_npc.nextFrame();
      if (this.state == 2)
      {
        label72: this.die_time_count = (1 + this.die_time_count);
        if (this.die_time_count >= 50)
        {
          this.state = 3;
          this.die_time_count = 0;
        }
      }
      if ((this.row == this.dest_row) && (this.col == this.dest_col))
      {
        this.next_dir = 1;
        this.dir = 1;
      }
      if ((this.x <= 48 * (3 + GameView.map[0].length)) && (this.y <= 48 * (3 + GameView.map.length)) && (this.blood > 0))
        break label338;
    }
    while ((this.state == 2) || (this.state == 3));
    if (this.blood <= 0)
    {
      GameView.money += Data.money_count[this.kind] + (2 - GameView.difficulty);
      GameView.score += ((1 + Data.money_count[this.kind]) * GameView.wave << 1);
    }
    while (true)
    {
      this.state = 2;
      this.die_time_count = 0;
      this.act_npc.setAction(4 + this.act_npc.getAction());
      break label15:
      label278: if (this.kind == 1);
      for (int i = 4; ; i = 2)
      {
        if (this.time % i == 0);
        this.act_npc.nextFrame();
        break label72:
      }
      GameView.heart -= 1;
      if (!GameView.is_shake)
        continue;
      GameView.vibrator.vibrate(20L);
    }
    if (this.show_blood_time > 50)
    {
      label338: this.show_blood_time = -1;
      label353: if (this.slow_down_time <= 50)
        break label490;
      this.slow_down_time = -1;
      this.speed_down = 0;
      this.act_npc.setBitmap(this.bm_npc_normal[this.kind][this.kind2]);
    }
    while (true)
    {
      this.act_npc.setPosition(this.x, this.y);
      this.row = get_row(this.y);
      this.col = get_col(this.x);
      move();
      if (this.dir != this.dir_last);
      this.act_npc.setAction(this.dir);
      this.dir_last = this.dir;
      break label15:
      if (this.show_blood_time >= 0);
      this.show_blood_time = (1 + this.show_blood_time);
      break label353:
      label490: this.slow_down_time = (1 + this.slow_down_time);
    }
  }

  public void move()
  {
    if ((this.row == this.dest_row) && (this.col == this.dest_col))
      if (this.out_direction == 0)
      {
        this.next_dir = 1;
        this.dir = 1;
        if ((this.x % 48 < 28) && (this.x % 48 > 20) && (this.y % 48 < 28) && (this.y % 48 > 20))
          label39: this.dir = this.next_dir;
        if (this.dir != 2)
          break label533;
        this.y -= this.speed - this.speed_down;
      }
    while (true)
    {
      return;
      this.next_dir = 0;
      this.dir = 0;
      break label39:
      if ((this.kind == 3) || (this.kind == 5))
      {
        if (this.out_direction == 0)
        {
          this.next_dir = 1;
          this.dir = 1;
        }
        this.next_dir = 0;
        this.dir = 0;
      }
      if ((this.row >= 0) && (this.row <= GameView.map.length - 1) && (this.col >= 0) && (this.col <= GameView.map[0].length - 1))
      {
        if ((this.path == null) || (this.path.equals(GameView.NPC_NULL_PATH)))
          get_path();
        int i = 0;
        for (int j = 0; ; ++j)
        {
          if (j >= this.path.length);
          while (true)
          {
            if (i >= this.path.length);
            this.path = get_path();
            break label39:
            if ((this.path[j][0] != this.row) || (this.path[j][1] != this.col))
              break;
            if (this.path[(j - 1)][0] > this.path[j][0])
              this.next_dir = 0;
            if (this.path[(j - 1)][0] < this.path[j][0])
              this.next_dir = 2;
            if (this.path[(j - 1)][1] > this.path[j][1])
              this.next_dir = 1;
            if (this.path[(j - 1)][1] >= this.path[j][1])
              continue;
            this.next_dir = 3;
          }
          ++i;
        }
      }
      if (this.row < 0)
      {
        this.next_dir = 0;
        this.dir = 0;
      }
      if (this.row > GameView.map.length - 1)
      {
        this.next_dir = 0;
        this.dir = 0;
      }
      if (this.col < 0)
      {
        this.next_dir = 1;
        this.dir = 1;
      }
      if (this.col > GameView.map[0].length - 1);
      this.next_dir = 1;
      this.dir = 1;
      break label39:
      if (this.dir == 0)
        label533: this.y += this.speed - this.speed_down;
      if (this.dir == 1)
        this.x += this.speed - this.speed_down;
      if (this.dir != 3)
        continue;
      this.x -= this.speed - this.speed_down;
    }
  }

  public void paint(Canvas paramCanvas, Paint paramPaint)
  {
    paramPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    if (this.state == 1)
    {
      paramPaint.setColor(0);
      paramPaint.setAlpha(127);
      this.oval.left = (84 + (this.x + GameView.off_x) - 14);
      this.oval.top = (72 + (this.y + GameView.off_y) - 6);
      this.oval.right = (14 + (84 + (this.x + GameView.off_x)));
      this.oval.bottom = (6 + (72 + (this.y + GameView.off_y)));
      paramCanvas.drawOval(this.oval, paramPaint);
      paramPaint.setAlpha(255);
      this.act_npc.draw(paramCanvas, paramPaint, 84 + (this.x + GameView.off_x), 72 + (this.y + GameView.off_y));
      if ((this.show_blood_time >= 0) && (this.blood > 0))
      {
        paramPaint.setStyle(Paint.Style.FILL);
        paramPaint.setColor(-65536);
        paramCanvas.drawRect(84 + (this.x - 15 + GameView.off_x), 72 + (this.y - 65 + GameView.off_y), 84 + (15 + this.x + GameView.off_x), 72 + (this.y - 60 + GameView.off_y), paramPaint);
        int i = 30 * (10000 * this.blood / this.off_blood) / 10000;
        paramPaint.setColor(-16711936);
        paramCanvas.drawRect(84 + (this.x - 15 + GameView.off_x), 72 + (this.y - 65 + GameView.off_y), 84 + (i + (this.x - 15) + GameView.off_x), 72 + (this.y - 60 + GameView.off_y), paramPaint);
      }
    }
    do
      label349: return;
    while (this.state != 2);
    paramPaint.setColor(0);
    if (this.act_npc.getAlpha() > 95)
    {
      paramPaint.setAlpha(this.act_npc.getAlpha() - 95);
      label389: this.oval.left = (84 + (this.x + GameView.off_x) - 14);
      this.oval.top = (72 + (this.y + GameView.off_y) - 6);
      this.oval.right = (14 + (84 + (this.x + GameView.off_x)));
      this.oval.bottom = (6 + (72 + (this.y + GameView.off_y)));
      if ((this.kind != 3) && (this.kind != 5))
        paramCanvas.drawOval(this.oval, paramPaint);
      if (this.act_npc.getAlpha() <= 0)
        break label577;
      this.act_npc.setAlpha(this.act_npc.getAlpha() - 5);
    }
    while (true)
    {
      this.act_npc.draw(paramCanvas, paramPaint, 84 + (this.x + GameView.off_x), 72 + (this.y + GameView.off_y));
      paramPaint.setAlpha(255);
      break label349:
      paramPaint.setAlpha(0);
      break label389:
      label577: this.act_npc.setAlpha(0);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.Npc
 * JD-Core Version:    0.5.4
 */