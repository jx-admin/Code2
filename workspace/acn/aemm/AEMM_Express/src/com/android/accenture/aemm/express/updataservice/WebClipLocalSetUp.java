package com.android.accenture.aemm.express.updataservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;

import com.android.accenture.aemm.express.R;

public class WebClipLocalSetUp {
	private static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
	
	/**添加快捷方式*/
	public static int addUrlShort(Context context,String name,String url,String iconStr){
		int ret = -1;
		Uri myBlogUri = Uri.parse(url);
		Intent urlIntent= new Intent(Intent.ACTION_VIEW, myBlogUri);
		
		Intent installShortCut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		installShortCut.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);
		Bitmap icon=null;
	    byte[] temp = Base64.decode(iconStr,0);
	    InputStream is  = new ByteArrayInputStream(temp);
	    icon = BitmapFactory.decodeStream(is);
		
		if(icon==null){
			BitmapDrawable bmpDraw=(BitmapDrawable)context.getResources().getDrawable(R.drawable.webclipicon);
			icon=bmpDraw.getBitmap();
		}

		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,urlIntent);
		context.sendBroadcast(installShortCut);
		
		return ret;
	}
	
	/**删除快捷方式*/
	public static void delUrlShort(Context context,String name,String url,String iconStr){
		Uri myBlogUri = Uri.parse(url);
		Intent urlIntent= new Intent(Intent.ACTION_VIEW, myBlogUri);
		
		Intent installShortCut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		installShortCut.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);
	
	  	//decode
		byte[] temp = Base64.decode(iconStr,0);
		InputStream is  = new ByteArrayInputStream(temp);
		Bitmap icon = BitmapFactory.decodeStream(is);
		
	
		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
		installShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,urlIntent);
		context.sendBroadcast(installShortCut);
	}
	
	//read shortcut from launcher.db by shortcut name. 
	//Judge whether there is the shortcut in desktop. for fix bug2835. by cuixiaowei 20110825
	public static boolean hasShortcut(Context context,String webClipName)  
    {  
        boolean isInstallShortcut = false;  
        final String AUTHORITY ="com.android.launcher2.settings";  
        final Uri CONTENT_URI = Uri.parse("content://" +AUTHORITY + "/favorites?notify=true");  
        Cursor c = context.getContentResolver().query(CONTENT_URI,new String[] {"title","intent" },"title=?", new String[] {webClipName}, null);  
        if(c!=null && c.getCount()>0){  
            isInstallShortcut = true ;  
        }  
        return isInstallShortcut ;  
    } 
}
