package com.qzl.shoujiweishi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SetUp4Activity extends SetUpBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up4);
    }

    @Override
    public void next_activity() {
        //保存一下用户第一次进入手机防盗模块设置向导的状态
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("first",false);//表示已经设置过了
        editor.commit();
        //跳转到手机防盗页面
        Intent intent = new Intent(this,LostfindActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void pre_activity() {
        //跳转到第三个界面
        Intent intent = new Intent(this, SetUp3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
