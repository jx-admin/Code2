package org.accenture.product.lemonade.model;

import java.io.Serializable;

public class SceneBean implements LemonadeBean, Serializable
{
	public class DateBase
	{
		public static final String TABLENAME = "scene";
		public static final String _ID = "_id";
		public static final String COLUMN_INFO = "info";
		public static final String COLUMN_ICONS_ID = "icon_id";
		public static final String COLUMN_WALLPAPER_ID = "wallpaper_id";
		public static final String COLUMN_ISUSE = "is_use";// 1代表正在使用
		public static final String COLUMN_SCREEN_COUNT = "screen_count";
		public static final String COLUMN_PAGEPATH = "pagepath";
		public static final String COLUMN_THUMPATH = "thumpath";
	}

	/**
	 *场景id
	 */
	private int _id;
	/**
	 * 场景描述
	 */
	private String _Info;

	/**
	 * 墙纸id
	 */
	private int _WallpaperId;

	/**
	 * icon包id
	 */
	private int _IconsId;

	/**
	 * 屏数
	 */
	private int _ScreenCount;

	/**
	 * 是否在用
	 */
	private boolean _IsUse;
	
	private String _PagePath;

	private String _ThumPath; // Thumbnail path;
	
	public String getPagePath(){
		return _PagePath;
	}
	
	public void setPagePath(String pagePath){
		_PagePath=pagePath;
	}
	
	public String getThumPath(){
		return _ThumPath;
	}
	
	public void setThumPath(String thumPath){
		_ThumPath=thumPath;
	}

	public int getScreenCount()
	{
		return _ScreenCount;
	}

	public void setScreenCount(int screenCount)
	{
		this._ScreenCount = screenCount;
	}

	public boolean isUse()
	{
		return _IsUse;
	}

	public void setUse(boolean isUse)
	{
		this._IsUse = isUse;
	}

	public int getWallpaperId()
	{
		return _WallpaperId;
	}

	public void setWallpaperId(int wallpaperId)
	{
		this._WallpaperId = wallpaperId;
	}

	public int getIconsId()
	{
		return _IconsId;
	}

	public void setIconsId(int iconsId)
	{
		this._IconsId = iconsId;
	}

	public int getId()
	{
		return _id;
	}

	public void setId(int id)
	{
		this._id = id;
	}

	public String getInfo()
	{
		return _Info;
	}

	public void setInfo(String info)
	{
		this._Info = info;
	}

}
