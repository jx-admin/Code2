package org.accenture.product.lemonade.content;

import java.util.Vector;

import org.accenture.product.lemonade.model.LemonadeBean;
import org.accenture.product.lemonade.model.SceneBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * 场景数据库
 * 
 * 
 * @author seekting.x.zhang
 * 
 */
public class SceneDateBaseAdapter implements DataBaseAdapter
{
	private static SceneDateBaseAdapter adapter;
	private LemonadeSQliteHelper _Helper;
	private SQLiteDatabase _SQLiteDatabase;

	private SceneDateBaseAdapter()
	{
		_Helper = new LemonadeSQliteHelper();
	}

	public static SceneDateBaseAdapter getInstance()
	{
		if (adapter == null)
		{
			adapter = new SceneDateBaseAdapter();
		}

		return adapter;
	}

	@Override
	public void close()
	{
		_Helper.close();

	}

	@Override
	public void delete(int id)
	{

	}

	@Override
	public long insert(LemonadeBean bean)
	{

		if (bean instanceof SceneBean)
		{

		}
		return 0;
	}

	@Override
	public void open()
	{

		_SQLiteDatabase = _Helper.getWritableDatabase();
	}

	@Override
	public Vector<LemonadeBean> select()
	{
		Vector<LemonadeBean> v = new Vector<LemonadeBean>();

		Cursor c = _SQLiteDatabase.query(SceneBean.DateBase.TABLENAME, null, null, null, null, null, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{

			SceneBean sceneBean = new SceneBean();
			sceneBean.setId(c.getInt(c.getColumnIndex(SceneBean.DateBase._ID)));
			sceneBean.setIconsId(c.getInt(c.getColumnIndex(SceneBean.DateBase.COLUMN_ICONS_ID)));
			sceneBean.setWallpaperId(c.getInt(c.getColumnIndex(SceneBean.DateBase.COLUMN_WALLPAPER_ID)));
			sceneBean.setInfo(c.getString(c.getColumnIndex(SceneBean.DateBase.COLUMN_INFO)));
			sceneBean.setScreenCount(c.getInt(c.getColumnIndex(SceneBean.DateBase.COLUMN_SCREEN_COUNT)));
			sceneBean.setUse(c.getInt(c.getColumnIndex(SceneBean.DateBase.COLUMN_ISUSE)) == 1 ? true : false);

			sceneBean.setPagePath(c.getString(c.getColumnIndex(SceneBean.DateBase.COLUMN_PAGEPATH)));
			sceneBean.setThumPath(c.getString(c.getColumnIndex(SceneBean.DateBase.COLUMN_THUMPATH)));
			v.add(sceneBean);
		}
		c.close();
		return v;
	}

	/**
	 * 拿到当前场景
	 * 
	 * @return
	 */
	public SceneBean getCurrentScene()
	{

		SceneBean sceneBean = new SceneBean();

		String selection = "is_use=?";
		String[] selectionArgs = new String[]
		{ "1" };
		Cursor c = _SQLiteDatabase.query(SceneBean.DateBase.TABLENAME, null, selection, selectionArgs, null, null, null);
		if (c.moveToFirst())
		{
			sceneBean.setId(c.getInt(c.getColumnIndex(SceneBean.DateBase._ID)));
			sceneBean.setIconsId(c.getInt(c.getColumnIndex(SceneBean.DateBase.COLUMN_ICONS_ID)));
			sceneBean.setWallpaperId(c.getInt(c.getColumnIndex(SceneBean.DateBase.COLUMN_WALLPAPER_ID)));
			sceneBean.setInfo(c.getString(c.getColumnIndex(SceneBean.DateBase.COLUMN_INFO)));

			sceneBean.setScreenCount(c.getInt(c.getColumnIndex(SceneBean.DateBase.COLUMN_SCREEN_COUNT)));
			sceneBean.setUse(c.getInt(c.getColumnIndex(SceneBean.DateBase.COLUMN_ISUSE)) == 1 ? true : false);

			sceneBean.setPagePath(c.getString(c.getColumnIndex(SceneBean.DateBase.COLUMN_PAGEPATH)));
			sceneBean.setThumPath(c.getString(c.getColumnIndex(SceneBean.DateBase.COLUMN_THUMPATH)));
			if (sceneBean.getWallpaperId() <= 0)
			{
				System.out.println("没有壁纸");

			}
			c.close();
		}

		return sceneBean;
	}

	@Override
	public void update(LemonadeBean bean)
	{

		SceneBean b = (SceneBean) bean;
		String sql = "update " + SceneBean.DateBase.TABLENAME + " set " + SceneBean.DateBase.COLUMN_ISUSE + " = 0";
		_SQLiteDatabase.execSQL(sql);

		sql = "update " + SceneBean.DateBase.TABLENAME + " set " + SceneBean.DateBase.COLUMN_ISUSE + " = 1" + " where "
				+ SceneBean.DateBase._ID + " = " + b.getId();
		ContentValues contentValues = new ContentValues();
		contentValues.put("is_use", 1);
		int result = _SQLiteDatabase.update(SceneBean.DateBase.TABLENAME, contentValues, "_id=?", new String[]
		{ String.valueOf(b.getId()) });
		System.out.println(result);
		_SQLiteDatabase.execSQL(sql);

	}

	public void updateIcons(SceneBean sceneBean)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(SceneBean.DateBase.COLUMN_ICONS_ID, sceneBean.getIconsId());
		_SQLiteDatabase.update(SceneBean.DateBase.TABLENAME, contentValues, "_id=?", new String[]
		{ String.valueOf(sceneBean.getId()) });
	}

	public void addScreen(SceneBean sceneBean)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(SceneBean.DateBase.COLUMN_SCREEN_COUNT, sceneBean.getScreenCount() + 1);
		_SQLiteDatabase.update(SceneBean.DateBase.TABLENAME, contentValues, "_id=?", new String[]
		{ String.valueOf(sceneBean.getId()) });

	}

	/**
	 * 修改屏幕数量
	 * 
	 * @param bean
	 * @param value
	 */
	public void setScreenValue(LemonadeBean bean, int value)
	{
		SceneBean b = (SceneBean) bean;
		ContentValues contentValues = new ContentValues();
		contentValues.put(SceneBean.DateBase.COLUMN_SCREEN_COUNT, value);
		int result = _SQLiteDatabase.update(SceneBean.DateBase.TABLENAME, contentValues, "_id=?", new String[]
		{ String.valueOf(b.getId()) });

		System.out.println(result);
	}
}
