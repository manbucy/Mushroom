package com.manbu.mushroom.activity;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.manbu.mushroom.R;
import com.manbu.mushroom.common.BaseActivity;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
/**
 * 找回密码
 *@Description:
 *@author: ManBu
 *@Date:2016年6月12日 下午9:18:29
 */
public class FindPassActivity extends BaseActivity {
	
	private EditText etxt_phone,etxt_code,etxt_password;
	private TextView txt_send,txt_tile;
	private TextView img_back;
	private Button btn_reset;
	MyCountTimer timer;


	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.findpassword_activity);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		etxt_code=(EditText) findViewById(R.id.findpass_etx_code);
		etxt_password=(EditText) findViewById(R.id.findpass_etx_pwd);
		etxt_phone= (EditText) findViewById(R.id.findpass_etx_phone);
		txt_tile= (TextView) findViewById(R.id.tv_title);
		img_back= (TextView) findViewById(R.id.tv_left);
		txt_send= (TextView) findViewById(R.id.findpass_txt_send);
		btn_reset= (Button) findViewById(R.id.findpass_btn_reset);
		txt_tile.setText("找回密码");
		img_back.setVisibility(View.VISIBLE);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		img_back.setOnClickListener(this);
		txt_send.setOnClickListener(this);
		btn_reset.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_left:
			finish();
			break;
		case R.id.findpass_btn_reset:
			resetPwd();
			break;
		case R.id.findpass_txt_send:
			requestSMSCode();
			break;

		default:
			break;
		}
	}

	/**
	 * 验证码发送控制器
	 */
	class MyCountTimer extends CountDownTimer{  
		  
        public MyCountTimer(long millisInFuture, long countDownInterval) {  
            super(millisInFuture, countDownInterval);  
        }  
		@Override  
        public void onTick(long millisUntilFinished) {  
            txt_send.setText((millisUntilFinished / 1000) +"秒后重发");  
        }  
        @Override  
        public void onFinish() {  
        	txt_send.setText("重新发送验证码");  
        }  
    }

	/**
	 * 发送验证码
	 */
	private void requestSMSCode() {
		String number = etxt_phone.getText().toString();
		if (!TextUtils.isEmpty(number)) {
			timer = new MyCountTimer(60000, 1000);   
			timer.start();   
			BmobSMS.requestSMSCode(this, number, "重置密码模板",new RequestSMSCodeListener() {
				@Override
				public void done(Integer smsId, BmobException ex) {
					// TODO Auto-generated method stub
					if (ex == null) {// 验证码发送成功
						showToastS("验证码发送成功");// 用于查询本次短信发送详情
					}else{//如果验证码发送错误，可停止计时
						timer.cancel();
						showToastS("验证码发送错误");
					}
				}
			});
		} else {
			showToastS("请输入手机号码");
		}
	}

	/**
	 * 重置密码
	 *
	 */
	private void resetPwd() {
		final String code = etxt_code.getText().toString();
		final String pwd = etxt_password.getText().toString();
		if (TextUtils.isEmpty(code)) {
			showToastS("验证码不能为空");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			showToastS("密码不能为空");
			return;
		}
		final ProgressDialog progress = new ProgressDialog(FindPassActivity.this);
		progress.setMessage("正在重置密码...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9提供的重置密码功能，只需要输入验证码和新密码即可
		BmobUser.resetPasswordBySMSCode(this, code, pwd, new ResetPasswordByCodeListener() {
			
			@Override
			public void done(BmobException ex) {
				// TODO Auto-generated method stub
				progress.dismiss();
				if(ex==null){
					showToastS("密码重置成功");
					finish();
				}else{
					showToastS("密码重置失败：code="+ex.getErrorCode()+"，错误描述："+ex.getLocalizedMessage());
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(timer!=null){
			timer.cancel();
		}
	}
	
}
