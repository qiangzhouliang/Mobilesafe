package com.qzl.shoujiweishi.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Q on 2016-06-06.
 */
public abstract class MyAsycnTask {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            postTask();
        }
    };
    /**
     * 在子线程之前执行的方法
     */
    public abstract void preTask();
    /**
     * 在子线程之中执行的方法
     */
    public abstract void doinBack();
    /**
     * 在子线程之后执行的方法
     */
    public abstract void postTask();

    /**
     * 执行
     */
    public void execute(){
        preTask();
        new Thread(){
            @Override
            public void run() {
                doinBack();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
