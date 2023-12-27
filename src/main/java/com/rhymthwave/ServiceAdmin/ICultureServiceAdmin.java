package com.rhymthwave.ServiceAdmin;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rhymthwave.entity.Culture;
import com.rhymthwave.entity.Mood;

import jakarta.servlet.http.HttpServletRequest;

public interface ICultureServiceAdmin {

	Culture findById(Integer idCulture);

	Culture create(Culture Culture, HttpServletRequest request); 
	
	Culture update(Culture Culture, HttpServletRequest request); 
	
	boolean delete(Integer idCulture );
	
	List<Culture> findAllCulture();
	
	Page<Culture> getCulturePage(Integer page, String sortBy, String sortField);

	
}
