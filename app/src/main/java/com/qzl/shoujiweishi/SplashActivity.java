package com.qzl.shoujiweishi;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.qzl.shoujiweishi.utils.StreamUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    private TextView tv_splash_versionname;
    private String code,apkurl,des;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
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
                    }else {
                        //连接失败
                        System.out.println("连接失败。。。。。");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
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
