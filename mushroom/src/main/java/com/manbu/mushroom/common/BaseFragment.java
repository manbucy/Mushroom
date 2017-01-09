package com.manbu.mushroom.common;

import com.manbu.mushroom.bean.MyUser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;

public abstract class BaseFragment extends Fragment {

	protected abstract void initView(View view, Bundle savedInstanceState);

	// 获取fragment布局文件ID
	protected abstract int getLayoutId();

	/**
	 * 进行数据初始化 initData
	 */
	protected abstract void initData();

	/**
	 * 初始化控件的监听
	 */
	protected abstract void initListeners();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutId(), container, false);
		initView(view, savedInstanceState);

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initListeners();
	}

	protected MyUser getUser() {

		return BmobUser.getCurrentUser(getActivity().getBaseContext(), MyUser.class);
	}
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
				mToast = Toast.makeText(getActivity().getApplicationContext(), text,Toast.LENGTH_SHORT);
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
				mToast = Toast.makeText(getActivity().getApplicationContext(), text,Toast.LENGTH_LONG);
			} else {
				mToast.setText(text);
			}
			mToast.show();
		}
	}


}
