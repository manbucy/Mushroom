package com.manbu.mushroom.common;


import com.manbu.mushroom.bean.MyUser;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import android.view.View.OnClickListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public abstract class BaseActivity extends Activity implements OnClickListener{
	
	protected int mScreenWidth;
	protected int mScreenHeight;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//初始化Bmob
		Bmob.initialize(this, Constants.BMOB_APPID);

		setContentView();
		initViews();
		initListeners();
		initData();
	}
	/**
	 * 设置布局文件
	 */
	public abstract void setContentView();

	/**
	 * 初始化布局文件中的控件
	 */
	public abstract void initViews();

	/** 进行数据初始化
	  * initData
	  */
	public abstract void initData();
	
	/**
	 * 初始化控件的监听
	 */
	public abstract void initListeners();
	
	
	
	public Toast mToast;
	
	/**
	 * 
	* @Title: ShowToastS
	* @Description: TODO 展示Short Toast
	* @param @param text  需要展示的内容 
	* @return void  
	* @throws
	 */
	public void showToastS(String text) {
		if (!TextUtils.isEmpty(text)) {
			if (mToast == null) {
				mToast = Toast.makeText(getApplicationContext(), text,Toast.LENGTH_SHORT);
			} else {
				mToast.setText(text);
			}
			mToast.show();
		}
	}
	
	/**
	 * 
	* @Title: ShowToastL
	* @Description: TODO 展示Long Toast
	* @param @param text 需要展示的内容
	* @return void  
	* @throws
	 */
	public void showToastL(String text) {
		if (!TextUtils.isEmpty(text)) {
			if (mToast == null) {
				mToast = Toast.makeText(getApplicationContext(), text,Toast.LENGTH_LONG);
			} else {
				mToast.setText(text);
			}
			mToast.show();
		}
	}
	
	protected MyUser getUser() {

		return BmobUser.getCurrentUser(getApplicationContext(), MyUser.class);
	}


}
