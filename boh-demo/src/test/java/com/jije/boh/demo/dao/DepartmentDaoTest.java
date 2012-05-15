package com.jije.boh.demo.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.jije.boh.demo.dao.IDepartmentDao;
import com.jije.boh.demo.entity.Department;

@ContextConfiguration(locations = { "/applicationContext-test.xml" })
public class DepartmentDaoTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private IDepartmentDao departmentDao;
	
	@Test
	public void getDepartmentDetail(){
		
		Department department = departmentDao.getDepartmentDetail(1L);
		assertEquals("Development", department.getName());
		assertEquals("Jack", department.getManager().getName());
		assertEquals(2, department.getUserList().size());
		assertEquals("Jack", department.getUserList().get(0).getName());
	}
}
