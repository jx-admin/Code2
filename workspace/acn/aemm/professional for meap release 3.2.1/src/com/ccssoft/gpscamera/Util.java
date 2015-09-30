package com.ccssoft.gpscamera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Util
{
   /** Called when the activity is first created. */

   public final static String DATABASEPATH = "database";
   private final static String FILENAME = "database.db";
   public SQLiteDatabase db;
   public Context context;

   public Util(Context context)
   {
      this.context = context;
   }

   public SQLiteDatabase readDatabase(int resId)
   {
      try
      {
         String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
               + DATABASEPATH;
         String dbFile = DB_PATH + File.separator + FILENAME;
         File dir = new File(DB_PATH);
         if (!dir.exists())
            dir.mkdir();
         // 获得封装dictionary.db文件的InputStream对象 
         InputStream is = context.getResources().openRawResource(resId);
         FileOutputStream fos = new FileOutputStream(dbFile);
         byte[] buffer = new byte[8192];
         int count = 0;
         // 开始复制db文件 
         while ((count = is.read(buffer)) > 0)
         {
            fos.write(buffer, 0, count);
         }

         fos.close();
         is.close();
         SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
         return database;
      }
      catch (Exception e)
      {
      }
      return null;

   }

}