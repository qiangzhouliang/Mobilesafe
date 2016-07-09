package com.qzl.shoujiweishi.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

import com.qzl.shoujiweishi.db.dao.BlackNumDao;

public class BlackNumService extends Service {

    private SmsReceiver smsReceiver;
    private BlackNumDao blackNumDao;

    public BlackNumService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        blackNumDao = new BlackNumDao(getApplicationContext());
        //用代码注册短信到来的广播接收者
        // 1 创建广播接收者
        smsReceiver = new SmsReceiver();
        // 2 设置监听的广播事件了
        IntentFilter intentFilter = new IntentFilter();
        //广播接收者的最高优先级,优先级相同的话，代码注册的优先级先执行
        intentFilter.setPriority(Integer.MAX_VALUE);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //3 注册广播接收者
        registerReceiver(smsReceiver,intentFilter);
    }

    private class SmsReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //解析接收短信的操作
            //70汉字一条短信,71汉字两条短信
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for(Object obj:objs) {
                //解析成SmsMessage
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getMessageBody();//获取短信的内容
                String sender = smsMessage.getOriginatingAddress();//获取发件人
                //根据发件人的号码，获取号码拦截模式
                int mode = blackNumDao.queryBlackNumMode(sender);
                //判断是否是短信拦截或全部拦截
                if(mode == BlackNumDao.SMS || mode == BlackNumDao.ALL){
                    System.out.println("拦截短信");
                    abortBroadcast();//拦截广播事件，拦截短信的操作
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);//注销广播接收者
    }
}
