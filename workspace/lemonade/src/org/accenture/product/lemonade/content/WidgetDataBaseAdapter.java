package org.accenture.product.lemonade.content;

import java.util.ArrayList;
import java.util.Vector;

import org.accenture.product.lemonade.model.LemonadeBean;
import org.accenture.product.lemonade.model.WidgetBean;
import org.accenture.product.lemonade.model.WidgetBean.WidgetEnum;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WidgetDataBaseAdapter implements DataBaseAdapter
{

	private static WidgetDataBaseAdapter adapter;
	private LemonadeSQliteHelper _Helper;
	private SQLiteDatabase _SQLiteDatabase;

	private WidgetDataBaseAdapter()
	{
		_Helper = new LemonadeSQliteHelper();
	}

	public static WidgetDataBaseAdapter getInstance()
	{

		if (adapter == null)
		{
			adapter = new WidgetDataBaseAdapter();
		}
		return adapter;
	}

	/**
	 */
	public void open()
	{
		_Helper = new LemonadeSQliteHelper();
		_SQLiteDatabase = _Helper.getWritableDatabase();
	}

	/**
	 */
	public void close()
	{
		_Helper.close();
	}

	@Override
	public long insert(LemonadeBean bean)
	{
		WidgetBean b = (WidgetBean) bean;
		ContentValues cv = new ContentValues();

		cv.put(WidgetBean.DateBase.COLUMN_ID, b.getWidgetId());
		cv.put(WidgetBean.DateBase.COLUMN_X, b.getX());
		cv.put(WidgetBean.DateBase.COLUMN_Y, b.getY());
		cv.put(WidgetBean.DateBase.COLUMN_SPANX, b.getSpanX());
		cv.put(WidgetBean.DateBase.COLUMN_SPANY, b.getSpanY());
		cv.put(WidgetBean.DateBase.COLUMN_SCREEN, b.getScreen());
		cv.put(WidgetBean.DateBase.COLUMN_TYPE, b.getType().getValue());
		cv.put(WidgetBean.DateBase.COLUMN_SCENE, b.getScene());
		return _SQLiteDatabase.insert(WidgetBean.DateBase.TABLENAME, null, cv);
	}

	public void insert(int widgetId, int x, int y, int spanX, int spanY, int screen, WidgetBean.WidgetEnum type)
	{
		ContentValues cv = new ContentValues();

		cv.put(WidgetBean.DateBase.COLUMN_ID, widgetId);
		cv.put(WidgetBean.DateBase.COLUMN_X, x);
		cv.put(WidgetBean.DateBase.COLUMN_Y, y);
		cv.put(WidgetBean.DateBase.COLUMN_SPANX, spanX);
		cv.put(WidgetBean.DateBase.COLUMN_SPANY, spanY);
		cv.put(WidgetBean.DateBase.COLUMN_TYPE, type.getValue());
		cv.put(WidgetBean.DateBase.COLUMN_SCREEN, screen);
		long id = _SQLiteDatabase.insert(WidgetBean.DateBase.TABLENAME, null, cv);
	}

	@Override
	public void update(LemonadeBean bean)
	{
		WidgetBean b = (WidgetBean) bean;
		// String sql = "update " + WidgetBean.DateBase.TABLENAME + " set " +
		// WidgetBean.DateBase.COLUMN_X + " = " + b.getX()
		// + ", " + WidgetBean.DateBase.COLUMN_Y + " = " + b.getY() + "," +
		// WidgetBean.DateBase.COLUMN_SCREEN + " = "
		// + b.getScreen() + "," + WidgetBean.DateBase.COLUMN_TYPE + " = " +
		// b.getType().getValue() + " where "
		// + WidgetBean.DateBase._ID + " = " + b.getId();
		// _SQLiteDatabase.execSQL(sql);

		ContentValues contentValues = new ContentValues();
		// contentValues.put(WidgetBean.DateBase._ID, b.getId());
		// contentValues.put(WidgetBean.DateBase.COLUMN_ID, b.getWidgetId());
		contentValues.put(WidgetBean.DateBase.COLUMN_X, b.getX());
		contentValues.put(WidgetBean.DateBase.COLUMN_Y, b.getY());
		contentValues.put(WidgetBean.DateBase.COLUMN_SCREEN, b.getScreen());
		String whereClause = WidgetBean.DateBase._ID + "=?";
		String[] whereArgs = new String[]
		{ Integer.toString(b.getId()) };
		int result = _SQLiteDatabase.update(WidgetBean.DateBase.TABLENAME, contentValues, whereClause, whereArgs);
		// System.out.println(result);
	}

	// public void update()

	public void update(int x, int y, int screen, int id, WidgetBean.WidgetEnum type)
	{
		String sql = "update " + WidgetBean.DateBase.TABLENAME + " set " + WidgetBean.DateBase.COLUMN_X + " = " + x + ", "
				+ WidgetBean.DateBase.COLUMN_Y + " = " + y + "," + WidgetBean.DateBase.COLUMN_SCREEN + " = " + screen + ","
				+ WidgetBean.DateBase.COLUMN_TYPE + " = " + type.getValue() + " where " + WidgetBean.DateBase._ID + " = " + id;
		_SQLiteDatabase.execSQL(sql);
	}

	@Override
	public void delete(int id)
	{
		_SQLiteDatabase.delete(WidgetBean.DateBase.TABLENAME, WidgetBean.DateBase._ID + "=" + id, null);
	}

	/**
	 * 删除一类场景里的某一屏的所有数据
	 * 
	 * @param screenCount删除之前屏幕数量
	 * @param sceneId
	 * @param screen
	 */
	public void deleteByScreenAndScene(int screenCount, int sceneId, int screen)
	{
		String where = WidgetBean.DateBase.COLUMN_SCREEN + " = ? and " + WidgetBean.DateBase.COLUMN_SCENE + " = ?";
		String args[] =
		{ String.valueOf(screen), String.valueOf(sceneId) };
		_SQLiteDatabase.delete(WidgetBean.DateBase.TABLENAME, where, args);
		updateScreenIndex(sceneId, screen, false);
	}

	public void addScreenByScene(int sceneId)
	{

		updateScreenIndex(sceneId, -1, true);
	}

	/**
	 * 
	 * 
	 */
	/**
	 * @param nowScreenCount
	 *            当前屏幕数量
	 * @param index
	 *            被删除的屏幕index
	 * @param upcase
	 *            ture means screen++
	 */
	private void updateScreenIndex(int sceneId, int index, boolean upcase)
	{
		String selection = WidgetBean.DateBase.COLUMN_SCREEN + " > ? and " + WidgetBean.DateBase.COLUMN_SCENE + " = ?";
		String selectionArgs[] =
		{ String.valueOf(index), String.valueOf(sceneId) };
		Cursor c = _SQLiteDatabase.query(WidgetBean.DateBase.TABLENAME, null, selection, selectionArgs, null, null, null);

		ArrayList<int[]> list = new ArrayList<int[]>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			int[] id_index = new int[2];
			int idIndex = c.getColumnIndex(WidgetBean.DateBase._ID);
			int screenIndex = c.getColumnIndex(WidgetBean.DateBase.COLUMN_SCREEN);

			id_index[0] = c.getInt(idIndex);
			id_index[1] = c.getInt(screenIndex);
			list.add(id_index);
		}
		c.close();
		for (int[] id_index1 : list)
		{
			doUpdateScreenIndex(id_index1, upcase);

		}

	}

	public void doUpdateScreenIndex(int[] id_index, boolean upcase)
	{

		String selection = WidgetBean.DateBase._ID + " =?";
		String selectionArgs[] =
		{ String.valueOf(id_index[0]) };
		ContentValues values = new ContentValues();
		if (upcase)
		{
			values.put(WidgetBean.DateBase.COLUMN_SCREEN, id_index[1] + 1);

		}
		else
		{
			values.put(WidgetBean.DateBase.COLUMN_SCREEN, id_index[1] - 1);
		}
		_SQLiteDatabase.update(WidgetBean.DateBase.TABLENAME, values, selection, selectionArgs);

	}

	@Override
	public Vector<LemonadeBean> select()
	{
		int index;
		String sql = "SELECT * FROM " + WidgetBean.DateBase.TABLENAME;
		Cursor c = _SQLiteDatabase.rawQuery(sql, null);
		Vector<LemonadeBean> vec = new Vector<LemonadeBean>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			WidgetBean bean = new WidgetBean();
			index = c.getColumnIndex(WidgetBean.DateBase._ID);
			bean.setId(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_ID);
			bean.setWidgetId(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_X);
			bean.setX(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_Y);
			bean.setY(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_SPANX);
			bean.setSpanX(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_SPANY);
			bean.setSpanY(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_SCREEN);
			bean.setScreen(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_TYPE);
			bean.setType(WidgetEnum.doIt(c.getInt(index)));

			vec.add(bean);
		}
		return vec;
	}

	public Vector<LemonadeBean> select(long sceneid)
	{
		int index;
		String sql = "SELECT * FROM " + WidgetBean.DateBase.TABLENAME + " where scene=" + sceneid;
		Cursor c = _SQLiteDatabase.rawQuery(sql, null);
		Vector<LemonadeBean> vec = new Vector<LemonadeBean>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			WidgetBean bean = new WidgetBean();
			index = c.getColumnIndex(WidgetBean.DateBase._ID);
			bean.setId(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_ID);
			bean.setWidgetId(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_X);
			bean.setX(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_Y);
			bean.setY(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_SPANX);
			bean.setSpanX(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_SPANY);
			bean.setSpanY(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_SCREEN);
			bean.setScreen(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_TYPE);
			bean.setType(WidgetEnum.doIt(c.getInt(index)));

			vec.add(bean);
		}
		c.close();
		return vec;
	}

	public Vector<LemonadeBean> selectByScreen(int screen)
	{
		int index;
		String sql = "SELECT * FROM " + WidgetBean.DateBase.TABLENAME + " where " + WidgetBean.DateBase.COLUMN_SCREEN + " = "
				+ screen;
		Cursor c = _SQLiteDatabase.rawQuery(sql, null);
		Vector<LemonadeBean> vec = new Vector<LemonadeBean>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			WidgetBean bean = new WidgetBean();
			index = c.getColumnIndex(WidgetBean.DateBase._ID);
			bean.setId(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_ID);
			bean.setWidgetId(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_X);
			bean.setX(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_Y);
			bean.setY(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_SPANX);
			bean.setSpanX(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_SPANY);
			bean.setSpanY(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_SCREEN);
			bean.setScreen(c.getInt(index));

			index = c.getColumnIndex(WidgetBean.DateBase.COLUMN_TYPE);
			bean.setType(WidgetEnum.doIt(c.getInt(index)));

			vec.add(bean);
		}
		close();
		return vec;
	}
}
