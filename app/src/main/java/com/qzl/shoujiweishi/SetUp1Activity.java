package com.qzl.shoujiweishi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 设置手机防盗功能设置向导的第一个界面
 */
public class SetUp1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up1);
    }

    /**
     * 跳转到第二个界面
     * @param view
     */
    public void next(View view){
        Intent intent = new Intent(this,SetUp2Activity.class);
        startActivity(intent);
        finish();
    }
}
