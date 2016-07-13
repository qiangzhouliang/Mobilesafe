package com.qzl.shoujiweishi.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qzl.shoujiweishi.bean.BlackNumInfo;
import com.qzl.shoujiweishi.db.BlackNumOpenHlper;
import com.qzl.shoujiweishi.db.WatchDogOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qzl on 2016-07-07.
 */
public class WatchDogDao {

    private WatchDogOpenHelper watchDogOpenHelper;
    private byte[] b = new byte[1024];
    /**
     * 在构造函数中获取BlackNumOpenHlper
     * @param context
     */
    public WatchDogDao(Context context) {
        watchDogOpenHelper = new WatchDogOpenHelper(context);
    }
    //增  删  改  查
    //我在同一时刻对数据库既进行读操作，也进行写操作，怎么避免这两个同时操作数据库,同步锁+将WatchDogOpenHelper设置成单例模式
    /**
     * 添加应用程序包名
     */
    public void addLockApp(String packagename){
        synchronized (b){
            //1. 获取数据库
            SQLiteDatabase database = watchDogOpenHelper.getWritableDatabase();
            //2.添加操作
            //ContentValues:添加的数据
            ContentValues values = new ContentValues();
            values.put("packagename", packagename);
            database.insert(WatchDogOpenHelper.DB_NAME, null, values);
            //3.关闭数据库，可以防止内存溢出
            database.close();
        }
    }

    /**
     * 查询数据库中是否有包名,有 return true，没有 return false
     */
    public boolean queryLockApp(String packagename){
        boolean isLock = false;
        //1.获取数据库
        SQLiteDatabase database = watchDogOpenHelper.getReadableDatabase();
        //2.查询数据
        //table：表名
        // colums:查询的字段 mode
        //selection:查询条件 where xxx = xxx
        //selectionArgs：查询条件的参数
        //groupby:分组
        //having:去重
        //orderBy:排序
        Cursor cursor = database.query(BlackNumOpenHlper.DB_NAME,null,"packagename=?",new String[]{packagename},null,null,null);
        //3.解析cursor
        if(cursor.moveToNext()){
            //获取查询出来的数据
            isLock = true;
        }
        //4.关闭数据库
        cursor.close();
        database.close();
        return isLock;
    }

    /**
     *删除包名
     */
    public void deletePackageName(String packagename){
        //1 获取数据库
        SQLiteDatabase database = watchDogOpenHelper.getWritableDatabase();
        //2 删除数据
        //table : 表名
        //whereClause: 查询条件
        //whereArgus：查询条件的参数
        database.delete(BlackNumOpenHlper.DB_NAME,"packagename=?",new String[]{packagename});
        //3 关闭数据库
        database.close();
    }

    /**
     * 查询全部数据
     */
    public List<String> queryAllLockApp(){
        List<String> list = new ArrayList<>();
        // 1 获取数据库
        SQLiteDatabase database = watchDogOpenHelper.getReadableDatabase();
        //2 查询数据库
        Cursor cursor = database.query(BlackNumOpenHlper.DB_NAME,new String[]{"packagename"},null,null,null,null,null);//desc 倒叙查询，asc 正序查询
        //解析cursor
        while (cursor.moveToNext()){
            //获取查询出来的数据
            String packagename = cursor.getString(0);
            list.add(packagename);
        }
        //4 关闭数据库
        cursor.close();
        database.close();
        return list;
    }
}