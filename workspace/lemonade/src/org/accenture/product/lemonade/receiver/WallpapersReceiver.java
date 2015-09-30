package org.accenture.product.lemonade.receiver;

import org.accenture.product.lemonade.Launcher;
import org.accenture.product.lemonade.SettingActivity;
import org.accenture.product.lemonade.content.WallpapersDateBaseAdapter;
import org.accenture.product.lemonade.model.WallpapersBean;
import org.accenture.product.lemonade.util.ResourcesUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WallpapersReceiver extends BroadcastReceiver
{

	public static final String EXTRAS_KEY = "wallpapers_key";
	public static final String UPDATE_WALLPAPER = "org.accenture.product.lemonade.update_wallpapers";

	@Override
	public void onReceive(Context arg0, Intent arg1)
	{
		String action = arg1.getAction();
		WallpapersBean bean = (WallpapersBean) arg1.getExtras().get(EXTRAS_KEY);
		WallpapersDateBaseAdapter adapter = WallpapersDateBaseAdapter.getInstance();
		
		if (UPDATE_WALLPAPER.equals(action))
		{	
			adapter.open();
			adapter.update(bean);
			ResourcesUtil.getInstance()._CurrentWallpapers = bean;
			
			Launcher.luanchr.setWallpapers(bean);
			adapter.close();
			
		}

	}

}
