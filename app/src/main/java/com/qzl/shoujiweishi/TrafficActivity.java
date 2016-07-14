package com.qzl.shoujiweishi;

import android.net.TrafficStats;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;

public class TrafficActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);

       /* TrafficStats.getMobileRxBytes();//获取手机下载流量
        TrafficStats.getMobileTxBytes();// 获取手机上传流量

        TrafficStats.getTotalRxBytes();//获取下载的总流量
        TrafficStats.getTotalTxBytes();//获取上传的总流量

        TrafficStats.getUidRxBytes(uid);//获取某个应用程序下载的流量，uid：应用程序的userid
        TrafficStats.getUidTxBytes(uid);//获取某个应用程序上传的流量，uid：应用程序的userid*/
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.dl);
        dl.openDrawer(Gravity.RIGHT);//表示默认打开那个方式布局
        dl.openDrawer(Gravity.LEFT);
    }
}
