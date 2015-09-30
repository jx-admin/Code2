package com.anjoystudio.pandoradef;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class MainActivity$3
  implements DialogInterface.OnClickListener
{
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    String str = this.this$0.getString(2131034118);
    MainActivity.3.1 local1 = new MainActivity.3.1(this, ProgressDialog.show(this.this$0, null, str, true));
    new DiguaInstaller(this.this$0).installDigua(new MainActivity.3.2(this, local1));
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.MainActivity.3
 * JD-Core Version:    0.5.4
 */