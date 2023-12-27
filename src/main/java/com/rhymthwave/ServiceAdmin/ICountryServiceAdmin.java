package com.rhymthwave.ServiceAdmin;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rhymthwave.entity.Country;

import jakarta.servlet.http.HttpServletRequest;

public interface ICountryServiceAdmin {

	Country findById(String idCountry);

	Country create(Country Country, HttpServletRequest request); 
	
	Country update(Country Country, HttpServletRequest request); 
	
	boolean delete(String idCountry );
	
	List<Country> findAllCountry();
	
	
	Page<Country> getCountryPage(Integer page, String sortBy, String sortField);

	
}
