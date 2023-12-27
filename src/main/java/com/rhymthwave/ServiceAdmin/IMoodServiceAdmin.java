package com.rhymthwave.ServiceAdmin;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rhymthwave.entity.Mood;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.servlet.http.HttpServletRequest;

public interface IMoodServiceAdmin {
	
	Mood findById(Integer idMood);

	Mood create(Mood mood, HttpServletRequest request); 
	
	Mood update(Mood mood, HttpServletRequest request); 
	
	boolean delete(Integer idMood );
	
	Page<Mood> getMoodPage(Integer page, String sortBy, String sortField);

	List<Mood> findAllMood();
}
