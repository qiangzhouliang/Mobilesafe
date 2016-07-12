package com.qzl.shoujiweishi.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.qzl.shoujiweishi.R;
import com.qzl.shoujiweishi.receiver.MyWidget;
import com.qzl.shoujiweishi.utils.TaskUtil;

import java.sql.Time;
import java.util.Formatter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 桌面小控件服务
 */
public class WidgetService extends Service {

    private AppWidgetManager appWidgetManager;

    public WidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //更新桌面小控件
        //widget的管理者
        appWidgetManager = AppWidgetManager.getInstance(this);
        //更新操作
        updateWidget();
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
        Timer timer = new Timer();
        //执行操作
        //task：要执行操作
        // when: 延迟时间
        // period:每次执行的间隔时间
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
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
                //更新操作
                appWidgetManager.updateAppWidget(provider,views);
            }
        }, 2000, 2000);
    }
}
