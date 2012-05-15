package com.jije.boh.demo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.jije.boh.demo.dao.IUserDao;
import com.jije.boh.demo.dao.mongodb.UserMongoDao;
import com.jije.boh.demo.dao.mybatis.UserMybatisDao;
import com.jije.boh.demo.entity.User;

//@Component
public class UserDao implements IUserDao {

	@Autowired
	private UserMybatisDao userMybatisDao;

	@Autowired
	private UserMongoDao userMongoDao;

	@Override
	public Long insert(User user) {

		long id = userMybatisDao.insert(user);
		userMongoDao.insert(user);

		return id;
	}

	@Override
	public void update(User user) {

		userMybatisDao.update(user);
	}

	@Override
	public void delete(long id) {

		userMybatisDao.delete(id);
	}

	@Override
	public User selectById(long id) {

		return userMybatisDao.selectById(id);
	}
	
	@Override
	public List<User> batchSelectById(List<Long> ids) {
		
		return userMybatisDao.batchSelectById(ids);
	}

	@Override
	public long count(Map<String, Object> parameter) {

		return userMybatisDao.count(parameter);
	}

	@Override
	public Page<User> selectPage(Pageable pageable,
			Map<String, Object> parameter) {

		return userMybatisDao.selectPage(pageable, parameter);
	}

	@Override
	public Iterable<User> select(Map<String, Object> parameter) {

		return userMybatisDao.select(parameter);
	}

	@Override
	public Iterable<User> select(Map<String, Object> parameter, Sort sort) {

		return userMybatisDao.select(parameter, sort);
	}

}
