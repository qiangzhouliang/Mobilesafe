package com.qzl.shoujiweishi.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Qzl on 2016-07-12.
 */
public class TaskUtil {
    /**
     * 获取正在运行的进程个数
     * @return
     */
    public static int getProcessCount(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo>runningAppProcess = activityManager.getRunningAppProcesses();
        return runningAppProcess.size();
    }

    /**
     * 获取剩余内存
     * @return
     */
    public static long getAcailableRam(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取内存的一些信息，保存到memoryInfo中
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        //获取空闲的内存
        //memoryInfo.availMem;
        //获取总内存
        //memoryInfo.totalMem;
        return memoryInfo.availMem;
    }
    /**
     * 获取总内存
     * @return
     * @deprecated
     */
    public static long getTotalRam(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取内存的一些信息，保存到memoryInfo中
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        //获取空闲的内存
        //memoryInfo.availMem;
        //获取总内存
        //memoryInfo.totalMem;
        return memoryInfo.totalMem;//16版本之上才有，之下是没有的

    }

    /**
     * 兼容低版本
     * @return
     */
    public static long getTotalRam(){
        File file = new File("/proc/meminfo");
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //读取文件
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String readLine = bufferedReader.readLine();
            //获取数字
            char[] charArray = readLine.toCharArray();
            //遍历数组
            for (char c : charArray) {
                if (c >= '0' && c <= '9'){
                    stringBuilder.append(c);
                }
            }
            String string = stringBuilder.toString();
            //转化成long
            long parseLong = Long.parseLong(string);
            return parseLong * 1024;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
