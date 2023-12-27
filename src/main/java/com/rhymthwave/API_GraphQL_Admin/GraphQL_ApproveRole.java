package com.rhymthwave.API_GraphQL_Admin;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.ServiceAdmin.IArtistService;
import com.rhymthwave.entity.Artist;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GraphQL_ApproveRole {

	private  final  IArtistService artistService;

	@QueryMapping("getAllIsVerifyArtist")
	public List<Artist> getIsVerityArtist() {
		return artistService.getIsVerityArtist();
	}
}
