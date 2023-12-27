package com.rhymthwave.API.GraphQL;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Artist;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AlbumByArtistGraphQL {

	private final CRUD<Artist, Long> crudArtist;

	@QueryMapping("findAlByArtist")
	public Artist findById(@Argument("idArist") Long id) {
		return crudArtist.findOne(id);
	}
}
