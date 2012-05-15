package com.jije.boh.demo.data;

import com.jije.boh.core.test.data.RandomData;
import com.jije.boh.demo.entity.Department;
import com.jije.boh.demo.entity.User;

public class UserData {

	public static User getRandomUser(){
		String userName = RandomData.randomString("User");
		Department department = DepartmentData.getDefaultDepartment();
		
		User user = new User();
		user.setLoginName(userName);
		user.setName(userName);
		user.setPassword("123456");
		user.setEmail(userName+"@qq.com");
		user.setDepartment(department);
		
		return user;
	}
}
