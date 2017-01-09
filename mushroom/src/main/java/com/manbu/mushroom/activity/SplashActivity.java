package com.manbu.mushroom.activity;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.manbu.mushroom.R;
import com.manbu.mushroom.bean.MyUser;
import com.manbu.mushroom.common.BaseActivity;
import com.manbu.mushroom.ui.FlyTxtView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * 
* @ClassName: SplashActivity
* @Description: TODO(欢迎界面，文字飞入效果)
* @author ManBu 
* @date 2016年8月18日 下午5:59:15
*
 */
@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity {

//	private FlyTxtView flytxtview;
	private static final int GOTO_LOGIN = 100;
	private HTextView hTextView;


	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.splash_activity);

	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
//		flytxtview = (FlyTxtView) findViewById(R.id.fly_txt);

		hTextView = (HTextView) findViewById(R.id.splash_action_text);
		hTextView.setAnimateType(HTextViewType.TYPER);
		hTextView.animateText("我们不懂蘑菇，但是你懂得");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
//		showFlyTxet();
		
		Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case GOTO_LOGIN:
					goTOLogin();
					break;
				}
			}
		};
		mHandler.sendEmptyMessageDelayed(GOTO_LOGIN, 2000);
	}


	/**
	 * 
	* @Title: goTOLogin
	* @Description: TODO 跳转到登录界面
	* @return void    返回类型
	* @throws
	 */
	public void goTOLogin() {
		MyUser myUser = getUser();
		if (myUser != null) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			intent.putExtra("selectPage",2);
			startActivity(intent);
			this.finish();
		} else {
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			intent.putExtra("title", "登录");
			startActivity(intent);
			this.finish();
		}

	}

	/**
	 *
	 * @Title: showFlyTxet
	 * @Description: TODO(飞入文字的相关设置，大小、颜色、内容)
	 * @return void    返回类型
	 * @throws
	 */
//	public void showFlyTxet() {
//		flytxtview.setTextSize(15);
//		flytxtview.setTextColor(Color.WHITE);
//		flytxtview.setTexts("我们不懂蘑菇，但是你懂得");
//	}
	@Override
	public void initListeners() {

	}

	@Override
	public void onClick(View v) {

	}
}
