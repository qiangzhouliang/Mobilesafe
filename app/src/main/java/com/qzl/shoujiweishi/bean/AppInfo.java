package com.qzl.shoujiweishi.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Qzl on 2016-07-10.
 */
public class AppInfo {
    //名称
    private String name;
    //图标
    private Drawable icon;
    //包名
    private String packagName;
    //版本号
    private String versionName;
    //是否安装到SD卡
    private boolean isSDK;
    //是否是用户程序
    private boolean isUser;

    public AppInfo() {
    }

    public AppInfo(String name, Drawable icon, String packagName, String versionName, boolean isSDK, boolean isUser) {
        this.name = name;
        this.icon = icon;
        this.packagName = packagName;
        this.versionName = versionName;
        this.isSDK = isSDK;
        this.isUser = isUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackagName() {
        return packagName;
    }

    public void setPackagName(String packagName) {
        this.packagName = packagName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isSDK() {
        return isSDK;
    }

    public void setSDK(boolean SDK) {
        isSDK = SDK;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", packagName='" + packagName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", isSDK=" + isSDK +
                ", isUser=" + isUser +
                '}';
    }
}
