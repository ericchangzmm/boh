package com.jije.boh.demo.dao.mybatis;

import org.springframework.stereotype.Component;

import com.jije.boh.core.mybatis.MemcacheMybatisDao;
import com.jije.boh.demo.entity.User;

@Component
public class UserMybatisCacheDao extends MemcacheMybatisDao<User> {

	@Override
	protected String getNamespace() {
		return "User";
	}

	
}
