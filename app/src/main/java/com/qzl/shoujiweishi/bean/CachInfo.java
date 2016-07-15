package com.qzl.shoujiweishi.bean;

/**
 * 保存有缓存信息的bean类
 * Created by Qzl on 2016-07-15.
 */
public class CachInfo {
    private String packageName;
    private String cachSize;

    public CachInfo() {
    }

    public CachInfo(String packageName, String cachSize) {
        this.packageName = packageName;
        this.cachSize = cachSize;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCachSize() {
        return cachSize;
    }

    public void setCachSize(String cachSize) {
        this.cachSize = cachSize;
    }
}
