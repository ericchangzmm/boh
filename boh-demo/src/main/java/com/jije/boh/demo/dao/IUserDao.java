package com.jije.boh.demo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jije.boh.demo.entity.User;

public interface IUserDao {

	Long insert(User user);

	void update(User user);

	void delete(long id);

	List<User> batchSelectById(List<Long> ids);
	
	User selectById(long id);

	long count(Map<String, Object> parameter);

	Page<User> selectPage(final Pageable pageable, Map<String, Object> parameter);
	
	Iterable<User> select(Map<String, Object> parameter);
	
	Iterable<User> select(Map<String, Object> parameter, Sort sort);
}
