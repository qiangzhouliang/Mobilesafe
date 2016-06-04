package com.qzl.shoujiweishi;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.qzl.shoujiweishi.ui.SettingView;

public class SettingActivity extends Activity {

    private SettingView sv_setting_update;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //name : 保存信息的文件的名称
        //mode : 权限
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
        //初始化自定义控件中各个控件的值
        sv_setting_update.setTitle("提示更新");
        //defValue : 缺省的值
        if (sp.getBoolean("update", true)) {
            sv_setting_update.setDes("打开提示更新");
            sv_setting_update.setChecked(true);
        }else{
            sv_setting_update.setDes("关闭提示更新");
            sv_setting_update.setChecked(false);
        }
        //设置自定义组合控件的点击事件
        //问题1:点击checkbox发现描述信息没有改变,原因:因为checkbox天生是有点击事件和获取焦点事件,当点击checkbox,这个checkbox就会执行他的点击事件
        //而不会执行条目的点击事件
        //问题2:没有保存用户点击操作
        sv_setting_update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Editor edit = sp.edit();
                //更改状态
                //根据checkbox之前的状态来改变checkbox的状态
                if (sv_setting_update.isChecked()) {
                    //关闭提示更新
                    sv_setting_update.setDes("关闭提示更新");
                    sv_setting_update.setChecked(false);
                    //保存状态
                    edit.putBoolean("update", false);
                    //edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下保存到内存中的
                }else{
                    //打开提示更新
                    sv_setting_update.setDes("打开提示更新");
                    sv_setting_update.setChecked(true);
                    //保存状态
                    edit.putBoolean("update", true);
                }
                edit.commit();
            }
        });
    }

}

