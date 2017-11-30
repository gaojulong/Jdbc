package com.lovelqq.julong.jdbc.userlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lovelqq.julong.jdbc.R;
import com.lovelqq.julong.jdbc.jdbcUtils.SqlSentence;
import com.lovelqq.julong.jdbc.jdbcUtils.ValidateUserPut;
import com.lovelqq.julong.jdbc.user.User;

public class Login extends Activity implements OnClickListener{
	private Button loginbt,zhucebt,zhaohuibt;
	private EditText edusernaem,edpassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		loginbt=(Button) findViewById(R.id.loginbt);
		zhucebt=(Button) findViewById(R.id.zhucebt);
		zhaohuibt=(Button) findViewById(R.id.zhaohuibt);
		edusernaem=(EditText) findViewById(R.id.loginusername);
		edpassword=(EditText) findViewById(R.id.loginpassword);

		loginbt.setOnClickListener(this);
		zhucebt.setOnClickListener(this);
		zhaohuibt.setOnClickListener(this);

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
				login();
				break;

			default:
				break;
		}
	}
	private void  login() {
		String strname=edusernaem.getText().toString();
		String strpassw=edpassword.getText().toString();
		//判断用户输入是否合法
		int i= ValidateUserPut.login(strname,strpassw);
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
					Toast.makeText(Login.this,"登录失败",Toast.LENGTH_SHORT).show();
				}
				break;
			case 2:
				Log.e("验证登录", "用户名不能为空");
				break;
			case 3:
				Log.e("验证登录", "用户名超出范围");
				break;
			case 4:
				Log.e("验证登录", "密码至少为6位");
				break;
			case 5:
				Log.e("验证登录", "密码不能超过16位");
				break;
			default:
				Log.e("验证登录", "未知错误");
				break;
		}
	}

}
