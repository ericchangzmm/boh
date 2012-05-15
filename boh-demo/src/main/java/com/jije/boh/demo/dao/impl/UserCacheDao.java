package com.jije.boh.demo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.jije.boh.core.mapper.JsonMapper;
import com.jije.boh.core.memcached.MemcachedObjectType;
import com.jije.boh.demo.dao.IUserDao;
import com.jije.boh.demo.dao.mybatis.UserMybatisCacheDao;
import com.jije.boh.demo.entity.User;

@Component
public class UserCacheDao implements IUserDao {

	@Autowired
	private UserMybatisCacheDao userMybatisCacheDao;

	private JsonMapper jsonMapper = JsonMapper.buildNonDefaultMapper();

	@Override
	public Long insert(User user) {
		return userMybatisCacheDao.insert(user);
	}

	@Override
	public void update(User user) {
		userMybatisCacheDao.updateCached(user,
				MemcachedObjectType.USER.getPrefix(),
				MemcachedObjectType.USER.getExpiredTime(), jsonMapper);
	}

	@Override
	public void delete(long id) {
		userMybatisCacheDao.deleteCached(id,
				MemcachedObjectType.USER.getPrefix());
	}

	@Override
	public List<User> batchSelectById(List<Long> ids) {
		return userMybatisCacheDao.batchSelectByIdCached(ids,
				MemcachedObjectType.USER.getPrefix(),
				MemcachedObjectType.USER.getExpiredTime(), jsonMapper);
	}

	@Override
	public User selectById(long id) {
		return userMybatisCacheDao.selectByIdCached(id,
				MemcachedObjectType.USER.getPrefix(),
				MemcachedObjectType.USER.getExpiredTime(), jsonMapper);
	}

	@Override
	public long count(Map<String, Object> parameter) {
		return userMybatisCacheDao.count(parameter);
	}

	@Override
	public Page<User> selectPage(Pageable pageable,
			Map<String, Object> parameter) {
		return userMybatisCacheDao.selectPage(pageable, parameter);
	}

	@Override
	public Iterable<User> select(Map<String, Object> parameter) {
		return userMybatisCacheDao.select(parameter);
	}

	@Override
	public Iterable<User> select(Map<String, Object> parameter, Sort sort) {
		return userMybatisCacheDao.select(parameter, sort);
	}

}
