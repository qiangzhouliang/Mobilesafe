package com.qzl.shoujiweishi;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
    //用户的程序的集合
    private List<AppInfo> userappinfo;
    //系统程序的集合
    private List<AppInfo> systemappinfo;
    private TextView tv_softmanager_usetorsystem;

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
        listViewOnscroll();
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
            AppInfo appInfo;
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
}
