package com.lovelqq.julong.jdbc.jdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import android.os.Message;
import android.util.Log;

import com.lovelqq.julong.jdbc.user.Phonenumber;
import com.lovelqq.julong.jdbc.user.User;
import com.lovelqq.julong.jdbc.userlogin.Homepage;
import com.lovelqq.julong.jdbc.userlogin.Login;
import com.lovelqq.julong.jdbc.userlogin.Zhuce;

import javax.security.auth.login.LoginException;

public class SqlSentence {
	//云端联系人
	public static ArrayList<Phonenumber> numberAllConten (  ) {

		final ArrayList<Phonenumber> list = new ArrayList<Phonenumber>();
		//Homepage homepage=new Homepage();
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				Connection conn=null;
				PreparedStatement ps=null;
				ResultSet rs=null;
				try {
					String sql="SELECT * from phonenumber WHERE userid=?";
					conn=JdbcUtils.getconnection();
					ps=conn.prepareStatement(sql);
					ps.setInt(1, User.getId());
					rs=ps.executeQuery();
					while (rs.next()) {
						Phonenumber phonenumber=new Phonenumber();
						phonenumber.setUsername(rs.getString("username"));
						phonenumber.setPhonenumber(rs.getString("phonenumber"));
						list.add(phonenumber);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("数据库连接", "连接失败");
				}finally{
					JdbcUtils.freeResorce(conn, ps, rs);
				}
				Message msg=new Message();
				msg.what=2;
				msg.arg1=list.size();
				Homepage.handler.sendMessage(msg);
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 查找用户账号和 密码是否匹配
	 * @param user
	 * usid返回查到的用户ID
	 */
	private static int usid;
    private  static Login login=new Login();
	public static int  loginuser(final User user) {
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				Connection conn=null;
				PreparedStatement ps=null;
				ResultSet rs=null;

				//定义msg，发送id到Login里
				Message msg=new Message();
				msg.what=1;//获取用户id
				usid=-1;
				try {
					String sql="SELECT * from login WHERE username=? and password=?";
					conn=JdbcUtils.getconnection();
					ps=conn.prepareStatement(sql);
					ps.setString(1,user.getUsername());
					ps.setString(2, user.getPassword());
					rs=ps.executeQuery();
					if (rs.next()) {
						usid=rs.getInt("userid");
						User.setLogin_flay(1);
						Log.e("登录", "登录成功");
						//发送
						msg.arg1=usid;
						login.handlerLogin.sendMessage(msg);
					}else{
					    msg.arg1=-1;
						Log.e("登录", "登录失败");
                        login.handlerLogin.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("数据库连接", "连接失败");
				}finally{
					JdbcUtils.freeResorce(conn, ps, rs);
				}
			}
		});
		thread.start();

		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usid;
	}
	/**
	 * 判断手机号是否已经存在返回布尔类型
	 */
	//如果查询到了返回ture
	static boolean flage1=false;
	public static boolean validatePhone (final Phonenumber phonenumber) {

		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				Connection conn=null;
				PreparedStatement ps=null;
				ResultSet rs=null;

				try {
					String sql="SELECT * from phonenumber WHERE username=? and phonenumber=?";
					conn=JdbcUtils.getconnection();
					ps=conn.prepareStatement(sql);
					ps.setString(1,phonenumber.getUsername());
					ps.setString(2, phonenumber.getPhonenumber());
					rs=ps.executeQuery();
					if (rs.next()) {
						flage1=true;
						Log.e("phonenumber", "查询到记录返回true");

					}else{
						Log.e("phonenumber", "未查询到");
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("数据库连接", "连接失败");
				}finally{
					JdbcUtils.freeResorce(conn, ps, rs);
				}
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flage1;
	}
	/**
	 * 注册用户
	 * @param user
	 * 2表示已有此用户
	 * 1表示注册成功
	 */
	public static void insetUser(final User user) {
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				Connection conn=null;
				PreparedStatement ps=null;
				ResultSet rs=null;
				Message msg=new Message();
				msg.what=1;
				try {
					String sql="SELECT * from login WHERE username=?";
					conn=JdbcUtils.getconnection();
					ps=conn.prepareStatement(sql);
					ps.setString(1,user.getUsername());
					rs=ps.executeQuery();
					if (rs.next()) {
						Log.e("用户已经存在","无法注册");
						msg.arg1=2;
						Zhuce.handler.sendMessage(msg);
					}else{
						String sql1="INSERT INTO login(username,PASSWORD,phonenumber) VALUES(?,?,?)";
						ps=conn.prepareStatement(sql1);
						ps.setString(1, user.getUsername());
						ps.setString(2, user.getPassword());
						ps.setLong(3, user.getPhonenumber());
						//受影响的行数
						int re=ps.executeUpdate();
						System.out.println("受影响行数"+re);
						msg.arg1=1;
						Zhuce.handler.sendMessage(msg);
					}

				} catch (Exception e) {
					e.printStackTrace();
					Log.e("数据库连接", "连接失败");
				}finally{
					JdbcUtils.freeResorce(conn, ps, rs);
				}
			}
		});
		thread.start();
	}
	/**
	 * 上传手机号
	 * @param
	 */
    public static void  insetPhoneNumber(final ArrayList<Phonenumber> Phonenumber) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn=null;
                PreparedStatement ps=null;
                try {
                    for (Phonenumber phone:Phonenumber)
                    {
                        String sql="INSERT INTO phonenumber (userid,username,phonenumber)VALUES(?,?,?)";
                        conn=JdbcUtils.getconnection();
                        ps=conn.prepareStatement(sql);
                        ps.setInt(1, phone.getUserid());
                        ps.setString(2,phone.getUsername());
                        ps.setString(3, phone.getPhonenumber());
                        int re=ps.executeUpdate();
                        Log.e("插入手机号","受影响行数"+re);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("数据库连接", "连接失败");
                }finally{
                    JdbcUtils.freeResorce(conn, ps, null);
                }
            }
        });
        thread.start();
    }
}
