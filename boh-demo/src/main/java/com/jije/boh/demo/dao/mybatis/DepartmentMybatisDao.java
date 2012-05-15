package com.jije.boh.demo.dao.mybatis;

import org.springframework.stereotype.Component;

import com.jije.boh.core.mybatis.BaseMybatisDao;
import com.jije.boh.demo.entity.Department;

@Component
public class DepartmentMybatisDao extends BaseMybatisDao<Department> {

	public Department getDepartmentDetail(Long id) {
		return (Department) getSqlSession().selectOne(
				getStatement("getDepartmentDetail"), id);
	}

	@Override
	protected String getNamespace() {
		return "Department";
	}
}
