package com.qzl.shoujiweishi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.qzl.shoujiweishi.ui.SettingView;

public class SetUp2Activity extends SetUpBaseActivity {

    private SettingView sv_setup2_sim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up2);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        sv_setup2_sim = (SettingView) findViewById(R.id.sv_setup2_sim);
        //设置回显操作
        //根据保存的SIM卡去初始化控件状态，有保存SIM卡号就是绑定SIM卡，如果没有，就是没有绑定SIM卡
        if(TextUtils.isEmpty(sp.getString("sim",""))){
            //没有绑定
            sv_setup2_sim.setChecked(false);
        }else {
            //绑定SIM卡
            sv_setup2_sim.setChecked(true);
        }
        sv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                //绑定sim卡
                //根据checkBox的状态设置描述信息的状态
                //isChecked() : 获取之前checkox的状态
                if(sv_setup2_sim.isChecked()){
                    //解绑
                    editor.putString("sim", "");
                    sv_setup2_sim.setChecked(false);
                }else {
                    //绑定sim卡
                    //TelephonyManager : 电话的管理者
                    TelephonyManager tel = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    //tel.getLine1Number();//获取SIM卡绑定的电话号码 line1：解决双卡双待，（在中国不太实用，运营商一般不会将SIM卡和手机号码绑定）获取主卡电话号码
                    String sim = tel.getSimSerialNumber();//获取SIM卡序列号，唯一标示
                    //保存SIM卡号
                    editor.putString("sim", sim);
                    sv_setup2_sim.setChecked(true);
                }
                editor.commit();
            }
        });
    }

    @Override
    public void next_activity() {
        //跳转到第三个界面
        Intent intent = new Intent(this, SetUp3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
    }

    @Override
    public void pre_activity() {
        //跳转到第一个界面
        Intent intent = new Intent(this, SetUp1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre,R.anim.setup_exit_pre);
    }
}
