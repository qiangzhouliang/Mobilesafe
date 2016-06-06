package com.qzl.shoujiweishi;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.widget.Toast;

import com.qzl.shoujiweishi.engine.ContactEngine;

import java.util.HashMap;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    //测试获取联系人
    public void testContacts(){
        List<HashMap<String,String>> list = ContactEngine.getAllContactInfo(getContext());
        for (HashMap<String, String> hashMap: list){
            System.out.println("姓名："+hashMap.get("name")+"    电话"+hashMap.get("phone"));
        }
    }
}