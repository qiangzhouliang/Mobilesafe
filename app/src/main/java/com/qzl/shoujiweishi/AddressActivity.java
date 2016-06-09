package com.qzl.shoujiweishi;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
        //监听输入框文本变化的操作
        et_address_queryphone.addTextChangedListener(new TextWatcher() {
            //当文本变化之前调运的方法
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //文本变化完成
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //1 获取出输入框输入的内容
                String phone = s.toString();
                //2 根据号码查询号码归属地
                String qureyAddress = AddressDao.queryAddress(phone, getApplicationContext());
                //3 判断查询的号码归属地是否为空
                if(!TextUtils.isEmpty(qureyAddress)){
                    //将查询的号码归属地设置给textView显示
                    tv_address_queryaddress.setText(qureyAddress);
                }
            }
            //文本变化之后调运
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
            //实现抖动的效果
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            /*shake.setInterpolator(new Interpolator() {
                //动画插补器
                @Override
                public float getInterpolation(float x) {
                    return 0;//根据x的值获取y的值 y = x*x 等
                }
            });*/
            et_address_queryphone.startAnimation(shake);//开启动画
            //震动
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(100);//0.1秒
        }else {
            //3 根据号码查询号码归属地
            String queryAddress = AddressDao.queryAddress(phone, getApplicationContext());
            //4 判断查询的号码归属地是否为空
            if(!TextUtils.isEmpty(queryAddress)){
                tv_address_queryaddress.setText(queryAddress);
            }
        }
    }
}
