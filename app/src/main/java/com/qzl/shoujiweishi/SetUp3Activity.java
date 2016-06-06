package com.qzl.shoujiweishi;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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
        et_setup3_safenum.setText(sp.getString("safenum", ""));
    }

    @Override
    public void next_activity() {
        //保存输入的安全号码
        //1 获取输入的安全号码
        String safenum = et_setup3_safenum.getText().toString().trim();
        //2 判断号码是否为空
        if (TextUtils.isEmpty(safenum)) {
            Toast.makeText(getApplicationContext(), "请输入安全号码", Toast.LENGTH_SHORT).show();
            return;
        }
        //3 保存输入的号码
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("safenum", safenum);
        editor.commit();
        //跳转到第四个界面
        Intent intent = new Intent(this, SetUp4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
    }

    @Override
    public void pre_activity() {
        //跳转到第二个界面
        Intent intent = new Intent(this, SetUp2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }

    /**
     * 选怎联系人按钮的操作
     *
     * @param view
     */
    public void selectContacts(View view) {
        /*//跳转到选怎联系人界面
        Intent intent = new Intent(this,ContactActivity.class);
        //当现在的activity退出时，会调运之前activity的onActivityResult方法
        startActivityForResult(intent,0);*/
        //打开系统联系人
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(data != null) {
            //接收选择联系人界面传递过来的数据
            String num = data.getStringExtra("num");
            //将获取到的号码，设置给安全输入框
            et_setup3_safenum.setText(num);
        }*/
        if (data != null) {
            //	String num = data.getStringExtra("num");
            Uri uri = data.getData();
            String num = null;
            // 创建内容解析者
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            while (cursor.moveToNext()) {
                num = cursor.getString(cursor.getColumnIndex("data1"));
            }
            cursor.close();
            num = num.replaceAll("-", "");//替换的操作,555-6 -> 5556
            et_setup3_safenum.setText(num);
        }
    }
}
