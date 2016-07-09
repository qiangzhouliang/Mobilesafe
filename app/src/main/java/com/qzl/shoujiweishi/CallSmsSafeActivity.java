package com.qzl.shoujiweishi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qzl.shoujiweishi.bean.BlackNumInfo;
import com.qzl.shoujiweishi.db.dao.BlackNumDao;
import com.qzl.shoujiweishi.utils.MyAsycnTask;

import java.util.List;

/**
 * Created by Qzl on 2016-07-09.
 */
public class CallSmsSafeActivity extends Activity {

    private ListView lv_callsmssafe_blacknum;
    private ProgressBar loading;
    private BlackNumDao blackNumDao;
    private List<BlackNumInfo> queryAllBlackNum;
    private ImageView iv_itemcallsmssfe_delete;
    private TextView tv_itemcallsmssafe_blacknum;
    private TextView tv_itemcallsmssafe_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsmssafe);
        blackNumDao = new BlackNumDao(getApplicationContext());
        lv_callsmssafe_blacknum = (ListView) findViewById(R.id.lv_callsmssafe_blacknum);
        loading = (ProgressBar) findViewById(R.id.loading);
        fillData();
    }

    /**
     *加载数据的操作
     */
    private void fillData() {
        //异步加载的方法，查询数据库是耗时操作
        new MyAsycnTask(){
            //在子线程之前
            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);//显示进度条
            }
            //在子线程中
            @Override
            public void doinBack() {
                queryAllBlackNum = blackNumDao.queryAllBlackNum();
            }
            //在子线程之后
            @Override
            public void postTask() {
                lv_callsmssafe_blacknum.setAdapter(new MyAdapter());
                //数据显示完成，隐藏进度条
                loading.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    private class MyAdapter extends BaseAdapter{
        //设置条目个数
        @Override
        public int getCount() {
            return queryAllBlackNum.size();
        }
        //获取条目对应的数据
        @Override
        public Object getItem(int position) {
            return queryAllBlackNum.get(position);
        }
        //获取条目ID
        @Override
        public long getItemId(int position) {
            return position;
        }
        //设置条目显示的样式
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //根据条目的位置获取对应的bean对象
            BlackNumInfo blackNumInfo = queryAllBlackNum.get(position);
            View view = View.inflate(getApplicationContext(),R.layout.item_callsmssafe,null);
            tv_itemcallsmssafe_blacknum = (TextView) view.findViewById(R.id.tv_itemcallsmssafe_blacknum);
            tv_itemcallsmssafe_mode = (TextView) view.findViewById(R.id.tv_itemcallsmssafe_mode);
            iv_itemcallsmssfe_delete = (ImageView) view.findViewById(R.id.iv_itemcallsmssfe_delete);

            //设置显示数据
            tv_itemcallsmssafe_blacknum.setText(blackNumInfo.getBlacknum());
            int mode = blackNumInfo.getMode();
            switch (mode){
                case BlackNumDao.CALL:
                    tv_itemcallsmssafe_mode.setText("电话拦截");
                   break;
                case BlackNumDao.SMS:
                    tv_itemcallsmssafe_mode.setText("短信拦截");
                    break;
                case BlackNumDao.ALL:
                    tv_itemcallsmssafe_mode.setText("全部拦截");
                    break;
                default:
                    break;
            }
            return view;
        }
    }
}
