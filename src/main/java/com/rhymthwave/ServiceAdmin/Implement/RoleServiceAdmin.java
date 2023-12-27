package com.rhymthwave.ServiceAdmin.Implement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.DAO.RoleDAO;
import com.rhymthwave.ServiceAdmin.IAccountServiceAdmin;
import com.rhymthwave.ServiceAdmin.IRole;
import com.rhymthwave.entity.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceAdmin implements IRole{

	private final RoleDAO roleDAO;
	
	private final AccountDAO accountDAO;
	
	@Override
	public List<Role> findAllRole() {
		return roleDAO.findAll();
	}

	@Override
	public List<Role> findAllByRoleNotIn() {
		return roleDAO.findAllByRoleNotIn();
	}
	

}
