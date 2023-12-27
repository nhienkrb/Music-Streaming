package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.entity.Album;

public interface AlbumService {
	List<Album> findAlbumNotRecord(String email);
	
	List<Album> findAlbumReleasedByArtist(Long artistId);
	
	List<Album> findAlbumByArtist(Long artistId);
	
	List<Object> findByName(String keyword);
	
	List<Album> top50AlbumLatest();
	
	List<Album> top50AlbumListenest();
}
