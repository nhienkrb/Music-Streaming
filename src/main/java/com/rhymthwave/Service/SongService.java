package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.entity.Song;

public interface SongService {
	List<Song> findSongNotRecord(String email);
	
	List<Song> findSongReleasedByArtist(String artistId);
	
	List<Song> findByName(String keyword);
}
