package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.SongGenre;

@Repository
public interface SongGenreDAO extends JpaRepository<SongGenre, Long>{
	@Query("select o from SongGenre o where o.recording.recordingId = :id")
	List<SongGenre> findSongGenreByRecord(@Param("id") Long id);
}
