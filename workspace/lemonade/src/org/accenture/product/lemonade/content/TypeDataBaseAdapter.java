package org.accenture.product.lemonade.content;

import java.util.Vector;

import org.accenture.product.lemonade.model.LemonadeBean;
import org.accenture.product.lemonade.model.TypeBean;
import org.accenture.product.lemonade.model.WidgetBean;
import org.accenture.product.lemonade.model.WidgetBean.WidgetEnum;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TypeDataBaseAdapter implements DataBaseAdapter
{

	private static TypeDataBaseAdapter adapter;

	private LemonadeSQliteHelper _Helper;
	private SQLiteDatabase _SQLiteDatabase;

	private TypeDataBaseAdapter()
	{
		_Helper = new LemonadeSQliteHelper();
	}

	public static TypeDataBaseAdapter getInstance()
	{
		if (adapter == null)
		{
			adapter = new TypeDataBaseAdapter();
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
	public void delete(int id)
	{

		_SQLiteDatabase.delete(WidgetBean.DateBase.TABLENAME, WidgetBean.DateBase._ID + "=" + id, null);
		// TODO Auto-generated method stub

	}

	@Override
	public long insert(LemonadeBean bean)
	{
		if (bean instanceof TypeBean)
		{

			TypeBean b = (TypeBean) bean;
			ContentValues cv = new ContentValues();

			cv.put(TypeBean.DateBase.COLUMN_ID, b.getWidgetId());
			cv.put(TypeBean.DateBase.COLUMN_X, b.getX());
			cv.put(TypeBean.DateBase.COLUMN_Y, b.getY());
			cv.put(TypeBean.DateBase.COLUMN_SPANX, b.getSpanX());
			cv.put(TypeBean.DateBase.COLUMN_SPANY, b.getSpanY());
			cv.put(TypeBean.DateBase.COLUMN_SCREEN, b.getScreen());
			cv.put(TypeBean.DateBase.COLUMN_TYPE, b.getType().getValue());
			return _SQLiteDatabase.insert(WidgetBean.DateBase.TABLENAME, null, cv);

		}
		// TODO Auto-generated method stub
		return 0;
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

	@Override
	public void update(LemonadeBean bean)
	{
		WidgetBean b = (WidgetBean) bean;

		ContentValues contentValues = new ContentValues();
		contentValues.put(WidgetBean.DateBase.COLUMN_X, b.getX());
		contentValues.put(WidgetBean.DateBase.COLUMN_Y, b.getY());
		contentValues.put(WidgetBean.DateBase.COLUMN_SCREEN, b.getScreen());
		String whereClause = WidgetBean.DateBase._ID + "=?";
		String[] whereArgs = new String[]
		{ Integer.toString(b.getId()) };
		int result = _SQLiteDatabase.update(WidgetBean.DateBase.TABLENAME, contentValues, whereClause, whereArgs);
	}

}
