package com.jije.boh.demo.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jije.boh.demo.dao.IDepartmentDao;
import com.jije.boh.demo.dao.mybatis.DepartmentMybatisDao;
import com.jije.boh.demo.entity.Department;

@Component
public class DepartmentDao implements IDepartmentDao{

	@Autowired
	private DepartmentMybatisDao departmentMybatisDao;
	
	@Override
	public Department getDepartmentDetail(Long id) {
		
		return departmentMybatisDao.getDepartmentDetail(id);
	}

}
