package com.qzl.shoujiweishi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callsmssafe);
        blackNumDao = new BlackNumDao(getApplicationContext());
        lv_callsmssafe_blacknum = (ListView) findViewById(R.id.lv_callsmssafe_blacknum);
        loading = (ProgressBar) findViewById(R.id.loading);
        //加载数据库的操作
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
                myAdapter = new MyAdapter();
                queryAllBlackNum = blackNumDao.queryAllBlackNum();
            }
            //在子线程之后
            @Override
            public void postTask() {
                lv_callsmssafe_blacknum.setAdapter(myAdapter);
                //数据显示完成，隐藏进度条
                loading.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    /**
     * 添加黑名单点击事件
     * @param view
     */
    public void addBlackNum(View view) {
        //弹出对话框提醒用户添加黑名单
        
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            //根据条目的位置获取对应的bean对象
            final BlackNumInfo blackNumInfo = queryAllBlackNum.get(position);
            View view;
            ViewHolder viewHolder;
            if(convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.item_callsmssafe, null);
                //创建控件容器
                viewHolder = new ViewHolder();
                //把控件存放到容器中
                viewHolder.tv_itemcallsmssafe_blacknum = (TextView) view.findViewById(R.id.tv_itemcallsmssafe_blacknum);
                viewHolder.tv_itemcallsmssafe_mode = (TextView) view.findViewById(R.id.tv_itemcallsmssafe_mode);
                viewHolder.iv_itemcallsmssfe_delete = (ImageView) view.findViewById(R.id.iv_itemcallsmssfe_delete);
                //将容器和view绑定在一起
                view.setTag(viewHolder);
            }else {
                view = convertView;
                //从view中得到控件的容器
                viewHolder = (ViewHolder) view.getTag();
            }
            /*if (convertView == null){
                convertView = View.inflate(getApplicationContext(), R.layout.item_callsmssafe, null);
            }*/

            //设置显示数据
            viewHolder.tv_itemcallsmssafe_blacknum.setText(blackNumInfo.getBlacknum());
            int mode = blackNumInfo.getMode();
            switch (mode){
                case BlackNumDao.CALL:
                    viewHolder.tv_itemcallsmssafe_mode.setText("电话拦截");
                   break;
                case BlackNumDao.SMS:
                    viewHolder.tv_itemcallsmssafe_mode.setText("短信拦截");
                    break;
                case BlackNumDao.ALL:
                    viewHolder.tv_itemcallsmssafe_mode.setText("全部拦截");
                    break;
                default:
                    break;
            }
            //删除黑名单
            viewHolder.iv_itemcallsmssfe_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除黑名单操作
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallSmsSafeActivity.this);
                    builder.setMessage("您确认要删除黑名单号码："+blackNumInfo.getBlacknum()+"?");
                    //设置确定和取消
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除黑名单操作
                            //1 删除数据库中的黑名单号码
                            blackNumDao.deleteBlackNum(blackNumInfo.getBlacknum());
                            //2 删除界面中已经显示的黑名单号码
                            // 2.1 将存放有所有数据的queryAllBlackNum集合中删除相应的数据
                            queryAllBlackNum.remove(position);//删除条目对应位置的相应的数据
                            // 2.2 更新界面
                            myAdapter.notifyDataSetChanged();//更新界面
                            //3 隐藏对话框
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消",null);//只是隐藏操作
                    //显示对话框
                    builder.show();
                }
            });
            return view;
        }
    }

    /**
     * 存放控件的容器
     * cunfang
     */
    class ViewHolder{
        TextView tv_itemcallsmssafe_blacknum,tv_itemcallsmssafe_mode;
        ImageView iv_itemcallsmssfe_delete;
    }
}
