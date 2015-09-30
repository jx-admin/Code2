package org.accenture.product.lemonade.content;

import org.accenture.product.lemonade.Launcher;
import org.accenture.product.lemonade.model.SceneBean;
import org.accenture.product.lemonade.model.TypeBean;
import org.accenture.product.lemonade.model.WallpapersBean;
import org.accenture.product.lemonade.model.WidgetBean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author seekting.x.zhang
 * 
 */
public class LemonadeSQliteHelper extends SQLiteOpenHelper
{

	public static final String DATEBASE = "lemonade";

	private static final String WALLPAPERS_TABLE = "CREATE TABLE " + WallpapersBean.DateBase.TABLENAME + "( "
			+ WallpapersBean.DateBase._ID + " INTEGER PRIMARY KEY," + WallpapersBean.DateBase.COLUMN_ID
			+ " INTEGER NOT NULL DEFAULT -1, " + WallpapersBean.DateBase.COLUMN_TYPE + " integer,"
			+ WallpapersBean.DateBase.COLUMN_USE + " integer," + WallpapersBean.DateBase.COLUMN_PAGENAME + " text,"
			+ WallpapersBean.DateBase.COLUMN_PAGEPATH + " text ," + WallpapersBean.DateBase.COLUMN_THUMPATH + " text,"
			+ WallpapersBean.DateBase.COLUMN_DOWNLOAD + " integer)";

	private static final String WIDGET_TABLE = "CREATE TABLE " + WidgetBean.DateBase.TABLENAME + "( " + WidgetBean.DateBase._ID
			+ " INTEGER PRIMARY KEY," + WidgetBean.DateBase.COLUMN_ID + " INTEGER NOT NULL DEFAULT -1 ,"
			+ WidgetBean.DateBase.COLUMN_X + " integer," + WidgetBean.DateBase.COLUMN_Y + " integer,"
			+ WidgetBean.DateBase.COLUMN_SPANX + " integer, " + WidgetBean.DateBase.COLUMN_SPANY + " integer,"
			+ WidgetBean.DateBase.COLUMN_SCREEN + " integer, " + WidgetBean.DateBase.COLUMN_TYPE + " integer,"
			+ WidgetBean.DateBase.COLUMN_SCENE + " integer" + ")";
	
	/**
	 * 主题表
	 */
	private static final String SCENE_TABLE = "CREATE TABLE " + SceneBean.DateBase.TABLENAME + "( " + SceneBean.DateBase._ID
			+ " INTEGER PRIMARY KEY," + SceneBean.DateBase.COLUMN_INFO + " text," + SceneBean.DateBase.COLUMN_ICONS_ID
			+ " integer," + SceneBean.DateBase.COLUMN_WALLPAPER_ID + " integer," + SceneBean.DateBase.COLUMN_ISUSE
			+ " integer," + SceneBean.DateBase.COLUMN_SCREEN_COUNT + " integer," + SceneBean.DateBase.COLUMN_PAGEPATH
			+ " integer," + SceneBean.DateBase.COLUMN_THUMPATH + " integer)";

	/**
	 * wallpaper icons widget app 合在一起的表15条
	 */
	private static final String TYPE_TABLE = "CREATE TABLE " + TypeBean.DateBase.TABLENAME + "( " + TypeBean.DateBase._ID
			+ " INTEGER PRIMARY KEY," + TypeBean.DateBase.COLUMN_ACIVITYPAGENAME + " text," + TypeBean.DateBase.COLUMN_ICONPATH
			+ " text," + TypeBean.DateBase.COLUMN_ID + " integer," + TypeBean.DateBase.COLUMN_X + " integer,"
			+ TypeBean.DateBase.COLUMN_Y + " integer," + TypeBean.DateBase.COLUMN_SPANX + " integer,"
			+ TypeBean.DateBase.COLUMN_SPANY + " integer," + TypeBean.DateBase.COLUMN_TYPE + " integer,"
			+ TypeBean.DateBase.COLUMN_SCREEN + " integer," + TypeBean.DateBase.COLUMN_PAGENAME + " text,"
			+ TypeBean.DateBase.COLUMN_PAGEPATH + " text," + TypeBean.DateBase.COLUMN_THUMPATH + " text,"
			+ TypeBean.DateBase.COLUMN_DOWNLOAD + " text," + TypeBean.DateBase.COLUMN_SCENEID + " integer" + ")";

	public LemonadeSQliteHelper()
	{
		super(Launcher.luanchr, DATEBASE, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		if (!isTableExits(db, WALLPAPERS_TABLE))
		{

			db.execSQL(WALLPAPERS_TABLE);

			String sql = "insert into table_wallpapers (wallpapersId,wallpaperstype,use,pagename,pagepath,thumPath,download) values ('001',1,0,'��ɫ','wallpapers/pic1.jpg','wallpapers/thumb1.jpg',1)";
			String sql1 = "insert into table_wallpapers (wallpapersId,wallpaperstype,use,pagename,pagepath,thumPath,download) values ('002',1,0,'��ɫ','wallpapers/pic2.jpg','wallpapers/thumb2.jpg',1)";
			String sql2 = "insert into table_wallpapers (wallpapersId,wallpaperstype,use,pagename,pagepath,thumPath,download) values ('002',1,0,'��ɫ','wallpapers/pic3.jpg','wallpapers/thumb3.jpg',1)";
			String sql3 = "insert into table_wallpapers (wallpapersId,wallpaperstype,use,pagename,pagepath,thumPath,download) values ('002',1,1,'��ɫ','wallpapers/pic4.jpg','wallpapers/thumb4.jpg',1)";
			String sql4 = "insert into table_wallpapers (wallpapersId,wallpaperstype,use,pagename,pagepath,thumPath,download) values ('002',1,1,'��ɫ','wallpapers/pic5.jpg','wallpapers/thumb5.jpg',1)";
			String sql5 = "insert into table_wallpapers (wallpapersId,wallpaperstype,use,pagename,pagepath,thumPath,download) values ('002',1,1,'��ɫ','wallpapers/pic6.jpg','wallpapers/thumb6.jpg',1)";

			db.execSQL(sql);
			db.execSQL(sql1);
			db.execSQL(sql2);
			db.execSQL(sql3);
			db.execSQL(sql4);
			db.execSQL(sql5);
		}
		if (!isTableExits(db, WIDGET_TABLE))
		{
			db.execSQL(WIDGET_TABLE);
		}
		
		if (!isTableExits(db, SCENE_TABLE))
		{

			db.execSQL(SCENE_TABLE);

			String sql1 = "insert into scene (info,is_use,icon_id,screen_count,pagepath,thumpath) values('Lemonade','1','0','7','scene/pic1.jpg','scene/thumb1.jpg')";
			String sql2 = "insert into scene (info,is_use,icon_id,screen_count,pagepath,thumpath) values('Social','0','0','7','scene/pic2.jpg','scene/thumb2.jpg')";
			String sql3 = "insert into scene (info,is_use,icon_id,screen_count,pagepath,thumpath) values('Business','0','0','7','scene/pic3.jpg','scene/thumb3.jpg')";
			String sql4 = "insert into scene (info,is_use,icon_id,screen_count,pagepath,thumpath) values('Entertainment','0','0','7','scene/pic4.jpg','scene/thumb4.jpg')";
			String sql5 = "insert into scene (info,is_use,icon_id,screen_count,pagepath,thumpath) values('Travel','0','0','7','scene/pic4.jpg','scene/thumb4.jpg')";
			String sql6 = "insert into scene (info,is_use,icon_id,screen_count,pagepath,thumpath) values('Customized','0','0','7','scene/pic4.jpg','scene/thumb4.jpg')";
			db.execSQL(sql1);
			db.execSQL(sql2);
			db.execSQL(sql3);
			db.execSQL(sql4);
			db.execSQL(sql5);
			db.execSQL(sql6);

		}
		if (!isTableExits(db, TYPE_TABLE))
		{
			db.execSQL(TYPE_TABLE);

		}

	}

	public boolean isTableExits(SQLiteDatabase db, String tablename)
	{
		boolean result = false;
		String str = "select count(*) xcount  from sqlite_master where name='" + tablename + "'";
		Cursor c = db.rawQuery(str, null);
		c.moveToFirst();
		int rowIndex = c.getColumnIndex("xcount");
		int xcount = c.getInt(rowIndex);
		c.close();
		if (xcount != 0)
		{
			result = true;
		}
		return result;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}
}
