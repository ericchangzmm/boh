package com.jije.boh.demo.data;

import com.jije.boh.demo.entity.Department;

public class DepartmentData {

	public static Department getDefaultDepartment(){
		Department department = new Department();
		department.setId(1L);
		return department;
	}
}
