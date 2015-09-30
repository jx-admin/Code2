package com.zrh.goldfree;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Daohang extends Activity
{
  private WebView webView;

  private void showAbout()
  {
    String str = null;
    try
    {
      InputStream localInputStream = getAssets().open("daohang.txt");
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      int i = localInputStream.read();
      if (i == -1)
      {
        str = localByteArrayOutputStream.toString();
        localInputStream.close();
        label44: this.webView.loadDataWithBaseURL("", str, "text/html", "utf-8", "");
        return;
      }
      localByteArrayOutputStream.write(i);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      break label44:
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903050);
    this.webView = ((WebView)findViewById(2131230745));
    this.webView.getSettings().setJavaScriptEnabled(true);
    this.webView.getSettings().setSaveFormData(true);
    this.webView.getSettings().setSavePassword(true);
    this.webView.getSettings().setSupportZoom(true);
    this.webView.getSettings().setBuiltInZoomControls(true);
    showAbout();
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.Daohang
 * JD-Core Version:    0.5.4
 */