package com.anjoystudio.pandoradef;

import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.media.MediaPlayer;
import android.os.Vibrator;

class GameView$1
  implements DialogInterface.OnMultiChoiceClickListener
{
  public void onClick(DialogInterface paramDialogInterface, int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
      if (paramInt == 0)
      {
        GameView.is_playmusic = true;
        if (this.this$0.game_pause);
      }
    try
    {
      GameView.mp_music.prepare();
      label28: GameView.mp_music.start();
      while (true)
      {
        return;
        if (paramInt != 1)
          continue;
        GameView.is_shake = true;
        GameView.vibrator.vibrate(200L);
        continue;
        if (paramInt != 0)
          break;
        GameView.is_playmusic = false;
        GameView.mp_music.pause();
      }
      GameView.is_shake = false;
    }
    catch (Exception localException)
    {
      break label28:
    }
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.GameView.1
 * JD-Core Version:    0.5.4
 */