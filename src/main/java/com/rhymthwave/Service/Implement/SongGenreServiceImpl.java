package com.rhymthwave.Service.Implement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.SongGenreDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.SongGenreService;
import com.rhymthwave.entity.SongGenre;

import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SongGenreServiceImpl implements SongGenreService, CRUD<SongGenre, Long> {

	private final SongGenreDAO dao;

	@Override
	@Transactional
	public SongGenre create(SongGenre entity) {
		Optional<SongGenre> songGenre = Optional.ofNullable(entity);
		if (songGenre.isPresent()) {
			dao.save(entity);
		}
		return null;
	}

	@Override
	@Transactional
	public SongGenre update(SongGenre entity) {
		Optional<SongGenre> songGenre = Optional.ofNullable(entity);
		if (songGenre.isPresent()) {
			dao.save(entity);
		}
		return null;
	}

	@Override
	@Transactional
	public Boolean delete(@Nonnull Long key) {
		if (key instanceof Long && key > 0) {
			dao.deleteById(key);
			return true;
		}
		return false;
	}

	@Override
	public SongGenre findOne(@Nonnull Long key) {
		if (key instanceof Long && key > 0) {
			return dao.findById(key).get();
		}
		return null;
	}

	@Override
	public List<SongGenre> findAll() {
		return dao.findAll();
	}

	@Override
	public List<SongGenre> findListSongGenreByRecord(Long id) {
		return dao.findSongGenreByRecord(id);
	}

	
}
