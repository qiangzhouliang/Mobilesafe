package com.qzl.shoujiweishi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SetUp3Activity extends SetUpBaseActivity {

    private EditText et_setup3_safenum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up3);
        et_setup3_safenum = (EditText) findViewById(R.id.et_setup3_safenum);
        //回显操作
        et_setup3_safenum.setText(sp.getString("safenum",""));
    }

    @Override
    public void next_activity() {
        //保存输入的安全号码
        //1 获取输入的安全号码
        String safenum = et_setup3_safenum.getText().toString().trim();
        //2 判断号码是否为空
        if(TextUtils.isEmpty(safenum)){
            Toast.makeText(getApplicationContext(), "请输入安全号码", Toast.LENGTH_SHORT).show();
            return;
        }
        //3 保存输入的号码
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("safenum",safenum);
        editor.commit();
        //跳转到第四个界面
        Intent intent = new Intent(this,SetUp4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
    }

    @Override
    public void pre_activity() {
        //跳转到第二个界面
        Intent intent = new Intent(this,SetUp2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
