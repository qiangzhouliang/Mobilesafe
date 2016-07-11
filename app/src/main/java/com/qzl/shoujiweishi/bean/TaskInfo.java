package com.qzl.shoujiweishi.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Qzl on 2016-07-11.
 */
public class TaskInfo {
    //进程名称
    private String name;
    //图标
    private Drawable icon;
    //占用的内存空间
    private long ramSize;
    //包名
    private String paskageName;
    //是否是用户程序
    private boolean isUser;

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    //checkbox是都被选中
    private boolean ischecked = false;

    public TaskInfo() {
    }

    public TaskInfo(String name, Drawable icon, long ramSize, String paskageName, boolean isUser) {
        this.name = name;
        this.icon = icon;
        this.ramSize = ramSize;
        this.paskageName = paskageName;
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

    public long getRamSize() {
        return ramSize;
    }

    public void setRamSize(long ramSize) {
        this.ramSize = ramSize;
    }

    public String getPaskageName() {
        return paskageName;
    }

    public void setPaskageName(String paskageName) {
        this.paskageName = paskageName;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", ramSize=" + ramSize +
                ", paskageName='" + paskageName + '\'' +
                ", isUser=" + isUser +
                '}';
    }
}
