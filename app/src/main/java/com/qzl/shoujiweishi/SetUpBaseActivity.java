package com.qzl.shoujiweishi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public abstract class SetUpBaseActivity extends AppCompatActivity {
    //将每个界面中的上一步下一步按钮的操作，抽取到父类中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_base);
    }
    //按钮的操作
    public void pre(View view){
        pre_activity();
    }
    public void next(View view){
        next_activity();
    }
    //因为父类不知道子类上一步，下一步具体的执行操作方法，所以要创建一个抽象方法，让子类实现这个抽象类，根据自己的特性去实现相应的操作
    //下一步操作
    public abstract void next_activity();
    //上一步操作
    public abstract void pre_activity();
}
