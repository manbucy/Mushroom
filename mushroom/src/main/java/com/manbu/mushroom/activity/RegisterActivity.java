package com.manbu.mushroom.activity;

import java.util.List;

import com.manbu.mushroom.R;
import com.manbu.mushroom.common.BaseActivity;
import com.manbu.mushroom.bean.MyUser;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.UpdateListener;
/**
 * 业务逻辑：点击【发送验证码】后，会根据所填写的信息，进行判断，如果用户名、手机号码均未被使用（根据用户名、手机号码到数据库中查询）、
 * 两次密码一致，三个条件均满足则发送验证码。 点击【注册】后，首先会调用“用手机号码注册”的方法并登录，登录成功后对用户名及密码进行设置
* @ClassName: RegisterActivity
* @Description: TODO(注册界面)
* @author ManBu 
* @date 2016年8月18日 下午6:58:06
*
 */
public class RegisterActivity extends BaseActivity {
	private EditText etxt_username,etxt_password,etxt_pasagain,etxt_phone,etxt_vcode;
	private Button btn_register;
	private TextView txt_send,txt_tile;
	private TextView img_back;
	MyCountTimer timer;
	private MyUser mcur;
	private Integer flag1,flag2,flag3;

	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.register_activity);
	}


	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		etxt_username=(EditText) findViewById(R.id.register_etxt_username);
		etxt_password=(EditText) findViewById(R.id.register_etxt_password);
		etxt_pasagain=(EditText) findViewById(R.id.register_etxt_pwd_again);
		etxt_phone=(EditText) findViewById(R.id.register_etxt_phone);
		etxt_vcode=(EditText) findViewById(R.id.register_etxt_vcode);
		txt_send=(TextView) findViewById(R.id.register_txt_send);
		btn_register=(Button) findViewById(R.id.register_btn_register);
		txt_tile=(TextView) findViewById(R.id.tv_title);
		img_back=(TextView) findViewById(R.id.tv_left);
		img_back.setVisibility(View.VISIBLE);
		txt_tile.setText("注册");
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
		btn_register.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_left:
			Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.register_txt_send:
			flag1=1;flag2=1;flag3=1;
			check();
//			requestSMSCode();
			break;
		case R.id.register_btn_register:
			verifyOrBind();
			break;

		default:
			break;
		}
	}
	/**
	 * 
	* @ClassName: MyCountTimer
	* @Description: TODO(发送验证码控制)
	* @author ManBu 
	* @date 2016年8月18日 下午6:58:53
	*
	 */
	class MyCountTimer extends CountDownTimer{  //发送验证码
		  
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
        public void onReset(){
        	txt_send.setText("发送验证码");
        }
    } 
	
	/**
	 * 
	* @Title: check
	* @Description: TODO(验证该用户名、手机号码是否被使用，两次密码是否一致)
	* @param     设定文件
	* @return void    返回类型
	* @throws
	 */
	private void check(){
		String password = etxt_password.getText().toString();
		String pwd = etxt_pasagain.getText().toString();
		String name= etxt_username.getText().toString();
		String phone= etxt_phone.getText().toString();
		
		BmobQuery<BmobUser> queryname = new BmobQuery<BmobUser>();
		queryname.addWhereEqualTo("username",name);
		queryname.findObjects(RegisterActivity.this , new FindListener<BmobUser>() {
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    }
		    public void onSuccess(List<BmobUser> object) {
		        // TODO Auto-generated method stub
		    		if(object.size()==1){
		    			Log.e("qurryname---", "gongyou "+object.size());
		    			showToastS("该用户名已被注册");
			    		flag1=0;
		    		}
		    }
		});
		
		BmobQuery<BmobUser> queryphone = new BmobQuery<BmobUser>();
		queryphone.addWhereEqualTo("mobilePhoneNumber",phone);
		queryphone.findObjects(RegisterActivity.this , new FindListener<BmobUser>() {
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    }
		    public void onSuccess(List<BmobUser> object) {
		        // TODO Auto-generated method stub
		    	if(object.size()==1){
		    		Log.e("qurryphone---", "gongyou"+object.size());
		    		showToastS("该号码已被绑定");
		    		flag2=0;	
		    	}
		    }
		});
		
		if (!password.equals(pwd)) {
			showToastS("两次密码不一样");
			flag3=0;
		}
		if(flag1==1 && flag2==1 && flag3==1){
			requestSMSCode();
		}
			
	}
	
	/**
	* 
	* @Title: requestSMSCode
	* @Description: TODO(发送验证码)
	* @param     设定文件
	* @return void    返回类型
	* @throws
	*/
	private void requestSMSCode() {
		String number =  etxt_phone.getText().toString();	
		
		if (!TextUtils.isEmpty(number)) {
			timer = new MyCountTimer(60000, 1000);   
			timer.start();
			Log.e("star----", "qidongchenggong");
			BmobSMS.requestSMSCode(this, number, "手机号码登陆模板",new RequestSMSCodeListener() {
			@Override
				public void done(Integer smsId, BmobException ex) {
					// TODO Auto-generated method stub
					if (ex == null) {// 验证码发送成功
						showToastS("验证码发送成功");// 用于查询本次短信发送详情
					}else{//如果验证码发送错误，可停止计时
						timer.cancel();
//						timer.onFinish();
						timer.onReset();
						Log.e("cancel----", "fasongcuowu");
					}
				}
			});
		} else {
			showToastS("请输入手机号码");
		}
			
	}
	
	/**
	 * 
	* @Title: verifyOrBind
	* @Description: TODO(验证验证码)
	* @param     设定文件
	* @return void    返回类型
	* @throws
	 */
	private void verifyOrBind(){
		String phone = etxt_phone.getText().toString();
		String code = etxt_vcode.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			showToastS("手机号码不能为空");
			return;
		}
		if (TextUtils.isEmpty(code)) {
			showToastS("验证码不能为空");
			return;
		}
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("正在验证短信验证码...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9提供的一键注册或登录方式，可传手机号码和验证码
		BmobUser.signOrLoginByMobilePhone(RegisterActivity.this,phone, code, new LogInListener<MyUser>() {

			@Override
			public void done(MyUser myUser, BmobException ex) {
				// TODO Auto-generated method stub
				progress.dismiss();
				if(myUser !=null){
					setnamepass();
				}else{
					showToastS("验证码错误");
					Log.e("---zhuceshibai", "登录失败：code="+ex.getErrorCode()+"，错误描述："+ex.getLocalizedMessage());
				}
			}

		});
	}
	
	/**
	 * 
	* @Title: setnamepass
	* @Description: TODO(设置用户名和密码)
	* @param     设定文件
	* @return void    返回类型
	* @throws
	 */
	private void setnamepass() {
		// TODO Auto-generated method stub
		String account = etxt_username.getText().toString();
		String password = etxt_password.getText().toString();
		mcur = BmobUser.getCurrentUser(RegisterActivity.this,MyUser.class);
		BmobUser newUser = new BmobUser();
		newUser.setUsername(account);
		newUser.setPassword(password);
		newUser.update(RegisterActivity.this,mcur.getObjectId(),new UpdateListener() {
		    @Override
		    public void onSuccess() {
		        // TODO Auto-generated method stub
				Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
		    }
		    @Override
		    public void onFailure(int code, String msg) {
		        // TODO Auto-generated method stub;
		    }
		});
	}


}
