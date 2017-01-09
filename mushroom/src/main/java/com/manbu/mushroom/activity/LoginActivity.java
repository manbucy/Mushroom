package com.manbu.mushroom.activity;

import com.manbu.mushroom.R;
import com.manbu.mushroom.common.BaseActivity;
import com.manbu.mushroom.bean.MyUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * 
 * @ClassName: LoginActivity
 * @Description: TODO(登录界面，及其相关操作)
 * @author ManBu
 * @date 2016年8月18日 下午5:41:58
 *
 */
public class LoginActivity extends BaseActivity {
	private TextView txt_register, txt_findpass, mTitle;
	private EditText etxt_username, etxt_password;
	private Button btn_login;
	private Long exitTime = 0L;


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN
				&& mTitle.getText().equals("登录")) {
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_txt_find:
			Intent fintent = new Intent(LoginActivity.this, FindPassActivity.class);
			startActivity(fintent);
			// showToastS("找回密码");
			break;
		case R.id.login_txt_register:
			Intent rintent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(rintent);
			// showToastS("注册新用户");
			break;
		case R.id.login_btn_login:
			Login();
			break;

		default:
			break;
		}
	}

	@Override
	public void setContentView() {
		setContentView(R.layout.login_activity);
	}

	@Override
	public void initViews() {
		txt_findpass = (TextView) findViewById(R.id.login_txt_find);
		txt_register = (TextView) findViewById(R.id.login_txt_register);
		etxt_username = (EditText) findViewById(R.id.login_etxt_username);
		etxt_password = (EditText) findViewById(R.id.login_etxt_password);
		btn_login = (Button) findViewById(R.id.login_btn_login);
		mTitle = (TextView) findViewById(R.id.tv_title);
		mTitle.setText(getIntent().getStringExtra("title"));
	}

	@Override
	public void initData() {

	}

	@Override
	public void initListeners() {
		txt_register.setOnClickListener(this);
		txt_findpass.setOnClickListener(this);
		btn_login.setOnClickListener(this);
	}


	private void Login() {
		String account = etxt_username.getText().toString();
		String password = etxt_password.getText().toString();
		if (TextUtils.isEmpty(account)) {
			showToastS("账号不能为空");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			showToastS("密码不能为空");
			return;
		}
		final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
		progress.setMessage("正在登录中...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9提供的新的登录方式，可传用户名/邮箱/手机号码
		BmobUser.loginByAccount(this, account, password, new LogInListener<MyUser>() {

			@Override
			public void done(MyUser myUser, BmobException ex) {
				progress.dismiss();
				if (ex == null) {
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra("selectPage", 4);
					startActivity(intent);
					finish();
				} else {
					showToastS("密码错误");
				}
			}
		});
	}

}
