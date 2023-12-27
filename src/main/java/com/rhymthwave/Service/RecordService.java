package com.rhymthwave.Service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.Song;

public interface RecordService {
	List<Recording> findListRecordNotRaw(String email);
	
	List<Recording> findRecordByCreater(String email);
	
	List<Recording> findRawRecordByCreater(String email);
	
	List<Recording> findRecordBySong(Long songId);
	
	List<Recording> findRecordDelete(String email);
	
	List<Recording> findListRecordRandom();
	
	List<Recording> findListRandomFavorite(String nameGenre, String culture, String instrument, String mood, String songstyle, String versions);

	List<Recording> findMyProject(Long artistId);
	
	List<Recording> findSongPl(String songName);
	
	List<Recording> findListPopularByArtist(Long artistId);
	
	List<Recording> findAppearOn(Long artistId);
	
	List<Recording> statisticsByDate(String email, Integer date);
	
	List<Recording> top50SongByAreaListened(String country,Boolean deleted);
	
	List<Recording> top50SongByDate(String country,Boolean deleted);
}
