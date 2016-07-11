package com.qzl.shoujiweishi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qzl.shoujiweishi.bean.AppInfo;
import com.qzl.shoujiweishi.engine.AppEngine;
import com.qzl.shoujiweishi.utils.MyAsycnTask;

import java.util.List;

public class SoftManagerActivity extends AppCompatActivity {

    private ListView lv_softmanager_application;
    private ProgressBar loading;
    private List<AppInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        //初始化控件
        lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
        loading = (ProgressBar) findViewById(R.id.loading);
        //加载数据
        fillData();
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
            return list.size();
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
            View view;
            ViewHolder viewHolder;
            if (convertView != null){
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
            AppInfo appInfo = list.get(position);
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

    static class ViewHolder{
        ImageView iv_itemsoftmanager_icon;
        TextView tv_softmanager_name,tv_softmanager_isSD,tv_softmanager_version;
    }
}
