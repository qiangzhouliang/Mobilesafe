package com.qzl.shoujiweishi.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qzl.shoujiweishi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CacheFragment extends Fragment {

    public CacheFragment() {
        // Required empty public constructor
    }

    /**
     * 初始化的操作
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置fragment的布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //参数一：布局文件
        //参数二：容器
        //参数三：自动挂载，一律false
        View view = inflater.inflate(R.layout.fragment_cache, container, false);
        return view;
    }

    /**
     * 设置填充显示数据
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
