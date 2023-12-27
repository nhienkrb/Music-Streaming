package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.TrackDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.TrackService;
import com.rhymthwave.entity.Track;

import jakarta.transaction.Transactional;

@Service
public class TrackServiceImpl implements TrackService,CRUD<Track, Integer>{

	@Autowired
	TrackDAO dao;
	
	@Override
	@Transactional
	public Track create(Track entity) {
		if(entity !=null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	@Transactional
	public Track update(Track entity) {
		if(entity !=null) {
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
	public Track findOne(Integer key) {
		if(key instanceof Integer && key>0) {
			return dao.findById(key).get();
		}
		return null;
	}

	@Override
	public List<Track> findAll() {
		return dao.findAll();
	}

	@Override
	public List<Track> getTrackByAlbum(Integer albumId) {
		return dao.getTrackByAlbum(albumId);
	}
	
	
	
}
