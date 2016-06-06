package com.qzl.shoujiweishi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 手机防盗
 */
public class LostfindActivity extends AppCompatActivity {

    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        //分为两部分 1.显示设置过的手机防盗功能，2 设置手机防盗功能
        //判断用户是否是第一次进入手机防盗模块，是，跳转到设置向导界面，不是，跳转到防盗功能显示界面
        if(sp.getBoolean("first",true)){
            //第一次进入，跳转到手机防盗设置向导界面
            Intent intent = new Intent(this,SetUp1Activity.class);
            startActivity(intent);
            //移除LostfindActivity
            finish();
        }else {
            //手机防盗显示界面
            setContentView(R.layout.activity_lostfind);
        }
    }

    /**
     * 重新进入设置向导的点击事件
     * @param v
     */
    public void resetUp(View v){
        Intent intent = new Intent(this,SetUp1Activity.class);
        startActivity(intent);
        finish();
    }
}
