package com.anjoystudio.pandoradef;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

class HightScoreView extends SurfaceView
  implements Runnable, SurfaceHolder.Callback
{
  public Bitmap bm_hight_score;
  private Thread gameThread;
  public int high_selected;
  public int high_time = -1;
  public int highscore_choice_map;
  public int highscore_choice_mode;
  private boolean isExit;
  private boolean isFirstRun;
  public Intent jumpto_mainActivity;
  private Paint p = new Paint();
  private boolean pause;
  public int scr_h;
  public int scr_w;
  private SurfaceHolder surfaceHolder;

  public HightScoreView(Context paramContext)
  {
    super(paramContext);
    this.p.setAntiAlias(true);
    this.surfaceHolder = getHolder();
    this.surfaceHolder.addCallback(this);
    setFocusable(true);
    this.isFirstRun = true;
    this.gameThread = new Thread(this);
  }

  private void logic()
  {
    if (this.high_time >= 0)
      this.high_time = (1 + this.high_time);
    if (this.high_time >= 5)
    {
      this.high_time = -1;
      switch (this.high_selected)
      {
      default:
      case 0:
      }
    }
    while (true)
    {
      return;
      this.jumpto_mainActivity = new Intent(HightScoreActivity.hsa, MainActivity.class);
      HightScoreActivity.hsa.startActivity(this.jumpto_mainActivity);
    }
  }

  private void pointerDragred(int paramInt1, int paramInt2)
  {
  }

  private void pointerPressed(int paramInt1, int paramInt2)
  {
    this.high_time = 0;
    if (Tool.prsButton(paramInt1, paramInt2, 410, 0, 480, 60))
      this.high_selected = 0;
    while (true)
    {
      label27: return;
      this.high_time = -1;
      if (Tool.prsButton(paramInt1, paramInt2, 40, 50, 120, 110));
      for (this.highscore_choice_map = 0; Tool.prsButton(paramInt1, paramInt2, 60, 270, 130, 310); this.highscore_choice_map = 3)
        do
          while (true)
          {
            this.highscore_choice_mode = 0;
            break label27:
            if (Tool.prsButton(paramInt1, paramInt2, 40, 110, 120, 165))
              this.highscore_choice_map = 1;
            if (!Tool.prsButton(paramInt1, paramInt2, 40, 165, 120, 215))
              break;
            this.highscore_choice_map = 2;
          }
        while (!Tool.prsButton(paramInt1, paramInt2, 40, 215, 120, 270));
      if (Tool.prsButton(paramInt1, paramInt2, 210, 270, 280, 310))
        this.highscore_choice_mode = 1;
      if (!Tool.prsButton(paramInt1, paramInt2, 340, 270, 410, 310))
        continue;
      this.highscore_choice_mode = 2;
    }
  }

  private void pointerRaleased(int paramInt1, int paramInt2)
  {
  }

  public void doPause()
  {
    this.pause = true;
  }

  public void drawRect(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramCanvas.drawRect(paramInt1 + (this.scr_w - this.bm_hight_score.getWidth() >> 1), paramInt2 + (this.scr_h - this.bm_hight_score.getHeight() >> 1), paramInt3 + (this.scr_w - this.bm_hight_score.getWidth() >> 1), paramInt4 + (this.scr_h - this.bm_hight_score.getHeight() >> 1), this.p);
  }

  public boolean getExit()
  {
    return this.isExit;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    switch (paramMotionEvent.getAction())
    {
    default:
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      return true;
      pointerPressed(i, j);
      continue;
      pointerRaleased(i, j);
      continue;
      pointerDragred(i, j);
    }
  }

  protected void paint(Canvas paramCanvas)
  {
    if (!this.isExit)
    {
      Tool.draw_bitmap(paramCanvas, this.p, GameView.bm_main_menu, this.scr_w >> 1, this.scr_h >> 1, 3);
      Tool.draw_bitmap(paramCanvas, this.p, this.bm_hight_score, this.scr_w >> 1, this.scr_h >> 1, 3);
      this.p.setColor(-256);
      this.p.setStyle(Paint.Style.STROKE);
      switch (this.highscore_choice_map)
      {
      default:
        label112: switch (this.highscore_choice_mode)
        {
        default:
        case 0:
        case 1:
        case 2:
        }
      case 0:
      case 1:
      case 2:
      case 3:
      }
    }
    while (true)
    {
      return;
      drawRect(paramCanvas, 34, 49, 96, 96);
      break label112:
      drawRect(paramCanvas, 34, 102, 96, 148);
      break label112:
      drawRect(paramCanvas, 34, 155, 96, 202);
      break label112:
      drawRect(paramCanvas, 34, 209, 96, 254);
      break label112:
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 61 + (this.scr_w - this.bm_hight_score.getWidth() >> 1), 262 + (this.scr_w - this.bm_hight_score.getWidth() >> 1), 0, 375, 50, 19, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 211 + (this.scr_w - this.bm_hight_score.getWidth() >> 1), 262 + (this.scr_w - this.bm_hight_score.getWidth() >> 1), 0, 394, 50, 17, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 340 + (this.scr_w - this.bm_hight_score.getWidth() >> 1), 262 + (this.scr_w - this.bm_hight_score.getWidth() >> 1), 0, 411, 50, 20, 0);
    }
  }

  public void resume()
  {
    this.pause = false;
  }

  public void run()
  {
    while (true)
    {
      if (this.pause)
        continue;
      long l1 = SystemClock.currentThreadTimeMillis();
      Canvas localCanvas = null;
      try
      {
        logic();
        localCanvas = this.surfaceHolder.lockCanvas();
        paint(localCanvas);
        long l2 = SystemClock.currentThreadTimeMillis() - l1;
        if (l2 < 50L)
          Thread.sleep(50L - l2);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      finally
      {
        if (localCanvas != null)
          this.surfaceHolder.unlockCanvasAndPost(localCanvas);
      }
    }
  }

  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    if (!this.isFirstRun)
      return;
    this.scr_w = getWidth();
    this.scr_h = getHeight();
    this.bm_hight_score = BitmapFactory.decodeResource(getResources(), 2130837520);
    this.high_time = -1;
    this.isExit = false;
    this.gameThread.start();
    this.isFirstRun = false;
  }

  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    this.isExit = true;
    GameView.state = 1;
    this.jumpto_mainActivity = new Intent(HightScoreActivity.hsa, MainActivity.class);
    HightScoreActivity.hsa.exit();
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.HightScoreView
 * JD-Core Version:    0.5.4
 */