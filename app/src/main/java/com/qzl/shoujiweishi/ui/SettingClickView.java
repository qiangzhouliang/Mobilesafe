package com.qzl.shoujiweishi.ui;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qzl.shoujiweishi.R;


public class SettingClickView extends RelativeLayout {
	private TextView tv_setting_title;
	private TextView tv_setting_des;
	//在代码中使用的时候调用
	public SettingClickView(Context context) {
		super(context);
		//可以在初始化自定义控件的时候就添加控件
		init();
	}
	//在布局文件中使用的时候调用,比两个参数多了个样式
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	//在布局文件中使用的时候调用
	//AttributeSet:保存有控件的所有属性
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		//通过AttributeSet获取控件属性,不能获取某一个值
//		int count = attrs.getAttributeCount();//获取控件属性的个数
//		System.out.println("属性的个数："+count);
//		for (int i = 0; i < count; i++) {
//			//attrs.getAttributeValue(i);//获取某个属性的值
//			System.out.println("属性的值"+attrs.getAttributeValue(i));
//		}

		//通过命名空间和属性名称获取属性的值
		//namespase : 命名空间
		// name ： 属性名称
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "text");
		//给自定义组合控件的控件设置相应的值
		//初始化控件的值
		tv_setting_title.setText(title);
	}
	/**
	 * 添加控件
	 */
	private void init(){
		//添加布局文件
//		TextView textView = new TextView(getContext());
//		textView.setText("我是自定义组合控件的textview");
		//第一种方式
		//将布局文件转化成view对象
//		View view = View.inflate(getContext(), R.layout.settingview, null);//爹有了,去找孩子,亲生
//		//添加操作
//		this.addView(view);//在自定义组合控件中添加一个textview
		//第二种方式
		//获取view对象,同时给veiw对象设置父控件,相当于先创建一个view对象,在把控件放到自定义控件中
		View view = View.inflate(getContext(), R.layout.settingclickview, this);//孩子有了,去找爹,喜当爹
		//初始化标题,描述信息,checkbox控件
		//view.findViewById : 表示从settingview布局文件中获取控件
		tv_setting_title = (TextView) view.findViewById(R.id.tv_setting_title);
		tv_setting_des = (TextView) view.findViewById(R.id.tv_setting_des);
	}
	//需要添加一些方法,使程序员能方便的去改变自定义控件中的控件的值
	/**
	 * 设置标题的方法
	 */
	public void setTitle(String title){
		tv_setting_title.setText(title);
	}
	/**
	 * 设置描述信息的方法
	 */
	public void setDes(String des){
		tv_setting_des.setText(des);
	}
}
