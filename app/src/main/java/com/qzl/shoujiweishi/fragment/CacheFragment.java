package com.qzl.shoujiweishi.fragment;


import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qzl.shoujiweishi.R;

import java.lang.reflect.Method;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CacheFragment extends Fragment {

    private TextView tv_cachefragment_text;
    private ProgressBar pb_cachefragment_progressbar;

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
        tv_cachefragment_text = (TextView) view.findViewById(R.id.tv_cachefragment_text);
        pb_cachefragment_progressbar = (ProgressBar) view.findViewById(R.id.pb_cachefragment_progressbar);
        return view;
    }

    /**
     * 设置填充显示数据
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        scanner();
    }

    /**
     * 扫描的操作
     */
    private void scanner() {
        // 1 获取包的管理者
        //getActivity:获取fragment所在的activity
        final PackageManager pm = getActivity().getPackageManager();
        tv_cachefragment_text.setText("正在初始化128核扫描引擎");
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(100);
                List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
                //设置进度条最大进度
                pb_cachefragment_progressbar.setMax(installedPackages.size());
                int count = 0;
                for (PackageInfo packageInfo : installedPackages) {
                    SystemClock.sleep(100);
                    //设置进度条最大进度和当前进度
                    count++;
                    pb_cachefragment_progressbar.setProgress(count);

                    //获取缓存大小
                    //反射获取缓存
                    try {
                        Class<?> loadClass = getActivity().getClass().getClassLoader().loadClass("android.content.pm.PackageManager");
                        Method method = loadClass.getDeclaredMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
                        //receiver : 类的实例,隐藏参数,方法不是静态的必须指定
                        method.invoke(pm, packageInfo.packageName,mStatsObserver);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //设置扫描显示的应用名称
                    final String name = packageInfo.applicationInfo.loadLabel(pm).toString();
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_cachefragment_text.setText("正在扫描：" + name);
                            }
                        });
                    }
                }
                if (getActivity() != null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_cachefragment_text.setVisibility(View.GONE);
                            pb_cachefragment_progressbar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 获取缓存大小的操作
     */
    IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
        public void onGetStatsCompleted(PackageStats stats, boolean succeeded) {
            long cachesize = stats.cacheSize;//缓存大小
            long codesize = stats.codeSize;//应用程序的大小
            long datasize = stats.dataSize;//数据大小

            String cache = Formatter.formatFileSize(getActivity(), cachesize);
            String code = Formatter.formatFileSize(getActivity(), codesize);
            String data = Formatter.formatFileSize(getActivity(), datasize);

            System.out.println(stats.packageName+"cachesize:"+cache +" codesize:"+code+" datasize:"+data);
        }
    };
}
