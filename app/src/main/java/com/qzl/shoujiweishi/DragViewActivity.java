package com.qzl.shoujiweishi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DragViewActivity extends Activity {

    private LinearLayout ll_dragview_toast;
    private SharedPreferences sp;
    private int width;
    private int height;
    private TextView tv_dragview_top;
    private TextView tv_dragview_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        setContentView(R.layout.activity_drag_view);
        ll_dragview_toast = (LinearLayout) findViewById(R.id.ll_dragview_toast);
        tv_dragview_top = (TextView) findViewById(R.id.tv_dragview_top);
        tv_dragview_bottom = (TextView) findViewById(R.id.tv_dragview_bottom);
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
        //获取屏幕宽度
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //windowManager.getDefaultDisplay().getWidth();
        DisplayMetrics outMetrics = new DisplayMetrics();//创建一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);//给白纸设置宽高
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;
        //回显
        if(y > height/2){
            //隐藏下方显示上方
            tv_dragview_bottom.setVisibility(View.INVISIBLE);
            tv_dragview_top.setVisibility(View.VISIBLE);
        }else {
            //隐藏上方显下方
            tv_dragview_bottom.setVisibility(View.VISIBLE);
            tv_dragview_top.setVisibility(View.INVISIBLE);
        }
        setTouch();
        //双击居中
        setDoubleClick();
    }
    long[] mHits = new long[2];//2表示双击
    /**
     * 双击居中事件
     */
    private void setDoubleClick() {
        //点击事件 ： 按下 + 抬起
        ll_dragview_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拷贝数组的操作
                //mHits : 拷贝的原数组
                //1 :从哪个位置拷贝
                //mHits:拷贝的目标数组
                //0 : 从目标数组的那个位置开始写
                //mHits.length-1:拷贝的长度
                System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();//将离开机的时间设置给数组的第二个元素，离开机时间：毫秒值，手机休眠不算
                if (mHits[0] >= (SystemClock.uptimeMillis()-500)) {//判断是否是多击操作
                    System.out.println("双击了。。。。");
                    //双击居中
                    int l = (width - ll_dragview_toast.getWidth())/2;
                    int t = (height - 25 -ll_dragview_toast.getHeight())/2;
                    ll_dragview_toast.layout(l,t,l+ll_dragview_toast.getWidth(),t+ll_dragview_toast.getHeight());
                    //保存控件坐标
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("x",l);
                    editor.putInt("y",t);
                    editor.commit();
                }
            }
        });
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

                        //再绘制控件之前，判断ltrb的值是否超出屏幕的大小，如果是，就不再进行绘制控件的操作了
                        if(l < 0 || r > width || t < 0 || b > height - 25){
                            break;
                        }
                        ll_dragview_toast.layout(l,t,r,b);//重新绘制控件
                        //判断textView的隐藏显示了
                        int top = ll_dragview_toast.getTop();
                        if(top > height/2){
                            //隐藏下方显示上方
                            tv_dragview_bottom.setVisibility(View.INVISIBLE);
                            tv_dragview_top.setVisibility(View.VISIBLE);
                        }else {
                            //隐藏上方显下方
                            tv_dragview_bottom.setVisibility(View.VISIBLE);
                            tv_dragview_top.setVisibility(View.INVISIBLE);
                        }
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
                //true ： 事件消费了，执行了，false：表示事件被拦截了，有点击事件时，是false，没点击事件时，为false
                return false;
            }
        });
    }
}
