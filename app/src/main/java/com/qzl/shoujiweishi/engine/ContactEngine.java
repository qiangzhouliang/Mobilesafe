package com.qzl.shoujiweishi.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Q on 2016-06-06.
 */
public class ContactEngine {

    /**
     * 获取系统联系人
     * @return
     */
    public static List<HashMap<String,String>> getAllContactInfo(Context context){
        //1 获取一个内容解析者
        ContentResolver resolver = context.getContentResolver();
        //2 获取内容提供者的地址: com.android.contacts
        //raw_contacts表的地址：raw_contacts
        //view_data表的地址：data
        //3 生成查询地址
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri data_uri = Uri.parse("content://com.android.contacts/data");
        //4 查询操作,先查询raw_contacts，查询contact_id 字段
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
        //5 解析cursor
        while (cursor.moveToNext()){
            //6 获取查询的数据
            String contact_id = cursor.getString(0);
            //cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex : 查询字段在cursor中的索引值，一般都用在查询字段比较多的时候
            //7 根据contact_id查询view_data表中的数据
            
        }
        return null;
    }
}
