package com.rhymthwave.Controller.Artist;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexArtist {
	
	@GetMapping("artist")
	public String layoutArtist() {
		return "Artist/ArtistControl";
	}
	
	@GetMapping("claim")
	public String claimArtist() {
		return "Artist/Information";
	}
	
	@GetMapping("artist/home")
	public String layoutArtistHome() {
		return "Artist/Artist";
	}
	
}
