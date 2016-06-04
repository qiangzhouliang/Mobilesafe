package com.qzl.shoujiweishi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.qzl.shoujiweishi.utils.StreamUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    private static final int MSG_UPDATE_DIALOG = 1;
    private static final int MSG_ENTER_HOME = 2;
    private static final int MSG_SERVER_ERROR = 3;
    private static final int MSG_URL_ERROR = 4;
    private static final int MSG_IO_ERROR = 5;
    private static final int MSG_JSON_ERROR = 6;

    private TextView tv_splash_versionname,tv_splash_plan;
    private String code,apkurl,des;
    private int statrTime;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_UPDATE_DIALOG:
                    //弹出对话框
                    showdialog();
                    break;
                case MSG_ENTER_HOME:
                    enterHome();
                    break;
                case MSG_SERVER_ERROR:
                    //连接失败，服务器出现异常
                    Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_IO_ERROR:
                    Toast.makeText(getApplicationContext(), "亲，网络没有连接..", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_URL_ERROR:
                    //方便后期定位异常
                    Toast.makeText(getApplicationContext(), "错误号："+MSG_URL_ERROR, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_JSON_ERROR:
                    Toast.makeText(getApplicationContext(), "错误号："+MSG_JSON_ERROR, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
            }
        }
    };

    /**
     * 用来弹出对话框
     */
    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框不能消失
        builder.setCancelable(false);
        //设置标题
        builder.setTitle("新版本：" + code);
        //设置对话框图标
        builder.setIcon(R.mipmap.ic_launcher);
        //设置对话框的描述信息
        builder.setMessage(des);
        //设置升级取消的按钮
        builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //3 下载最新版本
                download();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //1 隐藏对话框
                dialog.dismiss();
                //2 跳转到主界面
                enterHome();
            }
        });
        //显示对话框
        //builder.create().show();//两种方式，效果一样
        builder.show();
    }

    /**
     * 3 下载最新版本操作
     */
    private void download() {
        HttpUtils httpUtils = new HttpUtils();
        //判断sd卡是否挂载
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //url:新版本下载路径
            //target：保存新版本目录
            //callback：RequestCallBack
            httpUtils.download(apkurl, "/mnt/sdcard/mobliesafe_2.apk", new RequestCallBack<File>() {
                //下载成功调运
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //4 安装最新版本
                    installAPK();
                }

                //下载失败
                @Override
                public void onFailure(HttpException e, String s) {

                }

                //显示当前下载进度的操作
                //total ： 下载总进度
                //current ： 下载当前进度
                //isUploading ： 是否支持断点续传
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    //设置显示下载进度的textView可见，同时设置相应的下载进度
                    tv_splash_plan.setVisibility(View.VISIBLE);//设置控件是否可见
                    tv_splash_plan.setText(current + "/" + total);
                }
            });
        }
    }

    /**
     * 4 安装最新版本
     */
    private void installAPK() {
        /**
         *  <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <category android:name="android.intent.category.DEFAULT" />
         <data android:scheme="content" /> //content : 从内容提供者中获取数据  content://
         <data android:scheme="file" /> // file : 从文件中获取数据
         <data android:mimeType="application/vnd.android.package-archive" />
         </intent-filter>
         */
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        //单独设置会相互覆盖
        /*intent.setData(Uri.fromFile(new File("/mnt/sdcard/mobliesafe_2.apk")));
        intent.setType("application/vnd.android.package-archive");*///这两种方法不能共存
        intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/mobliesafe_2.apk")),"application/vnd.android.package-archive");
        //在当前activity退出时，会调运之前activity的onActivityResult方法
        //requestCode : 请求码，用来标示是从哪个activity跳转过来的
        // ABC a->c b->c ,c区分intent是从哪个activity传递过来的,这时候就要用到请求码了
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    /**
     * 跳转到主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        //移除splash界面
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
        tv_splash_plan = (TextView) findViewById(R.id.tv_splash_plan);
        tv_splash_versionname.setText("版本号:"+getVersionName());

        update();
    }

    /**
     * 提醒用户更新版本
     */
    private void update() {
        //1 连接服务器，查看是否有最新版本，联网操作，耗时操作，4.0以后不允许在主线程中执行，放到子线程中去执行
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                //在连接之前获取一个时间
                statrTime = (int) System.currentTimeMillis();
                //1.1 连接服务器
                try {
                    //1.1.1 设置连接路径
                    URL url = new URL("http://14906t1q06.iask.in:10082/updateinfo.html");
                    //1.1.2 获取连接操作
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //1.2.3 设置超时时间
                    conn.setConnectTimeout(5000);//设置连接超时时间
                    //conn.setReadTimeout(5000);//设置读取超时时间
                    //1.1.4 设置请求方式
                    conn.setRequestMethod("GET");//post
                    //1.1.5 获取服务器返回的状态码,200,404,500
                    int responseCode = conn.getResponseCode();
                    if(responseCode == 200){
                        //连接成功,获取服务器返回的数据，code：新版本的版本号 apkurl：新版本下载路径 des：描述信息，告诉用户增加了那些功能，修改了那些bug
                        //获取数据之前，服务器是如何封装数据的 xml json
                        System.out.println("连接成功了。。。。。");
                        //获取服务器返回的流信息
                        InputStream in = conn.getInputStream();
                        //将获取到的流信息转化成字符串
                        String json = StreamUtil.parserStreamUtil(in);
                        //解析json数据
                        JSONObject jsonObject = new JSONObject(json);
                        //获取数据
                        code = jsonObject.getString("code");
                        apkurl = jsonObject.getString("apkurl");
                        des = jsonObject.getString("des");
                        System.out.println("code = "+code+"apkurl = "+apkurl+"des = "+des);
                        //1.2 查看是否有最新版本
                        //判断服务器返回的版本号和当前应用程序的版本号是否一致,一致，就表示没有最新版本，不一致，就表示有最新版本
                        if(code.equals(getVersionName())){
                            //没有最新版本
                            message.what = MSG_ENTER_HOME;
                        }else {
                            //有最新版本
                            //2 弹出对话框，提醒用户更新版本
                            message.what = MSG_UPDATE_DIALOG;

                        }
                    }else {
                        //连接失败
                        System.out.println("连接失败。。。。。");
                        message.what = MSG_SERVER_ERROR;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = MSG_URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = MSG_IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = MSG_JSON_ERROR;
                }finally {
                    //不管有没有异常，都会执行
                    //处理连接外网连接时间的处理
                    //在连接成功之后再去获取一个时间
                    int endTime = (int) System.currentTimeMillis();
                    //比较两个时间的时间差，如果小于两秒，睡两秒，大于两秒，不睡
                    int dTime = endTime - statrTime;
                    if(dTime < 2000){
                        //睡两秒钟
                        SystemClock.sleep(2000 - dTime);//始终都是睡两秒钟
                    }
                    handler.sendMessage(message);
                }
            }
        }).start();
    }


    /**
     * 获取当前应用程序的版本号
     */
    private String getVersionName(){
        //包的管理者，获取清单文件中的所有信息
        PackageManager pm = getPackageManager();
        String versionName = "";
        try {
            //根据包名获取清单文件中的信息,返回一个保存有清单文件信息的javabean
            //packagename：应用程序的包名，
            // flags：指定信息的标签，0：获取基础的信息，比如包名，版本号，要想获取权限等信息，必须通过标签来指定才能获取
            //GET_PERMISSION:标签的含义：除了获取基本信息之外，还会额外获取权限信息
            //getPackageName:获取当前应用程序的包名
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(),0);
            //获取版本号名称
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //包名找不到异常
            e.printStackTrace();
        }
        return versionName;
    }

}
