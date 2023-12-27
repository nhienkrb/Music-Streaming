package com.rhymthwave.ServiceAdmin;

import java.util.List;

import com.rhymthwave.entity.Role;

public interface IRole {

	List<Role> findAllRole();
	
	List<Role> findAllByRoleNotIn();
}
