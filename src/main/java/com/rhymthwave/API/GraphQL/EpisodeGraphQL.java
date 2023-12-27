package com.rhymthwave.API.GraphQL;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Episode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Controller
@Slf4j
@RequiredArgsConstructor
public class EpisodeGraphQL {
	private final CRUD<Episode, Long> crudEpisode;
	
	@QueryMapping("episodeById")
	public Episode findOne(@Argument("episodeId") Long id) {
		return crudEpisode.findOne(id);
	}
}
