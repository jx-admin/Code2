package com.anjoystudio.pandoradef;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

class AboutView extends SurfaceView
  implements Runnable, SurfaceHolder.Callback
{
  public int about_select;
  public int about_time = -1;
  public Bitmap bm_about;
  private Thread gameThread;
  private boolean isExit;
  private boolean isFirstRun;
  public Intent jumpto_mainActivity;
  private Paint p = new Paint();
  private boolean pause;
  public int scr_h;
  public int scr_w;
  private SurfaceHolder surfaceHolder;

  public AboutView(Context paramContext)
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
    if (this.about_time >= 0)
    {
      this.about_time = (1 + this.about_time);
      if (this.about_time >= 5)
      {
        this.about_time = -1;
        switch (this.about_select)
        {
        default:
        case 0:
        }
      }
    }
    while (true)
    {
      return;
      this.jumpto_mainActivity = new Intent(AboutActivity.aa, MainActivity.class);
      AboutActivity.aa.startActivity(this.jumpto_mainActivity);
    }
  }

  private void pointerDragred(int paramInt1, int paramInt2)
  {
  }

  private void pointerPressed(int paramInt1, int paramInt2)
  {
    this.about_time = 0;
    if (Tool.prsButton(paramInt1, paramInt2, 400, 0, 480, 80))
      this.about_select = 0;
    while (true)
    {
      return;
      this.about_time = -1;
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
      Tool.draw_bitmap(paramCanvas, this.p, this.bm_about, this.scr_w >> 1, this.scr_h >> 1, 3);
      if (this.about_time >= 0)
        switch (this.about_select)
        {
        default:
        case 0:
        }
    }
    while (true)
    {
      return;
      Tool.draw_bitmap_clip(paramCanvas, this.p, GameView.bm_all_b, 416, 22, 0, 223, 30, 33, 0);
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
    Log.v("[show]", "is Changed !");
  }

  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    Log.v("[show]", "is Created !");
    if (!this.isFirstRun)
      return;
    this.scr_w = getWidth();
    this.scr_h = getHeight();
    this.bm_about = BitmapFactory.decodeResource(getResources(), 2130837504);
    this.isExit = false;
    this.gameThread.start();
    this.isFirstRun = false;
  }

  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    this.isExit = true;
    AboutActivity.aa.exit();
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.AboutView
 * JD-Core Version:    0.5.4
 */