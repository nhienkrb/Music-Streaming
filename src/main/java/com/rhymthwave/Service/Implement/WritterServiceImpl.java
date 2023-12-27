package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.WritterDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.WritterService;
import com.rhymthwave.entity.Writter;

import jakarta.transaction.Transactional;

@Service
public class WritterServiceImpl implements WritterService,CRUD<Writter, Long>{
	
	@Autowired
	WritterDAO dao;
	
	@Override
	@Transactional
	public Writter create(Writter entity) {
		if(entity!=null) {
			dao.save(entity);
		}
		return null;
	}

	@Override
	public Writter update(Writter entity) {
		if(entity!=null) {
			dao.save(entity);
		}
		return null;
	}

	@Override
	public Boolean delete(Long key) {
		if(key instanceof Long && key != null) {
			dao.deleteById(key);
			return true;
		}
		return false;
	}

	@Override
	public Writter findOne(Long key) {
		if(key instanceof Long && key != null) {
			return dao.findById(key).get();
		}
		return null;
	}

	@Override
	public List<Writter> findAll() {
		return dao.findAll();
	}

	@Override
	public List<Writter> getFindWritterSong(Long id) {
		return dao.getFindWritterSong(id);
	}
	
	
}
