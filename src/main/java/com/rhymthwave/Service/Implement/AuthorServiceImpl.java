package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.AuthorDAO;
import com.rhymthwave.Service.AuthorService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Author;

import jakarta.transaction.Transactional;

@Service
public class AuthorServiceImpl implements AuthorService, CRUD<Author, Long>{

	@Autowired
	AuthorDAO dao;
	
	@Override
	public Author findAuthor(Integer roleId, String email) {
		return dao.findAuthor(roleId, email);
	}

	@Override
	@Transactional
	public Author create(Author entity) {
		Author author =entity;
		return dao.save(author);
	}

	@Override
	@Transactional
	public Author update(Author entity) {
		Author author =entity;
		return dao.save(author);
	}

	@Override
	@Transactional
	public Boolean delete(Long key) {
		dao.deleteById(key);
		return true;
	}

	@Override
	public Author findOne(Long key) {
		return dao.findById(key).get();
	}

	@Override
	public List<Author> findAll() {
		return dao.findAll();
	}
	
	
}
