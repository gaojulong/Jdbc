package com.lovelqq.julong.jdbc.jdbcUtils;

public class Phonenumber {
	private int userid;
	private String username;
	private String phonenumber;
	//private static  int validate_flag=0;
	
	public Phonenumber(){
		
	}
	
	public Phonenumber(String username, String phonenumber) {
		super();
		this.username = username;
		this.phonenumber = phonenumber;
	}

	public Phonenumber(int userid, String username, String phonenumber) {
		super();
		this.userid = userid;
		this.username = username;
		this.phonenumber = phonenumber;
	}
	
//	public static int getValidate_flag() {
//		return validate_flag;
//	}
//
//	public static void setVsalidate_flag(int validate_flag) {
//		Phonenumber.validate_flag = validate_flag;
//	}

	@Override
	public String toString() {
		return "Phonenumber [userid=" + userid + ", username=" + username
				+ ", phonenumber=" + phonenumber + "]";
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

}
