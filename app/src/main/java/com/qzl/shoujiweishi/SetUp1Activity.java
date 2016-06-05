package com.qzl.shoujiweishi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 设置手机防盗功能设置向导的第一个界面
 */
public class SetUp1Activity extends SetUpBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up1);
    }

    /**
     * 跳转到第二个界面
     */
    @Override
    public void next_activity() {
        Intent intent = new Intent(this,SetUp2Activity.class);
        startActivity(intent);
        //移除当前activity
        finish();
    }

    @Override
    public void pre_activity() {

    }
}
