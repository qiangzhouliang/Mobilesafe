package com.qzl.shoujiweishi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void queryAddress(View view){
        //跳转到查询号码归属地
        Intent intent = new Intent(AToolsActivity.this,AddressActivity.class);
        startActivity(intent);
    }
}
