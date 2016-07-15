package com.qzl.shoujiweishi;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.qzl.shoujiweishi.fragment.CacheFragment;
import com.qzl.shoujiweishi.fragment.SDFragment;

/**
 * 如果要对fragment进行管理操作，activity必须继承fragmentActivity进行操作
 */
public class ClearCacheActivity extends FragmentActivity {

    private CacheFragment cacheFragment;
    private SDFragment sdFragment;
    private FragmentManager supportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cache);
        //挂载fragment
        // 1 获取fragment
        cacheFragment = new CacheFragment();
        sdFragment = new SDFragment();
        //2 加载fragment
        // 2.1 获取fragment的管理者
        supportFragmentManager = getSupportFragmentManager();
        //2.2 开启事务
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        //2.3 添加fragment操作
        //数据不会重新刷新
        //参数一：fragment要替换的控件id
        //参数二：要替换的fragment
        beginTransaction.add(R.id.fram_clearcache_fragment, cacheFragment);
        //界面展示以最后一个添加的为准
        beginTransaction.add(R.id.fram_clearcache_fragment, sdFragment);
        //隐藏fragment
        beginTransaction.hide(sdFragment);
        //也可以实现替换操作，数据会重新刷新
        //beginTransaction.replace();
        //2.3 提交事务
        beginTransaction.commit();
    }

    /**
     * 缓存清理点击事件
     * @param view
     */
    public void cache(View view) {
        //隐藏sdfragment，显示cachefragment
        //1 获取事务
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        //2 隐藏显示操作
        beginTransaction.show(cacheFragment);//显示fragment
        beginTransaction.hide(sdFragment);//隐藏fragment
        // 3 提交
        beginTransaction.commit();
    }

    /**
     * sd卡清理点击事件
     * @param view
     */
    public void sd(View view) {
        //隐藏cachefragment，显示sdfragment
        //1 获取事务
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        //2 隐藏显示操作
        beginTransaction.show(sdFragment);//显示fragment
        beginTransaction.hide(cacheFragment);//隐藏fragment
        // 3 提交
        beginTransaction.commit();
    }
}
