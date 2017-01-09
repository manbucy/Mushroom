package com.manbu.mushroom.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manbu.mushroom.R;
import com.manbu.mushroom.activity.LoginActivity;
import com.manbu.mushroom.common.BaseFragment;
import com.manbu.mushroom.bean.MyUser;
import com.meg7.widget.CustomShapeImageView;

import cn.bmob.v3.BmobUser;

public class ManageFragment extends BaseFragment implements OnClickListener {
    private LinearLayout mLogin;
    private TextView mUserName;
    private Button mBtnExit;
    private CustomShapeImageView mImgHead;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.managment_login:
                Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                intentLogin.putExtra("title", "切换账号");
                startActivity(intentLogin);
                break;
            case R.id.management_btn_exit:
                Intent intentExit = new Intent(getActivity(), LoginActivity.class);
                intentExit.putExtra("title", "登录");
                startActivity(intentExit);
                BmobUser.logOut(getContext());
                getActivity().finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mLogin = (LinearLayout) view.findViewById(R.id.managment_login);
        mUserName = (TextView) view.findViewById(R.id.managment_username);
        mBtnExit = (Button) view.findViewById(R.id.management_btn_exit);
        mImgHead = (CustomShapeImageView) view.findViewById(R.id.management_img_head);
        MyUser myUser = getUser();
        //查询本地是否有缓存的用户 如果没有则跳转到登录界面
        if (myUser != null) {
            if (myUser.getNickName() == null || myUser.getNickName().isEmpty()) {
                mUserName.setText(myUser.getUsername());
            } else {
                mUserName.setText(myUser.getNickName());
            }
        }
        else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("title", "登录");
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_management;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        mLogin.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
    }

}
