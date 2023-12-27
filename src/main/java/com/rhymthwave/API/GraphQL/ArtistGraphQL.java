package com.rhymthwave.API.GraphQL;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.ArtistService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Artist;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ArtistGraphQL {
	
	private final CRUD<Artist, Long> crudArtist;
	
	private final ArtistService artistSer;
	
	@QueryMapping("artistById")
	public Artist findById(@Argument("artistId") Long id) {
		return crudArtist.findOne(id);
	}
	
	@QueryMapping("top50ArtistByListener")
	public List<Artist> top50ArtistByListener(@Argument("country") String country) {
		return artistSer.top50ArtistByListener(country,true,true);
	}
	
	@QueryMapping("top50ArtistByFollow")
	public List<Artist> top50ArtistByFollow(@Argument("country") String country) {
		return artistSer.top50ArtistByFollow(2,country,true);
	}
}
