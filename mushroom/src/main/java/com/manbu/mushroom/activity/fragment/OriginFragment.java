package com.manbu.mushroom.activity.fragment;

import com.manbu.mushroom.R;
import com.manbu.mushroom.common.BaseFragment;
import com.manbu.mushroom.bean.OriginInfo;
import com.manbu.mushroom.data.OriginInfoHandel;
import com.manbu.zxinglib.activity.CaptureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OriginFragment extends BaseFragment implements OnClickListener {
	private TextView mTextName;
	private TextView mTextAddress;
	private TextView mTextLife;
	private TextView mTextSpec;
	private TextView mTextMoreinfo;
	private TextView mOther;
	private Button mBtnScan;
	private LinearLayout mResutInfo;
	private LinearLayout mOtherInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.origin_scan: // 打开扫描
			mResutInfo.setVisibility(View.GONE);
			mOtherInfo.setVisibility(View.GONE);
			Intent sacn = new Intent(getActivity().getBaseContext(), CaptureActivity.class);
			sacn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(sacn, 1);
			break;

		default:
			break;
		}
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		mTextName = (TextView) view.findViewById(R.id.origin_prodres_name);
		mTextAddress = (TextView) view.findViewById(R.id.origin_prodres_address);
		mTextLife = (TextView) view.findViewById(R.id.origin_prodres_life);
		mTextSpec = (TextView) view.findViewById(R.id.origin_prodres_spec);
		mTextMoreinfo = (TextView) view.findViewById(R.id.origin_prodres_moreinfo);
		mOther = (TextView) view.findViewById(R.id.origin_other_info);

		mBtnScan = (Button) view.findViewById(R.id.origin_scan);
		mResutInfo = (LinearLayout) view.findViewById(R.id.origin_product_info);
		mOtherInfo = (LinearLayout) view.findViewById(R.id.origin_other);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_origin;
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initListeners() {
		mBtnScan.setOnClickListener(this);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == -1) {
			String str = data.getExtras().getString("result");
			try {
//				OriginInfoHandel resultHandel = new OriginInfoHandel();
				OriginInfo result = OriginInfoHandel.getResult(str);
				mTextName.setText(result.getName());
				mTextAddress.setText(result.getAddress());
				mTextLife.setText(result.getLife());
				mTextSpec.setText(result.getSpec());
				mTextMoreinfo.setText(result.getMoreInfo());
				mResutInfo.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				mOther.setText(str);
				mOtherInfo.setVisibility(View.VISIBLE);
			}

		}
	}

}
