package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.SongStyle;

@Repository
public interface SongStyleDAO extends JpaRepository<SongStyle, Integer>{

  SongStyle findBySongStyleName(String songStyleName);
}
