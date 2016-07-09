package com.qzl.shoujiweishi.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qzl.shoujiweishi.bean.BlackNumInfo;
import com.qzl.shoujiweishi.db.BlackNumOpenHlper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qzl on 2016-07-07.
 */
public class BlackNumDao {

    public static final int CALL = 0;//电话拦截
    public static final int SMS = 1;//短信拦截
    public static final int ALL = 2;//全部拦截
    private BlackNumOpenHlper blackNumOpenHlper;

    /**
     * 在构造函数中获取BlackNumOpenHlper
     *
     * @param context
     */
    public BlackNumDao(Context context) {
        blackNumOpenHlper = new BlackNumOpenHlper(context);
    }

    //增  删  改  查

    /**
     * 添加黑名单操作
     *
     * @param blacknum
     * @param mode
     */
    public void addBlackNum(String blacknum, int mode) {
        //1. 获取数据库
        SQLiteDatabase database = blackNumOpenHlper.getWritableDatabase();
        //2.添加操作
        //ContentValues:添加的数据
        ContentValues values = new ContentValues();
        values.put("blacknum", blacknum);
        values.put("mode", mode);
        database.insert(BlackNumOpenHlper.DB_NAME, null, values);
        //3.关闭数据库，可以防止内存溢出
        database.close();
    }

    /**
     * 更新黑名单的拦截模式
     */
    public void updateBlackNum(String blacknum, int mode) {
        //1.获取数据库
        SQLiteDatabase database = blackNumOpenHlper.getWritableDatabase();
        //更新操作
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        //table:表名
        //values:要更新数据
        //whereClause： 查询条件
        // whereArgs:查询条件的参数
        database.update(BlackNumOpenHlper.DB_NAME,values,"blacknum=?",new String[]{blacknum});
        //3.关闭数据库
        database.close();
    }

    /**
     * 通过黑名单号码，查询黑名单号码的拦截模式
     */
    public int queryBlackNumMode(String blacknum){
        int mode = -1;
        //1.获取数据库
        SQLiteDatabase database = blackNumOpenHlper.getReadableDatabase();
        //2.查询数据
        //table：表名
        // colums:查询的字段 mode
        //selection:查询条件 where xxx = xxx
        //selectionArgs：查询条件的参数
        //groupby:分组
        //having:去重
        //orderBy:排序
        Cursor cursor = database.query(BlackNumOpenHlper.DB_NAME,new String[]{"mode"},"blacknum=?",new String[]{blacknum},null,null,null);
        //3.解析cursor
        if(cursor.moveToNext()){
            //获取查询出来的数据
            mode = cursor.getInt(0);
        }
        //4.关闭数据库
        cursor.close();
        database.close();
        return mode;
    }

    /**
     *根据黑名单号码删除相应的操作
     * @param blacknum
     */
    public void deleteBlackNum(String blacknum){
        //1 获取数据库
        SQLiteDatabase database = blackNumOpenHlper.getWritableDatabase();
        //2 删除数据
        //table : 表名
        //whereClause: 查询条件
        //whereArgus：查询条件的参数
        database.delete(BlackNumOpenHlper.DB_NAME,"blacknum=?",new String[]{blacknum});
        //3 关闭数据库
        database.close();
    }

    /**
     * 查询全部黑名单号码
     */
    public List<BlackNumInfo> queryAllBlackNum(){
        List<BlackNumInfo> list = new ArrayList<>();
        // 1 获取数据库
        SQLiteDatabase database = blackNumOpenHlper.getReadableDatabase();
        //2 查询数据库
        Cursor cursor = database.query(BlackNumOpenHlper.DB_NAME,new String[]{"blacknum","mode"},null,null,null,null,"_id desc");//desc 倒叙查询，asc 正序查询
        //解析cursor
        while (cursor.moveToNext()){
            //获取查询出来的数据
            String blacknum = cursor.getString(0);
            int mode = cursor.getInt(1);
            BlackNumInfo blackNumInfo = new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }
        //4 关闭数据库
        cursor.close();
        database.close();
        return list;
    }

    /**
     *查询部分数据的方法
     * 查询20条数据
     * MaxNum:查询总个数
     * startindex：查询起始位置
     * @return
     */
    public List<BlackNumInfo> getPartBlackNum(int MaxNum,int startindex){
        List<BlackNumInfo> list = new ArrayList<>();
        // 1 获取数据库
        SQLiteDatabase database = blackNumOpenHlper.getReadableDatabase();
        //2 查询数据库
        //Cursor cursor = database.query(BlackNumOpenHlper.DB_NAME,new String[]{"blacknum","mode"},null,null,null,null,"_id desc");//desc 倒叙查询，asc 正序查询
        Cursor cursor = database.rawQuery("select blacknum,mode from info order by _id desc limit ? offset ?",new String[]{MaxNum+"",startindex+""});
        //解析cursor
        while (cursor.moveToNext()){
            //获取查询出来的数据
            String blacknum = cursor.getString(0);
            int mode = cursor.getInt(1);
            BlackNumInfo blackNumInfo = new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }
        //4 关闭数据库
        cursor.close();
        database.close();
        return list;
    }
}