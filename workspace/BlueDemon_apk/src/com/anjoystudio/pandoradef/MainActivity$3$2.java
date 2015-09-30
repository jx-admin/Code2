package com.anjoystudio.pandoradef;

import android.app.AlertDialog.Builder;
import android.os.Handler;

class MainActivity$3$2
  implements DiguaInstaller.FileInstallListener
{
  public void performComplete()
  {
    this.val$progHandler.sendMessage(this.val$progHandler.obtainMessage());
  }

  public void performFail()
  {
    this.val$progHandler.sendMessage(this.val$progHandler.obtainMessage());
    new AlertDialog.Builder(MainActivity.3.access$0(this.this$1)).setMessage(MainActivity.3.access$0(this.this$1).getString(2131034120)).setIcon(17301570).setNegativeButton(2131034119, new MainActivity.3.2.1(this)).show();
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.MainActivity.3.2
 * JD-Core Version:    0.5.4
 */