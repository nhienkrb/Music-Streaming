package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Genre;

@Repository
public interface GenreDAO extends JpaRepository<Genre, Integer>{

}
