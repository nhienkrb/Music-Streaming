package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.DAO.SongDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.SongService;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Song;

@Service
public class SongServiceImpl implements SongService, CRUD<Song, Long> {
	@Autowired
	SongDAO dao;
	
	@Autowired
	AccountDAO accDao;

	@Override
	public Song create(Song entity) {
		if (entity != null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	public Song update(Song entity) {
		if (entity != null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	public Boolean delete(Long key) {
		if (key instanceof Long && key >= 0) {
			dao.deleteById(key);
			return true;
		}
		return false;
	}

	@Override
	public Song findOne(Long key) {
		if (key instanceof Long && key >= 0) {
			return dao.findById(key).get();
		}
		return null;
	}

	@Override
	public List<Song> findAll() {
		return dao.findAll();
	}

	@Override
	public List<Song> findSongNotRecord(String email) {
		Account account = accDao.findById(email).get();
		return dao.getSongNotRecord(account.getArtist().getArtistId());
	}

	@Override
	public List<Song> findSongReleasedByArtist(String emailCreate) {
		List<Song> listSong = dao.getListSongReleasedByArtist(emailCreate);
		return listSong;
	}

	@Override
	public List<Song> findByName(String keyword) {
		return dao.findByName(keyword);
	}
}
