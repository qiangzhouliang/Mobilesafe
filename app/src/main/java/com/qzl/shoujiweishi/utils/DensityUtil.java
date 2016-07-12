package com.qzl.shoujiweishi.utils;

import android.content.Context;

/**
 * Created by Qzl on 2016-07-12.
 */
public class DensityUtil {

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2qx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density; //获取屏幕的密度
        return (int) (dpValue * scale + 0.5f);//+ 0.5f：为了四舍五入
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
