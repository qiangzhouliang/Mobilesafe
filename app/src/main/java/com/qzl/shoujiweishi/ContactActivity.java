package com.qzl.shoujiweishi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qzl.shoujiweishi.engine.ContactEngine;
import com.qzl.shoujiweishi.utils.MyAsycnTask;

import java.util.HashMap;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    @ViewInject(R.id.lv_contact_contacts)
    private ListView lv_contact_contacts;
    private List<HashMap<String, String>> list;
    //注解初始化控件，类似Spring，注解的形式生成Javabean，内部：通过反射的方式执行了findViewById
    @ViewInject(R.id.loading)
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ViewUtils.inject(this);//这一行可以把所有的使用注解的控件初始化出来
        //异步加载框架
        new MyAsycnTask() {
            @Override
            public void preTask() {
                //加载数据之前显示进度条，在子线程之前执行
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void doinBack() {
                //获取联系人，在子线程之中操作
                list = ContactEngine.getAllContactInfo(getApplicationContext());
            }

            @Override
            public void postTask() {
                //在子线程之后操作
                lv_contact_contacts.setAdapter(new Myadapter());
                loading.setVisibility(View.INVISIBLE);//数据显示完成，隐藏进度条
            }
        }.execute();

        /*//参数：为了提高扩展性
        //参数1 子线程中执行所需的方法，参数2 执行进度的一个操作 参数3 子线程执行的结果
        //异步加载框架：面试必问，手写异步加载框架，百度面试题：当他执行到多少个操作时候就和new一个线程没区别了（5）
        new AsyncTask<String, Integer, String>() {
            //在子线程之前执行的方法
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //在子线程之中执行的方法
            @Override
            protected String doInBackground(String... params) {
                return null;
            }

            //在子线程之后执行的操作
            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }
        };*/
        //lv_contact_contacts = (ListView) findViewById(R.id.lv_contact_contacts);

        lv_contact_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将选择联系人的号码传递给设置安全号码界面
                Intent intent = new Intent();
                intent.putExtra("num", list.get(position).get("phone"));
                //将数据传递给设置安全号码界面
                //设置结果的方法，会将结果传递给调运当前activity的activity
                setResult(RESULT_OK, intent);
                //移除界面
                finish();
            }
        });
    }

    private class Myadapter extends BaseAdapter {

        //获取条目个数
        @Override
        public int getCount() {
            return list.size();
        }

        //获取条目对应的数据
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        //获取条目id
        @Override
        public long getItemId(int position) {
            return position;
        }

        //设置条目样式
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_contacts, null);
            //初始化控件
            TextView tv_itemcontact_name = (TextView) view.findViewById(R.id.tv_itemcontact_name);
            TextView tv_itemcontact_phone = (TextView) view.findViewById(R.id.tv_itemcontact_phone);
            //设置控件的值
            tv_itemcontact_name.setText(list.get(position).get("name"));//根据条目的位置从list中获取相应的数据
            tv_itemcontact_phone.setText(list.get(position).get("phone"));
            return view;
        }
    }
}
