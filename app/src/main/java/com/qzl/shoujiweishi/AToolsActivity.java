package com.qzl.shoujiweishi;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.qzl.shoujiweishi.engine.SmsEngine;

import org.xmlpull.v1.XmlPullParser;

public class AToolsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void queryAddress(View view){
        //跳转到查询号码归属地
        Intent intent = new Intent(AToolsActivity.this,AddressActivity.class);
        startActivity(intent);
    }

    /**
     * 备份短信
     * @param view
     */
    public void backupams(View view) {
        //progressdialog是可以在子线程中更新
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);//不能取消
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        //1 读取短信
        //2 备份短信
        new Thread(){
            @Override
            public void run() {
                super.run();
                //3 接收刷子
                SmsEngine.getAllSMS(getApplicationContext(), new SmsEngine.ShowProgress() {
                    @Override
                    public void setMax(int max) {
                        progressDialog.setMax(max);
                    }

                    @Override
                    public void setProgress(int progress) {
                        progressDialog.setProgress(progress);
                    }
                });
                progressDialog.dismiss();
            }
        }.start();
        //回调函数，就可以将要做的操作放到我们这边来执行，业务类提供一个操作，但是这个操作她不知道要怎么做，他会把这个操作交给我们来做，具体的操作有我们来做

    }

    /**
     * 还原短信备份
     * @param view
     */
    public void restoresms(View view) {
        //解析XML
        SmsEngine.parseXMLWithPull(getApplicationContext());
        Toast.makeText(getApplicationContext(), "短信还原完成...", Toast.LENGTH_SHORT).show();
    }
}
