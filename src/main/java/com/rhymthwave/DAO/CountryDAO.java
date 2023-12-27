package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Country;

@Repository
public interface CountryDAO extends JpaRepository<Country, String>{
	Country findByNameCountry(String nameCountry);
}
