package com.anjoystudio.pandoradef;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ArchiveUitl
{
  public static final String MUSIC_VALUE = "music_value";
  public static final String SOUND_EFFECTS = "sound_effects";
  public static final String STAGE_SCORE = "stage_score";
  public static final String STAGE_VALUE = "stage_value";
  private static ArchiveUitl archiveUitl;
  private SharedPreferences.Editor editor;
  private SharedPreferences preference;

  private ArchiveUitl(Context paramContext)
  {
    this.preference = ((Activity)paramContext).getPreferences(0);
    this.editor = this.preference.edit();
  }

  public static ArchiveUitl newInstance(Context paramContext)
  {
    ArchiveUitl localArchiveUitl;
    if (archiveUitl == null)
    {
      localArchiveUitl = new ArchiveUitl(paramContext);
      archiveUitl = localArchiveUitl;
    }
    while (true)
    {
      return localArchiveUitl;
      localArchiveUitl = archiveUitl;
    }
  }

  public int getInt(String paramString)
  {
    return this.preference.getInt(paramString, 0);
  }

  public void saveInt(String paramString, int paramInt)
  {
    this.editor.putInt(paramString, paramInt);
    this.editor.commit();
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.ArchiveUitl
 * JD-Core Version:    0.5.4
 */