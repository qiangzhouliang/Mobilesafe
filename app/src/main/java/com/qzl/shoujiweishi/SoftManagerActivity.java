package com.qzl.shoujiweishi;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qzl.shoujiweishi.bean.AppInfo;
import com.qzl.shoujiweishi.engine.AppEngine;
import com.qzl.shoujiweishi.utils.MyAsycnTask;

import java.util.ArrayList;
import java.util.List;

public class SoftManagerActivity extends AppCompatActivity {

    private ListView lv_softmanager_application;
    private ProgressBar loading;
    private List<AppInfo> list;
    AppInfo appInfo;
    //用户的程序的集合
    private List<AppInfo> userappinfo;
    //系统程序的集合
    private List<AppInfo> systemappinfo;
    private TextView tv_softmanager_usetorsystem;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        //初始化控件
        lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
        loading = (ProgressBar) findViewById(R.id.loading);
        tv_softmanager_usetorsystem = (TextView) findViewById(R.id.tv_softmanager_usetorsystem);
        //加载数据
        fillData();
        //listView的滑动监听事件
        listViewOnscroll();
        //listView条目的点击事件
        listViewItemOnclick();
    }

    /**
     * listView条目的点击事件
     */
    private void listViewItemOnclick() {
        lv_softmanager_application.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //view : 条目的对象
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //弹出气泡
                // 1 屏蔽用户程序和系统程序（。。。个）弹出气泡
                if(position == 0 || position == userappinfo.size()+1){
                    return;
                }
                //2 获取条目所对应的应用程序的信息
                //数据就要从userappinfo和systemappinfo中获取
                if(position <= (userappinfo.size())){
                    //用户程序中获取
                    appInfo = userappinfo.get(position-1);
                }else {
                    //系统程序中获取
                    appInfo = systemappinfo.get(position - userappinfo.size() - 2);
                }
                //5 弹出新的气泡之前，先删除旧的气泡
                hidePopuwindow();
                //3 弹出气泡
                View contentView = View.inflate(getApplicationContext(),R.layout.popu_window,null);
                //contentView:显示view对象
                // width，height：view宽高
                popupWindow = new PopupWindow(contentView, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //4 获取条目的位置，让气泡显示在相应的位置上
                int[] location = new int[2];//保存x和y坐标的数组
                view.getLocationInWindow(location);//获取条目x和y的坐标，同时保存到int[]
                //获取x和y的坐标
                int x = location[0];
                int y = location[1];
                //parent:要挂在在哪个控件上
                // gravity，x,y:控制控件显示的位置
                popupWindow.showAtLocation(parent, Gravity.LEFT|Gravity.TOP,x+50,y);
                //6 设置动画
                //缩放动画
                //前四个：控制控件由没有变成有 动画0：没有  1： 整个控件
                // 后四个：控制控件是按照自身还是父控件进行操作
                //RELATIVE_TO_SELF:以自身变化
                //RELATIVE_TO_PARENT:以父控件变化
                ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation.setDuration(500);
                //渐变动画
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f,1.0f);//有半透明变成不透明
                alphaAnimation.setDuration(500);
                //组合动画
                //shareInterpolatoe:是否使用相同的动画插补器 true : 共享，false：各自使用各自的
                AnimationSet animationSet = new AnimationSet(true);
                //添加动画
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);
                //执行动画
                contentView.startAnimation(animationSet);

            }
        });
    }

    /**
     *隐藏气泡的方法
     */
    private void hidePopuwindow() {
        if(popupWindow != null){
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * listView的滑动监听事件
     */
    private void listViewOnscroll() {
        lv_softmanager_application.setOnScrollListener(new AbsListView.OnScrollListener() {
            //滑动状态改变的时候调运
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            //滑动的时候调运
            // view ： listView
            // firstVisibleItem： 界面第一个显示的条目
            // visibleItemCount：显示的条目总个数
            // totalItemCount ： 条目的总个数
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //隐藏气泡
                hidePopuwindow();
                //为null的原因：listView在初始化的时候就会调运onScroll
                if(userappinfo != null && systemappinfo != null) {
                    if (firstVisibleItem >= userappinfo.size() + 1) {
                        tv_softmanager_usetorsystem.setText("系统程序(" + systemappinfo.size() + ")");
                    } else {
                        tv_softmanager_usetorsystem.setText("用户程序(" + userappinfo.size() + ")");
                    }
                }
            }
        });
    }

    /**
     * 加载数据
     */
    private void fillData() {
        new MyAsycnTask(){
            //在子线程之前
            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);
            }
            //在子线程之中
            @Override
            public void doinBack() {
                list = AppEngine.getAppInfo(getApplicationContext());
                userappinfo = new ArrayList<AppInfo>();
                systemappinfo = new ArrayList<AppInfo>();
                for (AppInfo appinfo:list) {
                    //将数据分别存放在用户程序集合和系统程序集合
                    if(appinfo.isUser()){
                        userappinfo.add(appinfo);
                    }else {
                        systemappinfo.add(appinfo);
                    }
                }
            }
            //在子线程之后
            @Override
            public void postTask() {
                //用适配器加载数据
                lv_softmanager_application.setAdapter(new MyAdapter());
                loading.setVisibility(View.INVISIBLE);//隐藏进度条
            }
        }.execute();
    }
    private class MyAdapter extends BaseAdapter{
        //获取总条目数
        @Override
        public int getCount() {
            //list.size() = userappinfo.size() + systemappinfo.size();
            return userappinfo.size() + systemappinfo.size() + 2;
        }
        //获取条目对应的数据
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }
        //获取条目的位置
        @Override
        public long getItemId(int position) {
            return position;
        }
        //加载
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0){
                //添加用户程序（。。。）个TextView
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("用户程序（"+userappinfo.size()+"）");
                return textView;//只有return了  才能显示
            }else if(position == userappinfo.size()+1){
                //添加系统程序字样
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("系统程序（"+systemappinfo.size()+"）");
                return textView;
            }
            View view;
            ViewHolder viewHolder;
            //复用的时候判断复用view对象的类型
            if (convertView != null && convertView instanceof RelativeLayout){
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }else {
                view = View.inflate(getApplicationContext(),R.layout.item_softmanager,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemsoftmanager_icon = (ImageView) view.findViewById(R.id.iv_itemsoftmanager_icon);
                viewHolder.tv_softmanager_name = (TextView) view.findViewById(R.id.tv_softmanager_name);
                viewHolder.tv_softmanager_isSD = (TextView) view.findViewById(R.id.tv_softmanager_isSD);
                viewHolder.tv_softmanager_version = (TextView) view.findViewById(R.id.tv_softmanager_version);
                //将viewholder和view对象绑定
                view.setTag(viewHolder);
            }
            //1 获取应用程序信息
            //数据就要从userappinfo和systemappinfo中获取
            if(position <= (userappinfo.size())){
                //用户程序中获取
                appInfo = userappinfo.get(position-1);
            }else {
                //系统程序中获取
                appInfo = systemappinfo.get(position - userappinfo.size() - 2);
            }
            //设置显示数据 null.方法，参数为空
            viewHolder.iv_itemsoftmanager_icon.setImageDrawable(appInfo.getIcon());
            viewHolder.tv_softmanager_name.setText(appInfo.getName());
            boolean sd = appInfo.isSDK();
            if(sd){
                viewHolder.tv_softmanager_isSD.setText("SD卡");
            }else {
                viewHolder.tv_softmanager_isSD.setText("手机内存");
            }
            viewHolder.tv_softmanager_version.setText(appInfo.getVersionName());
            return view;
        }
    }

    /**
     * 控件容器
     */
    static class ViewHolder{
        ImageView iv_itemsoftmanager_icon;
        TextView tv_softmanager_name,tv_softmanager_isSD,tv_softmanager_version;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //隐藏气泡
        hidePopuwindow();
    }
}
