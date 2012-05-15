package com.jije.boh.demo.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.jije.boh.demo.data.UserData;
import com.jije.boh.demo.entity.User;

@ContextConfiguration(locations = { "/applicationContext-test.xml" })
public class UserDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private IUserDao userDao;

	@Test
	public void saveUser() {

		User user = UserData.getRandomUser();
		Long id = userDao.insert(user);
		assertEquals(new Long(5L), id);

		assertEquals(4 + 1, this.countRowsInTable("acct_user"));
	}

	@Test
	public void getUser() {

		User user = userDao.selectById(1L);
		assertEquals("user1", user.getLoginName());
		assertEquals(new Long(1L), user.getDepartment().getId());

		user = userDao.selectById(999L);
		assertNull(user);
	}
	
	@Test
	public void getUserBatch() {

		List<Long> ids = new ArrayList<Long>();
		ids.add(new Long(1L));
		ids.add(new Long(2L));
		List<User> users = userDao.batchSelectById(ids);
		
		assertEquals(2, users.size());
	}

	@Test
	public void searchUser() {

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("loginName", null);
		parameters.put("name", null);
		List<User> result = (List<User>) userDao.select(parameters);
		assertEquals(4, result.size());

		parameters.put("loginName", "user1");
		parameters.put("name", null);
		result = (List<User>) userDao.select(parameters);
		assertEquals(1, result.size());

		parameters.clear();
		parameters.put("name", "Jack");
		parameters.put("loginName", null);
		result = (List<User>) userDao.select(parameters);
		assertEquals(1, result.size());

		parameters.clear();
		parameters.put("name", "Jack");
		parameters.put("loginName", "user1");
		result = (List<User>) userDao.select(parameters);
		assertEquals(1, result.size());

		parameters.clear();
		parameters.put("name", "Jack");
		parameters.put("loginName", "errorName");
		result = (List<User>) userDao.select(parameters);
		assertEquals(0, result.size());
	}

	@Test
	public void searchUserOrder() {

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("loginName", null);
		parameters.put("name", null);
		List<User> result = (List<User>) userDao.select(parameters, new Sort(
				new Order(Direction.DESC, "id"), new Order(Direction.ASC, "login_name")));
		assertEquals(4, result.size());
		assertEquals(new Long(4L), result.get(0).getId());
	}

	@Test
	public void searchUserPage() {

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("loginName", null);
		parameters.put("name", null);
		Page<User> page = userDao.selectPage(new PageRequest(0, 2, Direction.DESC, "id"), parameters);
		assertEquals(4, page.getTotalElements());
		assertEquals(2, page.getSize());
		assertEquals(new Long(4L), page.iterator().next().getId());
	}
}
