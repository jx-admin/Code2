package com.anjoystudio.pandoradef;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound
  implements Runnable
{
  public int playCount;
  public MediaPlayer[] sound;
  public int stopCount;
  public boolean will_start = false;
  public boolean will_stop = false;

  public Sound(Context paramContext, MediaPlayer[] paramArrayOfMediaPlayer)
  {
    this.sound = paramArrayOfMediaPlayer;
    new Thread(this).start();
  }

  private void logic()
  {
    if (this.will_start)
    {
      this.sound[this.playCount].start();
      this.will_start = false;
    }
    if (!this.will_stop)
      return;
    this.sound[this.stopCount].stop();
    this.will_stop = false;
  }

  public void play(int paramInt)
  {
    this.playCount = paramInt;
    this.will_start = true;
  }

  public void run()
  {
    try
    {
      logic();
      Thread.sleep(50L);
    }
    catch (Exception localException)
    {
    }
  }

  public void stop(int paramInt)
  {
    this.stopCount = paramInt;
    this.will_stop = true;
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.Sound
 * JD-Core Version:    0.5.4
 */