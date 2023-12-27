package com.rhymthwave.API.GraphQL;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Album;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AlbumGraphQL {

	private final CRUD<Album, Long> crudAlbum;

	@QueryMapping("findAlbum")
	public Album findById(@Argument("id") Long id) {
		return crudAlbum.findOne(id);
	}
}
