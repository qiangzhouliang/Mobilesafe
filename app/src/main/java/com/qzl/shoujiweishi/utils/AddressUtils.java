package com.qzl.shoujiweishi.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by Q on 2016-06-11.
 */
public class AddressUtils {
    /**
     * 动态获取服务是否开启的
     * @return
     */
    public static boolean isRunningService(String className,Context context){
        //进程的管理者，活动的管理者
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取正在运行的服务
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);//maxNum 返回正在运行的服务的上限个数，最多返回多少个服务
        //遍历集合
        for (ActivityManager.RunningServiceInfo runningServiceInfo: runningServices) {
            //获取控件的标识
            ComponentName service = runningServiceInfo.service;
            //获取正在运行的服务的全类名
            String serviceName = service.getClassName();
            //将获取到的正在运行的服务的全类名和传过来的服务的全类名比较，一致标识服务正在运行，返回true，不一致表示服务没有运行，返回false
            if(className.equals(serviceName)){
                return true;
            }
        }
        return false;
    }
}
