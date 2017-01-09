package com.manbu.mushroom.activity.fragment;

import com.manbu.mushroom.R;
import com.manbu.mushroom.activity.SellAddActivity;
import com.manbu.mushroom.common.BaseFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SellFragment extends BaseFragment implements OnClickListener {
	private Button mSellButton;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sell_shop_btn_send: // 添加商品
			Intent addIntent = new Intent(getActivity(),SellAddActivity.class);
			startActivity(addIntent);
			break;

		default:
			break;
		}
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		mSellButton = (Button) view.findViewById(R.id.sell_shop_btn_send);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_sell;
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void initListeners() {
		mSellButton.setOnClickListener(this);
	}

}
