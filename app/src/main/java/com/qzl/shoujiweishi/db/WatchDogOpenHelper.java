package com.qzl.shoujiweishi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 看门狗数据库操作
 * Created by Qzl on 2016-07-13.
 */
public class WatchDogOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "info";
    public WatchDogOpenHelper(Context context) {
        super(context, "watchdog.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DB_NAME+"(_id integer primary key autoincrement,packagename varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
