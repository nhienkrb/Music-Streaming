package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Culture;

@Repository
public interface CultureDAO extends JpaRepository<Culture, Integer>{

	Culture findByCultureName(String cultureName);
}
