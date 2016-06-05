package com.qzl.shoujiweishi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SetUp2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up2);
    }

    public void pre(View view){
        //跳转到第一个界面
        Intent intent = new Intent(this,SetUp1Activity.class);
        startActivity(intent);
        finish();
    }
    public void next(View view){
        //跳转到第三个界面
        //Intent intent = new Intent(this,SetUp3Activity.class);
        //startActivity(intent);
    }
}
