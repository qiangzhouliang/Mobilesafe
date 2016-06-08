package com.qzl.shoujiweishi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qzl.shoujiweishi.db.AddressDao;

public class AddressActivity extends AppCompatActivity {
    @ViewInject(R.id.et_address_queryphone)
    private EditText et_address_queryphone;
    @ViewInject(R.id.tv_address_queryaddress)
    private TextView tv_address_queryaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ViewUtils.inject(this);
    }

    /**
     * 查询号码归属地
     * @param view
     */
    public void query(View view){
        //1 获取输入的号码
        String phone = et_address_queryphone.getText().toString().trim();
        //2 判断号码是否为空
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(getApplicationContext(), "请输入要查询的号码", Toast.LENGTH_SHORT).show();
            return;
        }
        //3 根据号码查询号码归属地
        String queryAddress = AddressDao.queryAddress(phone, getApplicationContext());
        //4 判断查询的号码归属地是否为空
        if(!TextUtils.isEmpty(queryAddress)){
            tv_address_queryaddress.setText(queryAddress);
        }
    }
}
