package com.qzl.shoujiweishi.bean;

/**
 * Created by Qzl on 2016-07-09.
 */
public class BlackNumInfo {
    private String blacknum;//黑名单号码
    private int mode;//拦截模式

    public String getBlacknum() {
        return blacknum;
    }

    public void setBlacknum(String blacknum) {
        this.blacknum = blacknum;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        // 0 1 2
        //对mode进行判断，在这里判断，之后再调运该方法时，就不用再去对mode进行判断
        if(mode >= 0 && mode <= 2){
            this.mode = mode;
        }else {
            this.mode = 0;
        }
    }

    public BlackNumInfo() {
    }

    /**
     * 方便添加数据
     * @param blacknum
     * @param mode
     */
    public BlackNumInfo(String blacknum, int mode) {
        this.blacknum = blacknum;
        if(mode >= 0 && mode <= 2){
            this.mode = mode;
        }else {
            this.mode = 0;
        }
    }

    /**
     * 方便我们输出测试数据使用
     * @return
     */
    @Override
    public String toString() {
        return "BlackNumInfo{" +
                "blacknum='" + blacknum + '\'' +
                ", mode=" + mode +
                '}';
    }
}
