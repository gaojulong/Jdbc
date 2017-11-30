package com.lovelqq.julong.jdbc.jdbcUtils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import android.util.Log;
public class JdbcUtils {
	static String driver = "com.mysql.jdbc.Driver";
	static String use="long";
	static   String paw="123456";
	static  String url="jdbc:mysql://123.206.32.248:3306/user?useUnicode=true&characterEncoding=utf-8";
	static{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("加载驱动", "加载驱动失败");
		}
	}
	//链接数据库
	public static Connection getconnection() throws Exception{
		return DriverManager.getConnection(url, use, paw);
	}
	public static void freeResorce(Connection conn ,PreparedStatement ps,ResultSet rs){
		try {
			if(null!=rs){
				rs.close();
			}
			if(null!=ps){
				ps.close();
			}
			if(null!=conn){
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
