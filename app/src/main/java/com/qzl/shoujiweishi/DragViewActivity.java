package com.qzl.shoujiweishi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DragViewActivity extends Activity {

    private LinearLayout ll_dragview_toast;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        setContentView(R.layout.activity_drag_view);
        ll_dragview_toast = (LinearLayout) findViewById(R.id.ll_dragview_toast);
        //设置控件的回显操作
        //1.获取保存的左边
        int x = sp.getInt("x",0);
        int y = sp.getInt("y",0);
        System.out.println("x :"+x+"  y :"+y);
        //2.重新绘制控件
        //获取控件的宽高，在oncreate里面无法获取控件的宽高
        /*int width = ll_dragview_toast.getWidth();
        int height = ll_dragview_toast.getHeight();
        System.out.println("width :"+width+" height :"+height);
        ll_dragview_toast.layout(x,y,x+width,y+height);*/
        //在初始化控件之前，重新设置控件的属性
        //获取父控件的属性规则，父控件的layoutparams
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_dragview_toast.getLayoutParams();
        //设置相应的属性
        //leftMargin: 距离父控件左边框的距离，跟布局文件中layout_marginLeft属性相似
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        ll_dragview_toast.setLayoutParams(layoutParams);
        setTouch();
    }

    /**
     * 设置触摸监听的控件
     */
    private void setTouch() {
        ll_dragview_toast.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;
            //v:当前控件
            //event:控件执行的事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //event.getAction() 获取控件执行的事件
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //按下的事件
                        System.out.println("按下了。。。。");
                        //1 按下控件，记录开始的x和y的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动的事件
                        System.out.println("移动了。。。");
                        //2 移动到新的位置，记录新的位置的x和y的坐标
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        //3 计算偏移量
                        int dx = newX - startX;
                        int dy = newY - startY;
                        //4.移动相应的偏移量，重新绘制控件
                        //这里获取的是原控件距离父控件左边的顶部的距离
                        int l = ll_dragview_toast.getLeft();//控件距离左边的距离
                        int t = ll_dragview_toast.getTop();
                        //获取新的控件的距离父控件左边和顶部的距离
                        l += dx;
                        t += dy;
                        int r = l + ll_dragview_toast.getWidth();
                        int b = t + ll_dragview_toast.getHeight();
                        ll_dragview_toast.layout(l,t,r,b);//重新绘制控件
                        //5 更新开始的坐标
                        startX = newX;
                        startY = newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起的事件
                        System.out.println("抬起了。。。。");
                        //保存控件的坐标，保存的是控件的坐标，不是手指的坐标
                        //获取控件坐标
                        int x = ll_dragview_toast.getLeft();
                        int y = ll_dragview_toast.getTop();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("x",x);
                        editor.putInt("y",y);
                        editor.commit();
                    default:
                        break;
                }
                //true ： 事件消费了，执行了，false：表示事件被拦截了
                return true;
            }
        });
    }
}
