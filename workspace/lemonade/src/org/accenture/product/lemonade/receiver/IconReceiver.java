package org.accenture.product.lemonade.receiver;

import java.util.Vector;

import org.accenture.product.lemonade.R;
import org.accenture.product.lemonade.util.ResourcesUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class IconReceiver extends BroadcastReceiver
{

	public static final String EXTRAS_KEY = "icon_key";
	public static final String UPDATE_WALLPAPER = "org.accenture.product.lemonade.update_icon";

	@Override
	public void onReceive(Context context, Intent arg1)
	{
		
	}

}
