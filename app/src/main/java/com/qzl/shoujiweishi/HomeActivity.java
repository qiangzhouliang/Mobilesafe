package com.qzl.shoujiweishi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
        gridView_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //position : 条目的位置  0-8
                //根据条目的位置判断用户点击那个条目
                switch (position) {
                    case 8://设置中心
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private class Myadapter extends BaseAdapter{
        //存放图片id
        int[] imageId = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
                R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
                R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };
        String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
                "高级工具", "设置中心" };

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
            View view = View.inflate(getApplicationContext(), R.layout.item_home, null);
            //每个条目的样式都不一样，初始化控件，去设置控件的值
            //view.findViewById ： 是从item_home布局文件中找控件，findViewById：是从activity_home中找控件了
            ImageView imageView_itemhome_icon = (ImageView) view.findViewById(R.id.iv_itemhome_icon);
            TextView tv_itemhome_text = (TextView) view.findViewById(R.id.tv_itemhome_text);
            imageView_itemhome_icon.setImageResource(imageId[position]);//给imageView设置图片,根据条目的位置从图片数组中获取相应的图片
            tv_itemhome_text.setText(names[position]);
            return view;
        }

    }
}
