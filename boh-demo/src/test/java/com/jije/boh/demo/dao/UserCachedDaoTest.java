package com.jije.boh.demo.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.jije.boh.demo.entity.User;

@ContextConfiguration(locations = { "/applicationContext-test.xml",
		"/applicationContext-test-memcached.xml" })
public class UserCachedDaoTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private IUserDao userDao;

	@Test
	public void getUser() {

		User user = userDao.selectById(1L);
		assertEquals("user1", user.getLoginName());
		assertEquals(new Long(1L), user.getDepartment().getId());

		User user2 = userDao.selectById(1L);
		assertEquals("user1", user2.getLoginName());
		assertEquals(new Long(1L), user2.getDepartment().getId());

		user2.setLoginName("user1updated");
		userDao.update(user2);

		User user3 = userDao.selectById(1L);
		assertEquals("user1updated", user3.getLoginName());

		user = userDao.selectById(999L);
		assertNull(user);
	}

	@Test
	@SuppressWarnings("unused")
	public void batchGetUser() {

		User user = userDao.selectById(1L);

		List<Long> ids = new ArrayList<Long>();
		ids.add(new Long(1L));
		ids.add(new Long(2L));

		List<User> users = userDao.batchSelectById(ids);

		List<User> users2 = userDao.batchSelectById(ids);
	}

}
