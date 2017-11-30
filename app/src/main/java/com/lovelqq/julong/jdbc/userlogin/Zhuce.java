package com.lovelqq.julong.jdbc.userlogin;

//import com.lovelqq.jdbcUtils.SqlSentence;
//import com.lovelqq.jdbcUtils.ValidateUserPut;
import com.lovelqq.julong.jdbc.R;
import com.lovelqq.julong.jdbc.jdbcUtils.SqlSentence;
import com.lovelqq.julong.jdbc.user.User;
import com.lovelqq.julong.jdbc.jdbcUtils.ValidateUserPut;
//import com.lovelqq.user.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class Zhuce extends Activity {
	private Button zcbt;
	private EditText username,password,password1,phonenumber;
	private Context context=this;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhuce);
		init();
		zcbt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String strusername="",strpassword="",strpassword1="",strphonenumber="";
				strusername=username.getText().toString();
				strpassword=password.getText().toString();
				strpassword1=password1.getText().toString();
				strphonenumber=phonenumber.getText().toString();

				if(strphonenumber.equals(""))
				{
					strphonenumber="0";
				}
				int i=	ValidateUserPut.zhuce(strusername, strpassword, strpassword1,Long.parseLong(strphonenumber));
				Log.e("注册验证返回值",""+i);
				switch (i) {
					case 1:
						User user=new User(strusername,strpassword,Long.parseLong(strphonenumber));
						SqlSentence.insetUser(user);
						Toast.makeText(context, "注册成功", Toast.LENGTH_LONG).show();
						break;
					case 2:
						Toast.makeText(context, "用户名不能为空", Toast.LENGTH_SHORT).show();
						break;
					case 3:
						Toast.makeText(context, "用户名超出范围", Toast.LENGTH_LONG).show();
						break;
					case 4:
						Toast.makeText(context, "密码至少为6位", Toast.LENGTH_LONG).show();
						break;
					case 5:
						Toast.makeText(context, "密码不能超过16位", Toast.LENGTH_LONG).show();
					case 6:
						Toast.makeText(context, "两次密码不相同", Toast.LENGTH_LONG).show();
						break;
					case 7:
						Log.e("检查注册", "手机号为空");
						user=new User(strusername,strpassword,Long.parseLong(strphonenumber));
						SqlSentence.insetUser(user);
						Toast.makeText(Zhuce.this, "注册成功", Toast.LENGTH_LONG).show();
						break;
					case 8:
						Toast.makeText(context, "手机号格式不正确", Toast.LENGTH_LONG).show();
						break;
					default:
						Toast.makeText(context, "未知错误", Toast.LENGTH_LONG).show();
						break;
				}
			}
		});
	}
	private void  init() {
		zcbt=(Button) findViewById(R.id.btzc);
		username=(EditText) findViewById(R.id.username);
		password=(EditText) findViewById(R.id.password);
		password1=(EditText) findViewById(R.id.password1);
		phonenumber=(EditText) findViewById(R.id.phonenumber);
	}
}
