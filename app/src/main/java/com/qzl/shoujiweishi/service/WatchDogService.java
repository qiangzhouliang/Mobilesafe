package com.qzl.shoujiweishi.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;

import com.qzl.shoujiweishi.WatchDogActivity;
import com.qzl.shoujiweishi.db.dao.WatchDogDao;

public class WatchDogService extends Service {

	private WatchDogDao watchDogDao;
	private boolean isLock;
	private UnLockCurrentReceiver unLockCurrentReceiver;
	private String unlockcurrentPackagName;
	private ScreenOffReceiver screenOffReceiver;
	private List<String> list;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		watchDogDao = new WatchDogDao(getApplicationContext());
		//注册解锁的广播接收者
		// 1 创建一个广播接收者
		unLockCurrentReceiver = new UnLockCurrentReceiver();
		// 2 设置接收广播事件
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.qzl.shoujiweishi.unlock");
		//3 注册广播接收者
		registerReceiver(unLockCurrentReceiver,intentFilter);

		//注册锁屏的广播接受者和
		screenOffReceiver = new ScreenOffReceiver();
		IntentFilter screenOffIntentFilter = new IntentFilter();
		screenOffIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenOffReceiver,screenOffIntentFilter);

		//时时刻刻监听用户打开的程序
		//activity都是存放在任务栈中的,一个应用只有一个任务栈
		final ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		isLock = true;
		new Thread(){
			public void run() {
				//先将数据库中的数据查询存放到内存中，然后再把数据从内存中取出来进行操作
				//当数据库变化的时候，重新更新内存中的数据，当数据库变化的时候，通知内容观察者数据库变化了，然后在内容观察者中去更新最新数据
				Uri uri = Uri.parse("content://com.qzl.shoujiweishi.lock.changed");
				//notifyForDescendents:匹配规则：true：精确匹配，false ：模糊匹配
				getContentResolver().registerContentObserver(uri, true, new ContentObserver(null) {
					@Override
					public void onChange(boolean selfChange) {
						super.onChange(selfChange);
						//更新数据
						list = watchDogDao.queryAllLockApp();
					}
				});
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
						//判断list集合中是否包含包名
						boolean b = list.contains(packageName);
						//通过查询数据库，如果数据库中有，弹出解锁界面，没有，不做操作
						if (b) {
							if (!packageName.equals(unlockcurrentPackagName)){
								//弹出解锁界面
								Intent intent = new Intent(WatchDogService.this, WatchDogActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra("packageName",packageName);
								startActivity(intent);
							}
						}
					}
					System.out.println("----------------------");
					SystemClock.sleep(300);
				}
			};
		}.start();
	}

	/**
	 * 解锁广播接收者
	 */
	private class UnLockCurrentReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//解锁操作
			unlockcurrentPackagName = intent.getStringExtra("packageName");
		}
	}

	private class ScreenOffReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//加锁的操作
			unlockcurrentPackagName = null;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		//当服务关闭的时候我们要去禁止监听
		isLock = false;
		//注销解锁的广播接受者和
		if (unLockCurrentReceiver != null){
			unregisterReceiver(unLockCurrentReceiver);
			unLockCurrentReceiver = null;
		}
		//注销锁屏的广播接收者
		if (screenOffReceiver != null){
			unregisterReceiver(screenOffReceiver);
			screenOffReceiver = null;
		}
	}
}
