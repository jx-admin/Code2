package org.accenture.product.lemonade.model;

import java.io.Serializable;

/**
 * 
 * @author zheng.zhao
 * 
 * 
 */
public class WallpapersBean implements LemonadeBean ,Serializable
{
	
	private static final long serialVersionUID = -4631751911334209724L;

	private int _Id; // PRIMARY KEY

	private String _WallpapersId;

	private WallpapersEnum _WallpapersType;

	private int _Use; // 1 or 0 , 1 is Use

	private String _PageName;

	private String _PagePath;

	private String _ThumPath; // Thumbnail path;

	private int _download = 1; // 1 or 0 , 1 is true

	public int get_Id()
	{
		return _Id;
	}

	public void set_Id(int _Id)
	{
		this._Id = _Id;
	}

	public String get_WallpapersId()
	{
		return _WallpapersId;
	}

	public void set_WallpapersId(String _WallpapersId)
	{
		this._WallpapersId = _WallpapersId;
	}

	public WallpapersEnum get_WallpapersType()
	{
		return _WallpapersType;
	}

	public void set_WallpapersType(WallpapersEnum _WallpapersType)
	{
		this._WallpapersType = _WallpapersType;
	}

	public int get_Use()
	{
		return _Use;
	}

	public void set_Use(int _Use)
	{
		this._Use = _Use;
	}

	public String get_PageName()
	{
		return _PageName;
	}

	public void set_PageName(String _PageName)
	{
		this._PageName = _PageName;
	}

	public String get_PagePath()
	{
		return _PagePath;
	}

	public void set_PagePath(String _PagePath)
	{
		this._PagePath = _PagePath;
	}

	public String get_ThumPath()
	{
		return _ThumPath;
	}

	public void set_ThumPath(String _ThumPath)
	{
		this._ThumPath = _ThumPath;
	}

	public int get_download()
	{
		return _download;
	}

	public void set_download(int _download)
	{
		this._download = _download;
	}

	public enum WallpapersEnum // 1��2��3
	{
		LiveWallpapers(1), MediaGallery(2), Wallpapers(3);

		private final int value;

		WallpapersEnum(int value)
		{
			this.value = value;
		}

		public int getValue()
		{
			return value;
		}

		public static WallpapersEnum doIt(int v)
		{
			switch (v)
			{
				case 1:
					return WallpapersEnum.LiveWallpapers;
				case 2:
					return WallpapersEnum.MediaGallery;
				case 3:
					return WallpapersEnum.Wallpapers;
			}
			return null;
		}
	}

	public class DateBase
	{
		public static final String TABLENAME = "table_wallpapers";
		public static final String _ID = "_id";
		public static final String COLUMN_ID = "wallpapersId";
		public static final String COLUMN_TYPE = "wallpaperstype";
		public static final String COLUMN_USE = "use";
		public static final String COLUMN_PAGENAME = "pagename";
		public static final String COLUMN_PAGEPATH = "pagepath";
		public static final String COLUMN_THUMPATH = "thumPath";
		public static final String COLUMN_DOWNLOAD = "download";
	}
}
