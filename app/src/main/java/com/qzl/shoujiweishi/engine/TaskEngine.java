package com.qzl.shoujiweishi.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.qzl.shoujiweishi.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取进程信息
 * Created by Qzl on 2016-07-11.
 */
public class TaskEngine {
    /**
     * 获取系统中所有进程信息的操作
     * @return
     */
    public static List<TaskInfo> getTaskAllInfo(Context context){
        List<TaskInfo> list = new ArrayList<>();
        //1 进程的管理者
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        //2 获取所有真在运行的进程
        List<ActivityManager.RunningAppProcessInfo>runningAppProcess = activityManager.getRunningAppProcesses();
        //遍历集合
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo :runningAppProcess) {
            TaskInfo taskInfo = new TaskInfo();
            //3 获取相应信息
            //获取进程的名称,就是获取包名
            String packageName = runningAppProcessInfo.processName;
            taskInfo.setPaskageName(packageName);
            //获取进程所占用的内存空间 ， int[] pids:输入几个进程的pid，就会返回几个进程所占空间
            Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
            int totalPss = memoryInfo[0].getTotalPss();//获取以kb为单位的内存空间
            //数据转化
            long ramSize = totalPss * 1024;
            taskInfo.setRamSize(ramSize);
            try {
                //获取application信息
                //packages：包名；flags：指定信息标签
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName,0);
                //获取图标
                Drawable icon = applicationInfo.loadIcon(pm);
                taskInfo.setIcon(icon);
                //获取名称
                String name = applicationInfo.loadLabel(pm).toString();
                taskInfo.setName(name);
                //获取程序的所有标签信息，是否是系统程序是以标签的形式展示
                int flags = applicationInfo.flags;
                boolean isUser;
                //判断是不是用户程序
                if((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM){
                    //系统程序
                    isUser = false;
                }else {
                    //用户程序
                    isUser = true;
                }
                //保存信息
                taskInfo.setUser(isUser);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            //taskInfo添加到集合中
            list.add(taskInfo);
        }
        return list;
    }
}
