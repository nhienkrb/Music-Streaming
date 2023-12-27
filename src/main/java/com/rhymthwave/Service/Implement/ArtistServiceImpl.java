package com.rhymthwave.Service.Implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.ArtistDAO;
import com.rhymthwave.Request.DTO.Top10ArtistDTO;
import com.rhymthwave.Service.ArtistService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Artist;

import jakarta.transaction.Transactional;

@Service
public class ArtistServiceImpl implements ArtistService, CRUD<Artist, Long> {
	@Autowired
	ArtistDAO dao;

	@Override
	@Transactional
	public Artist create(Artist entity) {
		entity.setActive(true);
		entity.setIsVerify(false);
		return dao.save(entity);
	}

	@Override
	@Transactional
	public Artist update(Artist entity) {
		return dao.save(entity);
	}

	@Override
	@Transactional
	public Boolean delete(Long key) {
		dao.deleteById(key);
		return true;
	}

	@Override
	public Artist findOne(Long key) {

		Optional<Artist> artist = dao.findById(key);

		if (artist.isEmpty()) {
			return null;
		}

		return artist.get();
	}

	@Override
	public List<Artist> findAll() {
		return dao.findAll();
	}

	@Override
	public Artist findByEmail(String email) {
		return dao.findByEmail(email);
	}

	@Override
	public List<Artist> findIsVerify(Boolean verify) {
		return dao.findAllIsVerify(verify);
	}

	@Override
	public List<Artist> findAllArtistNameisVerify(Long id, String artistName) {
		return dao.findAllArtistVerify(id, artistName);
	}

	@Override
	public List<Object> findByName(String keyword) {
		return dao.findByName(keyword);
	}

	@Override
	public List<Artist> top50ArtistByListener(String country, Boolean active, Boolean verify) {
		return dao.top50ArtistByListener(country, active, verify);
	}

	@Override
	public List<Artist> top50ArtistByFollow(Integer role, String country, Boolean verify) {
		return dao.top50ArtistByFollow(role, country, verify);
	}

	@Override
	public List<Top10ArtistDTO> top3ArtistByByListener() {
		return dao.top3ArtistByListened();
	}

}
