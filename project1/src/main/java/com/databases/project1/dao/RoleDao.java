package com.databases.project1.dao;


import com.databases.project1.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
