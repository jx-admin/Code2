
package com.accenture.mbank.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.accenture.mbank.MobileBankApplication;
import com.accenture.mbank.model.DataBaseModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.util.LogManager;

public class BankSqliteHelper extends SQLiteOpenHelper {

    public static final String databasename = "mbank";

    private static BankSqliteHelper bankSqliteHelper;

    private static final String Tag = "database";

    private boolean showLog = true;

    public static BankSqliteHelper getInstance() {

        if (bankSqliteHelper == null) {
            bankSqliteHelper = new BankSqliteHelper(MobileBankApplication.applicationContext);
        }
        return bankSqliteHelper;
    }

    private BankSqliteHelper(Context context) {
        super(context, databasename, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        DataBaseModel setBaseModel = new SettingModel();
        db.execSQL(setBaseModel.createTableSQL());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized long insert(DataBaseModel settingModel) {
        ContentValues contentValues = settingModel.generateValues();
        long raw = getWritableDatabase().insert(settingModel.getTableName(), null, contentValues);

        if (raw > -1) {
            log("insert in database table name=" + settingModel.getTableName());
        } else {
            log("insert in database fail table name=" + settingModel.getTableName());
        }
        return raw;
    }

    public synchronized DataBaseModel findById(String tablename, long id) {

        return findBykey(tablename, BaseColumns._ID, id);
    }

    public synchronized DataBaseModel findBykey(String tablename, String keyname, long keyvalue) {

        String sql = "select * from " + tablename + " where " + keyname + " =" + keyname;
        List<DataBaseModel> list = query(tablename, sql);

        log("sql:" + sql);
        if (list.size() > 0) {
            log(list.size() + "");
            return list.get(0);
        }

        return null;
    }

    public synchronized DataBaseModel findBykey(String tablename, String keyname, String keyvalue) {

        String sql = "select * from " + tablename + " where " + keyname + " =" + "'" + keyvalue
                + "'";
        log("sql:" + sql);
        List<DataBaseModel> list = query(tablename, sql);

        if (list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    public synchronized List<DataBaseModel> query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = getReadableDatabase().query(table, columns, selection, selectionArgs,
                groupBy, having, orderBy);

        List<DataBaseModel> list = readCursor(table, cursor);
        if (cursor != null) {
            cursor.close();
        }
        log("database:query in database  table name=" + table + "size=" + list.size());
        return list;
    }

    public synchronized List<DataBaseModel> query(String table, String sql) {
        log("sql:" + sql);
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        List<DataBaseModel> list = readCursor(table, cursor);
        log("database:query in database  table name=" + table + "size=" + list.size());
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    private List<DataBaseModel> readCursor(String table, Cursor cursor) {
        List<DataBaseModel> list = new ArrayList<DataBaseModel>();
        while (cursor.moveToNext()) {

            DataBaseModel dataBaseModel = DataBaseModel.createModel(table);
            dataBaseModel.setDataByValues(cursor);
            list.add(dataBaseModel);
        }
        return list;
    }

    public synchronized boolean update(DataBaseModel dataBaseModel) {

        SQLiteDatabase database = getWritableDatabase();
        String whereclause = BaseColumns._ID + "=" + dataBaseModel.getId();
        long raw = database.update(dataBaseModel.getTableName(), dataBaseModel.generateValues(),
                whereclause, null);
        if (raw >= 0) {
            log("update:" + dataBaseModel);
            return true;
        }

        return false;
    }

    public synchronized boolean update(String table, ContentValues values, String whereClause,
            String[] whereArgs) {
        SQLiteDatabase database = getWritableDatabase();
        long raw = database.update(table, values, whereClause, whereArgs);
        if (raw > 0) {
            log("update:" + "success");
            return true;
        }
        return false;
    }

    public long delete(long id) {

        return 0;
    }

    private void log(String message) {

        if (showLog) {
            LogManager.d(Tag + message);
        }
    }

}
