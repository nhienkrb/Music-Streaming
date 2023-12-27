package com.rhymthwave.Service.Implement;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.PlaylistDAO;
import com.rhymthwave.DAO.PlaylistRecordDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.PlaylistService;
import com.rhymthwave.entity.Playlist;
import com.rhymthwave.entity.PlaylistRecord;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.UserType;

import jakarta.transaction.Transactional;

@Service
public class PlaylistServiceImpl implements PlaylistService, CRUD<Playlist, Long> {

	@Autowired
	PlaylistDAO dao;

	@Autowired
	PlaylistRecordDAO daoPR;

	@Override
	@Transactional
	public Playlist create(Playlist entity) {
		if (entity.getQuantity() > 0 || entity.getPlaylistName() != null) {
			return dao.save(entity);
		}
		Playlist playlist = entity;
		playlist.setQuantity(0);
		if (playlist.getPlaylistName() == null) {
			playlist.setPlaylistName("My Playlist");
		}
		return dao.save(playlist);
	}

	@Override
	@Transactional
	public Playlist update(Playlist entity) {
		Playlist playlist = entity;
		return dao.save(playlist);
	}

	@Override
	@Transactional
	public Boolean delete(Long key) {
		dao.deleteById(key);
		return true;
	}

	@Override
	public Playlist findOne(Long key) {
		return dao.findById(key).get();
	}

	@Override
	public List<Playlist> findAll() {
		return dao.findAll();
	}

	@Override
	public List<Playlist> findMyPlaylist(UserType usertype) {
		return dao.findByUsertype(usertype);
	}

	@Override
	@Transactional
	public Playlist createSimilarPlaylist(Playlist playlist, List<Recording> list) {
		for (Recording recording : list) {
			PlaylistRecord playlistRecord = new PlaylistRecord();
			playlistRecord.setPlaylist(playlist);
			playlistRecord.setRecording(recording);
			daoPR.save(playlistRecord);
		}
		return playlist;
	}

	@Override
	public List<Playlist> findPublicPlaylist(UserType userType, Boolean isPublic) {
		return dao.findByUsertypeAndIsPublic(userType, isPublic);
	}

	@Override
	public List<Playlist> findPlaylistFeaturingArtist(Long artistId, List<Integer> roleId) {
		return dao.findPlaylistFeaturingByArtist(artistId, roleId);
	}

	@Override
	public List<Playlist> findDiscoverArtist(Long artistId, List<Integer> roleId) {
		return dao.findPlaylistDiscoverByArtist(artistId, roleId);
	}

	@Override
	public List<Playlist> top50PlaylistLatest(Boolean isPublic) {
		return dao.top50PlaylistLatest(isPublic);
	}

	@Override
	public List<Playlist> top50PlaylistRecentListen(Boolean isPublic,
			Optional<List<String>> nameGenre, Optional<String> culture, Optional<String> instrument,
			Optional<String> mood, Optional<String> songstyle, Optional<String> versions) {
		return dao.top50PlaylistRecentListen(isPublic, nameGenre.orElse(Arrays.asList("%%")),
				culture.orElse("%%"), instrument.orElse("%%"), mood.orElse("%%"), songstyle.orElse("%%"),versions.orElse("%%"));
	}
}
