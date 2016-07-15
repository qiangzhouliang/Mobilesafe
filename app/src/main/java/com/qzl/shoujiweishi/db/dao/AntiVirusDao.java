package com.qzl.shoujiweishi.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * 查询md5值是否在数据库中
 * Created by Qzl on 2016-07-15.
 */
public class AntiVirusDao {
    /**
     * 查询md5值是否在病毒数据库中
     * @param context
     * @param md5
     * @return
     */
    public static boolean quaryAntiVirus(Context context,String md5){
        boolean ishave = false;
        //1 获取数据库的一个路径
        File file = new File(context.getFilesDir(), "antivirus.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.query("datable",null,"md5=?",new String[]{md5},null,null,null);
        if(cursor.moveToNext()){
            ishave = true;
        }
        cursor.close();
        database.close();
        return ishave;
    }
}
