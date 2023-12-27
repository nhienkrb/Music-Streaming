package com.rhymthwave.API_GraphQL_Admin;

import com.rhymthwave.DAO.EpisodeDAO;
import com.rhymthwave.DAO.RecordDAO;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Recording;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GraphQL_Statistics {

    private final RecordDAO recordDAO;
    private final EpisodeDAO episodeDAO;
    @QueryMapping("getAllRecordTop100")
    public List<Recording> getAllRecordTop100() {
        return recordDAO.findTop100ByOrderByListenedDesc();
    }

    @QueryMapping("getAllEpisodeTop100")
    public List<Episode> getAllEpisodeTop100() {
        return episodeDAO.findTop100ByOrderByListenedDesc();
    }

    @QueryMapping("top10Trending")
    public List<Recording> top10Trending( @Argument("day") int day) {
        return recordDAO.findTop10Trending(day);
    }
}
