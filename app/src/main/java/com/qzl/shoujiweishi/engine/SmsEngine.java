package com.qzl.shoujiweishi.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Qzl on 2016-07-14.
 */
public class SmsEngine {
    /**
     * 获取短信的操作
     */
    public static void getAllSMS(Context context){
        //1 获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        // 2 获取内容提供者地址 sms，sms表的地址：null
        //3 获取查询路径
        Uri uri = Uri.parse("content://sms");
        //4 查询操作
        //projection：查询的字段
        // selection：查询条件
        //delectionArgs:查询条件的参数
        //sortOrder：排序
        Cursor cursor = resolver.query(uri,new String[]{"address","date","type","body"},null,null,null);
        //5 解析cursor
        while (cursor.moveToNext()){
            String address = cursor.getString(0);
            String date = cursor.getString(1);
            String type = cursor.getString(2);
            String body = cursor.getString(3);
            System.out.println("address:"+address+"  date:"+date+"   type:"+type+"    body:"+body);
        }
    }
}
