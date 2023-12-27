package com.rhymthwave.API.GraphQL;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.PodcastService;
import com.rhymthwave.entity.Podcast;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PodcastGraphQL {
	private final PodcastService podcastSer;
	
	@QueryMapping("findPodcastByEmail")
	public List<Podcast> findPodcastByEmail(@Argument("email") String email) {
		return podcastSer.findMyPodcast(email);
	}
}
