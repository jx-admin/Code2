package com.anjoystudio.pandoradef;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Random;

public class Bullet
  implements Serializable
{
  public static final int BOMB_BULLET = 5;
  public static final int BOOM_BULLET = 2;
  public static final int FIRE_BULLET = 4;
  public static final int FLOOD_BULLET = 3;
  public static final int GUN_BULLET = 0;
  public static final int SLOW_DOWN_BULLET = 1;
  Action act;
  Action act1;
  Action act2;
  Action act3;
  public int angle = 30;
  public int angle_r = 20;
  boolean attack;
  Bitmap bmpBullet;
  int count;
  int dir;
  int du = 90;
  int end_x;
  int end_y;
  public int flash_time;
  int g = 10;
  boolean isAttack;
  boolean isChangeAction;
  public boolean isLive;
  public boolean is_flash;
  double length_x;
  double length_y;
  public int play_x;
  public int play_y;
  public int[][] point;
  Resources res;
  int sbx;
  int sby;
  int sleepTime;
  int speed;
  int speed_x;
  int speed_y;
  int t1;
  int t2 = 20;
  int t2r = 40;
  int t2r1 = 55;
  int t2r2 = 70;
  int t2r3 = 85;
  int tbeishu = 20000;
  int type;
  int vx1;
  int vy1;
  int windSpeed;
  int x;
  int y;

  public Bullet(Resources paramResources, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.type = paramInt3;
    this.sleepTime = Data.sleepTime[paramInt3];
    switch (paramInt3)
    {
    case 0:
    default:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    }
    while (true)
    {
      return;
      this.res = paramResources;
      this.bmpBullet = paramBitmap;
      this.act = new Action(paramResources, paramBitmap, 2130968579);
      this.act.setAction(0);
      continue;
      this.res = paramResources;
      this.bmpBullet = paramBitmap;
      this.dir = paramInt4;
      this.act = new Action(paramResources, paramBitmap, 2130968577);
      this.act.setAction(paramInt4);
      continue;
      this.isLive = true;
      int[] arrayOfInt = new int[2];
      arrayOfInt[0] = 5;
      arrayOfInt[1] = 2;
      this.point = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt));
      continue;
      this.res = paramResources;
      this.bmpBullet = paramBitmap;
      this.act = new Action(paramResources, paramBitmap, 2130968578);
      this.act1 = new Action(paramResources, paramBitmap, 2130968578);
      this.act2 = new Action(paramResources, paramBitmap, 2130968578);
      this.act3 = new Action(paramResources, paramBitmap, 2130968578);
      this.act.setAction(0);
      this.act1.setAction(0);
      this.act1.setFrame(2);
      this.act2.setAction(0);
      this.act2.setFrame(4);
      this.act3.setAction(0);
      this.act3.setFrame(6);
      continue;
      this.res = paramResources;
      this.bmpBullet = paramBitmap;
      this.act = new Action(paramResources, paramBitmap, 2130968576);
      this.act.setAction(0);
    }
  }

  public void addBullet(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    switch (this.type)
    {
    case 3:
    default:
    case 1:
    case 2:
    case 4:
    case 5:
    }
    while (true)
    {
      return;
      this.isLive = true;
      this.x = paramInt1;
      this.y = paramInt2;
      this.dir = paramInt5;
      this.end_x = paramInt3;
      this.end_y = paramInt4;
      this.length_x = Math.abs(paramInt1 - paramInt3);
      this.length_y = Math.abs(paramInt2 - paramInt4);
      if (this.length_x >= this.length_y)
        if (paramInt3 - paramInt1 != 0)
        {
          this.speed_x = (8 * ((paramInt3 - paramInt1) / Math.abs(paramInt3 - paramInt1)));
          if (paramInt2 > paramInt4)
            this.speed_y = (int)(-this.length_y / Math.abs(this.length_x / this.speed_x));
          this.speed_y = (int)(this.length_y / Math.abs(this.length_x / this.speed_x));
        }
      if (paramInt4 - paramInt2 != 0)
      {
        this.speed_y = (8 * ((paramInt4 - paramInt2) / Math.abs(paramInt4 - paramInt2)));
        if (paramInt1 > paramInt3)
          this.speed_x = (int)(-this.length_x / Math.abs(this.length_y / this.speed_y));
        this.speed_x = (int)(this.length_x / Math.abs(this.length_y / this.speed_y));
      }
      continue;
      this.isLive = true;
      this.x = paramInt1;
      this.y = paramInt2;
      this.end_x = paramInt3;
      this.end_y = paramInt4;
      this.length_x = (paramInt3 - paramInt1);
      this.length_y = (paramInt4 - paramInt2);
      this.speed = 10;
      continue;
      this.isLive = true;
      this.x = paramInt1;
      this.y = paramInt2;
      this.sbx = (paramInt1 - this.t2r);
      this.sby = paramInt2;
      this.dir = paramInt3;
      continue;
      this.isLive = true;
      this.t1 = 0;
      this.x = paramInt1;
      this.y = paramInt2;
      this.sbx = paramInt1;
      this.sby = paramInt2;
      this.end_x = paramInt3;
      this.end_y = paramInt4;
      getBombData(paramInt3, paramInt4);
    }
  }

  public void getBombData(int paramInt1, int paramInt2)
  {
    this.windSpeed = ((paramInt1 - this.sbx) / this.t2);
    this.vy1 = ((100000 * (this.sby - paramInt2) + this.g / 2 * this.t2 * this.t2 * this.tbeishu) / this.t2);
  }

  public void logic()
  {
    if (this.isLive);
    switch (this.type)
    {
    case 0:
    default:
    case 1:
    case 2:
    case 3:
    case 4:
      while (true)
      {
        label48: return;
        this.count = (1 + this.count);
        if (!this.attack)
        {
          this.x += this.speed_x;
          this.y += this.speed_y;
        }
        if (!this.isChangeAction)
        {
          this.act.setAction(1);
          this.isChangeAction = true;
          this.isAttack = true;
        }
        this.act.nextFrame();
        if ((this.act.getAction() != 1) || (this.act.getFrame() != this.act.getMaxFrame()))
          continue;
        this.isLive = false;
        this.isChangeAction = false;
        this.attack = false;
        this.isAttack = false;
        this.act.setAction(0);
        continue;
        this.length_x = (this.end_x - this.x);
        this.length_y = (this.end_y - this.y);
        if (this.length_x == 0.0D)
        {
          if (this.end_y < this.y)
            break label366;
          this.length_x = 1.0E-006D;
        }
        if (this.length_y == 0.0D)
        {
          label241: if (this.end_x < this.x)
            break label376;
          this.length_y = 1.0E-006D;
        }
        if ((this.length_x > 0.0D) && (this.length_y > 0.0D))
          label268: this.angle = (int)Math.atan(Math.abs(this.length_y / this.length_x));
        while (!this.attack)
        {
          this.x = (int)(this.x + this.speed * Math.cos(this.angle));
          this.y = (int)(this.y + this.speed * Math.sin(this.angle));
          break label48:
          label366: this.length_x = -1.0E-006D;
          break label241:
          label376: this.length_y = -1.0E-006D;
          break label268:
          if ((this.length_x < 0.0D) && (this.length_y > 0.0D))
            this.angle = (int)(3.141592653589793D - Math.atan(Math.abs(this.length_y / this.length_x)));
          if ((this.length_x < 0.0D) && (this.length_y < 0.0D))
            this.angle = (int)(3.141592653589793D + Math.atan(Math.abs(this.length_y / this.length_x)));
          if ((this.length_x > 0.0D) && (this.length_y < 0.0D))
            this.angle = (int)(6.283185307179586D - Math.atan(Math.abs(this.length_y / this.length_x)));
          this.x = this.end_x;
          this.y = this.end_y;
        }
        if (!this.isChangeAction)
        {
          this.act.setAction(1);
          this.isChangeAction = true;
          this.isAttack = true;
        }
        this.act.nextFrame();
        if ((this.act.getAction() != 1) || (this.act.getFrame() != this.act.getMaxFrame()))
          continue;
        this.isLive = false;
        this.isChangeAction = false;
        this.attack = false;
        this.isAttack = false;
        this.act.setAction(0);
        continue;
        if (!this.is_flash)
          continue;
        this.flash_time = (1 + this.flash_time);
        int i = 3 * Math.abs(this.play_x - this.x) / 5;
        int j = 3 * Math.abs(this.play_y - this.y) / 5;
        Random localRandom = new Random();
        if (i < 40)
          i = 40;
        if (j < 40)
          j = 40;
        for (int k = 1; ; ++k)
        {
          if (k >= this.point.length - 1)
          {
            this.point[0][0] = this.play_x;
            this.point[0][1] = this.play_y;
            this.point[(this.point.length - 1)][0] = this.x;
            this.point[(this.point.length - 1)][1] = this.y;
            if (this.flash_time > 5);
            this.is_flash = false;
            this.flash_time = 0;
          }
          this.point[k][0] = (Math.abs(localRandom.nextInt()) % i + Math.min(this.x, this.play_x) + i / 3);
          this.point[k][1] = (Math.abs(localRandom.nextInt()) % j + Math.min(this.y, this.play_y) + j / 3);
        }
        this.x = (this.sbx + (int)(this.t2r - this.t2r * Math.cos(3.141592653589793D * (30 * this.dir - 90) / 180.0D)));
        this.y = (this.sby - (int)(this.t2r * Math.sin(3.141592653589793D * (30 * this.dir - 90) / 180.0D)));
        this.act.setPosition(this.x + GameView.off_x, this.y + GameView.off_y);
        this.act.nextFrame();
        this.x = (this.sbx - 15 + (int)(this.t2r1 - this.t2r1 * Math.cos(3.141592653589793D * (30 * this.dir - 90) / 180.0D)));
        this.y = (this.sby - (int)(this.t2r1 * Math.sin(3.141592653589793D * (30 * this.dir - 90) / 180.0D)));
        this.act1.setPosition(this.x + GameView.off_x, this.y + GameView.off_y);
        this.act1.nextFrame();
        this.x = (this.sbx - 30 + (int)(this.t2r2 - this.t2r2 * Math.cos(3.141592653589793D * (30 * this.dir - 90) / 180.0D)));
        this.y = (this.sby - (int)(this.t2r2 * Math.sin(3.141592653589793D * (30 * this.dir - 90) / 180.0D)));
        this.act2.setPosition(this.x + GameView.off_x, this.y + GameView.off_y);
        this.act2.nextFrame();
        this.x = (this.sbx - 45 + (int)(this.t2r3 - this.t2r3 * Math.cos(3.141592653589793D * (30 * this.dir - 90) / 180.0D)));
        this.y = (this.sby - (int)(this.t2r3 * Math.sin(3.141592653589793D * (30 * this.dir - 90) / 180.0D)));
        this.act3.setPosition(this.x + GameView.off_x, this.y + GameView.off_y);
        this.act3.nextFrame();
      }
    case 5:
    }
    this.x = (this.sbx + (this.vx1 + 100000 * this.windSpeed) * this.t1 / 100000);
    this.y = (this.sby - (this.vy1 * this.t1 - this.g / 2 * this.t1 * this.t1 * this.tbeishu) / 100000);
    if (this.t1 < this.t2)
      this.t1 = (2 + this.t1);
    while (true)
    {
      this.act.nextFrame();
      if ((this.act.getAction() == 1) && (this.act.getFrame() == this.act.getMaxFrame()));
      this.isLive = false;
      this.isChangeAction = false;
      this.act.setAction(0);
      break label48:
      if (this.isChangeAction)
        continue;
      this.isChangeAction = true;
      this.attack = true;
      this.act.setAction(1);
    }
  }

  public void paint(Canvas paramCanvas, Paint paramPaint)
  {
    if (this.isLive)
      switch (this.type)
      {
      case 0:
      default:
      case 1:
      case 2:
      case 5:
      case 3:
      case 4:
      }
    while (true)
    {
      return;
      this.act.draw(paramCanvas, paramPaint);
      continue;
      if (!this.is_flash)
        continue;
      for (int i = 0; ; ++i)
      {
        if (i < this.point.length - 1);
        paramPaint.setColor(-1);
        paramCanvas.drawLine(this.point[i][0] + GameView.off_x, this.point[i][1] + GameView.off_y, this.point[(i + 1)][0] + GameView.off_x, this.point[(i + 1)][1] + GameView.off_y, paramPaint);
        paramPaint.setColor(-1828716289);
        paramCanvas.drawLine(1 + this.point[i][0] + GameView.off_x, 1 + this.point[i][1] + GameView.off_y, 1 + this.point[(i + 1)][0] + GameView.off_x, 1 + this.point[(i + 1)][1] + GameView.off_y, paramPaint);
        paramCanvas.drawLine(this.point[i][0] - 1 + GameView.off_x, this.point[i][1] - 1 + GameView.off_y, this.point[(i + 1)][0] - 1 + GameView.off_x, this.point[(i + 1)][1] - 1 + GameView.off_y, paramPaint);
      }
      this.act.draw(paramCanvas, paramPaint);
      this.act1.draw(paramCanvas, paramPaint);
      this.act2.draw(paramCanvas, paramPaint);
      this.act3.draw(paramCanvas, paramPaint);
    }
  }

  public void setCamera()
  {
    if (this.act != null)
      this.act.setPosition(this.x + GameView.off_x, this.y + GameView.off_y);
    if (this.act1 != null)
      this.act1.setPosition(this.x + GameView.off_x, this.y + GameView.off_y);
    if (this.act2 != null)
      this.act2.setPosition(this.x + GameView.off_x, this.y + GameView.off_y);
    if (this.act3 == null)
      return;
    this.act3.setPosition(this.x + GameView.off_x, this.y + GameView.off_y);
  }

  public void updateDir(int paramInt)
  {
    this.dir = paramInt;
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.Bullet
 * JD-Core Version:    0.5.4
 */