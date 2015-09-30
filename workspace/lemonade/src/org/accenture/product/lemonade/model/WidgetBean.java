package org.accenture.product.lemonade.model;

/**
 * 
 * @author zheng.zhao
 * 
 */
public class WidgetBean implements LemonadeBean
{
	private int _Id;
	private int _WidgetId;
	private int _X;
	private int _Y;
	private int _Screen;
	private int _SpanX;
	private int _SpanY;
	private WidgetEnum type;

	private long _Scene;

	public long getScene()
	{
		return _Scene;
	}

	public void setScene(long scene)
	{
		_Scene = scene;
	}

	public WidgetEnum getType()
	{
		return type;
	}

	public void setType(WidgetEnum type)
	{
		this.type = type;
	}

	public int getSpanY()
	{
		return _SpanY;
	}

	public void setSpanY(int spanY)
	{
		_SpanY = spanY;
	}

	public int getSpanX()
	{
		return _SpanX;
	}

	public void setSpanX(int spanX)
	{
		_SpanX = spanX;
	}

	public WidgetBean()
	{

	}

	public WidgetBean(int widgetId, int x, int y, int screen, int id, int spanX, int spanY)
	{
		super();
		this._WidgetId = widgetId;
		this._X = x;
		this._Y = y;
		this._Screen = screen;
		this._Id = id;
		this._SpanX = spanX;
		this._SpanY = spanY;
	}

	public int getId()
	{
		return _Id;
	}

	public void setId(int id)
	{
		_Id = id;
	}

	public int getWidgetId()
	{
		return _WidgetId;
	}

	public void setWidgetId(int widgetId)
	{
		this._WidgetId = widgetId;
	}

	public int getX()
	{
		return _X;
	}

	public void setX(int x)
	{
		this._X = x;
	}

	public int getY()
	{
		return _Y;
	}

	public void setY(int y)
	{
		this._Y = y;
	}

	public int getScreen()
	{
		return _Screen;
	}

	public void setScreen(int screen)
	{
		this._Screen = screen;
	}

	// @Override
	// public String toString()
	// {
	// return "WidgetBean [_IconBean=" + _IconBean + ", _Id=" + _Id +
	// ", _Screen=" + _Screen + ", _WidgetId=" + _WidgetId
	// + ", _X=" + _X + ", _Y=" + _Y + "]";
	// }
	//
	// public void initIconBean(String url, String activityPage)
	// {
	//
	// if (_IconBean == null)
	// {
	// _IconBean = new IconBean(url, activityPage);
	// }
	// else
	// {
	// Log.e(Global.TAG, "WidgetBean in IconBean init error");
	// }
	// }

	public enum WidgetEnum
	{
		WidgetItem(1), AppItem(2);

		private final int value;

		WidgetEnum(int value)
		{
			this.value = value;
		}

		public int getValue()
		{
			return value;
		}

		public static WidgetEnum doIt(int v)
		{
			switch (v)
			{
				case 1:
					return WidgetEnum.WidgetItem;
				case 2:
					return WidgetEnum.AppItem;

			}
			return null;
		}
	}

	public class DateBase
	{
		public static final String TABLENAME = "table_widget";
		public static final String _ID = "_id";
		public static final String COLUMN_ID = "appwidgetId";
		public static final String COLUMN_X = "x";
		public static final String COLUMN_Y = "y";
		public static final String COLUMN_SPANX = "span_x";
		public static final String COLUMN_SPANY = "span_y";
		public static final String COLUMN_TYPE = "w_type";
		public static final String COLUMN_SCREEN = "screen"; // 所在screen 下标
		public static final String COLUMN_SCENE = "scene"; // 所在场景下标

	}

}
