package com.lovelqq.julong.jdbc.user;

public class User {
	
	private static int login_flay;
	public static  int id;
	private  String username;
	private  String password;
	private  String password1;
	private long phonenumber;


	public User(){
		
	};	

	public User( String username, String password, String password1,
			long phonenumber) {
		super();
		this.username = username;
		this.password = password;
		this.password1 = password1;
		this.phonenumber = phonenumber;
	}
	public User( String username, String password,
			long phonenumber) {
		super();
		this.username = username;
		this.password = password;
		this.phonenumber = phonenumber;
	}

	public User( String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public static int getLogin_flay() {
		return login_flay;
	}

	public static void setLogin_flay(int login_flay) {
		User.login_flay = login_flay;
	}
	
	public  long getPhonenumber() {
		return phonenumber;
	}


	public void setPhonenumber(long phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}


	public static int getId() {
		return id;
	}
	public void setId(int id) {
		User.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", phonenumber=" + phonenumber + "]";
	}

}
