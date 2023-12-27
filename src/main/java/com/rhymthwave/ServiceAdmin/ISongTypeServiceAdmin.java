package com.rhymthwave.ServiceAdmin;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rhymthwave.entity.SongStyle;

import jakarta.servlet.http.HttpServletRequest;

public interface ISongTypeServiceAdmin {
	
	SongStyle findById(Integer idSongStyle);

	SongStyle create(SongStyle SongStyle, HttpServletRequest request); 
	
	SongStyle update(SongStyle SongStyle, HttpServletRequest request); 
	
	boolean delete(Integer idInstrument );
	
	List<SongStyle> findAllSongStyle();

	Page<SongStyle> getSongTypePage(Integer page, String sortBy, String sortField);

	
}
