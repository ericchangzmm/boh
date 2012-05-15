package com.jije.boh.demo.dto;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 6249707411218126723L;

	private String userName;
	private int userId;
	private String name;

	public UserInfo() {
		super();
	}

	public UserInfo(String userName, int userId, String name) {
		super();
		this.userName = userName;
		this.userId = userId;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}