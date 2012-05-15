package com.jije.boh.demo.dao.mybatis;

import org.springframework.stereotype.Component;

import com.jije.boh.core.mybatis.BaseMybatisDao;
import com.jije.boh.demo.entity.User;

@Component
public class UserMybatisDao extends BaseMybatisDao<User> {

	@Override
	protected String getNamespace() {
		return "User";
	}
}
