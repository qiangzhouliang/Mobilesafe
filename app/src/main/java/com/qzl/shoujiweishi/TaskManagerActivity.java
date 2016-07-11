package com.qzl.shoujiweishi;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qzl.shoujiweishi.bean.TaskInfo;
import com.qzl.shoujiweishi.engine.TaskEngine;
import com.qzl.shoujiweishi.utils.MyAsycnTask;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends AppCompatActivity {

    private ListView lv_taskmanager_processes;
    private ProgressBar loading;
    private List<TaskInfo> list;
    //用户的程序的集合
    private List<TaskInfo> userappinfo;
    //系统程序的集合
    private List<TaskInfo> systemappinfo;
    private MyAdapter myAdapter;
    private TaskInfo taskInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        lv_taskmanager_processes = (ListView) findViewById(R.id.lv_taskmanager_processes);
        loading = (ProgressBar) findViewById(R.id.loading);
        //加载数据
        fillData();
        //listView的条目点击事件
        listViewItemClick();
    }

    /**
     * listView的条目点击事件
     */
    private void listViewItemClick() {
        lv_taskmanager_processes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //动态改变checkBox状态
                // 1 屏蔽用户程序和系统程序（。。。个）弹出气泡
                if(position == 0 || position == userappinfo.size()+1){
                    return;
                }
                //2 获取条目所对应的应用程序的信息
                //数据就要从userappinfo和systemappinfo中获取
                if(position <= (userappinfo.size())){
                    //用户程序中获取
                    taskInfo = userappinfo.get(position-1);
                }else {
                    //系统程序中获取
                    taskInfo = systemappinfo.get(position - userappinfo.size() - 2);
                }
                //3 根据之前保存的checkbox的状态设置点击之后的状态，原先选中，点击之后不选中
                if(taskInfo.ischecked()){
                    taskInfo.setIschecked(false);
                }else {
                    taskInfo.setIschecked(true);
                }
                //4 更新界面
                //myAdapter.notifyDataSetChanged();//更新整个界面
                //只更新点击的界面
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.cb_itemtaskmanager_ischecked.setChecked(taskInfo.ischecked());
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
                list = TaskEngine.getTaskAllInfo(getApplicationContext());
                userappinfo = new ArrayList<TaskInfo>();
                systemappinfo = new ArrayList<TaskInfo>();
                for (TaskInfo taskinfo:list) {
                    //将数据分别存放在用户程序集合和系统程序集合
                    if(taskinfo.isUser()){
                        userappinfo.add(taskinfo);
                    }else {
                        systemappinfo.add(taskinfo);
                    }
                }
            }
            //在子线程之后
            @Override
            public void postTask() {
                if(myAdapter == null) {
                    myAdapter = new MyAdapter();
                    //用适配器加载数据
                    lv_taskmanager_processes.setAdapter(myAdapter);
                }else {
                    myAdapter.notifyDataSetChanged();
                }
                loading.setVisibility(View.INVISIBLE);//隐藏进度条
            }
        }.execute();
    }
    private class MyAdapter extends BaseAdapter {
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
                textView.setText("用户进程（"+userappinfo.size()+"）");
                return textView;//只有return了  才能显示
            }else if(position == userappinfo.size()+1){
                //添加系统程序字样
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("系统进程（"+systemappinfo.size()+"）");
                return textView;
            }
            View view;
            ViewHolder viewHolder;
            //复用的时候判断复用view对象的类型
            if (convertView != null && convertView instanceof RelativeLayout){
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }else {
                view = View.inflate(getApplicationContext(),R.layout.item_taskmanager,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemtaskmanager_icon = (ImageView) view.findViewById(R.id.iv_itemtaskmanager_icon);
                viewHolder.tv_taskmanager_name = (TextView) view.findViewById(R.id.tv_taskmanager_name);
                viewHolder.tv_taskmanager_ram = (TextView) view.findViewById(R.id.tv_taskmanager_ram);
                viewHolder.cb_itemtaskmanager_ischecked = (CheckBox) view.findViewById(R.id.cb_itemtaskmanager_ischecked);
                //将viewholder和view对象绑定
                view.setTag(viewHolder);
            }
            //1 获取应用程序信息
            //数据就要从userappinfo和systemappinfo中获取
            if(position <= (userappinfo.size())){
                //用户程序中获取
                taskInfo = userappinfo.get(position-1);
            }else {
                //系统程序中获取
                taskInfo = systemappinfo.get(position - userappinfo.size() - 2);
            }
            //设置显示数据 null.方法，参数为空
            if(taskInfo.getIcon() == null){
                viewHolder.iv_itemtaskmanager_icon.setImageResource(R.mipmap.ic_launcher);
            }else {
                viewHolder.iv_itemtaskmanager_icon.setImageDrawable(taskInfo.getIcon());
            }
            //名称为空的话，可以设置为包名
            if (TextUtils.isEmpty(taskInfo.getName())){
                viewHolder.tv_taskmanager_name.setText(taskInfo.getPaskageName());
            }else {
                viewHolder.tv_taskmanager_name.setText(taskInfo.getName());
            }
            long ramSize = taskInfo.getRamSize();
            //数据转化
            String formatFileSize = Formatter.formatFileSize(getApplicationContext(),ramSize);
            viewHolder.tv_taskmanager_ram.setText("内存占用:"+formatFileSize);
            //因为checkbox的状态会跟着一起复用，所以一般要动态修改的控件的状态，不会跟着去复用，而是将状态保存到bean对象，在每次复用使用控件的时候
            //根据每个条目对应的bean对象保存的状态，来设置控件显示的相应状态
            if(taskInfo.ischecked()){
                viewHolder.cb_itemtaskmanager_ischecked.setChecked(true);
            }else {
                viewHolder.cb_itemtaskmanager_ischecked.setChecked(false);
            }
            return view;
        }
    }
    /**
     * 控件容器
     */
    static class ViewHolder{
        ImageView iv_itemtaskmanager_icon;
        TextView tv_taskmanager_name,tv_taskmanager_ram;
        CheckBox cb_itemtaskmanager_ischecked;
    }
}
