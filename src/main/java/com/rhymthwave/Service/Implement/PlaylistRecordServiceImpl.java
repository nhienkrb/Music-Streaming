package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.PlaylistRecordDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.PlaylistRecord;

import jakarta.transaction.Transactional;

@Service
public class PlaylistRecordServiceImpl implements CRUD<PlaylistRecord, Long>{
	
	@Autowired
	PlaylistRecordDAO dao;

	@Override
	@Transactional
	public PlaylistRecord create(PlaylistRecord entity) {
		PlaylistRecord playlist = entity;
		if(playlist!=null) {
			return dao.save(playlist);
		}
		return null;
	}

	@Override
	@Transactional
	public PlaylistRecord update(PlaylistRecord entity) {
		PlaylistRecord playlist = entity;
		if(playlist!=null) {
			return dao.save(playlist);
		}
		return null;
	}

	@Override
	@Transactional
	public Boolean delete(Long key) {
		dao.deleteById(key);
		return true;
	}

	@Override
	public PlaylistRecord findOne(Long key) {
		return dao.findById(key).orElse(null);
	}

	@Override
	public List<PlaylistRecord> findAll() {
		return dao.findAll();
	}
	
	
}
