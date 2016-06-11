package com.qzl.shoujiweishi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.qzl.shoujiweishi.service.AddressServices;
import com.qzl.shoujiweishi.ui.SettingView;
import com.qzl.shoujiweishi.utils.AddressUtils;

public class SettingActivity extends Activity {

    private SettingView sv_setting_update;
    private SharedPreferences sp;//保存各种状态
    private SettingView sv_setting_address;//号码归属地
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //name : 保存信息的文件的名称，config
        //mode : 权限，MODE_PRIVATE（私有的）
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
        sv_setting_address = (SettingView) findViewById(R.id.sv_setting_address);
        update();

    }

    //获取焦点时调运，在onStart之后
    @Override
    protected void onResume() {
        super.onResume();
        address();
    }
    //没有焦点时调运
    @Override
    protected void onPause() {
        super.onPause();
    }
    //activity可见时调运
    @Override
    protected void onStart() {
        super.onStart();
    }
    //activity不可见时调运
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     *显示号码归属地
     */
    private void address() {
        //回显操作
        //动态的获取服务是否开启
        System.out.println(getLocalClassName());
        if(AddressUtils.isRunningService("com.qzl.shoujiweishi.service.AddressServices",getApplicationContext())){
            //开启服务
            sv_setting_address.setChecked(true);
        }else {
            //关闭服务
            sv_setting_address.setChecked(false);
        }
        sv_setting_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AddressServices.class);
                //根据checkBox的状态设置描述信息的状态
                // isChecked : 代表之前的状态
                if(sv_setting_address.isChecked()){
                    //关闭提示更新
                    stopService(intent);
                    //更新checkBox的状态
                    sv_setting_address.setChecked(false);
                }else {
                    //打开提示更新
                    startService(intent);
                    sv_setting_address.setChecked(true);
                }
            }
        });
    }

    /**
     * 提示更新的操作
     */
    private void update() {
        //初始化自定义控件中各个控件的值
        //sv_setting_update.setTitle("提示更新");
        //根据保存值，来初始化
        //defValue : 缺省的值
        //使用了自定义组合控件，这儿不需要了这样设置了
        /*if (sp.getBoolean("update", true)) {
            sv_setting_update.setDes("打开提示更新");
            sv_setting_update.setChecked(true);
        }else{
            sv_setting_update.setDes("关闭提示更新");
            sv_setting_update.setChecked(false);
        }*/
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
                    //sv_setting_update.setDes("提示更新已关闭");
                    sv_setting_update.setChecked(false);
                    //保存状态
                    edit.putBoolean("update", false);
                    //edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下保存到内存中的
                }else{
                    //打开提示更新
                    //sv_setting_update.setDes("提示更新已打开");
                    sv_setting_update.setChecked(true);
                    //保存状态
                    edit.putBoolean("update", true);
                }
                edit.commit();
            }
        });
    }

}

