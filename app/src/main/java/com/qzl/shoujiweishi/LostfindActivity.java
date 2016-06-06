package com.qzl.shoujiweishi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 手机防盗
 */
public class LostfindActivity extends AppCompatActivity {

    private SharedPreferences sp;
    @ViewInject(R.id.tv_lostfind_safenum)
    private TextView tv_lostfind_safenum;

    @ViewInject(R.id.tv_lostfind_protected)
    private ImageView tv_lostfind_protected;
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
            ViewUtils.inject(this);
            //根据保存的安全号码和防盗保护状态进行设置
            tv_lostfind_safenum.setText(sp.getString("safenum",""));
            //设置防盗保护是否开启的状态
            //获取保存的防盗保护状态
            boolean b = sp.getBoolean("protected",false);
            //根据获取的防盗保护状态设置相应的显示图片
            if(b){
               //开启防盗保护
                tv_lostfind_protected.setImageResource(R.drawable.lock);
            }else {
                //关闭防盗保护
                tv_lostfind_protected.setImageResource(R.drawable.unlock);
            }
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
