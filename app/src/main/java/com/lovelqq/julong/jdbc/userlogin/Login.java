package com.lovelqq.julong.jdbc.userlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lovelqq.julong.jdbc.R;
import com.lovelqq.julong.jdbc.jdbcUtils.LoginUtils;
import com.lovelqq.julong.jdbc.jdbcUtils.SqlSentence;
import com.lovelqq.julong.jdbc.jdbcUtils.ValidateUserPut;
import com.lovelqq.julong.jdbc.user.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class Login extends Activity implements OnClickListener{
	public String mAppid = "1106429423";
	private Tencent mTencent;
	private UserInfo mInfo;
	//获取的用户openid
	public  String openId;
	//
	private Button loginbt,zhucebt,zhaohuibt,qqlogin;
	private EditText edusernaem,edpassword;
	private static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		context=getApplicationContext();
		// 实例化
		mTencent = Tencent.createInstance(mAppid, this);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		loginbt=(Button) findViewById(R.id.loginbt);
		zhucebt=(Button) findViewById(R.id.zhucebt);
		zhaohuibt=(Button) findViewById(R.id.zhaohuibt);
		edusernaem=(EditText) findViewById(R.id.loginusername);
		edpassword=(EditText) findViewById(R.id.loginpassword);
		qqlogin= (Button) findViewById(R.id.loginQqbt);

		loginbt.setOnClickListener(this);
		zhucebt.setOnClickListener(this);
		zhaohuibt.setOnClickListener(this);
		qqlogin.setOnClickListener(this);


	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
			case R.id.zhucebt:
				Intent intent=new Intent(Login.this,Zhuce.class);
				startActivity(intent);
				break;
			case R.id.zhaohuibt:

				break;
			case R.id.loginbt:
				//设置点击间隔不小于2秒
				if (LoginUtils.isFastClick())
				login();
				break;
			case R.id.loginQqbt:
				//第三方QQ登录
				mTencent.login(Login.this, "all", listener);
				break;

			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Tencent.onActivityResultData(requestCode,resultCode,data,listener);
		super.onActivityResult(requestCode, resultCode, data);
	}
	// 实例化回调接口
	IUiListener listener = new BaseUiListener() {
		@Override
		protected void doComplete(JSONObject values) {
			Log.e("登陆信息", "登陆信息"+values.toString());
			//获取用户openid
			initOpenidAndToken(values);
			//回调获取openID进行登录
			qqlogin();
			//获取用户信息
			//updateUserInfo();
		}
	};
	/**
	 * 调用SDK封装好的借口，需要传入回调的实例 会返回服务器的消息
	 */
	private class BaseUiListener implements IUiListener {
		@Override
		public void onComplete(Object response) {
			JSONObject jsonResponse=(JSONObject)response;

			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			Log.e("wang", e.toString());
		}

		@Override
		public void onCancel() {
		}
	}

	/**
	 * 获取用户个人信息
	 */
	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {
				@Override
				public void onError(UiError e) {
					// TODO Auto-generated method stub
					Log.e("wang", "userInfo 错误");
				}

				@Override
				public void onComplete(final Object response) {
					JSONObject jsonObject=(JSONObject)response;
					Log.e("用户信息", "用户信息"+jsonObject.toString());
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

				}
			};
			mInfo = new UserInfo(this, mTencent.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
		}
	}

	//初始化OPENID和TOKEN值（为了得了用户信息）
	public  void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			 openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
			}

		} catch(Exception e) {
		}

	}
	//第三方QQ登录
	private void qqlogin(){
		Log.e("OPENID","openid进行查找");
		User.id=SqlSentence.loginopenid(openId);
		Log.e("useeid",User.id+"");
		if (User.id!=-1) {
            Toast.makeText(context,"QQ登录成功",Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Login.this, Homepage.class);
			startActivity(intent);
		}
		else {
			//不存在openid用户，注册openid用户,返回受影响的行数
			int i=SqlSentence.insetopenid(openId);
			if(i>0){
                User.id=SqlSentence.loginopenid(openId);
                Log.e("注册useeid，再进行查询openid",User.id+"");
                if (User.id!=-1) {
                    Toast.makeText(context,"首次QQ登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Homepage.class);
                    startActivity(intent);
                }
            }
            else {
                Toast.makeText(context,"QQ登录失败",Toast.LENGTH_SHORT).show();
            }
		}

	}
	private void  login() {
		String strname=edusernaem.getText().toString();
		String strpassw=edpassword.getText().toString();
		//判断用户输入是否合法
		int i= ValidateUserPut.login(strname,strpassw);
		Log.e("登录返回值",""+i);
		switch (i) {
			//返回1用户名和密码输入合法
			case 1:
				User user=new User(strname,strpassw);
				//在这给查找用户id，复制给User.id
				User.id= SqlSentence.loginuser(user);
				Log.e("登录获取","userid"+User.getId()+"在线标识"+User.getLogin_flay());
				if (User.id!=-1) {
					Intent intent = new Intent(Login.this, Homepage.class);
					startActivity(intent);
				}
				else {
					Toast.makeText(Login.this,"用户名和密码不匹配",Toast.LENGTH_SHORT).show();
				}
				break;
			case 2:
				Toast.makeText(Login.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(Login.this,"用户名超出范围",Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(Login.this,"密码至少为6位",Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(Login.this,"密码不能超过16位",Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(Login.this,"未知错误",Toast.LENGTH_SHORT).show();
				break;
		}
	}

}
