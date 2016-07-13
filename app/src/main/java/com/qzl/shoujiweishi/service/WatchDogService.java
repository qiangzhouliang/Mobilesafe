package com.qzl.shoujiweishi.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

import com.qzl.shoujiweishi.WatchDogActivity;
import com.qzl.shoujiweishi.db.dao.WatchDogDao;

public class WatchDogService extends Service {

	private WatchDogDao watchDogDao;
	private boolean isLock;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		watchDogDao = new WatchDogDao(getApplicationContext());
		//时时刻刻监听用户打开的程序
		//activity都是存放在任务栈中的,一个应用只有一个任务栈
		final ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		isLock = true;
		new Thread(){
			public void run() {
				while(isLock){
					//监听操作
					//监听用户打开了哪些任务栈,打开哪些应用
					//获取正在运行的任务栈,如果任务栈运行,就表示应用打开过
					//maxNum : 获取正在运行的任务栈的个数
					//现在打开的应用的任务栈,永远在第一个,而之前(点击home最小化,没有退出)的应用的任务栈会依次往后推
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					System.out.println("----------------------");
					for (RunningTaskInfo runningTaskInfo : runningTasks) {
						//获取任务栈,栈底的activity
						ComponentName baseactivity = runningTaskInfo.baseActivity;
						/*//获取任务栈栈顶的activity
						runningTaskInfo.topActivity;*/
						String packageName = baseactivity.getPackageName();
						System.out.println(packageName);
						//通过查询数据库，如果数据库中有，弹出解锁界面，没有，不做操作
						if (watchDogDao.queryLockApp(packageName)) {
							System.out.println("存在加锁程序");
							//弹出解锁界面
							Intent intent = new Intent(WatchDogService.this, WatchDogActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					}
					System.out.println("----------------------");
					SystemClock.sleep(300);
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//当服务关闭的时候我们要去禁止监听
		isLock = false;
	}
}
