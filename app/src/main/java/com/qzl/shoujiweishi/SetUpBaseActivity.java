package com.qzl.shoujiweishi;

import android.content.Intent;
import android.gesture.Gesture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public abstract class SetUpBaseActivity extends AppCompatActivity {
    //将每个界面中的上一步下一步按钮的操作，抽取到父类中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1、获取收拾识别器
        GestureDetector gestureDetector = new GestureDetector(this,new MyOnGestureListener());
    }
    // base simple
    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener{
       //e1 : 按下的事件，保存有按下的坐标
        //e2 : 抬起的事件，保存有抬起的坐标
        //velocityX : 在x轴上移动的速度
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //得到按下的坐标
            float startX = e1.getRawX();
            //得到抬起的坐标
            float endX = e2.getRawX();
            if((startX - endX) > 100){
                //下一步
                next_activity();

            }
            if((endX - startX) > 100){
                //上一步
                pre_activity();
            }
            //true : 事件执行 false ： 事件不执行
            return true;
        }
    }
    //按钮的点击事件操作
    public void pre(View view){
        pre_activity();
    }
    public void next(View view){
        next_activity();
    }
    //因为父类不知道子类上一步，下一步具体的执行操作方法，所以要创建一个抽象方法，让子类实现这个抽象类，根据自己的特性去实现相应的操作
    //下一步操作
    public abstract void next_activity();
    //上一步操作
    public abstract void pre_activity();
    //在父类中直接对子类中的返回键进行统一处理
    /*@Override
    public void onBackPressed() {
        pre_activity();
        super.onBackPressed();
    }*/

    //监听手机物理按键的点击事件
    //keyCode : 表示物理按键的标识
    //event ： 按键的处理事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断keycode是否是返回键的标识
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //true 是可以屏蔽按键的，高版本的home键是不能屏蔽的
            //return true;
            pre_activity();
        }
        return super.onKeyDown(keyCode, event);
    }
}
