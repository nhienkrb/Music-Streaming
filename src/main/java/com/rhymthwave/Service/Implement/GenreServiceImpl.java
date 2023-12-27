package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.GenreDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.ServiceAdmin.IGenreService;
import com.rhymthwave.Utilities.SortBy;
import com.rhymthwave.entity.Genre;
import com.rhymthwave.entity.Mood;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements CRUD<Genre, Integer>, IGenreService{
	
	private final GenreDAO dao;

	private final SortBy< String, String> sortService;
	
	@Override
	@Transactional
	public Genre create(Genre entity) {
		if(entity!=null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	@Transactional
	public Genre update(Genre entity) {
		if(entity!=null) {
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
	public Genre findOne(Integer key) {
		if(key instanceof Integer && key>0) {
			return dao.findById(key).get();
		}
		return null;
	}

	@Override
	public List<Genre> findAll() {
		return dao.findAll();
	}

	@Override
	public Page<Genre> getGenrePage(Integer page, String sortBy, String sortField) {

		try {
			Sort sort = sortService.sortBy(sortBy, sortField);

			Pageable pageable = PageRequest.of(page, 6, sort);

			Page<Genre> pageMood = dao.findAll(pageable);
			return pageMood;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Genre> findAllGenre() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}
	
}
