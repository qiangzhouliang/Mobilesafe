package com.qzl.shoujiweishi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.qzl.shoujiweishi.db.AddressDao;

public class AddressServices extends Service {
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    public AddressServices() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //监听电话状态
        //1 获取电话管理者
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // 2 监听电话状态
        myPhoneStateListener = new MyPhoneStateListener();
        //listener：电话状态的回调监听
        // events : 监听电话的事件  LISTEN_NONE : 不做任何监听操作  LISTEN_CALL_STATE : 监听电话状态的操作
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneStateListener extends PhoneStateListener{
        //监听电话状态的回调方法
        // stats: 电话的的状态 incomingNumber：来电电话
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE://空闲状态,挂断状态
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                    //查询号码归属地并显示
                    String queryAddress = AddressDao.queryAddress(incomingNumber, getApplicationContext());
                    //判断号码是否为空
                    if(!TextUtils.isEmpty(queryAddress)){
                        //显示号码归属地
                        Toast.makeText(getApplicationContext(), queryAddress, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    @Override
    public void onDestroy() {
        //当服务关闭时，取消监听操作
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }
}
