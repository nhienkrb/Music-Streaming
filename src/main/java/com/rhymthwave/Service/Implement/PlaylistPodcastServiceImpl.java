package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.PlaylistPodcastDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Playlist_Podcast;

import jakarta.transaction.Transactional;

@Service
public class PlaylistPodcastServiceImpl implements CRUD<Playlist_Podcast, Long>{
	
	@Autowired
	PlaylistPodcastDAO dao;
	
	@Override
	@Transactional
	public Playlist_Podcast create(Playlist_Podcast entity) {
		Playlist_Podcast playlistPodcast = entity;
		if(playlistPodcast!=null) {
			return dao.save(playlistPodcast);
		}
		return null;
	}

	@Override
	@Transactional
	public Playlist_Podcast update(Playlist_Podcast entity) {
		Playlist_Podcast playlistPodcast = entity;
		if(playlistPodcast!=null) {
			return dao.save(playlistPodcast);
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
	public Playlist_Podcast findOne(Long key) {
		return dao.findById(key).get();
	}

	@Override
	public List<Playlist_Podcast> findAll() {
		return dao.findAll();
	}
	
}
