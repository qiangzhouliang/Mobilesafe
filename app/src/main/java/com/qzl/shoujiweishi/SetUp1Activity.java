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
        //执行平移动画 ;执行界面切换动画的操作，是在startActivity或finish之后执行的
        //enterAnim : 新的界面进入的动画
        //exitAnim : 旧的界面退出的动画
        overridePendingTransition(R.anim.setup_enter_next,R.anim.setup_exit_next);
    }

    @Override
    public void pre_activity() {

    }
}
