package com.rhymthwave.Service.Implement;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.EpisodeDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.EpisodeService;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Image;

import jakarta.transaction.Transactional;

@Service
public class EpisodeServiceImpl implements EpisodeService, CRUD<Episode, Long>{

	@Autowired
	EpisodeDAO dao;
	
	@Override
	@Transactional
	public Episode create(Episode entity) {
		if(entity!=null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	@Transactional
	public Episode update(Episode entity) {
		if(entity!=null) {
			dao.save(entity);
			return entity;
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
	public Episode findOne(Long key) {
		if(key == null) {
			return null;
		}
		return dao.findById(key).get();
	}

	@Override
	public List<Episode> findAll() {
		return dao.findAll();
	}

	@Override
	public Episode snapEpisode(Episode episode, Map<?, ?> recordAudio, Image coverImg) {
		String url = (String) recordAudio.get("url");
		String publicID = (String) recordAudio.get("public_id");
		episode.setFileUrl(url);
		episode.setPublicIdFile(publicID);
		episode.setImage(coverImg);
		return episode;
	}

	@Override
	public List<Episode> findAllEpisodeByPodcast(Long podcastId, Boolean status) {
		return dao.findAllEpisodeByPodcast(podcastId,status);
	}

	@Override
	public Episode findLatestEpisodeByPodcast(Long podcastId) {
		return dao.findLatestByPodcast(podcastId);
	}

	@Override
	public List<Episode> findByName(String keyword) {
		return dao.findByName(keyword);
	}

	@Override
	public List<Episode> top50EpForYou(Boolean ispublic, Optional<List<Integer>> tags) {
		return dao.top50EpForYou(ispublic, tags.orElse(Arrays.asList(1)));
	}	
}
