package com.lovelqq.julong.jdbc.jdbcUtils;

import android.util.Log;

public class ValidateUserPut {
	//验证登录时候密码长度和用户名长度
	public static int login(String username,String password) {
		//	Log.e("传来的", "name"+username+"pass"+password);
		if(usernam(username)==1&&password(password)==1)
		{
			Log.e("验证登录", "合法");
			return 1;
		}else {

			if(usernam(username)==2){
				Log.e("验证登录", "用户名不能为空");
				return 2;
			}
			if(usernam(username)==3){
				Log.e("验证登录", "用户名超出范围");
				return 3;
			}
			if(password(password)==2){

				Log.e("验证登录", "密码至少为6位");
				return 4;
			}
			if(password(password)==3){

				Log.e("验证登录", "密码不能超过16位");
				return 5;
			}
		}
		Log.e("返回0", "密码不合法");
		return 0;
	}

	/**
	 * 注册
	 * @param username
	 * @param password
	 * @param password1
	 * @return
	 */
	public static int zhuce(String username,String password,String password1,Long phonenumber) {
		Log.e("检查注册传来的参数", "name"+username+"密码:"+password+"密码:"+password1);
		if(!password.equals(password1)){
			Log.e("检查注册", "两次密码不相同");
			return 6;
		}
		//如果用户名长度密码长度手机号长度都符合
		if(usernam(username)==1&&password(password)==1&&phNumber(phonenumber)==1)
		{
			Log.e("检查注册", "合法");
			return 1;
		}else {

			if(usernam(username)==2){
				Log.e("检查注册", "用户名不能为空");
				return 2;
			}
			if(usernam(username)==3){
				Log.e("检查注册", "用户名超出范围");
				return 3;
			}
			if(password(password)==2){
				Log.e("检查注册", "密码至少为6位");
				return 4;
			}
			if(password(password)==3){
				Log.e("检查注册", "密码不能超过16位");
				return 5;
			}
			if(phNumber(phonenumber)==2){
				Log.e("检查注册", "手机号为空");
				return 7;
			}
			if(phNumber(phonenumber)==3){
				Log.e("检查注册", "手机号格式不正确");
				return 8;
			}
		}
		return 0;
	}

	/**
	 * 判断用户名字长度
	 * @param username
	 * @return
	 */
	public static int usernam(String username) {
		if (username.length()<20&&username.length()>0) {
			return 1;
		}
		if (username.length()<1) {
			return 2;
		}
		if (username.length()>20) {
			return 3;
		}
		return 0;
	}

	/**
	 * 判断密码长度
	 * @param password
	 * @return
	 */
	public static int password(String password) {
		if(password.length()<16&&password.length()>5)
		{
			return 1;
		}
		if(password.length()<6)
		{
			return 2;
		}
		if(password.length()>16)
		{
			return 3;
		}
		return 0;
	}

	/**
	 * 判断手机号长度
	 * @param phonenumber
	 * @return
	 */
	public static int  phNumber(Long phonenumber) {
		if(phonenumber.toString().length()==11)
		{
			return 1;
		}
		if (phonenumber.toString().equals("0")) {
			return 2;
		}
		return 3;
	}
}
