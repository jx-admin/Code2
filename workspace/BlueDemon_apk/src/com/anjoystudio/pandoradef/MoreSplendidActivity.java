package com.anjoystudio.pandoradef;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MoreSplendidActivity extends Activity
{
  static MoreSplendidActivity msa;
  MoreSplendidView msv;

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
    msa = this;
    this.msv = new MoreSplendidView(this);
    setContentView(this.msv);
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.MoreSplendidActivity
 * JD-Core Version:    0.5.4
 */