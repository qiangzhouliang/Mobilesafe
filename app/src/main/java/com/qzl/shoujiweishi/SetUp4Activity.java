package com.qzl.shoujiweishi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SetUp4Activity extends SetUpBaseActivity {
    @ViewInject(R.id.cb_setup4_protected)
    private CheckBox cb_setup4_protected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up4);
        ViewUtils.inject(this);
        //根据保存的用户状态进行回显操作
        if(sp.getBoolean("protected",false)){
            //开启了防盗保护
            cb_setup4_protected.setText("您已经开启了防盗保护");
            cb_setup4_protected.setChecked(true);//必须要写
        }else {
            //关闭防盗保护
            cb_setup4_protected.setText("您还没有开启防盗保护");
            cb_setup4_protected.setChecked(false);//必须要写
        }
        //设置checkBox点击事件
        //当checkBox状态改变得时候调运
        cb_setup4_protected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //CompoundButton:checkBox
            //isChecked : 改变之后的值，点击之后的值
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                //根基checkBox状态设置checkBox信息
                if(isChecked){
                    //开启防盗保护
                    cb_setup4_protected.setText("您已经开启了防盗保护");
                    cb_setup4_protected.setChecked(true);//程序的严谨性
                    //保存用户选中的状态
                    editor.putBoolean("protected", true);
                }else {
                    //关闭防盗保护
                    cb_setup4_protected.setText("您还没有开启了防盗保护");
                    cb_setup4_protected.setChecked(false);//程序的严谨性
                    //保存用户选中的状态
                    editor.putBoolean("protected", true);
                }
                editor.commit();
            }
        });
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
