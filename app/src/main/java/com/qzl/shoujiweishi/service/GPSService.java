package com.qzl.shoujiweishi.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

import java.util.List;

public class GPSService extends Service {
    private LocationManager locationManager;
    private MyLocationListener myLocationListener;
    private SharedPreferences sp;

    public GPSService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config",MODE_PRIVATE);
        //1.获取位置的管理者
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2.获取定位方式
        //2.1获取所有的定位方式
        //enabledOnly : true : 返回所有可用的定位方式
        List<String> providers = locationManager.getProviders(true);
        for (String string : providers) {
            System.out.println(string);
        }
        //2.2获取最佳的定位方式
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);//设置是否可以定位海拔，true：可以定位海拔，一定返回gps定位
        //criteria : 设置定位的属性，决定使用什么定位方式
        //enabledOnly : true : 定位可用的就返回
        String bestProvider = locationManager.getBestProvider(criteria, true);
        System.out.println("最佳的定位方式:" + bestProvider);
        //3.定位
        myLocationListener = new MyLocationListener();
        //provider : 定位方式
        //minTime : 定位的最小时间间隔
        //minDistance : 定位的最小距离间隔
        //listener : LocationListener：回调监听
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);
    }

    private class MyLocationListener implements LocationListener {
        //当定位位置改变得时候调运
        //location : 当前的位置
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();//获取维度，平行于赤道
            double longitude = location.getLongitude();//获取经度
            //给安全号码发送一个包含经纬度坐标的短信
            SmsManager smsManage = SmsManager.getDefault();
            System.out.println(sp.getString("safenum","5556"));
            smsManage.sendTextMessage(sp.getString("safenum","5556"),null,"longitude:"+longitude+"latitude:"+latitude,null,null);
            //停止服务
            stopSelf();
        }

        //当定位状态改变时调运
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        //定位可用的时候调运
        @Override
        public void onProviderEnabled(String provider) {

        }

        //定位不可用的时候调运
        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭gps定位，高版本中已经不能这么做了，高版本中规定关闭和开启gps必须交由用户自己去实现
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.removeUpdates(myLocationListener);
    }
}
