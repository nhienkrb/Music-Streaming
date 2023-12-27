package com.rhymthwave.Service.Implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.PodcastDAO;
import com.rhymthwave.DTO.checkPodcastRole;
import com.rhymthwave.Request.DTO.Top3PodcastDTO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.PodcastService;
import com.rhymthwave.entity.Podcast;

import jakarta.transaction.Transactional;

@Service
public class PodcastServiceImpl implements PodcastService,CRUD<Podcast,Long>{

	@Autowired
	PodcastDAO dao;
	
	@Override
	@Transactional
	public Podcast create(Podcast entity) {
		if(entity!=null) {
			entity.setRate(0);
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	@Transactional
	public Podcast update(Podcast entity) {
		if(entity!=null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	@Transactional
	public Boolean delete(Long key) {
		if(key instanceof Long && key >= 0) {
			dao.deleteById(key);
			return true;
		}
		return false;
	}

	@Override
	public Podcast findOne(Long key) {
		return dao.findById(key).get();
	}

	@Override
	public List<Podcast> findAll() {
		return dao.findAll();
	}

	@Override
	public List<Podcast> findMyPodcast(String email) {
		return dao.findMyPobcast(email);
	}

	@Override
	public List<Podcast> top50NewPodcast(Optional<String> country) {
		return dao.top50NewPodcast(country.orElse("%%"));
	}

	@Override
	public List<Podcast> top50PodcastPopular(Optional<String> country) {
		return dao.top50PodcastPopular(country.orElse("%%"));
	}

	@Override
	public checkPodcastRole checkPocastRole(String email) {
		return dao.CheckPodcastRole(email);
	}

	@Override
	public List<Top3PodcastDTO> top3podcast() {
		return dao.top3Podcast();
	}
}
