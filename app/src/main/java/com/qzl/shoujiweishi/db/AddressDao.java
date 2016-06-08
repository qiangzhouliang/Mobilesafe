package com.qzl.shoujiweishi.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by Q on 2016-06-08.
 */
public class AddressDao {

    /**
     * 打开数据库，查询号码归属地
     * @return
     */
    public static String queryAssress(String num,Context context){
        String location = "";
        //1 获取数据库的一个路径
        File file = new File(context.getFilesDir(), "address.db");
        //2 打开数据库
        // getAbsolutePath():获取绝对路径
        // factory:游标工厂
        SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        //查询号码归属地
        //sql : sql语句
        //selectionArgs:查询条件的参数
        //substring : 包含头不包含尾
        Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{num.substring(0, 7)});
        // 4 解析
        //因为每个号码对应一个号码归属地，所以查询出来的是一个号码归属地，没有必要用while，用if就可以了
        if(cursor.moveToNext()){
            location = cursor.getString(0);
        }
        // 5 关闭数据库
        cursor.close();
        database.close();
        return location;
    }
}
