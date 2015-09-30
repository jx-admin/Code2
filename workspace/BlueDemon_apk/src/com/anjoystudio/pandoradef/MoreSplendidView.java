package com.anjoystudio.pandoradef;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

class MoreSplendidView extends SurfaceView
  implements Runnable, SurfaceHolder.Callback
{
  public Bitmap bm_more_splendid;
  private Thread gameThread;
  private boolean isExit;
  private boolean isFirstRun;
  private Paint p = new Paint();
  private boolean pause;
  public int scr_h;
  public int scr_w;
  private SurfaceHolder surfaceHolder;

  public MoreSplendidView(Context paramContext)
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
  }

  private void pointerDragred(int paramInt1, int paramInt2)
  {
  }

  private void pointerPressed(int paramInt1, int paramInt2)
  {
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
    if (this.isExit)
      return;
    Tool.draw_bitmap(paramCanvas, this.p, GameView.bm_main_menu, this.scr_w >> 1, this.scr_h >> 1, 3);
    Tool.draw_bitmap(paramCanvas, this.p, this.bm_more_splendid, this.scr_w >> 1, this.scr_h >> 1, 3);
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
    this.gameThread.start();
    this.isFirstRun = false;
  }

  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    this.isExit = true;
    MoreSplendidActivity.msa.exit();
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.MoreSplendidView
 * JD-Core Version:    0.5.4
 */