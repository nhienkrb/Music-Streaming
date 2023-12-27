package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.RoleDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Role;

import jakarta.transaction.Transactional;

@Service
public class RoleServiceImpl implements CRUD<Role, Integer>{
	@Autowired
	RoleDAO dao;

	@Override
	@Transactional
	public Role create(Role entity) {
		Role role = entity;
		return dao.save(role);
	}

	@Override
	@Transactional
	public Role update(Role entity) {
		Role role = entity;
		return dao.save(role);
	}

	@Override
	@Transactional
	public Boolean delete(Integer key) {
		dao.deleteById(key);
		return true;
	}

	@Override
	public Role findOne(Integer key) {
		return dao.findById(key).get();
	}

	@Override
	public List<Role> findAll() {
		return dao.findAll();
	}
}
