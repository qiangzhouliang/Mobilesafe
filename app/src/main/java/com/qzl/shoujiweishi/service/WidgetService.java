package com.qzl.shoujiweishi.service;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.qzl.shoujiweishi.R;
import com.qzl.shoujiweishi.receiver.MyWidget;
import com.qzl.shoujiweishi.utils.TaskUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 桌面小控件服务
 */
public class WidgetService extends Service {

    private AppWidgetManager appWidgetManager;
    private WidgetReceiver widgetReceiver;
    private Timer timer;
    private ScreenOffReceiver screenOffReceiver;
    private ScreenOnReceiver screenOnReceiver;

    public WidgetService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    /**
     * 清理进程的广播接受者
     */
    private class WidgetReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //清理进程
            killProcess();
        }
    }

    /**
     * 清理进程的方法
     */
    private void killProcess() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //获取正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcess = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcess) {
            //判断我们的应用
            if (!runningAppProcessInfo.processName.equals(getPackageName())) {
                am.killBackgroundProcesses(runningAppProcessInfo.processName);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //更新桌面小控件
        //注册清理进程广播接受者
        widReceiver();
        //注册锁屏的广播接收者
        screenReceiver();
        //注册解锁的广播接收者
        scrOnReceiver();
        //widget的管理者
        appWidgetManager = AppWidgetManager.getInstance(this);
        //更新操作
        updateWidget();
    }

    /**
     * 注册解锁的广播接收者
     */
    private void scrOnReceiver() {
        screenOnReceiver = new ScreenOnReceiver();
        //设置接收的广播事件
        IntentFilter screenOnintentFilter = new IntentFilter();
        screenOnintentFilter.addAction(Intent.ACTION_SCREEN_ON);
        //注册广播接收者
        registerReceiver(screenOnReceiver,screenOnintentFilter);
    }

    /**
     * 注册锁屏的广播接收者
     */
    private void screenReceiver() {
        screenOffReceiver = new ScreenOffReceiver();
        //设置接收的广播事件
        IntentFilter screenOffIntentFilter1 = new IntentFilter();
        screenOffIntentFilter1.addAction(Intent.ACTION_SCREEN_OFF);
        //注册广播接收者
        registerReceiver(screenOffReceiver,screenOffIntentFilter1);
    }

    /**
     * 注册清理进程广播接受者
     */
    private void widReceiver() {
        //1 广播接受者
        widgetReceiver = new WidgetReceiver();
        //2 设置接受的广播事件
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("aa.bb.cc");
        //3 注册广播接受者
        registerReceiver(widgetReceiver,intentFilter);
    }
    /**
     * 解锁的广播接收者
     */
    private class ScreenOnReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新桌面小控件
            updateWidget();
        }
    }
    /**
     * 锁屏的广播接收者
     */
    private class ScreenOffReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("锁屏了。。。");
            //清理进程
            killProcess();
            //停止更新widget
            stopUpdates();
        }
    }

    /**
     * 停止更新
     */
    private void stopUpdates() {
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 更新widget
     */
    private void updateWidget() {
        //第一种方式
        /*new Thread(){
            @Override
            public void run() {
                while (true){
                    SystemClock.sleep(2000);
                }
            }
        }.start();*/
        //第二种方式
        //计数器
        timer = new Timer();
        //执行操作
        //task：要执行操作
        // when: 延迟时间
        // period:每次执行的间隔时间
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("widget更新了。。。。。。");
                //实现跟新操作
                //获取组件标识
                ComponentName provider = new ComponentName(WidgetService.this, MyWidget.class);
                //获取远程布局
                //packageName: 应用包名
                // layoutId:widget布局文件
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
                //远程布局是不能通过findviewbyid获取初始化控件的
                //跟新相应布局文件中相应控件的值
                //viewID:更新控件的id
                // text:更新的内容
                views.setTextViewText(R.id.process_count,"正在运行软件："+ TaskUtil.getProcessCount(WidgetService.this));
                views.setTextViewText(R.id.process_memory,"可用内存："+ android.text.format.Formatter.formatFileSize(WidgetService.this,TaskUtil.getAcailableRam(WidgetService.this)));
                //按钮点击事件
                Intent intent = new Intent();
                intent.setAction("aa.bb.cc");//设置要发送的广播，aa.bb.cc：是自定义的广播事件
                //通过发送一个广播表示要执行清理操作，通过接受发送的广播执行清理的操作
                //flags：指定信息的标签  FLAG_UPDATE_CURRENT:更新我们当前的操作
                PendingIntent pendingIntent = PendingIntent.getBroadcast(WidgetService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                // viewID:点击的控件ID
                // pendingIntent:延迟意图 包含了一个intent意图，当点击的时候才去执行这个意图，不点击就不执行意图
                views.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);
                //更新操作
                appWidgetManager.updateAppWidget(provider,views);
            }
        }, 2000, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止更新widget
        stopUpdates();
        //注销清理进程的广播接受者
        if(widgetReceiver != null){
            unregisterReceiver(widgetReceiver);
            widgetReceiver = null;
        }
        if(screenOffReceiver != null){
            unregisterReceiver(screenOffReceiver);
            screenOffReceiver = null;
        }
        if(screenOnReceiver != null){
            unregisterReceiver(screenOnReceiver);
            screenOnReceiver = null;
        }
    }
}
