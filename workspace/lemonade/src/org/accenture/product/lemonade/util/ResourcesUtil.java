package org.accenture.product.lemonade.util;

import java.util.Vector;

import org.accenture.product.lemonade.content.SceneDateBaseAdapter;
import org.accenture.product.lemonade.content.WallpapersDateBaseAdapter;
import org.accenture.product.lemonade.model.LemonadeBean;
import org.accenture.product.lemonade.model.SceneBean;
import org.accenture.product.lemonade.model.WallpapersBean;

/**
 * 
 * @author zheng.zhao
 */
public class ResourcesUtil
{

	private static final ResourcesUtil util = new ResourcesUtil();

	public WallpapersBean _CurrentWallpapers;

	public SceneBean _CurrentSceneBean;

	public Vector<LemonadeBean> wallPaperList;
	public Vector<LemonadeBean> iconList;
	public Vector<LemonadeBean> sceneList;

	private ResourcesUtil()
	{
		SceneDateBaseAdapter scene = SceneDateBaseAdapter.getInstance();
		scene.open();
		_CurrentSceneBean = scene.getCurrentScene();
		scene.close();
	}

	public static ResourcesUtil getInstance()
	{

		return util;
	}

	public void refresh()
	{
		WallpapersDateBaseAdapter wall = WallpapersDateBaseAdapter.getInstance();
		wall.open();
		_CurrentWallpapers = wall.getUseBean();
		wallPaperList = wall.select();
		wall.close();

		SceneDateBaseAdapter scene = SceneDateBaseAdapter.getInstance();
		scene.open();
		_CurrentSceneBean = scene.getCurrentScene();
		sceneList = scene.select();
		scene.close();
		
	}

}
