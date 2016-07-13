package com.qzl.shoujiweishi;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WatchDogActivity extends AppCompatActivity {

    private ImageView iv_watchdog_icon;
    private TextView tv_watchdog_name;
    private String packageName;
    private EditText et_watchdog_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_dog);
        iv_watchdog_icon = (ImageView) findViewById(R.id.iv_watchdog_icon);
        tv_watchdog_name = (TextView) findViewById(R.id.tv_watchdog_name);
        et_watchdog_password = (EditText) findViewById(R.id.et_watchdog_password);
        //接收获取数据
        Intent intent = getIntent();
        packageName = intent.getStringExtra("packageName");
        //显示加锁的应用程序的图片和名称
        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName,0);
            Drawable icon = applicationInfo.loadIcon(packageManager);
            String name = applicationInfo.loadLabel(packageManager).toString();
            //设置显示
            iv_watchdog_icon.setImageDrawable(icon);
            tv_watchdog_name.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            /**
             * Starting: Intent {
             act=android.intent.action.MAIN
             cat=[android.intent.category.HOME
             ] cmp=com.android.launcher/com.android.launcher2.Launcher } from pid 208
             */
            //跳转到主界面
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 解锁点击事件
     * @param view
     */
    public void lock(View view) {
        //解锁
        // 1 获取输入的密码
        String passeword = et_watchdog_password.getText().toString().trim();
        // 2 判断密码输入是否正确
        if("123".equals(passeword)){
            //解锁
            //一般通过广播的形式将信息发送给服务
            Intent intent = new Intent();
            intent.setAction("com.qzl.shoujiweishi.unlock");//自定义要发送的广播事件
            intent.putExtra("packageName",packageName);
            sendBroadcast(intent);
            finish();

        }else {
            Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
        }
    }
}
