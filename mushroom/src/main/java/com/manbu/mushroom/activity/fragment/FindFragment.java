package com.manbu.mushroom.activity.fragment;

import com.manbu.mushroom.R;
import com.manbu.mushroom.common.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class FindFragment extends BaseFragment implements OnClickListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_find;
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initListeners() {
		
	}

}
