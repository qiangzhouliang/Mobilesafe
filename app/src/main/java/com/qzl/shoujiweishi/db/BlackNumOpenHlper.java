package com.qzl.shoujiweishi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库
 * Created by Qzl on 2016-07-07.
 */
public class BlackNumOpenHlper extends SQLiteOpenHelper{
    //方便后期实现数据库操作的时候能方便去使用表名，同时也方便后期去修改表名
    public static final String DB_NAME = "info";
    /**
     * context : 上下文
     * name： 数据库名称
     * factory：游标工厂
     * version：版本号
     * @param context
     */
    public BlackNumOpenHlper(Context context) {
        super(context, "blacknum.db", null, 1);
    }

    /**
     * 第一次创建数据库的时候调运，创建表结构
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构 字段：blacknum ：黑名单号码   ，mode ：拦截类型
        //参数：创建表结构的sql语句
        db.execSQL("create table "+DB_NAME+"(_id integer primary key autoincrement,blacknum varchar(20),mode varchar(2))");
    }

    /**
     * 数据库版本发生变化的时候调运
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
