package com.qzl.shoujiweishi.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import com.qzl.shoujiweishi.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取应用程序的方法
 * Created by Qzl on 2016-07-10.
 */
public class AppEngine {
    /**
     * 获取系统中所有应用程序的信息
     */
    public static List<AppInfo> getAppInfo(Context context){
        List<AppInfo> list = new ArrayList<>();
        //获取应用程序信息
        //包的管理者
        PackageManager pm = context.getPackageManager();
        //获取系统中安装的所有软件信息
        List<PackageInfo>  installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            //获取包名
            String packageName = packageInfo.packageName;
            //获取版本号
            String versionName = packageInfo.versionName;
            //获取application的信息
            ApplicationInfo  applicationInfo = packageInfo.applicationInfo;
            //获取应用程序图标
            Drawable icon = applicationInfo.loadIcon(pm);
            //获取应用程序名称
            String name = applicationInfo.loadLabel(pm).toString();
            //获取是否是用户程序
            //获取应用程序中的相关信息，是否是系统程序和是否是安装到sd卡
            boolean isUser;
            int flags = applicationInfo.flags;
            if((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM){
                //系统应用程序
                isUser = false;
            }else {
                //用户程序
                isUser = true;
            }
            //是否安装到sd卡
            boolean isSD;
            if((applicationInfo.FLAG_EXTERNAL_STORAGE  & flags) == applicationInfo.FLAG_EXTERNAL_STORAGE){
                //是安装到了sd卡中
                isSD = true;
            }else {
                //安装到手机中
                isSD = false;
            }
            //添加到bean中
            AppInfo appInfo = new AppInfo(name,icon,packageName,versionName,isSD,isUser);
            //将bean存放到list集合中
            list.add(appInfo);
        }
        return list;
    }
}
