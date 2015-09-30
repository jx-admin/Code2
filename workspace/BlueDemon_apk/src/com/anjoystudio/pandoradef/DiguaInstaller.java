package com.anjoystudio.pandoradef;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DiguaInstaller
{
  private static final String APK_FILE = "digua.apk";
  private static final String DEFAULT_DL_SUBDIR = "/downjoy";
  private File file;
  private Context mContext;

  public DiguaInstaller(Context paramContext)
  {
    this.mContext = paramContext;
  }

  private void startInstall()
  {
    Intent localIntent = new Intent();
    localIntent.addFlags(268435456);
    localIntent.setAction("android.intent.action.VIEW");
    localIntent.setDataAndType(Uri.fromFile(this.file), "application/vnd.android.package-archive");
    this.mContext.startActivity(localIntent);
  }

  public void installDigua(FileInstallListener paramFileInstallListener)
  {
    if (Environment.getExternalStorageState().equals("mounted"))
    {
      File localFile = new File(Environment.getExternalStorageDirectory().getPath() + "/downjoy");
      if (!localFile.isDirectory())
        localFile.mkdirs();
      this.file = new File(localFile.getAbsolutePath() + File.separator + "digua.apk");
      try
      {
        if (this.file.exists())
          this.file.delete();
        this.file.createNewFile();
        BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(this.file)));
        InputStream localInputStream = this.mContext.getAssets().open("digua.apk");
        byte[] arrayOfByte = new byte[512];
        int i = localInputStream.read(arrayOfByte);
        if (i <= 0)
        {
          localInputStream.close();
          localBufferedOutputStream.close();
          paramFileInstallListener.performComplete();
          startInstall();
          return;
        }
        localBufferedOutputStream.write(arrayOfByte, 0, i);
      }
      catch (IOException localIOException)
      {
        paramFileInstallListener.performFail();
        localIOException.printStackTrace();
      }
    }
    else
    {
      paramFileInstallListener.performFail();
    }
  }

  public static abstract interface FileInstallListener
  {
    public abstract void performComplete();

    public abstract void performFail();
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.DiguaInstaller
 * JD-Core Version:    0.5.4
 */