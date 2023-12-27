package com.rhymthwave.API.GraphQL;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.MonitorEpisodeService;
import com.rhymthwave.entity.MonitorEpisode;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MonitorEpGraphQL {
	private final MonitorEpisodeService monitorEpSer;

	@QueryMapping("getNewListenedEpisode")
	public List<MonitorEpisode> getNewListened(@Argument("episodeId") Long episodeId,
			@Argument("duration") Integer duration) {
		return monitorEpSer.getNewListener(episodeId, duration);
	}
}
