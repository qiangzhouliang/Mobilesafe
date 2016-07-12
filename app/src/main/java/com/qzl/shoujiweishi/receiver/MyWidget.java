package com.qzl.shoujiweishi.receiver;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.qzl.shoujiweishi.service.WidgetService;

/**
 * 桌面小控件
 */
public class MyWidget extends AppWidgetProvider {

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		System.out.println("onAppWidgetOptionsChanged");
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		System.out.println("onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	//删除最后一个的时候会调用的方法
	@Override
	public void onDisabled(Context context) {
		System.out.println("onDisabled");
		super.onDisabled(context);
		Intent intent = new Intent(context,WidgetService.class);
		context.stopService(intent);
	}

	/**
	 * 第一次创建的时候调运
	 * @param context
     */
	@Override
	public void onEnabled(Context context) {
		System.out.println("onEnabled");
		super.onEnabled(context);
		//开启更新服务
		Intent intent = new Intent(context, WidgetService.class);
		context.startService(intent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("onReceive");
		super.onReceive(context, intent);
	}

	@SuppressLint("NewApi") @Override
	public void onRestored(Context context, int[] oldWidgetIds,
			int[] newWidgetIds) {
		System.out.println("onRestored");
		super.onRestored(context, oldWidgetIds, newWidgetIds);
	}

	//当更更新时间到了，会调运次方法
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		System.out.println("onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		//开启更新服务
		Intent intent = new Intent(context, WidgetService.class);
		context.startService(intent);
	}
}
