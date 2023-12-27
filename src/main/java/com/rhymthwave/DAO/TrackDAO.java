package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Track;

@Repository
public interface TrackDAO extends JpaRepository<Track, Integer>{
	
	@Query("Select o from Track o where album.albumId = :albumId")
	List<Track> getTrackByAlbum(@Param("albumId") Integer albumId);
}
