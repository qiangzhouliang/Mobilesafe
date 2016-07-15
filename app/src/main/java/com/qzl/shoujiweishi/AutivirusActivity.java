package com.qzl.shoujiweishi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qzl.shoujiweishi.db.dao.AntiVirusDao;
import com.qzl.shoujiweishi.utils.MD5Util;

import java.util.ArrayList;
import java.util.List;

public class AutivirusActivity extends AppCompatActivity {

    private ImageView iv_antivirus_scanner;
    private ProgressBar pb_antivirus_progressbar;
    private TextView tv_antivirus_text;
    private LinearLayout ll_antivirus_safeapks;
    //存放病毒应用的包名
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autivirus);
        list = new ArrayList<>();
        iv_antivirus_scanner = (ImageView) findViewById(R.id.iv_antivirus_scanner);
        pb_antivirus_progressbar = (ProgressBar) findViewById(R.id.pb_antivirus_progressbar);
        tv_antivirus_text = (TextView) findViewById(R.id.tv_antivirus_text);
        ll_antivirus_safeapks = (LinearLayout) findViewById(R.id.ll_antivirus_safeapks);

        //设置旋转动画
        //fromDegrees:旋转开始的角度
        //toDegree: 结束的角度
        //后面的四个是以自身变化，还是以父控件变化
        //Animation.RELATIVE_TO_SELF:以自身旋转，Animation.RELATIVE_TO_PARENT：以父控件旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);//持续时间
        rotateAnimation.setRepeatCount(Animation.INFINITE);//设置旋转此时，INFINTE：一直旋转

        //设置动画插补器，解决旋转卡顿的现象
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        rotateAnimation.setInterpolator(linearInterpolator);
        iv_antivirus_scanner.startAnimation(rotateAnimation);

        //扫描程序，设置进度条进度
        scanner();
    }

    /**
     * 扫描程序
     */
    private void scanner() {
        // 1 获取包的管理者
        final PackageManager pm = getPackageManager();
        tv_antivirus_text.setText("正在初始化64核杀毒引擎...");
        new Thread() {
            @Override
            public void run() {
                super.run();
                //延迟100毫秒，让用户看到这个操作
                SystemClock.sleep(100);
                //2 获取所有安装应用程序信息
                List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
                // 3.1 设置进度条最大进度
                int count = 0;
                pb_antivirus_progressbar.setMax(installedPackages.size());
                // 3.2 设置当前进度
                for (final PackageInfo pasckageInfo : installedPackages) {
                    SystemClock.sleep(100);
                    //3 设置进度条的最大进度和当前进度
                    count++;
                    pb_antivirus_progressbar.setProgress(count);
                    //4 设置扫描显示的软件名称
                    final String name = pasckageInfo.applicationInfo.loadLabel(pm).toString();
                    //在ui线程进行操作
                    runOnUiThread(new Runnable() {//就是分装了handler
                        @Override
                        public void run() {
                            tv_antivirus_text.setText("正在扫描：" + name);
                            //7 获取应用程序的签名信息，并加密
                            Signature[] signatures = pasckageInfo.signatures;//获取应用程序的签名信息,获取签名数组
                            String charsString = signatures[0].toCharsString();
                            //对签名信息进行md5加密
                            String signature = MD5Util.passwordMD5(charsString);
                            System.out.println(name + "  :" + signature);
                            //8 查询md5值是否在病毒数据库中
                            boolean b = AntiVirusDao.quaryAntiVirus(getApplicationContext(), signature);
                            //6 展示扫描的软件的名称信息
                            TextView textView = new TextView(getApplicationContext());
                            // 9 判断根据返回值将病毒标示出来
                            if (b) {
                                //有病毒
                                textView.setTextColor(Color.RED);
                                //将病毒的包名添加扫list集合当中
                                list.add(pasckageInfo.packageName);
                            } else {
                                //没有病毒
                                textView.setTextColor(Color.BLACK);
                            }
                            textView.setText(name);
                            //textView添加到线性布局中
                            //ll_antivirus_safeapks.addView(textView);
                            ll_antivirus_safeapks.addView(textView, 0);//index:表示将textview添加到线性布局的按个位置
                        }
                    });
                }
                // 5 扫描完成，显示扫描完成信息，同时旋转动画停止
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 10 根据是否有病毒进行显示判断
                        if (list.size() > 0) {
                            //有病毒
                            tv_antivirus_text.setText("扫描完成，发现" + list.size() + "个病毒");
                            //卸载病毒应用
                            final AlertDialog.Builder builder = new AlertDialog.Builder(AutivirusActivity.this);
                            builder.setTitle("提醒!发现"+list.size()+"个病毒");
                            builder.setIcon(R.drawable.ic_launcher);
                            builder.setMessage("是否卸载病毒应用");
                            builder.setPositiveButton("卸载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //卸载操作
                                    for (String packagname : list) {
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.DELETE");
                                        intent.addCategory("android.intent.category.DEFAULT");
                                        intent.setData(Uri.parse("package:" + packagname));
                                        startActivity(intent);
                                    }
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("取消",null);
                            builder.show();
                        } else {
                            //设置显示信息以及停止动画
                            tv_antivirus_text.setText("扫描完成，未发现病毒");
                        }
                        iv_antivirus_scanner.clearAnimation();//移出动画
                    }
                });
            }
        }.start();
    }
}
