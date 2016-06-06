package com.qzl.shoujiweishi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {
    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("手机重启了。。。 ");
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //检查SIM卡是否发生变化
        //1 获取保存的SIM卡号
        String sp_sim = sp.getString("sim", "");
        // 2 再次获取本地的SIM卡号
        TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //tel.getLine1Number();//获取SIM卡绑定的电话号码 line1：解决双卡双待，（在中国不太实用，运营商一般不会将SIM卡和手机号码绑定）获取主卡电话号码
        String sim = tel.getSimSerialNumber();//获取SIM卡序列号，唯一标示
        // 3 判断两个SIM卡号是否为null
        if(!TextUtils.isEmpty(sp_sim) && !TextUtils.isEmpty(sim)){
            // 4 判断两个SIM卡是否一致，如果一致，就不发送报警短信，如果不一致，就发送
            if(!sp_sim.equals(sim)){
                //发送报警短信
                //短信的管理者
                SmsManager smsManager = SmsManager.getDefault();
                //destinationAddress : 收件人
                //scAddress：短信中心地址，一般为null
                //text : 短信内容
                // sentIntent ： 是否发送成功
                // deliveryIntent : 短信协议，一般为null
                smsManager.sendTextMessage("5556",null,"dage......",null,null);
            }
        }
    }
}
