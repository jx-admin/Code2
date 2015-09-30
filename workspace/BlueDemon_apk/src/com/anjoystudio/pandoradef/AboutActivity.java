package com.anjoystudio.pandoradef;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class AboutActivity extends Activity
{
  static AboutActivity aa;
  AboutView av;

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
    aa = this;
    this.av = new AboutView(this);
    setContentView(this.av);
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.AboutActivity
 * JD-Core Version:    0.5.4
 */