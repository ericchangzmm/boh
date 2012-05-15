package com.jije.boh.demo.entity;

import java.util.ArrayList;
import java.util.List;

import com.jije.boh.core.entity.IdEntity;


public class Department extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private User manager;
	
	private List<User> userList = new ArrayList<User>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	
}
