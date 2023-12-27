package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.PlaylistRecord;

@Repository
public interface PlaylistRecordDAO extends JpaRepository<PlaylistRecord, Long>{

}
