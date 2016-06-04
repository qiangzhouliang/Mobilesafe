package com.qzl.shoujiweishi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private GridView gridView_home_gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局文件
        setContentView(R.layout.activity_home);
        gridView_home_gridview = (GridView) findViewById(R.id.gridView_home_gridview);
        gridView_home_gridview.setAdapter(new Myadapter());
    }

    private class Myadapter extends BaseAdapter{

        //设置条目的个数
        @Override
        public int getCount() {
            return 9;
        }

        //获取条条目对应的数据
        @Override
        public Object getItem(int position) {
            return null;
        }

        //获取条目的id
        @Override
        public long getItemId(int position) {
            return 0;
        }

        //设置条目的样式
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*TextView textView = new TextView(getApplicationContext());
            textView.setText("第"+position+"个条目");//position:代表是条目的位置，从0开始0-8*/
            //将布局文件转化成view对象
            View.inflate(getApplicationContext(),)
            return ;
        }
    }
}
