package com.rhymthwave.API_GraphQL_Admin;

import com.rhymthwave.DAO.RecordDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.ServiceAdmin.IPlayListServiceAdmin;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Playlist;
import com.rhymthwave.entity.Recording;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GraphQL_Playlist {

    private final CRUD<Recording, Long> recordingService;
    private final CRUD<Episode, Long> episodeService;
    private final IPlayListServiceAdmin playList;

    @QueryMapping("getAllPlaylist")
    public List<Playlist> getAllPlaylist() {
        return playList.getAllSongsPlaylist();
    }

    @QueryMapping("getAllPodcastPlaylist")
    public List<Playlist> getAllPodcastPlaylist() {
        return playList.getAllPodcastPlaylist();
    }

    @QueryMapping("getPlayListById")
    public Playlist getPlayListById(@Argument("idPlaylist") Long id) {
        return playList.findById(id);
    }

    @QueryMapping("getAllEpisode")
    public List<Episode> getAllEpisode() {
        return episodeService.findAll();
    }

    @QueryMapping("getAllRecord")
    public List<Recording> getAllRecord() {
        return recordingService.findAll();
    }
}
