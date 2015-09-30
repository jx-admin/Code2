package com.anjoystudio.pandoradef;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

class HelpView extends SurfaceView
  implements Runnable, SurfaceHolder.Callback
{
  public Bitmap bm_help;
  private Thread gameThread;
  public int help_selected;
  public int help_time = -1;
  public int index;
  private boolean isExit;
  private boolean isFirstRun = true;
  public Intent jumpto_mainActivity;
  private Paint p = new Paint();
  private boolean pause;
  public int scr_h;
  public int scr_w;
  private SurfaceHolder surfaceHolder;

  public HelpView(Context paramContext)
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
    if (this.help_time >= 0)
      this.help_time = (1 + this.help_time);
    if (this.help_time >= 5)
    {
      this.help_time = -1;
      switch (this.help_selected)
      {
      default:
      case 0:
      case 1:
      case 2:
      }
    }
    while (true)
    {
      return;
      this.index -= 1;
      if (this.index >= 0)
        continue;
      this.index = 0;
      continue;
      this.index = (1 + this.index);
      if (this.index <= 2)
        continue;
      this.index = 2;
      continue;
      this.jumpto_mainActivity = new Intent(HelpActivity.ha, MainActivity.class);
      HelpActivity.ha.startActivity(this.jumpto_mainActivity);
    }
  }

  private void pointerDragred(int paramInt1, int paramInt2)
  {
  }

  private void pointerPressed(int paramInt1, int paramInt2)
  {
    this.help_time = 0;
    if (Tool.prsButton(paramInt1, paramInt2, 50, 150, 110, 200))
      this.help_selected = 0;
    while (true)
    {
      return;
      if (Tool.prsButton(paramInt1, paramInt2, 380, 150, 460, 200))
        this.help_selected = 1;
      if (Tool.prsButton(paramInt1, paramInt2, 400, 0, 480, 70))
        this.help_selected = 2;
      this.help_time = -1;
    }
  }

  private void pointerRaleased(int paramInt1, int paramInt2)
  {
  }

  public void doPause()
  {
    this.pause = true;
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
      Tool.draw_bitmap(paramCanvas, this.p, this.bm_help, this.scr_w >> 1, this.scr_h >> 1, 3);
      switch (this.index)
      {
      default:
        label88: if (this.help_time < 0)
          break label124;
        switch (this.help_selected)
        {
        default:
        case 0:
        case 1:
        case 2:
        }
      case 0:
      case 1:
      case 2:
      }
    }
    while (true)
    {
      label124: return;
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 86 + (this.scr_w - this.bm_help.getWidth() >> 1), 115 + (this.scr_w - this.bm_help.getWidth() >> 1), 0, 130, this.bm_help.getWidth(), 31, 0);
      break label88:
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 86 + (this.scr_w - this.bm_help.getWidth() >> 1), 115 + (this.scr_w - this.bm_help.getWidth() >> 1), 0, 161, 281, 31, 0);
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 86 + (this.scr_w - this.bm_help.getWidth() >> 1), 145 + (this.scr_w - this.bm_help.getWidth() >> 1), 284, 161, 60, 31, 0);
      break label88:
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 86 + (this.scr_w - this.bm_help.getWidth() >> 1), 115 + (this.scr_w - this.bm_help.getWidth() >> 1), 0, 192, this.bm_help.getWidth(), 31, 0);
      break label88:
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 38 + (this.scr_w - this.bm_help.getWidth() >> 1), 175 + (this.scr_h - this.bm_help.getHeight() >> 1), 0, 39, 50, 23, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 380 + (this.scr_w - this.bm_help.getWidth() >> 1), 174 + (this.scr_h - this.bm_help.getHeight() >> 1), 0, 62, 70, 25, 0);
      continue;
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 401 + (this.scr_w - this.bm_help.getWidth() >> 1), 22 + (this.scr_h - this.bm_help.getHeight() >> 1), 0, 223, 30, 33, 0);
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
    this.bm_help = BitmapFactory.decodeResource(getResources(), 2130837518);
    this.help_time = -1;
    this.isExit = false;
    this.gameThread.start();
    this.isFirstRun = false;
  }

  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    this.isExit = true;
    HelpActivity.ha.exit();
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.HelpView
 * JD-Core Version:    0.5.4
 */