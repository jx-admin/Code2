
package com.act.mbanking.bean;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

public abstract class DataBaseModel {

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ContentValues generateValues() {

        ContentValues contentValues = new ContentValues();
//        contentValues.put(BaseColumns._ID, id);
        return contentValues;

    }

    public void setDataByValues(Cursor cursor) {

        if (cursor != null && cursor.getCount() > 0) {
            id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
        }
    }

   

    public abstract String getTableName();

    public abstract String createTableSQL();

    public static DataBaseModel createModel(String tableName) {

        DataBaseModel dataBaseModel = null;
        if (tableName == null || tableName.equals("")) {

            return null;
        } else if (tableName.equals(SettingModel.TABLE_NAME)) {
            dataBaseModel = new SettingModel();
        }

        return dataBaseModel;

    }
}
