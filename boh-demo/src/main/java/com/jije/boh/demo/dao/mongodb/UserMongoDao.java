package com.jije.boh.demo.dao.mongodb;

import org.springframework.stereotype.Component;

import com.jije.boh.demo.entity.User;

@Component
public class UserMongoDao {

	public void insert(User user)
	{
		System.out.println("使用mongodb插入user");
	}
}
