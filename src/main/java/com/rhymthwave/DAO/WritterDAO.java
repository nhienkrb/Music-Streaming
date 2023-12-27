package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Writter;

@Repository
public interface WritterDAO extends JpaRepository<Writter, Long>{
	@Query("select o from Writter o where o.song.songId = :id")
	List<Writter> getFindWritterSong(@Param("id") Long id);
}
