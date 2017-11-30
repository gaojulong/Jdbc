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

public class SqlSentence {

	//云端联系人
	public static ArrayList<Phonenumber> numberAllConten (  ) {

		final ArrayList<Phonenumber> list = new ArrayList<Phonenumber>();
		Homepage homepage=new Homepage();
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
	 * 查找用户是否存在
	 * @param user
	 */
	private static int usid;
	public static int  loginuser(final User user) {
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				Connection conn=null;
				PreparedStatement ps=null;
				ResultSet rs=null;
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

					}else{
						Log.e("登录", "登录失败");
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
	 */
	public static void  insetUser(final User user) {
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				Connection conn=null;
				PreparedStatement ps=null;
				try {
					String sql="INSERT INTO login(username,PASSWORD,phonenumber) VALUES(?,?,?)";
					conn=JdbcUtils.getconnection();
					ps=conn.prepareStatement(sql);
					ps.setString(1, user.getUsername());
					ps.setString(2, user.getPassword());
					ps.setLong(3, user.getPhonenumber());
					int re=ps.executeUpdate();
					System.out.println("受影响行数"+re);
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
	/**
	 * 插入手机号
	 * @param phonenumber
	 */
	public static void  insetPhoneNumber(final Phonenumber phonenumber) {
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				Connection conn=null;
				PreparedStatement ps=null;
				try {
					String sql="INSERT INTO phonenumber (userid,username,phonenumber)VALUES(?,?,?)";
					conn=JdbcUtils.getconnection();
					ps=conn.prepareStatement(sql);
					ps.setInt(1, phonenumber.getUserid());
					ps.setString(2,phonenumber.getUsername());
					ps.setString(3, phonenumber.getPhonenumber());
					int re=ps.executeUpdate();
					System.out.println("受影响行数"+re);
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
