package com.qzl.shoujiweishi;

import android.app.ProgressDialog;
import android.test.AndroidTestCase;

import com.qzl.shoujiweishi.engine.SmsEngine;

/**
 * Created by Qzl on 2016-07-14.
 */
public class SMSTest extends AndroidTestCase {
    public void testSms(){
        //SmsEngine.getAllSMS(getContext());
    }
    public void testparseXMLWithPull(){
        SmsEngine.parseXMLWithPull(getContext());
    }
}
