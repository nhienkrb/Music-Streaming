package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.TagDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Tag;

import jakarta.transaction.Transactional;

@Service
public class TagServiceImpl implements CRUD<Tag, Integer>{
	
	@Autowired
	TagDAO dao;
	
	@Override
	@Transactional
	public Tag create(Tag entity) {
		if(entity!=null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	@Transactional
	public Tag update(Tag entity) {
		if(entity!=null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	@Transactional
	public Boolean delete(Integer key) {
		if(key instanceof Integer && key>0) {
			dao.deleteById(key);
			return true;
		}
		return false;
	}

	@Override
	public Tag findOne(Integer key) {
		if(key instanceof Integer && key>0) {
			return dao.findById(key).get();
		}
		return null;
	}

	@Override
	public List<Tag> findAll() {
		return dao.findAll();
	}
	
}
