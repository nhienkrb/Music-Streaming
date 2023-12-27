package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.entity.SongGenre;

public interface SongGenreService {
	List<SongGenre> findListSongGenreByRecord(Long id);
}
