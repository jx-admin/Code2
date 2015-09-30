package org.accenture.product.lemonade.receiver;

import org.accenture.product.lemonade.Launcher;
import org.accenture.product.lemonade.SettingActivity;
import org.accenture.product.lemonade.content.SceneDateBaseAdapter;
import org.accenture.product.lemonade.model.SceneBean;
import org.accenture.product.lemonade.util.ResourcesUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SceneReceiver extends BroadcastReceiver
{

	public static final String EXTRAS_KEY = "scene_key";
	public static final String UPDATE_SCENE = "org.accenture.product.lemonade.update_scene";

	@Override
	public void onReceive(Context arg0, Intent arg1)
	{
		String action = arg1.getAction();
		SceneBean bean = (SceneBean) arg1.getExtras().get(EXTRAS_KEY);
		SceneDateBaseAdapter adapter = SceneDateBaseAdapter.getInstance();

		if (UPDATE_SCENE.equals(action))
		{
			//更改当前场景---------begin
			adapter.open();
			adapter.update(bean);
			ResourcesUtil.getInstance()._CurrentSceneBean = bean;
			adapter.close();
			Launcher.luanchr.setScene();
			//更改当前场景---------end
			
			//更改当前icon
			
			//更改当前icon
			
		}

	}

}
