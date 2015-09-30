package com.anjoystudio.pandoradef;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.media.MediaPlayer;
import java.io.Serializable;
import java.util.ArrayList;

public class Tower extends Sprite
  implements Serializable
{
  public static final int TOWER_DIR_0 = 9;
  public static final int TOWER_DIR_120 = 5;
  public static final int TOWER_DIR_150 = 4;
  public static final int TOWER_DIR_180 = 3;
  public static final int TOWER_DIR_210 = 2;
  public static final int TOWER_DIR_240 = 1;
  public static final int TOWER_DIR_270 = 0;
  public static final int TOWER_DIR_30 = 8;
  public static final int TOWER_DIR_300 = 11;
  public static final int TOWER_DIR_330 = 10;
  public static final int TOWER_DIR_60 = 7;
  public static final int TOWER_DIR_90 = 6;
  static final int T_BOMB = 5;
  static final int T_BOOM = 2;
  static final int T_FIRE = 4;
  static final int T_FLOOD = 3;
  static final int T_GUN = 0;
  static final int T_SLOWDOWN = 1;
  static final int[] buildFee;
  static final int[][] saleFee;
  static final int[] upDataFee;
  Action act;
  ArrayList<Npc> anpc;
  Bitmap bmpTower;
  Bullet bullet;
  boolean canBuild;
  int col;
  int count;
  int currentDir;
  int currentFrame;
  private double[] gunshot;
  boolean isChangeDir;
  boolean isPressTower;
  int p;
  int py;
  int row;
  int shotCon;
  int[] sleepTime;
  int stage;
  int targetDir;
  int targetFrame;
  Npc targetNpc;
  int targetX;
  int targetY;
  int type;
  int x;
  int y;

  static
  {
    int[] arrayOfInt1 = new int[6];
    arrayOfInt1[0] = 5;
    arrayOfInt1[1] = 10;
    arrayOfInt1[2] = 20;
    arrayOfInt1[3] = 50;
    arrayOfInt1[4] = 70;
    arrayOfInt1[5] = 120;
    buildFee = arrayOfInt1;
    int[] arrayOfInt2 = new int[6];
    arrayOfInt2[0] = 4;
    arrayOfInt2[1] = 5;
    arrayOfInt2[2] = 14;
    arrayOfInt2[3] = 40;
    arrayOfInt2[4] = 50;
    arrayOfInt2[5] = 90;
    upDataFee = arrayOfInt2;
    int[][] arrayOfInt = new int[6][];
    int[] arrayOfInt3 = new int[3];
    arrayOfInt3[0] = 3;
    arrayOfInt3[1] = 6;
    arrayOfInt3[2] = 9;
    arrayOfInt[0] = arrayOfInt3;
    int[] arrayOfInt4 = new int[3];
    arrayOfInt4[0] = 7;
    arrayOfInt4[1] = 11;
    arrayOfInt4[2] = 15;
    arrayOfInt[1] = arrayOfInt4;
    int[] arrayOfInt5 = new int[3];
    arrayOfInt5[0] = 15;
    arrayOfInt5[1] = 26;
    arrayOfInt5[2] = 37;
    arrayOfInt[2] = arrayOfInt5;
    int[] arrayOfInt6 = new int[3];
    arrayOfInt6[0] = 37;
    arrayOfInt6[1] = 67;
    arrayOfInt6[2] = 97;
    arrayOfInt[3] = arrayOfInt6;
    int[] arrayOfInt7 = new int[3];
    arrayOfInt7[0] = 52;
    arrayOfInt7[1] = 90;
    arrayOfInt7[2] = 127;
    arrayOfInt[4] = arrayOfInt7;
    int[] arrayOfInt8 = new int[3];
    arrayOfInt8[0] = 90;
    arrayOfInt8[1] = 157;
    arrayOfInt8[2] = 225;
    arrayOfInt[5] = arrayOfInt8;
    saleFee = arrayOfInt;
  }

  public Tower()
  {
    double[] arrayOfDouble = new double[6];
    arrayOfDouble[0] = 2.0D;
    arrayOfDouble[1] = 2.5D;
    arrayOfDouble[2] = 3.0D;
    arrayOfDouble[3] = 2.0D;
    arrayOfDouble[4] = 2.5D;
    arrayOfDouble[5] = 4.0D;
    this.gunshot = arrayOfDouble;
    int[] arrayOfInt = new int[6];
    arrayOfInt[0] = 3;
    arrayOfInt[1] = 20;
    arrayOfInt[2] = 30;
    arrayOfInt[3] = 20;
    arrayOfInt[5] = 40;
    this.sleepTime = arrayOfInt;
    this.p = 52360;
    this.py = 25000;
  }

  public Tower(Resources paramResources, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3)
  {
    double[] arrayOfDouble = new double[6];
    arrayOfDouble[0] = 2.0D;
    arrayOfDouble[1] = 2.5D;
    arrayOfDouble[2] = 3.0D;
    arrayOfDouble[3] = 2.0D;
    arrayOfDouble[4] = 2.5D;
    arrayOfDouble[5] = 4.0D;
    this.gunshot = arrayOfDouble;
    int[] arrayOfInt = new int[6];
    arrayOfInt[0] = 3;
    arrayOfInt[1] = 20;
    arrayOfInt[2] = 30;
    arrayOfInt[3] = 20;
    arrayOfInt[5] = 40;
    this.sleepTime = arrayOfInt;
    this.p = 52360;
    this.py = 25000;
    this.type = paramInt3;
    this.x = paramInt1;
    this.y = paramInt2;
    this.bmpTower = paramBitmap;
    this.anpc = new ArrayList();
    this.row = ((paramInt2 - 72) / 48 - 1);
    this.col = ((paramInt1 - 84) / 48);
    this.shotCon = this.sleepTime[paramInt3];
    switch (paramInt3)
    {
    default:
    case 0:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    }
    while (true)
    {
      return;
      this.isChangeDir = true;
      this.currentDir = 0;
      this.act = new Action(paramResources, paramBitmap, 2130968602);
      this.act.setPosition(paramInt1, paramInt2);
      this.act.setAction(this.currentDir);
      this.bullet = new Bullet(null, null, getCenterX(), getCenterY(), paramInt3, 0);
      continue;
      this.isChangeDir = true;
      this.currentDir = 0;
      this.act = new Action(paramResources, paramBitmap, 2130968604);
      this.act.setPosition(paramInt1, paramInt2);
      this.act.setAction(this.currentDir);
      this.bullet = new Bullet(paramResources, GameView.bmpBulletSlowdown, getX(), getY() - 10, paramInt3, this.currentDir);
      continue;
      this.isChangeDir = true;
      this.currentDir = 0;
      this.act = new Action(paramResources, paramBitmap, 2130968600);
      this.act.setPosition(paramInt1, paramInt2);
      this.act.setAction(this.currentDir);
      this.bullet = new Bullet(paramResources, GameView.bmpBulletBoom, getX(), getY(), paramInt3, this.currentDir);
      continue;
      this.act = new Action(paramResources, paramBitmap, 2130968603);
      this.act.setPosition(paramInt1, paramInt2);
      this.act.setAction(0);
      this.bullet = new Bullet(null, null, getX(), getY(), paramInt3, 0);
      continue;
      this.isChangeDir = true;
      this.currentDir = 0;
      this.act = new Action(paramResources, paramBitmap, 2130968601);
      this.act.setPosition(paramInt1, paramInt2);
      this.act.setAction(this.currentDir);
      this.bullet = new Bullet(paramResources, GameView.bmpBulletFire, getX(), getY(), paramInt3, this.currentDir);
      continue;
      this.isChangeDir = true;
      this.act = new Action(paramResources, paramBitmap, 2130968599);
      this.act.setPosition(paramInt1, paramInt2);
      this.act.setAction(0);
      this.currentFrame = 0;
      this.targetFrame = this.act.getMaxFrame();
      this.act.setFrame(this.currentFrame);
      this.bullet = new Bullet(paramResources, GameView.bmpBulletBomb, getCenterX(), getCenterY(), paramInt3, 0);
    }
  }

  public static void buildTower(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 < 0) || (paramInt1 >= GameView.map.length) || (paramInt2 < 0) || (paramInt2 >= GameView.map[paramInt1].length));
    while (true)
    {
      return;
      GameView.map[paramInt1][paramInt2] = (byte)(paramInt3 + 1);
      GameView.money -= buildFee[paramInt3];
    }
  }

  public static boolean checkUpdateOrSell(ArrayList<Tower> paramArrayList, int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = 0;
    if (j >= GameView.towers.size())
      label5: return i;
    if (paramArrayList.get(j) == null);
    while (true)
    {
      ++j;
      break label5:
      Tower localTower = (Tower)paramArrayList.get(j);
      if ((localTower.isPressTower()) || (localTower.getRow() != getNowRow(paramInt2)) || (localTower.getCol() != getNowCol(paramInt1)))
        continue;
      localTower.isPressTower = true;
      i = 1;
    }
  }

  public static int getNowCol(int paramInt)
  {
    return (paramInt - GameView.off_x - 84) / 48;
  }

  public static int getNowRow(int paramInt)
  {
    return (paramInt - GameView.off_y - 72) / 48;
  }

  public void checkBuild(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((paramInt1 - GameView.off_x < 84) || (paramInt1 - GameView.off_x > 996) || (paramInt2 - GameView.off_y < 72) || (paramInt2 - GameView.off_y > 696))
      setCanBuild(false);
    while (true)
    {
      return;
      if (((getNowRow(paramInt2) - 1 >= 0) && (getNowRow(paramInt2) - 1 < GameView.map.length) && (getNowCol(paramInt1) >= 0) && (getNowCol(paramInt1) < GameView.map[(getNowRow(paramInt2) - 1)].length) && (GameView.map[(getNowRow(paramInt2) - 1)][getNowCol(paramInt1)] != 0)) || (!paramBoolean))
        setCanBuild(false);
      setCanBuild(true);
    }
  }

  public void drawCircle(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, Paint paramPaint)
  {
    if (this.canBuild)
    {
      paramPaint.setColor(-16711936);
      label14: if (paramInt3 >= 5)
        break label124;
      paramPaint.setAlpha(100);
      paramCanvas.drawCircle(paramInt1, paramInt2 - 48, 48 * (int)this.gunshot[paramInt3], paramPaint);
      paramPaint.setAlpha(255);
      paramPaint.setStyle(Paint.Style.STROKE);
      paramPaint.setStrokeWidth(2.0F);
      paramCanvas.drawCircle(paramInt1, paramInt2 - 48, 48 * (int)this.gunshot[paramInt3], paramPaint);
    }
    while (true)
    {
      paramPaint.reset();
      paramPaint.setAlpha(255);
      return;
      paramPaint.setColor(-65536);
      break label14:
      label124: Path localPath = new Path();
      localPath.setFillType(Path.FillType.EVEN_ODD);
      localPath.addCircle(paramInt1, paramInt2 - 48, 48 * (int)this.gunshot[paramInt3], Path.Direction.CW);
      localPath.addCircle(paramInt1, paramInt2 - 48, 96.0F, Path.Direction.CW);
      paramPaint.setAlpha(100);
      paramCanvas.drawPath(localPath, paramPaint);
      paramPaint.setAlpha(255);
      paramPaint.setStyle(Paint.Style.STROKE);
      paramPaint.setStrokeWidth(2.0F);
      paramCanvas.drawCircle(paramInt1, paramInt2 - 48, 48 * (int)this.gunshot[paramInt3], paramPaint);
      paramCanvas.drawCircle(paramInt1, paramInt2 - 48, 96.0F, paramPaint);
    }
  }

  public void drawMoveTower(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, Paint paramPaint)
  {
    paramCanvas.save();
    paramCanvas.clipRect(paramInt1 - 24, paramInt2 - (GameView.bmpMoveTower.getHeight() >> 1) - 48, 48 + (paramInt1 - 24), paramInt2 - (GameView.bmpMoveTower.getHeight() >> 1) + GameView.bmpMoveTower.getHeight() - 48);
    paramCanvas.drawBitmap(GameView.bmpMoveTower, paramInt1 - 24 - paramInt3 * 48, paramInt2 - (GameView.bmpMoveTower.getHeight() >> 1) - 48, paramPaint);
    paramCanvas.restore();
  }

  public void drawUpdateCircle(Canvas paramCanvas, Paint paramPaint, Bitmap paramBitmap1, Bitmap paramBitmap2)
  {
    paramPaint.setColor(-16711936);
    if (this.type < 5)
    {
      paramPaint.setAlpha(100);
      paramCanvas.drawCircle(getCenterX(), getCenterY(), 48 * (int)this.gunshot[this.type], paramPaint);
      paramPaint.setAlpha(255);
      paramPaint.setStyle(Paint.Style.STROKE);
      paramPaint.setStrokeWidth(2.0F);
      paramCanvas.drawCircle(getCenterX(), getCenterY(), 48 * (int)this.gunshot[this.type], paramPaint);
      label97: paramCanvas.drawBitmap(paramBitmap1, getCenterX() - 35 - paramBitmap1.getWidth(), getCenterY() - paramBitmap1.getHeight() / 2, paramPaint);
      Tool.drawImageNumber(paramCanvas, GameView.bmpBuildFeeNum, saleFee[this.type][this.stage], 30 + (getCenterX() - 35 - paramBitmap1.getWidth()), 39 + (getCenterY() - paramBitmap1.getHeight() / 2), paramPaint);
      if ((GameView.money >= upDataFee[this.type]) && (this.stage < 2))
        break label506;
    }
    for (int i = paramBitmap2.getWidth() >> 1; ; i = 0)
    {
      paramCanvas.clipRect(35 + getCenterX(), getCenterY() - paramBitmap2.getHeight() / 2, 35 + getCenterX() + paramBitmap2.getWidth() / 2, getCenterY() - paramBitmap2.getHeight() / 2 + paramBitmap2.getHeight());
      paramCanvas.drawBitmap(paramBitmap2, 35 + getCenterX() - i, getCenterY() - paramBitmap2.getHeight() / 2, paramPaint);
      paramCanvas.restore();
      Tool.drawImageNumber(paramCanvas, GameView.bmpBuildFeeNum, upDataFee[this.type], 30 + (35 + getCenterX()), 39 + (getCenterY() - paramBitmap2.getHeight() / 2), paramPaint);
      paramPaint.reset();
      paramPaint.setAlpha(255);
      return;
      Path localPath = new Path();
      localPath.setFillType(Path.FillType.EVEN_ODD);
      localPath.addCircle(getCenterX(), getCenterY(), 48 * (int)this.gunshot[this.type], Path.Direction.CW);
      localPath.addCircle(getCenterX(), getCenterY(), 96.0F, Path.Direction.CW);
      paramPaint.setAlpha(100);
      paramCanvas.drawPath(localPath, paramPaint);
      paramPaint.setAlpha(255);
      paramPaint.setStyle(Paint.Style.STROKE);
      paramPaint.setStrokeWidth(2.0F);
      paramCanvas.drawCircle(getCenterX(), getCenterY(), 48 * (int)this.gunshot[this.type], paramPaint);
      paramCanvas.drawCircle(getCenterX(), getCenterY(), 96.0F, paramPaint);
      label506: break label97:
    }
  }

  public int getCenterX()
  {
    return 24 + (84 + 48 * getCol() + GameView.off_x);
  }

  public int getCenterY()
  {
    return 24 + (72 + 48 * getRow() + GameView.off_y);
  }

  public int getCol()
  {
    return this.col;
  }

  public int getRow()
  {
    return this.row;
  }

  public int getStage()
  {
    return this.stage;
  }

  public int getX()
  {
    return this.x;
  }

  public int getX(int paramInt)
  {
    return 24 + (84 + 48 * getNowCol(paramInt));
  }

  public int getY()
  {
    return this.y - 24;
  }

  public int getY(int paramInt)
  {
    return 72 + 48 * getNowRow(paramInt);
  }

  public boolean isCanBuild()
  {
    return this.canBuild;
  }

  public boolean isMoneyEnuogh(int paramInt)
  {
    int i = 0;
    if (GameView.money >= upDataFee[paramInt])
      i = 1;
    return i;
  }

  public boolean isPressTower()
  {
    return this.isPressTower;
  }

  public void logic()
  {
    switch (this.type)
    {
    default:
    case 1:
    case 2:
    }
    do
      while (true)
      {
        label28: return;
        if ((this.targetNpc == null) || (!this.bullet.isLive))
          continue;
        if (Tool.is_collide_rect_rect(this.bullet.x - 5, this.bullet.y - 5, 10, 10, this.targetX - 30, this.targetY - 40, 60, 50))
          this.bullet.attack = true;
        if (!this.bullet.isAttack)
          continue;
        this.targetNpc.attacked(this.type, this.stage);
        this.shotCon = 0;
      }
    while ((this.targetNpc == null) || (!this.bullet.isLive));
    if (!this.bullet.isAttack)
    {
      this.bullet.end_x = this.targetNpc.getX();
      this.bullet.end_y = this.targetNpc.getY();
    }
    while (true)
    {
      if (Tool.is_collide_rect_rect(this.bullet.x - 5, this.bullet.y - 10, 10, 10, this.targetNpc.getX() - 10, this.targetNpc.getY() - 30, 30, 20));
      this.bullet.attack = true;
      break label28:
      this.targetNpc.attacked(this.type, this.stage);
      this.shotCon = 0;
    }
  }

  public void logic(Npc[] paramArrayOfNpc)
  {
    this.count = (1 + this.count);
    int i = 0;
    if (i >= paramArrayOfNpc.length)
    {
      if (!this.anpc.isEmpty())
      {
        label12: towerLogic(((Npc)this.anpc.get(0)).getCenterX(), ((Npc)this.anpc.get(0)).getCenterY(), getCenterX(), getCenterY());
        label68: return;
      }
    }
    else
    {
      Npc localNpc = paramArrayOfNpc[i];
      int j = (int)(48.0D * this.gunshot[this.type]);
      if (localNpc.state == 1)
        if (Tool.is_collide_point_cercle(localNpc.getCenterX(), localNpc.getCenterY(), getCenterX(), getCenterY(), j))
        {
          if (!this.anpc.contains(localNpc))
            this.anpc.add(localNpc);
          if ((this.type == 5) && (Tool.is_collide_point_cercle(localNpc.getCenterX(), localNpc.getCenterY(), getCenterX(), getCenterY(), 96)) && (!this.anpc.isEmpty()))
            this.anpc.remove(localNpc);
        }
      while (true)
      {
        ++i;
        break label12:
        if (this.anpc.isEmpty())
          continue;
        this.anpc.remove(localNpc);
        continue;
        if (this.anpc.isEmpty())
          continue;
        this.anpc.remove(localNpc);
      }
    }
    switch (this.type)
    {
    case 1:
    case 2:
    default:
    case 0:
    case 3:
    case 4:
    case 5:
    }
    while (true)
    {
      this.shotCon = this.sleepTime[this.type];
      break label68:
      this.act.setFrame(0);
      continue;
      this.act.setFrame(0);
      continue;
      this.bullet.isLive = false;
      continue;
      this.act.setAction(this.stage << 1);
      this.currentFrame = 0;
      this.targetFrame = this.act.getMaxFrame();
    }
  }

  protected void paint(Canvas paramCanvas, Paint paramPaint)
  {
    this.act.draw(paramCanvas, paramPaint);
  }

  public void removeTower(int paramInt1, int paramInt2)
  {
    GameView.map[paramInt1][paramInt2] = 0;
  }

  public void setBmpTower(Bitmap paramBitmap)
  {
    this.bmpTower = paramBitmap;
  }

  public void setCamera()
  {
    this.act.setPosition(this.x + GameView.off_x, this.y + GameView.off_y);
  }

  public void setCanBuild(boolean paramBoolean)
  {
    this.canBuild = paramBoolean;
  }

  public void setCol(int paramInt)
  {
    this.col = paramInt;
  }

  public void setPosition(int paramInt1, int paramInt2)
  {
    this.act.setPosition(paramInt1, paramInt2);
  }

  public void setPressTower(boolean paramBoolean)
  {
    this.isPressTower = paramBoolean;
  }

  public void setRow(int paramInt)
  {
    this.row = paramInt;
  }

  public void setStage(int paramInt)
  {
    this.stage = paramInt;
  }

  public void towerLogic(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int j;
    if (this.isChangeDir)
    {
      j = (int)(100000.0D * Math.atan2(paramInt1 - paramInt3, paramInt2 - paramInt4));
      if ((((j >= 5 * -this.p - this.py) || (j < 6 * -this.p))) && (((j <= 5 * this.p + this.py) || (j > 6 * this.p))))
        break label133;
    }
    for (this.targetDir = 6; ; this.targetDir = 5)
      do
        while (true)
          switch (this.type)
          {
          default:
            label132: return;
            if ((j > 4 * this.p + this.py) && (j < 6 * this.p - this.py))
              label133: this.targetDir = 7;
            if ((j > 3 * this.p + this.py) && (j < 5 * this.p - this.py))
              this.targetDir = 8;
            if ((j > 2 * this.p + this.py) && (j < 4 * this.p - this.py))
              this.targetDir = 9;
            if ((j > 1 * this.p + this.py) && (j < 3 * this.p - this.py))
              this.targetDir = 10;
            if ((j > 0 * this.p + this.py) && (j < 2 * this.p - this.py))
              this.targetDir = 11;
            if ((j > -1 * this.p + this.py) && (j < 1 * this.p - this.py))
              this.targetDir = 0;
            if ((j > -2 * this.p + this.py) && (j < 0 * this.p - this.py))
              this.targetDir = 1;
            if ((j > -3 * this.p + this.py) && (j < -1 * this.p - this.py))
              this.targetDir = 2;
            if ((j > -4 * this.p + this.py) && (j < -2 * this.p - this.py))
              this.targetDir = 3;
            if ((j <= -5 * this.p + this.py) || (j >= -3 * this.p - this.py))
              break;
            this.targetDir = 4;
          case 0:
          case 1:
          case 2:
          case 3:
          case 4:
          case 5:
          }
      while ((j <= -6 * this.p + this.py) || (j >= -4 * this.p - this.py));
    if (this.currentDir != this.targetDir)
    {
      if (this.targetDir - this.currentDir > 0)
        if (this.targetDir - this.currentDir > 6)
        {
          this.currentDir -= 1;
          if (this.currentDir >= 0);
        }
      for (this.currentDir = 11; ; this.currentDir = 0)
        do
        {
          while (true)
          {
            this.act.setAction(this.currentDir + 12 * this.stage);
            this.isChangeDir = false;
            break label132:
            this.currentDir = (1 + this.currentDir);
            if (this.currentDir <= 11)
              continue;
            this.currentDir = 0;
            continue;
            if (this.targetDir - this.currentDir <= -6)
              break;
            this.currentDir -= 1;
            if (this.currentDir >= 0)
              continue;
            this.currentDir = 11;
          }
          this.currentDir = (1 + this.currentDir);
        }
        while (this.currentDir <= 11);
    }
    this.isChangeDir = true;
    this.act.nextFrame();
    if (this.shotCon % this.sleepTime[this.type] == 0)
    {
      ((Npc)this.anpc.get(0)).attacked(this.type, this.stage);
      this.shotCon = 0;
    }
    if (GameView.is_playmusic)
      if (GameView.mp_gun == null)
        GameView.mp_gun = GameView.tool.creatMediaPlayer(2130968584);
    try
    {
      GameView.mp_gun.prepare();
      GameView.mp_gun.start();
      this.shotCon = (1 + this.shotCon);
      break label132:
      if (this.currentDir != this.targetDir)
      {
        if (this.targetDir - this.currentDir > 0)
          if (this.targetDir - this.currentDir > 6)
          {
            this.currentDir -= 1;
            if (this.currentDir >= 0);
          }
        for (this.currentDir = 11; ; this.currentDir = 0)
          do
          {
            while (true)
            {
              this.act.setAction(this.currentDir + 12 * this.stage);
              this.isChangeDir = false;
              break label132:
              this.currentDir = (1 + this.currentDir);
              if (this.currentDir <= 11)
                continue;
              this.currentDir = 0;
              continue;
              if (this.targetDir - this.currentDir <= -6)
                break;
              this.currentDir -= 1;
              if (this.currentDir >= 0)
                continue;
              this.currentDir = 11;
            }
            this.currentDir = (1 + this.currentDir);
          }
          while (this.currentDir <= 11);
      }
      label1930: label1559: label2330: label1186: label1845: label2243: label1636: label1900: label2163: if (!this.bullet.isLive)
        if (this.shotCon >= this.sleepTime[this.type])
        {
          this.targetNpc = ((Npc)this.anpc.get(0));
          this.targetX = this.targetNpc.getX();
          this.targetY = this.targetNpc.getY();
          this.act.nextFrame();
          this.bullet.addBullet(getX(), getY() - 10, this.targetX, this.targetY, this.currentDir);
          if (GameView.is_playmusic)
            if (GameView.mp_slowdown == null)
              GameView.mp_slowdown = GameView.tool.creatMediaPlayer(2130968598);
        }
    }
    catch (Exception localException6)
    {
      try
      {
        GameView.mp_slowdown.prepare();
        GameView.mp_slowdown.start();
        while (true)
        {
          this.shotCon = (1 + this.shotCon);
          this.isChangeDir = true;
          break label132:
          this.act.setFrame(0);
        }
        if (this.currentDir != this.targetDir)
        {
          if (this.targetDir - this.currentDir > 0)
            if (this.targetDir - this.currentDir > 6)
            {
              this.currentDir -= 1;
              if (this.currentDir >= 0);
            }
          for (this.currentDir = 11; ; this.currentDir = 0)
            do
            {
              while (true)
              {
                this.act.setAction(this.currentDir + 12 * this.stage);
                this.bullet.updateDir(this.currentDir);
                this.isChangeDir = false;
                break label132:
                this.currentDir = (1 + this.currentDir);
                if (this.currentDir <= 11)
                  continue;
                this.currentDir = 0;
                continue;
                if (this.targetDir - this.currentDir <= -6)
                  break;
                this.currentDir -= 1;
                if (this.currentDir >= 0)
                  continue;
                this.currentDir = 11;
              }
              this.currentDir = (1 + this.currentDir);
            }
            while (this.currentDir <= 11);
        }
        this.shotCon = (1 + this.shotCon);
        if ((this.shotCon >= this.sleepTime[this.type]) && (!this.bullet.isLive))
        {
          this.shotCon = 0;
          this.targetNpc = ((Npc)this.anpc.get(0));
          this.targetX = ((Npc)this.anpc.get(0)).getX();
          this.targetY = ((Npc)this.anpc.get(0)).getY();
          this.bullet.addBullet(getX(), getY(), this.targetX, this.targetY, 0);
          if (GameView.is_playmusic)
            if (GameView.mp_roket == null)
              GameView.mp_roket = GameView.tool.creatMediaPlayer(2130968597);
        }
      }
      catch (Exception localException5)
      {
        try
        {
          GameView.mp_roket.prepare();
          GameView.mp_roket.start();
          this.isChangeDir = true;
          break label132:
          if (this.shotCon >= this.sleepTime[this.type])
            if (!this.bullet.is_flash)
            {
              this.act.nextFrame();
              if (GameView.is_playmusic)
                if (GameView.mp_flood == null)
                  GameView.mp_flood = GameView.tool.creatMediaPlayer(2130968582);
            }
        }
        catch (Exception localException4)
        {
          try
          {
            GameView.mp_flood.prepare();
            GameView.mp_flood.start();
            if (this.act.getFrame() == this.act.getMaxFrame())
            {
              this.act.setFrame(0);
              this.bullet.x = getX();
              this.bullet.y = (getY() - 20);
              this.bullet.play_x = ((Npc)this.anpc.get(0)).getX();
              this.bullet.play_y = (((Npc)this.anpc.get(0)).getY() - 20);
              this.bullet.is_flash = true;
              ((Npc)this.anpc.get(0)).attacked(this.type, this.stage);
              this.shotCon = 0;
            }
            this.shotCon = (1 + this.shotCon);
            break label132:
            if (this.currentDir != this.targetDir)
              if (this.targetDir - this.currentDir > 0)
                if (this.targetDir - this.currentDir > 6)
                {
                  this.currentDir -= 1;
                  if (this.currentDir < 0)
                    this.currentDir = 11;
                  this.act.setAction(this.currentDir + 12 * this.stage);
                  if (!this.bullet.isLive)
                    this.bullet.addBullet(getX(), getY(), this.currentDir, 0, 0);
                  this.isChangeDir = false;
                  if (GameView.is_playmusic)
                    if (GameView.mp_fire == null)
                      GameView.mp_fire = GameView.tool.creatMediaPlayer(2130968581);
                }
          }
          catch (Exception localException3)
          {
            int i;
            try
            {
              GameView.mp_fire.prepare();
              GameView.mp_fire.start();
              this.bullet.updateDir(this.currentDir);
              break label132:
              this.currentDir = (1 + this.currentDir);
              if (this.currentDir > 11);
              this.currentDir = 0;
              break label1845:
              if (this.targetDir - this.currentDir > -6)
              {
                this.currentDir -= 1;
                if (this.currentDir < 0);
                this.currentDir = 11;
              }
              this.currentDir = (1 + this.currentDir);
              if (this.currentDir > 11);
              this.currentDir = 0;
              break label1845:
              if (!this.bullet.isLive)
                this.bullet.addBullet(getX(), getY(), this.currentDir, 0, 0);
              this.isChangeDir = true;
              ((Npc)this.anpc.get(0)).attacked(this.type, this.stage);
              break label1900:
              if (this.shotCon >= this.sleepTime[this.type])
              {
                if (this.currentFrame == this.targetFrame)
                  break label2243;
                if (!this.bullet.isChangeAction)
                {
                  this.currentFrame = (1 + this.currentFrame);
                  this.act.setFrame(this.currentFrame);
                }
              }
              this.shotCon = (1 + this.shotCon);
              if (this.bullet.attack);
              i = 0;
              if (i >= this.anpc.size())
              {
                this.bullet.attack = false;
                this.act.setAction(2 * this.stage);
                this.currentFrame = 0;
                this.targetFrame = this.act.getMaxFrame();
                this.shotCon = 0;
                break label132:
                if (!this.bullet.isLive)
                {
                  this.bullet.addBullet(getX(), getY() - 10, ((Npc)this.anpc.get(0)).getX(), ((Npc)this.anpc.get(0)).getY(), 0);
                  if (GameView.is_playmusic)
                    if (GameView.mp_boomb == null)
                      GameView.mp_boomb = GameView.tool.creatMediaPlayer(2130968580);
                }
              }
            }
            catch (Exception localException2)
            {
              try
              {
                GameView.mp_boomb.prepare();
                GameView.mp_boomb.start();
                if (this.act.getAction() == 2 * this.stage);
                this.act.setAction(1 + 2 * this.stage);
                this.currentFrame = 0;
                this.targetFrame = this.act.getMaxFrame();
                break label2163:
                if (i < 3)
                  ((Npc)this.anpc.get(i)).attacked(this.type, this.stage);
                ++i;
              }
              catch (Exception localException1)
              {
                break label2330:
                localException2 = localException2;
              }
              break label1930:
              localException3 = localException3;
            }
            break label1636:
            localException4 = localException4;
          }
          break label1559:
          localException5 = localException5;
        }
        break label1186:
        localException6 = localException6;
      }
    }
  }

  public void upDateOrSell(ArrayList<Tower> paramArrayList, int paramInt1, int paramInt2)
  {
    int i = 0;
    if (i >= GameView.towers.size())
      label3: return;
    if (paramArrayList.get(i) == null);
    while (true)
    {
      ++i;
      break label3:
      Tower localTower = (Tower)paramArrayList.get(i);
      if (!localTower.isPressTower)
        continue;
      if (Tool.isin(paramInt1, paramInt2, localTower.getCenterX() - 35 - 60, localTower.getCenterY() - 30, 60, 60))
      {
        paramArrayList.remove(i);
        localTower.removeTower(localTower.getRow(), localTower.getCol());
        GameView.money += saleFee[localTower.type][localTower.stage];
        GameView.sale_to_regetpath = true;
      }
      if ((Tool.isin(paramInt1, paramInt2, 35 + localTower.getCenterX(), localTower.getCenterY() - 30, 60, 60)) && (GameView.money >= upDataFee[localTower.type]) && (localTower.stage < 2))
      {
        localTower.updateTower();
        GameView.money -= upDataFee[localTower.type];
      }
      localTower.setPressTower(false);
    }
  }

  public void updateTower()
  {
    int i = GameView.map[this.row][this.col] - 1;
    switch (i)
    {
    default:
    case 0:
    case 1:
    case 2:
    case 4:
    case 3:
    case 5:
    }
    while (true)
    {
      setPressTower(false);
      return;
      if ((this.stage >= 2) || (!isMoneyEnuogh(i)))
        continue;
      this.stage = (1 + this.stage);
      this.act.setAction(this.currentDir + 12 * this.stage);
      continue;
      if ((this.stage >= 2) || (!isMoneyEnuogh(i)))
        continue;
      this.stage = (1 + this.stage);
      this.act.setAction(this.stage);
      continue;
      if ((this.stage >= 2) || (!isMoneyEnuogh(i)))
        continue;
      this.stage = (1 + this.stage);
      this.act.setAction(2 * this.stage);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.Tower
 * JD-Core Version:    0.5.4
 */