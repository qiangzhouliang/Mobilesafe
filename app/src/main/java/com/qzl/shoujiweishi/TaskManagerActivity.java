package com.qzl.shoujiweishi;

import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.Toast;

import com.qzl.shoujiweishi.bean.TaskInfo;
import com.qzl.shoujiweishi.engine.TaskEngine;
import com.qzl.shoujiweishi.utils.MyAsycnTask;
import com.qzl.shoujiweishi.utils.TaskUtil;

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
    private TextView tv_taskmanager_process;
    private TextView tv_taskmanager_freeandtotalram;
    private int processCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        lv_taskmanager_processes = (ListView) findViewById(R.id.lv_taskmanager_processes);
        loading = (ProgressBar) findViewById(R.id.loading);
        tv_taskmanager_process = (TextView) findViewById(R.id.tv_taskmanager_process);
        tv_taskmanager_freeandtotalram = (TextView) findViewById(R.id.tv_taskmanager_freeandtotalram);

        //设置显示数据
        //获取相应的数据
        //获取当前运行的进程个数
        processCount = TaskUtil.getProcessCount(getApplicationContext());
        tv_taskmanager_process.setText("运行中的进程：\n"+ processCount +"个");
        //剩余内存和总内存
        freeandtotaoRam();

        //加载数据
        fillData();
        //listView的条目点击事件
        listViewItemClick();
    }

    /**
     * 剩余内存和总内存
     */
    private void freeandtotaoRam() {
        //获取剩余，总内存
        long availableRam = TaskUtil.getAcailableRam(getApplicationContext());
        //数据转化
        String availaRam = Formatter.formatFileSize(getApplicationContext(),availableRam);
        //根据不同的SDK版本去调运不同的方法
        // 1 获取当前的sdk版本
        int sdkVersion = Build.VERSION.SDK_INT;
        long totalRam;
        if(sdkVersion >= 16) {
            totalRam = TaskUtil.getTotalRam(getApplicationContext());
        }else {
            totalRam = TaskUtil.getTotalRam();
        }
        //数据转化
        String totalram = Formatter.formatFileSize(getApplicationContext(),totalRam);
        tv_taskmanager_freeandtotalram.setText("剩余/总内存：\n"+availaRam+"/"+totalram);
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
                    //如果是当前应用，不能设置为true
                    if(!taskInfo.getPaskageName().equals(getPackageName())) {
                        taskInfo.setIschecked(true);
                    }
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

    /**
     *全选点击事件
     * @param view
     */
    public void all(View view) {
        //用户进程
        for (int i = 0; i < userappinfo.size(); i++) {
            if(!userappinfo.get(i).getPaskageName().equals(getPackageName())) {
                userappinfo.get(i).setIschecked(true);
            }
        }
        //系统进程
        for (int i = 0; i < systemappinfo.size(); i++) {
            systemappinfo.get(i).setIschecked(true);
        }
        //更新界面
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 取消全选
     * @param view
     */
    public void cancel(View view) {
        //用户进程
        for (int i = 0; i < userappinfo.size(); i++) {
            userappinfo.get(i).setIschecked(false);
        }
        //系统进程
        for (int i = 0; i < systemappinfo.size(); i++) {
            systemappinfo.get(i).setIschecked(false);
        }
        //更新界面
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 清理
     * @param view
     */
    public void clear(View view) {
        //1 获取进程的管理者
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //保存杀死进程信息的集合
        List<TaskInfo> deleteTaskInos = new ArrayList<>();
        for (int i = 0; i < userappinfo.size(); i++) {
            if (userappinfo.get(i).ischecked()){
                //杀死进程
                //packageName：进程的包名，杀死后台进程
                activityManager.killBackgroundProcesses(userappinfo.get(i).getPaskageName());
                deleteTaskInos.add(userappinfo.get(i));//将杀死的进程信息保存到集合中
            }
        }
        for (int i = 0; i < systemappinfo.size(); i++) {
            if (systemappinfo.get(i).ischecked()){
                //杀死进程
                //packageName：进程的包名，杀死后台进程
                activityManager.killBackgroundProcesses(systemappinfo.get(i).getPaskageName());
                deleteTaskInos.add(systemappinfo.get(i));//将杀死的进程信息保存到集合中
            }
        }
        long memory = 0;
        //遍历deleteTaskInos，分别从usernameinfo和systemAppInfos中的数据
        for (TaskInfo taskIno : deleteTaskInos) {
            if (taskIno.isUser()){
                userappinfo.remove(taskIno);
            }else {
                systemappinfo.remove(taskIno);
            }
            memory += taskIno.getRamSize();
        }
        //数据转换
        String deletesize = Formatter.formatFileSize(getApplicationContext(),memory);
        Toast.makeText(getApplicationContext(), "共清理"+deleteTaskInos.size()+"个进程，释放"+deletesize+"内存空间", Toast.LENGTH_SHORT).show();
        //更改运行中的进程个数以及剩余总内存
        processCount = processCount - deleteTaskInos.size();
        tv_taskmanager_process.setText("运行中进程：\n"+processCount+"个");
        //更改剩余和总内存,重新获取剩余总内存
        freeandtotaoRam();
        //为下次清理进程做准备
        deleteTaskInos.clear();
        deleteTaskInos = null;
        //更新界面
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 设置
     * @param view
     */
    public void setting(View view) {

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
            //判断如果是我们的应用程序的话，就吧checkbox隐藏，不是的话，就显示,在getView中，有if必须有else
            if(taskInfo.getPaskageName().equals(getPackageName())){
                viewHolder.cb_itemtaskmanager_ischecked.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.cb_itemtaskmanager_ischecked.setVisibility(View.VISIBLE);
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
