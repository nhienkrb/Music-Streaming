package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.RecordDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.RecordService;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.Song;

@Service
public class RecordServiceImpl implements RecordService, CRUD<Recording, Long>{

	@Autowired
	RecordDAO dao;
	
	@Override
	public Recording create(Recording entity) {
		if (entity != null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	public Recording update(Recording entity) {
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
	public Recording findOne(Long key) {
		if (key instanceof Long && key >= 0) {
			return dao.findById(key).get();
		}
		return null;
	}

	@Override
	public List<Recording> findAll() {
		return dao.findAll();
	}

	@Override
	public List<Recording> findListRecordNotRaw(String email) {
		return dao.getListRecordNotRaw(email);
	}

	@Override
	public List<Recording> findRawRecordByCreater(String email) {
		return dao.getListRawRecord(email);
	}

	@Override
	public List<Recording> findRecordBySong(Long songId) {
		List<Recording> listRecord = dao.getListRecordBySong(songId);
		return listRecord;
	}

	@Override
	public List<Recording> findRecordByCreater(String email) {
		List<Recording> listRecord = dao.getRecordByCreater(email);
		return listRecord;
	}

	@Override
	public List<Recording> findRecordDelete(String email) {
		List<Recording> listRecord = dao.getRecordDelete(email);
		return listRecord;
	}

	@Override
	public List<Recording> findListRecordRandom() {
		return dao.findListRandom();
	}

	@Override
	public List<Recording> findListRandomFavorite(String nameGenre, String culture, String instrument, String mood,
			String songstyle, String versions) {
		return dao.findListRandomFavorite(nameGenre, "%"+culture+"%", "%"+instrument+"%","%"+mood+"%", "%"+songstyle+"%", "%"+versions+"%");
	}
	
	@Override
	public List<Recording> findMyProject(Long artistid) {
		List<Recording> listRecord = dao.getMyProject(artistid);
		return listRecord;
	}

	@Override
	public List<Recording> findSongPl(String songName) {
		List<Recording> list = dao.findSongPl(songName);
		return list;
	}

	public List<Recording> findListPopularByArtist(Long artistId) {
		return dao.findListPopularByArtist(artistId);
	}

	@Override
	public List<Recording> findAppearOn(Long artistId) {
		return dao.findListAppearOn(artistId);
	}

	@Override
	public List<Recording> statisticsByDate(String email, Integer date) {
		return dao.statisticsByDate(email, date);
	}

	@Override
	public List<Recording> top50SongByAreaListened(String country, Boolean deleted) {
		return dao.top50SongByAreaListened(country,deleted);
	}

	@Override
	public List<Recording> top50SongByDate(String country, Boolean deleted) {
		return dao.top50SongByDate(country,deleted);
	}
}
