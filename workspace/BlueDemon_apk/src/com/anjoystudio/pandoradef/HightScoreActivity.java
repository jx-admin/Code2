package com.anjoystudio.pandoradef;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class HightScoreActivity extends Activity
{
  static HightScoreActivity hsa;
  HightScoreView hsv;

  public void exit()
  {
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setRequestedOrientation(0);
    hsa = this;
    this.hsv = new HightScoreView(this);
    setContentView(this.hsv);
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.HightScoreActivity
 * JD-Core Version:    0.5.4
 */