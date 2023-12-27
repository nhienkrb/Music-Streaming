package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.Request.DTO.Top10ArtistDTO;
import com.rhymthwave.entity.Artist;

public interface ArtistService {
	Artist findByEmail(String email);
	
	List<Artist> findIsVerify(Boolean verify);
	
	List<Artist> findAllArtistNameisVerify(Long id,String artistName);
	
	List<Object> findByName(String keyword);
	
	List<Artist> top50ArtistByListener(String country, Boolean active, Boolean verify);
	
	List<Artist> top50ArtistByFollow(Integer role, String country, Boolean verify);
	
	List<Top10ArtistDTO> top3ArtistByByListener();
}
