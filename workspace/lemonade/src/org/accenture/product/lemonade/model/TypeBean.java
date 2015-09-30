package org.accenture.product.lemonade.model;

import java.io.Serializable;

/**
 * 
 * 包括：widget ,app,icons,wallpaper,screen,所属主题id
 * @author seekting.x.zhang
 *
 */
/**
 * @author seekting.x.zhang
 * 
 */
public class TypeBean implements LemonadeBean, Serializable
{
	public class DateBase
	{
		/**
		 * 表名称
		 */
		public static final String TABLENAME = "type";
		/**
		 * id
		 */
		public static final String _ID = "_id";
		/**
		 * activity包名
		 */
		public static final String COLUMN_ACIVITYPAGENAME = "actpagename";
		/**
		 * 
		 */
		public static final String COLUMN_ICONPATH = "iconpath";
		/**
		 * widget信息
		 */
		public static final String COLUMN_ID = "appwidgetId";
		public static final String COLUMN_X = "x";
		public static final String COLUMN_Y = "y";
		public static final String COLUMN_SPANX = "span_x";
		public static final String COLUMN_SPANY = "span_y";

		/**
		 * 1widget,2app,3icons,4LiveWallpapers,5MediaGallery,6Wallpapers;
		 */
		public static final String COLUMN_TYPE = "w_type";

		/**
		 * 所属屏
		 */
		public static final String COLUMN_SCREEN = "screen";

		/**
		 * wallpaper
		 */
		public static final String COLUMN_PAGENAME = "pagename";
		public static final String COLUMN_PAGEPATH = "pagepath";
		public static final String COLUMN_THUMPATH = "thumPath";
		public static final String COLUMN_DOWNLOAD = "download";

		public static final String COLUMN_SCENEID = "scene_id";
	}

	public enum TypeEnum
	{
		WidgetItem(1), AppItem(2), iconsItem(3), LiveWallpapers(4), MediaGallery(5), Wallpapers(6);

		private final int value;

		TypeEnum(int value)
		{
			this.value = value;
		}

		public int getValue()
		{
			return value;
		}

		public static TypeEnum doIt(int v)
		{
			switch (v)
			{
				case 1:
					return TypeEnum.WidgetItem;
				case 2:
					return TypeEnum.AppItem;
				case 3:
					return TypeEnum.iconsItem;
				case 4:
					return TypeEnum.LiveWallpapers;
				case 5:
					return TypeEnum.MediaGallery;
				case 6:
					return TypeEnum.Wallpapers;

			}
			return null;
		}
	}

	private int _Id;
	/**
	 * icon
	 */
	private String _ActivtyPageName;
	private String _IconPath;

	/**
	 * app,widget
	 */
	private int _WidgetId = -1;
	private int _X = -1;
	private int _Y = -1;
	private int _Screen = -1;
	private int _SpanX = -1;
	private int _SpanY = -1;
	/**
	 * 
	 * 1widget,2app,3icons,4LiveWallpapers,5MediaGallery,6Wallpapers;
	 */
	private TypeEnum type;

	/**
	 * wallpaper
	 */
	private String _PageName;
	private String _PagePath;
	private String _ThumPath; // Thumbnail path;

	private int _download = 1; // 1 or 0 , 1 is true
	private int _SceneId = -1;

	public int getId()
	{
		return _Id;
	}

	public void setId(int id)
	{
		_Id = id;
	}

	public String getActivtyPageName()
	{
		return _ActivtyPageName;
	}

	public void setActivtyPageName(String activtyPageName)
	{
		_ActivtyPageName = activtyPageName;
	}

	public String getIconPath()
	{
		return _IconPath;
	}

	public void setIconPath(String iconPath)
	{
		_IconPath = iconPath;
	}

	public int getWidgetId()
	{
		return _WidgetId;
	}

	public void setWidgetId(int widgetId)
	{
		_WidgetId = widgetId;
	}

	public int getX()
	{
		return _X;
	}

	public void setX(int x)
	{
		_X = x;
	}

	public int getY()
	{
		return _Y;
	}

	public void setY(int y)
	{
		_Y = y;
	}

	public int getScreen()
	{
		return _Screen;
	}

	public void setScreen(int screen)
	{
		_Screen = screen;
	}

	public int getSpanX()
	{
		return _SpanX;
	}

	public void setSpanX(int spanX)
	{
		_SpanX = spanX;
	}

	public int getSpanY()
	{
		return _SpanY;
	}

	public void setSpanY(int spanY)
	{
		_SpanY = spanY;
	}

	public TypeEnum getType()
	{
		return type;
	}

	public void setType(TypeEnum type)
	{
		this.type = type;
	}

	public String getPageName()
	{
		return _PageName;
	}

	public void setPageName(String pageName)
	{
		_PageName = pageName;
	}

	public String getPagePath()
	{
		return _PagePath;
	}

	public void setPagePath(String pagePath)
	{
		_PagePath = pagePath;
	}

	public String getThumPath()
	{
		return _ThumPath;
	}

	public void setThumPath(String thumPath)
	{
		_ThumPath = thumPath;
	}

	public int getDownload()
	{
		return _download;
	}

	public void setDownload(int download)
	{
		_download = download;
	}

	public int getSceneId()
	{
		return _SceneId;
	}

	public void setSceneId(int sceneId)
	{
		_SceneId = sceneId;
	}
}
