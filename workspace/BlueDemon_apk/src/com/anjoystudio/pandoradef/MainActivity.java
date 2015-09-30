package com.anjoystudio.pandoradef;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends Activity
{
  public static ProgressDialog dia_loading;
  static MainActivity mainActivity;
  public static int scr_h;
  public static int scr_w;
  GameView gv;
  private boolean isNeedShowing = true;

  private boolean checkDiguaInstall()
  {
    Iterator localIterator = getPackageManager().getInstalledPackages(0).iterator();
    if (!localIterator.hasNext());
    for (int i = 0; ; i = 1)
    {
      return i;
      if (!"com.diguayouxi".equals(((PackageInfo)localIterator.next()).packageName));
    }
  }

  public void exit()
  {
    GameView.state = 0;
    this.gv.save_isFirstRun(GameView.state);
    this.gv.saveMenu();
    this.gv.thread_run = false;
    Process.killProcess(Process.myPid());
  }

  public void onCreate(Bundle paramBundle)
  {
    Log.v("[show1]", "onCreat");
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setRequestedOrientation(0);
    getWindow().setFlags(128, 128);
    Process.setThreadPriority(-20);
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
    scr_w = localDisplayMetrics.widthPixels;
    scr_h = localDisplayMetrics.heightPixels;
    Log.v("[show]", "scr_w = " + scr_w + " , scr_h = " + scr_h);
    mainActivity = this;
    this.gv = new GameView(mainActivity);
    setContentView(this.gv);
  }

  protected Dialog onCreateDialog(int paramInt)
  {
    return new AlertDialog.Builder(this).setMessage(getString(2131034114)).setIcon(17301659).setNeutralButton(2131034115, new MainActivity.2(this)).setPositiveButton(2131034116, new MainActivity.3(this)).setNegativeButton(2131034117, new MainActivity.4(this)).create();
  }

  protected void onDestroy()
  {
    Log.v("[show1]", "onDestroy");
    super.onDestroy();
    this.gv.saveMenu();
    this.gv.save_isFirstRun(GameView.state);
    this.gv.thread_run = false;
    if (this.gv.is_exit)
      exit();
    while (true)
    {
      return;
      this.gv.save_isFirstRun(GameView.state);
    }
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    System.out.println("keyCode......" + paramInt);
    GameView.will_music = false;
    if (paramInt == 4)
    {
      if ((GameView.state != 3) && (GameView.state != 4) && (GameView.state != 2) && (GameView.state != 8) && (GameView.state != 9))
        break label98;
      GameView.state = 1;
    }
    while (true)
    {
      if (paramInt == 3)
        Log.v("[show]", "HOME is pressed !");
      for (boolean bool = this.gv.onKeyDown(paramInt, paramKeyEvent); ; bool = super.onKeyDown(paramInt, paramKeyEvent))
      {
        return bool;
        if (GameView.state == 5)
          label98: GameView.state = GameView.last_state;
        if (!this.gv.is_exit)
          break;
      }
      showFinish();
    }
  }

  public void onLowMemory()
  {
    Log.v("[show1]", "onLowMemory");
    super.onLowMemory();
  }

  protected void onPause()
  {
    Log.v("[show1]", "onPause");
    this.gv.save_isFirstRun(GameView.state);
    this.gv.doPause();
    GameView.will_not_play_music = true;
    super.onPause();
  }

  public void onRestart()
  {
    Log.v("[show1]", "onRestart");
    super.onRestart();
  }

  protected void onResume()
  {
    Log.v("[show1]", "onResme");
    if ((GameView.will_music) && (GameView.state != 0) && (GameView.state != 7) && (GameView.state != 11) && (GameView.state != 12))
      if (GameView.mp_music == null)
      {
        if (GameView.tool == null)
          GameView.tool = new Tool(this);
        GameView.mp_music = GameView.tool.creatMediaPlayer(2130968586);
        GameView.mp_music.setLooping(true);
        GameView.mp_music.stop();
      }
    try
    {
      GameView.mp_music.prepare();
      Log.v("[show]", "onStart mp_music.start()");
      GameView.mp_music.start();
      DisplayMetrics localDisplayMetrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
      scr_h = localDisplayMetrics.widthPixels;
      scr_w = localDisplayMetrics.heightPixels;
      this.gv.resume();
      if (dia_loading != null)
      {
        dia_loading.cancel();
        this.gv.setFocusable(true);
        dia_loading = null;
      }
      if ((this.isNeedShowing) && (!checkDiguaInstall()))
      {
        int i = ArchiveUitl.newInstance(this).getInt("run-time");
        if (i % 6 == 0)
          showDialog(0);
        ArchiveUitl.newInstance(this).saveInt("run-time", i + 1);
      }
      super.onResume();
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void onStart()
  {
    Log.v("[show1]", "onStart");
    super.onStart();
    getWindow().setFlags(1024, 1024);
    setRequestedOrientation(0);
    if ((GameView.state != 0) && (GameView.state != 7) && (GameView.state != 11) && (GameView.state != 12) && (((GameView.state != 5) || (GameView.last_state != 7))))
      if (GameView.mp_music == null)
      {
        if (GameView.tool == null)
          GameView.tool = new Tool(this);
        GameView.mp_music = GameView.tool.creatMediaPlayer(2130968586);
        GameView.mp_music.setLooping(true);
        GameView.mp_music.stop();
      }
    try
    {
      GameView.mp_music.prepare();
      Log.v("[show]", "onStart mp_music.start()");
      GameView.mp_music.start();
      this.gv.load_isFirstRun();
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void showFinish()
  {
    new AlertDialog.Builder(this).setMessage("是否退出游戏").setPositiveButton("确定", new MainActivity.1(this)).setNegativeButton("取消", null).create().show();
  }

  public void showLoading()
  {
    dia_loading = new ProgressDialog(this);
    dia_loading.setTitle("正在处理数据");
    dia_loading.setMessage("请稍后");
    dia_loading.setProgressStyle(0);
    dia_loading.show();
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.MainActivity
 * JD-Core Version:    0.5.4
 */