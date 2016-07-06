package com.qzl.shoujiweishi.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.qzl.shoujiweishi.R;
import com.qzl.shoujiweishi.db.AddressDao;

public class AddressServices extends Service {
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    private WindowManager windowManager;
    private MyOutGoingCallReceiver myOutGoingCallReceiver;
    private TextView textView;
    private SharedPreferences sp;
    private View view;
    public AddressServices() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config",MODE_PRIVATE);
        //代码注册监听外拨电话的广播接收者
        //需要的元素：1 广播接收者，2.设置监听的广播事件
        //1. 设置广播接收者
        myOutGoingCallReceiver = new MyOutGoingCallReceiver();
        // 2. 设置接收的广播事件
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");//设置接收的广播事件
        //3。 注册广播接收者
        registerReceiver(myOutGoingCallReceiver,intentFilter);

        //监听电话状态
        //1 获取电话管理者
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // 2 监听电话状态
        myPhoneStateListener = new MyPhoneStateListener();
        //listener：电话状态的回调监听
        // events : 监听电话的事件  LISTEN_NONE : 不做任何监听操作  LISTEN_CALL_STATE : 监听电话状态的操作
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 外拨电话的而广播接收者
     */
    private class MyOutGoingCallReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //查询外拨点话的号码归属地
            //1 获取外拨电话
            String phone = getResultData();
            //2 查询号码归属地
            String queryAddress = AddressDao.queryAddress(phone, getApplicationContext());
            //3 判断号码归属地是否为空
            if(!TextUtils.isEmpty(queryAddress)){
                //显示toast
                showToast(queryAddress);
            }
        }
    }
    private class MyPhoneStateListener extends PhoneStateListener{
        //监听电话状态的回调方法
        // stats: 电话的的状态 incomingNumber：来电电话
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE://空闲状态,挂断状态
                    //隐藏
                    hideToast();
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                    //查询号码归属地并显示
                    String queryAddress = AddressDao.queryAddress(incomingNumber, getApplicationContext());
                    //判断号码是否为空
                    if(!TextUtils.isEmpty(queryAddress)){
                        //显示号码归属地
                        //Toast.makeText(getApplicationContext(), queryAddress, Toast.LENGTH_SHORT).show();
                        showToast(queryAddress);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                    //通话状态，给外拨电话状态时冲突的
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }

    }

    /**
     * 显示toast
     */
    private void showToast(String queryAddress) {
        int[] bgcolor = new int[] {
                R.color.white,
                R.color.orange, R.color.blue,
                R.color.gray, R.color.green };
        //1 获取windowManger
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //通过这个将布局文件转换成View对象
        view = View.inflate(getApplicationContext(), R.layout.toast_custom,null);
        //初始化控件
        TextView tv_toastcustom_address = (TextView) view.findViewById(R.id.tv_toastcustom_address);
        tv_toastcustom_address.setText(queryAddress);
        //根据归属地提示框风格中设置的风格索引值设置toast显示的背景风格
        view.setBackgroundResource(bgcolor[sp.getInt("which",0)]);
        /*textView = new TextView(getApplicationContext());
        textView.setText(queryAddress);
        textView.setTextSize(30);
        textView.setTextColor(Color.RED);*/
        //3.设置toast的属性
        //layoutparams是toast的属性,控件要添加到那个父控件中,父控件就要使用那个父控件的属性,表示控件的属性规则符合父控件的属性规则
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;//高度包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT; //宽度包裹内容
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //没有焦点
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  // 不可触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // 保持当前屏幕
        params.format = PixelFormat.TRANSLUCENT; // 透明
        params.type = WindowManager.LayoutParams.TYPE_TOAST; // 执行toast的类型
        //设置toast位置
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 100;//不是坐标，表示边框的距离，根据gravity来设置的，如果gravity是left的话表示距离左边框的距离，如果是right的话，表示距离有边框的距离
        params.y = 100;//跟x的含义一样
        //效果冲突的话，以默认的效果为主
        //params.gravity = Gravity.LEFT | Gravity.RIGHT;
        //2 将view对象添加到windowManager中
        //params : layoutparams  控件的属性
        //将params属性设置给view对象，并添加到WindowManager中
        windowManager.addView(view,params);//将一个view对象添加到桌面
    }

    /**
     * 隐藏toast
     */
    public void hideToast(){
        if(windowManager != null && textView != null){
            windowManager.removeView(textView);
            windowManager = null;
            textView = null;
        }
    }

    @Override
    public void onDestroy() {
        //当服务关闭时，取消监听操作
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        //注销外拨电话广播接收者
        unregisterReceiver(myOutGoingCallReceiver);
        super.onDestroy();
    }
}
