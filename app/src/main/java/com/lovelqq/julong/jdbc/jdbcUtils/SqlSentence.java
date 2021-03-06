package com.lovelqq.julong.jdbc.jdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import android.os.Message;
import android.util.Log;

import com.lovelqq.julong.jdbc.GetTime.AbstractMethod;
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
				Log.e("云端联系人","读取云端联系人"+list.size()+"条");
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
	private static int usid=-1;
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
						//发送
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
	 * 查找用户的id
	 * 传进来openid
	 * @param openid
	 * @return
	 */
	public static int  loginopenid(final String openid) {
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				Connection conn=null;
				PreparedStatement ps=null;
				ResultSet rs=null;
				usid=-1;
				try {
					String sql="SELECT * from login WHERE openid=? ";
					conn=JdbcUtils.getconnection();
					ps=conn.prepareStatement(sql);
					ps.setString(1,openid);
					rs=ps.executeQuery();
					if (rs.next()) {
						usid=rs.getInt("userid");
						User.setLogin_flay(1);
						Log.e("登录", "登录成功");
						//发送
					}else{
						Log.e("登录", "登录失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("数据库连接查找openid", "连接失败");
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
	 * 没有opened，进行注册
	 */
	//插入的返回值
	private static int insetflag=-1;
	public static int insetopenid(final String openid) {
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				Connection conn=null;
				PreparedStatement ps=null;
				try {
						conn=JdbcUtils.getconnection();
						String sql="INSERT INTO login(openid) VALUES(?)";
						ps=conn.prepareStatement(sql);
						ps.setString(1, openid);
						//受影响的行数
						insetflag=ps.executeUpdate();
						System.out.println("受影响行数"+insetflag);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("数据库连接openid注册", "连接失败");
				}finally{
					JdbcUtils.freeResorce(conn, ps, null);
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
		return insetflag;
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
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
                //正在上传的个数
                int i=0;
                try {
                    for (Phonenumber phone:Phonenumber)
                    {
                        Message msg=new Message();
                        msg.what=4;
                        msg.arg1=i;
                        msg.arg2=Phonenumber.size();
                        Homepage.handler.sendMessage(msg);
                        String sql="INSERT INTO phonenumber (userid,username,phonenumber)VALUES(?,?,?)";
                        conn=JdbcUtils.getconnection();
                        ps=conn.prepareStatement(sql);
                        ps.setInt(1, phone.getUserid());
                        ps.setString(2,phone.getUsername());
                        ps.setString(3, phone.getPhonenumber());
                        int re=ps.executeUpdate();
                        Log.e("插入手机号","受影响行数"+re);
                        i++;//同步个数+1

                    }
                    Message msg1=new Message();
                    msg1.what=1;
                    msg1.arg1=Phonenumber.size();
                    Homepage.handler.sendMessage(msg1);
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
	 * 连接数据库
	 */
	public static void connecSql(final Connec coninter){
		Thread threadCon =new Thread(new Runnable() {
			//Connection conn=null;
			@Override
			public void run() {
				try {
					conn=JdbcUtils.getconnection();
					coninter.OnSucceed(conn);
					coninter.Onclossql();
				} catch (Exception e) {
					e.printStackTrace();
					coninter.OnError(e);
				}
			}
		});
		threadCon.start();
	}


	/**
	 * 用户登录验证
	 * @param name 用户名
	 * @param pswd	密码
	 */
	private  static Connection conn=null;
	private  static PreparedStatement ps=null;
	private  static ResultSet rs=null;
	public static int loginuse(final String name, final String pswd){
		connecSql(new Connec() {
			@Override
			public void OnSucceed(Connection conn) throws SQLException {
				Log.e("连接测试", "成功");
				String sql="SELECT * from login WHERE username=? and password=?";
				ps=conn.prepareStatement(sql);
					ps.setString(1,name);
					ps.setString(2,pswd);
					rs=ps.executeQuery();
					if (rs.next()) {
						usid=rs.getInt("userid");
						Log.e("登录", "登录成功");
						//发送
					}else{
						Log.e("登录", "登录失败");
					}

			}

			@Override
			public void OnError(Exception e) {
				Log.e("连接测试", "失败");
			}

			@Override
			public void Onclossql() {
				JdbcUtils.freeResorce(conn, ps, rs);
				Log.e("连接测试", "数据库关闭");
			}
		});

		return usid;
	}

}

/**
 * 连接数据库成功后回调的接口
 */
interface Connec{
	public void OnSucceed(Connection conn)throws SQLException;
	public void OnError(Exception e);
	public void Onclossql();
}

