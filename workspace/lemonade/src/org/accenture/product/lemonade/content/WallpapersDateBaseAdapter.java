package org.accenture.product.lemonade.content;

import java.util.Vector;

import org.accenture.product.lemonade.model.LemonadeBean;
import org.accenture.product.lemonade.model.WallpapersBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WallpapersDateBaseAdapter implements DataBaseAdapter
{

	private static WallpapersDateBaseAdapter adapter;

	private LemonadeSQliteHelper _Helper;
	private SQLiteDatabase _SQLiteDatabase;

	private WallpapersDateBaseAdapter()
	{
		_Helper = new LemonadeSQliteHelper();
	}

	public static WallpapersDateBaseAdapter getInstance()
	{
		if (adapter == null)
		{
			adapter = new WallpapersDateBaseAdapter();
		}
		return adapter;
	}

	@Override
	public void open()
	{
		_SQLiteDatabase = _Helper.getWritableDatabase();
	}

	@Override
	public void close()
	{
		_SQLiteDatabase.close();
	}

	@Override
	public long insert(LemonadeBean bean)
	{

		if (bean instanceof WallpapersBean)
		{
			WallpapersBean w = (WallpapersBean) bean;
			ContentValues c = new ContentValues();
			c.put(WallpapersBean.DateBase.COLUMN_PAGENAME, w.get_PageName());
			c.put(WallpapersBean.DateBase.COLUMN_DOWNLOAD, w.get_download());
			c.put(WallpapersBean.DateBase.COLUMN_PAGEPATH, w.get_PagePath());
			c.put(WallpapersBean.DateBase.COLUMN_THUMPATH, w.get_ThumPath());
			c.put(WallpapersBean.DateBase.COLUMN_TYPE, String.valueOf(w.get_WallpapersType()));
			c.put(WallpapersBean.DateBase.COLUMN_USE, w.get_Use());

			return _SQLiteDatabase.insert(WallpapersBean.DateBase.TABLENAME, null, c);
		}
		return -1;
	}

	@Override
	public void update(LemonadeBean bean)
	{
		WallpapersBean b = (WallpapersBean) bean;
		String sql = "update " + WallpapersBean.DateBase.TABLENAME + " set " + WallpapersBean.DateBase.COLUMN_USE + " = 0";
		_SQLiteDatabase.execSQL(sql);

		sql = "update " + WallpapersBean.DateBase.TABLENAME + " set " + WallpapersBean.DateBase.COLUMN_USE + " = 1" + " where "
				+ WallpapersBean.DateBase.COLUMN_ID + " = " + b.get_Id();
		_SQLiteDatabase.execSQL(sql);
	}

	@Override
	public void delete(int id)
	{
		_SQLiteDatabase.delete(WallpapersBean.DateBase.TABLENAME, WallpapersBean.DateBase._ID + " = " + id, null);
	}

	@Override
	public Vector<LemonadeBean> select()
	{
		int index;
		String sql = "SELECT * FROM " + WallpapersBean.DateBase.TABLENAME;
		Cursor c = _SQLiteDatabase.rawQuery(sql, null);
		Vector<LemonadeBean> vec = new Vector<LemonadeBean>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			WallpapersBean bean = new WallpapersBean();
			index = c.getColumnIndex(WallpapersBean.DateBase._ID);
			bean.set_Id(c.getInt(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_ID);
			bean.set_WallpapersId(c.getString(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_TYPE);
			bean.set_WallpapersType(WallpapersBean.WallpapersEnum.doIt(c.getInt(index)));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_USE);
			bean.set_Use(c.getInt(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_PAGENAME);
			bean.set_PageName(c.getString(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_PAGEPATH);
			bean.set_PagePath(c.getString(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_THUMPATH);
			bean.set_ThumPath(c.getString(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_DOWNLOAD);
			bean.set_download(c.getInt(index));

			vec.add(bean);
		}
		c.close();
		return vec;
	}

	public WallpapersBean getUseBean()
	{
		String sql = "SELECT * FROM " + WallpapersBean.DateBase.TABLENAME + " where " + WallpapersBean.DateBase.COLUMN_USE
				+ "=1";
		Cursor c = _SQLiteDatabase.rawQuery(sql, null);
		WallpapersBean bean = new WallpapersBean();
		int index;
		if (c.moveToFirst())
		{

			index = c.getColumnIndex(WallpapersBean.DateBase._ID);
			bean.set_Id(c.getInt(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_ID);
			bean.set_WallpapersId(c.getString(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_TYPE);
			bean.set_WallpapersType(WallpapersBean.WallpapersEnum.doIt(c.getInt(index)));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_USE);
			bean.set_Use(c.getInt(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_PAGENAME);
			bean.set_PageName(c.getString(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_PAGEPATH);
			bean.set_PagePath(c.getString(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_THUMPATH);
			bean.set_ThumPath(c.getString(index));

			index = c.getColumnIndex(WallpapersBean.DateBase.COLUMN_DOWNLOAD);
			bean.set_download(c.getInt(index));
		}
		c.close();
		return bean;
	}
}
